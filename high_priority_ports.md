# High Priority Ports - Action Plan

## Top 20 Files by Impact (Priority Score = Deps × (1 - Similarity))

| Rank | Source | Target | Similarity | Deps | Priority |
|------|--------|--------|------------|------|----------|
| 1 | `core.error` | `error.CodexError` | 0.85 | 50 | 7.4 |
| 2 | `ollama.parser` | `ollama.Parser` | 0.73 | 23 | 6.1 |
| 3 | `protocol.user_input` | `protocol.UserInput` | 0.71 | 20 | 5.9 |
| 4 | `otel.config` | `otel.Config` | 0.87 | 45 | 5.8 |
| 5 | `protocol.conversation_id` | `protocol.ConversationId` | 0.79 | 25 | 5.3 |
| 6 | `core.terminal` | `terminal.TerminalTest` | 0.62 | 13 | 4.9 |
| 7 | `state.session` | `state.SessionState` | 0.79 | 18 | 3.9 |
| 8 | `tools.context` | `tools.Context` | 0.80 | 19 | 3.8 |
| 9 | `tui.style` | `tui.Style` | 0.79 | 16 | 3.3 |
| 10 | `tui.app_event` | `tui.AppEvent` | 0.80 | 14 | 2.9 |
| 11 | `tui.color` | `color.Color` | 0.82 | 15 | 2.7 |
| 12 | `tui.tui` | `tui.Tui` | 0.70 | 9 | 2.7 |
| 13 | `tui.app_event_sender` | `tui.AppEventSender` | 0.78 | 12 | 2.6 |
| 14 | `render.renderable` | `render.Renderable` | 0.87 | 19 | 2.6 |
| 15 | `tui.history_cell` | `tui.HistoryCell` | 0.80 | 11 | 2.2 |
| 16 | `core.model_provider_info` | `model.ModelProviderInfo` | 0.72 | 7 | 1.9 |
| 17 | `core.conversation_manager` | `conversation.ConversationManager` | 0.79 | 9 | 1.9 |
| 18 | `windows-sandbox-rs.env` | `command_safety.WindowsEnvironment` | 0.79 | 8 | 1.7 |
| 19 | `tui.cli` | `tui.Cli` | 0.76 | 7 | 1.7 |
| 20 | `execpolicy-legacy.exec_call` | `legacy.ExecCall` | 0.80 | 8 | 1.6 |

## Critical Issues (Similarity < 0.60 with Dependencies)

These files need immediate attention:

- **core.turn_diff_tracker** → `session.TurnDiffTrackerExpect`
  - Similarity: 0.41
  - Dependencies: 2

- **windows-sandbox-rs.token** → `auth.IdTokenParser`
  - Similarity: 0.43
  - Dependencies: 2

- **core.mcp_connection_manager** → `connection.McpConnectionManager`
  - Similarity: 0.60
  - Dependencies: 2
  - TODOs: 3
  - Lint issues: 7

- **bottom_pane.approval_overlay** → `bottom_pane.ApprovalRequest`
  - Similarity: 0.52
  - Dependencies: 1

