// port-lint: source core/src/features.rs
package ai.solace.coder.core.features

/**
 * Unique features toggled via configuration.
 * This is the expect declaration - actual implementation is in nativeMain.
 */
expect enum class Feature {
    GhostCommit,
    UnifiedExec,
    RmcpClient,
    ApplyPatchFreeform,
    ViewImageTool,
    WebSearchRequest,
    ExecPolicy,
    SandboxCommandAssessment,
    WindowsSandbox,
    RemoteCompaction,
    ShellTool,
    ParallelToolCalls
}

/**
 * Holds the effective set of enabled features.
 * This is the expect declaration - actual implementation is in nativeMain.
 */
expect class Features {
    fun enabled(feature: Feature): Boolean
    fun enable(feature: Feature): Features
    fun disable(feature: Feature): Features
    fun enabledFeatures(): List<Feature>
    fun copy(): Features

    companion object {
        fun withDefaults(): Features
    }
}
