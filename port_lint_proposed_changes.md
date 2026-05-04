# port-lint Proposed Changes

**Generated:** 2026-05-03
**Source:** codex-rs/core/src/error.rs
**Target:** src/commonMain/kotlin/io/github/solaceharmony/codex/core/Error.kt

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/solaceharmony/codex/core/Error.kt` | `// port-lint: source core/src/error.rs` | `// port-lint: source error.rs` | `error.rs` | `port-lint provenance header matched only after fallback normalization: 'core/src/error.rs' vs expected 'error.rs'` |
