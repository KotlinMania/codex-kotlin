# port-lint Proposed Changes

**Generated:** 2026-04-29
**Source:** codex-rs
**Target:** src/commonMain/kotlin/ai/solace/coder

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonTest/kotlin/ai/solace/coder/core/ErrorTest.kt` | `// port-lint: source core/src/error.rs` | `// port-lint: source error.rs` | `error.rs` | `port-lint provenance header matched only after fallback normalization: 'core/src/error.rs' vs expected 'error.rs'` |
