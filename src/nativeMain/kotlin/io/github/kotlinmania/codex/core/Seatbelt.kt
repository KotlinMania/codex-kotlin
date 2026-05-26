// port-lint: source core/src/seatbelt.kt
package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.protocol.SandboxPolicy
import io.github.kotlinmania.codex.protocol.WritableRoot
import kotlinx.cinterop.*
import platform.posix.*

/**
 * PATH to seatbelt executable.
 */
const val MACOS_PATH_TO_SEATBELT_EXECUTABLE = "/usr/bin/sandbox-exec"

private const val MACOS_SEATBELT_BASE_POLICY = """(version 1)
; inspired by Chrome's sandbox policy:
; https://source.chromium.org/chromium/chromium/src/+/main:sandbox/policy/mac/common.sb;l=273-319;drc=7b3962fe2e5fc9e2ee58000dc8fbf3429d84d3bd
; https://source.chromium.org/chromium/chromium/src/+/main:sandbox/policy/mac/renderer.sb;l=64;drc=7b3962fe2e5fc9e2ee58000dc8fbf3429d84d3bd
; start with closed-by-default
(deny default)
; child processes inherit the policy of their parent
(allow process-exec)
(allow process-fork)
(allow signal (target same-sandbox))
; Allow cf prefs to work.
(allow user-preference-read)
; process-info
(allow process-info* (target same-sandbox))
(allow file-write-data
  (require-all
    (path "/dev/null")
    (vnode-type CHARACTER-DEVICE)))
; sysctls permitted.
(allow sysctl-read
  (sysctl-name "hw.activecpu")
  (sysctl-name "hw.busfrequency_compat")
  (sysctl-name "hw.byteorder")
  (sysctl-name "hw.cacheconfig")
  (sysctl-name "hw.cachelinesize_compat")
  (sysctl-name "hw.cpufamily")
  (sysctl-name "hw.cpufrequency_compat")
  (sysctl-name "hw.cputype")
  (sysctl-name "hw.l1dcachesize_compat")
  (sysctl-name "hw.l1icachesize_compat")
  (sysctl-name "hw.l2cachesize_compat")
  (sysctl-name "hw.l3cachesize_compat")
  (sysctl-name "hw.logicalcpu_max")
  (sysctl-name "hw.machine")
  (sysctl-name "hw.memsize")
  (sysctl-name "hw.ncpu")
  (sysctl-name "hw.nperflevels")
  ; Chrome locks these CPU feature detection down a bit more tightly,
  ; but mostly for fingerprinting concerns which isn't an issue for codex.
  (sysctl-name-prefix "hw.optional.arm.")
  (sysctl-name-prefix "hw.optional.armv8_")
  (sysctl-name "hw.packages")
  (sysctl-name "hw.pagesize_compat")
  (sysctl-name "hw.pagesize")
  (sysctl-name "hw.physicalcpu")
  (sysctl-name "hw.physicalcpu_max")
  (sysctl-name "hw.tbfrequency_compat")
  (sysctl-name "hw.vectorunit")
  (sysctl-name "kern.hostname")
  (sysctl-name "kern.maxfilesperproc")
  (sysctl-name "kern.maxproc")
  (sysctl-name "kern.osproductversion")
  (sysctl-name "kern.osrelease")
  (sysctl-name "kern.ostype")
  (sysctl-name "kern.osvariant_status")
  (sysctl-name "kern.osversion")
  (sysctl-name "kern.secure_kernel")
  (sysctl-name "kern.usrstack64")
  (sysctl-name "kern.version")
  (sysctl-name "sysctl.proc_cputype")
  (sysctl-name "vm.loadavg")
  (sysctl-name-prefix "hw.perflevel")
  (sysctl-name-prefix "kern.proc.pgrp.")
  (sysctl-name-prefix "kern.proc.pid.")
  (sysctl-name-prefix "net.routetable.")
)
; Allow Java to set CPU type grade when required
(allow sysctl-write
  (sysctl-name "kern.grade_cputype"))
; IOKit
(allow iokit-open
  (iokit-registry-entry-class "RootDomainUserClient")
)
; needed to look up user info, see https://crbug.com/792228
(allow mach-lookup
  (global-name "com.apple.system.opendirectoryd.libinfo")
)
; Added on top of Chrome profile
; Needed for python multiprocessing on MacOS for the SemLock
(allow ipc-posix-sem)
(allow mach-lookup
  (global-name "com.apple.PowerManagement.control")
)"""

