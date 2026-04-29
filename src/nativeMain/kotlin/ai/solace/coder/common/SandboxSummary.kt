// port-lint: source sandbox_summary.rs
package ai.solace.coder.common

import ai.solace.coder.protocol.SandboxPolicy

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
