// port-lint: source core/src/turn_diff_tracker.rs
package ai.solace.coder.core.session

import ai.solace.coder.protocol.FileChange
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val ZERO_OID = "0000000000000000000000000000000000000000"
private const val DEV_NULL = "/dev/null"

/**
 * Git file mode representation.
 *
 * Ported from Rust codex-rs/core/src/turn_diff_tracker.rs FileMode
 */
enum class FileMode(private val modeStr: String) {
    Regular("100644"),
    Executable("100755"),
    Symlink("120000");

    override fun toString(): String = modeStr
}

/**
 * Snapshot of a file's state at the time it was first seen by the tracker.
 *
 * Ported from Rust codex-rs/core/src/turn_diff_tracker.rs BaselineFileInfo
 */
private data class BaselineFileInfo(
    val path: String,
    val content: ByteArray,
    val mode: FileMode,
    val oid: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaselineFileInfo) return false
        return path == other.path &&
            content.contentEquals(other.content) &&
            mode == other.mode &&
            oid == other.oid
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + content.contentHashCode()
        result = 31 * result + mode.hashCode()
        result = 31 * result + oid.hashCode()
        return result
    }
}

/**
 * Tracks sets of changes to files and exposes the overall unified diff.
 *
 * Internally:
 * 1. Maintains an in-memory baseline snapshot of files when they are first seen.
 *    For new additions, baseline is empty with zero OID so diffs show as proper additions.
 * 2. Keeps a stable internal filename (UUID) per external path for rename tracking.
 * 3. To compute the aggregated unified diff, compares each baseline snapshot to the
 *    current file on disk entirely in-memory and emits unified diffs.
 *
 * Ported from Rust codex-rs/core/src/turn_diff_tracker.rs TurnDiffTracker
 */
@OptIn(ExperimentalUuidApi::class)
class TurnDiffTracker {
    /** Map external path -> internal filename (UUID). */
    private val externalToTempName = mutableMapOf<String, String>()

    /** Internal filename -> baseline file info. */
    private val baselineFileInfo = mutableMapOf<String, BaselineFileInfo>()

    /** Internal filename -> external path as of current accumulated state (after applying all changes). */
    private val tempNameToCurrentPath = mutableMapOf<String, String>()

    /** Cache of known git worktree roots to avoid repeated filesystem walks. */
    private val gitRootCache = mutableListOf<String>()

    /**
     * Front-run apply patch calls to track the starting contents of any modified files.
     *
     * - Creates an in-memory baseline snapshot for files that already exist on disk when first seen.
     * - For files that don't exist, creates a baseline with empty content and zero OID.
     * - Also updates internal mappings for move/rename events.
     *
     * Ported from Rust TurnDiffTracker::on_patch_begin
     */
    fun onPatchBegin(changes: Map<String, FileChange>) {
        val fs = FileSystem.SYSTEM

        for ((pathStr, change) in changes) {
            // Ensure a stable internal filename exists for this external path.
            if (pathStr !in externalToTempName) {
                val internal = Uuid.random().toString()
                externalToTempName[pathStr] = internal
                tempNameToCurrentPath[internal] = pathStr

                val path = pathStr.toPath()
                val baseline = if (fs.exists(path)) {
                    val mode = fileModeForPath(path)
                    val content = blobBytes(path, mode) ?: byteArrayOf()
                    val oid = if (mode == FileMode.Symlink) {
                        gitBlobSha1Hex(content)
                    } else {
                        gitBlobOidForPath(pathStr) ?: gitBlobSha1Hex(content)
                    }
                    BaselineFileInfo(
                        path = pathStr,
                        content = content,
                        mode = mode,
                        oid = oid
                    )
                } else {
                    BaselineFileInfo(
                        path = pathStr,
                        content = byteArrayOf(),
                        mode = FileMode.Regular,
                        oid = ZERO_OID
                    )
                }

                baselineFileInfo[internal] = baseline
            }

            // Track rename/move in current mapping if provided in an Update.
            if (change is FileChange.Update && change.movePath != null) {
                val dest = change.movePath
                val uuidFilename = externalToTempName[pathStr] ?: run {
                    // Rare path: source wasn't mapped yet, create with no baseline.
                    val i = Uuid.random().toString()
                    baselineFileInfo[i] = BaselineFileInfo(
                        path = pathStr,
                        content = byteArrayOf(),
                        mode = FileMode.Regular,
                        oid = ZERO_OID
                    )
                    i
                }
                // Update current external mapping for temp file name.
                tempNameToCurrentPath[uuidFilename] = dest
                // Update forward mapping: external current -> internal name.
                externalToTempName.remove(pathStr)
                externalToTempName[dest] = uuidFilename
            }
        }
    }

