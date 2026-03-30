# Code Port - Progress Report

**Generated:** 2026-03-30
**Source:** tmp/codex/codex-rs
**Target:** src

## Executive Summary

| Metric | Count | Percentage |
|--------|-------|------------|
| Total source files | 613 | 100% |
| Ported to target | 136 | 22.2% |
| Matched files | 134 | 21.9% |
| Missing files | 479 | 78.1% |

## Port Quality Analysis

**Average Similarity:** 0.75

**Quality Distribution:**
- Excellent (≥0.85): 25 files (18.7% of matched)
- Good (0.60-0.84): 92 files (68.7% of matched)
- Critical (<0.60): 17 files (12.7% of matched)

### Excellent Ports (Similarity ≥ 0.85)

These files are well-ported and likely complete:

- `error.CodexError` (0.85, 65 deps)
- `otel.Config` (0.87, 62 deps)
- `render.Renderable` (0.87, 40 deps)
- `tui.KeyHint` (0.87, 25 deps)
- `features.FeaturesExpect` (0.85, 13 deps)
- `client.Request` (0.85, 10 deps)
- `rmcpclient.FindCodexHome` (0.86, 8 deps)
- `execpolicy.Decision` (0.87, 7 deps)
- `provider.Provider` (0.89, 8 deps)
- `core.TokenData` (0.88, 5 deps)
- `legacy.ValidExec` (0.87, 4 deps)
- `client.ModelClient` (0.89, 4 deps)
- `models.PaginatedListTaskListItem` (0.91, 4 deps)
- `models.TaskListItem` (0.90, 3 deps)
- `git.GhostCommitsTest` (0.85, 2 deps)

### Critical Ports (Similarity < 0.60)

These files need significant work:

- `core.terminal` → `core.Terminal` (0.59, 24 deps)
- `core.mcp_connection_manager` → `connection.McpConnectionManager` (0.59, 3 deps)
- `core.turn_diff_tracker` → `session.TurnDiffTrackerExpect` (0.41, 2 deps)
- `windows-sandbox-rs.token` → `auth.IdTokenParser` (0.43, 2 deps)
- `bottom_pane.custom_prompt_view` → `prompt.Constants` (0.49, 2 deps)
- `chatwidget.session_header` → `session.CodexTest` (0.50, 2 deps)
- `bottom_pane.approval_overlay` → `bottom_pane.ApprovalRequest` (0.52, 2 deps)
- `core.bash` → `bash.BashParser` (0.39)
- `config.mod` → `config.Config` (0.51)
- `sandboxing.mod` → `sandboxing.SandboxPermissions` (0.46)
- `context_manager.mod` → `context.ContextManager` (0.46)
- `protocol.mod` → `config.ConfigToml` (0.50)
- `bottom_pane.mod` → `bottom_pane.CancellationEvent` (0.30)
- `endpoint.mod` → `process.SandboxType` (0.57)
- `tools.spec` → `prompt.ToolSpec` (0.50)
- `sse.mod` → `sandboxing.Assessment` (0.45)
- `tui.oss_selection` → `sse.SseEvent` (0.49)

## High Priority Missing Files

Files with highest dependency counts:

1. **tui.frame_requester** (22 deps)
2. **config.constraint** (12 deps)
3. **common.fuzzy_match** (6 deps)
4. **execpolicy-legacy.policy_parser** (6 deps)
5. **bottom_pane.textarea** (6 deps)
6. **tui.slash_command** (6 deps)
7. **tui.app** (5 deps)
8. **config_loader.config_requirements** (5 deps)
9. **bottom_pane.chat_composer** (5 deps)
10. **tui2.transcript_selection** (4 deps)
11. **tui.chatwidget** (4 deps)
12. **public_widgets.composer_input** (4 deps)
13. **mcp-server.outgoing_message** (3 deps)
14. **otel.otel_manager** (3 deps)
15. **models.code_task_details_response** (3 deps)
16. **models.credit_status_details** (3 deps)
17. **core.parse_command** (3 deps)
18. **cli.exit_status** (3 deps)
19. **common.approval_mode_cli_arg** (3 deps)
20. **core.path_utils** (3 deps)

## Documentation Gaps

**Documentation coverage:** 3206 / 4558 lines (70%)

Files with significant documentation gaps (>80%):

- `protocol.protocol` - 97% gap (568 → 18 lines)
- `config.mod` - 99% gap (506 → 5 lines)
- `config.types` - 93% gap (318 → 21 lines)
- `tui.custom_terminal` - 100% gap (246 → 0 lines)
- `tui2.custom_terminal` - 80% gap (246 → 48 lines)
- `apply-patch.parser` - 91% gap (164 → 15 lines)
- `core.terminal` - 93% gap (150 → 10 lines)
- `mcp-server.codex_tool_config` - 100% gap (72 → 0 lines)
- `rmcp-client.oauth` - 100% gap (52 → 0 lines)
- `bottom_pane.mod` - 94% gap (52 → 3 lines)

