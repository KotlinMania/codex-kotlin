# Code Port - Progress Report

**Generated:** 2026-01-30
**Source:** codex-rs
**Target:** src

## Executive Summary

| Metric | Count | Percentage |
|--------|-------|------------|
| Total source files | 444 | 100% |
| Ported to target | 153 | 34.5% |
| Matched files | 141 | 31.8% |
| Missing files | 303 | 68.2% |

## Port Quality Analysis

**Average Similarity:** 0.78

**Quality Distribution:**
- Excellent (≥0.85): 45 files (31.9% of matched)
- Good (0.60-0.84): 82 files (58.2% of matched)
- Critical (<0.60): 14 files (9.9% of matched)

### Excellent Ports (Similarity ≥ 0.85)

These files are well-ported and likely complete:

- `error.CodexError` (0.89, 50 deps)
- `otel.Config` (0.89, 45 deps)
- `protocol.ConversationId` (0.86, 25 deps)
- `render.Renderable` (0.90, 19 deps)
- `tools.Context` (0.90, 19 deps)
- `model.ModelFamily` (0.88, 5 deps)
- `features.Features` (0.88, 5 deps)
- `provider.Provider` (0.92, 7 deps)
- `session.Codex` (0.90, 3 deps)
- `client.ModelClient` (0.93, 4 deps)
- `protocol.Protocol` (0.88, 2 deps)
- `session.TurnContextTest` (0.90, 1 deps)
- `linuxX64Main.kotlin.ai.solace.coder.utils.git.GhostCommitsPlatform` (0.90, 1 deps)
- `readiness.ReadinessFlagTest` (0.89, 0 deps)
- `protocol.NumFormat` (0.89, 0 deps)

### Critical Ports (Similarity < 0.60)

These files need significant work:

- `config.mod` → `config.Config` (0.52)
- `core.client_common` → `prompt.Prompt` (0.58)
- `linux-sandbox.main` → `kotlin.Main` (0.52)
- `context_manager.mod` → `context.ContextManager` (0.50)
- `command_safety.mod` → `config.ConfigToml` (0.52)
- `streaming.mod` → `process.SandboxType` (0.41)
- `config_loader.mod` → `sandboxing.SandboxPermissions` (0.58)
- `apply-patch.parser` → `bash.BashParser` (0.53)
- `tui.oss_selection` → `sse.SseEvent` (0.53)
- `rmcp-client.auth_status` → `auth.IdTokenParser` (0.55)
- `rmcp-client.utils` → `utils.Environment` (0.52)
- `handlers.mod` → `handlers.ReadFileTest` (0.51)
- `sse.mod` → `sse.OkioEventsource` (0.51)
- `endpoint.mod` → `endpoint.Streaming` (0.49)

## High Priority Missing Files

Files with highest dependency counts:

1. **core.terminal** (13 deps)
2. **tui.app_event_sender** (12 deps)
3. **tui.history_cell** (11 deps)
4. **tui.key_hint** (11 deps)
5. **core.conversation_manager** (9 deps)
6. **tui.tui** (9 deps)
7. **codex-client.request** (9 deps)
8. **core.codex_conversation** (8 deps)
9. **execpolicy-legacy.exec_call** (8 deps)
10. **execpolicy.policy** (7 deps)
11. **tui.cli** (7 deps)
12. **execpolicy-legacy.arg_type** (6 deps)
13. **execpolicy-legacy.policy_parser** (6 deps)
14. **execpolicy.decision** (5 deps)
15. **execpolicy-legacy.arg_matcher** (5 deps)
16. **tui.update_action** (5 deps)
17. **core.token_data** (5 deps)
18. **rmcp-client.find_codex_home** (5 deps)
19. **bottom_pane.bottom_pane_view** (5 deps)
20. **execpolicy-legacy.valid_exec** (4 deps)

## Documentation Gaps

**Documentation coverage:** 3489 / 3390 lines (103%)

Files with significant documentation gaps (>80%):

- `protocol.protocol` - 96% gap (512 → 18 lines)
- `config.mod` - 99% gap (400 → 5 lines)
- `apply-patch.parser` - 90% gap (164 → 17 lines)
- `config.types` - 100% gap (128 → 0 lines)
- `core.git_info` - 96% gap (92 → 4 lines)
- `mcp-server.codex_tool_config` - 100% gap (72 → 0 lines)
- `tui.chatwidget` - 95% gap (74 → 4 lines)
- `rmcp-client.oauth` - 100% gap (52 → 0 lines)
- `unified_exec.mod` - 100% gap (44 → 0 lines)
- `tools.spec` - 96% gap (46 → 2 lines)