    private fun getPathForInternal(internal: String): String? {
        return tempNameToCurrentPath[internal]
            ?: baselineFileInfo[internal]?.path
    }

    /**
     * Find the git worktree root for a file/directory by walking up to the first
     * ancestor containing a `.git` entry. Uses a simple cache of known roots.
     *
     * Ported from Rust TurnDiffTracker::find_git_root_cached
     */
    private fun findGitRootCached(start: String): String? {
        val fs = FileSystem.SYSTEM
        val startPath = start.toPath()
        val dir = if (isDirectory(startPath)) startPath else startPath.parent ?: return null

        // Fast path: if any cached root is an ancestor of this path.
        val dirStr = dir.toString()
        gitRootCache.firstOrNull { dirStr.startsWith(it) }?.let { return it }

        // Walk up to find a `.git` marker.
        var cur = dir
        while (true) {
            val gitMarker = cur / ".git"
            if (fs.exists(gitMarker)) {
                val curStr = cur.toString()
                if (curStr !in gitRootCache) {
                    gitRootCache.add(curStr)
                }
                return curStr
            }

            cur = cur.parent ?: return null
        }
    }

    /**
     * Return a display string for `path` relative to its git root if found, else absolute.
     *
     * Ported from Rust TurnDiffTracker::relative_to_git_root_str
     */
    private fun relativeToGitRootStr(pathStr: String): String {
        val root = findGitRootCached(pathStr)
        val s = if (root != null && pathStr.startsWith(root)) {
            val rel = pathStr.removePrefix(root).trimStart('/')
            rel.ifEmpty { pathStr }
        } else {
            pathStr
        }
        return s.replace('\\', '/')
    }

