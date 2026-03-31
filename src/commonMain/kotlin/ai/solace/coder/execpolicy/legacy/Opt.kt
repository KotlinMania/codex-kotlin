// port-lint: source execpolicy-legacy/src/opt.rs
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

import kotlinx.serialization.Serializable

/// Command line option that takes a value.
@Serializable
data class Opt(
    /// The option as typed on the command line, e.g., `-h` or `--help`. If
    /// it can be used in the `--name=value` format, then this should be
    /// `--name` (though this is subject to change).
    val opt: String,
    val meta: OptMeta,
    val required: Boolean,
) {
    companion object {
        fun new(opt: String, meta: OptMeta, required: Boolean): Opt {
            return Opt(opt, meta, required)
        }
    }

    fun name(): String = opt

    override fun toString(): String = "opt($opt)"
}

/// When defining an Opt, use as specific an OptMeta as possible.
@Serializable
sealed class OptMeta {
    /// Option does not take a value.
    @Serializable
    data object Flag : OptMeta() {
        override fun toString(): String = "Flag"
    }

    /// Option takes a single value matching the specified type.
    @Serializable
    data class Value(val argType: ArgType) : OptMeta() {
        override fun toString(): String = "Value($argType)"
    }
}
