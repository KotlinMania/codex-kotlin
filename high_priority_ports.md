# High Priority Ports - Action Plan

## Top 20 Files by Impact (Priority Score = Deps × (1 - Similarity))

| Rank | Source | Target | Similarity | Deps | Priority |
|------|--------|--------|------------|------|----------|
| 1 | `core.error` | `error.CodexError` | 0.89 | 50 | 5.6 |
| 2 | `ollama.parser` | `ollama.Parser` | 0.76 | 23 | 5.5 |
| 3 | `otel.config` | `otel.Config` | 0.89 | 45 | 5.1 |
| 4 | `protocol.user_input` | `protocol.UserInput` | 0.75 | 20 | 5.0 |
| 5 | `tui.style` | `tui.Style` | 0.75 | 16 | 4.1 |
| 6 | `protocol.conversation_id` | `protocol.ConversationId` | 0.86 | 25 | 3.4 |
| 7 | `tui.app_event` | `tui.AppEvent` | 0.77 | 14 | 3.2 |
| 8 | `state.session` | `state.SessionState` | 0.83 | 18 | 3.1 |
| 9 | `tui.color` | `tui.Color` | 0.82 | 15 | 2.7 |
| 10 | `render.renderable` | `render.Renderable` | 0.90 | 19 | 2.0 |
| 11 | `tools.context` | `tools.Context` | 0.90 | 19 | 1.9 |
| 12 | `core.model_provider_info` | `model.ModelProviderInfo` | 0.75 | 7 | 1.7 |
| 13 | `windows-sandbox-rs.env` | `command_safety.WindowsEnvironment` | 0.84 | 8 | 1.3 |
| 14 | `protocol.models` | `protocol.Models` | 0.82 | 7 | 1.2 |
| 15 | `state.turn` | `session.Turn` | 0.73 | 3 | 0.8 |
| 16 | `core.mcp_connection_manager` | `connection.McpConnectionManager` | 0.61 | 2 | 0.8 |
| 17 | `tui.chatwidget` | `sse.Chat` | 0.64 | 2 | 0.7 |
| 18 | `core.model_family` | `model.ModelFamily` | 0.88 | 5 | 0.6 |
| 19 | `windows-sandbox-rs.token` | `nativeMain.kotlin.ai.solace.coder.core.auth.IdTokenParser` | 0.69 | 2 | 0.6 |
| 20 | `core.shell` | `shell.ShellDetector` | 0.85 | 4 | 0.6 |

## Critical Issues (Similarity < 0.60 with Dependencies)

No critical issues with dependencies.

