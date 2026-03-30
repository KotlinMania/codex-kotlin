# High Priority Ports - Action Plan

## Top 20 Files by Impact (Priority Score = Deps × (1 - Similarity))

| Rank | Source | Target | Similarity | Deps | Priority |
|------|--------|--------|------------|------|----------|
| 1 | `core.terminal` | `core.Terminal` | 0.59 | 24 | 9.9 |
| 2 | `core.error` | `error.CodexError` | 0.85 | 65 | 9.7 |
| 3 | `otel.config` | `otel.Config` | 0.87 | 62 | 8.0 |
| 4 | `tui.style` | `tui.Style` | 0.79 | 37 | 7.7 |
| 5 | `ollama.parser` | `ollama.Parser` | 0.73 | 26 | 6.9 |
| 6 | `tui.color` | `color.Color` | 0.82 | 36 | 6.5 |
| 7 | `tui.history_cell` | `tui.HistoryCell` | 0.77 | 28 | 6.3 |
| 8 | `protocol.conversation_id` | `protocol.ConversationId` | 0.79 | 27 | 5.7 |
| 9 | `tools.context` | `tools.Context` | 0.80 | 28 | 5.5 |
| 10 | `tui.app_event_sender` | `tui.AppEventSender` | 0.78 | 25 | 5.5 |
| 11 | `render.renderable` | `render.Renderable` | 0.87 | 40 | 5.4 |
| 12 | `core.env` | `utils.Environment` | 0.65 | 15 | 5.3 |
| 13 | `tui.app_event` | `tui.AppEvent` | 0.82 | 29 | 5.1 |
| 14 | `tui.tui` | `tui.Tui` | 0.73 | 19 | 5.1 |
| 15 | `protocol.user_input` | `protocol.UserInput` | 0.79 | 22 | 4.7 |
| 16 | `state.session` | `state.SessionState` | 0.78 | 20 | 4.4 |
| 17 | `tui.key_hint` | `tui.KeyHint` | 0.87 | 25 | 3.3 |
| 18 | `core.conversation_manager` | `conversation.ConversationManager` | 0.75 | 13 | 3.3 |
| 19 | `bottom_pane.scroll_state` | `bottom_pane.ScrollState` | 0.79 | 12 | 2.5 |
| 20 | `core.model_provider_info` | `model.ModelProviderInfo` | 0.74 | 9 | 2.4 |

## Critical Issues (Similarity < 0.60 with Dependencies)

These files need immediate attention:

- **core.terminal** → `core.Terminal`
  - Similarity: 0.59
  - Dependencies: 24

- **core.mcp_connection_manager** → `connection.McpConnectionManager`
  - Similarity: 0.59
  - Dependencies: 3
  - TODOs: 3
  - Lint issues: 7

- **core.turn_diff_tracker** → `session.TurnDiffTrackerExpect`
  - Similarity: 0.41
  - Dependencies: 2

- **windows-sandbox-rs.token** → `auth.IdTokenParser`
  - Similarity: 0.43
  - Dependencies: 2

- **bottom_pane.custom_prompt_view** → `prompt.Constants`
  - Similarity: 0.49
  - Dependencies: 2

- **chatwidget.session_header** → `session.CodexTest`
  - Similarity: 0.50
  - Dependencies: 2

- **bottom_pane.approval_overlay** → `bottom_pane.ApprovalRequest`
  - Similarity: 0.52
  - Dependencies: 2

