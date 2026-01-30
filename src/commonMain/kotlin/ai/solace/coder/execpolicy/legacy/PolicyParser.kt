// port-lint: source execpolicy-legacy/src/policy_parser.rs
package ai.solace.coder.execpolicy.legacy

// TODO: This file depends on starlark-kotlin which is being ported separately at:
// /Volumes/stuff/Projects/starlark-kotlin
// 
// Until starlark-kotlin is ready, this file cannot be fully ported.
// The Rust implementation uses:
// - starlark::environment::Module, GlobalsBuilder, LibraryExtension
// - starlark::eval::Evaluator
// - starlark::syntax::AstModule, Dialect
// - starlark::values::Heap
// - starlark_module macro for defining builtins
//
// This is a complete scripting language interpreter and cannot be stubbed.
