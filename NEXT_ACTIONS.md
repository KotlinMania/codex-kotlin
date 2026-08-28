# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 115/1756 (6.5%)
- **Function parity:** 302/24552 matched (target 825) — 1.2%
- **Class/type parity:** 188/4246 matched (target 800) — 4.4%
- **Combined symbol parity:** 490/28798 matched (target 1625) — 1.7%
- **Average inline-code cosine:** 0.21 (function body across 106 matched files)
- **Average documentation cosine:** 0.30 (doc text across 106 matched files)
- **Cheat-zeroed Files:** 29
- **Critical Issues:** 103 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. protocol.user_input
- **Similarity:** 0.00 (needs 85% improvement)
- **Dependencies:** 114
- **Priority Score:** 114080904.0
- **Functions:** 0/6 matched (target 0)
- **Missing functions:** `new`, `map_range`, `set_placeholder`, `_placeholder_for_conversion_only`, `placeholder`, `from`
- **Types:** 1/3 matched (target 4)
- **Missing types:** `TextElement`, `ByteRange`
- **Symbol Deficit:** 8 (functions: 6, types: 2)
- **Action:** Deep review - likely missing major functionality

### 2. tools.context
- **Similarity:** 0.05 (needs 80% improvement)
- **Dependencies:** 113
- **Priority Score:** 113202808.0
- **Functions:** 4/16 matched (target 10)
- **Missing functions:** `post_tool_use_response`, `code_mode_result`, `to_response_item`, `response_payload`, `from_text`, `from_content`, `into_text`, `truncated_output`, `response_text`, `response_input_to_code_mode_result`, `content_items_to_code_mode_result`, `function_tool_response`
- **Types:** 4/12 matched (target 9)
- **Missing types:** `ToolCallSource`, `McpToolOutput`, `ToolSearchOutput`, `FunctionToolOutput`, `ApplyPatchToolOutput`, `AbortedToolOutput`, `ExecCommandToolOutput`, `UnifiedExecCodeModeResult`
- **Symbol Deficit:** 20 (functions: 12, types: 8)
- **Action:** Deep review - likely missing major functionality

### 3. network-proxy.responses
- **Similarity:** 0.00 (needs 85% improvement)
- **Dependencies:** 51
- **Priority Score:** 51090912.0
- **Functions:** 0/8 matched (target 4)
- **Missing functions:** `text_response`, `json_response`, `blocked_header_value`, `blocked_message`, `blocked_text_response`, `blocked_message_with_policy`, `blocked_text_response_with_policy`, `blocked_message_with_policy_returns_human_message`
- **Types:** 0/1 matched (target 2)
- **Missing types:** `PolicyDecisionDetails`
- **Symbol Deficit:** 9 (functions: 8, types: 1)
- **Missing Tests:** 1 of 1 `#[test]` functions have no Kotlin counterpart
- **Action:** Deep review - likely missing major functionality

### 4. state.session
- **Similarity:** 0.26 (needs 59% improvement)
- **Dependencies:** 48
- **Priority Score:** 48233408.0
- **Functions:** 10/33 matched (target 10)
- **Missing functions:** `new`, `previous_turn_settings`, `set_previous_turn_settings`, `set_next_turn_is_first`, `take_next_turn_is_first`, `set_reference_context_item`, `reference_context_item`, `set_server_reasoning_included`, `server_reasoning_included`, `record_mcp_dependency_prompted`, `mcp_dependency_prompted`, `set_dependency_env`, `dependency_env`, `set_session_startup_prewarm`, `take_session_startup_prewarm`, `merge_connector_selection`, `get_connector_selection`, `clear_connector_selection`, `set_pending_session_start_source`, `take_pending_session_start_source`, `record_granted_permissions`, `granted_permissions`, `merge_rate_limit_fields`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Symbol Deficit:** 23 (functions: 23, types: 0)
- **Action:** Deep review - likely missing major functionality

### 5. render.renderable
- **Similarity:** 0.70 (needs 15% improvement)
- **Dependencies:** 41
- **Priority Score:** 41021804.0
- **Functions:** 9/10 matched (target 48)
- **Missing functions:** `cursor_style`
- **Types:** 7/8 matched (target 15)
- **Missing types:** `RenderableExt`
- **Symbol Deficit:** 2 (functions: 1, types: 1)
- **Action:** Review and complete missing sections

### 6. tui.app_event
- **Similarity:** 0.69 (needs 16% improvement)
- **Dependencies:** 36
- **Priority Score:** 36021304.0
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 9/11 matched (target 70)
- **Missing types:** `ConnectorsSnapshot`, `RealtimeWebrtcOffer`
- **Symbol Deficit:** 2 (functions: 0, types: 2)
- **Action:** Review and complete missing sections

### 7. tests.features
- **Similarity:** 0.00 (needs 85% improvement)
- **Dependencies:** 33
- **Priority Score:** 33050510.0
- **Functions:** 0/5 matched (target 14)
- **Missing functions:** `codex_command`, `features_enable_writes_feature_flag_to_config`, `features_disable_writes_feature_flag_to_config`, `features_enable_under_development_feature_prints_warning`, `features_list_is_sorted_alphabetically_by_feature_name`
- **Types:** 0/0 matched (target 5)
- **Missing types:** _none_
- **Symbol Deficit:** 5 (functions: 5, types: 0)
- **Action:** Deep review - likely missing major functionality

### 8. ollama.parser
- **Similarity:** 0.22 (needs 63% improvement)
- **Dependencies:** 32
- **Priority Score:** 32020308.0
- **Functions:** 1/3 matched (target 1)
- **Missing functions:** `test_pull_events_decoder_status_and_success`, `test_pull_events_decoder_progress`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Symbol Deficit:** 2 (functions: 2, types: 0)
- **Missing Tests:** 2 of 2 `#[test]` functions have no Kotlin counterpart
- **Action:** Deep review - likely missing major functionality

### 9. tui.key_hint
- **Similarity:** 0.29 (needs 56% improvement)
- **Dependencies:** 27
- **Priority Score:** 27203108.0
- **Functions:** 10/29 matched (target 11)
- **Missing functions:** `from_event`, `parts`, `display_label`, `normalize_key_parts`, `c0_control_char_to_ctrl_char`, `is_pressed`, `ctrl_alt`, `from`, `is_press_accepts_press_and_repeat_but_rejects_release`, `keybinding_list_ext_matches_any_binding`, `shifted_letter_binding_matches_uppercase_char_events`, `shift_letter_binding_preserves_other_modifiers_with_uppercase_compat`, `shift_letter_binding_does_not_match_plain_lowercase_or_other_uppercase`, `ctrl_letter_binding_matches_c0_control_char_events`, `ctrl_bindings_match_all_supported_c0_control_char_events`, `ctrl_binding_does_not_match_ambiguous_c0_escape_or_delete`, `history_search_ctrl_bindings_match_c0_control_char_events`, `ctrl_alt_sets_both_modifiers`, `has_ctrl_or_alt_checks_supported_modifier_combinations`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `KeyBindingListExt`
- **Symbol Deficit:** 20 (functions: 19, types: 1)
- **Missing Tests:** 11 of 11 `#[test]` functions have no Kotlin counterpart
- **Action:** Deep review - likely missing major functionality

### 10. tui.app_event_sender
- **Similarity:** 0.06 (needs 79% improvement)
- **Dependencies:** 26
- **Priority Score:** 26121410.0
- **Functions:** 1/13 matched (target 1)
- **Missing functions:** `new`, `interrupt`, `compact`, `set_thread_name`, `review`, `list_skills`, `realtime_conversation_audio`, `user_input_answer`, `exec_approval`, `request_permissions_response`, `patch_approval`, `resolve_elicitation`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Symbol Deficit:** 12 (functions: 12, types: 0)
- **Action:** Deep review - likely missing major functionality

### 11. ollama.url
- **Similarity:** 0.53 (needs 32% improvement)
- **Dependencies:** 25
- **Priority Score:** 25010304.0
- **Functions:** 2/3 matched (target 2)
- **Missing functions:** `test_base_url_to_host_root`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Symbol Deficit:** 1 (functions: 1, types: 0)
- **Missing Tests:** 1 of 1 `#[test]` functions have no Kotlin counterpart
- **Action:** Deep review - likely missing major functionality

### 12. tui.style
- **Similarity:** 0.41 (needs 44% improvement)
- **Dependencies:** 21
- **Priority Score:** 21030606.0
- **Functions:** 3/6 matched (target 3)
- **Missing functions:** `proposed_plan_style`, `proposed_plan_style_for`, `proposed_plan_bg`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Symbol Deficit:** 3 (functions: 3, types: 0)
- **Action:** Deep review - likely missing major functionality

### 13. tui.color
- **Similarity:** 0.61 (needs 24% improvement)
- **Dependencies:** 19
- **Priority Score:** 19000704.0
- **Functions:** 7/7 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Action:** Review and complete missing sections

### 14. tool.terminal
- **Similarity:** 0.00 (needs 85% improvement)
- **Dependencies:** 17
- **Priority Score:** 17262610.0
- **Functions:** 0/16 matched (target 6)
- **Missing functions:** `start_terminal_operation_from_invocation`, `start_terminal_operation_from_runtime`, `insert_terminal_operation`, `end_terminal_operation`, `ensure_terminal_session`, `sync_terminal_model_observation`, `next_terminal_operation_id`, `terminal_operation_kind`, `parse_protocol_terminal_request`, `parse_dispatch_terminal_request`, `parse_terminal_response_payload`, `parse_protocol_terminal_response`, `parse_dispatch_terminal_response`, `parse_code_mode_exec_result`, `json_text_content`, `terminal_id_from_json`
- **Types:** 0/10 matched (target 0)
- **Missing types:** `TerminalOperationStart`, `ParsedTerminalRequest`, `ParsedTerminalResponse`, `ExecCommandBeginPayload`, `ExecCommandEndPayload`, `DispatchedToolTraceRequestPayload`, `DispatchedToolPayload`, `DispatchedWriteStdinArgs`, `DispatchedToolTraceResponsePayload`, `CodeModeExecResult`
- **Symbol Deficit:** 26 (functions: 16, types: 10)
- **Action:** Deep review - likely missing major functionality

### 15. tui.frame_requester
- **Similarity:** 0.18 (needs 67% improvement)
- **Dependencies:** 17
- **Priority Score:** 17081508.0
- **Functions:** 5/13 matched (target 6)
- **Missing functions:** `test_schedule_frame_immediate_triggers_once`, `test_schedule_frame_in_triggers_at_delay`, `test_coalesces_multiple_requests_into_single_draw`, `test_coalesces_mixed_immediate_and_delayed_requests`, `test_limits_draw_notifications_to_120fps`, `test_rate_limit_clamps_early_delayed_requests`, `test_rate_limit_does_not_delay_future_draws`, `test_multiple_delayed_requests_coalesce_to_earliest`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Symbol Deficit:** 8 (functions: 8, types: 0)
- **Missing Tests:** 8 of 8 `#[test]` functions have no Kotlin counterpart
- **Action:** Deep review - likely missing major functionality

### 16. execpolicy.decision
- **Similarity:** 0.58 (needs 27% improvement)
- **Dependencies:** 17
- **Priority Score:** 17000204.0
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Action:** Deep review - likely missing major functionality

### 17. state.turn
- **Similarity:** 0.22 (needs 63% improvement)
- **Dependencies:** 15
- **Priority Score:** 15213308.0
- **Functions:** 8/26 matched (target 13)
- **Missing functions:** `default`, `insert_pending_request_permissions`, `remove_pending_request_permissions`, `insert_pending_user_input`, `remove_pending_user_input`, `insert_pending_elicitation`, `remove_pending_elicitation`, `insert_pending_dynamic_tool`, `remove_pending_dynamic_tool`, `prepend_pending_input`, `has_pending_input`, `accept_mailbox_delivery_for_current_turn`, `accepts_mailbox_delivery_for_current_turn`, `set_mailbox_delivery_phase`, `record_granted_permissions`, `granted_permissions`, `enable_strict_auto_review`, `strict_auto_review_enabled`
- **Types:** 4/7 matched (target 10)
- **Missing types:** `MailboxDeliveryPhase`, `RemovedTask`, `PendingRequestPermissions`
- **Symbol Deficit:** 21 (functions: 18, types: 3)
- **Action:** Deep review - likely missing major functionality

### 18. bottom_pane.scroll_state
- **Similarity:** 0.64 (needs 21% improvement)
- **Dependencies:** 14
- **Priority Score:** 14010804.0
- **Functions:** 6/7 matched (target 6)
- **Missing functions:** `wrap_navigation_and_visibility`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Symbol Deficit:** 1 (functions: 1, types: 0)
- **Missing Tests:** 1 of 1 `#[test]` functions have no Kotlin counterpart
- **Action:** Review and complete missing sections

### 19. core.turn_diff_tracker
- **Similarity:** 0.03 (needs 82% improvement)
- **Dependencies:** 11
- **Priority Score:** 11141810.0
- **Functions:** 1/15 matched (target 10)
- **Missing functions:** `new`, `get_path_for_internal`, `find_git_root_cached`, `relative_to_git_root_str`, `git_blob_oid_for_path`, `get_unified_diff`, `get_file_diff`, `git_blob_sha1_hex_bytes`, `as_str`, `fmt`, `file_mode_for_path`, `blob_bytes`, `symlink_blob_bytes`, `is_windows_drive_or_unc_root`
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Symbol Deficit:** 14 (functions: 14, types: 0)
- **Action:** Deep review - likely missing major functionality

### 20. core.shell
- **Similarity:** 0.57 (needs 28% improvement)
- **Dependencies:** 11
- **Priority Score:** 11042104.0
- **Functions:** 15/19 matched (target 24)
- **Missing functions:** `shell_snapshot`, `empty_shell_snapshot_receiver`, `eq`, `test_detect_shell_type`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Symbol Deficit:** 4 (functions: 4, types: 0)
- **Missing Tests:** 1 of 1 `#[test]` functions have no Kotlin counterpart
- **Action:** Deep review - likely missing major functionality

### 21. tools.router
- **Similarity:** 0.21 (needs 64% improvement)
- **Dependencies:** 10
- **Priority Score:** 10071308.0
- **Functions:** 4/10 matched (target 6)
- **Missing functions:** `model_visible_specs`, `find_spec`, `create_diff_consumer`, `configured_tool_supports_parallel`, `dispatch_tool_call_with_code_mode_result`, `filter_deferred_dynamic_tool_spec`
- **Types:** 2/3 matched (target 2)
- **Missing types:** `ToolRouterParams`
- **Symbol Deficit:** 7 (functions: 6, types: 1)
- **Action:** Deep review - likely missing major functionality

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **string.json** (183 deps)
   - Path: `utils/string/src/json.rs`
   - Essential for 183 other files

2. **protocol.thread_id** (154 deps)
   - Path: `protocol/src/thread_id.rs`
   - Essential for 154 other files

3. **runtime.value** (152 deps)
   - Path: `code-mode/src/runtime/value.rs`
   - Essential for 152 other files

4. **v2.fs** (132 deps)
   - Path: `app-server/tests/suite/v2/fs.rs`
   - Essential for 132 other files

5. **protocol.error** (105 deps)
   - Path: `protocol/src/error.rs`
   - Essential for 105 other files

6. **otel.config** (104 deps)
   - Path: `otel/src/config.rs`
   - Essential for 104 other files

7. **common.test_codex** (77 deps)
   - Path: `core/tests/common/test_codex.rs`
   - Essential for 77 other files

8. **tools.json_schema** (75 deps)
   - Path: `tools/src/json_schema.rs`
   - Essential for 75 other files

