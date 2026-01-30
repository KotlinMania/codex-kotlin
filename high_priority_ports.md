# High Priority Ports - Action Plan

## Top 20 Files by Impact (Priority Score = Deps × (1 - Similarity))

| Rank | Source | Target | Similarity | Deps | Priority |
|------|--------|--------|------------|------|----------|
| 1 | `core.terminal` | `terminal.Terminal.native` | 0.55 | 13 | 5.8 |
| 2 | `core.error` | `error.CodexError` | 0.89 | 50 | 5.6 |
| 3 | `ollama.parser` | `ollama.Parser` | 0.76 | 23 | 5.5 |
| 4 | `otel.config` | `otel.Config` | 0.89 | 45 | 5.1 |
| 5 | `protocol.user_input` | `protocol.UserInput` | 0.75 | 20 | 5.0 |
| 6 | `tui.style` | `tui.Style` | 0.75 | 16 | 4.1 |
| 7 | `codex-client.request` | `requests.ResponsesRequest` | 0.57 | 9 | 3.9 |
| 8 | `protocol.conversation_id` | `protocol.ConversationId` | 0.86 | 25 | 3.4 |
| 9 | `tui.history_cell` | `tui.HistoryCell` | 0.71 | 11 | 3.2 |
| 10 | `tui.app_event` | `tui.AppEvent` | 0.77 | 14 | 3.2 |
| 11 | `state.session` | `state.SessionState` | 0.83 | 18 | 3.1 |
| 12 | `protocol.models` | `auth.AuthModels` | 0.61 | 7 | 2.8 |
| 13 | `tui.tui` | `tui.Tui` | 0.69 | 9 | 2.8 |
| 14 | `tui.color` | `tui.Color` | 0.82 | 15 | 2.7 |
| 15 | `tui.app_event_sender` | `tui.AppEventSender` | 0.83 | 12 | 2.1 |
| 16 | `render.renderable` | `render.Renderable` | 0.90 | 19 | 2.0 |
| 17 | `tools.context` | `tools.Context` | 0.90 | 19 | 1.9 |
| 18 | `core.model_provider_info` | `model.ModelProviderInfo` | 0.75 | 7 | 1.7 |
| 19 | `core.conversation_manager` | `conversation.ConversationManager` | 0.83 | 9 | 1.6 |
| 20 | `core.parse_command` | `protocol.ParseCommand` | 0.50 | 3 | 1.5 |

## Critical Issues (Similarity < 0.60 with Dependencies)

These files need immediate attention:

- **core.terminal** → `terminal.Terminal.native`
  - Similarity: 0.55
  - Dependencies: 13

- **codex-client.request** → `requests.ResponsesRequest`
  - Similarity: 0.57
  - Dependencies: 9
  - TODOs: 5
  - Lint issues: 1

- **core.parse_command** → `protocol.ParseCommand`
  - Similarity: 0.50
  - Dependencies: 3

- **app-server.codex_message_processor** → `process.WindowsProcess`
  - Similarity: 0.59
  - Dependencies: 1
  - Lint issues: 5

