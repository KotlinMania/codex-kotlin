# Code Port - Progress Report

**Generated:** 2026-03-30
**Source:** codex-rs
**Target:** src

## Executive Summary

| Metric | Count | Percentage |
|--------|-------|------------|
| Total source files | 444 | 100% |
| Ported to target | 148 | 33.3% |
| Matched files | 143 | 32.2% |
| Missing files | 301 | 67.8% |

## Port Quality Analysis

**Average Similarity:** 0.75

**Quality Distribution:**
- Excellent (≥0.85): 30 files (21.0% of matched)
- Good (0.60-0.84): 98 files (68.5% of matched)
- Critical (<0.60): 15 files (10.5% of matched)

### Excellent Ports (Similarity ≥ 0.85)

These files are well-ported and likely complete:

- `error.CodexError` (0.85, 50 deps)
- `otel.Config` (0.87, 45 deps)
- `render.Renderable` (0.87, 19 deps)
- `client.Request` (0.85, 9 deps)
- `tui.KeyHint` (0.88, 11 deps)
- `execpolicy.Policy` (0.89, 7 deps)
- `provider.Provider` (0.89, 7 deps)
- `rmcpclient.FindCodexHome` (0.86, 5 deps)
- `execpolicy.Decision` (0.87, 5 deps)
- `core.TokenData` (0.88, 5 deps)
- `legacy.ValidExec` (0.87, 4 deps)
- `client.ModelClient` (0.89, 4 deps)
- `models.PaginatedListTaskListItem` (0.91, 4 deps)
- `models.TaskListItem` (0.90, 3 deps)
- `tui.SlashCommand` (0.91, 3 deps)

### Critical Ports (Similarity < 0.60)

These files need significant work:

- `core.turn_diff_tracker` → `session.TurnDiffTrackerExpect` (0.41, 2 deps)
- `windows-sandbox-rs.token` → `auth.IdTokenParser` (0.43, 2 deps)
- `core.mcp_connection_manager` → `connection.McpConnectionManager` (0.60, 2 deps)
- `bottom_pane.approval_overlay` → `bottom_pane.ApprovalRequest` (0.52, 1 deps)
- `bottom_pane.mod` → `bottom_pane.CancellationEvent` (0.30)
- `sandboxing.mod` → `sandboxing.SandboxPermissions` (0.47)
- `context_manager.mod` → `context.ContextManager` (0.46)
- `config.mod` → `config.Config` (0.51)
- `protocol.mod` → `config.ConfigToml` (0.49)
- `endpoint.mod` → `process.SandboxType` (0.55)
- `tools.spec` → `prompt.ToolSpec` (0.50)
- `core.bash` → `bash.BashParser` (0.42)
- `render.line_utils` → `utils.Environment` (0.49)
- `tui.update_prompt` → `prompt.Constants` (0.49)
- `tui.oss_selection` → `sse.SseEvent` (0.49)

## High Priority Missing Files

Files with highest dependency counts:

1. **execpolicy-legacy.policy_parser** (6 deps)
2. **bottom_pane.textarea** (3 deps)
3. **core.parse_command** (3 deps)
4. **models.rate_limit_window_snapshot** (3 deps)
5. **models.rate_limit_status_payload** (3 deps)
6. **models.credit_status_details** (3 deps)
7. **models.code_task_details_response** (3 deps)
8. **exec.event_processor** (3 deps)
9. **public_widgets.composer_input** (3 deps)
10. **mcp-server.outgoing_message** (3 deps)
11. **tui.app** (3 deps)
12. **core.apply_patch** (2 deps)
13. **rmcp-client.rmcp_client** (2 deps)
14. **common.format_env_display** (2 deps)
15. **common.approval_mode_cli_arg** (2 deps)
16. **tui.chatwidget** (2 deps)
17. **models.rate_limit_status_details** (2 deps)
18. **core.user_instructions** (2 deps)
19. **posix.escalation_policy** (2 deps)
20. **posix.stopwatch** (2 deps)

## Documentation Gaps

**Documentation coverage:** 3462 / 3816 lines (91%)

Files with significant documentation gaps (>80%):

- `protocol.protocol` - 96% gap (512 → 18 lines)
- `config.mod` - 99% gap (400 → 5 lines)
- `tui.custom_terminal` - 100% gap (246 → 0 lines)
- `apply-patch.parser` - 91% gap (164 → 15 lines)
- `config.types` - 84% gap (128 → 21 lines)
- `core.git_info` - 96% gap (92 → 4 lines)
- `mcp-server.codex_tool_config` - 100% gap (72 → 0 lines)
- `protocol.common` - 100% gap (70 → 0 lines)
- `bottom_pane.mod` - 94% gap (48 → 3 lines)
- `readiness.lib` - 100% gap (38 → 0 lines)

