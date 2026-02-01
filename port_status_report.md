# Code Port - Progress Report

**Generated:** 2026-02-01
**Source:** codex-rs
**Target:** src

## Executive Summary

| Metric | Count | Percentage |
|--------|-------|------------|
| Total source files | 444 | 100% |
| Ported to target | 116 | 26.1% |
| Matched files | 113 | 25.5% |
| Missing files | 331 | 74.5% |

## Port Quality Analysis

**Average Similarity:** 0.74

**Quality Distribution:**
- Excellent (≥0.85): 16 files (14.2% of matched)
- Good (0.60-0.84): 82 files (72.6% of matched)
- Critical (<0.60): 15 files (13.3% of matched)

### Excellent Ports (Similarity ≥ 0.85)

These files are well-ported and likely complete:

- `error.CodexError` (0.85, 50 deps)
- `render.Renderable` (0.86, 19 deps)
- `core.Terminal` (0.88, 13 deps)
- `provider.Provider` (0.89, 7 deps)
- `execpolicy.Policy` (0.89, 7 deps)
- `rmcpclient.FindCodexHome` (0.86, 5 deps)
- `execpolicy.Decision` (0.87, 5 deps)
- `client.ModelClient` (0.89, 4 deps)
- `protocol.Protocol` (0.87, 2 deps)
- `endpoint.Responses` (0.87, 0 deps)
- `protocol.Approvals` (0.86, 0 deps)
- `ratelimits.RateLimits` (0.87, 0 deps)
- `endpoint.Chat` (0.89, 0 deps)
- `command_safety.WindowsDangerousCommands` (0.89, 0 deps)
- `sandboxing.Assessment` (0.87, 0 deps)

### Critical Ports (Similarity < 0.60)

These files need significant work:

- `tui.history_cell` → `tui.HistoryCell` (0.47, 11 deps)
- `core.features` → `features.FeaturesExpect` (0.48, 5 deps)
- `core.turn_diff_tracker` → `session.TurnDiffTrackerExpect` (0.41, 2 deps)
- `windows-sandbox-rs.token` → `auth.IdTokenParser` (0.43, 2 deps)
- `core.mcp_connection_manager` → `connection.McpConnectionManager` (0.60, 2 deps)
- `core.environment_context` → `utils.Environment` (0.46, 1 deps)
- `core.bash` → `bash.BashParser` (0.42)
- `config.mod` → `config.Config` (0.51)
- `context_manager.mod` → `context.ContextManager` (0.46)
- `sandboxing.mod` → `sandboxing.SandboxPermissions` (0.47)
- `tool_handlers.mod` → `process.SandboxType` (0.55)
- `tui.terminal_palette` → `terminal.Terminal` (0.51)
- `tools.spec` → `prompt.ToolSpec` (0.50)
- `tui.update_prompt` → `prompt.Constants` (0.49)
- `tui.oss_selection` → `sse.SseEvent` (0.49)

## High Priority Missing Files

Files with highest dependency counts:

1. **tui.cli** (7 deps)
2. **execpolicy-legacy.policy_parser** (6 deps)
3. **bottom_pane.bottom_pane_view** (5 deps)
4. **tui.update_action** (5 deps)
5. **core.token_data** (5 deps)
6. **execpolicy-legacy.arg_matcher** (5 deps)
7. **models.paginated_list_task_list_item_** (4 deps)
8. **otel.otel_event_manager** (4 deps)
9. **execpolicy-legacy.valid_exec** (4 deps)
10. **bottom_pane.scroll_state** (4 deps)
11. **tui.app** (3 deps)
12. **models.code_task_details_response** (3 deps)
13. **models.task_list_item** (3 deps)
14. **models.rate_limit_window_snapshot** (3 deps)
15. **public_widgets.composer_input** (3 deps)
16. **models.rate_limit_status_payload** (3 deps)
17. **core.exec_env** (3 deps)
18. **tui.slash_command** (3 deps)
19. **exec.event_processor** (3 deps)
20. **mcp-server.outgoing_message** (3 deps)

## Documentation Gaps

**Documentation coverage:** 2626 / 3332 lines (79%)

Files with significant documentation gaps (>80%):

- `protocol.protocol` - 96% gap (512 → 18 lines)
- `config.mod` - 99% gap (400 → 5 lines)
- `tui.custom_terminal` - 100% gap (246 → 0 lines)
- `apply-patch.parser` - 91% gap (164 → 15 lines)
- `config.types` - 100% gap (128 → 0 lines)
- `mcp-server.codex_tool_config` - 100% gap (72 → 0 lines)
- `core.features` - 85% gap (54 → 8 lines)
- `tools.sandboxing` - 100% gap (42 → 0 lines)
- `readiness.lib` - 100% gap (38 → 0 lines)
- `tui.history_cell` - 86% gap (42 → 6 lines)