    /**
     * Compute the git blob OID for the file at `path`.
     *
     * In the Rust implementation, this shells out to `git hash-object`. Here we
     * compute the SHA-1 directly, which produces the identical result (both compute
     * SHA-1 of "blob <len>\0<data>"). This avoids needing platform-specific process
     * execution in commonMain.
     *
     * Ported from Rust TurnDiffTracker::git_blob_oid_for_path
     */
    private fun gitBlobOidForPath(pathStr: String): String? {
        val path = pathStr.toPath()
        val fs = FileSystem.SYSTEM
        return try {
            if (!fs.exists(path)) return null
            val content = fs.read(path) { readByteArray() }
            gitBlobSha1Hex(content)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Recompute the aggregated unified diff by comparing all of the in-memory snapshots
     * that were collected before the first time they were touched by apply_patch during
     * this turn with the current repo state.
     *
     * Ported from Rust TurnDiffTracker::get_unified_diff
     */
    fun getUnifiedDiff(): String? {
        val aggregated = StringBuilder()

        // Compute diffs per tracked internal file in a stable order by external path.
        val baselineFileNames = baselineFileInfo.keys.toMutableList()
        // Sort lexicographically by full repo-relative path to match git behavior.
        baselineFileNames.sortBy { internal ->
            getPathForInternal(internal)?.let { relativeToGitRootStr(it) } ?: ""
        }

        for (internal in baselineFileNames) {
            val fileDiff = getFileDiff(internal)
            aggregated.append(fileDiff)
            if (aggregated.isNotEmpty() && !aggregated.endsWith('\n')) {
                aggregated.append('\n')
            }
        }

        val result = aggregated.toString()
        return if (result.isBlank()) null else result
    }

    /**
     * Compute the diff for a single internally tracked file.
     *
     * Ported from Rust TurnDiffTracker::get_file_diff
     */
    private fun getFileDiff(internalFileName: String): String {
        val aggregated = StringBuilder()

        // Snapshot lightweight fields from baseline.
        val baselineInfo = baselineFileInfo[internalFileName]
        val baselineExternalPath = baselineInfo?.path ?: ""
        val baselineMode = baselineInfo?.mode ?: FileMode.Regular
        val leftOid = baselineInfo?.oid ?: ZERO_OID

        val currentExternalPath = getPathForInternal(internalFileName) ?: return ""

        val currentPath = currentExternalPath.toPath()
        val currentMode = fileModeForPath(currentPath)
        val rightBytes = blobBytes(currentPath, currentMode)

        val leftDisplay = relativeToGitRootStr(baselineExternalPath)
        val rightDisplay = relativeToGitRootStr(currentExternalPath)

        // Compute right OID.
        val rightOid = if (rightBytes != null) {
            if (currentMode == FileMode.Symlink) {
                gitBlobSha1Hex(rightBytes)
            } else {
                gitBlobOidForPath(currentExternalPath) ?: gitBlobSha1Hex(rightBytes)
            }
        } else {
            ZERO_OID
        }

        // Get baseline content.
        val leftPresent = leftOid != ZERO_OID
        val leftBytes: ByteArray? = if (leftPresent) baselineInfo?.content else null

        // Fast path: identical bytes or both missing.
        if (leftBytes.contentEqualsNullable(rightBytes)) {
            return ""
        }

        aggregated.append("diff --git a/$leftDisplay b/$rightDisplay\n")

        val isAdd = !leftPresent && rightBytes != null
        val isDelete = leftPresent && rightBytes == null

        when {
            isAdd -> aggregated.append("new file mode $currentMode\n")
            isDelete -> aggregated.append("deleted file mode $baselineMode\n")
            baselineMode != currentMode -> {
                aggregated.append("old mode $baselineMode\n")
                aggregated.append("new mode $currentMode\n")
            }
        }

        val leftText = leftBytes?.toUtf8OrNull()
        val rightText = rightBytes?.toUtf8OrNull()

        val canTextDiff = when {
            leftText != null && rightText != null -> true
            isAdd && rightText != null -> true
            isDelete && leftText != null -> true
            else -> false
        }

        if (canTextDiff) {
            val l = leftText ?: ""
            val r = rightText ?: ""

            aggregated.append("index $leftOid..$rightOid\n")

            val oldHeader = if (leftPresent) "a/$leftDisplay" else DEV_NULL
            val newHeader = if (rightBytes != null) "b/$rightDisplay" else DEV_NULL

            val unified = computeUnifiedDiff(l, r, oldHeader, newHeader)
            aggregated.append(unified)
        } else {
            aggregated.append("index $leftOid..$rightOid\n")
            val oldHeader = if (leftPresent) "a/$leftDisplay" else DEV_NULL
            val newHeader = if (rightBytes != null) "b/$rightDisplay" else DEV_NULL
            aggregated.append("--- $oldHeader\n")
            aggregated.append("+++ $newHeader\n")
            aggregated.append("Binary files differ\n")
        }

        return aggregated.toString()
    }

    /**
     * Check whether the tracker currently has any tracked baselines (i.e., any files
     * have been seen via [onPatchBegin]).
     */
    fun hasChanges(): Boolean = baselineFileInfo.isNotEmpty()

    /**
     * Clear all tracked state, resetting the tracker to its initial empty state.
     */
    fun clear() {
        externalToTempName.clear()
        baselineFileInfo.clear()
        tempNameToCurrentPath.clear()
        gitRootCache.clear()
    }

    /**
     * Get a list of changed files with their change types.
     */
    fun getChangedFiles(): List<TrackedChangedFile> {
        val fs = FileSystem.SYSTEM
        val result = mutableListOf<TrackedChangedFile>()

        for ((internal, baseline) in baselineFileInfo) {
            val currentPath = getPathForInternal(internal) ?: continue
            val currentExists = fs.exists(currentPath.toPath())
            val leftPresent = baseline.oid != ZERO_OID

            val changeType = when {
                !leftPresent && currentExists -> TrackedChangeType.Added
                leftPresent && !currentExists -> TrackedChangeType.Deleted
                baseline.path != currentPath -> TrackedChangeType.Renamed
                else -> TrackedChangeType.Modified
            }

            // Check if actually changed.
            if (changeType == TrackedChangeType.Modified) {
                val currentBytes = blobBytes(currentPath.toPath(), fileModeForPath(currentPath.toPath()))
                if (baseline.content.contentEquals(currentBytes ?: byteArrayOf())) {
                    continue // No actual change
                }
            }

            result.add(
                TrackedChangedFile(
                    path = currentPath,
                    originalPath = if (baseline.path != currentPath) baseline.path else null,
                    changeType = changeType
                )
            )
        }

        return result.sortedBy { it.path }
    }
}

/**
 * Information about a changed file, used for reporting.
 */
data class TrackedChangedFile(
    val path: String,
    val originalPath: String?,
    val changeType: TrackedChangeType
)

/**
 * Type of file change.
 */
enum class TrackedChangeType {
    Added,
    Modified,
    Deleted,
    Renamed
}

// =============================================================================
// SHA-1 Implementation (pure Kotlin, no platform dependencies)
// =============================================================================

/**
 * Compute the Git SHA-1 blob object ID for the given content bytes.
 * Git blob hash is SHA-1 of: "blob <len>\0<data>"
 *
 * Ported from Rust turn_diff_tracker::git_blob_sha1_hex_bytes
 */
internal fun gitBlobSha1Hex(data: ByteArray): String {
    val header = "blob ${data.size}\u0000"
    val headerBytes = header.encodeToByteArray()
    val combined = ByteArray(headerBytes.size + data.size)
    headerBytes.copyInto(combined, 0)
    data.copyInto(combined, headerBytes.size)
    return sha1Hex(combined)
}

/**
 * Pure-Kotlin SHA-1 implementation.
 *
 * Produces a 40-character lowercase hex string.
 */
private fun sha1Hex(input: ByteArray): String {
    val digest = Sha1Digest()
    digest.update(input)
    return digest.finalize().toHexString()
}

/**
 * Minimal SHA-1 message digest, following FIPS 180-4.
 */
private class Sha1Digest {
    private var h0 = 0x67452301u
    private var h1 = 0xEFCDAB89u
    private var h2 = 0x98BADCFEu
    private var h3 = 0x10325476u
    private var h4 = 0xC3D2E1F0u
    private var totalBytes = 0L
    private val buffer = ByteArray(64)
    private var bufferLen = 0

    fun update(data: ByteArray) {
        var offset = 0
        var remaining = data.size
        totalBytes += remaining

        // Fill buffer and process full blocks.
        if (bufferLen > 0) {
            val fill = minOf(64 - bufferLen, remaining)
            data.copyInto(buffer, bufferLen, offset, offset + fill)
            bufferLen += fill
            offset += fill
            remaining -= fill
            if (bufferLen == 64) {
                processBlock(buffer, 0)
                bufferLen = 0
            }
        }

        while (remaining >= 64) {
            processBlock(data, offset)
            offset += 64
            remaining -= 64
        }

        if (remaining > 0) {
            data.copyInto(buffer, 0, offset, offset + remaining)
            bufferLen = remaining
        }
    }

    fun finalize(): ByteArray {
        val totalBits = totalBytes * 8

        // Append 0x80
        val pad = ByteArray(1) { 0x80.toByte() }
        update(pad)

        // Pad to 56 mod 64
        while (bufferLen != 56) {
            update(byteArrayOf(0))
        }

        // Append total length in bits as 64-bit big-endian
        val lenBytes = ByteArray(8)
        for (i in 0..7) {
            lenBytes[i] = ((totalBits ushr (56 - i * 8)) and 0xFF).toByte()
        }
        // Must not add to totalBytes for the length field, so process directly
        lenBytes.copyInto(buffer, bufferLen)
        bufferLen += 8
        processBlock(buffer, 0)

        // Produce 20-byte digest
        val result = ByteArray(20)
        h0.putBigEndian(result, 0)
        h1.putBigEndian(result, 4)
        h2.putBigEndian(result, 8)
        h3.putBigEndian(result, 12)
        h4.putBigEndian(result, 16)
        return result
    }

    private fun processBlock(data: ByteArray, offset: Int) {
        val w = UIntArray(80)

        for (i in 0..15) {
            val base = offset + i * 4
            w[i] = ((data[base].toInt() and 0xFF).toUInt() shl 24) or
                ((data[base + 1].toInt() and 0xFF).toUInt() shl 16) or
                ((data[base + 2].toInt() and 0xFF).toUInt() shl 8) or
                (data[base + 3].toInt() and 0xFF).toUInt()
        }

        for (i in 16..79) {
            w[i] = (w[i - 3] xor w[i - 8] xor w[i - 14] xor w[i - 16]).rotateLeft(1)
        }

        var a = h0
        var b = h1
        var c = h2
        var d = h3
        var e = h4

        for (i in 0..79) {
            val (f, k) = when (i) {
                in 0..19 -> ((b and c) or (b.inv() and d)) to 0x5A827999u
                in 20..39 -> (b xor c xor d) to 0x6ED9EBA1u
                in 40..59 -> ((b and c) or (b and d) or (c and d)) to 0x8F1BBCDCu
                else -> (b xor c xor d) to 0xCA62C1D6u
            }

            val temp = a.rotateLeft(5) + f + e + k + w[i]
            e = d
            d = c
            c = b.rotateLeft(30)
            b = a
            a = temp
        }

        h0 += a
        h1 += b
        h2 += c
        h3 += d
        h4 += e
    }

    private fun UInt.putBigEndian(dest: ByteArray, offset: Int) {
        dest[offset] = (this shr 24).toByte()
        dest[offset + 1] = (this shr 16).toByte()
        dest[offset + 2] = (this shr 8).toByte()
        dest[offset + 3] = this.toByte()
    }
}

private fun ByteArray.toHexString(): String {
    return buildString(size * 2) {
        for (b in this@toHexString) {
            val v = b.toInt() and 0xFF
            append("0123456789abcdef"[v shr 4])
            append("0123456789abcdef"[v and 0x0F])
        }
    }
}

// =============================================================================
// Unified Diff Generation
// =============================================================================

/**
 * Compute a unified diff between two strings with 3 lines of context.
 * Produces output compatible with git's unified diff format.
 *
 * This implements the Myers diff algorithm (linear space variant) and
 * formats hunks with the standard @@ -a,b +c,d @@ markers.
 */
internal fun computeUnifiedDiff(
    oldText: String,
    newText: String,
    oldHeader: String,
    newHeader: String,
    contextLines: Int = 3
): String {
    val oldLines = splitLinesKeepEndings(oldText)
    val newLines = splitLinesKeepEndings(newText)

    val editScript = myersDiff(oldLines, newLines)

    if (editScript.all { it.type == EditType.Equal }) {
        return "" // No differences
    }

    val hunks = buildHunks(editScript, contextLines)

    val sb = StringBuilder()
    sb.append("--- $oldHeader\n")
    sb.append("+++ $newHeader\n")

    for (hunk in hunks) {
        sb.append(formatHunkHeader(hunk))
        for (edit in hunk.edits) {
            when (edit.type) {
                EditType.Equal -> {
                    sb.append(' ')
                    sb.append(edit.line)
                    if (!edit.line.endsWith('\n')) sb.append('\n')
                }
                EditType.Delete -> {
                    sb.append('-')
                    sb.append(edit.line)
                    if (!edit.line.endsWith('\n')) sb.append('\n')
                }
                EditType.Insert -> {
                    sb.append('+')
                    sb.append(edit.line)
                    if (!edit.line.endsWith('\n')) sb.append('\n')
                }
            }
        }
    }

    return sb.toString()
}

private enum class EditType { Equal, Delete, Insert }

private data class Edit(val type: EditType, val line: String)

private data class Hunk(
    val oldStart: Int,
    val oldCount: Int,
    val newStart: Int,
    val newCount: Int,
    val edits: List<Edit>
)

/**
 * Split text into lines, stripping trailing newline characters from each line
 * to produce clean comparison units. Returns lines without newlines for diffing,
 * matching the behavior of the `similar` crate's TextDiff::from_lines.
 */
private fun splitLinesKeepEndings(text: String): List<String> {
    if (text.isEmpty()) return emptyList()
    // Split on newline boundaries. Each resulting line does NOT contain the newline
    // because that is how `similar::TextDiff::from_lines` works -- it strips the
    // newline from each line for comparison purposes.
    val result = mutableListOf<String>()
    var start = 0
    while (start < text.length) {
        val newlineIdx = text.indexOf('\n', start)
        if (newlineIdx == -1) {
            result.add(text.substring(start))
            break
        } else {
            result.add(text.substring(start, newlineIdx))
            start = newlineIdx + 1
        }
    }
    // If the text ends with a newline, the split above does NOT produce a trailing
    // empty element, which matches the Rust `similar` behavior.
    return result
}

/**
 * Myers diff algorithm producing an edit script.
 *
 * Compares [oldLines] and [newLines] and produces a list of [Edit] entries
 * marking each line as Equal, Delete, or Insert.
 */
private fun myersDiff(oldLines: List<String>, newLines: List<String>): List<Edit> {
    val n = oldLines.size
    val m = newLines.size
    val max = n + m

    if (max == 0) return emptyList()

    // Forward pass of Myers' O(ND) algorithm.
    // v[k] = furthest reaching x on diagonal k.
    // We offset k by max so indices are non-negative.
    val v = IntArray(2 * max + 1) { -1 }
    v[max + 1] = 0

    // Record trace for backtracking.
    val trace = mutableListOf<IntArray>()

    var found = false
    for (d in 0..max) {
        val vCopy = v.copyOf()
        trace.add(vCopy)

        for (k in -d..d step 2) {
            val idx = k + max
            val x: Int
            if (k == -d || (k != d && v[idx - 1] < v[idx + 1])) {
                x = v[idx + 1]
            } else {
                x = v[idx - 1] + 1
            }
            var curX = x
            var curY = curX - k

            // Follow the diagonal (snake).
            while (curX < n && curY < m && oldLines[curX] == newLines[curY]) {
                curX++
                curY++
            }

            v[idx] = curX

            if (curX >= n && curY >= m) {
                found = true
                break
            }
        }
        if (found) break
    }

    // Backtrack to recover the edit script.
    val edits = mutableListOf<Edit>()
    var cx = n
    var cy = m

    for (d in (trace.size - 1) downTo 0) {
        val vPrev = trace[d]
        val k = cx - cy

        val prevK: Int
        if (d == 0) {
            prevK = 0 // doesn't matter, we'll handle the base case
        } else if (k == -d || (k != d && vPrev[k - 1 + max] < vPrev[k + 1 + max])) {
            prevK = k + 1
        } else {
            prevK = k - 1
        }

        val prevX = if (d == 0) 0 else vPrev[prevK + max]
        val prevY = prevX - prevK

        // Add diagonal (equal) moves.
        var sx = cx
        var sy = cy
        while (sx > prevX && sy > prevY && sx > 0 && sy > 0) {
            sx--
            sy--
        }

        // Record diagonal entries (equals) in forward order later.
        val diagonals = mutableListOf<Edit>()
        var tx = sx
        var ty = sy
        while (tx < cx && ty < cy) {
            diagonals.add(Edit(EditType.Equal, oldLines[tx]))
            tx++
            ty++
        }

        // The edit step (insertion or deletion).
        if (d > 0) {
            if (prevK < k) {
                // Deletion
                if (prevX in 0 until n) {
                    edits.add(Edit(EditType.Delete, oldLines[prevX]))
                }
            } else {
                // Insertion
                if (prevY in 0 until m) {
                    edits.add(Edit(EditType.Insert, newLines[prevY]))
                }
            }
        }

        edits.addAll(diagonals.reversed())

        cx = prevX
        cy = prevY
    }

    edits.reverse()
    return edits
}

/**
 * Group edit script entries into hunks with the specified number of
 * context lines around each change.
 */
private fun buildHunks(edits: List<Edit>, contextLines: Int): List<Hunk> {
    if (edits.isEmpty()) return emptyList()

    // First, find the indices of changed edits.
    val changeIndices = edits.indices.filter { edits[it].type != EditType.Equal }
    if (changeIndices.isEmpty()) return emptyList()

    // Group changes that are within `contextLines` of each other.
    val groups = mutableListOf<MutableList<Int>>()
    var currentGroup = mutableListOf(changeIndices[0])
    for (i in 1 until changeIndices.size) {
        val gap = changeIndices[i] - changeIndices[i - 1] - 1
        if (gap <= contextLines * 2) {
            currentGroup.add(changeIndices[i])
        } else {
            groups.add(currentGroup)
            currentGroup = mutableListOf(changeIndices[i])
        }
    }
    groups.add(currentGroup)

    // Build hunks from groups.
    val hunks = mutableListOf<Hunk>()
    for (group in groups) {
        val firstChange = group.first()
        val lastChange = group.last()

        val hunkStart = maxOf(0, firstChange - contextLines)
        val hunkEnd = minOf(edits.size - 1, lastChange + contextLines)

        val hunkEdits = edits.subList(hunkStart, hunkEnd + 1)

        // Compute old/new line numbers at the start of this hunk.
        // Lines are 1-indexed; we start at 0 and increment past edits before the hunk.
        var oldLine = 0
        var newLine = 0
        for (i in 0 until hunkStart) {
            when (edits[i].type) {
                EditType.Equal -> { oldLine++; newLine++ }
                EditType.Delete -> oldLine++
                EditType.Insert -> newLine++
            }
        }

        var oldCount = 0
        var newCount = 0
        for (edit in hunkEdits) {
            when (edit.type) {
                EditType.Equal -> { oldCount++; newCount++ }
                EditType.Delete -> oldCount++
                EditType.Insert -> newCount++
            }
        }

        // Convert to 1-indexed. If count is 0, start stays at 0 (git convention for
        // empty side, e.g., "-0,0" when adding to a previously empty file).
        val oldStart = if (oldCount == 0) 0 else oldLine + 1
        val newStart = if (newCount == 0) 0 else newLine + 1

        hunks.add(Hunk(oldStart, oldCount, newStart, newCount, hunkEdits.toList()))
    }

    return hunks
}

private fun formatHunkHeader(hunk: Hunk): String {
    val oldRange = if (hunk.oldCount == 1) "${hunk.oldStart}" else "${hunk.oldStart},${hunk.oldCount}"
    val newRange = if (hunk.newCount == 1) "${hunk.newStart}" else "${hunk.newStart},${hunk.newCount}"
    return "@@ -$oldRange +$newRange @@\n"
}

// =============================================================================
// File System Helpers
// =============================================================================

/**
 * Determine the file mode for a path.
 *
 * Ported from Rust turn_diff_tracker::file_mode_for_path
 */
private fun fileModeForPath(path: Path): FileMode {
    val fs = FileSystem.SYSTEM
    return try {
        val metadata = fs.metadata(path)
        when {
            metadata.symlinkTarget != null -> FileMode.Symlink
            else -> {
                // Check executable bit on Unix-like systems.
                // Okio doesn't expose permission bits directly, so we default to Regular.
                // Platform-specific executable detection can be added via expect/actual if needed.
                FileMode.Regular
            }
        }
    } catch (_: Exception) {
        FileMode.Regular
    }
}

/**
 * Read the file content bytes, handling symlinks by reading the symlink target path.
 *
 * Ported from Rust turn_diff_tracker::blob_bytes
 */
private fun blobBytes(path: Path, mode: FileMode): ByteArray? {
    val fs = FileSystem.SYSTEM
    return try {
        if (!fs.exists(path)) return null
        if (mode == FileMode.Symlink) {
            symlinkBlobBytes(path)
        } else {
            fs.read(path) { readByteArray() }
        }
    } catch (_: Exception) {
        null
    }
}

/**
 * For symlinks, the "blob" content is the target path as bytes.
 *
 * Ported from Rust turn_diff_tracker::symlink_blob_bytes
 */
private fun symlinkBlobBytes(path: Path): ByteArray? {
    val fs = FileSystem.SYSTEM
    return try {
        val metadata = fs.metadata(path)
        val target = metadata.symlinkTarget ?: return null
        target.toString().encodeToByteArray()
    } catch (_: Exception) {
        null
    }
}

/**
 * Check if a path is a directory.
 */
private fun isDirectory(path: Path): Boolean {
    return try {
        FileSystem.SYSTEM.metadata(path).isDirectory
    } catch (_: Exception) {
        false
    }
}

// =============================================================================
// Utility Extensions
// =============================================================================

/**
 * Try to decode bytes as UTF-8. Returns null if the bytes are not valid UTF-8.
 */
private fun ByteArray.toUtf8OrNull(): String? {
    return try {
        val s = decodeToString()
        // Verify no replacement characters were introduced for truly invalid sequences.
        // The Kotlin decodeToString() is lenient; check for the replacement character
        // that indicates malformed input.
        if (s.contains('\uFFFD')) {
            // Double-check: if the original bytes don't round-trip, it's binary.
            val roundTrip = s.encodeToByteArray()
            if (!roundTrip.contentEquals(this)) null else s
        } else {
            s
        }
    } catch (_: Exception) {
        null
    }
}

/**
 * Nullable-safe content comparison for byte arrays.
 */
private fun ByteArray?.contentEqualsNullable(other: ByteArray?): Boolean {
    if (this == null && other == null) return true
    if (this == null || other == null) return false
    return this.contentEquals(other)
}
