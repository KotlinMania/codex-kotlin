# codex-kotlin

<div align="center">

![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-7F52FF.svg?logo=kotlin&logoColor=white)
![Platform](https://img.shields.io/badge/platform-native%20|%20jvm%20|%20js-orange)
![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)
![Build](https://img.shields.io/badge/build-passing-brightgreen)

**The Native Kotlin Port of the Codex Agentic Backend**

*Maintained by **Sydney Renee, The Solace Project***  
*A **KotlinMania** Project*

[Features](#features) • [Installation](#installation) • [Usage](#usage) • [Status](#status) • [Contributing](#contributing)

</div>

---

## 📖 Overview

**codex-kotlin** is a high-performance, type-safe Kotlin Multiplatform port of the `codex-rs` coding agent backend. Use the power of advanced Agentic AI directly from your Kotlin applications, or run it as a standalone, zero-dependency native CLI.

This project brings the robust architecture of the original Rust implementation to the Kotlin ecosystem, enabling:
*   **Native Performance:** Compiled to native binaries (macOS, Linux, Windows) via Kotlin Native.
*   **Zero JVM Dependency:** Run the CLI without installing a Java Runtime Environment.
*   **Type Safety:** Leverage Kotlin's powerful type system for protocol definitions and error handling.
*   **Multiplatform:** Share core agent logic across Server (JVM), Desktop (Native), and Web (JS/Wasm) targets.

## ✨ Features

*   **🧩 Universal Model Client:** A unified, reactive API for interacting with major LLM providers:
    *   **OpenAI:** Full support including `o1` reasoning models and `gpt-4o`.
    *   **Anthropic:** Native support for Claude 3.5 Sonnet and Haiku.
    *   **Google Gemini:** Deep integration with Google's latest models.
*   **🔐 Secure Authentication:**
    *   Native OS Keychain integration (macOS Security, Linux libsecret, Windows CredMan) via `KeychainAuthStorage`.
    *   Secure fallback to encrypted file storage (`auth.json`).
*   **🛡️ Robust Sandboxing:**
    *   Ported execution engine with pluggable approval policies.
    *   Safe command execution wrappers (WIP).
*   **⚡ Reactive Architecture:**
    *   Built on **Kotlin Coroutines** and **Flow** for asynchronous, non-blocking stream processing.
    *   Reactive telemetry and event handling.
*   **📦 Protocol 1:1 Parity:**
    *   Complete implementation of the Codex Protocol (all DTOs, Enums, and Serialization rules).

## 🚀 Usage

`codex-kotlin` is designed to be idiomatic and easy to use. Below are examples using the actual ported APIs.

### 1. Initialize the Model Client

The `ModelClient` is the versatile core of the library, handling connection reuse, rate limiting, and provider switching.

```kotlin
// Import the core definitions
import ai.solace.coder.core.client.ModelClient
import ai.solace.coder.core.config.Config
import ai.solace.coder.core.auth.AuthManager

// 1. Setup Configuration & Auth
val config = Config.fromEnv() // Loads from ~/.codex/config.toml or ENV
val auth = AuthManager()      // Handles token refresh & storage automatically

// 2. Create the Client
val client = ModelClient(
    config = config,
    authManager = auth,
    provider = ModelProviderInfo.OpenAI, // or Anthropic, Google
    effort = ReasoningEffortConfig.Medium, // For reasoning models
    verbosity = Verbosity.Normal
)
```

### 2. Streaming Chat Completions

Interact with models using Kotlin Flows for real-time response processing.

```kotlin
import ai.solace.coder.api.common.Prompt

// Define your prompt
val prompt = Prompt(
    system = "You are a helpful coding assistant.",
    user = "Write a Kotlin function to calculate Fibonacci numbers."
)

// Stream the response
client.stream(prompt).collect { event ->
    when (event) {
        is ResponseEvent.Text -> print(event.content)
        is ResponseEvent.ToolUse -> println("Tool Call: ${event.toolName}")
        is ResponseEvent.Citation -> println("Source: ${event.uri}")
        is ResponseEvent.Done -> println("\nStream Finished.")
    }
}
```

### 3. Secure Storage Access

Directly access the storage layer to manage credentials securely across different platforms.

```kotlin
import ai.solace.coder.core.auth.createAuthStorage
import ai.solace.coder.core.auth.AuthCredentialsStoreMode

// Create storage (Auto prefers Keychain, falls back to File)
val storage = createAuthStorage(
    codexHome = Path("/Users/me/.codex"),
    mode = AuthCredentialsStoreMode.Auto
)

// Save credentials (encrypted/protected by OS)
val authData = AuthDotJson(openaiApiKey = "sk-...")
storage.save(authData).getOrThrow()

// Load credentials
val loadedAuth = storage.load().getOrNull()
```

### 4. Native SHA-256 Hashing

Use the high-performance, zero-dependency SHA-256 implementation (no Java stdlib needed).

```kotlin
import ai.solace.coder.core.Sha256

val data = "Hello Codex".encodeToByteArray()
val hash = Sha256.digest(data)
println(hash.toHexString())
```

## 🛠️ Build & Installation

### Requirements
*   **JDK 17+** (for building)
*   **Kotlin 2.1.0** (bundled in Gradle wrapper)
*   **Gradle** (via wrapper)

### Building from Source

```bash
# Clone the repository
git clone https://github.com/KotlinMania/codex-kotlin.git
cd codex-kotlin

# Build the Native executable (release mode)
./gradlew :src:nativeMain:linkReleaseExecutableNative

# The binary will be available at:
# ./build/bin/native/releaseExecutable/codex-kotlin.kexe
```

## 📊 Project Status

The porting effort is active and ongoing.

| Module | Status | Notes |
| :--- | :---: | :--- |
| **Protocol** | ✅ 100% | Full type safety and serialization parity. |
| **Auth & Storage** | ✅ 100% | Native keychain integration and file fallback logic. |
| **Model Client** | 🟢 90% | Core streaming and provider logic complete. |
| **SHA-256** | ✅ 100% | Pure Kotlin Native implementation. |
| **Execution** | 🟡 40% | Basic process spawning works; PTY support WIP. |

👉 **[View Detailed Porting Status](docs/kotlin/PORTING_STATUS_COMPREHENSIVE.md)**

## 🤝 Contributing

We welcome contributions! Please see [CONTRIBUTING.md](docs/contributing.md) for details on how to get started.

## 📄 License

This project is licensed under the [Apache License 2.0](LICENSE).

---

<p align="center">
  Built with ❤️ by <strong>KotlinMania</strong>
</p>
