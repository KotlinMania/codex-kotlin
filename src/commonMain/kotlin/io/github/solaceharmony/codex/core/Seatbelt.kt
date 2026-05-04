// port-lint: source seatbelt.rs
package io.github.solaceharmony.codex.core

import io.github.solaceharmony.codex.protocol.SandboxPolicy

private const val MACOS_SEATBELT_BASE_POLICY: String = """(version 1)

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
)
"""

private const val MACOS_SEATBELT_NETWORK_POLICY: String = """; when network access is enabled, these policies are added after those in seatbelt_base_policy.sbpl
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
)
"""

/**
 * When working with `sandbox-exec`, only consider `sandbox-exec` in `/usr/bin`
 * to defend against an attacker trying to inject a malicious version on the
 * PATH. If /usr/bin/sandbox-exec has been tampered with, then the attacker
 * already has root access.
 */
const val MACOS_PATH_TO_SEATBELT_EXECUTABLE: String = "/usr/bin/sandbox-exec"

fun createSeatbeltCommandArgs(
    command: List<String>,
    sandboxPolicy: SandboxPolicy,
    sandboxPolicyCwd: String,
): List<String> {
    val (fileWritePolicy, fileWriteDirParams) =
        if (sandboxPolicy.hasFullDiskWriteAccess()) {
            // Allegedly, this is more permissive than `(allow file-write*)`.
            Pair(
                "(allow file-write* (regex #\"^/\"))",
                emptyList<Pair<String, String>>(),
            )
        } else {
            val writableRoots = sandboxPolicy.getWritableRootsWithCwd(sandboxPolicyCwd)

            val writableFolderPolicies: MutableList<String> = mutableListOf()
            val fileWriteParams: MutableList<Pair<String, String>> = mutableListOf()

            for ((index, wr) in writableRoots.withIndex()) {
                // Rust canonicalizes here (e.g. /var vs /private/var on macOS) and
                // falls back to the original path on failure. Cross-platform
                // realpath is not available in this shared nativeMain source set,
                // so we import the path as-provided — matching the
                val canonicalRoot = wr.root
                val rootParam = "WRITABLE_ROOT_$index"
                fileWriteParams.add(rootParam to canonicalRoot)

                if (wr.readOnlySubpaths.isEmpty()) {
                    writableFolderPolicies.add("(subpath (param \"$rootParam\"))")
                } else {
                    // Add parameters for each read-only subpath and generate
                    // the `(require-not ...)` clauses.
                    val requireParts: MutableList<String> = mutableListOf()
                    requireParts.add("(subpath (param \"$rootParam\"))")
                    for ((subpathIndex, ro) in wr.readOnlySubpaths.withIndex()) {
                        val canonicalRo = ro
                        val roParam = "WRITABLE_ROOT_${index}_RO_$subpathIndex"
                        requireParts.add("(require-not (subpath (param \"$roParam\")))")
                        fileWriteParams.add(roParam to canonicalRo)
                    }
                    val policyComponent = "(require-all ${requireParts.joinToString(" ")} )"
                    writableFolderPolicies.add(policyComponent)
                }
            }

            if (writableFolderPolicies.isEmpty()) {
                Pair("", emptyList())
            } else {
                val joined = writableFolderPolicies.joinToString(" ")
                val fileWritePolicy = "(allow file-write*\n$joined\n)"
                Pair(fileWritePolicy, fileWriteParams.toList())
            }
        }

    val fileReadPolicy = if (sandboxPolicy.hasFullDiskReadAccess()) {
        "; allow read-only file operations\n(allow file-read*)"
    } else {
        ""
    }

    // TODO(mbolin): applyPatch calls must also honor the SandboxPolicy.
    val networkPolicy = if (sandboxPolicy.hasFullNetworkAccess()) {
        MACOS_SEATBELT_NETWORK_POLICY
    } else {
        ""
    }

    val fullPolicy =
        "$MACOS_SEATBELT_BASE_POLICY\n$fileReadPolicy\n$fileWritePolicy\n$networkPolicy"

    val dirParams: List<Pair<String, String>> = fileWriteDirParams + macosDirParams()

    val seatbeltArgs: MutableList<String> = mutableListOf("-p", fullPolicy)
    for ((key, value) in dirParams) {
        seatbeltArgs.add("-D$key=$value")
    }
    seatbeltArgs.add("--")
    seatbeltArgs.addAll(command)
    return seatbeltArgs
}

/**
 * Wraps confstr (via the platform expect/actual) to return canonicalized
 * `(name, path)` pairs. On non-macOS targets this returns an empty list,
 * matching the the file in `(cfg(targetOs = "macos"))` gating.
 */
private fun macosDirParams(): List<Pair<String, String>> = platformGetMacosDirParams()