private const val MACOS_SEATBELT_NETWORK_POLICY = """; when network access is enabled, these policies are added after those in seatbelt_base_policy.sbpl
; Ref https://source.chromium.org/chromium/chromium/src/+/main:sandbox/policy/mac/network.sb;drc=f8f264d5e4e7509c913f4c60c2639d15905a07e4
(allow network-outbound)
(allow network-inbound)
(allow system-socket)
(allow mach-lookup
    ; Used to look up the _CS_DARWIN_USER_CACHE_DIR in the sandbox.
    (global-name "com.apple.bsd.dirhelper")
    (global-name "com.apple.system.opendirectoryd.membership")
    ; Communicate with the security server for TLS certificate information.
    (global-name "com.apple.SecurityServer")
    (global-name "com.apple.networkd")
    (global-name "com.apple.ocspd")
    (global-name "com.apple.trustd.agent")
    ; Read network configuration.
    (global-name "com.apple.SystemConfiguration.DNSConfiguration")
    (global-name "com.apple.SystemConfiguration.configd")
)
(allow sysctl-read
  (sysctl-name-regex #"^net.routetable")
)
(allow file-write*
  (subpath (param "DARWIN_USER_CACHE_DIR"))
)"""

/**
 * Creates the command line arguments for sandbox-exec on macOS.
 */
fun createSeatbeltCommandArgs(
    command: List<String>,
    sandboxPolicy: SandboxPolicy,
    sandboxPolicyCwd: String
): List<String> {
    val (fileWritePolicy, fileWriteDirParams) = run {
        if (sandboxPolicy.hasFullDiskWriteAccess()) {
            Pair("(allow file-write* (regex #\"^/\"))", emptyList<Pair<String, String>>())
        } else {
            val writableRoots = sandboxPolicy.getWritableRootsWithCwd(sandboxPolicyCwd)
            val writableFolderPolicies = mutableListOf<String>()
            val fileWriteParams = mutableListOf<Pair<String, String>>()

            writableRoots.forEachIndexed { index, wr ->
                // TODO: Canonicalize paths properly in Multiplatform
                val canonicalRoot = wr.root 
                val rootParam = "WRITABLE_ROOT_$index"
                fileWriteParams.add(rootParam to canonicalRoot)

                if (wr.readOnlySubpaths.isEmpty()) {
                    writableFolderPolicies.add("(subpath (param \"$rootParam\"))")
                } else {
                    val requireParts = mutableListOf<String>()
                    requireParts.add("(subpath (param \"$rootParam\"))")
                    wr.readOnlySubpaths.forEachIndexed { subpathIndex, ro ->
                        val roParam = "WRITABLE_ROOT_${index}_RO_$subpathIndex"
                        requireParts.add("(require-not (subpath (param \"$roParam\")))")
                        fileWriteParams.add(roParam to ro)
                    }
                    writableFolderPolicies.add("(require-all ${requireParts.joinToString(" ")} )")
                }
            }

            if (writableFolderPolicies.isEmpty()) {
                "" to emptyList()
            } else {
                val policy = "(allow file-write*\n${writableFolderPolicies.joinToString("\n")}\n)"
                policy to fileWriteParams
            }
        }
    }

    val fileReadPolicy = if (sandboxPolicy.hasFullDiskReadAccess()) {
        "; allow read-only file operations\n(allow file-read*)"
    } else {
        ""
    }

    val networkPolicy = if (sandboxPolicy.hasFullNetworkAccess()) {
        MACOS_SEATBELT_NETWORK_POLICY
    } else {
        ""
    }

    val fullPolicy = "$MACOS_SEATBELT_BASE_POLICY\n$fileReadPolicy\n$fileWritePolicy\n$networkPolicy"

    val dirParams = fileWriteDirParams + macosDirParams()

    val seatbeltArgs = mutableListOf("-p", fullPolicy)
    dirParams.forEach { (key, value) ->
        seatbeltArgs.add("-D$key=$value")
    }
    seatbeltArgs.add("--")
    seatbeltArgs.addAll(command)
    return seatbeltArgs
}

/**
 * Wraps libc::confstr to return a String.
 */
@OptIn(ExperimentalForeignApi::class)
private fun confstr(name: Int): String? {
    val len = platform.posix.confstr(name, null, 0u.convert())
    if (len == 0u.convert<size_t>()) return null

    return memScoped {
        val buf = allocArray<ByteVar>(len.convert())
        if (platform.posix.confstr(name, buf, len) == 0u.convert<size_t>()) {
            null
        } else {
            buf.toKString()
        }
    }
}

/**
 * Returns macOS specific directory parameters.
 */
private fun macosDirParams(): List<Pair<String, String>> {
    // _CS_DARWIN_USER_CACHE_DIR is 65538
    val CS_DARWIN_USER_CACHE_DIR = 65538
    val p = confstr(CS_DARWIN_USER_CACHE_DIR)
    return if (p != null) {
        listOf("DARWIN_USER_CACHE_DIR" to p)
    } else {
        emptyList()
    }
}
