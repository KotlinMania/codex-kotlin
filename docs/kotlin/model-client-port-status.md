# System Specification: Model Client

## Overview
This document specifies the as-built state of the `ai.solace.coder.core.client.ModelClient.kt` component, which maps to the Rust `codex-rs/core/src/client.rs` implementation.

## Structural Implementation
The Kotlin implementation provides 1:1 structural parity with the Rust `ModelClient`, ensuring that the core coordination of streaming turns, authentication refreshes, and telemetry hooks is preserved.

| Feature | Status | Implementation Detail |
|---------|--------|----------------------|
| **Dual API Support** | ✅ Complete | Routes to Chat or Responses API based on `WireApi`. |
| **Token Refresh** | ✅ Complete | Handles 401 unauthorized errors with ChatGPT token refresh. |
| **Reasoning Support** | ✅ Complete | Passes effort and summary configurations to the API. |
| **Telemetry** | ✅ Structural | Hooks for `RequestTelemetry` and `SseTelemetry` in place. |
| **Streaming** | ✅ Structural | `ResponseStream` implemented using Kotlin `Flow`. |

## Implementation Details

### API Interaction Layer
- Routes requests to either Chat Completions or Responses API endpoints.
- Supports reasoning summaries and verbosity controls for compatible models.
- **As-Built Note**: The streaming path utilizing `SseParser` is currently a structural placeholder in `mapResponseStream`. Full SSE event decoding is the primary development focus for this module.

### Telemetry and Tracking
- Integrates with `OtelEventManager` to record API requests and SSE poll durations.
- **As-Built Note**: Currently utilizes internal structural stubs for `OtelEventManager`. Integration with the `codex-otel` logic is required for production telemetry.

### Subagent and Context
- Supports `x-openai-subagent` header injection for review and task contexts.
- Handles automated context compaction via the unary `compactConversationHistory` endpoint.

## Reference Mapping

| Rust | Kotlin |
|------|--------|
| `client.rs` | `ModelClient.kt` |
| `ApiResponseStream` | `ApiResponseStream` |
| `ModelClient` | `ModelClient` |

---
**Status**: Structural port complete. Implementation of the SSE parser and telemetry integration are the next technical milestones.

