# Security

## Dependency Security Advisories

This document tracks security vulnerabilities in project dependencies and their resolution status.

### NPM Dependencies (ktreesitter-kotlin)

The tree-sitter language grammars (bash and java) include NPM development dependencies for building grammar files. These dependencies are used only during development and are not included in runtime artifacts.

**Status**: ✅ All resolved as of 2026-05-20

The following high and moderate severity NPM vulnerabilities have been addressed:
- tar-fs: Path traversal and symlink validation bypasses (FIXED)
- minimatch: ReDoS vulnerabilities (FIXED)
- flatted: Prototype pollution (FIXED)
- js-yaml: Prototype pollution (FIXED)

### Rust Dependencies (ktreesitter-kotlin/tree-sitter)

The tree-sitter repository includes a Cargo.lock file for the tree-sitter CLI tool. **Important**: The Kotlin build does NOT use the Rust CLI or build the Rust workspace. Instead, it compiles the C library directly (lib/src/lib.c) using the Kotlin Native toolchain.

The vulnerabilities in the Rust dependencies are therefore **not exploitable** in the Kotlin build artifacts:

- wasmtime (multiple CVEs) - Used only by tree-sitter CLI, not the C library
- rustls-webpki - Used only by tree-sitter CLI for network operations
- smallbitvec - Used only by tree-sitter CLI
- bytes - Used only by tree-sitter CLI
- time - Used only by tree-sitter CLI

**Status**: ⚠️ Not applicable - These dependencies are not used in the Kotlin build

The tree-sitter Rust workspace in this repository is incomplete (missing the `tags/` directory and other components) and cannot be built or updated. This is intentional as we only need the C library source code for the Kotlin Native bindings.

### Mitigation

If you need to address these Rust vulnerabilities:

1. **Do not use the tree-sitter CLI from this vendored copy** - Install the official tree-sitter CLI from the upstream repository if needed for development.

2. **The C library is safe** - The tree-sitter C library (lib/src/lib.c) which is actually used by the Kotlin build does not have these vulnerabilities.

3. **Development dependencies only** - All NPM vulnerabilities are in development-scoped packages that are not included in published artifacts.

### Reporting Security Issues

If you discover a security vulnerability in the Kotlin code or runtime dependencies, please report it to the maintainers.
