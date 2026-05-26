// port-lint: source utils/sandbox-summary/src/sandbox_summary.rs
package io.github.kotlinmania.codex.common

import io.github.kotlinmania.codex.protocol.SandboxPolicy

fun summarizeSandboxPolicy(sandboxPolicy: SandboxPolicy): String {
    return when (sandboxPolicy) {
        is SandboxPolicy.DangerFullAccess -> "danger-full-access"
        is SandboxPolicy.ReadOnly -> "read-only"
        is SandboxPolicy.WorkspaceWrite -> {
            val writableEntries = mutableListOf<String>()
            writableEntries.add("workdir")
            if (!sandboxPolicy.excludeSlashTmp) {
                writableEntries.add("/tmp")
            }
            if (!sandboxPolicy.excludeTmpdirEnvVar) {
                writableEntries.add("\$TMPDIR")
            }
            writableEntries.addAll(sandboxPolicy.writableRoots)

            val summary = StringBuilder("workspace-write")
            summary.append(" [${writableEntries.joinToString(", ")}]")
            if (sandboxPolicy.networkAccess) {
                summary.append(" (network access enabled)")
            }
            summary.toString()
        }
    }
}
