# Code Port - Progress Report

**Generated:** 2026-01-30
**Source:** codex-rs
**Target:** src

## Executive Summary

| Metric | Count | Percentage |
|--------|-------|------------|
| Total source files | 444 | 100% |
| Ported to target | 163 | 36.7% |
| Matched files | 150 | 33.8% |
| Missing files | 294 | 66.2% |

## Port Quality Analysis

**Average Similarity:** 0.76

**Quality Distribution:**
- Excellent (≥0.85): 40 files (26.7% of matched)
- Good (0.60-0.84): 90 files (60.0% of matched)
- Critical (<0.60): 20 files (13.3% of matched)

### Excellent Ports (Similarity ≥ 0.85)

These files are well-ported and likely complete:

- `error.CodexError` (0.89, 50 deps)
- `otel.Config` (0.89, 45 deps)
- `protocol.ConversationId` (0.86, 25 deps)
- `render.Renderable` (0.90, 19 deps)
- `tools.Context` (0.90, 19 deps)
- `legacy.ExecCall` (0.85, 8 deps)
- `conversation.CodexConversation` (0.87, 8 deps)
- `tui.KeyHint` (0.94, 11 deps)
- `model.ModelFamily` (0.88, 5 deps)
- `features.Features` (0.88, 5 deps)
- `provider.Provider` (0.92, 7 deps)
- `session.Codex` (0.87, 3 deps)
- `protocol.Protocol` (0.88, 2 deps)
- `session.TurnContextTest` (0.90, 1 deps)
- `readiness.ReadinessFlagTest` (0.89, 0 deps)

### Critical Ports (Similarity < 0.60)

These files need significant work:

- `core.terminal` → `terminal.Terminal.native` (0.55, 13 deps)
- `codex-client.request` → `requests.ResponsesRequest` (0.57, 9 deps)
- `core.parse_command` → `protocol.ParseCommand` (0.50, 3 deps)
- `app-server.codex_message_processor` → `process.WindowsProcess` (0.59, 1 deps)
- `responses-api-proxy.main` → `kotlin.Main` (0.57)
- `protocol.mod` → `config.ConfigToml` (0.52)
- `context_manager.mod` → `context.ContextManager` (0.50)
- `tools.spec` → `tools.ToolSpec` (0.56)
- `core.client_common` → `prompt.Prompt` (0.58)
- `endpoint.mod` → `sandboxing.SandboxPermissions` (0.56)
- `core.custom_prompts` → `protocol.CustomPrompts` (0.55)
- `core.message_history` → `protocol.MessageHistory` (0.56)
- `app-server.models` → `protocol.Models` (0.57)
- `app-server-protocol.lib` → `api.lib` (0.52)
- `sse.mod` → `sandbox.SandboxManager` (0.49)
- `tasks.compact` → `endpoint.Compact` (0.60)
- `execpolicy.parser` → `bash.BashParser` (0.53)
- `render.line_utils` → `utils.Environment` (0.57)
- `rmcp-client.auth_status` → `auth.IdTokenParser` (0.55)
- `handlers.mod` → `handlers.ReadFileTest` (0.51)

## High Priority Missing Files

Files with highest dependency counts:

1. **execpolicy.policy** (7 deps)
2. **execpolicy-legacy.policy_parser** (6 deps)
3. **execpolicy-legacy.arg_type** (6 deps)
4. **tui.update_action** (5 deps)
5. **core.token_data** (5 deps)
6. **rmcp-client.find_codex_home** (5 deps)
7. **execpolicy.decision** (5 deps)
8. **bottom_pane.bottom_pane_view** (5 deps)
9. **execpolicy-legacy.arg_matcher** (5 deps)
10. **bottom_pane.scroll_state** (4 deps)
11. **ollama.url** (4 deps)
12. **otel.otel_event_manager** (4 deps)
13. **core.client** (4 deps)
14. **execpolicy-legacy.valid_exec** (4 deps)
15. **models.paginated_list_task_list_item_** (4 deps)
16. **mcp-server.outgoing_message** (3 deps)
17. **bottom_pane.textarea** (3 deps)
18. **exec.event_processor** (3 deps)
19. **core.exec_env** (3 deps)
20. **execpolicy-legacy.opt** (3 deps)

## Documentation Gaps

**Documentation coverage:** 3907 / 3358 lines (116%)

Files with significant documentation gaps (>80%):

- `protocol.protocol` - 96% gap (512 → 18 lines)
- `tui.custom_terminal` - 80% gap (246 → 48 lines)
- `apply-patch.parser` - 100% gap (164 → 0 lines)
- `config.types` - 96% gap (128 → 5 lines)
- `core.git_info` - 96% gap (92 → 4 lines)
- `mcp-server.codex_tool_config` - 100% gap (72 → 0 lines)
- `core.message_history` - 92% gap (62 → 5 lines)
- `rmcp-client.oauth` - 100% gap (52 → 0 lines)
- `unified_exec.mod` - 100% gap (44 → 0 lines)
- `tasks.mod` - 90% gap (48 → 5 lines)