9. **codex-client.sse** (66 deps)
   - Path: `codex-client/src/sse.rs`
   - Essential for 66 other files

10. **common.mcp_process** (65 deps)
   - Path: `mcp-server/tests/common/mcp_process.rs`
   - Essential for 65 other files

11. **session.turn_context** (51 deps)
   - Path: `core/src/session/turn_context.rs`
   - Essential for 51 other files

12. **cloud-tasks-mock-client.mock** (46 deps)
   - Path: `cloud-tasks-mock-client/src/mock.rs`
   - Essential for 46 other files

13. **git-utils.info** (45 deps)
   - Path: `git-utils/src/info.rs`
   - Essential for 45 other files

14. **transport.stdio** (33 deps)
   - Path: `app-server-transport/src/transport/stdio.rs`
   - Essential for 33 other files

15. **models-manager.model_info** (33 deps)
   - Path: `models-manager/src/model_info.rs`
   - Essential for 33 other files

16. **codex-client.request** (32 deps)
   - Path: `codex-client/src/request.rs`
   - Essential for 32 other files

17. **keymap_setup.debug** (32 deps)
   - Path: `tui/src/keymap_setup/debug.rs`
   - Essential for 32 other files

18. **tui.token_usage** (31 deps)
   - Path: `tui/src/token_usage.rs`
   - Essential for 31 other files

19. **rollout-trace.thread** (29 deps)
   - Path: `rollout-trace/src/thread.rs`
   - Essential for 29 other files

20. **code-mode.response** (28 deps)
   - Path: `code-mode/src/response.rs`
   - Essential for 28 other files

21. **suite.personality** (27 deps)
   - Path: `core/tests/suite/personality.rs`
   - Essential for 27 other files

22. **protocol.tool_name** (26 deps)
   - Path: `protocol/src/tool_name.rs`
   - Essential for 26 other files

23. **events.session_telemetry** (25 deps)
   - Path: `otel/src/events/session_telemetry.rs`
   - Essential for 25 other files

24. **tools.tool_spec** (24 deps)
   - Path: `tools/src/tool_spec.rs`
   - Essential for 24 other files

25. **path-utils.env** (23 deps)
   - Path: `utils/path-utils/src/env.rs`
   - Essential for 23 other files

26. **config.config_toml** (23 deps)
   - Path: `config/src/config_toml.rs`
   - Essential for 23 other files

27. **core.codex_thread** (22 deps)
   - Path: `core/src/codex_thread.rs`
   - Essential for 22 other files

28. **rollout.policy** (22 deps)
   - Path: `rollout/src/policy.rs`
   - Essential for 22 other files

29. **core.thread_manager** (21 deps)
   - Path: `core/src/thread_manager.rs`
   - Essential for 21 other files

30. **protocol.agent_path** (20 deps)
   - Path: `protocol/src/agent_path.rs`
   - Essential for 20 other files

31. **suite.tool** (20 deps)
   - Path: `apply-patch/tests/suite/tool.rs`
   - Essential for 20 other files

32. **tui.history_cell** (19 deps)
   - Path: `tui/src/history_cell.rs`
   - Essential for 19 other files

33. **bottom_pane.bottom_pane_view** (17 deps)
   - Path: `tui/src/bottom_pane/bottom_pane_view.rs`
   - Essential for 17 other files

34. **request_user_input.layout** (15 deps)
   - Path: `tui/src/bottom_pane/request_user_input/layout.rs`
   - Essential for 15 other files

35. **tui.tui** (15 deps)
   - Path: `tui/src/tui.rs`
   - Essential for 15 other files

36. **otel.provider** (15 deps)
   - Path: `otel/src/provider.rs`
   - Essential for 15 other files

37. **config.constraint** (15 deps)
   - Path: `config/src/constraint.rs`
   - Essential for 15 other files

38. **mcp-server.outgoing_message** (15 deps)
   - Path: `mcp-server/src/outgoing_message.rs`
   - Essential for 15 other files

39. **execpolicy-legacy.exec_call** (14 deps)
   - Path: `execpolicy-legacy/src/exec_call.rs`
   - Essential for 14 other files

40. **plugin.plugin_id** (14 deps)
   - Path: `plugin/src/plugin_id.rs`
   - Essential for 14 other files

41. **config.config_requirements** (14 deps)
   - Path: `config/src/config_requirements.rs`
   - Essential for 14 other files

42. **suite.originator** (14 deps)
   - Path: `exec/tests/suite/originator.rs`
   - Essential for 14 other files

43. **app-server.thread_status** (14 deps)
   - Path: `app-server/src/thread_status.rs`
   - Essential for 14 other files

44. **login.token_data** (12 deps)
   - Path: `login/src/token_data.rs`
   - Essential for 12 other files

45. **exec-server.process_id** (12 deps)
   - Path: `exec-server/src/process_id.rs`
   - Essential for 12 other files

46. **execpolicy-legacy.arg_type** (11 deps)
   - Path: `execpolicy-legacy/src/arg_type.rs`
   - Essential for 11 other files

47. **config.tui_keymap** (11 deps)
   - Path: `config/src/tui_keymap.rs`
   - Essential for 11 other files

48. **guardian.prompt** (11 deps)
   - Path: `core/src/guardian/prompt.rs`
   - Essential for 11 other files

49. **tools.tool_definition** (11 deps)
   - Path: `tools/src/tool_definition.rs`
   - Essential for 11 other files

50. **tui.chatwidget** (10 deps)
   - Path: `tui/src/chatwidget.rs`
   - Essential for 10 other files

51. **core.connectors** (10 deps)
   - Path: `core/src/connectors.rs`
   - Essential for 10 other files

52. **common.test_codex_exec** (10 deps)
   - Path: `core/tests/common/test_codex_exec.rs`
   - Essential for 10 other files

53. **execpolicy-legacy.valid_exec** (10 deps)
   - Path: `execpolicy-legacy/src/valid_exec.rs`
   - Essential for 10 other files

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. protocol.user_input

