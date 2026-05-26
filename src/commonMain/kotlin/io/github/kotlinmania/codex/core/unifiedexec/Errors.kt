// port-lint: source core/src/unified_exec/errors.rs
<<<<<<<< HEAD:src/nativeMain/kotlin/io/github/kotlinmania/codex/core/unified_exec/Errors.kt
package io.github.kotlinmania.codex.core.unified_exec

import io.github.kotlinmania.codex.core.exec.ExecToolCallOutput
========
package io.github.kotlinmania.codex.core.unifiedexec

import io.github.kotlinmania.codex.core.ExecToolCallOutput
>>>>>>>> origin/main:src/commonMain/kotlin/io/github/kotlinmania/codex/core/unifiedexec/Errors.kt

sealed class UnifiedExecError : Exception() {
    data class CreateSession(override val message: String) : UnifiedExecError()
    
    // Called "session" in the model training.
    data class UnknownSessionId(val processId: String) : UnifiedExecError() {
        override val message: String = "Unknown session id $processId"
    }
    
    class WriteToStdin : UnifiedExecError() {
        override val message: String = "failed to write to stdin"
    }
    
    class MissingCommandLine : UnifiedExecError() {
        override val message: String = "missing command line for unified exec request"
    }
    
    data class SandboxDenied(
        override val message: String,
        val output: ExecToolCallOutput
    ) : UnifiedExecError()

    data class SandboxError(override val message: String) : UnifiedExecError()

    companion object {
        fun createSession(message: String): UnifiedExecError = CreateSession(message)
        fun sandboxDenied(message: String, output: ExecToolCallOutput): UnifiedExecError = SandboxDenied(message, output)
    }
}
