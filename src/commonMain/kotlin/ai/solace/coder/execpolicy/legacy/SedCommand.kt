// port-lint: source execpolicy-legacy/src/sed_command.rs
package ai.solace.coder.execpolicy.legacy

/*
 * Copyright 2019 The Starlark in Rust Authors.
 * Copyright (c) Facebook, Inc. and its affiliates.
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

fun parseSedCommand(sedCommand: String): ExecPolicyResult<Unit> {
    // For now, we parse only commands like `122,202p`.
    if (sedCommand.endsWith("p")) {
        val stripped = sedCommand.dropLast(1)
        val commaIdx = stripped.indexOf(',')
        if (commaIdx >= 0) {
            val first = stripped.substring(0, commaIdx)
            val rest = stripped.substring(commaIdx + 1)
            if (first.toULongOrNull() != null && rest.toULongOrNull() != null) {
                return ExecPolicyResult.Ok(Unit)
            }
        }
    }

    return ExecPolicyResult.Err(ExecPolicyError.SedCommandNotProvablySafe(
        command = sedCommand,
    ))
}
