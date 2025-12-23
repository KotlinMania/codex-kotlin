# System Specification: Storage

## Overview
This document specifies the as-built state of the `ai.solace.coder.core.auth.Storage.kt` component, which maps to the Rust `codex-rs/core/src/auth/storage.rs` implementation.

## Structural Implementation
The Kotlin implementation maintains a 1:1 structural parity with the Rust source, enabling consistent cross-platform authentication state management.

| Component | Status | Implementation Detail |
|-----------|--------|----------------------|
| `AuthCredentialsStoreMode` | ✅ Complete | Mapped to Rust enum variants. |
| `AuthStorageBackend` | ✅ Complete | Interface for storage strategy. |
| `FileAuthStorage` | ✅ Complete | Uses `kotlinx.io` for filesystem operations. |
| `KeychainAuthStorage` | ✅ Structural | Implements logic with delegation to `KeychainStore`. |
| `AutoAuthStorage` | ✅ Complete | Handles keychain-first with file fallback. |
| `KeychainStore` | ✅ Complete | Interface for platform-specific extensions. |

## Implementation Details

### File-Based Storage
- Reads and writes `auth.json` using `kotlinx.serialization`.
- Automatically handles directory creation for the `CODEX_HOME` path.
- **As-Built Note**: Unix file permissions (mode `0600`) are currently a post-processing requirement as they require platform-specific POSIX calls.

### Keychain-Based Storage
- Computes storage keys using a SHA-256 hash (implemented in `Hashing.kt`).
- Supports the `KeychainStore` interface for secure credential persistence.
- **As-Built Note**: The `DefaultKeychainStore` currently serves as a structural stub. Real-world persistence on macOS (`Security.framework`), Linux (`libsecret`), and Windows (`Credential Manager`) requires the implementation of these platform-specific drivers.

### Path Management
- **As-Built Note**: Path canonicalization is currently performed using absolute path strings. Symbolic link resolution is a future structural enhancement.

## Hashing and Key Generation
The `computeStoreKey` function utilizes the purely Kotlin `Sha256MessageDigest` from `Hashing.kt` to ensure stable, platform-independent key generation.

## Reference Mapping

| Rust | Kotlin |
|------|--------|
| `storage.rs` | `Storage.kt` |
| `KeyringStore` | `KeychainStore` |
| `AuthCredentialsStoreMode` | `AuthCredentialsStoreMode` |

---
**Status**: Structural port complete. Functional platform drivers for Keychains are the next integration milestone.