- **Target:** `protocol.UserInput [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 114
- **Priority Score:** 114080904.0
- **Functions:** 0/6 matched (target 0)
- **Missing functions:** `new`, `map_range`, `set_placeholder`, `_placeholder_for_conversion_only`, `placeholder`, `from`
- **Types:** 1/3 matched (target 4)
- **Missing types:** `TextElement`, `ByteRange`

### 2. tools.context

- **Target:** `tools.Context`
- **Similarity:** 0.05
- **Dependents:** 113
- **Priority Score:** 113202808.0
- **Functions:** 4/16 matched (target 10)
- **Missing functions:** `post_tool_use_response`, `code_mode_result`, `to_response_item`, `response_payload`, `from_text`, `from_content`, `into_text`, `truncated_output`, `response_text`, `response_input_to_code_mode_result`, `content_items_to_code_mode_result`, `function_tool_response`
- **Types:** 4/12 matched (target 9)
- **Missing types:** `ToolCallSource`, `McpToolOutput`, `ToolSearchOutput`, `FunctionToolOutput`, `ApplyPatchToolOutput`, `AbortedToolOutput`, `ExecCommandToolOutput`, `UnifiedExecCodeModeResult`

### 3. network-proxy.responses

- **Target:** `endpoint.Responses [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 51
- **Priority Score:** 51090912.0
- **Functions:** 0/8 matched (target 4)
- **Missing functions:** `text_response`, `json_response`, `blocked_header_value`, `blocked_message`, `blocked_text_response`, `blocked_message_with_policy`, `blocked_text_response_with_policy`, `blocked_message_with_policy_returns_human_message`
- **Types:** 0/1 matched (target 2)
- **Missing types:** `PolicyDecisionDetails`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `responses.rs` vs expected `responses.rs`
- **Proposed provenance header:** `// port-lint: source responses.rs` (current: `// port-lint: source responses.rs`)
- **Lint issues:** 1

### 4. state.session

- **Target:** `state.SessionState`
- **Similarity:** 0.26
- **Dependents:** 48
- **Priority Score:** 48233408.0
- **Functions:** 10/33 matched (target 10)
- **Missing functions:** `new`, `previous_turn_settings`, `set_previous_turn_settings`, `set_next_turn_is_first`, `take_next_turn_is_first`, `set_reference_context_item`, `reference_context_item`, `set_server_reasoning_included`, `server_reasoning_included`, `record_mcp_dependency_prompted`, `mcp_dependency_prompted`, `set_dependency_env`, `dependency_env`, `set_session_startup_prewarm`, `take_session_startup_prewarm`, `merge_connector_selection`, `get_connector_selection`, `clear_connector_selection`, `set_pending_session_start_source`, `take_pending_session_start_source`, `record_granted_permissions`, `granted_permissions`, `merge_rate_limit_fields`
- **Types:** 1/1 matched
- **Missing types:** _none_

### 5. render.renderable

- **Target:** `render.Renderable`
- **Similarity:** 0.70
- **Dependents:** 41
- **Priority Score:** 41021804.0
- **Functions:** 9/10 matched (target 48)
- **Missing functions:** `cursor_style`
- **Types:** 7/8 matched (target 15)
- **Missing types:** `RenderableExt`
- **Lint issues:** 2

### 6. tui.app_event

- **Target:** `tui.AppEvent`
- **Similarity:** 0.69
- **Dependents:** 36
- **Priority Score:** 36021304.0
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 9/11 matched (target 70)
- **Missing types:** `ConnectorsSnapshot`, `RealtimeWebrtcOffer`

### 7. tests.features

- **Target:** `features.Features [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 33
- **Priority Score:** 33050510.0
- **Functions:** 0/5 matched (target 14)
- **Missing functions:** `codex_command`, `features_enable_writes_feature_flag_to_config`, `features_disable_writes_feature_flag_to_config`, `features_enable_under_development_feature_prints_warning`, `features_list_is_sorted_alphabetically_by_feature_name`
- **Types:** 0/0 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only by basename: `core/src/features.rs` vs expected `cli/tests/features.rs`
- **Proposed provenance header:** `// port-lint: source cli/tests/features.rs` (current: `// port-lint: source core/src/features.rs`)
- **Lint issues:** 1

### 8. ollama.parser

- **Target:** `ollama.Parser`
- **Similarity:** 0.22
- **Dependents:** 32
- **Priority Score:** 32020308.0
- **Functions:** 1/3 matched (target 1)
- **Missing functions:** `test_pull_events_decoder_status_and_success`, `test_pull_events_decoder_progress`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/2 matched

### 9. tui.key_hint

- **Target:** `tui.KeyHint`
- **Similarity:** 0.29
- **Dependents:** 27
- **Priority Score:** 27203108.0
- **Functions:** 10/29 matched (target 11)
- **Missing functions:** `from_event`, `parts`, `display_label`, `normalize_key_parts`, `c0_control_char_to_ctrl_char`, `is_pressed`, `ctrl_alt`, `from`, `is_press_accepts_press_and_repeat_but_rejects_release`, `keybinding_list_ext_matches_any_binding`, `shifted_letter_binding_matches_uppercase_char_events`, `shift_letter_binding_preserves_other_modifiers_with_uppercase_compat`, `shift_letter_binding_does_not_match_plain_lowercase_or_other_uppercase`, `ctrl_letter_binding_matches_c0_control_char_events`, `ctrl_bindings_match_all_supported_c0_control_char_events`, `ctrl_binding_does_not_match_ambiguous_c0_escape_or_delete`, `history_search_ctrl_bindings_match_c0_control_char_events`, `ctrl_alt_sets_both_modifiers`, `has_ctrl_or_alt_checks_supported_modifier_combinations`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `KeyBindingListExt`
- **Tests:** 0/11 matched

### 10. tui.app_event_sender

- **Target:** `tui.AppEventSender`
- **Similarity:** 0.06
- **Dependents:** 26
- **Priority Score:** 26121410.0
- **Functions:** 1/13 matched (target 1)
- **Missing functions:** `new`, `interrupt`, `compact`, `set_thread_name`, `review`, `list_skills`, `realtime_conversation_audio`, `user_input_answer`, `exec_approval`, `request_permissions_response`, `patch_approval`, `resolve_elicitation`
- **Types:** 1/1 matched
- **Missing types:** _none_

### 11. ollama.url

- **Target:** `ollama.Url`
- **Similarity:** 0.53
- **Dependents:** 25
- **Priority Score:** 25010304.0
- **Functions:** 2/3 matched (target 2)
- **Missing functions:** `test_base_url_to_host_root`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 12. tui.style

- **Target:** `tui.Style`
- **Similarity:** 0.41
- **Dependents:** 21
- **Priority Score:** 21030606.0
- **Functions:** 3/6 matched (target 3)
- **Missing functions:** `proposed_plan_style`, `proposed_plan_style_for`, `proposed_plan_bg`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 13. tui.color

- **Target:** `tui.Color`
- **Similarity:** 0.61
- **Dependents:** 19
- **Priority Score:** 19000704.0
- **Functions:** 7/7 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 14. tool.terminal

- **Target:** `core.Terminal [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 17
- **Priority Score:** 17262610.0
- **Functions:** 0/16 matched (target 6)
- **Missing functions:** `start_terminal_operation_from_invocation`, `start_terminal_operation_from_runtime`, `insert_terminal_operation`, `end_terminal_operation`, `ensure_terminal_session`, `sync_terminal_model_observation`, `next_terminal_operation_id`, `terminal_operation_kind`, `parse_protocol_terminal_request`, `parse_dispatch_terminal_request`, `parse_terminal_response_payload`, `parse_protocol_terminal_response`, `parse_dispatch_terminal_response`, `parse_code_mode_exec_result`, `json_text_content`, `terminal_id_from_json`
- **Types:** 0/10 matched (target 0)
- **Missing types:** `TerminalOperationStart`, `ParsedTerminalRequest`, `ParsedTerminalResponse`, `ExecCommandBeginPayload`, `ExecCommandEndPayload`, `DispatchedToolTraceRequestPayload`, `DispatchedToolPayload`, `DispatchedWriteStdinArgs`, `DispatchedToolTraceResponsePayload`, `CodeModeExecResult`
- **Provenance warning:** port-lint provenance header matched only by basename: `core/src/terminal.rs` vs expected `reducer/tool/terminal.rs`
- **Proposed provenance header:** `// port-lint: source reducer/tool/terminal.rs` (current: `// port-lint: source core/src/terminal.rs`)
- **Lint issues:** 1

### 15. tui.frame_requester

- **Target:** `tui.FrameRequester`
- **Similarity:** 0.18
- **Dependents:** 17
- **Priority Score:** 17081508.0
- **Functions:** 5/13 matched (target 6)
- **Missing functions:** `test_schedule_frame_immediate_triggers_once`, `test_schedule_frame_in_triggers_at_delay`, `test_coalesces_multiple_requests_into_single_draw`, `test_coalesces_mixed_immediate_and_delayed_requests`, `test_limits_draw_notifications_to_120fps`, `test_rate_limit_clamps_early_delayed_requests`, `test_rate_limit_does_not_delay_future_draws`, `test_multiple_delayed_requests_coalesce_to_earliest`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Tests:** 0/8 matched

### 16. execpolicy.decision

- **Target:** `execpolicy.Decision`
- **Similarity:** 0.58
- **Dependents:** 17
- **Priority Score:** 17000204.0
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 17. state.turn

- **Target:** `session.Turn`
- **Similarity:** 0.22
- **Dependents:** 15
- **Priority Score:** 15213308.0
- **Functions:** 8/26 matched (target 13)
- **Missing functions:** `default`, `insert_pending_request_permissions`, `remove_pending_request_permissions`, `insert_pending_user_input`, `remove_pending_user_input`, `insert_pending_elicitation`, `remove_pending_elicitation`, `insert_pending_dynamic_tool`, `remove_pending_dynamic_tool`, `prepend_pending_input`, `has_pending_input`, `accept_mailbox_delivery_for_current_turn`, `accepts_mailbox_delivery_for_current_turn`, `set_mailbox_delivery_phase`, `record_granted_permissions`, `granted_permissions`, `enable_strict_auto_review`, `strict_auto_review_enabled`
- **Types:** 4/7 matched (target 10)
- **Missing types:** `MailboxDeliveryPhase`, `RemovedTask`, `PendingRequestPermissions`
- **Lint issues:** 2

### 18. bottom_pane.scroll_state

- **Target:** `bottompane.ScrollState`
- **Similarity:** 0.64
- **Dependents:** 14
- **Priority Score:** 14010804.0
- **Functions:** 6/7 matched (target 6)
- **Missing functions:** `wrap_navigation_and_visibility`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 19. core.turn_diff_tracker

- **Target:** `session.TurnDiffTracker`
- **Similarity:** 0.03
- **Dependents:** 11
- **Priority Score:** 11141810.0
- **Functions:** 1/15 matched (target 10)
- **Missing functions:** `new`, `get_path_for_internal`, `find_git_root_cached`, `relative_to_git_root_str`, `git_blob_oid_for_path`, `get_unified_diff`, `get_file_diff`, `git_blob_sha1_hex_bytes`, `as_str`, `fmt`, `file_mode_for_path`, `blob_bytes`, `symlink_blob_bytes`, `is_windows_drive_or_unc_root`
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Lint issues:** 1

### 20. core.shell

- **Target:** `shell.ShellDetector`
- **Similarity:** 0.57
- **Dependents:** 11
- **Priority Score:** 11042104.0
- **Functions:** 15/19 matched (target 24)
- **Missing functions:** `shell_snapshot`, `empty_shell_snapshot_receiver`, `eq`, `test_detect_shell_type`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 21. tools.router

- **Target:** `tools.Router`
- **Similarity:** 0.21
- **Dependents:** 10
- **Priority Score:** 10071308.0
- **Functions:** 4/10 matched (target 6)
- **Missing functions:** `model_visible_specs`, `find_spec`, `create_diff_consumer`, `configured_tool_supports_parallel`, `dispatch_tool_call_with_code_mode_result`, `filter_deferred_dynamic_tool_spec`
- **Types:** 2/3 matched (target 2)
- **Missing types:** `ToolRouterParams`

### 22. codex-api.common

- **Target:** `common.Common [PROVENANCE-FALLBACK]`
- **Similarity:** 0.15
- **Dependents:** 6
- **Priority Score:** 6112008.5
- **Functions:** 1/4 matched (target 2)
- **Missing functions:** `from`, `response_create_client_metadata`, `poll_next`
- **Types:** 8/16 matched (target 9)
- **Missing types:** `MemorySummarizeInput`, `RawMemory`, `RawMemoryMetadata`, `MemorySummarizeOutput`, `ResponseEvent`, `ResponseCreateWsRequest`, `ResponsesWsRequest`, `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `common.rs` vs expected `common.rs`
- **Proposed provenance header:** `// port-lint: source common.rs` (current: `// port-lint: source common.rs`)
- **Lint issues:** 1

### 23. tui.update_action

- **Target:** `tui.UpdateAction`
- **Similarity:** 0.22
- **Dependents:** 6
- **Priority Score:** 6030708.0
- **Functions:** 3/6 matched (target 5)
- **Missing functions:** `from_install_context`, `maps_install_context_to_update_action`, `standalone_update_commands_rerun_latest_installer`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Tests:** 0/2 matched

### 24. protocol.parse_command

- **Target:** `protocol.ParseCommand [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 6
- **Priority Score:** 6000110.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `parse_command.rs` vs expected `parse_command.rs`
- **Proposed provenance header:** `// port-lint: source parse_command.rs` (current: `// port-lint: source parse_command.rs`)
- **Lint issues:** 1

### 25. cli.exit_status

- **Target:** `cli.ExitStatus`
- **Similarity:** 0.30
- **Dependents:** 5
- **Priority Score:** 5000107.0
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 26. tui.slash_command

- **Target:** `tui.SlashCommand`
- **Similarity:** 0.15
- **Dependents:** 4
- **Priority Score:** 4071208.5
- **Functions:** 4/11 matched (target 4)
- **Missing functions:** `command`, `supports_inline_args`, `available_in_side_conversation`, `stop_command_is_canonical_name`, `clean_alias_parses_to_stop_command`, `certain_commands_are_available_during_task`, `auto_review_command_is_approve`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Tests:** 0/4 matched

### 27. context.environment_context

- **Target:** `utils.Environment [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3171710.0
- **Functions:** 0/13 matched (target 2)
- **Missing functions:** `legacy`, `from_turn_environments`, `from_vec`, `equals_except_shell`, `new`, `new_with_environments`, `diff_from_turn_context_item`, `from_turn_context`, `from_turn_context_item`, `with_subagents`, `network_from_turn_context`, `network_from_turn_context_item`, `body`
- **Types:** 0/4 matched (target 1)
- **Missing types:** `EnvironmentContext`, `EnvironmentContextEnvironment`, `EnvironmentContextEnvironments`, `NetworkContext`
- **Provenance warning:** port-lint provenance header matched only by basename: `core/src/environmentContext.rs` vs expected `context/environment_context.rs`
- **Proposed provenance header:** `// port-lint: source context/environment_context.rs` (current: `// port-lint: source core/src/environmentContext.rs`)
- **Lint issues:** 1

### 28. protocol.account

- **Target:** `protocol.Account [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3091010.0
- **Functions:** 0/8 matched (target 0)
- **Missing functions:** `is_team_like`, `is_business_like`, `is_workspace_account`, `from`, `usage_based_plan_types_use_expected_wire_names`, `plan_family_helpers_group_usage_based_variants_with_existing_plans`, `workspace_account_helper_includes_usage_based_workspace_plans`, `auth_plan_type_converts_to_account_plan_type`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `ProviderAccount`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `account.rs` vs expected `account.rs`
- **Proposed provenance header:** `// port-lint: source account.rs` (current: `// port-lint: source account.rs`)
- **Lint issues:** 1

### 29. cli.format_env_display

- **Target:** `common.FormatEnvDisplay`
- **Similarity:** 0.15
- **Dependents:** 3
- **Priority Score:** 3040508.5
- **Functions:** 1/5 matched (target 1)
- **Missing functions:** `returns_dash_when_empty`, `formats_sorted_env_pairs`, `formats_env_vars_with_dollar_prefix`, `combines_env_pairs_and_vars`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/4 matched

### 30. core.user_shell_command

- **Target:** `session.UserShellCommand`
- **Similarity:** 0.33
- **Dependents:** 3
- **Priority Score:** 3020306.8
- **Functions:** 1/3 matched (target 6)
- **Missing functions:** `user_shell_command_fragment`, `format_user_shell_command_record`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 31. context.user_instructions

- **Target:** `session.UserInstructions [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3010210.0
- **Functions:** 0/1 matched (target 4)
- **Missing functions:** `body`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only by basename: `core/src/userInstructions.rs` vs expected `context/user_instructions.rs`
- **Proposed provenance header:** `// port-lint: source context/user_instructions.rs` (current: `// port-lint: source core/src/userInstructions.rs`)
- **Lint issues:** 1

### 32. protocol.auth

- **Target:** `core.Auth [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2081010.0
- **Functions:** 0/6 matched (target 37)
- **Missing functions:** `from_raw_value`, `display_name`, `raw_value`, `is_workspace_account`, `new`, `plan_type_deserializes_raw_aliases`
- **Types:** 2/4 matched (target 17)
- **Missing types:** `RefreshTokenFailedError`, `RefreshTokenFailedReason`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `auth.rs` vs expected `auth.rs`
- **Proposed provenance header:** `// port-lint: source auth.rs` (current: `// port-lint: source auth.rs`)
- **Lint issues:** 1

### 33. cli.sandbox_mode_cli_arg

- **Target:** `common.SandboxModeCliArg`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2020310.0
- **Functions:** 0/2 matched (target 1)
- **Missing functions:** `from`, `maps_cli_args_to_protocol_modes`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 34. cli.approval_mode_cli_arg

- **Target:** `common.ApprovalModeCliArg`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2010210.0
- **Functions:** 0/1 matched
- **Missing functions:** `from`
- **Types:** 1/1 matched
- **Missing types:** _none_

### 35. command_safety.is_safe_command

- **Target:** `commandsafety.IsSafeCommand [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1222510.0
- **Functions:** 3/25 matched (target 3)
- **Missing functions:** `is_safe_powershell_words`, `git_branch_is_read_only`, `git_has_unsafe_global_option`, `git_subcommand_args_are_read_only`, `vec_str`, `known_safe_examples`, `git_branch_mutating_flags_are_not_safe`, `git_branch_global_options_respect_safety_rules`, `git_first_positional_is_the_subcommand`, `git_output_flags_are_not_safe`, `git_global_override_flags_are_not_safe`, `cargo_check_is_not_safe`, `zsh_lc_safe_command_sequence`, `unknown_or_partial`, `base64_output_options_are_unsafe`, `ripgrep_rules`, `windows_powershell_full_path_is_safe`, `windows_git_full_path_is_safe`, `bash_lc_safe_examples`, `bash_lc_safe_examples_with_operators`, `bash_lc_unsafe_examples`, `direct_powershell_words_use_windows_safelist`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/18 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `core/src/commandSafety/isSafeCommand.rs` vs expected `command_safety/is_safe_command.rs`
- **Proposed provenance header:** `// port-lint: source command_safety/is_safe_command.rs` (current: `// port-lint: source core/src/commandSafety/isSafeCommand.rs`)
- **Lint issues:** 1

### 36. core.compact

- **Target:** `endpoint.Compact [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1171710.0
- **Functions:** 0/15 matched (target 4)
- **Missing functions:** `should_use_remote_compact_task`, `run_inline_auto_compact_task`, `run_compact_task`, `run_compact_task_inner`, `run_compact_task_inner_impl`, `begin`, `track`, `compaction_status_from_result`, `content_items_to_text`, `collect_user_messages`, `is_summary_message`, `insert_initial_context_before_last_real_user_or_summary`, `build_compacted_history`, `build_compacted_history_with_limit`, `drain_to_completed`
- **Types:** 0/2 matched
- **Missing types:** `InitialContextInjection`, `CompactionAnalyticsAttempt`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `compact.rs` vs expected `compact.rs`
- **Proposed provenance header:** `// port-lint: source compact.rs` (current: `// port-lint: source compact.rs`)
- **TODOs:** 4
- **Lint issues:** 1

### 37. command_safety.is_dangerous_command

- **Target:** `commandsafety.IsDangerousCommand [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1111310.0
- **Functions:** 2/13 matched (target 3)
- **Missing functions:** `is_dangerous_powershell_words`, `is_git_global_option_with_value`, `is_git_global_option_with_inline_value`, `git_global_option_requires_prompt`, `executable_name_lookup_key`, `find_git_subcommand`, `vec_str`, `rm_rf_is_dangerous`, `rm_f_is_dangerous`, `git_dash_c_requires_prompt`, `direct_powershell_words_reuse_windows_dangerous_detection`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/5 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `core/src/commandSafety/isDangerousCommand.rs` vs expected `command_safety/is_dangerous_command.rs`
- **Proposed provenance header:** `// port-lint: source command_safety/is_dangerous_command.rs` (current: `// port-lint: source core/src/commandSafety/isDangerousCommand.rs`)
- **Lint issues:** 1

### 38. execpolicy.rule

- **Target:** `execpolicy.Rule`
- **Similarity:** 0.48
- **Dependents:** 1
- **Priority Score:** 1082005.2
- **Functions:** 7/12 matched (target 9)
- **Missing functions:** `with_resolved_program`, `parse`, `as_policy_string`, `normalize_network_rule_host`, `as_any`
- **Types:** 5/8 matched
- **Missing types:** `NetworkRuleProtocol`, `NetworkRule`, `RuleRef`

### 39. string.truncate

- **Target:** `context.TruncationPolicy [PROVENANCE-FALLBACK]`
- **Similarity:** 0.55
- **Dependents:** 1
- **Priority Score:** 1031104.5
- **Functions:** 8/11 matched (target 13)
- **Missing functions:** `truncate_middle_chars`, `truncate_middle_with_token_budget`, `removed_units`
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `core/src/truncate.rs` vs expected `truncate.rs`
- **Proposed provenance header:** `// port-lint: source truncate.rs` (current: `// port-lint: source core/src/truncate.rs`)
- **Lint issues:** 1

### 40. tui.frame_rate_limiter

- **Target:** `tui.FrameRateLimiter`
- **Similarity:** 0.41
- **Dependents:** 1
- **Priority Score:** 1020505.9
- **Functions:** 2/4 matched (target 2)
- **Missing functions:** `default_does_not_clamp`, `clamps_to_min_interval_since_last_emit`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Tests:** 0/2 matched

### 41. core.spawn

- **Target:** `core.Spawn [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1020310.0
- **Functions:** 0/1 matched (target 0)
- **Missing functions:** `spawn_child_async`
- **Types:** 1/2 matched
- **Missing types:** `SpawnChildRequest`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `spawn.rs` vs expected `spawn.rs`
- **Proposed provenance header:** `// port-lint: source spawn.rs` (current: `// port-lint: source spawn.rs`)
- **Lint issues:** 1

### 42. sse.responses

- **Target:** `streaming.SseParser`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 636310.0
- **Functions:** 0/55 matched (target 2)
- **Missing functions:** `stream_from_fixture`, `spawn_response_stream`, `from`, `kind`, `response_model`, `model_verifications`, `header_openai_model_value_from_json`, `model_verifications_from_json_value`, `parse_model_verification`, `json_value_as_string`, `into_api_error`, `process_responses_event`, `process_sse`, `try_parse_retry_after`, `is_context_window_error`, `is_quota_exceeded_error`, `is_usage_not_included`, `is_invalid_prompt_error`, `is_cyber_policy_error`, `is_server_overloaded_error`, `cyber_policy_fallback_message`, `cyber_policy_message`, `rate_limit_regex`, `collect_events`, `run_sse`, `idle_timeout`, `parses_items_and_completed`, `error_when_missing_completed`, `parses_tool_search_call_items`, `parses_tool_call_input_deltas`, `emits_completed_without_stream_end`, `error_when_error_event`, `context_window_error_is_fatal`, `context_window_error_with_newline_is_fatal`, `quota_exceeded_error_is_fatal`, `cyber_policy_error_is_fatal`, `cyber_policy_error_uses_fallback_for_empty_message`, `invalid_prompt_without_type_is_invalid_request`, `table_driven_event_kinds`, `is_created`, `is_output`, `is_completed`, `spawn_response_stream_emits_header_events`, `spawn_response_stream_ignores_model_verification_header`, `process_sse_ignores_response_model_field_in_payload`, `process_sse_emits_server_model_from_response_headers_payload`, `process_sse_emits_model_verification_field`, `responses_stream_event_response_model_reads_top_level_headers`, `responses_stream_event_response_model_prefers_response_headers`, `responses_stream_event_model_verification_reads_metadata_field`, `responses_stream_event_model_verification_ignores_unknown_field`, `responses_stream_event_model_verification_ignores_non_array_field`, `test_try_parse_retry_after`, `test_try_parse_retry_after_no_delay`, `test_try_parse_retry_after_azure`
- **Types:** 0/8 matched (target 2)
- **Missing types:** `Error`, `ResponseCompleted`, `ResponseCompletedUsage`, `ResponseCompletedInputTokensDetails`, `ResponseCompletedOutputTokensDetails`, `ResponsesStreamEvent`, `ResponsesEventError`, `TestCase`
- **Tests:** 0/32 matched

### 43. protocol.config_types

- **Target:** `protocol.ConfigTypes [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 485310.0
- **Functions:** 0/27 matched (target 0)
- **Missing functions:** `schema_name`, `json_schema`, `default`, `string_enum_schema_with_description`, `merge`, `from`, `timeout`, `refresh_interval`, `default_provider_auth_timeout_ms`, `default_provider_auth_refresh_interval_ms`, `non_zero_u64`, `default_provider_auth_cwd`, `is_default_provider_auth_cwd`, `display_name`, `is_tui_visible`, `allows_request_user_input`, `settings_ref`, `model`, `reasoning_effort`, `with_updates`, `apply_mask`, `apply_mask_can_clear_optional_fields`, `mode_kind_deserializes_alias_values_to_default`, `approvals_reviewer_serializes_auto_review_and_accepts_legacy_guardian_subagent`, `tui_visible_collaboration_modes_match_mode_kind_visibility`, `web_search_location_merge_prefers_overlay_values`, `web_search_tool_config_merge_prefers_overlay_values`
- **Types:** 5/26 matched (target 6)
- **Missing types:** `ApprovalsReviewer`, `ShellEnvironmentPolicyInherit`, `EnvironmentVariablePattern`, `ShellEnvironmentPolicy`, `WindowsSandboxLevel`, `Personality`, `WebSearchMode`, `WebSearchContextSize`, `WebSearchLocation`, `WebSearchToolConfig`, `WebSearchFilters`, `WebSearchUserLocationType`, `WebSearchUserLocation`, `WebSearchConfig`, `ServiceTier`, `ModelProviderAuthInfo`, `AltScreenMode`, `ModeKind`, `CollaborationMode`, `Settings`, `CollaborationModeMask`
- **Tests:** 0/6 matched

### 44. core-plugins.loader

- **Target:** `config.ConfigLoader [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 484810.0
- **Functions:** 0/40 matched (target 5)
- **Missing functions:** `log_plugin_load_errors`, `into_mcp_servers`, `load_plugins_from_layer_stack`, `remote_installed_plugins_to_config`, `refresh_curated_plugin_cache`, `curated_plugin_cache_version`, `refresh_non_curated_plugin_cache`, `refresh_non_curated_plugin_cache_force_reinstall`, `refresh_non_curated_plugin_cache_with_mode`, `configured_plugins_from_stack`, `is_full_git_sha`, `configured_plugins_from_user_config_value`, `configured_plugins_from_codex_home`, `configured_plugin_ids`, `curated_plugin_ids_from_config_keys`, `non_curated_plugin_ids_from_config_keys`, `configured_curated_plugin_ids_from_codex_home`, `load_plugin`, `apply_plugin_mcp_server_policy`, `has_enabled_skills`, `load_plugin_skills`, `plugin_skill_roots`, `default_skill_roots`, `plugin_mcp_config_paths`, `default_mcp_config_paths`, `load_plugin_apps`, `plugin_app_config_paths`, `default_app_config_paths`, `load_plugin_hooks`, `append_plugin_hook_file`, `load_apps_from_paths`, `plugin_telemetry_metadata_from_root`, `load_plugin_mcp_servers`, `installed_plugin_telemetry_metadata`, `load_mcp_servers_from_file`, `normalize_plugin_mcp_servers`, `normalize_plugin_mcp_server_value`, `materialize_marketplace_plugin_source`, `clone_git_plugin_source`, `run_git`
- **Types:** 0/8 matched (target 2)
- **Missing types:** `NonCuratedCacheRefreshMode`, `PluginMcpServersFile`, `PluginMcpFile`, `PluginAppFile`, `PluginAppConfig`, `ResolvedPluginSkills`, `PluginMcpDiscovery`, `MaterializedMarketplacePluginSource`
- **Provenance warning:** port-lint provenance header matched only by basename: `core/src/skills/loader.rs` vs expected `loader.rs`
- **Proposed provenance header:** `// port-lint: source loader.rs` (current: `// port-lint: source core/src/skills/loader.rs`)
- **Lint issues:** 1

### 45. command_safety.windows_dangerous_commands

- **Target:** `commandsafety.WindowsDangerousCommands [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 465710.0
- **Functions:** 10/56 matched (target 11)
- **Missing functions:** `is_dangerous_powershell_words`, `split_embedded_cmd_operators`, `has_force_delete_cmdlet`, `has_force_flag_cmd`, `has_recursive_flag_cmd`, `has_quiet_flag_cmd`, `vec_str`, `powershell_start_process_url_is_dangerous`, `powershell_start_process_url_with_trailing_semicolon_is_dangerous`, `powershell_start_process_local_is_not_flagged`, `cmd_start_with_url_is_dangerous`, `msedge_with_url_is_dangerous`, `explorer_with_directory_is_not_flagged`, `powershell_remove_item_force_is_dangerous`, `powershell_remove_item_recurse_force_is_dangerous`, `powershell_ri_alias_force_is_dangerous`, `powershell_remove_item_without_force_is_not_flagged`, `cmd_del_force_is_dangerous`, `cmd_erase_force_is_dangerous`, `cmd_del_without_force_is_not_flagged`, `cmd_rd_recursive_is_dangerous`, `cmd_rd_without_quiet_is_not_flagged`, `cmd_rmdir_recursive_is_dangerous`, `powershell_remove_item_path_recurse_force_is_dangerous`, `powershell_remove_item_force_with_semicolon_is_dangerous`, `powershell_remove_item_force_inside_block_is_dangerous`, `powershell_remove_item_force_inside_brackets_is_dangerous`, `cmd_del_path_containing_f_is_not_flagged`, `cmd_rd_path_containing_s_is_not_flagged`, `cmd_bypass_chained_del_is_dangerous`, `powershell_chained_no_space_is_dangerous`, `powershell_comma_separated_is_dangerous`, `cmd_echo_del_is_not_dangerous`, `cmd_del_single_string_argument_is_dangerous`, `cmd_del_chained_single_string_argument_is_dangerous`, `cmd_chained_no_space_del_is_dangerous`, `cmd_chained_andand_no_space_del_is_dangerous`, `cmd_chained_oror_no_space_del_is_dangerous`, `cmd_start_url_single_string_is_dangerous`, `cmd_chained_no_space_rmdir_is_dangerous`, `cmd_del_force_uppercase_flag_is_dangerous`, `cmdexe_r_del_force_is_dangerous`, `cmd_start_quoted_url_single_string_is_dangerous`, `cmd_start_title_then_url_is_dangerous`, `powershell_rm_alias_force_is_dangerous`, `powershell_benign_force_separate_command_is_not_dangerous`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Tests:** 0/40 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `core/src/commandSafety/windowsDangerousCommands.rs` vs expected `command_safety/windows_dangerous_commands.rs`
- **Proposed provenance header:** `// port-lint: source command_safety/windows_dangerous_commands.rs` (current: `// port-lint: source core/src/commandSafety/windowsDangerousCommands.rs`)
- **Lint issues:** 1

### 46. exec-server.protocol

- **Target:** `protocol.Protocol [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 414210.0
- **Functions:** 0/5 matched (target 31)
- **Missing functions:** `into_inner`, `from`, `serialize`, `deserialize`, `http_request_timeout_treats_omitted_and_null_as_no_timeout`
- **Types:** 1/37 matched (target 188)
- **Missing types:** `ByteChunk`, `InitializeParams`, `InitializeResponse`, `ExecParams`, `ExecEnvPolicy`, `ExecResponse`, `ReadParams`, `ProcessOutputChunk`, `ReadResponse`, `WriteParams`, `WriteStatus`, `WriteResponse`, `TerminateParams`, `TerminateResponse`, `FsReadFileParams`, `FsReadFileResponse`, `FsWriteFileParams`, `FsWriteFileResponse`, `FsCreateDirectoryParams`, `FsCreateDirectoryResponse`, `FsGetMetadataParams`, `FsGetMetadataResponse`, `FsReadDirectoryParams`, `FsReadDirectoryEntry`, `FsReadDirectoryResponse`, `FsRemoveParams`, `FsRemoveResponse`, `FsCopyParams`, `FsCopyResponse`, `HttpHeader`, `HttpRequestParams`, `HttpRequestResponse`, `HttpRequestBodyDeltaNotification`, `ExecOutputDeltaNotification`, `ExecExitedNotification`, `ExecClosedNotification`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `protocol.rs` vs expected `protocol.rs`
- **Proposed provenance header:** `// port-lint: source protocol.rs` (current: `// port-lint: source protocol.rs`)
- **Lint issues:** 1

### 47. shell-command.bash

- **Target:** `bash.Bash`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 404309.6
- **Functions:** 3/43 matched (target 8)
- **Missing functions:** `try_parse_shell`, `try_parse_word_only_commands_sequence`, `parse_plain_command_from_node`, `parse_heredoc_command_words`, `is_literal_word_or_number`, `is_allowed_heredoc_attachment_kind`, `find_single_command_node`, `has_named_descendant_kind`, `parse_double_quoted_string`, `parse_raw_string`, `parse_seq`, `accepts_single_simple_command`, `accepts_multiple_commands_with_allowed_operators`, `extracts_double_and_single_quoted_strings`, `accepts_double_quoted_strings_with_newlines`, `accepts_mixed_quote_concatenation`, `rejects_double_quoted_strings_with_expansions`, `accepts_numbers_as_words`, `rejects_parentheses_and_subshells`, `rejects_redirections_and_unsupported_operators`, `rejects_command_and_process_substitutions_and_expansions`, `rejects_variable_assignment_prefix`, `rejects_trailing_operator_parse_error`, `rejects_empty_command_position_with_leading_operator`, `rejects_empty_command_position_with_double_separator`, `rejects_empty_command_position_with_empty_pipeline_segment`, `parse_zsh_lc_plain_commands`, `accepts_concatenated_flag_and_value`, `accepts_concatenated_flag_with_single_quotes`, `rejects_concatenation_with_variable_substitution`, `rejects_concatenation_with_command_substitution`, `parse_shell_lc_single_command_prefix_supports_heredoc`, `parse_shell_lc_single_command_prefix_rejects_multi_command_scripts`, `parse_shell_lc_single_command_prefix_rejects_non_heredoc_redirects`, `parse_shell_lc_single_command_prefix_rejects_heredoc_with_extra_file_redirect`, `parse_shell_lc_single_command_prefix_rejects_heredoc_with_variable_assignment`, `parse_shell_lc_single_command_prefix_rejects_herestring_with_chaining`, `parse_shell_lc_single_command_prefix_rejects_herestring_with_substitution`, `parse_shell_lc_single_command_prefix_rejects_arithmetic_shift_non_heredoc_script`, `parse_shell_lc_single_command_prefix_rejects_heredoc_command_with_word_expansion`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/30 matched

### 48. core.exec

- **Target:** `core.Exec`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 384109.8
- **Functions:** 1/34 matched (target 3)
- **Missing functions:** `windows_sandbox_uses_elevated_backend`, `select_process_exec_tool_sandbox_type`, `from`, `wait_with_outcome`, `timeout_ms`, `with_cancellation`, `cancel_when_either`, `retained_bytes_cap`, `io_drain_timeout`, `uses_expiration`, `process_exec_tool_call`, `build_exec_request`, `execute_exec_request`, `get_raw_output_result`, `extract_create_process_as_user_error_code`, `windowsapps_path_kind`, `record_windows_sandbox_spawn_failure`, `exec_windows_sandbox`, `finalize_exec_result`, `append_capped`, `aggregate_output`, `exec`, `should_use_windows_restricted_token_sandbox`, `unsupported_windows_restricted_token_sandbox_reason`, `resolve_windows_restricted_token_filesystem_overrides`, `normalize_windows_override_path`, `resolve_windows_elevated_filesystem_overrides`, `has_reopened_writable_descendant`, `consume_output`, `await_output`, `read_output`, `synthetic_exit_status`, `synthetic_exit_status_for_code`
- **Types:** 2/7 matched (target 5)
- **Missing types:** `WindowsSandboxFilesystemOverrides`, `ExecCapturePolicy`, `ExecExpiration`, `ExecExpirationOutcome`, `RawExecToolCallOutput`
- **Lint issues:** 3

### 49. core.exec_policy

- **Target:** `execpolicy.ExecPolicy`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 363610.0
- **Functions:** 0/29 matched (target 2)
- **Missing functions:** `child_uses_parent_exec_policy`, `exec_policy_config_folders`, `is_policy_match`, `prompt_is_rejected_by_policy`, `new`, `load`, `current`, `create_exec_approval_requirement_for_command`, `append_amendment_and_update`, `append_network_rule_and_update`, `default`, `check_execpolicy_for_warnings`, `exec_policy_message_for_display`, `parse_starlark_line_from_message`, `format_exec_policy_error_with_source`, `load_exec_policy_with_warning`, `load_exec_policy`, `render_decision_for_unmatched_command`, `profile_is_managed_read_only`, `default_policy_path`, `commands_for_exec_policy`, `try_derive_execpolicy_amendment_for_prompt_rules`, `try_derive_execpolicy_amendment_for_allow_rules`, `derive_requested_execpolicy_amendment_from_prefix_rule`, `prefix_rule_would_approve_all_commands`, `derive_prompt_reason`, `render_shlex_command`, `derive_forbidden_reason`, `collect_policy_files`
- **Types:** 0/7 matched (target 0)
- **Missing types:** `ExecPolicyCommandOrigin`, `UnmatchedCommandContext`, `ExecPolicyCommands`, `ExecPolicyError`, `ExecPolicyUpdateError`, `ExecPolicyManager`, `ExecApprovalRequest`

### 50. model-provider.provider

- **Target:** `provider.Provider [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 353510.0
- **Functions:** 0/28 matched (target 4)
- **Missing functions:** `default`, `fmt`, `capabilities`, `api_provider`, `runtime_base_url`, `api_auth`, `create_model_provider`, `new`, `info`, `auth_manager`, `auth`, `account_state`, `models_manager`, `provider_info_with_command_auth`, `test_codex_home`, `provider_for`, `remote_model`, `configured_provider_uses_default_capabilities`, `configured_provider_runtime_base_url_uses_configured_base_url`, `create_model_provider_builds_command_auth_manager_without_base_manager`, `create_model_provider_does_not_use_openai_auth_manager_for_amazon_bedrock_provider`, `openai_provider_returns_unauthenticated_openai_account_state`, `openai_provider_returns_api_key_account_state`, `custom_non_openai_provider_returns_no_account_state`, `amazon_bedrock_provider_returns_bedrock_account_state`, `amazon_bedrock_provider_creates_static_models_manager`, `amazon_bedrock_provider_uses_configured_static_catalog_when_present`, `configured_provider_models_manager_uses_provider_bearer_token`
- **Types:** 0/7 matched (target 3)
- **Missing types:** `ProviderCapabilities`, `ProviderAccountState`, `ProviderAccountError`, `ProviderAccountResult`, `ModelProvider`, `SharedModelProvider`, `ConfiguredModelProvider`
- **Tests:** 0/15 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `provider.rs` vs expected `provider.rs`
- **Proposed provenance header:** `// port-lint: source provider.rs` (current: `// port-lint: source provider.rs`)
- **Lint issues:** 1

### 51. backend-client.types

- **Target:** `config.Types [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 323210.0
- **Functions:** 0/19 matched (target 7)
- **Missing functions:** `text`, `text_values`, `diff_text`, `unified_diff`, `message_texts`, `user_prompt`, `error_summary`, `is_assistant`, `summary`, `assistant_text_messages`, `user_text_prompt`, `assistant_error_message`, `deserialize_vec`, `fixture`, `unified_diff_prefers_current_diff_task_turn`, `unified_diff_falls_back_to_pr_output_diff`, `assistant_text_messages_extracts_text_content`, `user_text_prompt_joins_parts_with_spacing`, `assistant_error_message_combines_code_and_message`
- **Types:** 0/13 matched (target 28)
- **Missing types:** `CodeTaskDetailsResponse`, `Turn`, `TurnItem`, `ContentFragment`, `StructuredContent`, `DiffPayload`, `Worklog`, `WorklogMessage`, `Author`, `WorklogContent`, `TurnError`, `CodeTaskDetailsResponseExt`, `TurnAttemptsSiblingTurnsResponse`
- **Tests:** 0/6 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `types.rs` vs expected `types.rs`
- **Proposed provenance header:** `// port-lint: source types.rs` (current: `// port-lint: source types.rs`)
- **Lint issues:** 1

### 52. sandboxing.seatbelt

- **Target:** `core.Seatbelt [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 272910.0
- **Functions:** 2/24 matched (target 2)
- **Missing functions:** `is_loopback_host`, `proxy_scheme_default_port`, `proxy_loopback_ports_from_env`, `default`, `proxy_policy_inputs`, `normalize_path_for_sandbox`, `unix_socket_path_params`, `unix_socket_path_param_key`, `unix_socket_dir_params`, `unix_socket_policy`, `dynamic_network_policy`, `dynamic_network_policy_for_network`, `root_absolute_path`, `build_seatbelt_access_policy`, `seatbelt_protected_metadata_name_regex`, `protected_metadata_names_for_writable_root`, `build_seatbelt_unreadable_glob_policy`, `canonicalize_glob_static_prefix_for_sandbox`, `seatbelt_regex_for_unreadable_glob`, `create_seatbelt_command_args_for_legacy_policy`, `confstr`, `confstr_path`
- **Types:** 0/5 matched (target 0)
- **Missing types:** `ProxyPolicyInputs`, `UnixDomainSocketPolicy`, `UnixSocketPathParam`, `SeatbeltAccessRoot`, `CreateSeatbeltCommandArgsParams`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `seatbelt.rs` vs expected `seatbelt.rs`
- **Proposed provenance header:** `// port-lint: source seatbelt.rs` (current: `// port-lint: source seatbelt.rs`)
- **TODOs:** 1
- **Lint issues:** 1

### 53. protocol.approvals

- **Target:** `protocol.Approvals [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 252910.0
- **Functions:** 0/9 matched (target 1)
- **Missing functions:** `new`, `command`, `from`, `effective_approval_id`, `effective_available_decisions`, `default_available_decisions`, `message`, `guardian_assessment_action_deserializes_command_shape`, `guardian_assessment_action_round_trips_execve_shape`
- **Types:** 4/20 matched (target 6)
- **Missing types:** `ResolvedPermissionProfile`, `EscalationPermissions`, `ExecPolicyAmendment`, `NetworkApprovalProtocol`, `NetworkApprovalContext`, `NetworkPolicyRuleAction`, `GuardianRiskLevel`, `GuardianUserAuthorization`, `GuardianAssessmentOutcome`, `GuardianAssessmentStatus`, `GuardianAssessmentDecisionSource`, `GuardianCommandSource`, `GuardianAssessmentAction`, `NetworkPolicyAmendment`, `GuardianAssessmentEvent`, `ElicitationRequest`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `approvals.rs` vs expected `approvals.rs`
- **Proposed provenance header:** `// port-lint: source approvals.rs` (current: `// port-lint: source approvals.rs`)
- **Lint issues:** 1

### 54. protocol.items

- **Target:** `protocol.Items [PROVENANCE-FALLBACK]`
- **Similarity:** 0.26
- **Dependents:** 0
- **Priority Score:** 243607.4
- **Functions:** 6/19 matched (target 10)
- **Missing functions:** `default`, `text_elements`, `local_image_paths`, `from_fragments`, `from_single_hook`, `build_hook_prompt_message`, `parse_hook_prompt_message`, `parse_hook_prompt_fragment`, `serialize_hook_prompt_fragment`, `as_legacy_begin_event`, `as_legacy_end_event`, `hook_prompt_roundtrips_multiple_fragments`, `hook_prompt_parses_legacy_single_hook_run_id`
- **Types:** 6/17 matched (target 11)
- **Missing types:** `HookPromptItem`, `HookPromptFragment`, `HookPromptXml`, `PlanItem`, `ImageViewItem`, `ImageGenerationItem`, `FileChangeItem`, `McpToolCallItem`, `McpToolCallStatus`, `McpToolCallError`, `ContextCompactionItem`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `items.rs` vs expected `items.rs`
- **Proposed provenance header:** `// port-lint: source items.rs` (current: `// port-lint: source items.rs`)
- **Lint issues:** 1

### 55. execpolicy.policy

- **Target:** `execpolicy.Policy`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 212809.1
- **Functions:** 5/24 matched (target 5)
- **Missing functions:** `new`, `from_parts`, `network_rules`, `host_executables`, `get_allowed_prefixes`, `add_prefix_rule`, `add_network_rule`, `set_host_executable_paths`, `merge_overlay`, `compiled_network_domains`, `check_with_options`, `check_multiple_with_options`, `matches_for_command`, `matches_for_command_with_options`, `match_exact_rules`, `match_host_executable_rules`, `upsert_domain`, `render_pattern_token`, `from_matches`
- **Types:** 2/4 matched
- **Missing types:** `HeuristicsFallback`, `MatchOptions`

### 56. tools.registry

- **Target:** `tools.Registry`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 203307.5
- **Functions:** 9/23 matched (target 11)
- **Missing functions:** `pre_tool_use_payload`, `post_tool_use_payload`, `create_diff_consumer`, `finish`, `into_response`, `code_mode_result`, `handle_any`, `empty_for_test`, `with_handler_for_test`, `has_handler`, `dispatch_any`, `from`, `hook_tool_kind`, `dispatch_after_tool_use_hook`
- **Types:** 4/10 matched (target 5)
- **Missing types:** `ToolArgumentDiffConsumer`, `AnyToolResult`, `PreToolUsePayload`, `PostToolUsePayload`, `AnyToolHandler`, `AfterToolUseHookDispatch`
- **Tests:** 0/3 matched

### 57. codex-api.rate_limits

- **Target:** `ratelimits.RateLimits [PROVENANCE-FALLBACK]`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 202608.1
- **Functions:** 5/21 matched (target 6)
- **Missing functions:** `fmt`, `parse_default_rate_limit`, `parse_all_rate_limits`, `parse_rate_limit_for_limit`, `parse_rate_limit_event`, `map_event_window`, `parse_promo_message`, `parse_header_str`, `has_rate_limit_data`, `header_name_to_limit_id`, `normalize_limit_id`, `parse_rate_limit_for_limit_defaults_to_codex_headers`, `parse_rate_limit_for_limit_reads_secondary_headers`, `parse_rate_limit_for_limit_prefers_limit_name_header`, `parse_all_rate_limits_reads_all_limit_families`, `parse_all_rate_limits_includes_default_codex_snapshot`
- **Types:** 1/5 matched (target 1)
- **Missing types:** `RateLimitEventWindow`, `RateLimitEventDetails`, `RateLimitEventCredits`, `RateLimitEvent`
- **Tests:** 0/5 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rate_limits.rs` vs expected `rate_limits.rs`
- **Proposed provenance header:** `// port-lint: source rate_limits.rs` (current: `// port-lint: source rate_limits.rs`)
- **Lint issues:** 1

### 58. handlers.apply_patch

- **Target:** `handlers.ApplyPatch`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 192308.9
- **Functions:** 3/20 matched (target 12)
- **Missing functions:** `consume_diff`, `finish`, `push_delta`, `finish_update_on_complete`, `convert_apply_patch_hunks_to_protocol`, `hunk_source_path`, `format_update_chunks_for_progress`, `file_paths_for_action`, `to_abs_path`, `write_permissions_for_paths`, `apply_patch_payload_command`, `effective_patch_permissions`, `kind`, `create_diff_consumer`, `pre_tool_use_payload`, `post_tool_use_payload`, `intercept_apply_patch`
- **Types:** 1/3 matched (target 12)
- **Missing types:** `ApplyPatchArgumentDiffConsumer`, `Output`

### 59. lmstudio.client

- **Target:** `client.ModelClient [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 171710.0
- **Functions:** 0/16 matched (target 34)
- **Missing functions:** `try_from_provider`, `check_server`, `load_model`, `fetch_models`, `find_lms`, `find_lms_with_home_dir`, `download_model`, `from_host_root`, `test_fetch_models_happy_path`, `test_fetch_models_no_data_array`, `test_fetch_models_server_error`, `test_check_server_happy_path`, `test_check_server_error`, `test_find_lms`, `test_find_lms_with_mock_home`, `test_from_host_root`
- **Types:** 0/1 matched (target 5)
- **Missing types:** `LMStudioClient`
- **Tests:** 0/9 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `client.rs` vs expected `client.rs`
- **Proposed provenance header:** `// port-lint: source client.rs` (current: `// port-lint: source client.rs`)
- **TODOs:** 11
- **Lint issues:** 12

### 60. mcp-server.codex_tool_config

- **Target:** `config.DurationSerializers`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 131310.0
- **Functions:** 0/9 matched (target 2)
- **Missing functions:** `from`, `create_tool_for_codex_tool_call_param`, `codex_tool_output_schema`, `into_config`, `get_thread_id`, `create_tool_for_codex_tool_call_reply_param`, `create_tool_input_schema`, `verify_codex_tool_json_schema`, `verify_codex_tool_reply_json_schema`
- **Types:** 0/4 matched (target 0)
- **Missing types:** `CodexToolCallParam`, `CodexToolCallApprovalPolicy`, `CodexToolCallSandboxMode`, `CodexToolCallReplyParam`
- **Tests:** 0/2 matched

### 61. command_safety.windows_safe_commands

- **Target:** `commandsafety.WindowsSafeCommands [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 121910.0
- **Functions:** 7/19 matched (target 10)
- **Missing functions:** `join_arguments_as_script`, `quote_argument`, `is_safe_powershell_words`, `vec_str`, `recognizes_safe_powershell_wrappers`, `accepts_full_path_powershell_invocations`, `allows_read_only_pipelines_and_git_usage`, `rejects_git_global_override_options`, `rejects_powershell_commands_with_side_effects`, `accepts_constant_expression_arguments`, `rejects_dynamic_arguments`, `uses_invoked_powershell_variant_for_parsing`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/8 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `core/src/commandSafety/windowsSafeCommands.rs` vs expected `command_safety/windows_safe_commands.rs`
- **Proposed provenance header:** `// port-lint: source command_safety/windows_safe_commands.rs` (current: `// port-lint: source core/src/commandSafety/windowsSafeCommands.rs`)
- **Lint issues:** 1

### 62. handlers.shell

- **Target:** `handlers.Shell`
- **Similarity:** 0.28
- **Dependents:** 0
- **Priority Score:** 121907.2
- **Functions:** 5/14 matched (target 9)
- **Missing functions:** `shell_payload_command`, `shell_command_payload_command`, `shell_runtime_backend`, `resolve_use_login_shell`, `base_command`, `from`, `kind`, `pre_tool_use_payload`, `post_tool_use_payload`
- **Types:** 2/5 matched (target 2)
- **Missing types:** `ShellCommandBackend`, `RunExecLikeArgs`, `Output`
- **Lint issues:** 3

### 63. core.client_common

- **Target:** `prompt.Prompt`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 121409.3
- **Functions:** 1/9 matched (target 2)
- **Missing functions:** `default`, `reserialize_shell_outputs`, `is_shell_tool_name`, `parse_structured_shell_output`, `build_structured_output`, `strip_total_output_header`, `poll_next`, `drop`
- **Types:** 1/5 matched (target 1)
- **Missing types:** `ExecOutputJson`, `ExecOutputMetadataJson`, `ResponseStream`, `Item`

### 64. shell-command.powershell

- **Target:** `core.PowerShell [PROVENANCE-FALLBACK]`
- **Similarity:** 0.06
- **Dependents:** 0
- **Priority Score:** 121309.4
- **Functions:** 1/13 matched (target 1)
- **Missing functions:** `prefix_powershell_script_with_utf8`, `parse_powershell_command_into_plain_commands`, `try_find_powershell_executable_blocking`, `try_find_pwsh_executable_blocking`, `try_find_powershellish_executable_in_path`, `is_powershellish_executable_available`, `extracts_basic_powershell_command`, `extracts_lowercase_flags`, `extracts_full_path_powershell_command`, `extracts_with_noprofile_and_alias`, `parses_plain_powershell_commands`, `parses_multiple_plain_powershell_commands`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/6 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `core/src/powershell.rs` vs expected `powershell.rs`
- **Proposed provenance header:** `// port-lint: source powershell.rs` (current: `// port-lint: source core/src/powershell.rs`)
- **Lint issues:** 1

### 65. login.device_code_auth

- **Target:** `auth.Hashing`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 121210.0
- **Functions:** 0/7 matched (target 18)
- **Missing functions:** `deserialize_interval`, `request_user_code`, `poll_for_token`, `print_device_code_prompt`, `request_device_code`, `complete_device_code_login`, `run_device_code_login`
- **Types:** 0/5 matched (target 1)
- **Missing types:** `DeviceCode`, `UserCodeResp`, `UserCodeReq`, `TokenPollReq`, `CodeSuccessResp`

### 66. tools.sandboxing

- **Target:** `tools.Sandboxing [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 102710.0
- **Functions:** 8/16 matched (target 12)
- **Missing functions:** `bash`, `proposed_execpolicy_amendment`, `default_exec_approval_requirement`, `sandbox_override_for_first_attempt`, `managed_network_for_sandbox_permissions`, `exec_approval_requirement`, `permission_request_payload`, `network_approval_spec`
- **Types:** 9/11 matched (target 19)
- **Missing types:** `PermissionRequestPayload`, `ExecApprovalRequirement`
- **Lint issues:** 1

### 67. handlers.unified_exec

- **Target:** `handlers.UnifiedExec`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 101607.8
- **Functions:** 4/12 matched (target 5)
- **Missing functions:** `default_exec_yield_time_ms`, `default_write_stdin_yield_time_ms`, `default_tty`, `effective_max_output_tokens`, `kind`, `pre_tool_use_payload`, `post_tool_use_payload`, `emit_unified_exec_tty_metric`
- **Types:** 2/4 matched (target 2)
- **Missing types:** `WriteStdinArgs`, `Output`
- **Lint issues:** 1

### 68. core.message_history

- **Target:** `protocol.MessageHistory [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 101110.0
- **Functions:** 0/10 matched (target 0)
- **Missing functions:** `history_filepath`, `append_entry`, `enforce_history_limit`, `trim_target_bytes`, `history_metadata`, `lookup`, `ensure_owner_only_permissions`, `history_metadata_for_file`, `lookup_history_entry`, `history_log_id`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `message_history.rs` vs expected `message_history.rs`
- **Proposed provenance header:** `// port-lint: source message_history.rs` (current: `// port-lint: source message_history.rs`)
- **Lint issues:** 1

### 69. write.storage

- **Target:** `auth.Storage [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 101010.0
- **Functions:** 0/10 matched (target 30)
- **Missing functions:** `rebuild_raw_memories_file_from_memories`, `sync_rollout_summaries_from_memories`, `rebuild_raw_memories_file`, `prune_rollout_summaries`, `write_rollout_summary_for_thread`, `retained_memories`, `raw_memories_format_error`, `rollout_summary_format_error`, `rollout_summary_file_stem`, `rollout_summary_file_stem_from_parts`
- **Types:** 0/0 matched (target 8)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `storage.rs` vs expected `storage.rs`
- **Proposed provenance header:** `// port-lint: source storage.rs` (current: `// port-lint: source storage.rs`)
- **Lint issues:** 3

### 70. tui.terminal_palette

- **Target:** `tui.TerminalPalette`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 91708.2
- **Functions:** 7/14 matched (target 7)
- **Missing functions:** `stdout_color_level`, `rgb_color`, `indexed_color`, `default`, `get_or_init_with`, `default_colors_cache`, `color_to_tuple`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `StdoutColorLevel`, `Cache`

### 71. handlers.view_image

- **Target:** `handlers.ViewImage`
- **Similarity:** 0.06
- **Dependents:** 0
- **Priority Score:** 91209.4
- **Functions:** 1/7 matched (target 1)
- **Missing functions:** `kind`, `log_preview`, `success_for_logging`, `to_response_item`, `code_mode_result`, `code_mode_result_returns_image_url_object`
- **Types:** 2/5 matched (target 2)
- **Missing types:** `ViewImageDetail`, `Output`, `ViewImageOutput`
- **Tests:** 0/1 matched

### 72. handlers.plan

- **Target:** `handlers.Plan`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 91109.5
- **Functions:** 1/8 matched (target 1)
- **Missing functions:** `log_preview`, `success_for_logging`, `to_response_item`, `code_mode_result`, `kind`, `handle_update_plan`, `parse_update_plan_arguments`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `PlanToolOutput`, `Output`

### 73. terminal-detection.lib

- **Target:** `terminal.TerminalDetection [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 83310.0
- **Functions:** 20/27 matched (target 23)
- **Missing functions:** `new`, `var_non_empty`, `var`, `user_agent`, `terminal_info`, `tmux_display_message`, `none_if_whitespace`
- **Types:** 5/6 matched (target 8)
- **Missing types:** `ProcessEnvironment`

### 74. runtimes.unified_exec

- **Target:** `runtimes.UnifiedExec [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 81610.0
- **Functions:** 5/12 matched (target 8)
- **Missing functions:** `unified_exec_options`, `new`, `approval_keys`, `exec_approval_requirement`, `permission_request_payload`, `network_approval_spec`, `unified_exec_options_combines_default_timeout_with_network_denial_cancellation`
- **Types:** 3/4 matched (target 3)
- **Missing types:** `ApprovalKey`
- **Tests:** 0/1 matched
- **Lint issues:** 1

### 75. runtimes.shell

- **Target:** `runtimes.Shell`
- **Similarity:** 0.39
- **Dependents:** 0
- **Priority Score:** 81606.1
- **Functions:** 6/12 matched (target 10)
- **Missing functions:** `new`, `for_shell_command`, `approval_keys`, `exec_approval_requirement`, `permission_request_payload`, `network_approval_spec`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `ShellRuntimeBackend`, `ApprovalKey`

### 76. ollama.client

- **Target:** `ollama.Client`
- **Similarity:** 0.38
- **Dependents:** 0
- **Priority Score:** 81506.2
- **Functions:** 6/14 matched (target 10)
- **Missing functions:** `try_from_provider_with_base_url`, `fetch_version`, `from_host_root`, `test_fetch_models_happy_path`, `test_fetch_version`, `test_probe_server_happy_path_openai_compat_and_native`, `test_try_from_oss_provider_ok_when_server_running`, `test_try_from_oss_provider_err_when_server_missing`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Tests:** 0/7 matched
- **Lint issues:** 1

### 77. handlers.mcp

- **Target:** `handlers.Mcp`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 81009.3
- **Functions:** 1/8 matched (target 1)
- **Missing functions:** `kind`, `pre_tool_use_payload`, `post_tool_use_payload`, `mcp_hook_tool_input`, `mcp_pre_tool_use_payload_uses_model_tool_name_and_raw_args`, `mcp_post_tool_use_payload_uses_model_tool_name_args_and_result`, `mcp_hook_tool_input_defaults_empty_args_to_object`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Output`
- **Tests:** 0/3 matched

### 78. model-provider.auth

- **Target:** `api.Auth [PROVENANCE-FALLBACK]`
- **Similarity:** 0.06
- **Dependents:** 0
- **Priority Score:** 80909.4
- **Functions:** 1/7 matched (target 2)
- **Missing functions:** `unauthenticated_auth_provider`, `auth_manager_for_provider`, `resolve_provider_auth`, `bearer_auth_for_provider`, `auth_provider_from_auth`, `unauthenticated_auth_provider_adds_no_headers`
- **Types:** 0/2 matched (target 1)
- **Missing types:** `AgentIdentityAuthProvider`, `UnauthenticatedAuthProvider`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `auth.rs` vs expected `auth.rs`
- **Proposed provenance header:** `// port-lint: source auth.rs` (current: `// port-lint: source auth.rs`)
- **Lint issues:** 1

### 79. suite.exec

- **Target:** `core.ExecExpiration [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 80810.0
- **Functions:** 0/8 matched (target 3)
- **Missing functions:** `skip_test`, `run_test_cmd`, `exit_code_0_succeeds`, `truncates_output_lines`, `truncates_output_bytes`, `exit_command_not_found_is_ok`, `openpty_works_under_real_exec_seatbelt_path`, `write_file_fails_as_sandbox_error`
- **Types:** 0/0 matched (target 6)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only by basename: `core/src/exec.rs` vs expected `core/tests/suite/exec.rs`
- **Proposed provenance header:** `// port-lint: source core/tests/suite/exec.rs` (current: `// port-lint: source core/src/exec.rs`)
- **Lint issues:** 1

### 80. handlers.list_dir

- **Target:** `handlers.ListDir`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 71606.6
- **Functions:** 5/11 matched (target 6)
- **Missing functions:** `default_offset`, `default_limit`, `default_depth`, `kind`, `list_dir_slice_with_policy`, `from`
- **Types:** 4/5 matched (target 4)
- **Missing types:** `Output`

### 81. execpolicy.error

- **Target:** `execpolicy.ExecPolicyError [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 70710.0
- **Functions:** 0/2 matched (target 0)
- **Missing functions:** `with_location`, `location`
- **Types:** 0/5 matched (target 7)
- **Missing types:** `Result`, `TextPosition`, `TextRange`, `ErrorLocation`, `Error`

### 82. runtimes.apply_patch

- **Target:** `runtimes.ApplyPatch`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 61406.6
- **Functions:** 5/11 matched
- **Missing functions:** `new`, `build_guardian_review_request`, `file_system_sandbox_context_for_attempt`, `approval_keys`, `exec_approval_requirement`, `permission_request_payload`
- **Types:** 3/3 matched
- **Missing types:** _none_

### 83. protocol.exec_output

- **Target:** `protocol.ExecOutput`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 61008.8
- **Functions:** 2/8 matched (target 3)
- **Missing functions:** `new`, `bytes_to_string_smart`, `detect_encoding`, `decode_bytes`, `looks_like_windows_1252_punctuation`, `is_windows_1252_punct`
- **Types:** 2/2 matched
- **Missing types:** _none_

### 84. core.util

- **Target:** `core.Util [PROVENANCE-FALLBACK]`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 60808.1
- **Functions:** 2/7 matched (target 3)
- **Missing functions:** `from_optional_fields`, `emit_feedback_auth_recovery_tags`, `resolve_path`, `normalize_thread_name`, `resume_command`
- **Types:** 0/1 matched (target 0)
- **Missing types:** `Auth401FeedbackSnapshot`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `util.rs` vs expected `util.rs`
- **Proposed provenance header:** `// port-lint: source util.rs` (current: `// port-lint: source util.rs`)
- **Lint issues:** 1

### 85. exec.exec_events

- **Target:** `exec.ExecEvents`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 53300.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 28/33 matched (target 43)
- **Missing types:** `CollabToolCallStatus`, `CollabTool`, `CollabAgentStatus`, `CollabAgentState`, `CollabToolCallItem`

### 86. handlers.mcp_resource

- **Target:** `handlers.McpResource`
- **Similarity:** 0.42
- **Dependents:** 0
- **Priority Score:** 52705.8
- **Functions:** 13/17 matched (target 13)
- **Missing functions:** `new`, `from_single_server`, `from_all_servers`, `kind`
- **Types:** 9/10 matched (target 9)
- **Missing types:** `Output`

### 87. tools.parallel

- **Target:** `tools.Parallel`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 50907.8
- **Functions:** 3/8 matched (target 5)
- **Missing functions:** `new`, `find_spec`, `create_diff_consumer`, `handle_tool_call_with_source`, `failure_response`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 88. protocol.num_format

- **Target:** `protocol.NumFormat`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 50809.0
- **Functions:** 3/8 matched (target 4)
- **Missing functions:** `make_local_formatter`, `make_en_us_formatter`, `formatter`, `format_with_separators_with_formatter`, `format_si_suffix_with_formatter`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 89. tools.orchestrator

- **Target:** `tools.Orchestrator`
- **Similarity:** 0.26
- **Dependents:** 0
- **Priority Score:** 50807.4
- **Functions:** 2/6 matched (target 2)
- **Missing functions:** `new`, `run_attempt`, `request_approval`, `reject_if_not_approved`
- **Types:** 1/2 matched
- **Missing types:** `OrchestratorRunResult`
- **Lint issues:** 1

### 90. sandbox-summary.sandbox_summary

- **Target:** `common.SandboxSummary`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 50608.7
- **Functions:** 1/6 matched (target 1)
- **Missing functions:** `summarize_permission_profile`, `summarizes_external_sandbox_without_network_access_suffix`, `summarizes_external_sandbox_with_enabled_network`, `summarizes_read_only_with_enabled_network`, `workspace_write_summary_still_includes_network_access`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Tests:** 0/4 matched

### 91. handlers.test_sync

- **Target:** `handlers.TestSync`
- **Similarity:** 0.26
- **Dependents:** 0
- **Priority Score:** 41007.4
- **Functions:** 2/5 matched (target 2)
- **Missing functions:** `default_timeout_ms`, `barrier_map`, `kind`
- **Types:** 4/5 matched (target 4)
- **Missing types:** `Output`

### 92. tools.spec

- **Target:** `tools.Spec`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 40410.0
- **Functions:** 0/3 matched (target 17)
- **Missing functions:** `tool_user_shell_type`, `map_mcp_tools_for_plan`, `build_specs_with_discoverable_tools`
- **Types:** 0/1 matched (target 11)
- **Missing types:** `McpToolPlanInputs`

### 93. aws-auth.config

- **Target:** `otel.Config [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30310.0
- **Functions:** 0/3 matched (target 0)
- **Missing functions:** `load_sdk_config`, `credentials_provider`, `resolved_region`
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `config.rs` vs expected `config.rs`
- **Proposed provenance header:** `// port-lint: source config.rs` (current: `// port-lint: source config.rs`)
- **Lint issues:** 1

### 94. app-server.models

- **Target:** `protocol.Models [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30310.0
- **Functions:** 0/3 matched (target 12)
- **Missing functions:** `supported_models`, `model_from_preset`, `reasoning_efforts_from_preset`
- **Types:** 0/0 matched (target 56)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `models.rs` vs expected `models.rs`
- **Proposed provenance header:** `// port-lint: source models.rs` (current: `// port-lint: source models.rs`)
- **Lint issues:** 1

### 95. mcp-server.tests.common.responses

- **Target:** `requests.ResponsesRequest [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30310.0
- **Functions:** 0/3 matched (target 14)
- **Missing functions:** `create_shell_command_sse_response`, `create_final_assistant_message_sse_response`, `create_apply_patch_sse_response`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only by basename: `responses.rs` vs expected `mcp-server/tests/common/responses.rs`
- **Proposed provenance header:** `// port-lint: source mcp-server/tests/common/responses.rs` (current: `// port-lint: source responses.rs`)
- **TODOs:** 5
- **Lint issues:** 1

### 96. unified_exec.errors

- **Target:** `unifiedexec.Errors`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 20406.9
- **Functions:** 1/3 matched (target 2)
- **Missing functions:** `create_process`, `process_failed`
- **Types:** 1/1 matched (target 7)
- **Missing types:** _none_

### 97. unified_exec.session

- **Target:** `unifiedexec.Session [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20210.0
- **Functions:** 0/2 matched (target 15)
- **Missing functions:** `spawn_windows_sandbox_session_legacy`, `spawn_windows_sandbox_session_elevated`
- **Types:** 0/0 matched (target 6)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `core/src/unifiedExec/session.rs` vs expected `unified_exec/session.rs`
- **Proposed provenance header:** `// port-lint: source unified_exec/session.rs` (current: `// port-lint: source core/src/unifiedExec/session.rs`)
- **Lint issues:** 1

### 98. thread-store.error

- **Target:** `core.ErrorTest [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20210.0
- **Functions:** 0/0 matched (target 21)
- **Missing functions:** _none_
- **Types:** 0/2 matched (target 1)
- **Missing types:** `ThreadStoreResult`, `ThreadStoreError`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `error.rs` vs expected `error.rs`
- **Proposed provenance header:** `// port-lint: source error.rs` (current: `// port-lint: source error.rs`)
- **Lint issues:** 1

### 99. codex-api.error

- **Target:** `core.Error [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20210.0
- **Functions:** 0/1 matched (target 62)
- **Missing functions:** `from`
- **Types:** 0/1 matched (target 42)
- **Missing types:** `ApiError`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `error.rs` vs expected `error.rs`
- **Proposed provenance header:** `// port-lint: source error.rs` (current: `// port-lint: source error.rs`)
- **Lint issues:** 1

### 100. execpolicy-legacy.error

- **Target:** `error.ApiError [PROVENANCE-FALLBACK]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 20200.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/2 matched (target 9)
- **Missing types:** `Result`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `error.rs` vs expected `error.rs`
- **Proposed provenance header:** `// port-lint: source error.rs` (current: `// port-lint: source error.rs`)
- **Lint issues:** 1

### 101. agent-graph-store.error

- **Target:** `error.TransportError [PROVENANCE-FALLBACK]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 20200.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/2 matched (target 8)
- **Missing types:** `AgentGraphStoreResult`, `AgentGraphStoreError`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `error.rs` vs expected `error.rs`
- **Proposed provenance header:** `// port-lint: source error.rs` (current: `// port-lint: source error.rs`)
- **Lint issues:** 1

### 102. tools.events

- **Target:** `tools.Events`
- **Similarity:** 0.74
- **Dependents:** 0
- **Priority Score:** 11802.6
- **Functions:** 11/12 matched (target 11)
- **Missing functions:** `new`
- **Types:** 6/6 matched (target 14)
- **Missing types:** _none_

### 103. render.mod

- **Target:** `render.Render [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10510.0
- **Functions:** 3/3 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 1)
- **Missing types:** `RectExt`

### 104. core.review_format

- **Target:** `core.ReviewFormat`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 10304.3
- **Functions:** 2/3 matched (target 2)
- **Missing functions:** `render_review_output_text`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 105. core.landlock

- **Target:** `core.Landlock`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched
- **Missing functions:** `spawn_command_under_linux_sandbox`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 106. tools.plan_tool

- **Target:** `protocol.PlanTool [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 0)
- **Missing functions:** `create_update_plan_tool`
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `plan_tool.rs` vs expected `plan_tool.rs`
- **Proposed provenance header:** `// port-lint: source plan_tool.rs` (current: `// port-lint: source plan_tool.rs`)
- **Lint issues:** 1

### 107. suite.user_notification

- **Target:** `core.UserNotification`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 4)
- **Missing functions:** `summarize_context_three_requests_and_instructions`
- **Types:** 0/0 matched (target 4)
- **Missing types:** _none_
- **Lint issues:** 1

### 108. ollama.pull

- **Target:** `ollama.Pull`
- **Similarity:** 0.73
- **Dependents:** 0
- **Priority Score:** 702.7
- **Functions:** 3/3 matched (target 4)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 9)
- **Missing types:** _none_

### 109. requests.headers

- **Target:** `requests.Headers [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 310.0
- **Functions:** 3/3 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **TODOs:** 1

### 110. login.pkce

- **Target:** `login.Pkce`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 202.5
- **Functions:** 1/1 matched (target 2)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 111. codex-client.telemetry

- **Target:** `telemetry.Telemetry [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `telemetry.rs` vs expected `telemetry.rs`
- **Proposed provenance header:** `// port-lint: source telemetry.rs` (current: `// port-lint: source telemetry.rs`)
- **Lint issues:** 1

### 112. core.function_tool

- **Target:** `core.FunctionTool [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 4)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 113. models-manager.model_presets

- **Target:** `common.ModelPresets [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `common/src/modelPresets.rs` vs expected `model_presets.rs`
- **Proposed provenance header:** `// port-lint: source model_presets.rs` (current: `// port-lint: source common/src/modelPresets.rs`)
- **Lint issues:** 1

### 114. core.flags

- **Target:** `core.Flags`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 115. tui.ui_consts

- **Target:** `tui.UiConsts`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `keyring-store.lib` | `keyringstore.src.Lib` | 0 | `keyring-store/src/lib.rs` | `keyringstore/src/Lib.kt` |
| `agent-identity.lib` | `agentidentity.src.Lib` | 0 | `agent-identity/src/lib.rs` | `agentidentity/src/Lib.kt` |
| `analytics.lib` | `analytics.src.Lib` | 0 | `analytics/src/lib.rs` | `analytics/src/Lib.kt` |
| `ansi-escape.lib` | `ansiescape.src.Lib` | 0 | `ansi-escape/src/lib.rs` | `ansiescape/src/Lib.kt` |
| `app-server-client.lib` | `appserverclient.src.Lib` | 0 | `app-server-client/src/lib.rs` | `appserverclient/src/Lib.kt` |
| `app-server-protocol.lib` | `appserverprotocol.src.Lib` | 0 | `app-server-protocol/src/lib.rs` | `appserverprotocol/src/Lib.kt` |
| `protocol.mod` | `appserverprotocol.src.protocol.Mod` | 0 | `app-server-protocol/src/protocol/mod.rs` | `appserverprotocol/src/protocol/Mod.kt` |
| `app-server-test-client.lib` | `appservertestclient.src.Lib` | 0 | `app-server-test-client/src/lib.rs` | `appservertestclient/src/Lib.kt` |
| `app-server-transport.lib` | `appservertransport.src.Lib` | 0 | `app-server-transport/src/lib.rs` | `appservertransport/src/Lib.kt` |
| `transport.mod` | `appservertransport.src.transport.Mod` | 0 | `app-server-transport/src/transport/mod.rs` | `appservertransport/src/transport/Mod.kt` |
| `remote_control.mod` | `appservertransport.src.transport.remotecontrol.Mod` | 0 | `app-server-transport/src/transport/remote_control/mod.rs` | `appservertransport/src/transport/remotecontrol/Mod.kt` |
| `app-server.config.mod` | `appserver.src.config.Mod` | 0 | `app-server/src/config/mod.rs` | `appserver/src/config/Mod.kt` |
| `app-server.lib` | `appserver.src.Lib` | 0 | `app-server/src/lib.rs` | `appserver/src/Lib.kt` |
| `app-server.tests.common.lib` | `appserver.tests.common.Lib` | 0 | `app-server/tests/common/lib.rs` | `appserver/tests/common/Lib.kt` |
| `app-server.tests.suite.mod` | `appserver.tests.suite.Mod` | 0 | `app-server/tests/suite/mod.rs` | `appserver/tests/suite/Mod.kt` |
| `v2.mod` | `appserver.tests.suite.v2.Mod` | 0 | `app-server/tests/suite/v2/mod.rs` | `appserver/tests/suite/v2/Mod.kt` |
| `apply-patch.lib` | `applypatch.src.Lib` | 0 | `apply-patch/src/lib.rs` | `applypatch/src/Lib.kt` |
| `apply-patch.tests.suite.mod` | `applypatch.tests.suite.Mod` | 0 | `apply-patch/tests/suite/mod.rs` | `applypatch/tests/suite/Mod.kt` |
| `arg0.lib` | `arg0.src.Lib` | 0 | `arg0/src/lib.rs` | `arg0/src/Lib.kt` |
| `async-utils.lib` | `asyncutils.src.Lib` | 0 | `async-utils/src/lib.rs` | `asyncutils/src/Lib.kt` |
| `aws-auth.lib` | `awsauth.src.Lib` | 0 | `aws-auth/src/lib.rs` | `awsauth/src/Lib.kt` |
| `backend-client.lib` | `backendclient.src.Lib` | 0 | `backend-client/src/lib.rs` | `backendclient/src/Lib.kt` |
| `chatgpt.lib` | `chatgpt.src.Lib` | 0 | `chatgpt/src/lib.rs` | `chatgpt/src/Lib.kt` |
| `chatgpt.tests.suite.mod` | `chatgpt.tests.suite.Mod` | 0 | `chatgpt/tests/suite/mod.rs` | `chatgpt/tests/suite/Mod.kt` |
| `desktop_app.mod` | `cli.src.desktopapp.Mod` | 0 | `cli/src/desktop_app/mod.rs` | `cli/src/desktopapp/Mod.kt` |
| `cli.lib` | `cli.src.Lib` | 0 | `cli/src/lib.rs` | `cli/src/Lib.kt` |
| `cloud-requirements.lib` | `cloudrequirements.src.Lib` | 0 | `cloud-requirements/src/lib.rs` | `cloudrequirements/src/Lib.kt` |
| `cloud-tasks-client.lib` | `cloudtasksclient.src.Lib` | 0 | `cloud-tasks-client/src/lib.rs` | `cloudtasksclient/src/Lib.kt` |
| `cloud-tasks-mock-client.lib` | `cloudtasksmockclient.src.Lib` | 0 | `cloud-tasks-mock-client/src/lib.rs` | `cloudtasksmockclient/src/Lib.kt` |
| `cloud-tasks.lib` | `cloudtasks.src.Lib` | 0 | `cloud-tasks/src/lib.rs` | `cloudtasks/src/Lib.kt` |
| `code-mode.lib` | `codemode.src.Lib` | 0 | `code-mode/src/lib.rs` | `codemode/src/Lib.kt` |
| `runtime.mod` | `codemode.src.runtime.Mod` | 0 | `code-mode/src/runtime/mod.rs` | `codemode/src/runtime/Mod.kt` |
| `endpoint.mod` | `codexapi.src.endpoint.Mod` | 0 | `codex-api/src/endpoint/mod.rs` | `codexapi/src/endpoint/Mod.kt` |
| `realtime_websocket.mod` | `codexapi.src.endpoint.realtimewebsocket.Mod` | 0 | `codex-api/src/endpoint/realtime_websocket/mod.rs` | `codexapi/src/endpoint/realtimewebsocket/Mod.kt` |
| `codex-api.lib` | `codexapi.src.Lib` | 0 | `codex-api/src/lib.rs` | `codexapi/src/Lib.kt` |
| `requests.mod` | `codexapi.src.requests.Mod` | 0 | `codex-api/src/requests/mod.rs` | `codexapi/src/requests/Mod.kt` |
| `sse.mod` | `codexapi.src.sse.Mod` | 0 | `codex-api/src/sse/mod.rs` | `codexapi/src/sse/Mod.kt` |
| `codex-backend-openapi-models.lib` | `codexbackendopenapimodels.src.Lib` | 0 | `codex-backend-openapi-models/src/lib.rs` | `codexbackendopenapimodels/src/Lib.kt` |
| `models.mod` | `codexbackendopenapimodels.src.models.Mod` | 0 | `codex-backend-openapi-models/src/models/mod.rs` | `codexbackendopenapimodels/src/models/Mod.kt` |
| `codex-client.lib` | `codexclient.src.Lib` | 0 | `codex-client/src/lib.rs` | `codexclient/src/Lib.kt` |
| `codex-experimental-api-macros.lib` | `codexexperimentalapimacros.src.Lib` | 0 | `codex-experimental-api-macros/src/lib.rs` | `codexexperimentalapimacros/src/Lib.kt` |
| `codex-mcp.lib` | `codexmcp.src.Lib` | 0 | `codex-mcp/src/lib.rs` | `codexmcp/src/Lib.kt` |
| `mcp.mod` | `codexmcp.src.mcp.Mod` | 0 | `codex-mcp/src/mcp/mod.rs` | `codexmcp/src/mcp/Mod.kt` |
| `collaboration-mode-templates.lib` | `collaborationmodetemplates.src.Lib` | 0 | `collaboration-mode-templates/src/lib.rs` | `collaborationmodetemplates/src/Lib.kt` |
| `config.lib` | `config.src.Lib` | 0 | `config/src/lib.rs` | `config/src/Lib.kt` |
| `loader.mod` | `config.src.loader.Mod` | 0 | `config/src/loader/mod.rs` | `config/src/loader/Mod.kt` |
| `connectors.lib` | `connectors.src.Lib` | 0 | `connectors/src/lib.rs` | `connectors/src/Lib.kt` |
| `core-api.lib` | `coreapi.src.Lib` | 0 | `core-api/src/lib.rs` | `coreapi/src/Lib.kt` |
| `core-plugins.lib` | `coreplugins.src.Lib` | 0 | `core-plugins/src/lib.rs` | `coreplugins/src/Lib.kt` |
| `core-skills.lib` | `coreskills.src.Lib` | 0 | `core-skills/src/lib.rs` | `coreskills/src/Lib.kt` |
| `agent.mod` | `core.src.agent.Mod` | 0 | `core/src/agent/mod.rs` | `core/src/agent/Mod.kt` |
| `apps.mod` | `core.src.apps.Mod` | 0 | `core/src/apps/mod.rs` | `core/src/apps/Mod.kt` |
| `config.mod` | `core.src.config.Mod` | 0 | `core/src/config/mod.rs` | `core/src/config/Mod.kt` |
| `context.mod` | `core.src.context.Mod` | 0 | `core/src/context/mod.rs` | `core/src/context/Mod.kt` |
| `context_manager.mod` | `core.src.contextmanager.Mod` | 0 | `core/src/context_manager/mod.rs` | `core/src/contextmanager/Mod.kt` |
| `guardian.mod` | `core.src.guardian.Mod` | 0 | `core/src/guardian/mod.rs` | `core/src/guardian/Mod.kt` |
| `core.lib` | `core.src.Lib` | 0 | `core/src/lib.rs` | `core/src/Lib.kt` |
| `plugins.mod` | `core.src.plugins.Mod` | 0 | `core/src/plugins/mod.rs` | `core/src/plugins/Mod.kt` |
| `sandboxing.mod` | `core.src.sandboxing.Mod` | 0 | `core/src/sandboxing/mod.rs` | `core/src/sandboxing/Mod.kt` |
| `session.mod` | `core.src.session.Mod` | 0 | `core/src/session/mod.rs` | `core/src/session/Mod.kt` |
| `state.mod` | `core.src.state.Mod` | 0 | `core/src/state/mod.rs` | `core/src/state/Mod.kt` |
| `tasks.mod` | `core.src.tasks.Mod` | 0 | `core/src/tasks/mod.rs` | `core/src/tasks/Mod.kt` |
| `code_mode.mod` | `core.src.tools.codemode.Mod` | 0 | `core/src/tools/code_mode/mod.rs` | `core/src/tools/codemode/Mod.kt` |
| `handlers.mod` | `core.src.tools.handlers.Mod` | 0 | `core/src/tools/handlers/mod.rs` | `core/src/tools/handlers/Mod.kt` |
| `tools.mod` | `core.src.tools.Mod` | 0 | `core/src/tools/mod.rs` | `core/src/tools/Mod.kt` |
| `runtimes.mod` | `core.src.tools.runtimes.Mod` | 0 | `core/src/tools/runtimes/mod.rs` | `core/src/tools/runtimes/Mod.kt` |
| `unified_exec.mod` | `core.src.unifiedexec.Mod` | 0 | `core/src/unified_exec/mod.rs` | `core/src/unifiedexec/Mod.kt` |
| `utils.mod` | `core.src.utils.Mod` | 0 | `core/src/utils/mod.rs` | `core/src/utils/Mod.kt` |
| `common.lib` | `core.tests.common.Lib` | 0 | `core/tests/common/lib.rs` | `core/tests/common/Lib.kt` |
| `core.tests.suite.mod` | `core.tests.suite.Mod` | 0 | `core/tests/suite/mod.rs` | `core/tests/suite/Mod.kt` |
| `device-key.lib` | `devicekey.src.Lib` | 0 | `device-key/src/lib.rs` | `devicekey/src/Lib.kt` |
| `exec-server.lib` | `execserver.src.Lib` | 0 | `exec-server/src/lib.rs` | `execserver/src/Lib.kt` |
| `common.mod` | `execserver.tests.common.Mod` | 0 | `exec-server/tests/common/mod.rs` | `execserver/tests/common/Mod.kt` |
| `exec.lib` | `exec.src.Lib` | 0 | `exec/src/lib.rs` | `exec/src/Lib.kt` |
| `exec.tests.suite.mod` | `exec.tests.suite.Mod` | 0 | `exec/tests/suite/mod.rs` | `exec/tests/suite/Mod.kt` |
| `execpolicy-legacy.lib` | `execpolicylegacy.src.Lib` | 0 | `execpolicy-legacy/src/lib.rs` | `execpolicylegacy/src/Lib.kt` |
| `execpolicy-legacy.tests.suite.mod` | `execpolicylegacy.tests.suite.Mod` | 0 | `execpolicy-legacy/tests/suite/mod.rs` | `execpolicylegacy/tests/suite/Mod.kt` |
| `execpolicy.lib` | `execpolicy.src.Lib` | 0 | `execpolicy/src/lib.rs` | `execpolicy/src/Lib.kt` |
| `external-agent-migration.lib` | `externalagentmigration.src.Lib` | 0 | `external-agent-migration/src/lib.rs` | `externalagentmigration/src/Lib.kt` |
| `external-agent-sessions.lib` | `externalagentsessions.src.Lib` | 0 | `external-agent-sessions/src/lib.rs` | `externalagentsessions/src/Lib.kt` |
| `features.lib` | `features.src.Lib` | 0 | `features/src/lib.rs` | `features/src/Lib.kt` |
| `feedback.lib` | `feedback.src.Lib` | 0 | `feedback/src/lib.rs` | `feedback/src/Lib.kt` |
| `file-search.lib` | `filesearch.src.Lib` | 0 | `file-search/src/lib.rs` | `filesearch/src/Lib.kt` |
| `file-system.lib` | `filesystem.src.Lib` | 0 | `file-system/src/lib.rs` | `filesystem/src/Lib.kt` |
| `git-utils.lib` | `gitutils.src.Lib` | 0 | `git-utils/src/lib.rs` | `gitutils/src/Lib.kt` |
| `engine.mod` | `hooks.src.engine.Mod` | 0 | `hooks/src/engine/mod.rs` | `hooks/src/engine/Mod.kt` |
| `hooks.events.mod` | `hooks.src.events.Mod` | 0 | `hooks/src/events/mod.rs` | `hooks/src/events/Mod.kt` |
| `hooks.lib` | `hooks.src.Lib` | 0 | `hooks/src/lib.rs` | `hooks/src/Lib.kt` |
| `install-context.lib` | `installcontext.src.Lib` | 0 | `install-context/src/lib.rs` | `installcontext/src/Lib.kt` |
| `agent-graph-store.lib` | `agentgraphstore.src.Lib` | 0 | `agent-graph-store/src/lib.rs` | `agentgraphstore/src/Lib.kt` |
| `linux-sandbox.lib` | `linuxsandbox.src.Lib` | 0 | `linux-sandbox/src/lib.rs` | `linuxsandbox/src/Lib.kt` |
| `linux-sandbox.tests.suite.mod` | `linuxsandbox.tests.suite.Mod` | 0 | `linux-sandbox/tests/suite/mod.rs` | `linuxsandbox/tests/suite/Mod.kt` |
| `lmstudio.lib` | `lmstudio.src.Lib` | 0 | `lmstudio/src/lib.rs` | `lmstudio/src/Lib.kt` |
| `auth.mod` | `login.src.auth.Mod` | 0 | `login/src/auth/mod.rs` | `login/src/auth/Mod.kt` |
| `login.lib` | `login.src.Lib` | 0 | `login/src/lib.rs` | `login/src/Lib.kt` |
| `login.tests.suite.mod` | `login.tests.suite.Mod` | 0 | `login/tests/suite/mod.rs` | `login/tests/suite/Mod.kt` |
| `mcp-server.lib` | `mcpserver.src.Lib` | 0 | `mcp-server/src/lib.rs` | `mcpserver/src/Lib.kt` |
| `tool_handlers.mod` | `mcpserver.src.toolhandlers.Mod` | 0 | `mcp-server/src/tool_handlers/mod.rs` | `mcpserver/src/toolhandlers/Mod.kt` |
| `mcp-server.tests.common.lib` | `mcpserver.tests.common.Lib` | 0 | `mcp-server/tests/common/lib.rs` | `mcpserver/tests/common/Lib.kt` |
| `mcp-server.tests.suite.mod` | `mcpserver.tests.suite.Mod` | 0 | `mcp-server/tests/suite/mod.rs` | `mcpserver/tests/suite/Mod.kt` |
| `mcp.lib` | `memories.mcp.src.Lib` | 0 | `memories/mcp/src/lib.rs` | `memories/mcp/src/Lib.kt` |
| `read.lib` | `memories.read.src.Lib` | 0 | `memories/read/src/lib.rs` | `memories/read/src/Lib.kt` |
| `extensions.mod` | `memories.write.src.extensions.Mod` | 0 | `memories/write/src/extensions/mod.rs` | `memories/write/src/extensions/Mod.kt` |
| `write.lib` | `memories.write.src.Lib` | 0 | `memories/write/src/lib.rs` | `memories/write/src/Lib.kt` |
| `model-provider-info.lib` | `modelproviderinfo.src.Lib` | 0 | `model-provider-info/src/lib.rs` | `modelproviderinfo/src/Lib.kt` |
| `amazon_bedrock.mod` | `modelprovider.src.amazonbedrock.Mod` | 0 | `model-provider/src/amazon_bedrock/mod.rs` | `modelprovider/src/amazonbedrock/Mod.kt` |
| `model-provider.lib` | `modelprovider.src.Lib` | 0 | `model-provider/src/lib.rs` | `modelprovider/src/Lib.kt` |
| `models-manager.lib` | `modelsmanager.src.Lib` | 0 | `models-manager/src/lib.rs` | `modelsmanager/src/Lib.kt` |
| `network-proxy.lib` | `networkproxy.src.Lib` | 0 | `network-proxy/src/lib.rs` | `networkproxy/src/Lib.kt` |
| `ollama.lib` | `ollama.src.Lib` | 0 | `ollama/src/lib.rs` | `ollama/src/Lib.kt` |
| `events.mod` | `otel.src.events.Mod` | 0 | `otel/src/events/mod.rs` | `otel/src/events/Mod.kt` |
| `otel.lib` | `otel.src.Lib` | 0 | `otel/src/lib.rs` | `otel/src/Lib.kt` |
| `metrics.mod` | `otel.src.metrics.Mod` | 0 | `otel/src/metrics/mod.rs` | `otel/src/metrics/Mod.kt` |
| `harness.mod` | `otel.tests.harness.Mod` | 0 | `otel/tests/harness/mod.rs` | `otel/tests/harness/Mod.kt` |
| `otel.tests.suite.mod` | `otel.tests.suite.Mod` | 0 | `otel/tests/suite/mod.rs` | `otel/tests/suite/Mod.kt` |
| `plugin.lib` | `plugin.src.Lib` | 0 | `plugin/src/lib.rs` | `plugin/src/Lib.kt` |
| `process-hardening.lib` | `processhardening.src.Lib` | 0 | `process-hardening/src/lib.rs` | `processhardening/src/Lib.kt` |
| `protocol.lib` | `protocol.src.Lib` | 0 | `protocol/src/lib.rs` | `protocol/src/Lib.kt` |
| `realtime-webrtc.lib` | `realtimewebrtc.src.Lib` | 0 | `realtime-webrtc/src/lib.rs` | `realtimewebrtc/src/Lib.kt` |
| `response-debug-context.lib` | `responsedebugcontext.src.Lib` | 0 | `response-debug-context/src/lib.rs` | `responsedebugcontext/src/Lib.kt` |
| `responses-api-proxy.lib` | `responsesapiproxy.src.Lib` | 0 | `responses-api-proxy/src/lib.rs` | `responsesapiproxy/src/Lib.kt` |
| `rmcp-client.lib` | `rmcpclient.src.Lib` | 0 | `rmcp-client/src/lib.rs` | `rmcpclient/src/Lib.kt` |
| `rollout-trace.lib` | `rollouttrace.src.Lib` | 0 | `rollout-trace/src/lib.rs` | `rollouttrace/src/Lib.kt` |
| `rollout-trace.model.mod` | `rollouttrace.src.model.Mod` | 0 | `rollout-trace/src/model/mod.rs` | `rollouttrace/src/model/Mod.kt` |
| `reducer.mod` | `rollouttrace.src.reducer.Mod` | 0 | `rollout-trace/src/reducer/mod.rs` | `rollouttrace/src/reducer/Mod.kt` |
| `rollout.lib` | `rollout.src.Lib` | 0 | `rollout/src/lib.rs` | `rollout/src/Lib.kt` |
| `sandboxing.lib` | `sandboxing.src.Lib` | 0 | `sandboxing/src/lib.rs` | `sandboxing/src/Lib.kt` |
| `secrets.lib` | `secrets.src.Lib` | 0 | `secrets/src/lib.rs` | `secrets/src/Lib.kt` |
| `command_safety.mod` | `shellcommand.src.commandsafety.Mod` | 0 | `shell-command/src/command_safety/mod.rs` | `shellcommand/src/commandsafety/Mod.kt` |
| `shell-command.lib` | `shellcommand.src.Lib` | 0 | `shell-command/src/lib.rs` | `shellcommand/src/Lib.kt` |
| `shell-escalation.lib` | `shellescalation.src.Lib` | 0 | `shell-escalation/src/lib.rs` | `shellescalation/src/Lib.kt` |
| `unix.mod` | `shellescalation.src.unix.Mod` | 0 | `shell-escalation/src/unix/mod.rs` | `shellescalation/src/unix/Mod.kt` |
| `skills.lib` | `skills.src.Lib` | 0 | `skills/src/lib.rs` | `skills/src/Lib.kt` |
| `state.lib` | `state.src.Lib` | 0 | `state/src/lib.rs` | `state/src/Lib.kt` |
| `model.mod` | `state.src.model.Mod` | 0 | `state/src/model/mod.rs` | `state/src/model/Mod.kt` |
| `stdio-to-uds.lib` | `stdiotouds.src.Lib` | 0 | `stdio-to-uds/src/lib.rs` | `stdiotouds/src/Lib.kt` |
| `test-binary-support.lib` | `testbinarysupport.Lib` | 0 | `test-binary-support/lib.rs` | `testbinarysupport/Lib.kt` |
| `thread-store.lib` | `threadstore.src.Lib` | 0 | `thread-store/src/lib.rs` | `threadstore/src/Lib.kt` |
| `local.mod` | `threadstore.src.local.Mod` | 0 | `thread-store/src/local/mod.rs` | `threadstore/src/local/Mod.kt` |
| `remote.mod` | `threadstore.src.remote.Mod` | 0 | `thread-store/src/remote/mod.rs` | `threadstore/src/remote/Mod.kt` |
| `tools.lib` | `tools.src.Lib` | 0 | `tools/src/lib.rs` | `tools/src/Lib.kt` |
| `bottom_pane.mod` | `tui.src.bottompane.Mod` | 0 | `tui/src/bottom_pane/mod.rs` | `tui/src/bottompane/Mod.kt` |
| `request_user_input.mod` | `tui.src.bottompane.requestuserinput.Mod` | 0 | `tui/src/bottom_pane/request_user_input/mod.rs` | `tui/src/bottompane/requestuserinput/Mod.kt` |
| `exec_cell.mod` | `tui.src.execcell.Mod` | 0 | `tui/src/exec_cell/mod.rs` | `tui/src/execcell/Mod.kt` |
| `tui.lib` | `tui.src.Lib` | 0 | `tui/src/lib.rs` | `tui/src/Lib.kt` |
| `notifications.mod` | `tui.src.notifications.Mod` | 0 | `tui/src/notifications/mod.rs` | `tui/src/notifications/Mod.kt` |
| `onboarding.mod` | `tui.src.onboarding.Mod` | 0 | `tui/src/onboarding/mod.rs` | `tui/src/onboarding/Mod.kt` |
| `public_widgets.mod` | `tui.src.publicwidgets.Mod` | 0 | `tui/src/public_widgets/mod.rs` | `tui/src/publicwidgets/Mod.kt` |
| `status.mod` | `tui.src.status.Mod` | 0 | `tui/src/status/mod.rs` | `tui/src/status/Mod.kt` |
| `streaming.mod` | `tui.src.streaming.Mod` | 0 | `tui/src/streaming/mod.rs` | `tui/src/streaming/Mod.kt` |
| `suite.mod` | `tui.tests.suite.Mod` | 0 | `tui/tests/suite/mod.rs` | `tui/tests/suite/Mod.kt` |
| `uds.lib` | `uds.src.Lib` | 0 | `uds/src/lib.rs` | `uds/src/Lib.kt` |
| `absolute-path.lib` | `utils.absolutepath.src.Lib` | 0 | `utils/absolute-path/src/lib.rs` | `utils/absolutepath/src/Lib.kt` |
| `approval-presets.lib` | `utils.approvalpresets.src.Lib` | 0 | `utils/approval-presets/src/lib.rs` | `utils/approvalpresets/src/Lib.kt` |
| `cache.lib` | `utils.cache.src.Lib` | 0 | `utils/cache/src/lib.rs` | `utils/cache/src/Lib.kt` |
| `cargo-bin.lib` | `utils.cargobin.src.Lib` | 0 | `utils/cargo-bin/src/lib.rs` | `utils/cargobin/src/Lib.kt` |
| `utils.cli.lib` | `utils.cli.src.Lib` | 0 | `utils/cli/src/lib.rs` | `utils/cli/src/Lib.kt` |
| `elapsed.lib` | `utils.elapsed.src.Lib` | 0 | `utils/elapsed/src/lib.rs` | `utils/elapsed/src/Lib.kt` |
| `fuzzy-match.lib` | `utils.fuzzymatch.src.Lib` | 0 | `utils/fuzzy-match/src/lib.rs` | `utils/fuzzymatch/src/Lib.kt` |
| `home-dir.lib` | `utils.homedir.src.Lib` | 0 | `utils/home-dir/src/lib.rs` | `utils/homedir/src/Lib.kt` |
| `image.lib` | `utils.image.src.Lib` | 0 | `utils/image/src/lib.rs` | `utils/image/src/Lib.kt` |
| `json-to-toml.lib` | `utils.jsontotoml.src.Lib` | 0 | `utils/json-to-toml/src/lib.rs` | `utils/jsontotoml/src/Lib.kt` |
| `oss.lib` | `utils.oss.src.Lib` | 0 | `utils/oss/src/lib.rs` | `utils/oss/src/Lib.kt` |
| `output-truncation.lib` | `utils.outputtruncation.src.Lib` | 0 | `utils/output-truncation/src/lib.rs` | `utils/outputtruncation/src/Lib.kt` |
| `path-utils.lib` | `utils.pathutils.src.Lib` | 0 | `utils/path-utils/src/lib.rs` | `utils/pathutils/src/Lib.kt` |
| `plugins.lib` | `utils.plugins.src.Lib` | 0 | `utils/plugins/src/lib.rs` | `utils/plugins/src/Lib.kt` |
| `pty.lib` | `utils.pty.src.Lib` | 0 | `utils/pty/src/lib.rs` | `utils/pty/src/Lib.kt` |
| `win.mod` | `utils.pty.src.win.Mod` | 0 | `utils/pty/src/win/mod.rs` | `utils/pty/src/win/Mod.kt` |
| `readiness.lib` | `utils.readiness.src.Lib` | 0 | `utils/readiness/src/lib.rs` | `utils/readiness/src/Lib.kt` |
| `rustls-provider.lib` | `utils.rustlsprovider.src.Lib` | 0 | `utils/rustls-provider/src/lib.rs` | `utils/rustlsprovider/src/Lib.kt` |
| `sandbox-summary.lib` | `utils.sandboxsummary.src.Lib` | 0 | `utils/sandbox-summary/src/lib.rs` | `utils/sandboxsummary/src/Lib.kt` |
| `sleep-inhibitor.lib` | `utils.sleepinhibitor.src.Lib` | 0 | `utils/sleep-inhibitor/src/lib.rs` | `utils/sleepinhibitor/src/Lib.kt` |
| `stream-parser.lib` | `utils.streamparser.src.Lib` | 0 | `utils/stream-parser/src/lib.rs` | `utils/streamparser/src/Lib.kt` |
| `string.lib` | `utils.string.src.Lib` | 0 | `utils/string/src/lib.rs` | `utils/string/src/Lib.kt` |
| `template.lib` | `utils.template.src.Lib` | 0 | `utils/template/src/lib.rs` | `utils/template/src/Lib.kt` |
| `v8-poc.lib` | `v8poc.src.Lib` | 0 | `v8-poc/src/lib.rs` | `v8poc/src/Lib.kt` |
| `conpty.mod` | `windowssandboxrs.src.conpty.Mod` | 0 | `windows-sandbox-rs/src/conpty/mod.rs` | `windowssandboxrs/src/conpty/Mod.kt` |
| `windows-sandbox-rs.lib` | `windowssandboxrs.src.Lib` | 0 | `windows-sandbox-rs/src/lib.rs` | `windowssandboxrs/src/Lib.kt` |
| `backends.mod` | `windowssandboxrs.src.unifiedexec.backends.Mod` | 0 | `windows-sandbox-rs/src/unified_exec/backends/mod.rs` | `windowssandboxrs/src/unifiedexec/backends/Mod.kt` |

