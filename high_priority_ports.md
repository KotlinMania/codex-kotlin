# High Priority Ports - Action Plan

## Files by Impact

Priority = deps * 1,000,000 + SymDeficit * 10,000 + SrcSymbols * 100 + (1 - function similarity) * 10

Dependency fanout is ranked first so the ladder favors ports that clear downstream compilation failures fastest.

This list is complete and includes function/type detail for every matched file. Function similarity is the required body/parameter comparison; file-level shape does not rescue a port.

| Rank | Source | Target | Function similarity | Deps | Functions | Missing functions | Types | Missing types | SymDeficit | SrcSymbols | Priority |
|------|--------|--------|------------|------|-----------|-------------------|-------|---------------|-----------|------------|----------|
| 1 | `protocol.user_input` | `protocol.UserInput [ZERO]` | 0.00 | 114 | 0/6 matched (target 0) | `new`, `map_range`, `set_placeholder`, `_placeholder_for_conversion_only`, `placeholder`, `from` | 1/3 matched (target 4) | `TextElement`, `ByteRange` | 8 | 9 | 114080904.0 |
| 2 | `tools.context` | `tools.Context` | 0.05 | 113 | 4/16 matched (target 10) | `post_tool_use_response`, `code_mode_result`, `to_response_item`, `response_payload`, `from_text`, `from_content`, `into_text`, `truncated_output`, `response_text`, `response_input_to_code_mode_result`, `content_items_to_code_mode_result`, `function_tool_response` | 4/12 matched (target 9) | `ToolCallSource`, `McpToolOutput`, `ToolSearchOutput`, `FunctionToolOutput`, `ApplyPatchToolOutput`, `AbortedToolOutput`, `ExecCommandToolOutput`, `UnifiedExecCodeModeResult` | 20 | 28 | 113202808.0 |
| 3 | `network-proxy.responses` | `endpoint.Responses [PROVENANCE-FALLBACK]` | 0.00 | 51 | 0/8 matched (target 4) | `text_response`, `json_response`, `blocked_header_value`, `blocked_message`, `blocked_text_response`, `blocked_message_with_policy`, `blocked_text_response_with_policy`, `blocked_message_with_policy_returns_human_message` | 0/1 matched (target 2) | `PolicyDecisionDetails` | 9 | 9 | 51090912.0 |
| 4 | `state.session` | `state.SessionState` | 0.26 | 48 | 10/33 matched (target 10) | `new`, `previous_turn_settings`, `set_previous_turn_settings`, `set_next_turn_is_first`, `take_next_turn_is_first`, `set_reference_context_item`, `reference_context_item`, `set_server_reasoning_included`, `server_reasoning_included`, `record_mcp_dependency_prompted`, `mcp_dependency_prompted`, `set_dependency_env`, `dependency_env`, `set_session_startup_prewarm`, `take_session_startup_prewarm`, `merge_connector_selection`, `get_connector_selection`, `clear_connector_selection`, `set_pending_session_start_source`, `take_pending_session_start_source`, `record_granted_permissions`, `granted_permissions`, `merge_rate_limit_fields` | 1/1 matched | _none_ | 23 | 34 | 48233408.0 |
| 5 | `render.renderable` | `render.Renderable` | 0.70 | 41 | 9/10 matched (target 48) | `cursor_style` | 7/8 matched (target 15) | `RenderableExt` | 2 | 18 | 41021804.0 |
| 6 | `tui.app_event` | `tui.AppEvent` | 0.69 | 36 | 2/2 matched | _none_ | 9/11 matched (target 70) | `ConnectorsSnapshot`, `RealtimeWebrtcOffer` | 2 | 13 | 36021304.0 |
| 7 | `tests.features` | `features.Features [PROVENANCE-FALLBACK]` | 0.00 | 33 | 0/5 matched (target 14) | `codex_command`, `features_enable_writes_feature_flag_to_config`, `features_disable_writes_feature_flag_to_config`, `features_enable_under_development_feature_prints_warning`, `features_list_is_sorted_alphabetically_by_feature_name` | 0/0 matched (target 5) | _none_ | 5 | 5 | 33050510.0 |
| 8 | `ollama.parser` | `ollama.Parser` | 0.22 | 32 | 1/3 matched (target 1) | `test_pull_events_decoder_status_and_success`, `test_pull_events_decoder_progress` | 0/0 matched | _none_ | 2 | 3 | 32020308.0 |
| 9 | `tui.key_hint` | `tui.KeyHint` | 0.29 | 27 | 10/29 matched (target 11) | `from_event`, `parts`, `display_label`, `normalize_key_parts`, `c0_control_char_to_ctrl_char`, `is_pressed`, `ctrl_alt`, `from`, `is_press_accepts_press_and_repeat_but_rejects_release`, `keybinding_list_ext_matches_any_binding`, `shifted_letter_binding_matches_uppercase_char_events`, `shift_letter_binding_preserves_other_modifiers_with_uppercase_compat`, `shift_letter_binding_does_not_match_plain_lowercase_or_other_uppercase`, `ctrl_letter_binding_matches_c0_control_char_events`, `ctrl_bindings_match_all_supported_c0_control_char_events`, `ctrl_binding_does_not_match_ambiguous_c0_escape_or_delete`, `history_search_ctrl_bindings_match_c0_control_char_events`, `ctrl_alt_sets_both_modifiers`, `has_ctrl_or_alt_checks_supported_modifier_combinations` | 1/2 matched (target 1) | `KeyBindingListExt` | 20 | 31 | 27203108.0 |
| 10 | `tui.app_event_sender` | `tui.AppEventSender` | 0.06 | 26 | 1/13 matched (target 1) | `new`, `interrupt`, `compact`, `set_thread_name`, `review`, `list_skills`, `realtime_conversation_audio`, `user_input_answer`, `exec_approval`, `request_permissions_response`, `patch_approval`, `resolve_elicitation` | 1/1 matched | _none_ | 12 | 14 | 26121410.0 |
| 11 | `ollama.url` | `ollama.Url` | 0.53 | 25 | 2/3 matched (target 2) | `test_base_url_to_host_root` | 0/0 matched | _none_ | 1 | 3 | 25010304.0 |
| 12 | `tui.style` | `tui.Style` | 0.41 | 21 | 3/6 matched (target 3) | `proposed_plan_style`, `proposed_plan_style_for`, `proposed_plan_bg` | 0/0 matched | _none_ | 3 | 6 | 21030606.0 |
| 13 | `tui.color` | `tui.Color` | 0.61 | 19 | 7/7 matched | _none_ | 0/0 matched | _none_ | 0 | 7 | 19000704.0 |
| 14 | `tool.terminal` | `core.Terminal [PROVENANCE-FALLBACK]` | 0.00 | 17 | 0/16 matched (target 6) | `start_terminal_operation_from_invocation`, `start_terminal_operation_from_runtime`, `insert_terminal_operation`, `end_terminal_operation`, `ensure_terminal_session`, `sync_terminal_model_observation`, `next_terminal_operation_id`, `terminal_operation_kind`, `parse_protocol_terminal_request`, `parse_dispatch_terminal_request`, `parse_terminal_response_payload`, `parse_protocol_terminal_response`, `parse_dispatch_terminal_response`, `parse_code_mode_exec_result`, `json_text_content`, `terminal_id_from_json` | 0/10 matched (target 0) | `TerminalOperationStart`, `ParsedTerminalRequest`, `ParsedTerminalResponse`, `ExecCommandBeginPayload`, `ExecCommandEndPayload`, `DispatchedToolTraceRequestPayload`, `DispatchedToolPayload`, `DispatchedWriteStdinArgs`, `DispatchedToolTraceResponsePayload`, `CodeModeExecResult` | 26 | 26 | 17262610.0 |
| 15 | `tui.frame_requester` | `tui.FrameRequester` | 0.18 | 17 | 5/13 matched (target 6) | `test_schedule_frame_immediate_triggers_once`, `test_schedule_frame_in_triggers_at_delay`, `test_coalesces_multiple_requests_into_single_draw`, `test_coalesces_mixed_immediate_and_delayed_requests`, `test_limits_draw_notifications_to_120fps`, `test_rate_limit_clamps_early_delayed_requests`, `test_rate_limit_does_not_delay_future_draws`, `test_multiple_delayed_requests_coalesce_to_earliest` | 2/2 matched (target 6) | _none_ | 8 | 15 | 17081508.0 |
| 16 | `execpolicy.decision` | `execpolicy.Decision` | 0.58 | 17 | 1/1 matched | _none_ | 1/1 matched | _none_ | 0 | 2 | 17000204.0 |
| 17 | `state.turn` | `session.Turn` | 0.22 | 15 | 8/26 matched (target 13) | `default`, `insert_pending_request_permissions`, `remove_pending_request_permissions`, `insert_pending_user_input`, `remove_pending_user_input`, `insert_pending_elicitation`, `remove_pending_elicitation`, `insert_pending_dynamic_tool`, `remove_pending_dynamic_tool`, `prepend_pending_input`, `has_pending_input`, `accept_mailbox_delivery_for_current_turn`, `accepts_mailbox_delivery_for_current_turn`, `set_mailbox_delivery_phase`, `record_granted_permissions`, `granted_permissions`, `enable_strict_auto_review`, `strict_auto_review_enabled` | 4/7 matched (target 10) | `MailboxDeliveryPhase`, `RemovedTask`, `PendingRequestPermissions` | 21 | 33 | 15213308.0 |
| 18 | `bottom_pane.scroll_state` | `bottompane.ScrollState` | 0.64 | 14 | 6/7 matched (target 6) | `wrap_navigation_and_visibility` | 1/1 matched | _none_ | 1 | 8 | 14010804.0 |
| 19 | `core.turn_diff_tracker` | `session.TurnDiffTracker` | 0.03 | 11 | 1/15 matched (target 10) | `new`, `get_path_for_internal`, `find_git_root_cached`, `relative_to_git_root_str`, `git_blob_oid_for_path`, `get_unified_diff`, `get_file_diff`, `git_blob_sha1_hex_bytes`, `as_str`, `fmt`, `file_mode_for_path`, `blob_bytes`, `symlink_blob_bytes`, `is_windows_drive_or_unc_root` | 3/3 matched (target 5) | _none_ | 14 | 18 | 11141810.0 |
| 20 | `core.shell` | `shell.ShellDetector` | 0.57 | 11 | 15/19 matched (target 24) | `shell_snapshot`, `empty_shell_snapshot_receiver`, `eq`, `test_detect_shell_type` | 2/2 matched (target 3) | _none_ | 4 | 21 | 11042104.0 |
| 21 | `tools.router` | `tools.Router` | 0.21 | 10 | 4/10 matched (target 6) | `model_visible_specs`, `find_spec`, `create_diff_consumer`, `configured_tool_supports_parallel`, `dispatch_tool_call_with_code_mode_result`, `filter_deferred_dynamic_tool_spec` | 2/3 matched (target 2) | `ToolRouterParams` | 7 | 13 | 10071308.0 |
| 22 | `codex-api.common` | `common.Common [PROVENANCE-FALLBACK]` | 0.15 | 6 | 1/4 matched (target 2) | `from`, `response_create_client_metadata`, `poll_next` | 8/16 matched (target 9) | `MemorySummarizeInput`, `RawMemory`, `RawMemoryMetadata`, `MemorySummarizeOutput`, `ResponseEvent`, `ResponseCreateWsRequest`, `ResponsesWsRequest`, `Item` | 11 | 20 | 6112008.5 |
| 23 | `tui.update_action` | `tui.UpdateAction` | 0.22 | 6 | 3/6 matched (target 5) | `from_install_context`, `maps_install_context_to_update_action`, `standalone_update_commands_rerun_latest_installer` | 1/1 matched | _none_ | 3 | 7 | 6030708.0 |
| 24 | `protocol.parse_command` | `protocol.ParseCommand [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 6 | 0/0 matched (target 1) | _none_ | 1/1 matched (target 5) | _none_ | 0 | 1 | 6000110.0 |
| 25 | `cli.exit_status` | `cli.ExitStatus` | 0.30 | 5 | 1/1 matched | _none_ | 0/0 matched (target 1) | _none_ | 0 | 1 | 5000107.0 |
| 26 | `tui.slash_command` | `tui.SlashCommand` | 0.15 | 4 | 4/11 matched (target 4) | `command`, `supports_inline_args`, `available_in_side_conversation`, `stop_command_is_canonical_name`, `clean_alias_parses_to_stop_command`, `certain_commands_are_available_during_task`, `auto_review_command_is_approve` | 1/1 matched | _none_ | 7 | 12 | 4071208.5 |
| 27 | `context.environment_context` | `utils.Environment [PROVENANCE-FALLBACK]` | 0.00 | 3 | 0/13 matched (target 2) | `legacy`, `from_turn_environments`, `from_vec`, `equals_except_shell`, `new`, `new_with_environments`, `diff_from_turn_context_item`, `from_turn_context`, `from_turn_context_item`, `with_subagents`, `network_from_turn_context`, `network_from_turn_context_item`, `body` | 0/4 matched (target 1) | `EnvironmentContext`, `EnvironmentContextEnvironment`, `EnvironmentContextEnvironments`, `NetworkContext` | 17 | 17 | 3171710.0 |
| 28 | `protocol.account` | `protocol.Account [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 3 | 0/8 matched (target 0) | `is_team_like`, `is_business_like`, `is_workspace_account`, `from`, `usage_based_plan_types_use_expected_wire_names`, `plan_family_helpers_group_usage_based_variants_with_existing_plans`, `workspace_account_helper_includes_usage_based_workspace_plans`, `auth_plan_type_converts_to_account_plan_type` | 1/2 matched (target 1) | `ProviderAccount` | 9 | 10 | 3091010.0 |
| 29 | `cli.format_env_display` | `common.FormatEnvDisplay` | 0.15 | 3 | 1/5 matched (target 1) | `returns_dash_when_empty`, `formats_sorted_env_pairs`, `formats_env_vars_with_dollar_prefix`, `combines_env_pairs_and_vars` | 0/0 matched | _none_ | 4 | 5 | 3040508.5 |
| 30 | `core.user_shell_command` | `session.UserShellCommand` | 0.33 | 3 | 1/3 matched (target 6) | `user_shell_command_fragment`, `format_user_shell_command_record` | 0/0 matched | _none_ | 2 | 3 | 3020306.8 |
| 31 | `context.user_instructions` | `session.UserInstructions [PROVENANCE-FALLBACK]` | 0.00 | 3 | 0/1 matched (target 4) | `body` | 1/1 matched (target 2) | _none_ | 1 | 2 | 3010210.0 |
| 32 | `protocol.auth` | `core.Auth [PROVENANCE-FALLBACK]` | 0.00 | 2 | 0/6 matched (target 37) | `from_raw_value`, `display_name`, `raw_value`, `is_workspace_account`, `new`, `plan_type_deserializes_raw_aliases` | 2/4 matched (target 17) | `RefreshTokenFailedError`, `RefreshTokenFailedReason` | 8 | 10 | 2081010.0 |
| 33 | `cli.sandbox_mode_cli_arg` | `common.SandboxModeCliArg` | 0.00 | 2 | 0/2 matched (target 1) | `from`, `maps_cli_args_to_protocol_modes` | 1/1 matched | _none_ | 2 | 3 | 2020310.0 |
| 34 | `cli.approval_mode_cli_arg` | `common.ApprovalModeCliArg` | 0.00 | 2 | 0/1 matched | `from` | 1/1 matched | _none_ | 1 | 2 | 2010210.0 |
| 35 | `command_safety.is_safe_command` | `commandsafety.IsSafeCommand [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 1 | 3/25 matched (target 3) | `is_safe_powershell_words`, `git_branch_is_read_only`, `git_has_unsafe_global_option`, `git_subcommand_args_are_read_only`, `vec_str`, `known_safe_examples`, `git_branch_mutating_flags_are_not_safe`, `git_branch_global_options_respect_safety_rules`, `git_first_positional_is_the_subcommand`, `git_output_flags_are_not_safe`, `git_global_override_flags_are_not_safe`, `cargo_check_is_not_safe`, `zsh_lc_safe_command_sequence`, `unknown_or_partial`, `base64_output_options_are_unsafe`, `ripgrep_rules`, `windows_powershell_full_path_is_safe`, `windows_git_full_path_is_safe`, `bash_lc_safe_examples`, `bash_lc_safe_examples_with_operators`, `bash_lc_unsafe_examples`, `direct_powershell_words_use_windows_safelist` | 0/0 matched | _none_ | 22 | 25 | 1222510.0 |
| 36 | `core.compact` | `endpoint.Compact [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 1 | 0/15 matched (target 4) | `should_use_remote_compact_task`, `run_inline_auto_compact_task`, `run_compact_task`, `run_compact_task_inner`, `run_compact_task_inner_impl`, `begin`, `track`, `compaction_status_from_result`, `content_items_to_text`, `collect_user_messages`, `is_summary_message`, `insert_initial_context_before_last_real_user_or_summary`, `build_compacted_history`, `build_compacted_history_with_limit`, `drain_to_completed` | 0/2 matched | `InitialContextInjection`, `CompactionAnalyticsAttempt` | 17 | 17 | 1171710.0 |
| 37 | `command_safety.is_dangerous_command` | `commandsafety.IsDangerousCommand [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 1 | 2/13 matched (target 3) | `is_dangerous_powershell_words`, `is_git_global_option_with_value`, `is_git_global_option_with_inline_value`, `git_global_option_requires_prompt`, `executable_name_lookup_key`, `find_git_subcommand`, `vec_str`, `rm_rf_is_dangerous`, `rm_f_is_dangerous`, `git_dash_c_requires_prompt`, `direct_powershell_words_reuse_windows_dangerous_detection` | 0/0 matched | _none_ | 11 | 13 | 1111310.0 |
| 38 | `execpolicy.rule` | `execpolicy.Rule` | 0.48 | 1 | 7/12 matched (target 9) | `with_resolved_program`, `parse`, `as_policy_string`, `normalize_network_rule_host`, `as_any` | 5/8 matched | `NetworkRuleProtocol`, `NetworkRule`, `RuleRef` | 8 | 20 | 1082005.2 |
| 39 | `string.truncate` | `context.TruncationPolicy [PROVENANCE-FALLBACK]` | 0.55 | 1 | 8/11 matched (target 13) | `truncate_middle_chars`, `truncate_middle_with_token_budget`, `removed_units` | 0/0 matched (target 3) | _none_ | 3 | 11 | 1031104.5 |
| 40 | `tui.frame_rate_limiter` | `tui.FrameRateLimiter` | 0.41 | 1 | 2/4 matched (target 2) | `default_does_not_clamp`, `clamps_to_min_interval_since_last_emit` | 1/1 matched | _none_ | 2 | 5 | 1020505.9 |
| 41 | `core.spawn` | `core.Spawn [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 1 | 0/1 matched (target 0) | `spawn_child_async` | 1/2 matched | `SpawnChildRequest` | 2 | 3 | 1020310.0 |
| 42 | `sse.responses` | `streaming.SseParser` | 0.00 | 0 | 0/55 matched (target 2) | `stream_from_fixture`, `spawn_response_stream`, `from`, `kind`, `response_model`, `model_verifications`, `header_openai_model_value_from_json`, `model_verifications_from_json_value`, `parse_model_verification`, `json_value_as_string`, `into_api_error`, `process_responses_event`, `process_sse`, `try_parse_retry_after`, `is_context_window_error`, `is_quota_exceeded_error`, `is_usage_not_included`, `is_invalid_prompt_error`, `is_cyber_policy_error`, `is_server_overloaded_error`, `cyber_policy_fallback_message`, `cyber_policy_message`, `rate_limit_regex`, `collect_events`, `run_sse`, `idle_timeout`, `parses_items_and_completed`, `error_when_missing_completed`, `parses_tool_search_call_items`, `parses_tool_call_input_deltas`, `emits_completed_without_stream_end`, `error_when_error_event`, `context_window_error_is_fatal`, `context_window_error_with_newline_is_fatal`, `quota_exceeded_error_is_fatal`, `cyber_policy_error_is_fatal`, `cyber_policy_error_uses_fallback_for_empty_message`, `invalid_prompt_without_type_is_invalid_request`, `table_driven_event_kinds`, `is_created`, `is_output`, `is_completed`, `spawn_response_stream_emits_header_events`, `spawn_response_stream_ignores_model_verification_header`, `process_sse_ignores_response_model_field_in_payload`, `process_sse_emits_server_model_from_response_headers_payload`, `process_sse_emits_model_verification_field`, `responses_stream_event_response_model_reads_top_level_headers`, `responses_stream_event_response_model_prefers_response_headers`, `responses_stream_event_model_verification_reads_metadata_field`, `responses_stream_event_model_verification_ignores_unknown_field`, `responses_stream_event_model_verification_ignores_non_array_field`, `test_try_parse_retry_after`, `test_try_parse_retry_after_no_delay`, `test_try_parse_retry_after_azure` | 0/8 matched (target 2) | `Error`, `ResponseCompleted`, `ResponseCompletedUsage`, `ResponseCompletedInputTokensDetails`, `ResponseCompletedOutputTokensDetails`, `ResponsesStreamEvent`, `ResponsesEventError`, `TestCase` | 63 | 63 | 636310.0 |
| 43 | `protocol.config_types` | `protocol.ConfigTypes [ZERO]` | 0.00 | 0 | 0/27 matched (target 0) | `schema_name`, `json_schema`, `default`, `string_enum_schema_with_description`, `merge`, `from`, `timeout`, `refresh_interval`, `default_provider_auth_timeout_ms`, `default_provider_auth_refresh_interval_ms`, `non_zero_u64`, `default_provider_auth_cwd`, `is_default_provider_auth_cwd`, `display_name`, `is_tui_visible`, `allows_request_user_input`, `settings_ref`, `model`, `reasoning_effort`, `with_updates`, `apply_mask`, `apply_mask_can_clear_optional_fields`, `mode_kind_deserializes_alias_values_to_default`, `approvals_reviewer_serializes_auto_review_and_accepts_legacy_guardian_subagent`, `tui_visible_collaboration_modes_match_mode_kind_visibility`, `web_search_location_merge_prefers_overlay_values`, `web_search_tool_config_merge_prefers_overlay_values` | 5/26 matched (target 6) | `ApprovalsReviewer`, `ShellEnvironmentPolicyInherit`, `EnvironmentVariablePattern`, `ShellEnvironmentPolicy`, `WindowsSandboxLevel`, `Personality`, `WebSearchMode`, `WebSearchContextSize`, `WebSearchLocation`, `WebSearchToolConfig`, `WebSearchFilters`, `WebSearchUserLocationType`, `WebSearchUserLocation`, `WebSearchConfig`, `ServiceTier`, `ModelProviderAuthInfo`, `AltScreenMode`, `ModeKind`, `CollaborationMode`, `Settings`, `CollaborationModeMask` | 48 | 53 | 485310.0 |
| 44 | `core-plugins.loader` | `config.ConfigLoader [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/40 matched (target 5) | `log_plugin_load_errors`, `into_mcp_servers`, `load_plugins_from_layer_stack`, `remote_installed_plugins_to_config`, `refresh_curated_plugin_cache`, `curated_plugin_cache_version`, `refresh_non_curated_plugin_cache`, `refresh_non_curated_plugin_cache_force_reinstall`, `refresh_non_curated_plugin_cache_with_mode`, `configured_plugins_from_stack`, `is_full_git_sha`, `configured_plugins_from_user_config_value`, `configured_plugins_from_codex_home`, `configured_plugin_ids`, `curated_plugin_ids_from_config_keys`, `non_curated_plugin_ids_from_config_keys`, `configured_curated_plugin_ids_from_codex_home`, `load_plugin`, `apply_plugin_mcp_server_policy`, `has_enabled_skills`, `load_plugin_skills`, `plugin_skill_roots`, `default_skill_roots`, `plugin_mcp_config_paths`, `default_mcp_config_paths`, `load_plugin_apps`, `plugin_app_config_paths`, `default_app_config_paths`, `load_plugin_hooks`, `append_plugin_hook_file`, `load_apps_from_paths`, `plugin_telemetry_metadata_from_root`, `load_plugin_mcp_servers`, `installed_plugin_telemetry_metadata`, `load_mcp_servers_from_file`, `normalize_plugin_mcp_servers`, `normalize_plugin_mcp_server_value`, `materialize_marketplace_plugin_source`, `clone_git_plugin_source`, `run_git` | 0/8 matched (target 2) | `NonCuratedCacheRefreshMode`, `PluginMcpServersFile`, `PluginMcpFile`, `PluginAppFile`, `PluginAppConfig`, `ResolvedPluginSkills`, `PluginMcpDiscovery`, `MaterializedMarketplacePluginSource` | 48 | 48 | 484810.0 |
| 45 | `command_safety.windows_dangerous_commands` | `commandsafety.WindowsDangerousCommands [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 10/56 matched (target 11) | `is_dangerous_powershell_words`, `split_embedded_cmd_operators`, `has_force_delete_cmdlet`, `has_force_flag_cmd`, `has_recursive_flag_cmd`, `has_quiet_flag_cmd`, `vec_str`, `powershell_start_process_url_is_dangerous`, `powershell_start_process_url_with_trailing_semicolon_is_dangerous`, `powershell_start_process_local_is_not_flagged`, `cmd_start_with_url_is_dangerous`, `msedge_with_url_is_dangerous`, `explorer_with_directory_is_not_flagged`, `powershell_remove_item_force_is_dangerous`, `powershell_remove_item_recurse_force_is_dangerous`, `powershell_ri_alias_force_is_dangerous`, `powershell_remove_item_without_force_is_not_flagged`, `cmd_del_force_is_dangerous`, `cmd_erase_force_is_dangerous`, `cmd_del_without_force_is_not_flagged`, `cmd_rd_recursive_is_dangerous`, `cmd_rd_without_quiet_is_not_flagged`, `cmd_rmdir_recursive_is_dangerous`, `powershell_remove_item_path_recurse_force_is_dangerous`, `powershell_remove_item_force_with_semicolon_is_dangerous`, `powershell_remove_item_force_inside_block_is_dangerous`, `powershell_remove_item_force_inside_brackets_is_dangerous`, `cmd_del_path_containing_f_is_not_flagged`, `cmd_rd_path_containing_s_is_not_flagged`, `cmd_bypass_chained_del_is_dangerous`, `powershell_chained_no_space_is_dangerous`, `powershell_comma_separated_is_dangerous`, `cmd_echo_del_is_not_dangerous`, `cmd_del_single_string_argument_is_dangerous`, `cmd_del_chained_single_string_argument_is_dangerous`, `cmd_chained_no_space_del_is_dangerous`, `cmd_chained_andand_no_space_del_is_dangerous`, `cmd_chained_oror_no_space_del_is_dangerous`, `cmd_start_url_single_string_is_dangerous`, `cmd_chained_no_space_rmdir_is_dangerous`, `cmd_del_force_uppercase_flag_is_dangerous`, `cmdexe_r_del_force_is_dangerous`, `cmd_start_quoted_url_single_string_is_dangerous`, `cmd_start_title_then_url_is_dangerous`, `powershell_rm_alias_force_is_dangerous`, `powershell_benign_force_separate_command_is_not_dangerous` | 1/1 matched | _none_ | 46 | 57 | 465710.0 |
| 46 | `exec-server.protocol` | `protocol.Protocol [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/5 matched (target 31) | `into_inner`, `from`, `serialize`, `deserialize`, `http_request_timeout_treats_omitted_and_null_as_no_timeout` | 1/37 matched (target 188) | `ByteChunk`, `InitializeParams`, `InitializeResponse`, `ExecParams`, `ExecEnvPolicy`, `ExecResponse`, `ReadParams`, `ProcessOutputChunk`, `ReadResponse`, `WriteParams`, `WriteStatus`, `WriteResponse`, `TerminateParams`, `TerminateResponse`, `FsReadFileParams`, `FsReadFileResponse`, `FsWriteFileParams`, `FsWriteFileResponse`, `FsCreateDirectoryParams`, `FsCreateDirectoryResponse`, `FsGetMetadataParams`, `FsGetMetadataResponse`, `FsReadDirectoryParams`, `FsReadDirectoryEntry`, `FsReadDirectoryResponse`, `FsRemoveParams`, `FsRemoveResponse`, `FsCopyParams`, `FsCopyResponse`, `HttpHeader`, `HttpRequestParams`, `HttpRequestResponse`, `HttpRequestBodyDeltaNotification`, `ExecOutputDeltaNotification`, `ExecExitedNotification`, `ExecClosedNotification` | 41 | 42 | 414210.0 |
| 47 | `shell-command.bash` | `bash.Bash` | 0.04 | 0 | 3/43 matched (target 8) | `try_parse_shell`, `try_parse_word_only_commands_sequence`, `parse_plain_command_from_node`, `parse_heredoc_command_words`, `is_literal_word_or_number`, `is_allowed_heredoc_attachment_kind`, `find_single_command_node`, `has_named_descendant_kind`, `parse_double_quoted_string`, `parse_raw_string`, `parse_seq`, `accepts_single_simple_command`, `accepts_multiple_commands_with_allowed_operators`, `extracts_double_and_single_quoted_strings`, `accepts_double_quoted_strings_with_newlines`, `accepts_mixed_quote_concatenation`, `rejects_double_quoted_strings_with_expansions`, `accepts_numbers_as_words`, `rejects_parentheses_and_subshells`, `rejects_redirections_and_unsupported_operators`, `rejects_command_and_process_substitutions_and_expansions`, `rejects_variable_assignment_prefix`, `rejects_trailing_operator_parse_error`, `rejects_empty_command_position_with_leading_operator`, `rejects_empty_command_position_with_double_separator`, `rejects_empty_command_position_with_empty_pipeline_segment`, `parse_zsh_lc_plain_commands`, `accepts_concatenated_flag_and_value`, `accepts_concatenated_flag_with_single_quotes`, `rejects_concatenation_with_variable_substitution`, `rejects_concatenation_with_command_substitution`, `parse_shell_lc_single_command_prefix_supports_heredoc`, `parse_shell_lc_single_command_prefix_rejects_multi_command_scripts`, `parse_shell_lc_single_command_prefix_rejects_non_heredoc_redirects`, `parse_shell_lc_single_command_prefix_rejects_heredoc_with_extra_file_redirect`, `parse_shell_lc_single_command_prefix_rejects_heredoc_with_variable_assignment`, `parse_shell_lc_single_command_prefix_rejects_herestring_with_chaining`, `parse_shell_lc_single_command_prefix_rejects_herestring_with_substitution`, `parse_shell_lc_single_command_prefix_rejects_arithmetic_shift_non_heredoc_script`, `parse_shell_lc_single_command_prefix_rejects_heredoc_command_with_word_expansion` | 0/0 matched | _none_ | 40 | 43 | 404309.6 |
| 48 | `core.exec` | `core.Exec` | 0.02 | 0 | 1/34 matched (target 3) | `windows_sandbox_uses_elevated_backend`, `select_process_exec_tool_sandbox_type`, `from`, `wait_with_outcome`, `timeout_ms`, `with_cancellation`, `cancel_when_either`, `retained_bytes_cap`, `io_drain_timeout`, `uses_expiration`, `process_exec_tool_call`, `build_exec_request`, `execute_exec_request`, `get_raw_output_result`, `extract_create_process_as_user_error_code`, `windowsapps_path_kind`, `record_windows_sandbox_spawn_failure`, `exec_windows_sandbox`, `finalize_exec_result`, `append_capped`, `aggregate_output`, `exec`, `should_use_windows_restricted_token_sandbox`, `unsupported_windows_restricted_token_sandbox_reason`, `resolve_windows_restricted_token_filesystem_overrides`, `normalize_windows_override_path`, `resolve_windows_elevated_filesystem_overrides`, `has_reopened_writable_descendant`, `consume_output`, `await_output`, `read_output`, `synthetic_exit_status`, `synthetic_exit_status_for_code` | 2/7 matched (target 5) | `WindowsSandboxFilesystemOverrides`, `ExecCapturePolicy`, `ExecExpiration`, `ExecExpirationOutcome`, `RawExecToolCallOutput` | 38 | 41 | 384109.8 |
| 49 | `core.exec_policy` | `execpolicy.ExecPolicy` | 0.00 | 0 | 0/29 matched (target 2) | `child_uses_parent_exec_policy`, `exec_policy_config_folders`, `is_policy_match`, `prompt_is_rejected_by_policy`, `new`, `load`, `current`, `create_exec_approval_requirement_for_command`, `append_amendment_and_update`, `append_network_rule_and_update`, `default`, `check_execpolicy_for_warnings`, `exec_policy_message_for_display`, `parse_starlark_line_from_message`, `format_exec_policy_error_with_source`, `load_exec_policy_with_warning`, `load_exec_policy`, `render_decision_for_unmatched_command`, `profile_is_managed_read_only`, `default_policy_path`, `commands_for_exec_policy`, `try_derive_execpolicy_amendment_for_prompt_rules`, `try_derive_execpolicy_amendment_for_allow_rules`, `derive_requested_execpolicy_amendment_from_prefix_rule`, `prefix_rule_would_approve_all_commands`, `derive_prompt_reason`, `render_shlex_command`, `derive_forbidden_reason`, `collect_policy_files` | 0/7 matched (target 0) | `ExecPolicyCommandOrigin`, `UnmatchedCommandContext`, `ExecPolicyCommands`, `ExecPolicyError`, `ExecPolicyUpdateError`, `ExecPolicyManager`, `ExecApprovalRequest` | 36 | 36 | 363610.0 |
| 50 | `model-provider.provider` | `provider.Provider [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/28 matched (target 4) | `default`, `fmt`, `capabilities`, `api_provider`, `runtime_base_url`, `api_auth`, `create_model_provider`, `new`, `info`, `auth_manager`, `auth`, `account_state`, `models_manager`, `provider_info_with_command_auth`, `test_codex_home`, `provider_for`, `remote_model`, `configured_provider_uses_default_capabilities`, `configured_provider_runtime_base_url_uses_configured_base_url`, `create_model_provider_builds_command_auth_manager_without_base_manager`, `create_model_provider_does_not_use_openai_auth_manager_for_amazon_bedrock_provider`, `openai_provider_returns_unauthenticated_openai_account_state`, `openai_provider_returns_api_key_account_state`, `custom_non_openai_provider_returns_no_account_state`, `amazon_bedrock_provider_returns_bedrock_account_state`, `amazon_bedrock_provider_creates_static_models_manager`, `amazon_bedrock_provider_uses_configured_static_catalog_when_present`, `configured_provider_models_manager_uses_provider_bearer_token` | 0/7 matched (target 3) | `ProviderCapabilities`, `ProviderAccountState`, `ProviderAccountError`, `ProviderAccountResult`, `ModelProvider`, `SharedModelProvider`, `ConfiguredModelProvider` | 35 | 35 | 353510.0 |
| 51 | `backend-client.types` | `config.Types [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/19 matched (target 7) | `text`, `text_values`, `diff_text`, `unified_diff`, `message_texts`, `user_prompt`, `error_summary`, `is_assistant`, `summary`, `assistant_text_messages`, `user_text_prompt`, `assistant_error_message`, `deserialize_vec`, `fixture`, `unified_diff_prefers_current_diff_task_turn`, `unified_diff_falls_back_to_pr_output_diff`, `assistant_text_messages_extracts_text_content`, `user_text_prompt_joins_parts_with_spacing`, `assistant_error_message_combines_code_and_message` | 0/13 matched (target 28) | `CodeTaskDetailsResponse`, `Turn`, `TurnItem`, `ContentFragment`, `StructuredContent`, `DiffPayload`, `Worklog`, `WorklogMessage`, `Author`, `WorklogContent`, `TurnError`, `CodeTaskDetailsResponseExt`, `TurnAttemptsSiblingTurnsResponse` | 32 | 32 | 323210.0 |
| 52 | `sandboxing.seatbelt` | `core.Seatbelt [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 2/24 matched (target 2) | `is_loopback_host`, `proxy_scheme_default_port`, `proxy_loopback_ports_from_env`, `default`, `proxy_policy_inputs`, `normalize_path_for_sandbox`, `unix_socket_path_params`, `unix_socket_path_param_key`, `unix_socket_dir_params`, `unix_socket_policy`, `dynamic_network_policy`, `dynamic_network_policy_for_network`, `root_absolute_path`, `build_seatbelt_access_policy`, `seatbelt_protected_metadata_name_regex`, `protected_metadata_names_for_writable_root`, `build_seatbelt_unreadable_glob_policy`, `canonicalize_glob_static_prefix_for_sandbox`, `seatbelt_regex_for_unreadable_glob`, `create_seatbelt_command_args_for_legacy_policy`, `confstr`, `confstr_path` | 0/5 matched (target 0) | `ProxyPolicyInputs`, `UnixDomainSocketPolicy`, `UnixSocketPathParam`, `SeatbeltAccessRoot`, `CreateSeatbeltCommandArgsParams` | 27 | 29 | 272910.0 |
| 53 | `protocol.approvals` | `protocol.Approvals [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/9 matched (target 1) | `new`, `command`, `from`, `effective_approval_id`, `effective_available_decisions`, `default_available_decisions`, `message`, `guardian_assessment_action_deserializes_command_shape`, `guardian_assessment_action_round_trips_execve_shape` | 4/20 matched (target 6) | `ResolvedPermissionProfile`, `EscalationPermissions`, `ExecPolicyAmendment`, `NetworkApprovalProtocol`, `NetworkApprovalContext`, `NetworkPolicyRuleAction`, `GuardianRiskLevel`, `GuardianUserAuthorization`, `GuardianAssessmentOutcome`, `GuardianAssessmentStatus`, `GuardianAssessmentDecisionSource`, `GuardianCommandSource`, `GuardianAssessmentAction`, `NetworkPolicyAmendment`, `GuardianAssessmentEvent`, `ElicitationRequest` | 25 | 29 | 252910.0 |
| 54 | `protocol.items` | `protocol.Items [PROVENANCE-FALLBACK]` | 0.26 | 0 | 6/19 matched (target 10) | `default`, `text_elements`, `local_image_paths`, `from_fragments`, `from_single_hook`, `build_hook_prompt_message`, `parse_hook_prompt_message`, `parse_hook_prompt_fragment`, `serialize_hook_prompt_fragment`, `as_legacy_begin_event`, `as_legacy_end_event`, `hook_prompt_roundtrips_multiple_fragments`, `hook_prompt_parses_legacy_single_hook_run_id` | 6/17 matched (target 11) | `HookPromptItem`, `HookPromptFragment`, `HookPromptXml`, `PlanItem`, `ImageViewItem`, `ImageGenerationItem`, `FileChangeItem`, `McpToolCallItem`, `McpToolCallStatus`, `McpToolCallError`, `ContextCompactionItem` | 24 | 36 | 243607.4 |
| 55 | `execpolicy.policy` | `execpolicy.Policy` | 0.09 | 0 | 5/24 matched (target 5) | `new`, `from_parts`, `network_rules`, `host_executables`, `get_allowed_prefixes`, `add_prefix_rule`, `add_network_rule`, `set_host_executable_paths`, `merge_overlay`, `compiled_network_domains`, `check_with_options`, `check_multiple_with_options`, `matches_for_command`, `matches_for_command_with_options`, `match_exact_rules`, `match_host_executable_rules`, `upsert_domain`, `render_pattern_token`, `from_matches` | 2/4 matched | `HeuristicsFallback`, `MatchOptions` | 21 | 28 | 212809.1 |
| 56 | `tools.registry` | `tools.Registry` | 0.25 | 0 | 9/23 matched (target 11) | `pre_tool_use_payload`, `post_tool_use_payload`, `create_diff_consumer`, `finish`, `into_response`, `code_mode_result`, `handle_any`, `empty_for_test`, `with_handler_for_test`, `has_handler`, `dispatch_any`, `from`, `hook_tool_kind`, `dispatch_after_tool_use_hook` | 4/10 matched (target 5) | `ToolArgumentDiffConsumer`, `AnyToolResult`, `PreToolUsePayload`, `PostToolUsePayload`, `AnyToolHandler`, `AfterToolUseHookDispatch` | 20 | 33 | 203307.5 |
| 57 | `codex-api.rate_limits` | `ratelimits.RateLimits [PROVENANCE-FALLBACK]` | 0.19 | 0 | 5/21 matched (target 6) | `fmt`, `parse_default_rate_limit`, `parse_all_rate_limits`, `parse_rate_limit_for_limit`, `parse_rate_limit_event`, `map_event_window`, `parse_promo_message`, `parse_header_str`, `has_rate_limit_data`, `header_name_to_limit_id`, `normalize_limit_id`, `parse_rate_limit_for_limit_defaults_to_codex_headers`, `parse_rate_limit_for_limit_reads_secondary_headers`, `parse_rate_limit_for_limit_prefers_limit_name_header`, `parse_all_rate_limits_reads_all_limit_families`, `parse_all_rate_limits_includes_default_codex_snapshot` | 1/5 matched (target 1) | `RateLimitEventWindow`, `RateLimitEventDetails`, `RateLimitEventCredits`, `RateLimitEvent` | 20 | 26 | 202608.1 |
| 58 | `handlers.apply_patch` | `handlers.ApplyPatch` | 0.11 | 0 | 3/20 matched (target 12) | `consume_diff`, `finish`, `push_delta`, `finish_update_on_complete`, `convert_apply_patch_hunks_to_protocol`, `hunk_source_path`, `format_update_chunks_for_progress`, `file_paths_for_action`, `to_abs_path`, `write_permissions_for_paths`, `apply_patch_payload_command`, `effective_patch_permissions`, `kind`, `create_diff_consumer`, `pre_tool_use_payload`, `post_tool_use_payload`, `intercept_apply_patch` | 1/3 matched (target 12) | `ApplyPatchArgumentDiffConsumer`, `Output` | 19 | 23 | 192308.9 |
| 59 | `lmstudio.client` | `client.ModelClient [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/16 matched (target 34) | `try_from_provider`, `check_server`, `load_model`, `fetch_models`, `find_lms`, `find_lms_with_home_dir`, `download_model`, `from_host_root`, `test_fetch_models_happy_path`, `test_fetch_models_no_data_array`, `test_fetch_models_server_error`, `test_check_server_happy_path`, `test_check_server_error`, `test_find_lms`, `test_find_lms_with_mock_home`, `test_from_host_root` | 0/1 matched (target 5) | `LMStudioClient` | 17 | 17 | 171710.0 |
| 60 | `mcp-server.codex_tool_config` | `config.DurationSerializers` | 0.00 | 0 | 0/9 matched (target 2) | `from`, `create_tool_for_codex_tool_call_param`, `codex_tool_output_schema`, `into_config`, `get_thread_id`, `create_tool_for_codex_tool_call_reply_param`, `create_tool_input_schema`, `verify_codex_tool_json_schema`, `verify_codex_tool_reply_json_schema` | 0/4 matched (target 0) | `CodexToolCallParam`, `CodexToolCallApprovalPolicy`, `CodexToolCallSandboxMode`, `CodexToolCallReplyParam` | 13 | 13 | 131310.0 |
| 61 | `command_safety.windows_safe_commands` | `commandsafety.WindowsSafeCommands [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 7/19 matched (target 10) | `join_arguments_as_script`, `quote_argument`, `is_safe_powershell_words`, `vec_str`, `recognizes_safe_powershell_wrappers`, `accepts_full_path_powershell_invocations`, `allows_read_only_pipelines_and_git_usage`, `rejects_git_global_override_options`, `rejects_powershell_commands_with_side_effects`, `accepts_constant_expression_arguments`, `rejects_dynamic_arguments`, `uses_invoked_powershell_variant_for_parsing` | 0/0 matched | _none_ | 12 | 19 | 121910.0 |
| 62 | `handlers.shell` | `handlers.Shell` | 0.28 | 0 | 5/14 matched (target 9) | `shell_payload_command`, `shell_command_payload_command`, `shell_runtime_backend`, `resolve_use_login_shell`, `base_command`, `from`, `kind`, `pre_tool_use_payload`, `post_tool_use_payload` | 2/5 matched (target 2) | `ShellCommandBackend`, `RunExecLikeArgs`, `Output` | 12 | 19 | 121907.2 |
| 63 | `core.client_common` | `prompt.Prompt` | 0.07 | 0 | 1/9 matched (target 2) | `default`, `reserialize_shell_outputs`, `is_shell_tool_name`, `parse_structured_shell_output`, `build_structured_output`, `strip_total_output_header`, `poll_next`, `drop` | 1/5 matched (target 1) | `ExecOutputJson`, `ExecOutputMetadataJson`, `ResponseStream`, `Item` | 12 | 14 | 121409.3 |
| 64 | `shell-command.powershell` | `core.PowerShell [PROVENANCE-FALLBACK]` | 0.06 | 0 | 1/13 matched (target 1) | `prefix_powershell_script_with_utf8`, `parse_powershell_command_into_plain_commands`, `try_find_powershell_executable_blocking`, `try_find_pwsh_executable_blocking`, `try_find_powershellish_executable_in_path`, `is_powershellish_executable_available`, `extracts_basic_powershell_command`, `extracts_lowercase_flags`, `extracts_full_path_powershell_command`, `extracts_with_noprofile_and_alias`, `parses_plain_powershell_commands`, `parses_multiple_plain_powershell_commands` | 0/0 matched | _none_ | 12 | 13 | 121309.4 |
| 65 | `login.device_code_auth` | `auth.Hashing` | 0.00 | 0 | 0/7 matched (target 18) | `deserialize_interval`, `request_user_code`, `poll_for_token`, `print_device_code_prompt`, `request_device_code`, `complete_device_code_login`, `run_device_code_login` | 0/5 matched (target 1) | `DeviceCode`, `UserCodeResp`, `UserCodeReq`, `TokenPollReq`, `CodeSuccessResp` | 12 | 12 | 121210.0 |
| 66 | `tools.sandboxing` | `tools.Sandboxing [ZERO]` | 0.00 | 0 | 8/16 matched (target 12) | `bash`, `proposed_execpolicy_amendment`, `default_exec_approval_requirement`, `sandbox_override_for_first_attempt`, `managed_network_for_sandbox_permissions`, `exec_approval_requirement`, `permission_request_payload`, `network_approval_spec` | 9/11 matched (target 19) | `PermissionRequestPayload`, `ExecApprovalRequirement` | 10 | 27 | 102710.0 |
| 67 | `handlers.unified_exec` | `handlers.UnifiedExec` | 0.22 | 0 | 4/12 matched (target 5) | `default_exec_yield_time_ms`, `default_write_stdin_yield_time_ms`, `default_tty`, `effective_max_output_tokens`, `kind`, `pre_tool_use_payload`, `post_tool_use_payload`, `emit_unified_exec_tty_metric` | 2/4 matched (target 2) | `WriteStdinArgs`, `Output` | 10 | 16 | 101607.8 |
| 68 | `core.message_history` | `protocol.MessageHistory [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/10 matched (target 0) | `history_filepath`, `append_entry`, `enforce_history_limit`, `trim_target_bytes`, `history_metadata`, `lookup`, `ensure_owner_only_permissions`, `history_metadata_for_file`, `lookup_history_entry`, `history_log_id` | 1/1 matched | _none_ | 10 | 11 | 101110.0 |
| 69 | `write.storage` | `auth.Storage [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/10 matched (target 30) | `rebuild_raw_memories_file_from_memories`, `sync_rollout_summaries_from_memories`, `rebuild_raw_memories_file`, `prune_rollout_summaries`, `write_rollout_summary_for_thread`, `retained_memories`, `raw_memories_format_error`, `rollout_summary_format_error`, `rollout_summary_file_stem`, `rollout_summary_file_stem_from_parts` | 0/0 matched (target 8) | _none_ | 10 | 10 | 101010.0 |
| 70 | `tui.terminal_palette` | `tui.TerminalPalette` | 0.18 | 0 | 7/14 matched (target 7) | `stdout_color_level`, `rgb_color`, `indexed_color`, `default`, `get_or_init_with`, `default_colors_cache`, `color_to_tuple` | 1/3 matched (target 1) | `StdoutColorLevel`, `Cache` | 9 | 17 | 91708.2 |
| 71 | `handlers.view_image` | `handlers.ViewImage` | 0.06 | 0 | 1/7 matched (target 1) | `kind`, `log_preview`, `success_for_logging`, `to_response_item`, `code_mode_result`, `code_mode_result_returns_image_url_object` | 2/5 matched (target 2) | `ViewImageDetail`, `Output`, `ViewImageOutput` | 9 | 12 | 91209.4 |
| 72 | `handlers.plan` | `handlers.Plan` | 0.05 | 0 | 1/8 matched (target 1) | `log_preview`, `success_for_logging`, `to_response_item`, `code_mode_result`, `kind`, `handle_update_plan`, `parse_update_plan_arguments` | 1/3 matched (target 1) | `PlanToolOutput`, `Output` | 9 | 11 | 91109.5 |
| 73 | `terminal-detection.lib` | `terminal.TerminalDetection [STUB]` | 0.00 | 0 | 20/27 matched (target 23) | `new`, `var_non_empty`, `var`, `user_agent`, `terminal_info`, `tmux_display_message`, `none_if_whitespace` | 5/6 matched (target 8) | `ProcessEnvironment` | 8 | 33 | 83310.0 |
| 74 | `runtimes.unified_exec` | `runtimes.UnifiedExec [ZERO]` | 0.00 | 0 | 5/12 matched (target 8) | `unified_exec_options`, `new`, `approval_keys`, `exec_approval_requirement`, `permission_request_payload`, `network_approval_spec`, `unified_exec_options_combines_default_timeout_with_network_denial_cancellation` | 3/4 matched (target 3) | `ApprovalKey` | 8 | 16 | 81610.0 |
| 75 | `runtimes.shell` | `runtimes.Shell` | 0.39 | 0 | 6/12 matched (target 10) | `new`, `for_shell_command`, `approval_keys`, `exec_approval_requirement`, `permission_request_payload`, `network_approval_spec` | 2/4 matched (target 3) | `ShellRuntimeBackend`, `ApprovalKey` | 8 | 16 | 81606.1 |
| 76 | `ollama.client` | `ollama.Client` | 0.38 | 0 | 6/14 matched (target 10) | `try_from_provider_with_base_url`, `fetch_version`, `from_host_root`, `test_fetch_models_happy_path`, `test_fetch_version`, `test_probe_server_happy_path_openai_compat_and_native`, `test_try_from_oss_provider_ok_when_server_running`, `test_try_from_oss_provider_err_when_server_missing` | 1/1 matched | _none_ | 8 | 15 | 81506.2 |
| 77 | `handlers.mcp` | `handlers.Mcp` | 0.07 | 0 | 1/8 matched (target 1) | `kind`, `pre_tool_use_payload`, `post_tool_use_payload`, `mcp_hook_tool_input`, `mcp_pre_tool_use_payload_uses_model_tool_name_and_raw_args`, `mcp_post_tool_use_payload_uses_model_tool_name_args_and_result`, `mcp_hook_tool_input_defaults_empty_args_to_object` | 1/2 matched (target 1) | `Output` | 8 | 10 | 81009.3 |
| 78 | `model-provider.auth` | `api.Auth [PROVENANCE-FALLBACK]` | 0.06 | 0 | 1/7 matched (target 2) | `unauthenticated_auth_provider`, `auth_manager_for_provider`, `resolve_provider_auth`, `bearer_auth_for_provider`, `auth_provider_from_auth`, `unauthenticated_auth_provider_adds_no_headers` | 0/2 matched (target 1) | `AgentIdentityAuthProvider`, `UnauthenticatedAuthProvider` | 8 | 9 | 80909.4 |
| 79 | `suite.exec` | `core.ExecExpiration [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/8 matched (target 3) | `skip_test`, `run_test_cmd`, `exit_code_0_succeeds`, `truncates_output_lines`, `truncates_output_bytes`, `exit_command_not_found_is_ok`, `openpty_works_under_real_exec_seatbelt_path`, `write_file_fails_as_sandbox_error` | 0/0 matched (target 6) | _none_ | 8 | 8 | 80810.0 |
| 80 | `handlers.list_dir` | `handlers.ListDir` | 0.34 | 0 | 5/11 matched (target 6) | `default_offset`, `default_limit`, `default_depth`, `kind`, `list_dir_slice_with_policy`, `from` | 4/5 matched (target 4) | `Output` | 7 | 16 | 71606.6 |
| 81 | `execpolicy.error` | `execpolicy.ExecPolicyError [ZERO]` | 0.00 | 0 | 0/2 matched (target 0) | `with_location`, `location` | 0/5 matched (target 7) | `Result`, `TextPosition`, `TextRange`, `ErrorLocation`, `Error` | 7 | 7 | 70710.0 |
| 82 | `runtimes.apply_patch` | `runtimes.ApplyPatch` | 0.34 | 0 | 5/11 matched | `new`, `build_guardian_review_request`, `file_system_sandbox_context_for_attempt`, `approval_keys`, `exec_approval_requirement`, `permission_request_payload` | 3/3 matched | _none_ | 6 | 14 | 61406.6 |
| 83 | `protocol.exec_output` | `protocol.ExecOutput` | 0.12 | 0 | 2/8 matched (target 3) | `new`, `bytes_to_string_smart`, `detect_encoding`, `decode_bytes`, `looks_like_windows_1252_punctuation`, `is_windows_1252_punct` | 2/2 matched | _none_ | 6 | 10 | 61008.8 |
| 84 | `core.util` | `core.Util [PROVENANCE-FALLBACK]` | 0.19 | 0 | 2/7 matched (target 3) | `from_optional_fields`, `emit_feedback_auth_recovery_tags`, `resolve_path`, `normalize_thread_name`, `resume_command` | 0/1 matched (target 0) | `Auth401FeedbackSnapshot` | 6 | 8 | 60808.1 |
| 85 | `exec.exec_events` | `exec.ExecEvents` | 1.00 | 0 | 0/0 matched | _none_ | 28/33 matched (target 43) | `CollabToolCallStatus`, `CollabTool`, `CollabAgentStatus`, `CollabAgentState`, `CollabToolCallItem` | 5 | 33 | 53300.0 |
| 86 | `handlers.mcp_resource` | `handlers.McpResource` | 0.42 | 0 | 13/17 matched (target 13) | `new`, `from_single_server`, `from_all_servers`, `kind` | 9/10 matched (target 9) | `Output` | 5 | 27 | 52705.8 |
| 87 | `tools.parallel` | `tools.Parallel` | 0.22 | 0 | 3/8 matched (target 5) | `new`, `find_spec`, `create_diff_consumer`, `handle_tool_call_with_source`, `failure_response` | 1/1 matched (target 2) | _none_ | 5 | 9 | 50907.8 |
| 88 | `protocol.num_format` | `protocol.NumFormat` | 0.10 | 0 | 3/8 matched (target 4) | `make_local_formatter`, `make_en_us_formatter`, `formatter`, `format_with_separators_with_formatter`, `format_si_suffix_with_formatter` | 0/0 matched (target 1) | _none_ | 5 | 8 | 50809.0 |
| 89 | `tools.orchestrator` | `tools.Orchestrator` | 0.26 | 0 | 2/6 matched (target 2) | `new`, `run_attempt`, `request_approval`, `reject_if_not_approved` | 1/2 matched | `OrchestratorRunResult` | 5 | 8 | 50807.4 |
| 90 | `sandbox-summary.sandbox_summary` | `common.SandboxSummary` | 0.13 | 0 | 1/6 matched (target 1) | `summarize_permission_profile`, `summarizes_external_sandbox_without_network_access_suffix`, `summarizes_external_sandbox_with_enabled_network`, `summarizes_read_only_with_enabled_network`, `workspace_write_summary_still_includes_network_access` | 0/0 matched | _none_ | 5 | 6 | 50608.7 |
| 91 | `handlers.test_sync` | `handlers.TestSync` | 0.26 | 0 | 2/5 matched (target 2) | `default_timeout_ms`, `barrier_map`, `kind` | 4/5 matched (target 4) | `Output` | 4 | 10 | 41007.4 |
| 92 | `tools.spec` | `tools.Spec` | 0.00 | 0 | 0/3 matched (target 17) | `tool_user_shell_type`, `map_mcp_tools_for_plan`, `build_specs_with_discoverable_tools` | 0/1 matched (target 11) | `McpToolPlanInputs` | 4 | 4 | 40410.0 |
| 93 | `aws-auth.config` | `otel.Config [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/3 matched (target 0) | `load_sdk_config`, `credentials_provider`, `resolved_region` | 0/0 matched (target 7) | _none_ | 3 | 3 | 30310.0 |
| 94 | `app-server.models` | `protocol.Models [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/3 matched (target 12) | `supported_models`, `model_from_preset`, `reasoning_efforts_from_preset` | 0/0 matched (target 56) | _none_ | 3 | 3 | 30310.0 |
| 95 | `mcp-server.tests.common.responses` | `requests.ResponsesRequest [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/3 matched (target 14) | `create_shell_command_sse_response`, `create_final_assistant_message_sse_response`, `create_apply_patch_sse_response` | 0/0 matched (target 2) | _none_ | 3 | 3 | 30310.0 |
| 96 | `unified_exec.errors` | `unifiedexec.Errors` | 0.31 | 0 | 1/3 matched (target 2) | `create_process`, `process_failed` | 1/1 matched (target 7) | _none_ | 2 | 4 | 20406.9 |
| 97 | `unified_exec.session` | `unifiedexec.Session [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/2 matched (target 15) | `spawn_windows_sandbox_session_legacy`, `spawn_windows_sandbox_session_elevated` | 0/0 matched (target 6) | _none_ | 2 | 2 | 20210.0 |
| 98 | `thread-store.error` | `core.ErrorTest [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/0 matched (target 21) | _none_ | 0/2 matched (target 1) | `ThreadStoreResult`, `ThreadStoreError` | 2 | 2 | 20210.0 |
| 99 | `codex-api.error` | `core.Error [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/1 matched (target 62) | `from` | 0/1 matched (target 42) | `ApiError` | 2 | 2 | 20210.0 |
| 100 | `execpolicy-legacy.error` | `error.ApiError [PROVENANCE-FALLBACK]` | 1.00 | 0 | 0/0 matched | _none_ | 0/2 matched (target 9) | `Result`, `Error` | 2 | 2 | 20200.0 |
| 101 | `agent-graph-store.error` | `error.TransportError [PROVENANCE-FALLBACK]` | 1.00 | 0 | 0/0 matched | _none_ | 0/2 matched (target 8) | `AgentGraphStoreResult`, `AgentGraphStoreError` | 2 | 2 | 20200.0 |
| 102 | `tools.events` | `tools.Events` | 0.74 | 0 | 11/12 matched (target 11) | `new` | 6/6 matched (target 14) | _none_ | 1 | 18 | 11802.6 |
| 103 | `render.mod` | `render.Render [STUB]` | 0.00 | 0 | 3/3 matched (target 5) | _none_ | 1/2 matched (target 1) | `RectExt` | 1 | 5 | 10510.0 |
| 104 | `core.review_format` | `core.ReviewFormat` | 0.57 | 0 | 2/3 matched (target 2) | `render_review_output_text` | 0/0 matched | _none_ | 1 | 3 | 10304.3 |
| 105 | `core.landlock` | `core.Landlock` | 0.00 | 0 | 0/1 matched | `spawn_command_under_linux_sandbox` | 0/0 matched | _none_ | 1 | 1 | 10110.0 |
| 106 | `tools.plan_tool` | `protocol.PlanTool [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/1 matched (target 0) | `create_update_plan_tool` | 0/0 matched (target 3) | _none_ | 1 | 1 | 10110.0 |
| 107 | `suite.user_notification` | `core.UserNotification` | 0.00 | 0 | 0/1 matched (target 4) | `summarize_context_three_requests_and_instructions` | 0/0 matched (target 4) | _none_ | 1 | 1 | 10110.0 |
| 108 | `ollama.pull` | `ollama.Pull` | 0.73 | 0 | 3/3 matched (target 4) | _none_ | 4/4 matched (target 9) | _none_ | 0 | 7 | 702.7 |
| 109 | `requests.headers` | `requests.Headers [STUB]` | 0.00 | 0 | 3/3 matched | _none_ | 0/0 matched | _none_ | 0 | 3 | 310.0 |
| 110 | `login.pkce` | `login.Pkce` | 0.75 | 0 | 1/1 matched (target 2) | _none_ | 1/1 matched | _none_ | 0 | 2 | 202.5 |
| 111 | `codex-client.telemetry` | `telemetry.Telemetry [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/0 matched (target 1) | _none_ | 1/1 matched (target 2) | _none_ | 0 | 1 | 110.0 |
| 112 | `core.function_tool` | `core.FunctionTool [ZERO]` | 0.00 | 0 | 0/0 matched (target 4) | _none_ | 1/1 matched (target 5) | _none_ | 0 | 1 | 110.0 |
| 113 | `models-manager.model_presets` | `common.ModelPresets [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0 | 0/0 matched (target 2) | _none_ | 0/0 matched (target 3) | _none_ | 0 | 0 | 10.0 |
| 114 | `core.flags` | `core.Flags` | 1.00 | 0 | 0/0 matched | _none_ | 0/0 matched | _none_ | 0 | 0 | 0.0 |
| 115 | `tui.ui_consts` | `tui.UiConsts` | 1.00 | 0 | 0/0 matched | _none_ | 0/0 matched | _none_ | 0 | 0 | 0.0 |

## Cheat Detection / Scoring Failures

- `protocol.user_input` -> `protocol.UserInput [ZERO]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `protocol.parse_command` -> `protocol.ParseCommand [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only
- `protocol.account` -> `protocol.Account [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `command_safety.is_safe_command` -> `commandsafety.IsSafeCommand [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. IsSafeCommand.kt: Rust lifetime explanation in Kotlin comments
- `core.compact` -> `endpoint.Compact [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `command_safety.is_dangerous_command` -> `commandsafety.IsDangerousCommand [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. IsDangerousCommand.kt: Rust `let` binding in Kotlin comments
- `core.spawn` -> `core.Spawn [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `protocol.config_types` -> `protocol.ConfigTypes [ZERO]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `core-plugins.loader` -> `config.ConfigLoader [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `command_safety.windows_dangerous_commands` -> `commandsafety.WindowsDangerousCommands [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. WindowsDangerousCommands.kt: Rust lifetime explanation in Kotlin comments
- `backend-client.types` -> `config.Types [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. Types.kt: snake_case identifier `hideGpt5_1MigrationPrompt` in Kotlin code
- `sandboxing.seatbelt` -> `core.Seatbelt [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `lmstudio.client` -> `client.ModelClient [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `command_safety.windows_safe_commands` -> `commandsafety.WindowsSafeCommands [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. WindowsSafeCommands.kt: Rust lifetime explanation in Kotlin comments; WindowsSafeCommands.kt: Rust-only type/unsafe terminology in Kotlin comments
- `tools.sandboxing` -> `tools.Sandboxing [ZERO]`: function-by-function score forced to 0. Sandboxing.kt: score-padding suppression annotation `@Suppress` in Kotlin code
- `core.message_history` -> `protocol.MessageHistory [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `write.storage` -> `auth.Storage [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `terminal-detection.lib` -> `terminal.TerminalDetection [STUB]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `runtimes.unified_exec` -> `runtimes.UnifiedExec [ZERO]`: function-by-function score forced to 0. UnifiedExec.kt: score-padding suppression annotation `@Suppress` in Kotlin code
- `execpolicy.error` -> `execpolicy.ExecPolicyError [ZERO]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `aws-auth.config` -> `otel.Config [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `mcp-server.tests.common.responses` -> `requests.ResponsesRequest [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies; ResponsesRequest.kt: Rust lifetime explanation in Kotlin comments
- `thread-store.error` -> `core.ErrorTest [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only
- `render.mod` -> `render.Render [STUB]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `tools.plan_tool` -> `protocol.PlanTool [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `requests.headers` -> `requests.Headers [STUB]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `codex-client.telemetry` -> `telemetry.Telemetry [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only
- `core.function_tool` -> `core.FunctionTool [ZERO]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only
- `models-manager.model_presets` -> `common.ModelPresets [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only

## Critical Issues (Function Similarity < 0.60 with Dependencies)

These files need immediate attention:

- **protocol.user_input** → `protocol.UserInput [ZERO]`
  - Function similarity: 0.00
  - Dependencies: 114
  - Functions: 0/6 matched (target 0)
  - Missing functions: `new`, `map_range`, `set_placeholder`, `_placeholder_for_conversion_only`, `placeholder`, `from`
  - Types: 1/3 matched (target 4)
  - Missing types: `TextElement`, `ByteRange`
  - Scoring failure: no target functions found; report scoring is function-by-function only

- **tools.context** → `tools.Context`
  - Function similarity: 0.05
  - Dependencies: 113
  - Functions: 4/16 matched (target 10)
  - Missing functions: `post_tool_use_response`, `code_mode_result`, `to_response_item`, `response_payload`, `from_text`, `from_content`, `into_text`, `truncated_output`, `response_text`, `response_input_to_code_mode_result`, `content_items_to_code_mode_result`, `function_tool_response`
  - Types: 4/12 matched (target 9)
  - Missing types: `ToolCallSource`, `McpToolOutput`, `ToolSearchOutput`, `FunctionToolOutput`, `ApplyPatchToolOutput`, `AbortedToolOutput`, `ExecCommandToolOutput`, `UnifiedExecCodeModeResult`

- **network-proxy.responses** → `endpoint.Responses [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 51
  - Functions: 0/8 matched (target 4)
  - Missing functions: `text_response`, `json_response`, `blocked_header_value`, `blocked_message`, `blocked_text_response`, `blocked_message_with_policy`, `blocked_text_response_with_policy`, `blocked_message_with_policy_returns_human_message`
  - Types: 0/1 matched (target 2)
  - Missing types: `PolicyDecisionDetails`
  - Lint issues: 1

- **state.session** → `state.SessionState`
  - Function similarity: 0.26
  - Dependencies: 48
  - Functions: 10/33 matched (target 10)
  - Missing functions: `new`, `previous_turn_settings`, `set_previous_turn_settings`, `set_next_turn_is_first`, `take_next_turn_is_first`, `set_reference_context_item`, `reference_context_item`, `set_server_reasoning_included`, `server_reasoning_included`, `record_mcp_dependency_prompted`, `mcp_dependency_prompted`, `set_dependency_env`, `dependency_env`, `set_session_startup_prewarm`, `take_session_startup_prewarm`, `merge_connector_selection`, `get_connector_selection`, `clear_connector_selection`, `set_pending_session_start_source`, `take_pending_session_start_source`, `record_granted_permissions`, `granted_permissions`, `merge_rate_limit_fields`
  - Types: 1/1 matched
  - Missing types: _none_

- **tests.features** → `features.Features [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 33
  - Functions: 0/5 matched (target 14)
  - Missing functions: `codex_command`, `features_enable_writes_feature_flag_to_config`, `features_disable_writes_feature_flag_to_config`, `features_enable_under_development_feature_prints_warning`, `features_list_is_sorted_alphabetically_by_feature_name`
  - Types: 0/0 matched (target 5)
  - Missing types: _none_
  - Lint issues: 1

- **ollama.parser** → `ollama.Parser`
  - Function similarity: 0.22
  - Dependencies: 32
  - Functions: 1/3 matched (target 1)
  - Missing functions: `test_pull_events_decoder_status_and_success`, `test_pull_events_decoder_progress`
  - Types: 0/0 matched
  - Missing types: _none_

- **tui.key_hint** → `tui.KeyHint`
  - Function similarity: 0.29
  - Dependencies: 27
  - Functions: 10/29 matched (target 11)
  - Missing functions: `from_event`, `parts`, `display_label`, `normalize_key_parts`, `c0_control_char_to_ctrl_char`, `is_pressed`, `ctrl_alt`, `from`, `is_press_accepts_press_and_repeat_but_rejects_release`, `keybinding_list_ext_matches_any_binding`, `shifted_letter_binding_matches_uppercase_char_events`, `shift_letter_binding_preserves_other_modifiers_with_uppercase_compat`, `shift_letter_binding_does_not_match_plain_lowercase_or_other_uppercase`, `ctrl_letter_binding_matches_c0_control_char_events`, `ctrl_bindings_match_all_supported_c0_control_char_events`, `ctrl_binding_does_not_match_ambiguous_c0_escape_or_delete`, `history_search_ctrl_bindings_match_c0_control_char_events`, `ctrl_alt_sets_both_modifiers`, `has_ctrl_or_alt_checks_supported_modifier_combinations`
  - Types: 1/2 matched (target 1)
  - Missing types: `KeyBindingListExt`

- **tui.app_event_sender** → `tui.AppEventSender`
  - Function similarity: 0.06
  - Dependencies: 26
  - Functions: 1/13 matched (target 1)
  - Missing functions: `new`, `interrupt`, `compact`, `set_thread_name`, `review`, `list_skills`, `realtime_conversation_audio`, `user_input_answer`, `exec_approval`, `request_permissions_response`, `patch_approval`, `resolve_elicitation`
  - Types: 1/1 matched
  - Missing types: _none_

- **ollama.url** → `ollama.Url`
  - Function similarity: 0.53
  - Dependencies: 25
  - Functions: 2/3 matched (target 2)
  - Missing functions: `test_base_url_to_host_root`
  - Types: 0/0 matched
  - Missing types: _none_

- **tui.style** → `tui.Style`
  - Function similarity: 0.41
  - Dependencies: 21
  - Functions: 3/6 matched (target 3)
  - Missing functions: `proposed_plan_style`, `proposed_plan_style_for`, `proposed_plan_bg`
  - Types: 0/0 matched
  - Missing types: _none_

- **tool.terminal** → `core.Terminal [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 17
  - Functions: 0/16 matched (target 6)
  - Missing functions: `start_terminal_operation_from_invocation`, `start_terminal_operation_from_runtime`, `insert_terminal_operation`, `end_terminal_operation`, `ensure_terminal_session`, `sync_terminal_model_observation`, `next_terminal_operation_id`, `terminal_operation_kind`, `parse_protocol_terminal_request`, `parse_dispatch_terminal_request`, `parse_terminal_response_payload`, `parse_protocol_terminal_response`, `parse_dispatch_terminal_response`, `parse_code_mode_exec_result`, `json_text_content`, `terminal_id_from_json`
  - Types: 0/10 matched (target 0)
  - Missing types: `TerminalOperationStart`, `ParsedTerminalRequest`, `ParsedTerminalResponse`, `ExecCommandBeginPayload`, `ExecCommandEndPayload`, `DispatchedToolTraceRequestPayload`, `DispatchedToolPayload`, `DispatchedWriteStdinArgs`, `DispatchedToolTraceResponsePayload`, `CodeModeExecResult`
  - Lint issues: 1

- **tui.frame_requester** → `tui.FrameRequester`
  - Function similarity: 0.18
  - Dependencies: 17
  - Functions: 5/13 matched (target 6)
  - Missing functions: `test_schedule_frame_immediate_triggers_once`, `test_schedule_frame_in_triggers_at_delay`, `test_coalesces_multiple_requests_into_single_draw`, `test_coalesces_mixed_immediate_and_delayed_requests`, `test_limits_draw_notifications_to_120fps`, `test_rate_limit_clamps_early_delayed_requests`, `test_rate_limit_does_not_delay_future_draws`, `test_multiple_delayed_requests_coalesce_to_earliest`
  - Types: 2/2 matched (target 6)
  - Missing types: _none_

- **execpolicy.decision** → `execpolicy.Decision`
  - Function similarity: 0.58
  - Dependencies: 17
  - Functions: 1/1 matched
  - Missing functions: _none_
  - Types: 1/1 matched
  - Missing types: _none_

- **state.turn** → `session.Turn`
  - Function similarity: 0.22
  - Dependencies: 15
  - Functions: 8/26 matched (target 13)
  - Missing functions: `default`, `insert_pending_request_permissions`, `remove_pending_request_permissions`, `insert_pending_user_input`, `remove_pending_user_input`, `insert_pending_elicitation`, `remove_pending_elicitation`, `insert_pending_dynamic_tool`, `remove_pending_dynamic_tool`, `prepend_pending_input`, `has_pending_input`, `accept_mailbox_delivery_for_current_turn`, `accepts_mailbox_delivery_for_current_turn`, `set_mailbox_delivery_phase`, `record_granted_permissions`, `granted_permissions`, `enable_strict_auto_review`, `strict_auto_review_enabled`
  - Types: 4/7 matched (target 10)
  - Missing types: `MailboxDeliveryPhase`, `RemovedTask`, `PendingRequestPermissions`
  - Lint issues: 2

- **core.turn_diff_tracker** → `session.TurnDiffTracker`
  - Function similarity: 0.03
  - Dependencies: 11
  - Functions: 1/15 matched (target 10)
  - Missing functions: `new`, `get_path_for_internal`, `find_git_root_cached`, `relative_to_git_root_str`, `git_blob_oid_for_path`, `get_unified_diff`, `get_file_diff`, `git_blob_sha1_hex_bytes`, `as_str`, `fmt`, `file_mode_for_path`, `blob_bytes`, `symlink_blob_bytes`, `is_windows_drive_or_unc_root`
  - Types: 3/3 matched (target 5)
  - Missing types: _none_
  - Lint issues: 1

- **core.shell** → `shell.ShellDetector`
  - Function similarity: 0.57
  - Dependencies: 11
  - Functions: 15/19 matched (target 24)
  - Missing functions: `shell_snapshot`, `empty_shell_snapshot_receiver`, `eq`, `test_detect_shell_type`
  - Types: 2/2 matched (target 3)
  - Missing types: _none_

- **tools.router** → `tools.Router`
  - Function similarity: 0.21
  - Dependencies: 10
  - Functions: 4/10 matched (target 6)
  - Missing functions: `model_visible_specs`, `find_spec`, `create_diff_consumer`, `configured_tool_supports_parallel`, `dispatch_tool_call_with_code_mode_result`, `filter_deferred_dynamic_tool_spec`
  - Types: 2/3 matched (target 2)
  - Missing types: `ToolRouterParams`

- **codex-api.common** → `common.Common [PROVENANCE-FALLBACK]`
  - Function similarity: 0.15
  - Dependencies: 6
  - Functions: 1/4 matched (target 2)
  - Missing functions: `from`, `response_create_client_metadata`, `poll_next`
  - Types: 8/16 matched (target 9)
  - Missing types: `MemorySummarizeInput`, `RawMemory`, `RawMemoryMetadata`, `MemorySummarizeOutput`, `ResponseEvent`, `ResponseCreateWsRequest`, `ResponsesWsRequest`, `Item`
  - Lint issues: 1

- **tui.update_action** → `tui.UpdateAction`
  - Function similarity: 0.22
  - Dependencies: 6
  - Functions: 3/6 matched (target 5)
  - Missing functions: `from_install_context`, `maps_install_context_to_update_action`, `standalone_update_commands_rerun_latest_installer`
  - Types: 1/1 matched
  - Missing types: _none_

- **protocol.parse_command** → `protocol.ParseCommand [ZERO] [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 6
  - Functions: 0/0 matched (target 1)
  - Missing functions: _none_
  - Types: 1/1 matched (target 5)
  - Missing types: _none_
  - Scoring failure: no source functions found; target defines functions; report scoring is function-by-function only
  - Lint issues: 1

- **cli.exit_status** → `cli.ExitStatus`
  - Function similarity: 0.30
  - Dependencies: 5
  - Functions: 1/1 matched
  - Missing functions: _none_
  - Types: 0/0 matched (target 1)
  - Missing types: _none_

- **tui.slash_command** → `tui.SlashCommand`
  - Function similarity: 0.15
  - Dependencies: 4
  - Functions: 4/11 matched (target 4)
  - Missing functions: `command`, `supports_inline_args`, `available_in_side_conversation`, `stop_command_is_canonical_name`, `clean_alias_parses_to_stop_command`, `certain_commands_are_available_during_task`, `auto_review_command_is_approve`
  - Types: 1/1 matched
  - Missing types: _none_

- **context.environment_context** → `utils.Environment [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 3
  - Functions: 0/13 matched (target 2)
  - Missing functions: `legacy`, `from_turn_environments`, `from_vec`, `equals_except_shell`, `new`, `new_with_environments`, `diff_from_turn_context_item`, `from_turn_context`, `from_turn_context_item`, `with_subagents`, `network_from_turn_context`, `network_from_turn_context_item`, `body`
  - Types: 0/4 matched (target 1)
  - Missing types: `EnvironmentContext`, `EnvironmentContextEnvironment`, `EnvironmentContextEnvironments`, `NetworkContext`
  - Lint issues: 1

- **protocol.account** → `protocol.Account [ZERO] [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 3
  - Functions: 0/8 matched (target 0)
  - Missing functions: `is_team_like`, `is_business_like`, `is_workspace_account`, `from`, `usage_based_plan_types_use_expected_wire_names`, `plan_family_helpers_group_usage_based_variants_with_existing_plans`, `workspace_account_helper_includes_usage_based_workspace_plans`, `auth_plan_type_converts_to_account_plan_type`
  - Types: 1/2 matched (target 1)
  - Missing types: `ProviderAccount`
  - Scoring failure: no target functions found; report scoring is function-by-function only
  - Lint issues: 1

- **cli.format_env_display** → `common.FormatEnvDisplay`
  - Function similarity: 0.15
  - Dependencies: 3
  - Functions: 1/5 matched (target 1)
  - Missing functions: `returns_dash_when_empty`, `formats_sorted_env_pairs`, `formats_env_vars_with_dollar_prefix`, `combines_env_pairs_and_vars`
  - Types: 0/0 matched
  - Missing types: _none_

- **core.user_shell_command** → `session.UserShellCommand`
  - Function similarity: 0.33
  - Dependencies: 3
  - Functions: 1/3 matched (target 6)
  - Missing functions: `user_shell_command_fragment`, `format_user_shell_command_record`
  - Types: 0/0 matched
  - Missing types: _none_

- **context.user_instructions** → `session.UserInstructions [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 3
  - Functions: 0/1 matched (target 4)
  - Missing functions: `body`
  - Types: 1/1 matched (target 2)
  - Missing types: _none_
  - Lint issues: 1

- **protocol.auth** → `core.Auth [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 2
  - Functions: 0/6 matched (target 37)
  - Missing functions: `from_raw_value`, `display_name`, `raw_value`, `is_workspace_account`, `new`, `plan_type_deserializes_raw_aliases`
  - Types: 2/4 matched (target 17)
  - Missing types: `RefreshTokenFailedError`, `RefreshTokenFailedReason`
  - Lint issues: 1

- **cli.sandbox_mode_cli_arg** → `common.SandboxModeCliArg`
  - Function similarity: 0.00
  - Dependencies: 2
  - Functions: 0/2 matched (target 1)
  - Missing functions: `from`, `maps_cli_args_to_protocol_modes`
  - Types: 1/1 matched
  - Missing types: _none_

- **cli.approval_mode_cli_arg** → `common.ApprovalModeCliArg`
  - Function similarity: 0.00
  - Dependencies: 2
  - Functions: 0/1 matched
  - Missing functions: `from`
  - Types: 1/1 matched
  - Missing types: _none_

- **command_safety.is_safe_command** → `commandsafety.IsSafeCommand [ZERO] [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 1
  - Functions: 3/25 matched (target 3)
  - Missing functions: `is_safe_powershell_words`, `git_branch_is_read_only`, `git_has_unsafe_global_option`, `git_subcommand_args_are_read_only`, `vec_str`, `known_safe_examples`, `git_branch_mutating_flags_are_not_safe`, `git_branch_global_options_respect_safety_rules`, `git_first_positional_is_the_subcommand`, `git_output_flags_are_not_safe`, `git_global_override_flags_are_not_safe`, `cargo_check_is_not_safe`, `zsh_lc_safe_command_sequence`, `unknown_or_partial`, `base64_output_options_are_unsafe`, `ripgrep_rules`, `windows_powershell_full_path_is_safe`, `windows_git_full_path_is_safe`, `bash_lc_safe_examples`, `bash_lc_safe_examples_with_operators`, `bash_lc_unsafe_examples`, `direct_powershell_words_use_windows_safelist`
  - Types: 0/0 matched
  - Missing types: _none_
  - Scoring failure: IsSafeCommand.kt: Rust lifetime explanation in Kotlin comments
  - Lint issues: 1

- **core.compact** → `endpoint.Compact [STUB] [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 1
  - Functions: 0/15 matched (target 4)
  - Missing functions: `should_use_remote_compact_task`, `run_inline_auto_compact_task`, `run_compact_task`, `run_compact_task_inner`, `run_compact_task_inner_impl`, `begin`, `track`, `compaction_status_from_result`, `content_items_to_text`, `collect_user_messages`, `is_summary_message`, `insert_initial_context_before_last_real_user_or_summary`, `build_compacted_history`, `build_compacted_history_with_limit`, `drain_to_completed`
  - Types: 0/2 matched
  - Missing types: `InitialContextInjection`, `CompactionAnalyticsAttempt`
  - Scoring failure: target contains TODO/stub/placeholder markers in function bodies
  - TODOs: 4
  - Lint issues: 1

- **command_safety.is_dangerous_command** → `commandsafety.IsDangerousCommand [ZERO] [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 1
  - Functions: 2/13 matched (target 3)
  - Missing functions: `is_dangerous_powershell_words`, `is_git_global_option_with_value`, `is_git_global_option_with_inline_value`, `git_global_option_requires_prompt`, `executable_name_lookup_key`, `find_git_subcommand`, `vec_str`, `rm_rf_is_dangerous`, `rm_f_is_dangerous`, `git_dash_c_requires_prompt`, `direct_powershell_words_reuse_windows_dangerous_detection`
  - Types: 0/0 matched
  - Missing types: _none_
  - Scoring failure: IsDangerousCommand.kt: Rust `let` binding in Kotlin comments
  - Lint issues: 1

- **execpolicy.rule** → `execpolicy.Rule`
  - Function similarity: 0.48
  - Dependencies: 1
  - Functions: 7/12 matched (target 9)
  - Missing functions: `with_resolved_program`, `parse`, `as_policy_string`, `normalize_network_rule_host`, `as_any`
  - Types: 5/8 matched
  - Missing types: `NetworkRuleProtocol`, `NetworkRule`, `RuleRef`

- **string.truncate** → `context.TruncationPolicy [PROVENANCE-FALLBACK]`
  - Function similarity: 0.55
  - Dependencies: 1
  - Functions: 8/11 matched (target 13)
  - Missing functions: `truncate_middle_chars`, `truncate_middle_with_token_budget`, `removed_units`
  - Types: 0/0 matched (target 3)
  - Missing types: _none_
  - Lint issues: 1

- **tui.frame_rate_limiter** → `tui.FrameRateLimiter`
  - Function similarity: 0.41
  - Dependencies: 1
  - Functions: 2/4 matched (target 2)
  - Missing functions: `default_does_not_clamp`, `clamps_to_min_interval_since_last_emit`
  - Types: 1/1 matched
  - Missing types: _none_

- **core.spawn** → `core.Spawn [ZERO] [PROVENANCE-FALLBACK]`
  - Function similarity: 0.00
  - Dependencies: 1
  - Functions: 0/1 matched (target 0)
  - Missing functions: `spawn_child_async`
  - Types: 1/2 matched
  - Missing types: `SpawnChildRequest`
  - Scoring failure: no target functions found; report scoring is function-by-function only
  - Lint issues: 1

## Missing Files (by Dependents)

| Rank | Source file | Expected target | Deps | Functions | Classes/types | Symbols | Source path | Expected path |
|------|-------------|-----------------|------|-----------|---------------|---------|-------------|---------------|
| 1 | `string.json` | `utils.string.src.Json` | 183 | 5 | 3 | 8 | `utils/string/src/json.rs` | `utils/string/src/Json.kt` |
| 2 | `protocol.thread_id` | `protocol.src.ThreadId` | 154 | 12 | 2 | 14 | `protocol/src/thread_id.rs` | `protocol/src/ThreadId.kt` |
| 3 | `runtime.value` | `codemode.src.runtime.Value` | 152 | 9 | 0 | 9 | `code-mode/src/runtime/value.rs` | `codemode/src/runtime/Value.kt` |
| 4 | `v2.fs` | `appserver.tests.suite.v2.Fs` | 132 | 21 | 0 | 21 | `app-server/tests/suite/v2/fs.rs` | `appserver/tests/suite/v2/Fs.kt` |
| 5 | `protocol.error` | `protocol.src.Error` | 105 | 23 | 9 | 32 | `protocol/src/error.rs` | `protocol/src/Error.kt` |
| 6 | `otel.config` | `otel.src.Config` | 104 | 2 | 5 | 7 | `otel/src/config.rs` | `otel/src/Config.kt` |
| 7 | `common.test_codex` | `core.tests.common.TestCodex` | 77 | 74 | 9 | 83 | `core/tests/common/test_codex.rs` | `core/tests/common/TestCodex.kt` |
| 8 | `tools.json_schema` | `tools.src.JsonSchema` | 75 | 20 | 4 | 24 | `tools/src/json_schema.rs` | `tools/src/JsonSchema.kt` |
| 9 | `codex-client.sse` | `codexclient.src.Sse` | 66 | 1 | 0 | 1 | `codex-client/src/sse.rs` | `codexclient/src/Sse.kt` |
| 10 | `common.mcp_process` | `mcpserver.tests.common.McpProcess` | 65 | 12 | 1 | 13 | `mcp-server/tests/common/mcp_process.rs` | `mcpserver/tests/common/McpProcess.kt` |
| 11 | `session.turn_context` | `core.src.session.TurnContext` | 51 | 27 | 3 | 30 | `core/src/session/turn_context.rs` | `core/src/session/TurnContext.kt` |
| 12 | `cloud-tasks-mock-client.mock` | `cloudtasksmockclient.src.Mock` | 46 | 11 | 1 | 12 | `cloud-tasks-mock-client/src/mock.rs` | `cloudtasksmockclient/src/Mock.kt` |
| 13 | `git-utils.info` | `gitutils.src.Info` | 45 | 23 | 3 | 26 | `git-utils/src/info.rs` | `gitutils/src/Info.kt` |
| 14 | `transport.stdio` | `appservertransport.src.transport.Stdio` | 33 | 2 | 0 | 2 | `app-server-transport/src/transport/stdio.rs` | `appservertransport/src/transport/Stdio.kt` |
| 15 | `models-manager.model_info` | `modelsmanager.src.ModelInfo` | 33 | 3 | 0 | 3 | `models-manager/src/model_info.rs` | `modelsmanager/src/ModelInfo.kt` |
| 16 | `codex-client.request` | `codexclient.src.Request` | 32 | 9 | 5 | 14 | `codex-client/src/request.rs` | `codexclient/src/Request.kt` |
| 17 | `keymap_setup.debug` | `tui.src.keymapsetup.Debug` | 32 | 15 | 2 | 17 | `tui/src/keymap_setup/debug.rs` | `tui/src/keymapsetup/Debug.kt` |
| 18 | `tui.token_usage` | `tui.src.TokenUsage` | 31 | 7 | 2 | 9 | `tui/src/token_usage.rs` | `tui/src/TokenUsage.kt` |
| 19 | `rollout-trace.thread` | `rollouttrace.src.Thread` | 29 | 23 | 5 | 28 | `rollout-trace/src/thread.rs` | `rollouttrace/src/Thread.kt` |
| 20 | `code-mode.response` | `codemode.src.Response` | 28 | 0 | 2 | 2 | `code-mode/src/response.rs` | `codemode/src/Response.kt` |
| 21 | `suite.personality` | `core.tests.suite.Personality` | 27 | 15 | 0 | 15 | `core/tests/suite/personality.rs` | `core/tests/suite/Personality.kt` |
| 22 | `protocol.tool_name` | `protocol.src.ToolName` | 26 | 7 | 1 | 8 | `protocol/src/tool_name.rs` | `protocol/src/ToolName.kt` |
| 23 | `events.session_telemetry` | `otel.src.events.SessionTelemetry` | 25 | 40 | 3 | 43 | `otel/src/events/session_telemetry.rs` | `otel/src/events/SessionTelemetry.kt` |
| 24 | `tools.tool_spec` | `tools.src.ToolSpec` | 24 | 10 | 5 | 15 | `tools/src/tool_spec.rs` | `tools/src/ToolSpec.kt` |
| 25 | `path-utils.env` | `utils.pathutils.src.Env` | 23 | 1 | 0 | 1 | `utils/path-utils/src/env.rs` | `utils/pathutils/src/Env.kt` |
| 26 | `config.config_toml` | `config.src.ConfigToml` | 23 | 20 | 18 | 38 | `config/src/config_toml.rs` | `config/src/ConfigToml.kt` |
| 27 | `core.codex_thread` | `core.src.CodexThread` | 22 | 39 | 3 | 42 | `core/src/codex_thread.rs` | `core/src/CodexThread.kt` |
| 28 | `rollout.policy` | `rollout.src.Policy` | 22 | 7 | 1 | 8 | `rollout/src/policy.rs` | `rollout/src/Policy.kt` |
| 29 | `core.thread_manager` | `core.src.ThreadManager` | 21 | 61 | 12 | 73 | `core/src/thread_manager.rs` | `core/src/ThreadManager.kt` |
| 30 | `protocol.agent_path` | `protocol.src.AgentPath` | 20 | 23 | 4 | 27 | `protocol/src/agent_path.rs` | `protocol/src/AgentPath.kt` |
| 31 | `suite.tool` | `applypatch.tests.suite.Tool` | 20 | 17 | 0 | 17 | `apply-patch/tests/suite/tool.rs` | `applypatch/tests/suite/Tool.kt` |
| 32 | `tui.history_cell` | `tui.src.historycell.HistoryCell` | 19 | 222 | 30 | 252 | `tui/src/history_cell.rs` | `tui/src/historycell/HistoryCell.kt` |
| 33 | `bottom_pane.bottom_pane_view` | `tui.src.bottompane.BottomPaneView` | 17 | 19 | 2 | 21 | `tui/src/bottom_pane/bottom_pane_view.rs` | `tui/src/bottompane/BottomPaneView.kt` |
| 34 | `request_user_input.layout` | `tui.src.bottompane.requestuserinput.Layout` | 15 | 7 | 5 | 12 | `tui/src/bottom_pane/request_user_input/layout.rs` | `tui/src/bottompane/requestuserinput/Layout.kt` |
| 35 | `tui.tui` | `tui.src.tui.Tui` | 15 | 47 | 8 | 55 | `tui/src/tui.rs` | `tui/src/tui/Tui.kt` |
| 36 | `otel.provider` | `otel.src.Provider` | 15 | 20 | 2 | 22 | `otel/src/provider.rs` | `otel/src/Provider.kt` |
| 37 | `config.constraint` | `config.src.Constraint` | 15 | 24 | 6 | 30 | `config/src/constraint.rs` | `config/src/Constraint.kt` |
| 38 | `mcp-server.outgoing_message` | `mcpserver.src.OutgoingMessage` | 15 | 13 | 9 | 22 | `mcp-server/src/outgoing_message.rs` | `mcpserver/src/OutgoingMessage.kt` |
| 39 | `execpolicy-legacy.exec_call` | `execpolicylegacy.src.ExecCall` | 14 | 2 | 1 | 3 | `execpolicy-legacy/src/exec_call.rs` | `execpolicylegacy/src/ExecCall.kt` |
| 40 | `plugin.plugin_id` | `plugin.src.PluginId` | 14 | 4 | 2 | 6 | `plugin/src/plugin_id.rs` | `plugin/src/PluginId.kt` |
| 41 | `config.config_requirements` | `config.src.ConfigRequirements` | 14 | 95 | 29 | 124 | `config/src/config_requirements.rs` | `config/src/ConfigRequirements.kt` |
| 42 | `suite.originator` | `exec.tests.suite.Originator` | 14 | 2 | 0 | 2 | `exec/tests/suite/originator.rs` | `exec/tests/suite/Originator.kt` |
| 43 | `app-server.thread_status` | `appserver.src.ThreadStatus` | 14 | 52 | 5 | 57 | `app-server/src/thread_status.rs` | `appserver/src/ThreadStatus.kt` |
| 44 | `login.token_data` | `login.src.TokenData` | 12 | 9 | 7 | 16 | `login/src/token_data.rs` | `login/src/TokenData.kt` |
| 45 | `exec-server.process_id` | `execserver.src.ProcessId` | 12 | 11 | 2 | 13 | `exec-server/src/process_id.rs` | `execserver/src/ProcessId.kt` |
| 46 | `execpolicy-legacy.arg_type` | `execpolicylegacy.src.ArgType` | 11 | 2 | 2 | 4 | `execpolicy-legacy/src/arg_type.rs` | `execpolicylegacy/src/ArgType.kt` |
| 47 | `config.tui_keymap` | `config.src.TuiKeymap` | 11 | 9 | 12 | 21 | `config/src/tui_keymap.rs` | `config/src/TuiKeymap.kt` |
| 48 | `guardian.prompt` | `core.src.guardian.Prompt` | 11 | 14 | 8 | 22 | `core/src/guardian/prompt.rs` | `core/src/guardian/Prompt.kt` |
| 49 | `tools.tool_definition` | `tools.src.ToolDefinition` | 11 | 2 | 1 | 3 | `tools/src/tool_definition.rs` | `tools/src/ToolDefinition.kt` |
| 50 | `tui.chatwidget` | `tui.src.chatwidget.Chatwidget` | 10 | 493 | 38 | 531 | `tui/src/chatwidget.rs` | `tui/src/chatwidget/Chatwidget.kt` |
| 51 | `core.connectors` | `core.src.Connectors` | 10 | 25 | 4 | 29 | `core/src/connectors.rs` | `core/src/Connectors.kt` |
| 52 | `common.test_codex_exec` | `core.tests.common.TestCodexExec` | 10 | 6 | 1 | 7 | `core/tests/common/test_codex_exec.rs` | `core/tests/common/TestCodexExec.kt` |
| 53 | `execpolicy-legacy.valid_exec` | `execpolicylegacy.src.ValidExec` | 10 | 6 | 4 | 10 | `execpolicy-legacy/src/valid_exec.rs` | `execpolicylegacy/src/ValidExec.kt` |
| 54 | `tests.http_client` | `execserver.tests.HttpClient` | 9 | 20 | 2 | 22 | `exec-server/tests/http_client.rs` | `execserver/tests/HttpClient.kt` |
| 55 | `execpolicy-legacy.policy_parser` | `execpolicylegacy.src.PolicyParser` | 9 | 13 | 3 | 16 | `execpolicy-legacy/src/policy_parser.rs` | `execpolicylegacy/src/PolicyParser.kt` |
| 56 | `model.thread_metadata` | `state.src.model.ThreadMetadata` | 9 | 16 | 10 | 26 | `state/src/model/thread_metadata.rs` | `state/src/model/ThreadMetadata.kt` |
| 57 | `tui.app_command` | `tui.src.AppCommand` | 9 | 25 | 1 | 26 | `tui/src/app_command.rs` | `tui/src/AppCommand.kt` |
| 58 | `exec-server.environment` | `execserver.src.Environment` | 9 | 41 | 3 | 44 | `exec-server/src/environment.rs` | `execserver/src/Environment.kt` |
| 59 | `debug-client.output` | `debugclient.src.Output` | 9 | 12 | 3 | 15 | `debug-client/src/output.rs` | `debugclient/src/Output.kt` |
| 60 | `tools.image_detail` | `tools.src.ImageDetail` | 8 | 3 | 0 | 3 | `tools/src/image_detail.rs` | `tools/src/ImageDetail.kt` |
| 61 | `model.thread_goal` | `state.src.model.ThreadGoal` | 8 | 6 | 4 | 10 | `state/src/model/thread_goal.rs` | `state/src/model/ThreadGoal.kt` |
| 62 | `app-server.config_manager` | `appserver.src.ConfigManager` | 8 | 23 | 1 | 24 | `app-server/src/config_manager.rs` | `appserver/src/ConfigManager.kt` |
| 63 | `tui.app_server_session` | `tui.src.AppServerSession` | 8 | 88 | 4 | 92 | `tui/src/app_server_session.rs` | `tui/src/AppServerSession.kt` |
| 64 | `protocol.models` | `protocol.src.Models` | 8 | 123 | 35 | 158 | `protocol/src/models.rs` | `protocol/src/Models.kt` |
| 65 | `config.state` | `config.src.State` | 8 | 24 | 4 | 28 | `config/src/state.rs` | `config/src/State.kt` |
| 66 | `tui.app` | `tui.src.app.App` | 8 | 16 | 10 | 26 | `tui/src/app.rs` | `tui/src/app/App.kt` |
| 67 | `tui.cli` | `tui.src.Cli` | 7 | 10 | 3 | 13 | `tui/src/cli.rs` | `tui/src/Cli.kt` |
| 68 | `secrets.local` | `secrets.src.Local` | 7 | 24 | 2 | 26 | `secrets/src/local.rs` | `secrets/src/Local.kt` |
| 69 | `execpolicy-legacy.arg_matcher` | `execpolicylegacy.src.ArgMatcher` | 7 | 5 | 4 | 9 | `execpolicy-legacy/src/arg_matcher.rs` | `execpolicylegacy/src/ArgMatcher.kt` |
| 70 | `engine.dispatcher` | `hooks.src.engine.Dispatcher` | 7 | 16 | 1 | 17 | `hooks/src/engine/dispatcher.rs` | `hooks/src/engine/Dispatcher.kt` |
| 71 | `tui.test_backend` | `tui.src.TestBackend` | 7 | 18 | 1 | 19 | `tui/src/test_backend.rs` | `tui/src/TestBackend.kt` |
| 72 | `tests.env_filter` | `cloudtasks.tests.EnvFilter` | 7 | 1 | 0 | 1 | `cloud-tasks/tests/env_filter.rs` | `cloudtasks/tests/EnvFilter.kt` |
| 73 | `common.context_snapshot` | `core.tests.common.ContextSnapshot` | 7 | 24 | 2 | 26 | `core/tests/common/context_snapshot.rs` | `core/tests/common/ContextSnapshot.kt` |
| 74 | `tests.exec_process` | `execserver.tests.ExecProcess` | 7 | 24 | 2 | 26 | `exec-server/tests/exec_process.rs` | `execserver/tests/ExecProcess.kt` |
| 75 | `config.skills_config` | `config.src.SkillsConfig` | 6 | 3 | 4 | 7 | `config/src/skills_config.rs` | `config/src/SkillsConfig.kt` |
| 76 | `code-mode.service` | `codemode.src.Service` | 6 | 26 | 8 | 34 | `code-mode/src/service.rs` | `codemode/src/Service.kt` |
| 77 | `rollout.metadata` | `rollout.src.Metadata` | 6 | 9 | 1 | 10 | `rollout/src/metadata.rs` | `rollout/src/Metadata.kt` |
| 78 | `rollout.state_db` | `rollout.src.StateDb` | 6 | 20 | 1 | 21 | `rollout/src/state_db.rs` | `rollout/src/StateDb.kt` |
| 79 | `common.apps_test_server` | `core.tests.common.AppsTestServer` | 6 | 7 | 2 | 9 | `core/tests/common/apps_test_server.rs` | `core/tests/common/AppsTestServer.kt` |
| 80 | `mcp.backend` | `memories.mcp.src.Backend` | 6 | 2 | 12 | 14 | `memories/mcp/src/backend.rs` | `memories/mcp/src/Backend.kt` |
| 81 | `client.http_response_body_stream` | `execserver.src.client.HttpResponseBodyStream` | 6 | 17 | 3 | 20 | `exec-server/src/client/http_response_body_stream.rs` | `execserver/src/client/HttpResponseBodyStream.kt` |
| 82 | `core.client` | `core.src.Client` | 6 | 57 | 14 | 71 | `core/src/client.rs` | `core/src/Client.kt` |
| 83 | `common.exec_server` | `execserver.tests.common.ExecServer` | 6 | 17 | 2 | 19 | `exec-server/tests/common/exec_server.rs` | `execserver/tests/common/ExecServer.kt` |
| 84 | `core.shell_snapshot` | `core.src.ShellSnapshot` | 6 | 19 | 1 | 20 | `core/src/shell_snapshot.rs` | `core/src/ShellSnapshot.kt` |
| 85 | `engine.output_parser` | `hooks.src.engine.OutputParser` | 6 | 22 | 8 | 30 | `hooks/src/engine/output_parser.rs` | `hooks/src/engine/OutputParser.kt` |
| 86 | `utils.path_utils` | `core.src.utils.PathUtils` | 5 | 0 | 0 | 0 | `core/src/utils/path_utils.rs` | `core/src/utils/PathUtils.kt` |
| 87 | `config.permissions_toml` | `config.src.PermissionsToml` | 5 | 12 | 10 | 22 | `config/src/permissions_toml.rs` | `config/src/PermissionsToml.kt` |
| 88 | `suite.hooks` | `core.tests.suite.Hooks` | 5 | 56 | 0 | 56 | `core/tests/suite/hooks.rs` | `core/tests/suite/Hooks.kt` |
| 89 | `bottom_pane.chat_composer` | `tui.src.bottompane.chatcomposer.ChatComposer` | 5 | 345 | 10 | 355 | `tui/src/bottom_pane/chat_composer.rs` | `tui/src/bottompane/chatcomposer/ChatComposer.kt` |
| 90 | `hooks.schema` | `hooks.src.Schema` | 5 | 26 | 25 | 51 | `hooks/src/schema.rs` | `hooks/src/Schema.kt` |
| 91 | `login.auth_env_telemetry` | `login.src.AuthEnvTelemetry` | 5 | 4 | 1 | 5 | `login/src/auth_env_telemetry.rs` | `login/src/AuthEnvTelemetry.kt` |
| 92 | `unified_exec.head_tail_buffer` | `core.src.unifiedexec.HeadTailBuffer` | 5 | 10 | 1 | 11 | `core/src/unified_exec/head_tail_buffer.rs` | `core/src/unifiedexec/HeadTailBuffer.kt` |
| 93 | `core.network_policy_decision` | `core.src.NetworkPolicyDecision` | 5 | 4 | 1 | 5 | `core/src/network_policy_decision.rs` | `core/src/NetworkPolicyDecision.kt` |
| 94 | `login.server` | `login.src.Server` | 5 | 40 | 8 | 48 | `login/src/server.rs` | `login/src/Server.kt` |
| 95 | `guardian.approval_request` | `core.src.guardian.ApprovalRequest` | 5 | 11 | 9 | 20 | `core/src/guardian/approval_request.rs` | `core/src/guardian/ApprovalRequest.kt` |
| 96 | `app.thread_session_state` | `tui.src.app.ThreadSessionState` | 4 | 8 | 0 | 8 | `tui/src/app/thread_session_state.rs` | `tui/src/app/ThreadSessionState.kt` |
| 97 | `otel.targets` | `otel.src.Targets` | 4 | 2 | 0 | 2 | `otel/src/targets.rs` | `otel/src/Targets.kt` |
| 98 | `app-server-protocol.experimental_api` | `appserverprotocol.src.ExperimentalApi` | 4 | 11 | 7 | 18 | `app-server-protocol/src/experimental_api.rs` | `appserverprotocol/src/ExperimentalApi.kt` |
| 99 | `codex-mcp.rmcp_client` | `codexmcp.src.RmcpClient` | 4 | 19 | 4 | 23 | `codex-mcp/src/rmcp_client.rs` | `codexmcp/src/RmcpClient.kt` |
| 100 | `context_manager.history` | `core.src.contextmanager.History` | 4 | 38 | 2 | 40 | `core/src/context_manager/history.rs` | `core/src/contextmanager/History.kt` |
| 101 | `win.psuedocon` | `utils.pty.src.win.Psuedocon` | 4 | 14 | 2 | 16 | `utils/pty/src/win/psuedocon.rs` | `utils/pty/src/win/Psuedocon.kt` |
| 102 | `context.subagent_notification` | `core.src.context.SubagentNotification` | 4 | 2 | 1 | 3 | `core/src/context/subagent_notification.rs` | `core/src/context/SubagentNotification.kt` |
| 103 | `client.reqwest_http_client` | `execserver.src.client.ReqwestHttpClient` | 4 | 8 | 3 | 11 | `exec-server/src/client/reqwest_http_client.rs` | `execserver/src/client/ReqwestHttpClient.kt` |
| 104 | `config.managed_features` | `core.src.config.ManagedFeatures` | 4 | 23 | 2 | 25 | `core/src/config/managed_features.rs` | `core/src/config/ManagedFeatures.kt` |
| 105 | `exec.event_processor` | `exec.src.EventProcessor` | 4 | 3 | 2 | 5 | `exec/src/event_processor.rs` | `exec/src/EventProcessor.kt` |
| 106 | `protocol.permissions` | `protocol.src.Permissions` | 4 | 114 | 10 | 124 | `protocol/src/permissions.rs` | `protocol/src/Permissions.kt` |
| 107 | `v2.mcp_server_status` | `appserver.tests.suite.v2.McpServerStatus` | 4 | 11 | 2 | 13 | `app-server/tests/suite/v2/mcp_server_status.rs` | `appserver/tests/suite/v2/McpServerStatus.kt` |
| 108 | `context.permissions_instructions` | `core.src.context.PermissionsInstructions` | 4 | 14 | 2 | 16 | `core/src/context/permissions_instructions.rs` | `core/src/context/PermissionsInstructions.kt` |
| 109 | `unix.escalation_policy` | `shellescalation.src.unix.EscalationPolicy` | 4 | 0 | 1 | 1 | `shell-escalation/src/unix/escalation_policy.rs` | `shellescalation/src/unix/EscalationPolicy.kt` |
| 110 | `protocol.protocol` | `protocol.src.Protocol` | 4 | 152 | 173 | 325 | `protocol/src/protocol.rs` | `protocol/src/Protocol.kt` |
| 111 | `mcp-server.message_processor` | `mcpserver.src.MessageProcessor` | 4 | 25 | 1 | 26 | `mcp-server/src/message_processor.rs` | `mcpserver/src/MessageProcessor.kt` |
| 112 | `models.config_file_response` | `codexbackendopenapimodels.src.models.ConfigFileResponse` | 4 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/config_file_response.rs` | `codexbackendopenapimodels/src/models/ConfigFileResponse.kt` |
| 113 | `tui.model_catalog` | `tui.src.ModelCatalog` | 4 | 2 | 1 | 3 | `tui/src/model_catalog.rs` | `tui/src/ModelCatalog.kt` |
| 114 | `exec-server.local_file_system` | `execserver.src.LocalFileSystem` | 4 | 36 | 3 | 39 | `exec-server/src/local_file_system.rs` | `execserver/src/LocalFileSystem.kt` |
| 115 | `models.paginated_list_task_list_item_` | `codexbackendopenapimodels.src.models.PaginatedListTaskListItem` | 4 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/paginated_list_task_list_item_.rs` | `codexbackendopenapimodels/src/models/PaginatedListTaskListItem.kt` |
| 116 | `suite.request_compression` | `core.tests.suite.RequestCompression` | 4 | 2 | 0 | 2 | `core/tests/suite/request_compression.rs` | `core/tests/suite/RequestCompression.kt` |
| 117 | `model-provider.bearer_auth_provider` | `modelprovider.src.BearerAuthProvider` | 4 | 6 | 1 | 7 | `model-provider/src/bearer_auth_provider.rs` | `modelprovider/src/BearerAuthProvider.kt` |
| 118 | `onboarding.keys` | `tui.src.onboarding.Keys` | 4 | 0 | 0 | 0 | `tui/src/onboarding/keys.rs` | `tui/src/onboarding/Keys.kt` |
| 119 | `models.rate_limit_status_payload` | `codexbackendopenapimodels.src.models.RateLimitStatusPayload` | 3 | 1 | 4 | 5 | `codex-backend-openapi-models/src/models/rate_limit_status_payload.rs` | `codexbackendopenapimodels/src/models/RateLimitStatusPayload.kt` |
| 120 | `read.usage` | `memories.read.src.Usage` | 3 | 3 | 1 | 4 | `memories/read/src/usage.rs` | `memories/read/src/Usage.kt` |
| 121 | `models.task_list_item` | `codexbackendopenapimodels.src.models.TaskListItem` | 3 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/task_list_item.rs` | `codexbackendopenapimodels/src/models/TaskListItem.kt` |
| 122 | `rmcp-client.stdio_server_launcher` | `rmcpclient.src.StdioServerLauncher` | 3 | 26 | 12 | 38 | `rmcp-client/src/stdio_server_launcher.rs` | `rmcpclient/src/StdioServerLauncher.kt` |
| 123 | `context.collaboration_mode_instructions` | `core.src.context.CollaborationModeInstructions` | 3 | 2 | 1 | 3 | `core/src/context/collaboration_mode_instructions.rs` | `core/src/context/CollaborationModeInstructions.kt` |
| 124 | `rmcp-client.perform_oauth_login` | `rmcpclient.src.PerformOauthLogin` | 3 | 31 | 8 | 39 | `rmcp-client/src/perform_oauth_login.rs` | `rmcpclient/src/PerformOauthLogin.kt` |
| 125 | `context.available_plugins_instructions` | `core.src.context.AvailablePluginsInstructions` | 3 | 2 | 1 | 3 | `core/src/context/available_plugins_instructions.rs` | `core/src/context/AvailablePluginsInstructions.kt` |
| 126 | `context.apps_instructions` | `core.src.context.AppsInstructions` | 3 | 2 | 1 | 3 | `core/src/context/apps_instructions.rs` | `core/src/context/AppsInstructions.kt` |
| 127 | `execpolicy-legacy.opt` | `execpolicylegacy.src.Opt` | 3 | 4 | 4 | 8 | `execpolicy-legacy/src/opt.rs` | `execpolicylegacy/src/Opt.kt` |
| 128 | `models.code_task_details_response` | `codexbackendopenapimodels.src.models.CodeTaskDetailsResponse` | 3 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/code_task_details_response.rs` | `codexbackendopenapimodels/src/models/CodeTaskDetailsResponse.kt` |
| 129 | `protocol.v2` | `appserverprotocol.src.protocol.V2` | 3 | 228 | 469 | 697 | `app-server-protocol/src/protocol/v2.rs` | `appserverprotocol/src/protocol/V2.kt` |
| 130 | `config.network_proxy_spec` | `core.src.config.NetworkProxySpec` | 3 | 24 | 3 | 27 | `core/src/config/network_proxy_spec.rs` | `core/src/config/NetworkProxySpec.kt` |
| 131 | `protocol.shell_environment` | `protocol.src.ShellEnvironment` | 3 | 8 | 0 | 8 | `protocol/src/shell_environment.rs` | `protocol/src/ShellEnvironment.kt` |
| 132 | `unix.escalate_server` | `shellescalation.src.unix.EscalateServer` | 3 | 29 | 12 | 41 | `shell-escalation/src/unix/escalate_server.rs` | `shellescalation/src/unix/EscalateServer.kt` |
| 133 | `context.personality_spec_instructions` | `core.src.context.PersonalitySpecInstructions` | 3 | 2 | 1 | 3 | `core/src/context/personality_spec_instructions.rs` | `core/src/context/PersonalitySpecInstructions.kt` |
| 134 | `unix.stopwatch` | `shellescalation.src.unix.Stopwatch` | 3 | 10 | 2 | 12 | `shell-escalation/src/unix/stopwatch.rs` | `shellescalation/src/unix/Stopwatch.kt` |
| 135 | `model.agent_job` | `state.src.model.AgentJob` | 3 | 8 | 10 | 18 | `state/src/model/agent_job.rs` | `state/src/model/AgentJob.kt` |
| 136 | `protocol.memory_citation` | `protocol.src.MemoryCitation` | 3 | 0 | 2 | 2 | `protocol/src/memory_citation.rs` | `protocol/src/MemoryCitation.kt` |
| 137 | `model.backfill_state` | `state.src.model.BackfillState` | 3 | 5 | 2 | 7 | `state/src/model/backfill_state.rs` | `state/src/model/BackfillState.kt` |
| 138 | `agent.status` | `core.src.agent.Status` | 3 | 2 | 0 | 2 | `core/src/agent/status.rs` | `core/src/agent/Status.kt` |
| 139 | `context.turn_aborted` | `core.src.context.TurnAborted` | 3 | 2 | 1 | 3 | `core/src/context/turn_aborted.rs` | `core/src/context/TurnAborted.kt` |
| 140 | `thread-store.live_thread` | `threadstore.src.LiveThread` | 3 | 15 | 2 | 17 | `thread-store/src/live_thread.rs` | `threadstore/src/LiveThread.kt` |
| 141 | `core.file_watcher` | `core.src.FileWatcher` | 3 | 35 | 17 | 52 | `core/src/file_watcher.rs` | `core/src/FileWatcher.kt` |
| 142 | `otel.trace_context` | `otel.src.TraceContext` | 3 | 13 | 0 | 13 | `otel/src/trace_context.rs` | `otel/src/TraceContext.kt` |
| 143 | `server.session_registry` | `execserver.src.server.SessionRegistry` | 3 | 17 | 6 | 23 | `exec-server/src/server/session_registry.rs` | `execserver/src/server/SessionRegistry.kt` |
| 144 | `suite.model_availability_nux` | `tui.tests.suite.ModelAvailabilityNux` | 3 | 1 | 0 | 1 | `tui/tests/suite/model_availability_nux.rs` | `tui/tests/suite/ModelAvailabilityNux.kt` |
| 145 | `metrics.timer` | `otel.src.metrics.Timer` | 3 | 3 | 1 | 4 | `otel/src/metrics/timer.rs` | `otel/src/metrics/Timer.kt` |
| 146 | `events.shared` | `otel.src.events.Shared` | 3 | 1 | 0 | 1 | `otel/src/events/shared.rs` | `otel/src/events/Shared.kt` |
| 147 | `v2.remote_thread_store` | `appserver.tests.suite.v2.RemoteThreadStore` | 3 | 5 | 1 | 6 | `app-server/tests/suite/v2/remote_thread_store.rs` | `appserver/tests/suite/v2/RemoteThreadStore.kt` |
| 148 | `desktop_app.mac` | `cli.src.desktopapp.Mac` | 3 | 18 | 0 | 18 | `cli/src/desktop_app/mac.rs` | `cli/src/desktopapp/Mac.kt` |
| 149 | `bin.write_schema_fixtures` | `appserverprotocol.src.bin.WriteSchemaFixtures` | 3 | 1 | 1 | 2 | `app-server-protocol/src/bin/write_schema_fixtures.rs` | `appserverprotocol/src/bin/WriteSchemaFixtures.kt` |
| 150 | `core.skills_watcher` | `core.src.SkillsWatcher` | 3 | 6 | 2 | 8 | `core/src/skills_watcher.rs` | `core/src/SkillsWatcher.kt` |
| 151 | `app-server.in_process` | `appserver.src.InProcess` | 3 | 23 | 7 | 30 | `app-server/src/in_process.rs` | `appserver/src/InProcess.kt` |
| 152 | `bottom_pane.textarea` | `tui.src.bottompane.Textarea` | 3 | 177 | 10 | 187 | `tui/src/bottom_pane/textarea.rs` | `tui/src/bottompane/Textarea.kt` |
| 153 | `core-skills.model` | `coreskills.src.Model` | 3 | 12 | 8 | 20 | `core-skills/src/model.rs` | `coreskills/src/Model.kt` |
| 154 | `config.requirements_exec_policy` | `config.src.RequirementsExecPolicy` | 3 | 8 | 6 | 14 | `config/src/requirements_exec_policy.rs` | `config/src/RequirementsExecPolicy.kt` |
| 155 | `bottom_pane.slash_commands` | `tui.src.bottompane.SlashCommands` | 3 | 15 | 1 | 16 | `tui/src/bottom_pane/slash_commands.rs` | `tui/src/bottompane/SlashCommands.kt` |
| 156 | `bottom_pane.list_selection_view` | `tui.src.bottompane.ListSelectionView` | 3 | 95 | 12 | 107 | `tui/src/bottom_pane/list_selection_view.rs` | `tui/src/bottompane/ListSelectionView.kt` |
| 157 | `public_widgets.composer_input` | `tui.src.publicwidgets.ComposerInput` | 3 | 15 | 2 | 17 | `tui/src/public_widgets/composer_input.rs` | `tui/src/publicwidgets/ComposerInput.kt` |
| 158 | `exec-server.environment_provider` | `execserver.src.EnvironmentProvider` | 2 | 11 | 2 | 13 | `exec-server/src/environment_provider.rs` | `execserver/src/EnvironmentProvider.kt` |
| 159 | `bottom_pane.custom_prompt_view` | `tui.src.bottompane.CustomPromptView` | 2 | 11 | 2 | 13 | `tui/src/bottom_pane/custom_prompt_view.rs` | `tui/src/bottompane/CustomPromptView.kt` |
| 160 | `core-skills.injection` | `coreskills.src.Injection` | 2 | 18 | 5 | 23 | `core-skills/src/injection.rs` | `coreskills/src/Injection.kt` |
| 161 | `bottom_pane.skills_toggle_view` | `tui.src.bottompane.SkillsToggleView` | 2 | 19 | 2 | 21 | `tui/src/bottom_pane/skills_toggle_view.rs` | `tui/src/bottompane/SkillsToggleView.kt` |
| 162 | `windows-sandbox-rs.acl` | `windowssandboxrs.src.Acl` | 2 | 13 | 0 | 13 | `windows-sandbox-rs/src/acl.rs` | `windowssandboxrs/src/Acl.kt` |
| 163 | `pty.process` | `utils.pty.src.Process` | 2 | 17 | 10 | 27 | `utils/pty/src/process.rs` | `utils/pty/src/Process.kt` |
| 164 | `codex-mcp.tools` | `codexmcp.src.Tools` | 2 | 16 | 3 | 19 | `codex-mcp/src/tools.rs` | `codexmcp/src/Tools.kt` |
| 165 | `agent.mailbox` | `core.src.agent.Mailbox` | 2 | 11 | 2 | 13 | `core/src/agent/mailbox.rs` | `core/src/agent/Mailbox.kt` |
| 166 | `suite.validation` | `otel.tests.suite.Validation` | 2 | 6 | 0 | 6 | `otel/tests/suite/validation.rs` | `otel/tests/suite/Validation.kt` |
| 167 | `tui.version` | `tui.src.Version` | 2 | 0 | 0 | 0 | `tui/src/version.rs` | `tui/src/Version.kt` |
| 168 | `codex-mcp.elicitation` | `codexmcp.src.Elicitation` | 2 | 5 | 2 | 7 | `codex-mcp/src/elicitation.rs` | `codexmcp/src/Elicitation.kt` |
| 169 | `apps.render` | `core.src.apps.Render` | 2 | 4 | 0 | 4 | `core/src/apps/render.rs` | `core/src/apps/Render.kt` |
| 170 | `protocol.v1` | `appserverprotocol.src.protocol.V1` | 2 | 0 | 23 | 23 | `app-server-protocol/src/protocol/v1.rs` | `appserverprotocol/src/protocol/V1.kt` |
| 171 | `context.approved_command_prefix_saved` | `core.src.context.ApprovedCommandPrefixSaved` | 2 | 2 | 1 | 3 | `core/src/context/approved_command_prefix_saved.rs` | `core/src/context/ApprovedCommandPrefixSaved.kt` |
| 172 | `tui.session_state` | `tui.src.SessionState` | 2 | 0 | 2 | 2 | `tui/src/session_state.rs` | `tui/src/SessionState.kt` |
| 173 | `context.available_skills_instructions` | `core.src.context.AvailableSkillsInstructions` | 2 | 2 | 1 | 3 | `core/src/context/available_skills_instructions.rs` | `core/src/context/AvailableSkillsInstructions.kt` |
| 174 | `context.guardian_followup_review_reminder` | `core.src.context.GuardianFollowupReviewReminder` | 2 | 1 | 1 | 2 | `core/src/context/guardian_followup_review_reminder.rs` | `core/src/context/GuardianFollowupReviewReminder.kt` |
| 175 | `feedback.feedback_diagnostics` | `feedback.src.FeedbackDiagnostics` | 2 | 10 | 2 | 12 | `feedback/src/feedback_diagnostics.rs` | `feedback/src/FeedbackDiagnostics.kt` |
| 176 | `suite.parse_sed_command` | `execpolicylegacy.tests.suite.ParseSedCommand` | 2 | 2 | 0 | 2 | `execpolicy-legacy/tests/suite/parse_sed_command.rs` | `execpolicylegacy/tests/suite/ParseSedCommand.kt` |
| 177 | `models.rate_limit_window_snapshot` | `codexbackendopenapimodels.src.models.RateLimitWindowSnapshot` | 2 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/rate_limit_window_snapshot.rs` | `codexbackendopenapimodels/src/models/RateLimitWindowSnapshot.kt` |
| 178 | `models.rate_limit_status_details` | `codexbackendopenapimodels.src.models.RateLimitStatusDetails` | 2 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/rate_limit_status_details.rs` | `codexbackendopenapimodels/src/models/RateLimitStatusDetails.kt` |
| 179 | `models.credit_status_details` | `codexbackendopenapimodels.src.models.CreditStatusDetails` | 2 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/credit_status_details.rs` | `codexbackendopenapimodels/src/models/CreditStatusDetails.kt` |
| 180 | `models.additional_rate_limit_details` | `codexbackendopenapimodels.src.models.AdditionalRateLimitDetails` | 2 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/additional_rate_limit_details.rs` | `codexbackendopenapimodels/src/models/AdditionalRateLimitDetails.kt` |
| 181 | `rollout.list` | `rollout.src.List` | 2 | 42 | 16 | 58 | `rollout/src/list.rs` | `rollout/src/List.kt` |
| 182 | `sandboxing.manager` | `sandboxing.src.Manager` | 2 | 12 | 7 | 19 | `sandboxing/src/manager.rs` | `sandboxing/src/Manager.kt` |
| 183 | `bin.main_execve_wrapper` | `shellescalation.src.bin.MainExecveWrapper` | 2 | 1 | 0 | 1 | `shell-escalation/src/bin/main_execve_wrapper.rs` | `shellescalation/src/bin/MainExecveWrapper.kt` |
| 184 | `suite.conversation_summary` | `appserver.tests.suite.ConversationSummary` | 2 | 6 | 0 | 6 | `app-server/tests/suite/conversation_summary.rs` | `appserver/tests/suite/ConversationSummary.kt` |
| 185 | `context.hook_additional_context` | `core.src.context.HookAdditionalContext` | 2 | 2 | 1 | 3 | `core/src/context/hook_additional_context.rs` | `core/src/context/HookAdditionalContext.kt` |
| 186 | `context.image_generation_instructions` | `core.src.context.ImageGenerationInstructions` | 2 | 2 | 1 | 3 | `core/src/context/image_generation_instructions.rs` | `core/src/context/ImageGenerationInstructions.kt` |
| 187 | `context.model_switch_instructions` | `core.src.context.ModelSwitchInstructions` | 2 | 2 | 1 | 3 | `core/src/context/model_switch_instructions.rs` | `core/src/context/ModelSwitchInstructions.kt` |
| 188 | `exec.event_processor_with_human_output` | `exec.src.EventProcessorWithHumanOutput` | 2 | 18 | 1 | 19 | `exec/src/event_processor_with_human_output.rs` | `exec/src/EventProcessorWithHumanOutput.kt` |
| 189 | `context.network_rule_saved` | `core.src.context.NetworkRuleSaved` | 2 | 2 | 1 | 3 | `core/src/context/network_rule_saved.rs` | `core/src/context/NetworkRuleSaved.kt` |
| 190 | `chatgpt.workspace_settings` | `chatgpt.src.WorkspaceSettings` | 2 | 4 | 4 | 8 | `chatgpt/src/workspace_settings.rs` | `chatgpt/src/WorkspaceSettings.kt` |
| 191 | `state.log_db` | `state.src.LogDb` | 2 | 48 | 10 | 58 | `state/src/log_db.rs` | `state/src/LogDb.kt` |
| 192 | `context.plugin_instructions` | `core.src.context.PluginInstructions` | 2 | 2 | 1 | 3 | `core/src/context/plugin_instructions.rs` | `core/src/context/PluginInstructions.kt` |
| 193 | `context.realtime_end_instructions` | `core.src.context.RealtimeEndInstructions` | 2 | 2 | 1 | 3 | `core/src/context/realtime_end_instructions.rs` | `core/src/context/RealtimeEndInstructions.kt` |
| 194 | `context.realtime_start_instructions` | `core.src.context.RealtimeStartInstructions` | 2 | 1 | 1 | 2 | `core/src/context/realtime_start_instructions.rs` | `core/src/context/RealtimeStartInstructions.kt` |
| 195 | `local.live_writer` | `threadstore.src.local.LiveWriter` | 2 | 9 | 0 | 9 | `thread-store/src/local/live_writer.rs` | `threadstore/src/local/LiveWriter.kt` |
| 196 | `app-server.transport` | `appserver.src.Transport` | 2 | 9 | 2 | 11 | `app-server/src/transport.rs` | `appserver/src/Transport.kt` |
| 197 | `context.realtime_start_with_instructions` | `core.src.context.RealtimeStartWithInstructions` | 2 | 2 | 1 | 3 | `core/src/context/realtime_start_with_instructions.rs` | `core/src/context/RealtimeStartWithInstructions.kt` |
| 198 | `context.skill_instructions` | `core.src.context.SkillInstructions` | 2 | 2 | 1 | 3 | `core/src/context/skill_instructions.rs` | `core/src/context/SkillInstructions.kt` |
| 199 | `app-server.thread_state` | `appserver.src.ThreadState` | 2 | 22 | 8 | 30 | `app-server/src/thread_state.rs` | `appserver/src/ThreadState.kt` |
| 200 | `tui.ide_context` | `tui.src.idecontext.IdeContext` | 2 | 1 | 5 | 6 | `tui/src/ide_context.rs` | `tui/src/idecontext/IdeContext.kt` |
| 201 | `history_cell.hook_cell` | `tui.src.historycell.HookCell` | 2 | 54 | 5 | 59 | `tui/src/history_cell/hook_cell.rs` | `tui/src/historycell/HookCell.kt` |
| 202 | `app-server.connection_rpc_gate` | `appserver.src.ConnectionRpcGate` | 2 | 11 | 1 | 12 | `app-server/src/connection_rpc_gate.rs` | `appserver/src/ConnectionRpcGate.kt` |
| 203 | `windows-sandbox-rs.logging` | `windowssandboxrs.src.Logging` | 2 | 10 | 0 | 10 | `windows-sandbox-rs/src/logging.rs` | `windowssandboxrs/src/Logging.kt` |
| 204 | `bottom_pane.multi_select_picker` | `tui.src.bottompane.MultiSelectPicker` | 2 | 34 | 9 | 43 | `tui/src/bottom_pane/multi_select_picker.rs` | `tui/src/bottompane/MultiSelectPicker.kt` |
| 205 | `exec-server.local_process` | `execserver.src.LocalProcess` | 2 | 37 | 7 | 44 | `exec-server/src/local_process.rs` | `execserver/src/LocalProcess.kt` |
| 206 | `bottom_pane.memories_settings_view` | `tui.src.bottompane.MemoriesSettingsView` | 2 | 24 | 4 | 28 | `tui/src/bottom_pane/memories_settings_view.rs` | `tui/src/bottompane/MemoriesSettingsView.kt` |
| 207 | `bottom_pane.hooks_browser_view` | `tui.src.bottompane.HooksBrowserView` | 2 | 53 | 3 | 56 | `tui/src/bottom_pane/hooks_browser_view.rs` | `tui/src/bottompane/HooksBrowserView.kt` |
| 208 | `bottom_pane.experimental_features_view` | `tui.src.bottompane.ExperimentalFeaturesView` | 2 | 14 | 2 | 16 | `tui/src/bottom_pane/experimental_features_view.rs` | `tui/src/bottompane/ExperimentalFeaturesView.kt` |
| 209 | `suite.turn_state` | `core.tests.suite.TurnState` | 2 | 2 | 0 | 2 | `core/tests/suite/turn_state.rs` | `core/tests/suite/TurnState.kt` |
| 210 | `write.guard` | `memories.write.src.Guard` | 1 | 4 | 0 | 4 | `memories/write/src/guard.rs` | `memories/write/src/Guard.kt` |
| 211 | `runtime.timers` | `codemode.src.runtime.Timers` | 1 | 5 | 1 | 6 | `code-mode/src/runtime/timers.rs` | `codemode/src/runtime/Timers.kt` |
| 212 | `bottom_pane.approval_overlay` | `tui.src.bottompane.ApprovalOverlay` | 1 | 76 | 5 | 81 | `tui/src/bottom_pane/approval_overlay.rs` | `tui/src/bottompane/ApprovalOverlay.kt` |
| 213 | `suite.truncation` | `core.tests.suite.Truncation` | 1 | 11 | 0 | 11 | `core/tests/suite/truncation.rs` | `core/tests/suite/Truncation.kt` |
| 214 | `bottom_pane.chat_composer_history` | `tui.src.bottompane.ChatComposerHistory` | 1 | 39 | 8 | 47 | `tui/src/bottom_pane/chat_composer_history.rs` | `tui/src/bottompane/ChatComposerHistory.kt` |
| 215 | `bottom_pane.command_popup` | `tui.src.bottompane.CommandPopup` | 1 | 24 | 3 | 27 | `tui/src/bottom_pane/command_popup.rs` | `tui/src/bottompane/CommandPopup.kt` |
| 216 | `bottom_pane.app_link_view` | `tui.src.bottompane.AppLinkView` | 1 | 41 | 5 | 46 | `tui/src/bottom_pane/app_link_view.rs` | `tui/src/bottompane/AppLinkView.kt` |
| 217 | `tui.auto_review_denials` | `tui.src.AutoReviewDenials` | 1 | 7 | 1 | 8 | `tui/src/auto_review_denials.rs` | `tui/src/AutoReviewDenials.kt` |
| 218 | `bottom_pane.file_search_popup` | `tui.src.bottompane.FileSearchPopup` | 1 | 11 | 1 | 12 | `tui/src/bottom_pane/file_search_popup.rs` | `tui/src/bottompane/FileSearchPopup.kt` |
| 219 | `tui.ascii_animation` | `tui.src.AsciiAnimation` | 1 | 7 | 1 | 8 | `tui/src/ascii_animation.rs` | `tui/src/AsciiAnimation.kt` |
| 220 | `tools.tool_registry_plan` | `tools.src.ToolRegistryPlan` | 1 | 3 | 0 | 3 | `tools/src/tool_registry_plan.rs` | `tools/src/ToolRegistryPlan.kt` |
| 221 | `local.read_thread` | `threadstore.src.local.ReadThread` | 1 | 32 | 0 | 32 | `thread-store/src/local/read_thread.rs` | `threadstore/src/local/ReadThread.kt` |
| 222 | `bottom_pane.paste_burst` | `tui.src.bottompane.PasteBurst` | 1 | 24 | 4 | 28 | `tui/src/bottom_pane/paste_burst.rs` | `tui/src/bottompane/PasteBurst.kt` |
| 223 | `cloud-tasks.scrollable_diff` | `cloudtasks.src.ScrollableDiff` | 1 | 15 | 2 | 17 | `cloud-tasks/src/scrollable_diff.rs` | `cloudtasks/src/ScrollableDiff.kt` |
| 224 | `unified_exec.process_state` | `core.src.unifiedexec.ProcessState` | 1 | 2 | 1 | 3 | `core/src/unified_exec/process_state.rs` | `core/src/unifiedexec/ProcessState.kt` |
| 225 | `bottom_pane.pending_input_preview` | `tui.src.bottompane.PendingInputPreview` | 1 | 19 | 1 | 20 | `tui/src/bottom_pane/pending_input_preview.rs` | `tui/src/bottompane/PendingInputPreview.kt` |
| 226 | `bottom_pane.pending_thread_approvals` | `tui.src.bottompane.PendingThreadApprovals` | 1 | 11 | 1 | 12 | `tui/src/bottom_pane/pending_thread_approvals.rs` | `tui/src/bottompane/PendingThreadApprovals.kt` |
| 227 | `tools.tool_search_entry` | `core.src.tools.ToolSearchEntry` | 1 | 6 | 1 | 7 | `core/src/tools/tool_search_entry.rs` | `core/src/tools/ToolSearchEntry.kt` |
| 228 | `bottom_pane.skill_popup` | `tui.src.bottompane.SkillPopup` | 1 | 22 | 2 | 24 | `tui/src/bottom_pane/skill_popup.rs` | `tui/src/bottompane/SkillPopup.kt` |
| 229 | `tools.tool_dispatch_trace` | `core.src.tools.ToolDispatchTrace` | 1 | 6 | 1 | 7 | `core/src/tools/tool_dispatch_trace.rs` | `core/src/tools/ToolDispatchTrace.kt` |
| 230 | `shell.zsh_fork_backend` | `core.src.tools.runtimes.shell.ZshForkBackend` | 1 | 8 | 2 | 10 | `core/src/tools/runtimes/shell/zsh_fork_backend.rs` | `core/src/tools/runtimes/shell/ZshForkBackend.kt` |
| 231 | `shell.unix_escalation` | `core.src.tools.runtimes.shell.UnixEscalation` | 1 | 17 | 9 | 26 | `core/src/tools/runtimes/shell/unix_escalation.rs` | `core/src/tools/runtimes/shell/UnixEscalation.kt` |
| 232 | `exec-server.remote_file_system` | `execserver.src.RemoteFileSystem` | 1 | 14 | 1 | 15 | `exec-server/src/remote_file_system.rs` | `execserver/src/RemoteFileSystem.kt` |
| 233 | `write.phase2` | `memories.write.src.Phase2` | 1 | 13 | 2 | 15 | `memories/write/src/phase2.rs` | `memories/write/src/Phase2.kt` |
| 234 | `bottom_pane.unified_exec_footer` | `tui.src.bottompane.UnifiedExecFooter` | 1 | 10 | 1 | 11 | `tui/src/bottom_pane/unified_exec_footer.rs` | `tui/src/bottompane/UnifiedExecFooter.kt` |
| 235 | `tui.branch_summary` | `tui.src.BranchSummary` | 1 | 28 | 10 | 38 | `tui/src/branch_summary.rs` | `tui/src/BranchSummary.kt` |
| 236 | `chatwidget.session_header` | `tui.src.chatwidget.SessionHeader` | 1 | 2 | 1 | 3 | `tui/src/chatwidget/session_header.rs` | `tui/src/chatwidget/SessionHeader.kt` |
| 237 | `app-server.error_code` | `appserver.src.ErrorCode` | 1 | 4 | 0 | 4 | `app-server/src/error_code.rs` | `appserver/src/ErrorCode.kt` |
| 238 | `tui.collaboration_modes` | `tui.src.CollaborationModes` | 1 | 7 | 0 | 7 | `tui/src/collaboration_modes.rs` | `tui/src/CollaborationModes.kt` |
| 239 | `core.review_prompts` | `core.src.ReviewPrompts` | 1 | 9 | 1 | 10 | `core/src/review_prompts.rs` | `core/src/ReviewPrompts.kt` |
| 240 | `exec-server.remote_process` | `execserver.src.RemoteProcess` | 1 | 9 | 2 | 11 | `exec-server/src/remote_process.rs` | `execserver/src/RemoteProcess.kt` |
| 241 | `core.mention_syntax` | `core.src.MentionSyntax` | 1 | 0 | 0 | 0 | `core/src/mention_syntax.rs` | `core/src/MentionSyntax.kt` |
| 242 | `tui.custom_terminal` | `tui.src.CustomTerminal` | 1 | 63 | 5 | 68 | `tui/src/custom_terminal.rs` | `tui/src/CustomTerminal.kt` |
| 243 | `tui.cwd_prompt` | `tui.src.CwdPrompt` | 1 | 18 | 4 | 22 | `tui/src/cwd_prompt.rs` | `tui/src/CwdPrompt.kt` |
| 244 | `tui.external_editor` | `tui.src.ExternalEditor` | 1 | 9 | 2 | 11 | `tui/src/external_editor.rs` | `tui/src/ExternalEditor.kt` |
| 245 | `core.event_mapping` | `core.src.EventMapping` | 1 | 7 | 0 | 7 | `core/src/event_mapping.rs` | `core/src/EventMapping.kt` |
| 246 | `context_manager.normalize` | `core.src.contextmanager.Normalize` | 1 | 5 | 0 | 5 | `core/src/context_manager/normalize.rs` | `core/src/contextmanager/Normalize.kt` |
| 247 | `tui.get_git_diff` | `tui.src.GetGitDiff` | 1 | 4 | 0 | 4 | `tui/src/get_git_diff.rs` | `tui/src/GetGitDiff.kt` |
| 248 | `exec-server.sandboxed_file_system` | `execserver.src.SandboxedFileSystem` | 1 | 11 | 1 | 12 | `exec-server/src/sandboxed_file_system.rs` | `execserver/src/SandboxedFileSystem.kt` |
| 249 | `server.file_system_handler` | `execserver.src.server.FileSystemHandler` | 1 | 10 | 1 | 11 | `exec-server/src/server/file_system_handler.rs` | `execserver/src/server/FileSystemHandler.kt` |
| 250 | `server.process_handler` | `execserver.src.server.ProcessHandler` | 1 | 7 | 1 | 8 | `exec-server/src/server/process_handler.rs` | `execserver/src/server/ProcessHandler.kt` |
| 251 | `debug_sandbox.pid_tracker` | `cli.src.debugsandbox.PidTracker` | 1 | 14 | 2 | 16 | `cli/src/debug_sandbox/pid_tracker.rs` | `cli/src/debugsandbox/PidTracker.kt` |
| 252 | `local.create_thread` | `threadstore.src.local.CreateThread` | 1 | 2 | 0 | 2 | `thread-store/src/local/create_thread.rs` | `threadstore/src/local/CreateThread.kt` |
| 253 | `state.runtime` | `state.src.runtime.Runtime` | 1 | 14 | 1 | 15 | `state/src/runtime.rs` | `state/src/runtime/Runtime.kt` |
| 254 | `tests.websocket` | `execserver.tests.Websocket` | 1 | 1 | 0 | 1 | `exec-server/tests/websocket.rs` | `execserver/tests/Websocket.kt` |
| 255 | `tui.keymap_setup` | `tui.src.keymapsetup.KeymapSetup` | 1 | 79 | 2 | 81 | `tui/src/keymap_setup.rs` | `tui/src/keymapsetup/KeymapSetup.kt` |
| 256 | `keymap_setup.actions` | `tui.src.keymapsetup.Actions` | 1 | 11 | 5 | 16 | `tui/src/keymap_setup/actions.rs` | `tui/src/keymapsetup/Actions.kt` |
| 257 | `chatgpt.get_task` | `chatgpt.src.GetTask` | 1 | 1 | 5 | 6 | `chatgpt/src/get_task.rs` | `chatgpt/src/GetTask.kt` |
| 258 | `tests.event_processor_with_json_output` | `exec.tests.EventProcessorWithJsonOutput` | 1 | 30 | 0 | 30 | `exec/tests/event_processor_with_json_output.rs` | `exec/tests/EventProcessorWithJsonOutput.kt` |
| 259 | `chatgpt.apply_command` | `chatgpt.src.ApplyCommand` | 1 | 3 | 1 | 4 | `chatgpt/src/apply_command.rs` | `chatgpt/src/ApplyCommand.kt` |
| 260 | `unix.socket` | `shellescalation.src.unix.Socket` | 1 | 36 | 3 | 39 | `shell-escalation/src/unix/socket.rs` | `shellescalation/src/unix/Socket.kt` |
| 261 | `execpolicy-legacy.execv_checker` | `execpolicylegacy.src.ExecvChecker` | 1 | 7 | 1 | 8 | `execpolicy-legacy/src/execv_checker.rs` | `execpolicylegacy/src/ExecvChecker.kt` |
| 262 | `tui.main` | `tui.src.Main` | 1 | 2 | 1 | 3 | `tui/src/main.rs` | `tui/src/Main.kt` |
| 263 | `tui.markdown` | `tui.src.Markdown` | 1 | 7 | 0 | 7 | `tui/src/markdown.rs` | `tui/src/Markdown.kt` |
| 264 | `rollout-trace.payload` | `rollouttrace.src.Payload` | 1 | 0 | 3 | 3 | `rollout-trace/src/payload.rs` | `rollouttrace/src/Payload.kt` |
| 265 | `models.external_pull_request_response` | `codexbackendopenapimodels.src.models.ExternalPullRequestResponse` | 1 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/external_pull_request_response.rs` | `codexbackendopenapimodels/src/models/ExternalPullRequestResponse.kt` |
| 266 | `models.git_pull_request` | `codexbackendopenapimodels.src.models.GitPullRequest` | 1 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/git_pull_request.rs` | `codexbackendopenapimodels/src/models/GitPullRequest.kt` |
| 267 | `tui.multi_agents` | `tui.src.MultiAgents` | 1 | 43 | 4 | 47 | `tui/src/multi_agents.rs` | `tui/src/MultiAgents.kt` |
| 268 | `rollout-trace.compaction` | `rollouttrace.src.Compaction` | 1 | 12 | 8 | 20 | `rollout-trace/src/compaction.rs` | `rollouttrace/src/Compaction.kt` |
| 269 | `tui.npm_registry` | `tui.src.NpmRegistry` | 1 | 7 | 3 | 10 | `tui/src/npm_registry.rs` | `tui/src/NpmRegistry.kt` |
| 270 | `rollout-trace.code_cell` | `rollouttrace.src.CodeCell` | 1 | 9 | 4 | 13 | `rollout-trace/src/code_cell.rs` | `rollouttrace/src/CodeCell.kt` |
| 271 | `render.highlight` | `tui.src.render.Highlight` | 1 | 80 | 2 | 82 | `tui/src/render/highlight.rs` | `tui/src/render/Highlight.kt` |
| 272 | `models.task_response` | `codexbackendopenapimodels.src.models.TaskResponse` | 1 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/task_response.rs` | `codexbackendopenapimodels/src/models/TaskResponse.kt` |
| 273 | `rmcp-client.program_resolver` | `rmcpclient.src.ProgramResolver` | 1 | 11 | 1 | 12 | `rmcp-client/src/program_resolver.rs` | `rmcpclient/src/ProgramResolver.kt` |
| 274 | `tui.session_log` | `tui.src.SessionLog` | 1 | 10 | 1 | 11 | `tui/src/session_log.rs` | `tui/src/SessionLog.kt` |
| 275 | `hooks.config_rules` | `hooks.src.ConfigRules` | 1 | 5 | 0 | 5 | `hooks/src/config_rules.rs` | `hooks/src/ConfigRules.kt` |
| 276 | `remote_control.client_tracker` | `appservertransport.src.transport.remotecontrol.ClientTracker` | 1 | 17 | 3 | 20 | `app-server-transport/src/transport/remote_control/client_tracker.rs` | `appservertransport/src/transport/remotecontrol/ClientTracker.kt` |
| 277 | `rmcp-client.logging_client_handler` | `rmcpclient.src.LoggingClientHandler` | 1 | 10 | 1 | 11 | `rmcp-client/src/logging_client_handler.rs` | `rmcpclient/src/LoggingClientHandler.kt` |
| 278 | `rmcp-client.executor_process_transport` | `rmcpclient.src.ExecutorProcessTransport` | 1 | 16 | 2 | 18 | `rmcp-client/src/executor_process_transport.rs` | `rmcpclient/src/ExecutorProcessTransport.kt` |
| 279 | `tui.status_indicator_widget` | `tui.src.StatusIndicatorWidget` | 1 | 27 | 2 | 29 | `tui/src/status_indicator_widget.rs` | `tui/src/StatusIndicatorWidget.kt` |
| 280 | `tui.test_support` | `tui.src.TestSupport` | 1 | 5 | 0 | 5 | `tui/src/test_support.rs` | `tui/src/TestSupport.kt` |
| 281 | `tui.text_formatting` | `tui.src.TextFormatting` | 1 | 31 | 1 | 32 | `tui/src/text_formatting.rs` | `tui/src/TextFormatting.kt` |
| 282 | `hooks.registry` | `hooks.src.Registry` | 1 | 19 | 3 | 22 | `hooks/src/registry.rs` | `hooks/src/Registry.kt` |
| 283 | `tui.tooltips` | `tui.src.Tooltips` | 1 | 26 | 4 | 30 | `tui/src/tooltips.rs` | `tui/src/Tooltips.kt` |
| 284 | `tui.event_stream` | `tui.src.tui.EventStream` | 1 | 26 | 10 | 36 | `tui/src/tui/event_stream.rs` | `tui/src/tui/EventStream.kt` |
| 285 | `tui.updates` | `tui.src.Updates` | 1 | 7 | 3 | 10 | `tui/src/updates.rs` | `tui/src/Updates.kt` |
| 286 | `rmcp-client.elicitation_client_service` | `rmcpclient.src.ElicitationClientService` | 1 | 12 | 2 | 14 | `rmcp-client/src/elicitation_client_service.rs` | `rmcpclient/src/ElicitationClientService.kt` |
| 287 | `auth.default_client` | `login.src.auth.DefaultClient` | 1 | 13 | 2 | 15 | `login/src/auth/default_client.rs` | `login/src/auth/DefaultClient.kt` |
| 288 | `core.apply_patch` | `core.src.ApplyPatch` | 1 | 2 | 2 | 4 | `core/src/apply_patch.rs` | `core/src/ApplyPatch.kt` |
| 289 | `v2.plan_item` | `appserver.tests.suite.v2.PlanItem` | 1 | 6 | 0 | 6 | `app-server/tests/suite/v2/plan_item.rs` | `appserver/tests/suite/v2/PlanItem.kt` |
| 290 | `tui.workspace_command` | `tui.src.WorkspaceCommand` | 1 | 8 | 6 | 14 | `tui/src/workspace_command.rs` | `tui/src/WorkspaceCommand.kt` |
| 291 | `network-proxy.socks5` | `networkproxy.src.Socks5` | 1 | 13 | 1 | 14 | `network-proxy/src/socks5.rs` | `networkproxy/src/Socks5.kt` |
| 292 | `suite.logout` | `login.tests.suite.Logout` | 1 | 8 | 1 | 9 | `login/tests/suite/logout.rs` | `login/tests/suite/Logout.kt` |
| 293 | `core-skills.system` | `coreskills.src.System` | 1 | 1 | 0 | 1 | `core-skills/src/system.rs` | `coreskills/src/System.kt` |
| 294 | `core-skills.remote` | `coreskills.src.Remote` | 1 | 9 | 6 | 15 | `core-skills/src/remote.rs` | `coreskills/src/Remote.kt` |
| 295 | `pty.pipe` | `utils.pty.src.Pipe` | 1 | 7 | 2 | 9 | `utils/pty/src/pipe.rs` | `utils/pty/src/Pipe.kt` |
| 296 | `network-proxy.mitm` | `networkproxy.src.Mitm` | 1 | 19 | 9 | 28 | `network-proxy/src/mitm.rs` | `networkproxy/src/Mitm.kt` |
| 297 | `stream-parser.tagged_line_parser` | `utils.streamparser.src.TaggedLineParser` | 1 | 12 | 4 | 16 | `utils/stream-parser/src/tagged_line_parser.rs` | `utils/streamparser/src/TaggedLineParser.kt` |
| 298 | `windows-sandbox-rs.token` | `windowssandboxrs.src.Token` | 1 | 15 | 2 | 17 | `windows-sandbox-rs/src/token.rs` | `windowssandboxrs/src/Token.kt` |
| 299 | `core-skills.loader` | `coreskills.src.Loader` | 1 | 30 | 10 | 40 | `core-skills/src/loader.rs` | `coreskills/src/Loader.kt` |
| 300 | `write.phase1` | `memories.write.src.Phase1` | 1 | 23 | 4 | 27 | `memories/write/src/phase1.rs` | `memories/write/src/Phase1.kt` |
| 301 | `windows-sandbox-rs.dpapi` | `windowssandboxrs.src.Dpapi` | 1 | 3 | 0 | 3 | `windows-sandbox-rs/src/dpapi.rs` | `windowssandboxrs/src/Dpapi.kt` |
| 302 | `config.host_name` | `config.src.HostName` | 1 | 10 | 0 | 10 | `config/src/host_name.rs` | `config/src/HostName.kt` |
| 303 | `models-manager.collaboration_mode_presets` | `modelsmanager.src.CollaborationModePresets` | 1 | 5 | 0 | 5 | `models-manager/src/collaboration_mode_presets.rs` | `modelsmanager/src/CollaborationModePresets.kt` |
| 304 | `network-proxy.http_proxy` | `networkproxy.src.HttpProxy` | 1 | 30 | 1 | 31 | `network-proxy/src/http_proxy.rs` | `networkproxy/src/HttpProxy.kt` |
| 305 | `suite.unified_exec` | `core.tests.suite.UnifiedExec` | 1 | 41 | 1 | 42 | `core/tests/suite/unified_exec.rs` | `core/tests/suite/UnifiedExec.kt` |
| 306 | `exec-server.fs_helper` | `execserver.src.FsHelper` | 0 | 12 | 3 | 15 | `exec-server/src/fs_helper.rs` | `execserver/src/FsHelper.kt` |
| 307 | `windows-sandbox-rs.workspace_acl` | `windowssandboxrs.src.WorkspaceAcl` | 0 | 4 | 0 | 4 | `windows-sandbox-rs/src/workspace_acl.rs` | `windowssandboxrs/src/WorkspaceAcl.kt` |
| 308 | `config.mcp_edit_tests` | `config.src.McpEditTests` | 0 | 1 | 0 | 1 | `config/src/mcp_edit_tests.rs` | `config/src/McpEditTests.kt` |
| 309 | `config.mcp_types` | `config.src.McpTypes` | 0 | 14 | 8 | 22 | `config/src/mcp_types.rs` | `config/src/McpTypes.kt` |
| 310 | `config.mcp_types_tests` | `config.src.McpTypesTests` | 0 | 22 | 0 | 22 | `config/src/mcp_types_tests.rs` | `config/src/McpTypesTests.kt` |
| 311 | `config.merge` | `config.src.Merge` | 0 | 2 | 0 | 2 | `config/src/merge.rs` | `config/src/Merge.kt` |
| 312 | `config.merge_tests` | `config.src.MergeTests` | 0 | 4 | 0 | 4 | `config/src/merge_tests.rs` | `config/src/MergeTests.kt` |
| 313 | `config.overrides` | `config.src.Overrides` | 0 | 3 | 0 | 3 | `config/src/overrides.rs` | `config/src/Overrides.kt` |
| 314 | `config.marketplace_edit` | `config.src.MarketplaceEdit` | 0 | 12 | 2 | 14 | `config/src/marketplace_edit.rs` | `config/src/MarketplaceEdit.kt` |
| 315 | `config.plugin_edit` | `config.src.PluginEdit` | 0 | 19 | 1 | 20 | `config/src/plugin_edit.rs` | `config/src/PluginEdit.kt` |
| 316 | `config.profile_toml` | `config.src.ProfileToml` | 0 | 1 | 1 | 2 | `config/src/profile_toml.rs` | `config/src/ProfileToml.kt` |
| 317 | `config.project_root_markers` | `config.src.ProjectRootMarkers` | 0 | 2 | 0 | 2 | `config/src/project_root_markers.rs` | `config/src/ProjectRootMarkers.kt` |
| 318 | `loader.macos` | `config.src.loader.Macos` | 0 | 9 | 1 | 10 | `config/src/loader/macos.rs` | `config/src/loader/Macos.kt` |
| 319 | `config.schema` | `config.src.Schema` | 0 | 6 | 0 | 6 | `config/src/schema.rs` | `config/src/Schema.kt` |
| 320 | `loader.layer_io` | `config.src.loader.LayerIo` | 0 | 4 | 3 | 7 | `config/src/loader/layer_io.rs` | `config/src/loader/LayerIo.kt` |
| 321 | `config.key_aliases` | `config.src.KeyAliases` | 0 | 2 | 1 | 3 | `config/src/key_aliases.rs` | `config/src/KeyAliases.kt` |
| 322 | `config.state_tests` | `config.src.StateTests` | 0 | 1 | 0 | 1 | `config/src/state_tests.rs` | `config/src/StateTests.kt` |
| 323 | `config.thread_config` | `config.src.threadconfig.ThreadConfig` | 0 | 13 | 9 | 22 | `config/src/thread_config.rs` | `config/src/threadconfig/ThreadConfig.kt` |
| 324 | `proto.codex.thread_config.v1` | `config.src.threadconfig.proto.Codex.threadConfig.v1` | 0 | 22 | 17 | 39 | `config/src/thread_config/proto/codex.thread_config.v1.rs` | `config/src/threadconfig/proto/Codex.threadConfig.v1.kt` |
| 325 | `thread_config.remote` | `config.src.threadconfig.Remote` | 0 | 22 | 2 | 24 | `config/src/thread_config/remote.rs` | `config/src/threadconfig/Remote.kt` |
| 326 | `config.hooks_tests` | `config.src.HooksTests` | 0 | 4 | 0 | 4 | `config/src/hooks_tests.rs` | `config/src/HooksTests.kt` |
| 327 | `config.types` | `config.src.Types` | 0 | 15 | 39 | 54 | `config/src/types.rs` | `config/src/Types.kt` |
| 328 | `config.types_tests` | `config.src.TypesTests` | 0 | 4 | 0 | 4 | `config/src/types_tests.rs` | `config/src/TypesTests.kt` |
| 329 | `connectors.accessible` | `connectors.src.Accessible` | 0 | 1 | 1 | 2 | `connectors/src/accessible.rs` | `connectors/src/Accessible.kt` |
| 330 | `connectors.filter` | `connectors.src.Filter` | 0 | 4 | 0 | 4 | `connectors/src/filter.rs` | `connectors/src/Filter.kt` |
| 331 | `connectors.merge` | `connectors.src.Merge` | 0 | 4 | 0 | 4 | `connectors/src/merge.rs` | `connectors/src/Merge.kt` |
| 332 | `connectors.metadata` | `connectors.src.Metadata` | 0 | 5 | 0 | 5 | `connectors/src/metadata.rs` | `connectors/src/Metadata.kt` |
| 333 | `core-plugins.installed_marketplaces` | `coreplugins.src.InstalledMarketplaces` | 0 | 3 | 0 | 3 | `core-plugins/src/installed_marketplaces.rs` | `coreplugins/src/InstalledMarketplaces.kt` |
| 334 | `core-plugins.loader_tests` | `coreplugins.src.LoaderTests` | 0 | 18 | 0 | 18 | `core-plugins/src/loader_tests.rs` | `coreplugins/src/LoaderTests.kt` |
| 335 | `core-plugins.manager` | `coreplugins.src.Manager` | 0 | 55 | 25 | 80 | `core-plugins/src/manager.rs` | `coreplugins/src/Manager.kt` |
| 336 | `core-plugins.manager_tests` | `coreplugins.src.ManagerTests` | 0 | 64 | 0 | 64 | `core-plugins/src/manager_tests.rs` | `coreplugins/src/ManagerTests.kt` |
| 337 | `core-plugins.manifest` | `coreplugins.src.Manifest` | 0 | 16 | 9 | 25 | `core-plugins/src/manifest.rs` | `coreplugins/src/Manifest.kt` |
| 338 | `core-plugins.marketplace` | `coreplugins.src.Marketplace` | 0 | 29 | 17 | 46 | `core-plugins/src/marketplace.rs` | `coreplugins/src/Marketplace.kt` |
| 339 | `core-plugins.marketplace_add` | `coreplugins.src.marketplaceadd.MarketplaceAdd` | 0 | 10 | 3 | 13 | `core-plugins/src/marketplace_add.rs` | `coreplugins/src/marketplaceadd/MarketplaceAdd.kt` |
| 340 | `marketplace_add.install` | `coreplugins.src.marketplaceadd.Install` | 0 | 6 | 0 | 6 | `core-plugins/src/marketplace_add/install.rs` | `coreplugins/src/marketplaceadd/Install.kt` |
| 341 | `marketplace_add.metadata` | `coreplugins.src.marketplaceadd.Metadata` | 0 | 16 | 2 | 18 | `core-plugins/src/marketplace_add/metadata.rs` | `coreplugins/src/marketplaceadd/Metadata.kt` |
| 342 | `marketplace_add.source` | `coreplugins.src.marketplaceadd.Source` | 0 | 28 | 1 | 29 | `core-plugins/src/marketplace_add/source.rs` | `coreplugins/src/marketplaceadd/Source.kt` |
| 343 | `core-plugins.marketplace_remove` | `coreplugins.src.MarketplaceRemove` | 0 | 9 | 3 | 12 | `core-plugins/src/marketplace_remove.rs` | `coreplugins/src/MarketplaceRemove.kt` |
| 344 | `core-plugins.marketplace_tests` | `coreplugins.src.MarketplaceTests` | 0 | 29 | 0 | 29 | `core-plugins/src/marketplace_tests.rs` | `coreplugins/src/MarketplaceTests.kt` |
| 345 | `core-plugins.marketplace_upgrade` | `coreplugins.src.marketplaceupgrade.MarketplaceUpgrade` | 0 | 9 | 3 | 12 | `core-plugins/src/marketplace_upgrade.rs` | `coreplugins/src/marketplaceupgrade/MarketplaceUpgrade.kt` |
| 346 | `marketplace_upgrade.activation` | `coreplugins.src.marketplaceupgrade.Activation` | 0 | 5 | 1 | 6 | `core-plugins/src/marketplace_upgrade/activation.rs` | `coreplugins/src/marketplaceupgrade/Activation.kt` |
| 347 | `marketplace_upgrade.git` | `coreplugins.src.marketplaceupgrade.Git` | 0 | 16 | 0 | 16 | `core-plugins/src/marketplace_upgrade/git.rs` | `coreplugins/src/marketplaceupgrade/Git.kt` |
| 348 | `core-plugins.remote` | `coreplugins.src.remote.Remote` | 0 | 34 | 21 | 55 | `core-plugins/src/remote.rs` | `coreplugins/src/remote/Remote.kt` |
| 349 | `remote.remote_installed_plugin_sync` | `coreplugins.src.remote.RemoteInstalledPluginSync` | 0 | 12 | 5 | 17 | `core-plugins/src/remote/remote_installed_plugin_sync.rs` | `coreplugins/src/remote/RemoteInstalledPluginSync.kt` |
| 350 | `remote.share` | `coreplugins.src.remote.share.Share` | 0 | 19 | 7 | 26 | `core-plugins/src/remote/share.rs` | `coreplugins/src/remote/share/Share.kt` |
| 351 | `share.local_paths` | `coreplugins.src.remote.share.LocalPaths` | 0 | 9 | 1 | 10 | `core-plugins/src/remote/share/local_paths.rs` | `coreplugins/src/remote/share/LocalPaths.kt` |
| 352 | `share.tests` | `coreplugins.src.remote.share.Tests` | 0 | 17 | 0 | 17 | `core-plugins/src/remote/share/tests.rs` | `coreplugins/src/remote/share/Tests.kt` |
| 353 | `core-plugins.remote_bundle` | `coreplugins.src.RemoteBundle` | 0 | 36 | 2 | 38 | `core-plugins/src/remote_bundle.rs` | `coreplugins/src/RemoteBundle.kt` |
| 354 | `core-plugins.remote_legacy` | `coreplugins.src.RemoteLegacy` | 0 | 8 | 4 | 12 | `core-plugins/src/remote_legacy.rs` | `coreplugins/src/RemoteLegacy.kt` |
| 355 | `core-plugins.startup_remote_sync` | `coreplugins.src.StartupRemoteSync` | 0 | 4 | 0 | 4 | `core-plugins/src/startup_remote_sync.rs` | `coreplugins/src/StartupRemoteSync.kt` |
| 356 | `core-plugins.startup_remote_sync_tests` | `coreplugins.src.StartupRemoteSyncTests` | 0 | 1 | 0 | 1 | `core-plugins/src/startup_remote_sync_tests.rs` | `coreplugins/src/StartupRemoteSyncTests.kt` |
| 357 | `core-plugins.startup_sync` | `coreplugins.src.StartupSync` | 0 | 37 | 4 | 41 | `core-plugins/src/startup_sync.rs` | `coreplugins/src/StartupSync.kt` |
| 358 | `core-plugins.startup_sync_tests` | `coreplugins.src.StartupSyncTests` | 0 | 30 | 0 | 30 | `core-plugins/src/startup_sync_tests.rs` | `coreplugins/src/StartupSyncTests.kt` |
| 359 | `core-plugins.store` | `coreplugins.src.Store` | 0 | 21 | 4 | 25 | `core-plugins/src/store.rs` | `coreplugins/src/Store.kt` |
| 360 | `core-plugins.store_tests` | `coreplugins.src.StoreTests` | 0 | 17 | 0 | 17 | `core-plugins/src/store_tests.rs` | `coreplugins/src/StoreTests.kt` |
| 361 | `core-plugins.test_support` | `coreplugins.src.TestSupport` | 0 | 7 | 0 | 7 | `core-plugins/src/test_support.rs` | `coreplugins/src/TestSupport.kt` |
| 362 | `core-plugins.toggles` | `coreplugins.src.Toggles` | 0 | 3 | 0 | 3 | `core-plugins/src/toggles.rs` | `coreplugins/src/Toggles.kt` |
| 363 | `core-skills.config_rules` | `coreskills.src.ConfigRules` | 0 | 3 | 3 | 6 | `core-skills/src/config_rules.rs` | `coreskills/src/ConfigRules.kt` |
| 364 | `core-skills.env_var_dependencies` | `coreskills.src.EnvVarDependencies` | 0 | 1 | 1 | 2 | `core-skills/src/env_var_dependencies.rs` | `coreskills/src/EnvVarDependencies.kt` |
| 365 | `config.hook_config` | `config.src.HookConfig` | 0 | 6 | 7 | 13 | `config/src/hook_config.rs` | `config/src/HookConfig.kt` |
| 366 | `core-skills.injection_tests` | `coreskills.src.InjectionTests` | 0 | 27 | 0 | 27 | `core-skills/src/injection_tests.rs` | `coreskills/src/InjectionTests.kt` |
| 367 | `core-skills.invocation_utils` | `coreskills.src.InvocationUtils` | 0 | 9 | 0 | 9 | `core-skills/src/invocation_utils.rs` | `coreskills/src/InvocationUtils.kt` |
| 368 | `core-skills.invocation_utils_tests` | `coreskills.src.InvocationUtilsTests` | 0 | 7 | 0 | 7 | `core-skills/src/invocation_utils_tests.rs` | `coreskills/src/InvocationUtilsTests.kt` |
| 369 | `config.fingerprint` | `config.src.Fingerprint` | 0 | 3 | 0 | 3 | `config/src/fingerprint.rs` | `config/src/Fingerprint.kt` |
| 370 | `core-skills.loader_tests` | `coreskills.src.LoaderTests` | 0 | 53 | 1 | 54 | `core-skills/src/loader_tests.rs` | `coreskills/src/LoaderTests.kt` |
| 371 | `core-skills.manager` | `coreskills.src.Manager` | 0 | 15 | 3 | 18 | `core-skills/src/manager.rs` | `coreskills/src/Manager.kt` |
| 372 | `core-skills.manager_tests` | `coreskills.src.ManagerTests` | 0 | 24 | 0 | 24 | `core-skills/src/manager_tests.rs` | `coreskills/src/ManagerTests.kt` |
| 373 | `core-skills.mention_counts` | `coreskills.src.MentionCounts` | 0 | 1 | 0 | 1 | `core-skills/src/mention_counts.rs` | `coreskills/src/MentionCounts.kt` |
| 374 | `config.diagnostics` | `config.src.Diagnostics` | 0 | 24 | 5 | 29 | `config/src/diagnostics.rs` | `config/src/Diagnostics.kt` |
| 375 | `config.cloud_requirements` | `config.src.CloudRequirements` | 0 | 8 | 3 | 11 | `config/src/cloud_requirements.rs` | `config/src/CloudRequirements.kt` |
| 376 | `core-skills.render` | `coreskills.src.Render` | 0 | 70 | 9 | 79 | `core-skills/src/render.rs` | `coreskills/src/Render.kt` |
| 377 | `examples.generate-proto` | `config.examples.Generate-proto` | 0 | 1 | 0 | 1 | `config/examples/generate-proto.rs` | `config/examples/Generate-proto.kt` |
| 378 | `agent.agent_resolver` | `core.src.agent.AgentResolver` | 0 | 2 | 0 | 2 | `core/src/agent/agent_resolver.rs` | `core/src/agent/AgentResolver.kt` |
| 379 | `agent.control` | `core.src.agent.Control` | 0 | 40 | 5 | 45 | `core/src/agent/control.rs` | `core/src/agent/Control.kt` |
| 380 | `agent.control_tests` | `core.src.agent.ControlTests` | 0 | 55 | 1 | 56 | `core/src/agent/control_tests.rs` | `core/src/agent/ControlTests.kt` |
| 381 | `codex-mcp.runtime` | `codexmcp.src.Runtime` | 0 | 4 | 2 | 6 | `codex-mcp/src/runtime.rs` | `codexmcp/src/Runtime.kt` |
| 382 | `agent.registry` | `core.src.agent.Registry` | 0 | 20 | 4 | 24 | `core/src/agent/registry.rs` | `core/src/agent/Registry.kt` |
| 383 | `agent.registry_tests` | `core.src.agent.RegistryTests` | 0 | 17 | 0 | 17 | `core/src/agent/registry_tests.rs` | `core/src/agent/RegistryTests.kt` |
| 384 | `agent.role` | `core.src.agent.Role` | 0 | 18 | 0 | 18 | `core/src/agent/role.rs` | `core/src/agent/Role.kt` |
| 385 | `agent.role_tests` | `core.src.agent.RoleTests` | 0 | 24 | 0 | 24 | `core/src/agent/role_tests.rs` | `core/src/agent/RoleTests.kt` |
| 386 | `mcp.mod_tests` | `codexmcp.src.mcp.ModTests` | 0 | 11 | 0 | 11 | `codex-mcp/src/mcp/mod_tests.rs` | `codexmcp/src/mcp/ModTests.kt` |
| 387 | `core.agents_md` | `core.src.AgentsMd` | 0 | 8 | 2 | 10 | `core/src/agents_md.rs` | `core/src/AgentsMd.kt` |
| 388 | `core.agents_md_tests` | `core.src.AgentsMdTests` | 0 | 27 | 0 | 27 | `core/src/agents_md_tests.rs` | `core/src/AgentsMdTests.kt` |
| 389 | `mcp.auth` | `codexmcp.src.mcp.Auth` | 0 | 12 | 5 | 17 | `codex-mcp/src/mcp/auth.rs` | `codexmcp/src/mcp/Auth.kt` |
| 390 | `core.apply_patch_tests` | `core.src.ApplyPatchTests` | 0 | 1 | 0 | 1 | `core/src/apply_patch_tests.rs` | `core/src/ApplyPatchTests.kt` |
| 391 | `codex-mcp.connection_manager_tests` | `codexmcp.src.ConnectionManagerTests` | 0 | 40 | 0 | 40 | `codex-mcp/src/connection_manager_tests.rs` | `codexmcp/src/ConnectionManagerTests.kt` |
| 392 | `core.arc_monitor` | `core.src.ArcMonitor` | 0 | 7 | 9 | 16 | `core/src/arc_monitor.rs` | `core/src/ArcMonitor.kt` |
| 393 | `core.arc_monitor_tests` | `core.src.ArcMonitorTests` | 0 | 6 | 1 | 7 | `core/src/arc_monitor_tests.rs` | `core/src/ArcMonitorTests.kt` |
| 394 | `bin.config_schema` | `core.src.bin.ConfigSchema` | 0 | 1 | 1 | 2 | `core/src/bin/config_schema.rs` | `core/src/bin/ConfigSchema.kt` |
| 395 | `codex-mcp.connection_manager` | `codexmcp.src.ConnectionManager` | 0 | 29 | 1 | 30 | `codex-mcp/src/connection_manager.rs` | `codexmcp/src/ConnectionManager.kt` |
| 396 | `core.client_common_tests` | `core.src.ClientCommonTests` | 0 | 6 | 0 | 6 | `core/src/client_common_tests.rs` | `core/src/ClientCommonTests.kt` |
| 397 | `core.client_tests` | `core.src.ClientTests` | 0 | 18 | 4 | 22 | `core/src/client_tests.rs` | `core/src/ClientTests.kt` |
| 398 | `core.codex_delegate` | `core.src.CodexDelegate` | 0 | 14 | 0 | 14 | `core/src/codex_delegate.rs` | `core/src/CodexDelegate.kt` |
| 399 | `core.codex_delegate_tests` | `core.src.CodexDelegateTests` | 0 | 6 | 0 | 6 | `core/src/codex_delegate_tests.rs` | `core/src/CodexDelegateTests.kt` |
| 400 | `codex-mcp.codex_apps` | `codexmcp.src.CodexApps` | 0 | 13 | 4 | 17 | `codex-mcp/src/codex_apps.rs` | `codexmcp/src/CodexApps.kt` |
| 401 | `core.command_canonicalization` | `core.src.CommandCanonicalization` | 0 | 1 | 0 | 1 | `core/src/command_canonicalization.rs` | `core/src/CommandCanonicalization.kt` |
| 402 | `core.command_canonicalization_tests` | `core.src.CommandCanonicalizationTests` | 0 | 4 | 0 | 4 | `core/src/command_canonicalization_tests.rs` | `core/src/CommandCanonicalizationTests.kt` |
| 403 | `core.commit_attribution` | `core.src.CommitAttribution` | 0 | 3 | 0 | 3 | `core/src/commit_attribution.rs` | `core/src/CommitAttribution.kt` |
| 404 | `core.commit_attribution_tests` | `core.src.CommitAttributionTests` | 0 | 4 | 0 | 4 | `core/src/commit_attribution_tests.rs` | `core/src/CommitAttributionTests.kt` |
| 405 | `core.compact_remote` | `core.src.CompactRemote` | 0 | 9 | 1 | 10 | `core/src/compact_remote.rs` | `core/src/CompactRemote.kt` |
| 406 | `core.compact_remote_v2` | `core.src.CompactRemoteV2` | 0 | 12 | 0 | 12 | `core/src/compact_remote_v2.rs` | `core/src/CompactRemoteV2.kt` |
| 407 | `core.compact_tests` | `core.src.CompactTests` | 0 | 15 | 0 | 15 | `core/src/compact_tests.rs` | `core/src/CompactTests.kt` |
| 408 | `config.agent_roles` | `core.src.config.AgentRoles` | 0 | 16 | 2 | 18 | `core/src/config/agent_roles.rs` | `core/src/config/AgentRoles.kt` |
| 409 | `config.config_loader_tests` | `core.src.config.ConfigLoaderTests` | 0 | 56 | 0 | 56 | `core/src/config/config_loader_tests.rs` | `core/src/config/ConfigLoaderTests.kt` |
| 410 | `config.config_tests` | `core.src.config.ConfigTests` | 0 | 235 | 3 | 238 | `core/src/config/config_tests.rs` | `core/src/config/ConfigTests.kt` |
| 411 | `config.edit` | `core.src.config.Edit` | 0 | 69 | 6 | 75 | `core/src/config/edit.rs` | `core/src/config/Edit.kt` |
| 412 | `config.edit_tests` | `core.src.config.EditTests` | 0 | 41 | 0 | 41 | `core/src/config/edit_tests.rs` | `core/src/config/EditTests.kt` |
| 413 | `tests.ca_env` | `codexclient.tests.CaEnv` | 0 | 26 | 4 | 30 | `codex-client/tests/ca_env.rs` | `codexclient/tests/CaEnv.kt` |
| 414 | `codex-client.transport` | `codexclient.src.Transport` | 0 | 6 | 4 | 10 | `codex-client/src/transport.rs` | `codexclient/src/Transport.kt` |
| 415 | `config.network_proxy_spec_tests` | `core.src.config.NetworkProxySpecTests` | 0 | 16 | 0 | 16 | `core/src/config/network_proxy_spec_tests.rs` | `core/src/config/NetworkProxySpecTests.kt` |
| 416 | `config.permissions` | `core.src.config.Permissions` | 0 | 37 | 0 | 37 | `core/src/config/permissions.rs` | `core/src/config/Permissions.kt` |
| 417 | `config.permissions_tests` | `core.src.config.PermissionsTests` | 0 | 13 | 0 | 13 | `core/src/config/permissions_tests.rs` | `core/src/config/PermissionsTests.kt` |
| 418 | `core.config.schema` | `core.src.config.Schema` | 0 | 0 | 0 | 0 | `core/src/config/schema.rs` | `core/src/config/Schema.kt` |
| 419 | `config.schema_tests` | `core.src.config.SchemaTests` | 0 | 3 | 0 | 3 | `core/src/config/schema_tests.rs` | `core/src/config/SchemaTests.kt` |
| 420 | `config.template_interpolation` | `core.src.config.TemplateInterpolation` | 0 | 8 | 0 | 8 | `core/src/config/template_interpolation.rs` | `core/src/config/TemplateInterpolation.kt` |
| 421 | `core.config_lock` | `core.src.ConfigLock` | 0 | 12 | 1 | 13 | `core/src/config_lock.rs` | `core/src/ConfigLock.kt` |
| 422 | `codex-client.retry` | `codexclient.src.Retry` | 0 | 3 | 2 | 5 | `codex-client/src/retry.rs` | `codexclient/src/Retry.kt` |
| 423 | `core.connectors_tests` | `core.src.ConnectorsTests` | 0 | 40 | 0 | 40 | `core/src/connectors_tests.rs` | `core/src/ConnectorsTests.kt` |
| 424 | `codex-client.error` | `codexclient.src.Error` | 0 | 0 | 2 | 2 | `codex-client/src/error.rs` | `codexclient/src/Error.kt` |
| 425 | `codex-client.default_client` | `codexclient.src.DefaultClient` | 0 | 18 | 4 | 22 | `codex-client/src/default_client.rs` | `codexclient/src/DefaultClient.kt` |
| 426 | `codex-client.custom_ca` | `codexclient.src.CustomCa` | 0 | 28 | 7 | 35 | `codex-client/src/custom_ca.rs` | `codexclient/src/CustomCa.kt` |
| 427 | `codex-client.chatgpt_hosts` | `codexclient.src.ChatgptHosts` | 0 | 2 | 0 | 2 | `codex-client/src/chatgpt_hosts.rs` | `codexclient/src/ChatgptHosts.kt` |
| 428 | `codex-client.chatgpt_cloudflare_cookies` | `codexclient.src.ChatgptCloudflareCookies` | 0 | 16 | 1 | 17 | `codex-client/src/chatgpt_cloudflare_cookies.rs` | `codexclient/src/ChatgptCloudflareCookies.kt` |
| 429 | `context.contextual_user_message` | `core.src.context.ContextualUserMessage` | 0 | 3 | 0 | 3 | `core/src/context/contextual_user_message.rs` | `core/src/context/ContextualUserMessage.kt` |
| 430 | `context.contextual_user_message_tests` | `core.src.context.ContextualUserMessageTests` | 0 | 5 | 0 | 5 | `core/src/context/contextual_user_message_tests.rs` | `core/src/context/ContextualUserMessageTests.kt` |
| 431 | `context.environment_context_tests` | `core.src.context.EnvironmentContextTests` | 0 | 10 | 0 | 10 | `core/src/context/environment_context_tests.rs` | `core/src/context/EnvironmentContextTests.kt` |
| 432 | `context.fragment` | `core.src.context.Fragment` | 0 | 5 | 3 | 8 | `core/src/context/fragment.rs` | `core/src/context/Fragment.kt` |
| 433 | `bin.custom_ca_probe` | `codexclient.src.bin.CustomCaProbe` | 0 | 4 | 0 | 4 | `codex-client/src/bin/custom_ca_probe.rs` | `codexclient/src/bin/CustomCaProbe.kt` |
| 434 | `tests.sse_end_to_end` | `codexapi.tests.SseEndToEnd` | 0 | 7 | 2 | 9 | `codex-api/tests/sse_end_to_end.rs` | `codexapi/tests/SseEndToEnd.kt` |
| 435 | `tests.realtime_websocket_e2e` | `codexapi.tests.RealtimeWebsocketE2e` | 0 | 8 | 1 | 9 | `codex-api/tests/realtime_websocket_e2e.rs` | `codexapi/tests/RealtimeWebsocketE2e.kt` |
| 436 | `tests.models_integration` | `codexapi.tests.ModelsIntegration` | 0 | 3 | 1 | 4 | `codex-api/tests/models_integration.rs` | `codexapi/tests/ModelsIntegration.kt` |
| 437 | `tests.clients` | `codexapi.tests.Clients` | 0 | 26 | 6 | 32 | `codex-api/tests/clients.rs` | `codexapi/tests/Clients.kt` |
| 438 | `codex-api.telemetry` | `codexapi.src.Telemetry` | 0 | 4 | 3 | 7 | `codex-api/src/telemetry.rs` | `codexapi/src/Telemetry.kt` |
| 439 | `context.permissions_instructions_tests` | `core.src.context.PermissionsInstructionsTests` | 0 | 19 | 0 | 19 | `core/src/context/permissions_instructions_tests.rs` | `core/src/context/PermissionsInstructionsTests.kt` |
| 440 | `requests.responses` | `codexapi.src.requests.Responses` | 0 | 1 | 1 | 2 | `codex-api/src/requests/responses.rs` | `codexapi/src/requests/Responses.kt` |
| 441 | `codex-api.provider` | `codexapi.src.Provider` | 0 | 8 | 2 | 10 | `codex-api/src/provider.rs` | `codexapi/src/Provider.kt` |
| 442 | `codex-api.files` | `codexapi.src.Files` | 0 | 8 | 5 | 13 | `codex-api/src/files.rs` | `codexapi/src/Files.kt` |
| 443 | `endpoint.session` | `codexapi.src.endpoint.Session` | 0 | 7 | 1 | 8 | `codex-api/src/endpoint/session.rs` | `codexapi/src/endpoint/Session.kt` |
| 444 | `endpoint.responses_websocket` | `codexapi.src.endpoint.ResponsesWebsocket` | 0 | 27 | 6 | 33 | `codex-api/src/endpoint/responses_websocket.rs` | `codexapi/src/endpoint/ResponsesWebsocket.kt` |
| 445 | `endpoint.responses` | `codexapi.src.endpoint.Responses` | 0 | 5 | 2 | 7 | `codex-api/src/endpoint/responses.rs` | `codexapi/src/endpoint/Responses.kt` |
| 446 | `realtime_websocket.protocol_v2` | `codexapi.src.endpoint.realtimewebsocket.ProtocolV2` | 0 | 7 | 0 | 7 | `codex-api/src/endpoint/realtime_websocket/protocol_v2.rs` | `codexapi/src/endpoint/realtimewebsocket/ProtocolV2.kt` |
| 447 | `realtime_websocket.protocol_v1` | `codexapi.src.endpoint.realtimewebsocket.ProtocolV1` | 0 | 2 | 0 | 2 | `codex-api/src/endpoint/realtime_websocket/protocol_v1.rs` | `codexapi/src/endpoint/realtimewebsocket/ProtocolV1.kt` |
| 448 | `context.user_shell_command` | `core.src.context.UserShellCommand` | 0 | 2 | 1 | 3 | `core/src/context/user_shell_command.rs` | `core/src/context/UserShellCommand.kt` |
| 449 | `realtime_websocket.protocol_common` | `codexapi.src.endpoint.realtimewebsocket.ProtocolCommon` | 0 | 5 | 0 | 5 | `codex-api/src/endpoint/realtime_websocket/protocol_common.rs` | `codexapi/src/endpoint/realtimewebsocket/ProtocolCommon.kt` |
| 450 | `context_manager.history_tests` | `core.src.contextmanager.HistoryTests` | 0 | 77 | 0 | 77 | `core/src/context_manager/history_tests.rs` | `core/src/contextmanager/HistoryTests.kt` |
| 451 | `realtime_websocket.protocol` | `codexapi.src.endpoint.realtimewebsocket.Protocol` | 0 | 1 | 26 | 27 | `codex-api/src/endpoint/realtime_websocket/protocol.rs` | `codexapi/src/endpoint/realtimewebsocket/Protocol.kt` |
| 452 | `context_manager.updates` | `core.src.contextmanager.Updates` | 0 | 12 | 0 | 12 | `core/src/context_manager/updates.rs` | `core/src/contextmanager/Updates.kt` |
| 453 | `core.environment_selection` | `core.src.EnvironmentSelection` | 0 | 11 | 1 | 12 | `core/src/environment_selection.rs` | `core/src/EnvironmentSelection.kt` |
| 454 | `realtime_websocket.methods_v2` | `codexapi.src.endpoint.realtimewebsocket.MethodsV2` | 0 | 5 | 0 | 5 | `codex-api/src/endpoint/realtime_websocket/methods_v2.rs` | `codexapi/src/endpoint/realtimewebsocket/MethodsV2.kt` |
| 455 | `core.event_mapping_tests` | `core.src.EventMappingTests` | 0 | 14 | 0 | 14 | `core/src/event_mapping_tests.rs` | `core/src/EventMappingTests.kt` |
| 456 | `core.exec_env` | `core.src.ExecEnv` | 0 | 3 | 0 | 3 | `core/src/exec_env.rs` | `core/src/ExecEnv.kt` |
| 457 | `core.exec_env_tests` | `core.src.ExecEnvTests` | 0 | 13 | 0 | 13 | `core/src/exec_env_tests.rs` | `core/src/ExecEnvTests.kt` |
| 458 | `core.exec_policy_tests` | `core.src.ExecPolicyTests` | 0 | 88 | 1 | 89 | `core/src/exec_policy_tests.rs` | `core/src/ExecPolicyTests.kt` |
| 459 | `core.exec_policy_windows_tests` | `core.src.ExecPolicyWindowsTests` | 0 | 5 | 0 | 5 | `core/src/exec_policy_windows_tests.rs` | `core/src/ExecPolicyWindowsTests.kt` |
| 460 | `core.exec_tests` | `core.src.ExecTests` | 0 | 40 | 0 | 40 | `core/src/exec_tests.rs` | `core/src/ExecTests.kt` |
| 461 | `realtime_websocket.methods_v1` | `codexapi.src.endpoint.realtimewebsocket.MethodsV1` | 0 | 4 | 0 | 4 | `codex-api/src/endpoint/realtime_websocket/methods_v1.rs` | `codexapi/src/endpoint/realtimewebsocket/MethodsV1.kt` |
| 462 | `core.file_watcher_tests` | `core.src.FileWatcherTests` | 0 | 21 | 0 | 21 | `core/src/file_watcher_tests.rs` | `core/src/FileWatcherTests.kt` |
| 463 | `core.git_info_tests` | `core.src.GitInfoTests` | 0 | 24 | 0 | 24 | `core/src/git_info_tests.rs` | `core/src/GitInfoTests.kt` |
| 464 | `core.goals` | `core.src.Goals` | 0 | 59 | 9 | 68 | `core/src/goals.rs` | `core/src/Goals.kt` |
| 465 | `realtime_websocket.methods_common` | `codexapi.src.endpoint.realtimewebsocket.MethodsCommon` | 0 | 6 | 0 | 6 | `codex-api/src/endpoint/realtime_websocket/methods_common.rs` | `codexapi/src/endpoint/realtimewebsocket/MethodsCommon.kt` |
| 466 | `realtime_websocket.methods` | `codexapi.src.endpoint.realtimewebsocket.Methods` | 0 | 78 | 7 | 85 | `codex-api/src/endpoint/realtime_websocket/methods.rs` | `codexapi/src/endpoint/realtimewebsocket/Methods.kt` |
| 467 | `guardian.review` | `core.src.guardian.Review` | 0 | 20 | 2 | 22 | `core/src/guardian/review.rs` | `core/src/guardian/Review.kt` |
| 468 | `guardian.review_session` | `core.src.guardian.ReviewSession` | 0 | 52 | 9 | 61 | `core/src/guardian/review_session.rs` | `core/src/guardian/ReviewSession.kt` |
| 469 | `guardian.tests` | `core.src.guardian.Tests` | 0 | 55 | 0 | 55 | `core/src/guardian/tests.rs` | `core/src/guardian/Tests.kt` |
| 470 | `core.hook_runtime` | `core.src.HookRuntime` | 0 | 26 | 4 | 30 | `core/src/hook_runtime.rs` | `core/src/HookRuntime.kt` |
| 471 | `core.installation_id` | `core.src.InstallationId` | 0 | 4 | 0 | 4 | `core/src/installation_id.rs` | `core/src/InstallationId.kt` |
| 472 | `core.mcp` | `core.src.Mcp` | 0 | 4 | 1 | 5 | `core/src/mcp.rs` | `core/src/Mcp.kt` |
| 473 | `core.mcp_openai_file` | `core.src.McpOpenaiFile` | 0 | 8 | 0 | 8 | `core/src/mcp_openai_file.rs` | `core/src/McpOpenaiFile.kt` |
| 474 | `core.mcp_skill_dependencies` | `core.src.McpSkillDependencies` | 0 | 10 | 0 | 10 | `core/src/mcp_skill_dependencies.rs` | `core/src/McpSkillDependencies.kt` |
| 475 | `core.mcp_tool_approval_templates` | `core.src.McpToolApprovalTemplates` | 0 | 11 | 5 | 16 | `core/src/mcp_tool_approval_templates.rs` | `core/src/McpToolApprovalTemplates.kt` |
| 476 | `core.mcp_tool_call` | `core.src.McpToolCall` | 0 | 56 | 8 | 64 | `core/src/mcp_tool_call.rs` | `core/src/McpToolCall.kt` |
| 477 | `core.mcp_tool_call_tests` | `core.src.McpToolCallTests` | 0 | 74 | 0 | 74 | `core/src/mcp_tool_call_tests.rs` | `core/src/McpToolCallTests.kt` |
| 478 | `core.mcp_tool_exposure` | `core.src.McpToolExposure` | 0 | 2 | 1 | 3 | `core/src/mcp_tool_exposure.rs` | `core/src/McpToolExposure.kt` |
| 479 | `core.mcp_tool_exposure_test` | `core.src.McpToolExposureTest` | 0 | 8 | 0 | 8 | `core/src/mcp_tool_exposure_test.rs` | `core/src/McpToolExposureTest.kt` |
| 480 | `core.memory_usage` | `core.src.MemoryUsage` | 0 | 2 | 0 | 2 | `core/src/memory_usage.rs` | `core/src/MemoryUsage.kt` |
| 481 | `endpoint.realtime_call` | `codexapi.src.endpoint.RealtimeCall` | 0 | 25 | 5 | 30 | `codex-api/src/endpoint/realtime_call.rs` | `codexapi/src/endpoint/RealtimeCall.kt` |
| 482 | `core.message_history_tests` | `core.src.MessageHistoryTests` | 0 | 4 | 0 | 4 | `core/src/message_history_tests.rs` | `core/src/MessageHistoryTests.kt` |
| 483 | `endpoint.models` | `codexapi.src.endpoint.Models` | 0 | 13 | 3 | 16 | `codex-api/src/endpoint/models.rs` | `codexapi/src/endpoint/Models.kt` |
| 484 | `core.network_policy_decision_tests` | `core.src.NetworkPolicyDecisionTests` | 0 | 6 | 0 | 6 | `core/src/network_policy_decision_tests.rs` | `core/src/NetworkPolicyDecisionTests.kt` |
| 485 | `core.network_proxy_loader` | `core.src.NetworkProxyLoader` | 0 | 20 | 3 | 23 | `core/src/network_proxy_loader.rs` | `core/src/NetworkProxyLoader.kt` |
| 486 | `core.network_proxy_loader_tests` | `core.src.NetworkProxyLoaderTests` | 0 | 6 | 0 | 6 | `core/src/network_proxy_loader_tests.rs` | `core/src/NetworkProxyLoaderTests.kt` |
| 487 | `core.original_image_detail` | `core.src.OriginalImageDetail` | 0 | 0 | 0 | 0 | `core/src/original_image_detail.rs` | `core/src/OriginalImageDetail.kt` |
| 488 | `core.otel_init` | `core.src.OtelInit` | 0 | 2 | 0 | 2 | `core/src/otel_init.rs` | `core/src/OtelInit.kt` |
| 489 | `core.personality_migration` | `core.src.PersonalityMigration` | 0 | 4 | 1 | 5 | `core/src/personality_migration.rs` | `core/src/PersonalityMigration.kt` |
| 490 | `core.personality_migration_tests` | `core.src.PersonalityMigrationTests` | 0 | 9 | 0 | 9 | `core/src/personality_migration_tests.rs` | `core/src/PersonalityMigrationTests.kt` |
| 491 | `plugins.discoverable` | `core.src.plugins.Discoverable` | 0 | 1 | 0 | 1 | `core/src/plugins/discoverable.rs` | `core/src/plugins/Discoverable.kt` |
| 492 | `plugins.discoverable_tests` | `core.src.plugins.DiscoverableTests` | 0 | 10 | 0 | 10 | `core/src/plugins/discoverable_tests.rs` | `core/src/plugins/DiscoverableTests.kt` |
| 493 | `plugins.injection` | `core.src.plugins.Injection` | 0 | 1 | 0 | 1 | `core/src/plugins/injection.rs` | `core/src/plugins/Injection.kt` |
| 494 | `plugins.mentions` | `core.src.plugins.Mentions` | 0 | 5 | 1 | 6 | `core/src/plugins/mentions.rs` | `core/src/plugins/Mentions.kt` |
| 495 | `plugins.mentions_tests` | `core.src.plugins.MentionsTests` | 0 | 10 | 0 | 10 | `core/src/plugins/mentions_tests.rs` | `core/src/plugins/MentionsTests.kt` |
| 496 | `plugins.render` | `core.src.plugins.Render` | 0 | 2 | 0 | 2 | `core/src/plugins/render.rs` | `core/src/plugins/Render.kt` |
| 497 | `plugins.render_tests` | `core.src.plugins.RenderTests` | 0 | 2 | 0 | 2 | `core/src/plugins/render_tests.rs` | `core/src/plugins/RenderTests.kt` |
| 498 | `plugins.test_support` | `core.src.plugins.TestSupport` | 0 | 7 | 0 | 7 | `core/src/plugins/test_support.rs` | `core/src/plugins/TestSupport.kt` |
| 499 | `core.prompt_debug` | `core.src.PromptDebug` | 0 | 2 | 0 | 2 | `core/src/prompt_debug.rs` | `core/src/PromptDebug.kt` |
| 500 | `core.realtime_context` | `core.src.RealtimeContext` | 0 | 15 | 0 | 15 | `core/src/realtime_context.rs` | `core/src/RealtimeContext.kt` |
| 501 | `core.realtime_context_tests` | `core.src.RealtimeContextTests` | 0 | 14 | 0 | 14 | `core/src/realtime_context_tests.rs` | `core/src/RealtimeContextTests.kt` |
| 502 | `core.realtime_conversation` | `core.src.RealtimeConversation` | 0 | 49 | 13 | 62 | `core/src/realtime_conversation.rs` | `core/src/RealtimeConversation.kt` |
| 503 | `core.realtime_conversation_tests` | `core.src.RealtimeConversationTests` | 0 | 9 | 0 | 9 | `core/src/realtime_conversation_tests.rs` | `core/src/RealtimeConversationTests.kt` |
| 504 | `core.realtime_prompt` | `core.src.RealtimePrompt` | 0 | 6 | 0 | 6 | `core/src/realtime_prompt.rs` | `core/src/RealtimePrompt.kt` |
| 505 | `endpoint.memories` | `codexapi.src.endpoint.Memories` | 0 | 14 | 5 | 19 | `codex-api/src/endpoint/memories.rs` | `codexapi/src/endpoint/Memories.kt` |
| 506 | `core.rollout` | `core.src.Rollout` | 0 | 5 | 0 | 5 | `core/src/rollout.rs` | `core/src/Rollout.kt` |
| 507 | `core.safety` | `core.src.Safety` | 0 | 4 | 1 | 5 | `core/src/safety.rs` | `core/src/Safety.kt` |
| 508 | `core.safety_tests` | `core.src.SafetyTests` | 0 | 9 | 0 | 9 | `core/src/safety_tests.rs` | `core/src/SafetyTests.kt` |
| 509 | `core.sandbox_tags` | `core.src.SandboxTags` | 0 | 3 | 0 | 3 | `core/src/sandbox_tags.rs` | `core/src/SandboxTags.kt` |
| 510 | `core.sandbox_tags_tests` | `core.src.SandboxTagsTests` | 0 | 8 | 0 | 8 | `core/src/sandbox_tags_tests.rs` | `core/src/SandboxTagsTests.kt` |
| 511 | `session.config_lock` | `core.src.session.ConfigLock` | 0 | 11 | 0 | 11 | `core/src/session/config_lock.rs` | `core/src/session/ConfigLock.kt` |
| 512 | `session.handlers` | `core.src.session.Handlers` | 0 | 32 | 0 | 32 | `core/src/session/handlers.rs` | `core/src/session/Handlers.kt` |
| 513 | `session.mcp` | `core.src.session.Mcp` | 0 | 12 | 0 | 12 | `core/src/session/mcp.rs` | `core/src/session/Mcp.kt` |
| 514 | `session.multi_agents` | `core.src.session.MultiAgents` | 0 | 1 | 0 | 1 | `core/src/session/multi_agents.rs` | `core/src/session/MultiAgents.kt` |
| 515 | `session.review` | `core.src.session.Review` | 0 | 1 | 0 | 1 | `core/src/session/review.rs` | `core/src/session/Review.kt` |
| 516 | `session.rollout_reconstruction` | `core.src.session.RolloutReconstruction` | 0 | 3 | 3 | 6 | `core/src/session/rollout_reconstruction.rs` | `core/src/session/RolloutReconstruction.kt` |
| 517 | `session.rollout_reconstruction_tests` | `core.src.session.RolloutReconstructionTests` | 0 | 22 | 0 | 22 | `core/src/session/rollout_reconstruction_tests.rs` | `core/src/session/RolloutReconstructionTests.kt` |
| 518 | `session.session` | `core.src.session.Session` | 0 | 10 | 4 | 14 | `core/src/session/session.rs` | `core/src/session/Session.kt` |
| 519 | `session.tests` | `core.src.session.tests.Tests` | 0 | 217 | 6 | 223 | `core/src/session/tests.rs` | `core/src/session/tests/Tests.kt` |
| 520 | `tests.guardian_tests` | `core.src.session.tests.GuardianTests` | 0 | 9 | 2 | 11 | `core/src/session/tests/guardian_tests.rs` | `core/src/session/tests/GuardianTests.kt` |
| 521 | `session.turn` | `core.src.session.Turn` | 0 | 36 | 6 | 42 | `core/src/session/turn.rs` | `core/src/session/Turn.kt` |
| 522 | `endpoint.compact` | `codexapi.src.endpoint.Compact` | 0 | 8 | 3 | 11 | `codex-api/src/endpoint/compact.rs` | `codexapi/src/endpoint/Compact.kt` |
| 523 | `core.session_prefix` | `core.src.SessionPrefix` | 0 | 2 | 0 | 2 | `core/src/session_prefix.rs` | `core/src/SessionPrefix.kt` |
| 524 | `core.session_rollout_init_error` | `core.src.SessionRolloutInitError` | 0 | 2 | 0 | 2 | `core/src/session_rollout_init_error.rs` | `core/src/SessionRolloutInitError.kt` |
| 525 | `core.session_startup_prewarm` | `core.src.SessionStartupPrewarm` | 0 | 6 | 2 | 8 | `core/src/session_startup_prewarm.rs` | `core/src/SessionStartupPrewarm.kt` |
| 526 | `core.shell_detect` | `core.src.ShellDetect` | 0 | 1 | 0 | 1 | `core/src/shell_detect.rs` | `core/src/ShellDetect.kt` |
| 527 | `codex-api.auth` | `codexapi.src.Auth` | 0 | 4 | 4 | 8 | `codex-api/src/auth.rs` | `codexapi/src/Auth.kt` |
| 528 | `core.shell_snapshot_tests` | `core.src.ShellSnapshotTests` | 0 | 22 | 1 | 23 | `core/src/shell_snapshot_tests.rs` | `core/src/ShellSnapshotTests.kt` |
| 529 | `core.shell_tests` | `core.src.ShellTests` | 0 | 10 | 0 | 10 | `core/src/shell_tests.rs` | `core/src/ShellTests.kt` |
| 530 | `core.skills` | `core.src.Skills` | 0 | 4 | 0 | 4 | `core/src/skills.rs` | `core/src/Skills.kt` |
| 531 | `codex-api.api_bridge_tests` | `codexapi.src.ApiBridgeTests` | 0 | 9 | 0 | 9 | `codex-api/src/api_bridge_tests.rs` | `codexapi/src/ApiBridgeTests.kt` |
| 532 | `state.service` | `core.src.state.Service` | 0 | 0 | 1 | 1 | `core/src/state/service.rs` | `core/src/state/Service.kt` |
| 533 | `state.session_tests` | `core.src.state.SessionTests` | 0 | 5 | 0 | 5 | `core/src/state/session_tests.rs` | `core/src/state/SessionTests.kt` |
| 534 | `core.state_db_bridge` | `core.src.StateDbBridge` | 0 | 1 | 0 | 1 | `core/src/state_db_bridge.rs` | `core/src/StateDbBridge.kt` |
| 535 | `core.stream_events_utils` | `core.src.StreamEventsUtils` | 0 | 14 | 3 | 17 | `core/src/stream_events_utils.rs` | `core/src/StreamEventsUtils.kt` |
| 536 | `core.stream_events_utils_tests` | `core.src.StreamEventsUtilsTests` | 0 | 17 | 0 | 17 | `core/src/stream_events_utils_tests.rs` | `core/src/StreamEventsUtilsTests.kt` |
| 537 | `tasks.compact` | `core.src.tasks.Compact` | 0 | 3 | 1 | 4 | `core/src/tasks/compact.rs` | `core/src/tasks/Compact.kt` |
| 538 | `tasks.mod_tests` | `core.src.tasks.ModTests` | 0 | 8 | 0 | 8 | `core/src/tasks/mod_tests.rs` | `core/src/tasks/ModTests.kt` |
| 539 | `tasks.regular` | `core.src.tasks.Regular` | 0 | 5 | 1 | 6 | `core/src/tasks/regular.rs` | `core/src/tasks/Regular.kt` |
| 540 | `tasks.review` | `core.src.tasks.Review` | 0 | 13 | 1 | 14 | `core/src/tasks/review.rs` | `core/src/tasks/Review.kt` |
| 541 | `tasks.user_shell` | `core.src.tasks.UserShell` | 0 | 6 | 2 | 8 | `core/src/tasks/user_shell.rs` | `core/src/tasks/UserShell.kt` |
| 542 | `core.test_support` | `core.src.TestSupport` | 0 | 14 | 0 | 14 | `core/src/test_support.rs` | `core/src/TestSupport.kt` |
| 543 | `codex-api.api_bridge` | `codexapi.src.ApiBridge` | 0 | 5 | 2 | 7 | `codex-api/src/api_bridge.rs` | `codexapi/src/ApiBridge.kt` |
| 544 | `core.thread_manager_tests` | `core.src.ThreadManagerTests` | 0 | 27 | 0 | 27 | `core/src/thread_manager_tests.rs` | `core/src/ThreadManagerTests.kt` |
| 545 | `core.thread_rollout_truncation` | `core.src.ThreadRolloutTruncation` | 0 | 8 | 0 | 8 | `core/src/thread_rollout_truncation.rs` | `core/src/ThreadRolloutTruncation.kt` |
| 546 | `core.thread_rollout_truncation_tests` | `core.src.ThreadRolloutTruncationTests` | 0 | 13 | 0 | 13 | `core/src/thread_rollout_truncation_tests.rs` | `core/src/ThreadRolloutTruncationTests.kt` |
| 547 | `code_mode.execute_handler` | `core.src.tools.codemode.ExecuteHandler` | 0 | 4 | 2 | 6 | `core/src/tools/code_mode/execute_handler.rs` | `core/src/tools/codemode/ExecuteHandler.kt` |
| 548 | `code_mode.execute_handler_tests` | `core.src.tools.codemode.ExecuteHandlerTests` | 0 | 4 | 0 | 4 | `core/src/tools/code_mode/execute_handler_tests.rs` | `core/src/tools/codemode/ExecuteHandlerTests.kt` |
| 549 | `code_mode.response_adapter` | `core.src.tools.codemode.ResponseAdapter` | 0 | 3 | 1 | 4 | `core/src/tools/code_mode/response_adapter.rs` | `core/src/tools/codemode/ResponseAdapter.kt` |
| 550 | `code_mode.wait_handler` | `core.src.tools.codemode.WaitHandler` | 0 | 4 | 3 | 7 | `core/src/tools/code_mode/wait_handler.rs` | `core/src/tools/codemode/WaitHandler.kt` |
| 551 | `tools.context_tests` | `core.src.tools.ContextTests` | 0 | 14 | 0 | 14 | `core/src/tools/context_tests.rs` | `core/src/tools/ContextTests.kt` |
| 552 | `handlers.agent_jobs` | `core.src.tools.handlers.AgentJobs` | 0 | 28 | 9 | 37 | `core/src/tools/handlers/agent_jobs.rs` | `core/src/tools/handlers/AgentJobs.kt` |
| 553 | `handlers.agent_jobs_tests` | `core.src.tools.handlers.AgentJobsTests` | 0 | 5 | 0 | 5 | `core/src/tools/handlers/agent_jobs_tests.rs` | `core/src/tools/handlers/AgentJobsTests.kt` |
| 554 | `handlers.apply_patch_tests` | `core.src.tools.handlers.ApplyPatchTests` | 0 | 11 | 0 | 11 | `core/src/tools/handlers/apply_patch_tests.rs` | `core/src/tools/handlers/ApplyPatchTests.kt` |
| 555 | `handlers.dynamic` | `core.src.tools.handlers.Dynamic` | 0 | 4 | 2 | 6 | `core/src/tools/handlers/dynamic.rs` | `core/src/tools/handlers/Dynamic.kt` |
| 556 | `handlers.goal` | `core.src.tools.handlers.Goal` | 0 | 11 | 6 | 17 | `core/src/tools/handlers/goal.rs` | `core/src/tools/handlers/Goal.kt` |
| 557 | `handlers.grep_files_tests` | `core.src.tools.handlers.GrepFilesTests` | 0 | 7 | 0 | 7 | `core/src/tools/handlers/grep_files_tests.rs` | `core/src/tools/handlers/GrepFilesTests.kt` |
| 558 | `handlers.list_dir_tests` | `core.src.tools.handlers.ListDirTests` | 0 | 9 | 0 | 9 | `core/src/tools/handlers/list_dir_tests.rs` | `core/src/tools/handlers/ListDirTests.kt` |
| 559 | `handlers.mcp_resource_tests` | `core.src.tools.handlers.McpResourceTests` | 0 | 8 | 0 | 8 | `core/src/tools/handlers/mcp_resource_tests.rs` | `core/src/tools/handlers/McpResourceTests.kt` |
| 560 | `handlers.multi_agents` | `core.src.tools.handlers.multiagents.MultiAgents` | 0 | 2 | 0 | 2 | `core/src/tools/handlers/multi_agents.rs` | `core/src/tools/handlers/multiagents/MultiAgents.kt` |
| 561 | `multi_agents.close_agent` | `core.src.tools.handlers.multiagents.CloseAgent` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents/close_agent.rs` | `core/src/tools/handlers/multiagents/CloseAgent.kt` |
| 562 | `multi_agents.resume_agent` | `core.src.tools.handlers.multiagents.ResumeAgent` | 0 | 8 | 4 | 12 | `core/src/tools/handlers/multi_agents/resume_agent.rs` | `core/src/tools/handlers/multiagents/ResumeAgent.kt` |
| 563 | `multi_agents.send_input` | `core.src.tools.handlers.multiagents.SendInput` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents/send_input.rs` | `core/src/tools/handlers/multiagents/SendInput.kt` |
| 564 | `multi_agents.spawn` | `core.src.tools.handlers.multiagents.Spawn` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents/spawn.rs` | `core/src/tools/handlers/multiagents/Spawn.kt` |
| 565 | `multi_agents.wait` | `core.src.tools.handlers.multiagents.Wait` | 0 | 8 | 4 | 12 | `core/src/tools/handlers/multi_agents/wait.rs` | `core/src/tools/handlers/multiagents/Wait.kt` |
| 566 | `handlers.multi_agents_common` | `core.src.tools.handlers.MultiAgentsCommon` | 0 | 18 | 0 | 18 | `core/src/tools/handlers/multi_agents_common.rs` | `core/src/tools/handlers/MultiAgentsCommon.kt` |
| 567 | `handlers.multi_agents_tests` | `core.src.tools.handlers.MultiAgentsTests` | 0 | 71 | 3 | 74 | `core/src/tools/handlers/multi_agents_tests.rs` | `core/src/tools/handlers/MultiAgentsTests.kt` |
| 568 | `handlers.multi_agents_v2` | `core.src.tools.handlers.multiagentsv2.MultiAgentsV2` | 0 | 0 | 0 | 0 | `core/src/tools/handlers/multi_agents_v2.rs` | `core/src/tools/handlers/multiagentsv2/MultiAgentsV2.kt` |
| 569 | `multi_agents_v2.close_agent` | `core.src.tools.handlers.multiagentsv2.CloseAgent` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents_v2/close_agent.rs` | `core/src/tools/handlers/multiagentsv2/CloseAgent.kt` |
| 570 | `multi_agents_v2.followup_task` | `core.src.tools.handlers.multiagentsv2.FollowupTask` | 0 | 3 | 2 | 5 | `core/src/tools/handlers/multi_agents_v2/followup_task.rs` | `core/src/tools/handlers/multiagentsv2/FollowupTask.kt` |
| 571 | `multi_agents_v2.list_agents` | `core.src.tools.handlers.multiagentsv2.ListAgents` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents_v2/list_agents.rs` | `core/src/tools/handlers/multiagentsv2/ListAgents.kt` |
| 572 | `multi_agents_v2.message_tool` | `core.src.tools.handlers.multiagentsv2.MessageTool` | 0 | 4 | 3 | 7 | `core/src/tools/handlers/multi_agents_v2/message_tool.rs` | `core/src/tools/handlers/multiagentsv2/MessageTool.kt` |
| 573 | `multi_agents_v2.send_message` | `core.src.tools.handlers.multiagentsv2.SendMessage` | 0 | 3 | 2 | 5 | `core/src/tools/handlers/multi_agents_v2/send_message.rs` | `core/src/tools/handlers/multiagentsv2/SendMessage.kt` |
| 574 | `multi_agents_v2.spawn` | `core.src.tools.handlers.multiagentsv2.Spawn` | 0 | 8 | 4 | 12 | `core/src/tools/handlers/multi_agents_v2/spawn.rs` | `core/src/tools/handlers/multiagentsv2/Spawn.kt` |
| 575 | `multi_agents_v2.wait` | `core.src.tools.handlers.multiagentsv2.Wait` | 0 | 9 | 4 | 13 | `core/src/tools/handlers/multi_agents_v2/wait.rs` | `core/src/tools/handlers/multiagentsv2/Wait.kt` |
| 576 | `handlers.read_file_tests` | `core.src.tools.handlers.ReadFileTests` | 0 | 16 | 0 | 16 | `core/src/tools/handlers/read_file_tests.rs` | `core/src/tools/handlers/ReadFileTests.kt` |
| 577 | `handlers.request_permissions` | `core.src.tools.handlers.RequestPermissions` | 0 | 2 | 2 | 4 | `core/src/tools/handlers/request_permissions.rs` | `core/src/tools/handlers/RequestPermissions.kt` |
| 578 | `handlers.request_plugin_install` | `core.src.tools.handlers.RequestPluginInstall` | 0 | 9 | 2 | 11 | `core/src/tools/handlers/request_plugin_install.rs` | `core/src/tools/handlers/RequestPluginInstall.kt` |
| 579 | `handlers.request_plugin_install_tests` | `core.src.tools.handlers.RequestPluginInstallTests` | 0 | 6 | 0 | 6 | `core/src/tools/handlers/request_plugin_install_tests.rs` | `core/src/tools/handlers/RequestPluginInstallTests.kt` |
| 580 | `handlers.request_user_input` | `core.src.tools.handlers.RequestUserInput` | 0 | 2 | 2 | 4 | `core/src/tools/handlers/request_user_input.rs` | `core/src/tools/handlers/RequestUserInput.kt` |
| 581 | `handlers.request_user_input_tests` | `core.src.tools.handlers.RequestUserInputTests` | 0 | 1 | 0 | 1 | `core/src/tools/handlers/request_user_input_tests.rs` | `core/src/tools/handlers/RequestUserInputTests.kt` |
| 582 | `handlers.shell_tests` | `core.src.tools.handlers.ShellTests` | 0 | 9 | 0 | 9 | `core/src/tools/handlers/shell_tests.rs` | `core/src/tools/handlers/ShellTests.kt` |
| 583 | `handlers.tool_search` | `core.src.tools.handlers.ToolSearch` | 0 | 16 | 2 | 18 | `core/src/tools/handlers/tool_search.rs` | `core/src/tools/handlers/ToolSearch.kt` |
| 584 | `handlers.unavailable_tool` | `core.src.tools.handlers.UnavailableTool` | 0 | 3 | 2 | 5 | `core/src/tools/handlers/unavailable_tool.rs` | `core/src/tools/handlers/UnavailableTool.kt` |
| 585 | `handlers.unified_exec_tests` | `core.src.tools.handlers.UnifiedExecTests` | 0 | 15 | 0 | 15 | `core/src/tools/handlers/unified_exec_tests.rs` | `core/src/tools/handlers/UnifiedExecTests.kt` |
| 586 | `tools.hook_names` | `core.src.tools.HookNames` | 0 | 5 | 1 | 6 | `core/src/tools/hook_names.rs` | `core/src/tools/HookNames.kt` |
| 587 | `tools.network_approval` | `core.src.tools.NetworkApproval` | 0 | 38 | 11 | 49 | `core/src/tools/network_approval.rs` | `core/src/tools/NetworkApproval.kt` |
| 588 | `tools.network_approval_tests` | `core.src.tools.NetworkApprovalTests` | 0 | 17 | 0 | 17 | `core/src/tools/network_approval_tests.rs` | `core/src/tools/NetworkApprovalTests.kt` |
| 589 | `tools.registry_tests` | `core.src.tools.RegistryTests` | 0 | 3 | 2 | 5 | `core/src/tools/registry_tests.rs` | `core/src/tools/RegistryTests.kt` |
| 590 | `tools.router_tests` | `core.src.tools.RouterTests` | 0 | 5 | 0 | 5 | `core/src/tools/router_tests.rs` | `core/src/tools/RouterTests.kt` |
| 591 | `runtimes.apply_patch_tests` | `core.src.tools.runtimes.ApplyPatchTests` | 0 | 5 | 0 | 5 | `core/src/tools/runtimes/apply_patch_tests.rs` | `core/src/tools/runtimes/ApplyPatchTests.kt` |
| 592 | `runtimes.mod_tests` | `core.src.tools.runtimes.ModTests` | 0 | 26 | 1 | 27 | `core/src/tools/runtimes/mod_tests.rs` | `core/src/tools/runtimes/ModTests.kt` |
| 593 | `runtime.module_loader` | `codemode.src.runtime.ModuleLoader` | 0 | 8 | 0 | 8 | `code-mode/src/runtime/module_loader.rs` | `codemode/src/runtime/ModuleLoader.kt` |
| 594 | `shell.unix_escalation_tests` | `core.src.tools.runtimes.shell.UnixEscalationTests` | 0 | 21 | 0 | 21 | `core/src/tools/runtimes/shell/unix_escalation_tests.rs` | `core/src/tools/runtimes/shell/UnixEscalationTests.kt` |
| 595 | `runtime.globals` | `codemode.src.runtime.Globals` | 0 | 7 | 0 | 7 | `code-mode/src/runtime/globals.rs` | `codemode/src/runtime/Globals.kt` |
| 596 | `tools.sandboxing_tests` | `core.src.tools.SandboxingTests` | 0 | 8 | 0 | 8 | `core/src/tools/sandboxing_tests.rs` | `core/src/tools/SandboxingTests.kt` |
| 597 | `tools.spec_tests` | `core.src.tools.SpecTests` | 0 | 45 | 0 | 45 | `core/src/tools/spec_tests.rs` | `core/src/tools/SpecTests.kt` |
| 598 | `runtime.callbacks` | `codemode.src.runtime.Callbacks` | 0 | 10 | 0 | 10 | `code-mode/src/runtime/callbacks.rs` | `codemode/src/runtime/Callbacks.kt` |
| 599 | `tools.tool_dispatch_trace_tests` | `core.src.tools.ToolDispatchTraceTests` | 0 | 10 | 2 | 12 | `core/src/tools/tool_dispatch_trace_tests.rs` | `core/src/tools/ToolDispatchTraceTests.kt` |
| 600 | `code-mode.description` | `codemode.src.Description` | 0 | 34 | 6 | 40 | `code-mode/src/description.rs` | `codemode/src/Description.kt` |
| 601 | `core.turn_diff_tracker_tests` | `core.src.TurnDiffTrackerTests` | 0 | 10 | 0 | 10 | `core/src/turn_diff_tracker_tests.rs` | `core/src/TurnDiffTrackerTests.kt` |
| 602 | `core.turn_metadata` | `core.src.TurnMetadata` | 0 | 14 | 4 | 18 | `core/src/turn_metadata.rs` | `core/src/TurnMetadata.kt` |
| 603 | `core.turn_metadata_tests` | `core.src.TurnMetadataTests` | 0 | 6 | 0 | 6 | `core/src/turn_metadata_tests.rs` | `core/src/TurnMetadataTests.kt` |
| 604 | `core.turn_timing` | `core.src.TurnTiming` | 0 | 15 | 2 | 17 | `core/src/turn_timing.rs` | `core/src/TurnTiming.kt` |
| 605 | `core.turn_timing_tests` | `core.src.TurnTimingTests` | 0 | 5 | 0 | 5 | `core/src/turn_timing_tests.rs` | `core/src/TurnTimingTests.kt` |
| 606 | `core.unavailable_tool` | `core.src.UnavailableTool` | 0 | 5 | 0 | 5 | `core/src/unavailable_tool.rs` | `core/src/UnavailableTool.kt` |
| 607 | `unified_exec.async_watcher` | `core.src.unifiedexec.AsyncWatcher` | 0 | 8 | 0 | 8 | `core/src/unified_exec/async_watcher.rs` | `core/src/unifiedexec/AsyncWatcher.kt` |
| 608 | `unified_exec.async_watcher_tests` | `core.src.unifiedexec.AsyncWatcherTests` | 0 | 3 | 0 | 3 | `core/src/unified_exec/async_watcher_tests.rs` | `core/src/unifiedexec/AsyncWatcherTests.kt` |
| 609 | `cloud-tasks.util` | `cloudtasks.src.Util` | 0 | 8 | 0 | 8 | `cloud-tasks/src/util.rs` | `cloudtasks/src/Util.kt` |
| 610 | `unified_exec.head_tail_buffer_tests` | `core.src.unifiedexec.HeadTailBufferTests` | 0 | 6 | 0 | 6 | `core/src/unified_exec/head_tail_buffer_tests.rs` | `core/src/unifiedexec/HeadTailBufferTests.kt` |
| 611 | `unified_exec.mod_tests` | `core.src.unifiedexec.ModTests` | 0 | 19 | 1 | 20 | `core/src/unified_exec/mod_tests.rs` | `core/src/unifiedexec/ModTests.kt` |
| 612 | `unified_exec.process` | `core.src.unifiedexec.Process` | 0 | 24 | 7 | 31 | `core/src/unified_exec/process.rs` | `core/src/unifiedexec/Process.kt` |
| 613 | `unified_exec.process_manager` | `core.src.unifiedexec.ProcessManager` | 0 | 34 | 2 | 36 | `core/src/unified_exec/process_manager.rs` | `core/src/unifiedexec/ProcessManager.kt` |
| 614 | `unified_exec.process_manager_tests` | `core.src.unifiedexec.ProcessManagerTests` | 0 | 11 | 0 | 11 | `core/src/unified_exec/process_manager_tests.rs` | `core/src/unifiedexec/ProcessManagerTests.kt` |
| 615 | `cloud-tasks.ui` | `cloudtasks.src.Ui` | 0 | 21 | 1 | 22 | `cloud-tasks/src/ui.rs` | `cloudtasks/src/Ui.kt` |
| 616 | `unified_exec.process_tests` | `core.src.unifiedexec.ProcessTests` | 0 | 11 | 1 | 12 | `core/src/unified_exec/process_tests.rs` | `core/src/unifiedexec/ProcessTests.kt` |
| 617 | `core.user_shell_command_tests` | `core.src.UserShellCommandTests` | 0 | 3 | 0 | 3 | `core/src/user_shell_command_tests.rs` | `core/src/UserShellCommandTests.kt` |
| 618 | `core.util_tests` | `core.src.UtilTests` | 0 | 15 | 3 | 18 | `core/src/util_tests.rs` | `core/src/UtilTests.kt` |
| 619 | `cloud-tasks.new_task` | `cloudtasks.src.NewTask` | 0 | 2 | 1 | 3 | `cloud-tasks/src/new_task.rs` | `cloudtasks/src/NewTask.kt` |
| 620 | `core.web_search` | `core.src.WebSearch` | 0 | 3 | 0 | 3 | `core/src/web_search.rs` | `core/src/WebSearch.kt` |
| 621 | `core.windows_sandbox` | `core.src.WindowsSandbox` | 0 | 26 | 3 | 29 | `core/src/windows_sandbox.rs` | `core/src/WindowsSandbox.kt` |
| 622 | `core.windows_sandbox_read_grants` | `core.src.WindowsSandboxReadGrants` | 0 | 1 | 0 | 1 | `core/src/windows_sandbox_read_grants.rs` | `core/src/WindowsSandboxReadGrants.kt` |
| 623 | `core.windows_sandbox_read_grants_tests` | `core.src.WindowsSandboxReadGrantsTests` | 0 | 4 | 0 | 4 | `core/src/windows_sandbox_read_grants_tests.rs` | `core/src/WindowsSandboxReadGrantsTests.kt` |
| 624 | `core.windows_sandbox_tests` | `core.src.WindowsSandboxTests` | 0 | 12 | 0 | 12 | `core/src/windows_sandbox_tests.rs` | `core/src/WindowsSandboxTests.kt` |
| 625 | `core.tests.all` | `core.tests.All` | 0 | 0 | 0 | 0 | `core/tests/all.rs` | `core/tests/All.kt` |
| 626 | `cloud-tasks.env_detect` | `cloudtasks.src.EnvDetect` | 0 | 7 | 2 | 9 | `cloud-tasks/src/env_detect.rs` | `cloudtasks/src/EnvDetect.kt` |
| 627 | `cloud-tasks.cli` | `cloudtasks.src.Cli` | 0 | 2 | 7 | 9 | `cloud-tasks/src/cli.rs` | `cloudtasks/src/Cli.kt` |
| 628 | `common.process` | `core.tests.common.Process` | 0 | 4 | 0 | 4 | `core/tests/common/process.rs` | `core/tests/common/Process.kt` |
| 629 | `common.responses` | `core.tests.common.Responses` | 0 | 114 | 10 | 124 | `core/tests/common/responses.rs` | `core/tests/common/Responses.kt` |
| 630 | `common.streaming_sse` | `core.tests.common.StreamingSse` | 0 | 34 | 3 | 37 | `core/tests/common/streaming_sse.rs` | `core/tests/common/StreamingSse.kt` |
| 631 | `cloud-tasks.app` | `cloudtasks.src.App` | 0 | 26 | 11 | 37 | `cloud-tasks/src/app.rs` | `cloudtasks/src/App.kt` |
| 632 | `cloud-tasks-client.http` | `cloudtasksclient.src.Http` | 0 | 48 | 4 | 52 | `cloud-tasks-client/src/http.rs` | `cloudtasksclient/src/Http.kt` |
| 633 | `common.tracing` | `core.tests.common.Tracing` | 0 | 1 | 1 | 2 | `core/tests/common/tracing.rs` | `core/tests/common/Tracing.kt` |
| 634 | `common.zsh_fork` | `core.tests.common.ZshFork` | 0 | 6 | 1 | 7 | `core/tests/common/zsh_fork.rs` | `core/tests/common/ZshFork.kt` |
| 635 | `tests.responses_headers` | `core.tests.ResponsesHeaders` | 0 | 5 | 0 | 5 | `core/tests/responses_headers.rs` | `core/tests/ResponsesHeaders.kt` |
| 636 | `suite.abort_tasks` | `core.tests.suite.AbortTasks` | 0 | 3 | 0 | 3 | `core/tests/suite/abort_tasks.rs` | `core/tests/suite/AbortTasks.kt` |
| 637 | `suite.agent_jobs` | `core.tests.suite.AgentJobs` | 0 | 13 | 2 | 15 | `core/tests/suite/agent_jobs.rs` | `core/tests/suite/AgentJobs.kt` |
| 638 | `suite.agent_websocket` | `core.tests.suite.AgentWebsocket` | 0 | 7 | 0 | 7 | `core/tests/suite/agent_websocket.rs` | `core/tests/suite/AgentWebsocket.kt` |
| 639 | `suite.agents_md` | `core.tests.suite.AgentsMd` | 0 | 4 | 0 | 4 | `core/tests/suite/agents_md.rs` | `core/tests/suite/AgentsMd.kt` |
| 640 | `suite.apply_patch_cli` | `core.tests.suite.ApplyPatchCli` | 0 | 40 | 1 | 41 | `core/tests/suite/apply_patch_cli.rs` | `core/tests/suite/ApplyPatchCli.kt` |
| 641 | `suite.approvals` | `core.tests.suite.Approvals` | 0 | 31 | 7 | 38 | `core/tests/suite/approvals.rs` | `core/tests/suite/Approvals.kt` |
| 642 | `suite.cli_stream` | `core.tests.suite.CliStream` | 0 | 9 | 0 | 9 | `core/tests/suite/cli_stream.rs` | `core/tests/suite/CliStream.kt` |
| 643 | `suite.client` | `core.tests.suite.Client` | 0 | 45 | 1 | 46 | `core/tests/suite/client.rs` | `core/tests/suite/Client.kt` |
| 644 | `suite.client_websockets` | `core.tests.suite.ClientWebsockets` | 0 | 49 | 1 | 50 | `core/tests/suite/client_websockets.rs` | `core/tests/suite/ClientWebsockets.kt` |
| 645 | `suite.code_mode` | `core.tests.suite.CodeMode` | 0 | 52 | 0 | 52 | `core/tests/suite/code_mode.rs` | `core/tests/suite/CodeMode.kt` |
| 646 | `suite.codex_delegate` | `core.tests.suite.CodexDelegate` | 0 | 3 | 0 | 3 | `core/tests/suite/codex_delegate.rs` | `core/tests/suite/CodexDelegate.kt` |
| 647 | `suite.collaboration_instructions` | `core.tests.suite.CollaborationInstructions` | 0 | 17 | 0 | 17 | `core/tests/suite/collaboration_instructions.rs` | `core/tests/suite/CollaborationInstructions.kt` |
| 648 | `suite.compact` | `core.tests.suite.Compact` | 0 | 38 | 0 | 38 | `core/tests/suite/compact.rs` | `core/tests/suite/Compact.kt` |
| 649 | `suite.compact_remote` | `core.tests.suite.CompactRemote` | 0 | 45 | 0 | 45 | `core/tests/suite/compact_remote.rs` | `core/tests/suite/CompactRemote.kt` |
| 650 | `suite.compact_resume_fork` | `core.tests.suite.CompactResumeFork` | 0 | 23 | 0 | 23 | `core/tests/suite/compact_resume_fork.rs` | `core/tests/suite/CompactResumeFork.kt` |
| 651 | `suite.deprecation_notice` | `core.tests.suite.DeprecationNotice` | 0 | 5 | 0 | 5 | `core/tests/suite/deprecation_notice.rs` | `core/tests/suite/DeprecationNotice.kt` |
| 652 | `suite.exec_policy` | `core.tests.suite.ExecPolicy` | 0 | 8 | 0 | 8 | `core/tests/suite/exec_policy.rs` | `core/tests/suite/ExecPolicy.kt` |
| 653 | `suite.fork_thread` | `core.tests.suite.ForkThread` | 0 | 3 | 0 | 3 | `core/tests/suite/fork_thread.rs` | `core/tests/suite/ForkThread.kt` |
| 654 | `suite.hierarchical_agents` | `core.tests.suite.HierarchicalAgents` | 0 | 2 | 0 | 2 | `core/tests/suite/hierarchical_agents.rs` | `core/tests/suite/HierarchicalAgents.kt` |
| 655 | `cloud-tasks-client.api` | `cloudtasksclient.src.Api` | 0 | 1 | 14 | 15 | `cloud-tasks-client/src/api.rs` | `cloudtasksclient/src/Api.kt` |
| 656 | `suite.hooks_mcp` | `core.tests.suite.HooksMcp` | 0 | 7 | 0 | 7 | `core/tests/suite/hooks_mcp.rs` | `core/tests/suite/HooksMcp.kt` |
| 657 | `suite.image_rollout` | `core.tests.suite.ImageRollout` | 0 | 6 | 0 | 6 | `core/tests/suite/image_rollout.rs` | `core/tests/suite/ImageRollout.kt` |
| 658 | `suite.items` | `core.tests.suite.Items` | 0 | 17 | 0 | 17 | `core/tests/suite/items.rs` | `core/tests/suite/Items.kt` |
| 659 | `suite.json_result` | `core.tests.suite.JsonResult` | 0 | 3 | 0 | 3 | `core/tests/suite/json_result.rs` | `core/tests/suite/JsonResult.kt` |
| 660 | `suite.live_cli` | `core.tests.suite.LiveCli` | 0 | 5 | 0 | 5 | `core/tests/suite/live_cli.rs` | `core/tests/suite/LiveCli.kt` |
| 661 | `suite.live_reload` | `core.tests.suite.LiveReload` | 0 | 5 | 0 | 5 | `core/tests/suite/live_reload.rs` | `core/tests/suite/LiveReload.kt` |
| 662 | `suite.model_overrides` | `core.tests.suite.ModelOverrides` | 0 | 2 | 0 | 2 | `core/tests/suite/model_overrides.rs` | `core/tests/suite/ModelOverrides.kt` |
| 663 | `suite.model_switching` | `core.tests.suite.ModelSwitching` | 0 | 13 | 0 | 13 | `core/tests/suite/model_switching.rs` | `core/tests/suite/ModelSwitching.kt` |
| 664 | `suite.model_visible_layout` | `core.tests.suite.ModelVisibleLayout` | 0 | 10 | 0 | 10 | `core/tests/suite/model_visible_layout.rs` | `core/tests/suite/ModelVisibleLayout.kt` |
| 665 | `suite.models_cache_ttl` | `core.tests.suite.ModelsCacheTtl` | 0 | 9 | 1 | 10 | `core/tests/suite/models_cache_ttl.rs` | `core/tests/suite/ModelsCacheTtl.kt` |
| 666 | `suite.models_etag_responses` | `core.tests.suite.ModelsEtagResponses` | 0 | 1 | 0 | 1 | `core/tests/suite/models_etag_responses.rs` | `core/tests/suite/ModelsEtagResponses.kt` |
| 667 | `suite.openai_file_mcp` | `core.tests.suite.OpenaiFileMcp` | 0 | 4 | 0 | 4 | `core/tests/suite/openai_file_mcp.rs` | `core/tests/suite/OpenaiFileMcp.kt` |
| 668 | `suite.otel` | `core.tests.suite.Otel` | 0 | 28 | 0 | 28 | `core/tests/suite/otel.rs` | `core/tests/suite/Otel.kt` |
| 669 | `suite.override_updates` | `core.tests.suite.OverrideUpdates` | 0 | 8 | 0 | 8 | `core/tests/suite/override_updates.rs` | `core/tests/suite/OverrideUpdates.kt` |
| 670 | `suite.pending_input` | `core.tests.suite.PendingInput` | 0 | 22 | 0 | 22 | `core/tests/suite/pending_input.rs` | `core/tests/suite/PendingInput.kt` |
| 671 | `suite.permissions_messages` | `core.tests.suite.PermissionsMessages` | 0 | 8 | 0 | 8 | `core/tests/suite/permissions_messages.rs` | `core/tests/suite/PermissionsMessages.kt` |
| 672 | `tests.update` | `cli.tests.Update` | 0 | 2 | 0 | 2 | `cli/tests/update.rs` | `cli/tests/Update.kt` |
| 673 | `suite.personality_migration` | `core.tests.suite.PersonalityMigration` | 0 | 18 | 0 | 18 | `core/tests/suite/personality_migration.rs` | `core/tests/suite/PersonalityMigration.kt` |
| 674 | `suite.plugins` | `core.tests.suite.Plugins` | 0 | 14 | 0 | 14 | `core/tests/suite/plugins.rs` | `core/tests/suite/Plugins.kt` |
| 675 | `suite.prompt_caching` | `core.tests.suite.PromptCaching` | 0 | 13 | 0 | 13 | `core/tests/suite/prompt_caching.rs` | `core/tests/suite/PromptCaching.kt` |
| 676 | `suite.prompt_debug_tests` | `core.tests.suite.PromptDebugTests` | 0 | 1 | 0 | 1 | `core/tests/suite/prompt_debug_tests.rs` | `core/tests/suite/PromptDebugTests.kt` |
| 677 | `suite.quota_exceeded` | `core.tests.suite.QuotaExceeded` | 0 | 1 | 0 | 1 | `core/tests/suite/quota_exceeded.rs` | `core/tests/suite/QuotaExceeded.kt` |
| 678 | `suite.realtime_conversation` | `core.tests.suite.RealtimeConversation` | 0 | 49 | 1 | 50 | `core/tests/suite/realtime_conversation.rs` | `core/tests/suite/RealtimeConversation.kt` |
| 679 | `suite.remote_env` | `core.tests.suite.RemoteEnv` | 0 | 11 | 0 | 11 | `core/tests/suite/remote_env.rs` | `core/tests/suite/RemoteEnv.kt` |
| 680 | `suite.remote_models` | `core.tests.suite.RemoteModels` | 0 | 21 | 0 | 21 | `core/tests/suite/remote_models.rs` | `core/tests/suite/RemoteModels.kt` |
| 681 | `tests.mcp_list` | `cli.tests.McpList` | 0 | 4 | 0 | 4 | `cli/tests/mcp_list.rs` | `cli/tests/McpList.kt` |
| 682 | `suite.request_permissions` | `core.tests.suite.RequestPermissions` | 0 | 31 | 1 | 32 | `core/tests/suite/request_permissions.rs` | `core/tests/suite/RequestPermissions.kt` |
| 683 | `suite.request_permissions_tool` | `core.tests.suite.RequestPermissionsTool` | 0 | 14 | 0 | 14 | `core/tests/suite/request_permissions_tool.rs` | `core/tests/suite/RequestPermissionsTool.kt` |
| 684 | `suite.request_plugin_install` | `core.tests.suite.RequestPluginInstall` | 0 | 4 | 0 | 4 | `core/tests/suite/request_plugin_install.rs` | `core/tests/suite/RequestPluginInstall.kt` |
| 685 | `suite.request_user_input` | `core.tests.suite.RequestUserInput` | 0 | 9 | 0 | 9 | `core/tests/suite/request_user_input.rs` | `core/tests/suite/RequestUserInput.kt` |
| 686 | `suite.responses_api_proxy_headers` | `core.tests.suite.ResponsesApiProxyHeaders` | 0 | 8 | 0 | 8 | `core/tests/suite/responses_api_proxy_headers.rs` | `core/tests/suite/ResponsesApiProxyHeaders.kt` |
| 687 | `suite.resume` | `core.tests.suite.Resume` | 0 | 5 | 0 | 5 | `core/tests/suite/resume.rs` | `core/tests/suite/Resume.kt` |
| 688 | `suite.resume_warning` | `core.tests.suite.ResumeWarning` | 0 | 2 | 0 | 2 | `core/tests/suite/resume_warning.rs` | `core/tests/suite/ResumeWarning.kt` |
| 689 | `suite.review` | `core.tests.suite.Review` | 0 | 13 | 0 | 13 | `core/tests/suite/review.rs` | `core/tests/suite/Review.kt` |
| 690 | `suite.rmcp_client` | `core.tests.suite.RmcpClient` | 0 | 48 | 6 | 54 | `core/tests/suite/rmcp_client.rs` | `core/tests/suite/RmcpClient.kt` |
| 691 | `suite.rollout_list_find` | `core.tests.suite.RolloutListFind` | 0 | 11 | 0 | 11 | `core/tests/suite/rollout_list_find.rs` | `core/tests/suite/RolloutListFind.kt` |
| 692 | `suite.safety_check_downgrade` | `core.tests.suite.SafetyCheckDowngrade` | 0 | 8 | 0 | 8 | `core/tests/suite/safety_check_downgrade.rs` | `core/tests/suite/SafetyCheckDowngrade.kt` |
| 693 | `suite.search_tool` | `core.tests.suite.SearchTool` | 0 | 20 | 0 | 20 | `core/tests/suite/search_tool.rs` | `core/tests/suite/SearchTool.kt` |
| 694 | `suite.shell_command` | `core.tests.suite.ShellCommand` | 0 | 15 | 0 | 15 | `core/tests/suite/shell_command.rs` | `core/tests/suite/ShellCommand.kt` |
| 695 | `suite.shell_serialization` | `core.tests.suite.ShellSerialization` | 0 | 18 | 0 | 18 | `core/tests/suite/shell_serialization.rs` | `core/tests/suite/ShellSerialization.kt` |
| 696 | `suite.shell_snapshot` | `core.tests.suite.ShellSnapshot` | 0 | 20 | 2 | 22 | `core/tests/suite/shell_snapshot.rs` | `core/tests/suite/ShellSnapshot.kt` |
| 697 | `suite.skill_approval` | `core.tests.suite.SkillApproval` | 0 | 10 | 0 | 10 | `core/tests/suite/skill_approval.rs` | `core/tests/suite/SkillApproval.kt` |
| 698 | `suite.skills` | `core.tests.suite.Skills` | 0 | 8 | 0 | 8 | `core/tests/suite/skills.rs` | `core/tests/suite/Skills.kt` |
| 699 | `suite.spawn_agent_description` | `core.tests.suite.SpawnAgentDescription` | 0 | 4 | 0 | 4 | `core/tests/suite/spawn_agent_description.rs` | `core/tests/suite/SpawnAgentDescription.kt` |
| 700 | `suite.sqlite_state` | `core.tests.suite.SqliteState` | 0 | 6 | 0 | 6 | `core/tests/suite/sqlite_state.rs` | `core/tests/suite/SqliteState.kt` |
| 701 | `suite.stream_error_allows_next_turn` | `core.tests.suite.StreamErrorAllowsNextTurn` | 0 | 1 | 0 | 1 | `core/tests/suite/stream_error_allows_next_turn.rs` | `core/tests/suite/StreamErrorAllowsNextTurn.kt` |
| 702 | `suite.stream_no_completed` | `core.tests.suite.StreamNoCompleted` | 0 | 2 | 0 | 2 | `core/tests/suite/stream_no_completed.rs` | `core/tests/suite/StreamNoCompleted.kt` |
| 703 | `suite.subagent_notifications` | `core.tests.suite.SubagentNotifications` | 0 | 17 | 0 | 17 | `core/tests/suite/subagent_notifications.rs` | `core/tests/suite/SubagentNotifications.kt` |
| 704 | `suite.tool_harness` | `core.tests.suite.ToolHarness` | 0 | 6 | 0 | 6 | `core/tests/suite/tool_harness.rs` | `core/tests/suite/ToolHarness.kt` |
| 705 | `suite.tool_parallelism` | `core.tests.suite.ToolParallelism` | 0 | 9 | 0 | 9 | `core/tests/suite/tool_parallelism.rs` | `core/tests/suite/ToolParallelism.kt` |
| 706 | `suite.tools` | `core.tests.suite.Tools` | 0 | 14 | 0 | 14 | `core/tests/suite/tools.rs` | `core/tests/suite/Tools.kt` |
| 707 | `tests.mcp_add_remove` | `cli.tests.McpAddRemove` | 0 | 7 | 0 | 7 | `cli/tests/mcp_add_remove.rs` | `cli/tests/McpAddRemove.kt` |
| 708 | `tests.marketplace_upgrade` | `cli.tests.MarketplaceUpgrade` | 0 | 3 | 0 | 3 | `cli/tests/marketplace_upgrade.rs` | `cli/tests/MarketplaceUpgrade.kt` |
| 709 | `tests.marketplace_remove` | `cli.tests.MarketplaceRemove` | 0 | 5 | 0 | 5 | `cli/tests/marketplace_remove.rs` | `cli/tests/MarketplaceRemove.kt` |
| 710 | `suite.unstable_features_warning` | `core.tests.suite.UnstableFeaturesWarning` | 0 | 2 | 0 | 2 | `core/tests/suite/unstable_features_warning.rs` | `core/tests/suite/UnstableFeaturesWarning.kt` |
| 711 | `suite.user_shell_cmd` | `core.tests.suite.UserShellCmd` | 0 | 7 | 0 | 7 | `core/tests/suite/user_shell_cmd.rs` | `core/tests/suite/UserShellCmd.kt` |
| 712 | `suite.view_image` | `core.tests.suite.ViewImage` | 0 | 21 | 0 | 21 | `core/tests/suite/view_image.rs` | `core/tests/suite/ViewImage.kt` |
| 713 | `suite.web_search` | `core.tests.suite.WebSearch` | 0 | 6 | 0 | 6 | `core/tests/suite/web_search.rs` | `core/tests/suite/WebSearch.kt` |
| 714 | `suite.websocket_fallback` | `core.tests.suite.WebsocketFallback` | 0 | 4 | 0 | 4 | `core/tests/suite/websocket_fallback.rs` | `core/tests/suite/WebsocketFallback.kt` |
| 715 | `suite.window_headers` | `core.tests.suite.WindowHeaders` | 0 | 5 | 0 | 5 | `core/tests/suite/window_headers.rs` | `core/tests/suite/WindowHeaders.kt` |
| 716 | `debug-client.client` | `debugclient.src.Client` | 0 | 23 | 1 | 24 | `debug-client/src/client.rs` | `debugclient/src/Client.kt` |
| 717 | `debug-client.commands` | `debugclient.src.Commands` | 0 | 10 | 3 | 13 | `debug-client/src/commands.rs` | `debugclient/src/Commands.kt` |
| 718 | `debug-client.main` | `debugclient.src.Main` | 0 | 5 | 1 | 6 | `debug-client/src/main.rs` | `debugclient/src/Main.kt` |
| 719 | `tests.marketplace_add` | `cli.tests.MarketplaceAdd` | 0 | 5 | 0 | 5 | `cli/tests/marketplace_add.rs` | `cli/tests/MarketplaceAdd.kt` |
| 720 | `debug-client.reader` | `debugclient.src.Reader` | 0 | 7 | 0 | 7 | `debug-client/src/reader.rs` | `debugclient/src/Reader.kt` |
| 721 | `debug-client.state` | `debugclient.src.State` | 0 | 0 | 3 | 3 | `debug-client/src/state.rs` | `debugclient/src/State.kt` |
| 722 | `device-key.platform` | `devicekey.src.Platform` | 0 | 5 | 1 | 6 | `device-key/src/platform.rs` | `devicekey/src/Platform.kt` |
| 723 | `exec-server.client` | `execserver.src.client.Client` | 0 | 59 | 7 | 66 | `exec-server/src/client.rs` | `execserver/src/client/Client.kt` |
| 724 | `client.http_client` | `execserver.src.client.HttpClient` | 0 | 0 | 0 | 0 | `exec-server/src/client/http_client.rs` | `execserver/src/client/HttpClient.kt` |
| 725 | `tests.login` | `cli.tests.Login` | 0 | 5 | 0 | 5 | `cli/tests/login.rs` | `cli/tests/Login.kt` |
| 726 | `tests.execpolicy` | `cli.tests.Execpolicy` | 0 | 2 | 0 | 2 | `cli/tests/execpolicy.rs` | `cli/tests/Execpolicy.kt` |
| 727 | `client.rpc_http_client` | `execserver.src.client.RpcHttpClient` | 0 | 4 | 0 | 4 | `exec-server/src/client/rpc_http_client.rs` | `execserver/src/client/RpcHttpClient.kt` |
| 728 | `exec-server.client_api` | `execserver.src.ClientApi` | 0 | 0 | 3 | 3 | `exec-server/src/client_api.rs` | `execserver/src/ClientApi.kt` |
| 729 | `exec-server.connection` | `execserver.src.Connection` | 0 | 7 | 2 | 9 | `exec-server/src/connection.rs` | `execserver/src/Connection.kt` |
| 730 | `tests.debug_models` | `cli.tests.DebugModels` | 0 | 3 | 0 | 3 | `cli/tests/debug_models.rs` | `cli/tests/DebugModels.kt` |
| 731 | `agent-graph-store.store` | `agentgraphstore.src.Store` | 0 | 0 | 1 | 1 | `agent-graph-store/src/store.rs` | `agentgraphstore/src/Store.kt` |
| 732 | `agent-graph-store.local` | `agentgraphstore.src.Local` | 0 | 13 | 2 | 15 | `agent-graph-store/src/local.rs` | `agentgraphstore/src/Local.kt` |
| 733 | `exec-server.fs_helper_main` | `execserver.src.FsHelperMain` | 0 | 2 | 0 | 2 | `exec-server/src/fs_helper_main.rs` | `execserver/src/FsHelperMain.kt` |
| 734 | `exec-server.fs_sandbox` | `execserver.src.FsSandbox` | 0 | 30 | 1 | 31 | `exec-server/src/fs_sandbox.rs` | `execserver/src/FsSandbox.kt` |
| 735 | `tests.debug_clear_memories` | `cli.tests.DebugClearMemories` | 0 | 2 | 0 | 2 | `cli/tests/debug_clear_memories.rs` | `cli/tests/DebugClearMemories.kt` |
| 736 | `cli.wsl_paths` | `cli.src.WslPaths` | 0 | 4 | 0 | 4 | `cli/src/wsl_paths.rs` | `cli/src/WslPaths.kt` |
| 737 | `exec-server.process` | `execserver.src.Process` | 0 | 8 | 8 | 16 | `exec-server/src/process.rs` | `execserver/src/Process.kt` |
| 738 | `cli.mcp_cmd` | `cli.src.McpCmd` | 0 | 11 | 11 | 22 | `cli/src/mcp_cmd.rs` | `cli/src/McpCmd.kt` |
| 739 | `cli.marketplace_cmd` | `cli.src.MarketplaceCmd` | 0 | 8 | 5 | 13 | `cli/src/marketplace_cmd.rs` | `cli/src/MarketplaceCmd.kt` |
| 740 | `cli.main` | `cli.src.Main` | 0 | 99 | 35 | 134 | `cli/src/main.rs` | `cli/src/Main.kt` |
| 741 | `exec-server.rpc` | `execserver.src.Rpc` | 0 | 29 | 10 | 39 | `exec-server/src/rpc.rs` | `execserver/src/Rpc.kt` |
| 742 | `exec-server.runtime_paths` | `execserver.src.RuntimePaths` | 0 | 3 | 1 | 4 | `exec-server/src/runtime_paths.rs` | `execserver/src/RuntimePaths.kt` |
| 743 | `cli.login` | `cli.src.Login` | 0 | 17 | 0 | 17 | `cli/src/login.rs` | `cli/src/Login.kt` |
| 744 | `exec-server.server` | `execserver.src.server.Server` | 0 | 1 | 0 | 1 | `exec-server/src/server.rs` | `execserver/src/server/Server.kt` |
| 745 | `desktop_app.windows` | `cli.src.desktopapp.Windows` | 0 | 9 | 0 | 9 | `cli/src/desktop_app/windows.rs` | `cli/src/desktopapp/Windows.kt` |
| 746 | `server.handler` | `execserver.src.server.handler.Handler` | 0 | 23 | 1 | 24 | `exec-server/src/server/handler.rs` | `execserver/src/server/handler/Handler.kt` |
| 747 | `handler.tests` | `execserver.src.server.handler.Tests` | 0 | 14 | 0 | 14 | `exec-server/src/server/handler/tests.rs` | `execserver/src/server/handler/Tests.kt` |
| 748 | `server.jsonrpc` | `execserver.src.server.Jsonrpc` | 0 | 5 | 0 | 5 | `exec-server/src/server/jsonrpc.rs` | `execserver/src/server/Jsonrpc.kt` |
| 749 | `debug_sandbox.seatbelt` | `cli.src.debugsandbox.Seatbelt` | 0 | 5 | 2 | 7 | `cli/src/debug_sandbox/seatbelt.rs` | `cli/src/debugsandbox/Seatbelt.kt` |
| 750 | `server.processor` | `execserver.src.server.Processor` | 0 | 12 | 1 | 13 | `exec-server/src/server/processor.rs` | `execserver/src/server/Processor.kt` |
| 751 | `server.registry` | `execserver.src.server.Registry` | 0 | 1 | 0 | 1 | `exec-server/src/server/registry.rs` | `execserver/src/server/Registry.kt` |
| 752 | `cli.debug_sandbox` | `cli.src.debugsandbox.DebugSandbox` | 0 | 28 | 4 | 32 | `cli/src/debug_sandbox.rs` | `cli/src/debugsandbox/DebugSandbox.kt` |
| 753 | `server.transport` | `execserver.src.server.Transport` | 0 | 6 | 2 | 8 | `exec-server/src/server/transport.rs` | `execserver/src/server/Transport.kt` |
| 754 | `server.transport_tests` | `execserver.src.server.TransportTests` | 0 | 9 | 0 | 9 | `exec-server/src/server/transport_tests.rs` | `execserver/src/server/TransportTests.kt` |
| 755 | `cli.app_cmd` | `cli.src.AppCmd` | 0 | 1 | 1 | 2 | `cli/src/app_cmd.rs` | `cli/src/AppCmd.kt` |
| 756 | `cli.build` | `cli.Build` | 0 | 1 | 0 | 1 | `cli/build.rs` | `cli/Build.kt` |
| 757 | `tests.file_system` | `execserver.tests.FileSystem` | 0 | 33 | 1 | 34 | `exec-server/tests/file_system.rs` | `execserver/tests/FileSystem.kt` |
| 758 | `suite.apply_command_e2e` | `chatgpt.tests.suite.ApplyCommandE2e` | 0 | 5 | 1 | 6 | `chatgpt/tests/suite/apply_command_e2e.rs` | `chatgpt/tests/suite/ApplyCommandE2e.kt` |
| 759 | `tests.http_request` | `execserver.tests.HttpRequest` | 0 | 14 | 1 | 15 | `exec-server/tests/http_request.rs` | `execserver/tests/HttpRequest.kt` |
| 760 | `tests.initialize` | `execserver.tests.Initialize` | 0 | 1 | 0 | 1 | `exec-server/tests/initialize.rs` | `execserver/tests/Initialize.kt` |
| 761 | `tests.process` | `execserver.tests.Process` | 0 | 3 | 0 | 3 | `exec-server/tests/process.rs` | `execserver/tests/Process.kt` |
| 762 | `chatgpt.tests.all` | `chatgpt.tests.All` | 0 | 0 | 0 | 0 | `chatgpt/tests/all.rs` | `chatgpt/tests/All.kt` |
| 763 | `exec.cli` | `exec.src.Cli` | 0 | 16 | 8 | 24 | `exec/src/cli.rs` | `exec/src/Cli.kt` |
| 764 | `exec.cli_tests` | `exec.src.CliTests` | 0 | 4 | 0 | 4 | `exec/src/cli_tests.rs` | `exec/src/CliTests.kt` |
| 765 | `chatgpt.workspace_settings_tests` | `chatgpt.src.WorkspaceSettingsTests` | 0 | 2 | 0 | 2 | `chatgpt/src/workspace_settings_tests.rs` | `chatgpt/src/WorkspaceSettingsTests.kt` |
| 766 | `chatgpt.connectors` | `chatgpt.src.Connectors` | 0 | 16 | 0 | 16 | `chatgpt/src/connectors.rs` | `chatgpt/src/Connectors.kt` |
| 767 | `exec.event_processor_with_human_output_tests` | `exec.src.EventProcessorWithHumanOutputTests` | 0 | 20 | 0 | 20 | `exec/src/event_processor_with_human_output_tests.rs` | `exec/src/EventProcessorWithHumanOutputTests.kt` |
| 768 | `exec.event_processor_with_jsonl_output` | `exec.src.EventProcessorWithJsonlOutput` | 0 | 20 | 3 | 23 | `exec/src/event_processor_with_jsonl_output.rs` | `exec/src/EventProcessorWithJsonlOutput.kt` |
| 769 | `exec.event_processor_with_jsonl_output_tests` | `exec.src.EventProcessorWithJsonlOutputTests` | 0 | 1 | 0 | 1 | `exec/src/event_processor_with_jsonl_output_tests.rs` | `exec/src/EventProcessorWithJsonlOutputTests.kt` |
| 770 | `exec.lib_tests` | `exec.src.LibTests` | 0 | 25 | 0 | 25 | `exec/src/lib_tests.rs` | `exec/src/LibTests.kt` |
| 771 | `exec.main` | `exec.src.Main` | 0 | 1 | 1 | 2 | `exec/src/main.rs` | `exec/src/Main.kt` |
| 772 | `exec.main_tests` | `exec.src.MainTests` | 0 | 1 | 0 | 1 | `exec/src/main_tests.rs` | `exec/src/MainTests.kt` |
| 773 | `exec.tests.all` | `exec.tests.All` | 0 | 0 | 0 | 0 | `exec/tests/all.rs` | `exec/tests/All.kt` |
| 774 | `chatgpt.chatgpt_client` | `chatgpt.src.ChatgptClient` | 0 | 2 | 0 | 2 | `chatgpt/src/chatgpt_client.rs` | `chatgpt/src/ChatgptClient.kt` |
| 775 | `suite.add_dir` | `exec.tests.suite.AddDir` | 0 | 2 | 0 | 2 | `exec/tests/suite/add_dir.rs` | `exec/tests/suite/AddDir.kt` |
| 776 | `suite.apply_patch` | `exec.tests.suite.ApplyPatch` | 0 | 3 | 0 | 3 | `exec/tests/suite/apply_patch.rs` | `exec/tests/suite/ApplyPatch.kt` |
| 777 | `suite.auth_env` | `exec.tests.suite.AuthEnv` | 0 | 1 | 0 | 1 | `exec/tests/suite/auth_env.rs` | `exec/tests/suite/AuthEnv.kt` |
| 778 | `suite.ephemeral` | `exec.tests.suite.Ephemeral` | 0 | 3 | 0 | 3 | `exec/tests/suite/ephemeral.rs` | `exec/tests/suite/Ephemeral.kt` |
| 779 | `suite.mcp_required_exit` | `exec.tests.suite.McpRequiredExit` | 0 | 1 | 0 | 1 | `exec/tests/suite/mcp_required_exit.rs` | `exec/tests/suite/McpRequiredExit.kt` |
| 780 | `backend-client.client` | `backendclient.src.Client` | 0 | 42 | 5 | 47 | `backend-client/src/client.rs` | `backendclient/src/Client.kt` |
| 781 | `suite.output_schema` | `exec.tests.suite.OutputSchema` | 0 | 1 | 0 | 1 | `exec/tests/suite/output_schema.rs` | `exec/tests/suite/OutputSchema.kt` |
| 782 | `suite.prompt_stdin` | `exec.tests.suite.PromptStdin` | 0 | 6 | 0 | 6 | `exec/tests/suite/prompt_stdin.rs` | `exec/tests/suite/PromptStdin.kt` |
| 783 | `exec.tests.suite.resume` | `exec.tests.suite.Resume` | 0 | 12 | 0 | 12 | `exec/tests/suite/resume.rs` | `exec/tests/suite/Resume.kt` |
| 784 | `suite.sandbox` | `exec.tests.suite.Sandbox` | 0 | 11 | 0 | 11 | `exec/tests/suite/sandbox.rs` | `exec/tests/suite/Sandbox.kt` |
| 785 | `suite.server_error_exit` | `exec.tests.suite.ServerErrorExit` | 0 | 1 | 0 | 1 | `exec/tests/suite/server_error_exit.rs` | `exec/tests/suite/ServerErrorExit.kt` |
| 786 | `execpolicy-legacy.build` | `execpolicylegacy.Build` | 0 | 1 | 0 | 1 | `execpolicy-legacy/build.rs` | `execpolicylegacy/Build.kt` |
| 787 | `aws-auth.signing` | `awsauth.src.Signing` | 0 | 2 | 0 | 2 | `aws-auth/src/signing.rs` | `awsauth/src/Signing.kt` |
| 788 | `execpolicy-legacy.arg_resolver` | `execpolicylegacy.src.ArgResolver` | 0 | 3 | 2 | 5 | `execpolicy-legacy/src/arg_resolver.rs` | `execpolicylegacy/src/ArgResolver.kt` |
| 789 | `suite.scenarios` | `applypatch.tests.suite.Scenarios` | 0 | 5 | 1 | 6 | `apply-patch/tests/suite/scenarios.rs` | `applypatch/tests/suite/Scenarios.kt` |
| 790 | `suite.cli` | `applypatch.tests.suite.Cli` | 0 | 3 | 0 | 3 | `apply-patch/tests/suite/cli.rs` | `applypatch/tests/suite/Cli.kt` |
| 791 | `apply-patch.tests.all` | `applypatch.tests.All` | 0 | 0 | 0 | 0 | `apply-patch/tests/all.rs` | `applypatch/tests/All.kt` |
| 792 | `execpolicy-legacy.main` | `execpolicylegacy.src.Main` | 0 | 4 | 5 | 9 | `execpolicy-legacy/src/main.rs` | `execpolicylegacy/src/Main.kt` |
| 793 | `apply-patch.streaming_parser` | `applypatch.src.StreamingParser` | 0 | 13 | 3 | 16 | `apply-patch/src/streaming_parser.rs` | `applypatch/src/StreamingParser.kt` |
| 794 | `execpolicy-legacy.policy` | `execpolicylegacy.src.Policy` | 0 | 4 | 1 | 5 | `execpolicy-legacy/src/policy.rs` | `execpolicylegacy/src/Policy.kt` |
| 795 | `apply-patch.standalone_executable` | `applypatch.src.StandaloneExecutable` | 0 | 2 | 0 | 2 | `apply-patch/src/standalone_executable.rs` | `applypatch/src/StandaloneExecutable.kt` |
| 796 | `execpolicy-legacy.program` | `execpolicylegacy.src.Program` | 0 | 4 | 5 | 9 | `execpolicy-legacy/src/program.rs` | `execpolicylegacy/src/Program.kt` |
| 797 | `execpolicy-legacy.sed_command` | `execpolicylegacy.src.SedCommand` | 0 | 1 | 0 | 1 | `execpolicy-legacy/src/sed_command.rs` | `execpolicylegacy/src/SedCommand.kt` |
| 798 | `apply-patch.seek_sequence` | `applypatch.src.SeekSequence` | 0 | 7 | 0 | 7 | `apply-patch/src/seek_sequence.rs` | `applypatch/src/SeekSequence.kt` |
| 799 | `execpolicy-legacy.tests.all` | `execpolicylegacy.tests.All` | 0 | 0 | 0 | 0 | `execpolicy-legacy/tests/all.rs` | `execpolicylegacy/tests/All.kt` |
| 800 | `suite.bad` | `execpolicylegacy.tests.suite.Bad` | 0 | 1 | 0 | 1 | `execpolicy-legacy/tests/suite/bad.rs` | `execpolicylegacy/tests/suite/Bad.kt` |
| 801 | `suite.cp` | `execpolicylegacy.tests.suite.Cp` | 0 | 5 | 0 | 5 | `execpolicy-legacy/tests/suite/cp.rs` | `execpolicylegacy/tests/suite/Cp.kt` |
| 802 | `suite.good` | `execpolicylegacy.tests.suite.Good` | 0 | 1 | 0 | 1 | `execpolicy-legacy/tests/suite/good.rs` | `execpolicylegacy/tests/suite/Good.kt` |
| 803 | `suite.head` | `execpolicylegacy.tests.suite.Head` | 0 | 8 | 0 | 8 | `execpolicy-legacy/tests/suite/head.rs` | `execpolicylegacy/tests/suite/Head.kt` |
| 804 | `suite.literal` | `execpolicylegacy.tests.suite.Literal` | 0 | 1 | 0 | 1 | `execpolicy-legacy/tests/suite/literal.rs` | `execpolicylegacy/tests/suite/Literal.kt` |
| 805 | `suite.ls` | `execpolicylegacy.tests.suite.Ls` | 0 | 9 | 0 | 9 | `execpolicy-legacy/tests/suite/ls.rs` | `execpolicylegacy/tests/suite/Ls.kt` |
| 806 | `apply-patch.parser` | `applypatch.src.Parser` | 0 | 15 | 4 | 19 | `apply-patch/src/parser.rs` | `applypatch/src/Parser.kt` |
| 807 | `suite.pwd` | `execpolicylegacy.tests.suite.Pwd` | 0 | 5 | 0 | 5 | `execpolicy-legacy/tests/suite/pwd.rs` | `execpolicylegacy/tests/suite/Pwd.kt` |
| 808 | `suite.sed` | `execpolicylegacy.tests.suite.Sed` | 0 | 5 | 0 | 5 | `execpolicy-legacy/tests/suite/sed.rs` | `execpolicylegacy/tests/suite/Sed.kt` |
| 809 | `execpolicy.amend` | `execpolicy.src.Amend` | 0 | 10 | 1 | 11 | `execpolicy/src/amend.rs` | `execpolicy/src/Amend.kt` |
| 810 | `execpolicy.execpolicycheck` | `execpolicy.src.Execpolicycheck` | 0 | 3 | 2 | 5 | `execpolicy/src/execpolicycheck.rs` | `execpolicy/src/Execpolicycheck.kt` |
| 811 | `execpolicy.executable_name` | `execpolicy.src.ExecutableName` | 0 | 2 | 0 | 2 | `execpolicy/src/executable_name.rs` | `execpolicy/src/ExecutableName.kt` |
| 812 | `execpolicy.main` | `execpolicy.src.Main` | 0 | 1 | 1 | 2 | `execpolicy/src/main.rs` | `execpolicy/src/Main.kt` |
| 813 | `execpolicy.parser` | `execpolicy.src.Parser` | 0 | 27 | 3 | 30 | `execpolicy/src/parser.rs` | `execpolicy/src/Parser.kt` |
| 814 | `tests.basic` | `execpolicy.tests.Basic` | 0 | 35 | 1 | 36 | `execpolicy/tests/basic.rs` | `execpolicy/tests/Basic.kt` |
| 815 | `external-agent-sessions.detect` | `externalagentsessions.src.Detect` | 0 | 15 | 1 | 16 | `external-agent-sessions/src/detect.rs` | `externalagentsessions/src/Detect.kt` |
| 816 | `external-agent-sessions.export` | `externalagentsessions.src.Export` | 0 | 17 | 0 | 17 | `external-agent-sessions/src/export.rs` | `externalagentsessions/src/Export.kt` |
| 817 | `external-agent-sessions.ledger` | `externalagentsessions.src.Ledger` | 0 | 8 | 2 | 10 | `external-agent-sessions/src/ledger.rs` | `externalagentsessions/src/Ledger.kt` |
| 818 | `external-agent-sessions.records` | `externalagentsessions.src.Records` | 0 | 19 | 2 | 21 | `external-agent-sessions/src/records.rs` | `externalagentsessions/src/Records.kt` |
| 819 | `features.feature_configs` | `features.src.FeatureConfigs` | 0 | 4 | 2 | 6 | `features/src/feature_configs.rs` | `features/src/FeatureConfigs.kt` |
| 820 | `features.legacy` | `features.src.Legacy` | 0 | 6 | 2 | 8 | `features/src/legacy.rs` | `features/src/Legacy.kt` |
| 821 | `features.tests` | `features.src.Tests` | 0 | 40 | 0 | 40 | `features/src/tests.rs` | `features/src/Tests.kt` |
| 822 | `apply-patch.main` | `applypatch.src.Main` | 0 | 1 | 0 | 1 | `apply-patch/src/main.rs` | `applypatch/src/Main.kt` |
| 823 | `file-search.cli` | `filesearch.src.Cli` | 0 | 0 | 1 | 1 | `file-search/src/cli.rs` | `filesearch/src/Cli.kt` |
| 824 | `file-search.main` | `filesearch.src.Main` | 0 | 4 | 1 | 5 | `file-search/src/main.rs` | `filesearch/src/Main.kt` |
| 825 | `git-utils.apply` | `gitutils.src.Apply` | 0 | 29 | 2 | 31 | `git-utils/src/apply.rs` | `gitutils/src/Apply.kt` |
| 826 | `git-utils.baseline` | `gitutils.src.Baseline` | 0 | 38 | 4 | 42 | `git-utils/src/baseline.rs` | `gitutils/src/Baseline.kt` |
| 827 | `git-utils.branch` | `gitutils.src.Branch` | 0 | 10 | 0 | 10 | `git-utils/src/branch.rs` | `gitutils/src/Branch.kt` |
| 828 | `git-utils.errors` | `gitutils.src.Errors` | 0 | 0 | 1 | 1 | `git-utils/src/errors.rs` | `gitutils/src/Errors.kt` |
| 829 | `apply-patch.invocation` | `applypatch.src.Invocation` | 0 | 48 | 3 | 51 | `apply-patch/src/invocation.rs` | `applypatch/src/Invocation.kt` |
| 830 | `git-utils.operations` | `gitutils.src.Operations` | 0 | 7 | 1 | 8 | `git-utils/src/operations.rs` | `gitutils/src/Operations.kt` |
| 831 | `git-utils.platform` | `gitutils.src.Platform` | 0 | 2 | 0 | 2 | `git-utils/src/platform.rs` | `gitutils/src/Platform.kt` |
| 832 | `bin.write_hooks_schema_fixtures` | `hooks.src.bin.WriteHooksSchemaFixtures` | 0 | 1 | 0 | 1 | `hooks/src/bin/write_hooks_schema_fixtures.rs` | `hooks/src/bin/WriteHooksSchemaFixtures.kt` |
| 833 | `v2.windows_sandbox_setup` | `appserver.tests.suite.v2.WindowsSandboxSetup` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/windows_sandbox_setup.rs` | `appserver/tests/suite/v2/WindowsSandboxSetup.kt` |
| 834 | `engine.command_runner` | `hooks.src.engine.CommandRunner` | 0 | 3 | 1 | 4 | `hooks/src/engine/command_runner.rs` | `hooks/src/engine/CommandRunner.kt` |
| 835 | `engine.discovery` | `hooks.src.engine.Discovery` | 0 | 24 | 2 | 26 | `hooks/src/engine/discovery.rs` | `hooks/src/engine/Discovery.kt` |
| 836 | `v2.turn_steer` | `appserver.tests.suite.v2.TurnSteer` | 0 | 3 | 0 | 3 | `app-server/tests/suite/v2/turn_steer.rs` | `appserver/tests/suite/v2/TurnSteer.kt` |
| 837 | `engine.mod_tests` | `hooks.src.engine.ModTests` | 0 | 13 | 0 | 13 | `hooks/src/engine/mod_tests.rs` | `hooks/src/engine/ModTests.kt` |
| 838 | `v2.turn_start_zsh_fork` | `appserver.tests.suite.v2.TurnStartZshFork` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/turn_start_zsh_fork.rs` | `appserver/tests/suite/v2/TurnStartZshFork.kt` |
| 839 | `engine.schema_loader` | `hooks.src.engine.SchemaLoader` | 0 | 3 | 1 | 4 | `hooks/src/engine/schema_loader.rs` | `hooks/src/engine/SchemaLoader.kt` |
| 840 | `events.common` | `hooks.src.events.Common` | 0 | 25 | 0 | 25 | `hooks/src/events/common.rs` | `hooks/src/events/Common.kt` |
| 841 | `events.permission_request` | `hooks.src.events.PermissionRequest` | 0 | 8 | 4 | 12 | `hooks/src/events/permission_request.rs` | `hooks/src/events/PermissionRequest.kt` |
| 842 | `events.post_tool_use` | `hooks.src.events.PostToolUse` | 0 | 17 | 3 | 20 | `hooks/src/events/post_tool_use.rs` | `hooks/src/events/PostToolUse.kt` |
| 843 | `events.pre_tool_use` | `hooks.src.events.PreToolUse` | 0 | 19 | 3 | 22 | `hooks/src/events/pre_tool_use.rs` | `hooks/src/events/PreToolUse.kt` |
| 844 | `events.session_start` | `hooks.src.events.SessionStart` | 0 | 10 | 4 | 14 | `hooks/src/events/session_start.rs` | `hooks/src/events/SessionStart.kt` |
| 845 | `events.stop` | `hooks.src.events.Stop` | 0 | 15 | 3 | 18 | `hooks/src/events/stop.rs` | `hooks/src/events/Stop.kt` |
| 846 | `events.user_prompt_submit` | `hooks.src.events.UserPromptSubmit` | 0 | 10 | 3 | 13 | `hooks/src/events/user_prompt_submit.rs` | `hooks/src/events/UserPromptSubmit.kt` |
| 847 | `hooks.legacy_notify` | `hooks.src.LegacyNotify` | 0 | 5 | 1 | 6 | `hooks/src/legacy_notify.rs` | `hooks/src/LegacyNotify.kt` |
| 848 | `v2.turn_start` | `appserver.tests.suite.v2.TurnStart` | 0 | 35 | 1 | 36 | `app-server/tests/suite/v2/turn_start.rs` | `appserver/tests/suite/v2/TurnStart.kt` |
| 849 | `v2.turn_interrupt` | `appserver.tests.suite.v2.TurnInterrupt` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/turn_interrupt.rs` | `appserver/tests/suite/v2/TurnInterrupt.kt` |
| 850 | `hooks.types` | `hooks.src.Types` | 0 | 6 | 11 | 17 | `hooks/src/types.rs` | `hooks/src/Types.kt` |
| 851 | `linux-sandbox.build` | `linuxsandbox.Build` | 0 | 3 | 0 | 3 | `linux-sandbox/build.rs` | `linuxsandbox/Build.kt` |
| 852 | `linux-sandbox.bwrap` | `linuxsandbox.src.Bwrap` | 0 | 85 | 8 | 93 | `linux-sandbox/src/bwrap.rs` | `linuxsandbox/src/Bwrap.kt` |
| 853 | `linux-sandbox.landlock` | `linuxsandbox.src.Landlock` | 0 | 13 | 1 | 14 | `linux-sandbox/src/landlock.rs` | `linuxsandbox/src/Landlock.kt` |
| 854 | `linux-sandbox.launcher` | `linuxsandbox.src.Launcher` | 0 | 17 | 3 | 20 | `linux-sandbox/src/launcher.rs` | `linuxsandbox/src/Launcher.kt` |
| 855 | `linux-sandbox.linux_run_main` | `linuxsandbox.src.LinuxRunMain` | 0 | 61 | 11 | 72 | `linux-sandbox/src/linux_run_main.rs` | `linuxsandbox/src/LinuxRunMain.kt` |
| 856 | `linux-sandbox.linux_run_main_tests` | `linuxsandbox.src.LinuxRunMainTests` | 0 | 33 | 0 | 33 | `linux-sandbox/src/linux_run_main_tests.rs` | `linuxsandbox/src/LinuxRunMainTests.kt` |
| 857 | `linux-sandbox.main` | `linuxsandbox.src.Main` | 0 | 1 | 0 | 1 | `linux-sandbox/src/main.rs` | `linuxsandbox/src/Main.kt` |
| 858 | `linux-sandbox.proxy_routing` | `linuxsandbox.src.ProxyRouting` | 0 | 38 | 4 | 42 | `linux-sandbox/src/proxy_routing.rs` | `linuxsandbox/src/ProxyRouting.kt` |
| 859 | `linux-sandbox.vendored_bwrap` | `linuxsandbox.src.VendoredBwrap` | 0 | 5 | 0 | 5 | `linux-sandbox/src/vendored_bwrap.rs` | `linuxsandbox/src/VendoredBwrap.kt` |
| 860 | `linux-sandbox.tests.all` | `linuxsandbox.tests.All` | 0 | 0 | 0 | 0 | `linux-sandbox/tests/all.rs` | `linuxsandbox/tests/All.kt` |
| 861 | `suite.landlock` | `linuxsandbox.tests.suite.Landlock` | 0 | 34 | 0 | 34 | `linux-sandbox/tests/suite/landlock.rs` | `linuxsandbox/tests/suite/Landlock.kt` |
| 862 | `suite.managed_proxy` | `linuxsandbox.tests.suite.ManagedProxy` | 0 | 10 | 0 | 10 | `linux-sandbox/tests/suite/managed_proxy.rs` | `linuxsandbox/tests/suite/ManagedProxy.kt` |
| 863 | `auth.agent_identity` | `login.src.auth.AgentIdentity` | 0 | 15 | 2 | 17 | `login/src/auth/agent_identity.rs` | `login/src/auth/AgentIdentity.kt` |
| 864 | `auth.auth_tests` | `login.src.auth.AuthTests` | 0 | 45 | 4 | 49 | `login/src/auth/auth_tests.rs` | `login/src/auth/AuthTests.kt` |
| 865 | `v2.thread_unsubscribe` | `appserver.tests.suite.v2.ThreadUnsubscribe` | 0 | 7 | 0 | 7 | `app-server/tests/suite/v2/thread_unsubscribe.rs` | `appserver/tests/suite/v2/ThreadUnsubscribe.kt` |
| 866 | `auth.default_client_tests` | `login.src.auth.DefaultClientTests` | 0 | 7 | 0 | 7 | `login/src/auth/default_client_tests.rs` | `login/src/auth/DefaultClientTests.kt` |
| 867 | `auth.error` | `login.src.auth.Error` | 0 | 0 | 0 | 0 | `login/src/auth/error.rs` | `login/src/auth/Error.kt` |
| 868 | `auth.external_bearer` | `login.src.auth.ExternalBearer` | 0 | 8 | 3 | 11 | `login/src/auth/external_bearer.rs` | `login/src/auth/ExternalBearer.kt` |
| 869 | `auth.manager` | `login.src.auth.Manager` | 0 | 105 | 23 | 128 | `login/src/auth/manager.rs` | `login/src/auth/Manager.kt` |
| 870 | `auth.revoke` | `login.src.auth.Revoke` | 0 | 10 | 2 | 12 | `login/src/auth/revoke.rs` | `login/src/auth/Revoke.kt` |
| 871 | `auth.storage` | `login.src.auth.Storage` | 0 | 27 | 7 | 34 | `login/src/auth/storage.rs` | `login/src/auth/Storage.kt` |
| 872 | `auth.storage_tests` | `login.src.auth.StorageTests` | 0 | 22 | 1 | 23 | `login/src/auth/storage_tests.rs` | `login/src/auth/StorageTests.kt` |
| 873 | `auth.util` | `login.src.auth.Util` | 0 | 3 | 0 | 3 | `login/src/auth/util.rs` | `login/src/auth/Util.kt` |
| 874 | `v2.thread_unarchive` | `appserver.tests.suite.v2.ThreadUnarchive` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/thread_unarchive.rs` | `appserver/tests/suite/v2/ThreadUnarchive.kt` |
| 875 | `v2.thread_status` | `appserver.tests.suite.v2.ThreadStatus` | 0 | 3 | 0 | 3 | `app-server/tests/suite/v2/thread_status.rs` | `appserver/tests/suite/v2/ThreadStatus.kt` |
| 876 | `v2.thread_start` | `appserver.tests.suite.v2.ThreadStart` | 0 | 25 | 0 | 25 | `app-server/tests/suite/v2/thread_start.rs` | `appserver/tests/suite/v2/ThreadStart.kt` |
| 877 | `login.token_data_tests` | `login.src.TokenDataTests` | 0 | 12 | 1 | 13 | `login/src/token_data_tests.rs` | `login/src/TokenDataTests.kt` |
| 878 | `login.tests.all` | `login.tests.All` | 0 | 0 | 0 | 0 | `login/tests/all.rs` | `login/tests/All.kt` |
| 879 | `suite.auth_refresh` | `login.tests.suite.AuthRefresh` | 0 | 25 | 3 | 28 | `login/tests/suite/auth_refresh.rs` | `login/tests/suite/AuthRefresh.kt` |
| 880 | `suite.device_code_login` | `login.tests.suite.DeviceCodeLogin` | 0 | 12 | 0 | 12 | `login/tests/suite/device_code_login.rs` | `login/tests/suite/DeviceCodeLogin.kt` |
| 881 | `suite.login_server_e2e` | `login.tests.suite.LoginServerE2e` | 0 | 8 | 1 | 9 | `login/tests/suite/login_server_e2e.rs` | `login/tests/suite/LoginServerE2e.kt` |
| 882 | `v2.thread_shell_command` | `appserver.tests.suite.v2.ThreadShellCommand` | 0 | 9 | 0 | 9 | `app-server/tests/suite/v2/thread_shell_command.rs` | `appserver/tests/suite/v2/ThreadShellCommand.kt` |
| 883 | `mcp-server.codex_tool_runner` | `mcpserver.src.CodexToolRunner` | 0 | 5 | 0 | 5 | `mcp-server/src/codex_tool_runner.rs` | `mcpserver/src/CodexToolRunner.kt` |
| 884 | `mcp-server.exec_approval` | `mcpserver.src.ExecApproval` | 0 | 2 | 2 | 4 | `mcp-server/src/exec_approval.rs` | `mcpserver/src/ExecApproval.kt` |
| 885 | `config.mcp_edit` | `config.src.McpEdit` | 0 | 12 | 1 | 13 | `config/src/mcp_edit.rs` | `config/src/McpEdit.kt` |
| 886 | `v2.thread_rollback` | `appserver.tests.suite.v2.ThreadRollback` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/thread_rollback.rs` | `appserver/tests/suite/v2/ThreadRollback.kt` |
| 887 | `v2.thread_resume` | `appserver.tests.suite.v2.ThreadResume` | 0 | 39 | 2 | 41 | `app-server/tests/suite/v2/thread_resume.rs` | `appserver/tests/suite/v2/ThreadResume.kt` |
| 888 | `mcp-server.patch_approval` | `mcpserver.src.PatchApproval` | 0 | 2 | 2 | 4 | `mcp-server/src/patch_approval.rs` | `mcpserver/src/PatchApproval.kt` |
| 889 | `mcp-server.tests.all` | `mcpserver.tests.All` | 0 | 0 | 0 | 0 | `mcp-server/tests/all.rs` | `mcpserver/tests/All.kt` |
| 890 | `v2.thread_read` | `appserver.tests.suite.v2.ThreadRead` | 0 | 22 | 1 | 23 | `app-server/tests/suite/v2/thread_read.rs` | `appserver/tests/suite/v2/ThreadRead.kt` |
| 891 | `common.mock_model_server` | `mcpserver.tests.common.MockModelServer` | 0 | 2 | 1 | 3 | `mcp-server/tests/common/mock_model_server.rs` | `mcpserver/tests/common/MockModelServer.kt` |
| 892 | `suite.codex_tool` | `mcpserver.tests.suite.CodexTool` | 0 | 10 | 1 | 11 | `mcp-server/tests/suite/codex_tool.rs` | `mcpserver/tests/suite/CodexTool.kt` |
| 893 | `v2.thread_name_websocket` | `appserver.tests.suite.v2.ThreadNameWebsocket` | 0 | 7 | 0 | 7 | `app-server/tests/suite/v2/thread_name_websocket.rs` | `appserver/tests/suite/v2/ThreadNameWebsocket.kt` |
| 894 | `mcp.local` | `memories.mcp.src.Local` | 0 | 17 | 2 | 19 | `memories/mcp/src/local.rs` | `memories/mcp/src/Local.kt` |
| 895 | `mcp.local_tests` | `memories.mcp.src.LocalTests` | 0 | 27 | 0 | 27 | `memories/mcp/src/local_tests.rs` | `memories/mcp/src/LocalTests.kt` |
| 896 | `mcp.schema` | `memories.mcp.src.Schema` | 0 | 3 | 0 | 3 | `memories/mcp/src/schema.rs` | `memories/mcp/src/Schema.kt` |
| 897 | `mcp.server` | `memories.mcp.src.Server` | 0 | 15 | 4 | 19 | `memories/mcp/src/server.rs` | `memories/mcp/src/Server.kt` |
| 898 | `read.citations` | `memories.read.src.Citations` | 0 | 5 | 0 | 5 | `memories/read/src/citations.rs` | `memories/read/src/Citations.kt` |
| 899 | `read.citations_tests` | `memories.read.src.CitationsTests` | 0 | 3 | 0 | 3 | `memories/read/src/citations_tests.rs` | `memories/read/src/CitationsTests.kt` |
| 900 | `read.metrics` | `memories.read.src.Metrics` | 0 | 0 | 0 | 0 | `memories/read/src/metrics.rs` | `memories/read/src/Metrics.kt` |
| 901 | `read.prompts` | `memories.read.src.Prompts` | 0 | 2 | 0 | 2 | `memories/read/src/prompts.rs` | `memories/read/src/Prompts.kt` |
| 902 | `read.prompts_tests` | `memories.read.src.PromptsTests` | 0 | 1 | 0 | 1 | `memories/read/src/prompts_tests.rs` | `memories/read/src/PromptsTests.kt` |
| 903 | `v2.thread_metadata_update` | `appserver.tests.suite.v2.ThreadMetadataUpdate` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/thread_metadata_update.rs` | `appserver/tests/suite/v2/ThreadMetadataUpdate.kt` |
| 904 | `write.control` | `memories.write.src.Control` | 0 | 4 | 0 | 4 | `memories/write/src/control.rs` | `memories/write/src/Control.kt` |
| 905 | `extensions.ad_hoc` | `memories.write.src.extensions.AdHoc` | 0 | 1 | 0 | 1 | `memories/write/src/extensions/ad_hoc.rs` | `memories/write/src/extensions/AdHoc.kt` |
| 906 | `extensions.ad_hoc_tests` | `memories.write.src.extensions.AdHocTests` | 0 | 1 | 0 | 1 | `memories/write/src/extensions/ad_hoc_tests.rs` | `memories/write/src/extensions/AdHocTests.kt` |
| 907 | `extensions.prune` | `memories.write.src.extensions.Prune` | 0 | 3 | 0 | 3 | `memories/write/src/extensions/prune.rs` | `memories/write/src/extensions/Prune.kt` |
| 908 | `extensions.prune_tests` | `memories.write.src.extensions.PruneTests` | 0 | 2 | 0 | 2 | `memories/write/src/extensions/prune_tests.rs` | `memories/write/src/extensions/PruneTests.kt` |
| 909 | `v2.thread_memory_mode_set` | `appserver.tests.suite.v2.ThreadMemoryModeSet` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/thread_memory_mode_set.rs` | `appserver/tests/suite/v2/ThreadMemoryModeSet.kt` |
| 910 | `write.guard_tests` | `memories.write.src.GuardTests` | 0 | 5 | 0 | 5 | `memories/write/src/guard_tests.rs` | `memories/write/src/GuardTests.kt` |
| 911 | `write.metrics` | `memories.write.src.Metrics` | 0 | 0 | 0 | 0 | `memories/write/src/metrics.rs` | `memories/write/src/Metrics.kt` |
| 912 | `v2.thread_loaded_list` | `appserver.tests.suite.v2.ThreadLoadedList` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/thread_loaded_list.rs` | `appserver/tests/suite/v2/ThreadLoadedList.kt` |
| 913 | `v2.thread_list` | `appserver.tests.suite.v2.ThreadList` | 0 | 32 | 0 | 32 | `app-server/tests/suite/v2/thread_list.rs` | `appserver/tests/suite/v2/ThreadList.kt` |
| 914 | `write.prompts` | `memories.write.src.Prompts` | 0 | 4 | 0 | 4 | `memories/write/src/prompts.rs` | `memories/write/src/Prompts.kt` |
| 915 | `write.prompts_tests` | `memories.write.src.PromptsTests` | 0 | 3 | 0 | 3 | `memories/write/src/prompts_tests.rs` | `memories/write/src/PromptsTests.kt` |
| 916 | `write.runtime` | `memories.write.src.Runtime` | 0 | 13 | 3 | 16 | `memories/write/src/runtime.rs` | `memories/write/src/Runtime.kt` |
| 917 | `write.start` | `memories.write.src.Start` | 0 | 1 | 0 | 1 | `memories/write/src/start.rs` | `memories/write/src/Start.kt` |
| 918 | `write.startup_tests` | `memories.write.src.StartupTests` | 0 | 17 | 0 | 17 | `memories/write/src/startup_tests.rs` | `memories/write/src/StartupTests.kt` |
| 919 | `write.storage_tests` | `memories.write.src.StorageTests` | 0 | 6 | 0 | 6 | `memories/write/src/storage_tests.rs` | `memories/write/src/StorageTests.kt` |
| 920 | `write.workspace` | `memories.write.src.Workspace` | 0 | 8 | 0 | 8 | `memories/write/src/workspace.rs` | `memories/write/src/Workspace.kt` |
| 921 | `write.workspace_tests` | `memories.write.src.WorkspaceTests` | 0 | 4 | 0 | 4 | `memories/write/src/workspace_tests.rs` | `memories/write/src/WorkspaceTests.kt` |
| 922 | `model-provider-info.model_provider_info_tests` | `modelproviderinfo.src.ModelProviderInfoTests` | 0 | 19 | 0 | 19 | `model-provider-info/src/model_provider_info_tests.rs` | `modelproviderinfo/src/ModelProviderInfoTests.kt` |
| 923 | `amazon_bedrock.auth` | `modelprovider.src.amazonbedrock.Auth` | 0 | 13 | 2 | 15 | `model-provider/src/amazon_bedrock/auth.rs` | `modelprovider/src/amazonbedrock/Auth.kt` |
| 924 | `amazon_bedrock.catalog` | `modelprovider.src.amazonbedrock.Catalog` | 0 | 7 | 0 | 7 | `model-provider/src/amazon_bedrock/catalog.rs` | `modelprovider/src/amazonbedrock/Catalog.kt` |
| 925 | `amazon_bedrock.mantle` | `modelprovider.src.amazonbedrock.Mantle` | 0 | 9 | 0 | 9 | `model-provider/src/amazon_bedrock/mantle.rs` | `modelprovider/src/amazonbedrock/Mantle.kt` |
| 926 | `v2.thread_inject_items` | `appserver.tests.suite.v2.ThreadInjectItems` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/thread_inject_items.rs` | `appserver/tests/suite/v2/ThreadInjectItems.kt` |
| 927 | `model-provider.models_endpoint` | `modelprovider.src.ModelsEndpoint` | 0 | 10 | 2 | 12 | `model-provider/src/models_endpoint.rs` | `modelprovider/src/ModelsEndpoint.kt` |
| 928 | `models-manager.cache` | `modelsmanager.src.Cache` | 0 | 10 | 2 | 12 | `models-manager/src/cache.rs` | `modelsmanager/src/Cache.kt` |
| 929 | `v2.thread_fork` | `appserver.tests.suite.v2.ThreadFork` | 0 | 12 | 0 | 12 | `app-server/tests/suite/v2/thread_fork.rs` | `appserver/tests/suite/v2/ThreadFork.kt` |
| 930 | `models-manager.collaboration_mode_presets_tests` | `modelsmanager.src.CollaborationModePresetsTests` | 0 | 2 | 0 | 2 | `models-manager/src/collaboration_mode_presets_tests.rs` | `modelsmanager/src/CollaborationModePresetsTests.kt` |
| 931 | `models-manager.config` | `modelsmanager.src.Config` | 0 | 0 | 1 | 1 | `models-manager/src/config.rs` | `modelsmanager/src/Config.kt` |
| 932 | `models-manager.manager` | `modelsmanager.src.Manager` | 0 | 32 | 7 | 39 | `models-manager/src/manager.rs` | `modelsmanager/src/Manager.kt` |
| 933 | `models-manager.manager_tests` | `modelsmanager.src.ManagerTests` | 0 | 39 | 4 | 43 | `models-manager/src/manager_tests.rs` | `modelsmanager/src/ManagerTests.kt` |
| 934 | `v2.thread_archive` | `appserver.tests.suite.v2.ThreadArchive` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/thread_archive.rs` | `appserver/tests/suite/v2/ThreadArchive.kt` |
| 935 | `models-manager.model_info_overrides_tests` | `modelsmanager.src.ModelInfoOverridesTests` | 0 | 2 | 0 | 2 | `models-manager/src/model_info_overrides_tests.rs` | `modelsmanager/src/ModelInfoOverridesTests.kt` |
| 936 | `models-manager.model_info_tests` | `modelsmanager.src.ModelInfoTests` | 0 | 5 | 0 | 5 | `models-manager/src/model_info_tests.rs` | `modelsmanager/src/ModelInfoTests.kt` |
| 937 | `models-manager.test_support` | `modelsmanager.src.TestSupport` | 0 | 2 | 0 | 2 | `models-manager/src/test_support.rs` | `modelsmanager/src/TestSupport.kt` |
| 938 | `network-proxy.certs` | `networkproxy.src.Certs` | 0 | 14 | 1 | 15 | `network-proxy/src/certs.rs` | `networkproxy/src/Certs.kt` |
| 939 | `network-proxy.config` | `networkproxy.src.Config` | 0 | 52 | 12 | 64 | `network-proxy/src/config.rs` | `networkproxy/src/Config.kt` |
| 940 | `network-proxy.connect_policy` | `networkproxy.src.ConnectPolicy` | 0 | 7 | 5 | 12 | `network-proxy/src/connect_policy.rs` | `networkproxy/src/ConnectPolicy.kt` |
| 941 | `v2.skills_list` | `appserver.tests.suite.v2.SkillsList` | 0 | 14 | 0 | 14 | `app-server/tests/suite/v2/skills_list.rs` | `appserver/tests/suite/v2/SkillsList.kt` |
| 942 | `v2.safety_check_downgrade` | `appserver.tests.suite.v2.SafetyCheckDowngrade` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/safety_check_downgrade.rs` | `appserver/tests/suite/v2/SafetyCheckDowngrade.kt` |
| 943 | `network-proxy.mitm_tests` | `networkproxy.src.MitmTests` | 0 | 4 | 0 | 4 | `network-proxy/src/mitm_tests.rs` | `networkproxy/src/MitmTests.kt` |
| 944 | `network-proxy.network_policy` | `networkproxy.src.NetworkPolicy` | 0 | 52 | 13 | 65 | `network-proxy/src/network_policy.rs` | `networkproxy/src/NetworkPolicy.kt` |
| 945 | `network-proxy.policy` | `networkproxy.src.Policy` | 0 | 44 | 3 | 47 | `network-proxy/src/policy.rs` | `networkproxy/src/Policy.kt` |
| 946 | `network-proxy.proxy` | `networkproxy.src.Proxy` | 0 | 65 | 7 | 72 | `network-proxy/src/proxy.rs` | `networkproxy/src/Proxy.kt` |
| 947 | `network-proxy.reasons` | `networkproxy.src.Reasons` | 0 | 0 | 0 | 0 | `network-proxy/src/reasons.rs` | `networkproxy/src/Reasons.kt` |
| 948 | `network-proxy.runtime` | `networkproxy.src.Runtime` | 0 | 110 | 11 | 121 | `network-proxy/src/runtime.rs` | `networkproxy/src/Runtime.kt` |
| 949 | `v2.review` | `appserver.tests.suite.v2.Review` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/review.rs` | `appserver/tests/suite/v2/Review.kt` |
| 950 | `network-proxy.state` | `networkproxy.src.State` | 0 | 7 | 4 | 11 | `network-proxy/src/state.rs` | `networkproxy/src/State.kt` |
| 951 | `network-proxy.upstream` | `networkproxy.src.Upstream` | 0 | 14 | 4 | 18 | `network-proxy/src/upstream.rs` | `networkproxy/src/Upstream.kt` |
| 952 | `v2.request_user_input` | `appserver.tests.suite.v2.RequestUserInput` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/request_user_input.rs` | `appserver/tests/suite/v2/RequestUserInput.kt` |
| 953 | `v2.request_permissions` | `appserver.tests.suite.v2.RequestPermissions` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/request_permissions.rs` | `appserver/tests/suite/v2/RequestPermissions.kt` |
| 954 | `v2.realtime_conversation` | `appserver.tests.suite.v2.RealtimeConversation` | 0 | 61 | 9 | 70 | `app-server/tests/suite/v2/realtime_conversation.rs` | `appserver/tests/suite/v2/RealtimeConversation.kt` |
| 955 | `metrics.client` | `otel.src.metrics.Client` | 0 | 21 | 3 | 24 | `otel/src/metrics/client.rs` | `otel/src/metrics/Client.kt` |
| 956 | `metrics.config` | `otel.src.metrics.Config` | 0 | 5 | 2 | 7 | `otel/src/metrics/config.rs` | `otel/src/metrics/Config.kt` |
| 957 | `metrics.error` | `otel.src.metrics.Error` | 0 | 0 | 2 | 2 | `otel/src/metrics/error.rs` | `otel/src/metrics/Error.kt` |
| 958 | `metrics.names` | `otel.src.metrics.Names` | 0 | 0 | 0 | 0 | `otel/src/metrics/names.rs` | `otel/src/metrics/Names.kt` |
| 959 | `metrics.runtime_metrics` | `otel.src.metrics.RuntimeMetrics` | 0 | 11 | 2 | 13 | `otel/src/metrics/runtime_metrics.rs` | `otel/src/metrics/RuntimeMetrics.kt` |
| 960 | `metrics.tags` | `otel.src.metrics.Tags` | 0 | 4 | 1 | 5 | `otel/src/metrics/tags.rs` | `otel/src/metrics/Tags.kt` |
| 961 | `v2.rate_limits` | `appserver.tests.suite.v2.RateLimits` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/rate_limits.rs` | `appserver/tests/suite/v2/RateLimits.kt` |
| 962 | `metrics.validation` | `otel.src.metrics.Validation` | 0 | 7 | 0 | 7 | `otel/src/metrics/validation.rs` | `otel/src/metrics/Validation.kt` |
| 963 | `otel.otlp` | `otel.src.Otlp` | 0 | 12 | 0 | 12 | `otel/src/otlp.rs` | `otel/src/Otlp.kt` |
| 964 | `v2.plugin_uninstall` | `appserver.tests.suite.v2.PluginUninstall` | 0 | 15 | 0 | 15 | `app-server/tests/suite/v2/plugin_uninstall.rs` | `appserver/tests/suite/v2/PluginUninstall.kt` |
| 965 | `v2.plugin_share` | `appserver.tests.suite.v2.PluginShare` | 0 | 12 | 0 | 12 | `app-server/tests/suite/v2/plugin_share.rs` | `appserver/tests/suite/v2/PluginShare.kt` |
| 966 | `v2.plugin_read` | `appserver.tests.suite.v2.PluginRead` | 0 | 26 | 2 | 28 | `app-server/tests/suite/v2/plugin_read.rs` | `appserver/tests/suite/v2/PluginRead.kt` |
| 967 | `suite.manager_metrics` | `otel.tests.suite.ManagerMetrics` | 0 | 3 | 0 | 3 | `otel/tests/suite/manager_metrics.rs` | `otel/tests/suite/ManagerMetrics.kt` |
| 968 | `suite.otel_export_routing_policy` | `otel.tests.suite.OtelExportRoutingPolicy` | 0 | 12 | 0 | 12 | `otel/tests/suite/otel_export_routing_policy.rs` | `otel/tests/suite/OtelExportRoutingPolicy.kt` |
| 969 | `suite.otlp_http_loopback` | `otel.tests.suite.OtlpHttpLoopback` | 0 | 6 | 1 | 7 | `otel/tests/suite/otlp_http_loopback.rs` | `otel/tests/suite/OtlpHttpLoopback.kt` |
| 970 | `suite.runtime_summary` | `otel.tests.suite.RuntimeSummary` | 0 | 1 | 0 | 1 | `otel/tests/suite/runtime_summary.rs` | `otel/tests/suite/RuntimeSummary.kt` |
| 971 | `suite.send` | `otel.tests.suite.Send` | 0 | 5 | 0 | 5 | `otel/tests/suite/send.rs` | `otel/tests/suite/Send.kt` |
| 972 | `suite.snapshot` | `otel.tests.suite.Snapshot` | 0 | 2 | 0 | 2 | `otel/tests/suite/snapshot.rs` | `otel/tests/suite/Snapshot.kt` |
| 973 | `suite.timing` | `otel.tests.suite.Timing` | 0 | 2 | 0 | 2 | `otel/tests/suite/timing.rs` | `otel/tests/suite/Timing.kt` |
| 974 | `v2.plugin_list` | `appserver.tests.suite.v2.PluginList` | 0 | 38 | 0 | 38 | `app-server/tests/suite/v2/plugin_list.rs` | `appserver/tests/suite/v2/PluginList.kt` |
| 975 | `tests.tests` | `otel.tests.Tests` | 0 | 0 | 0 | 0 | `otel/tests/tests.rs` | `otel/tests/Tests.kt` |
| 976 | `plugin.load_outcome` | `plugin.src.LoadOutcome` | 0 | 13 | 3 | 16 | `plugin/src/load_outcome.rs` | `plugin/src/LoadOutcome.kt` |
| 977 | `v2.plugin_install` | `appserver.tests.suite.v2.PluginInstall` | 0 | 44 | 3 | 47 | `app-server/tests/suite/v2/plugin_install.rs` | `appserver/tests/suite/v2/PluginInstall.kt` |
| 978 | `plugin.plugin_namespace` | `plugin.src.PluginNamespace` | 0 | 3 | 1 | 4 | `plugin/src/plugin_namespace.rs` | `plugin/src/PluginNamespace.kt` |
| 979 | `v2.output_schema` | `appserver.tests.suite.v2.OutputSchema` | 0 | 3 | 0 | 3 | `app-server/tests/suite/v2/output_schema.rs` | `appserver/tests/suite/v2/OutputSchema.kt` |
| 980 | `protocol.dynamic_tools` | `protocol.src.DynamicTools` | 0 | 3 | 5 | 8 | `protocol/src/dynamic_tools.rs` | `protocol/src/DynamicTools.kt` |
| 981 | `v2.model_provider_capabilities_read` | `appserver.tests.suite.v2.ModelProviderCapabilitiesRead` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/model_provider_capabilities_read.rs` | `appserver/tests/suite/v2/ModelProviderCapabilitiesRead.kt` |
| 982 | `protocol.error_tests` | `protocol.src.ErrorTests` | 0 | 30 | 0 | 30 | `protocol/src/error_tests.rs` | `protocol/src/ErrorTests.kt` |
| 983 | `protocol.exec_output_tests` | `protocol.src.ExecOutputTests` | 0 | 9 | 0 | 9 | `protocol/src/exec_output_tests.rs` | `protocol/src/ExecOutputTests.kt` |
| 984 | `protocol.mcp` | `protocol.src.Mcp` | 0 | 9 | 9 | 18 | `protocol/src/mcp.rs` | `protocol/src/Mcp.kt` |
| 985 | `v2.model_list` | `appserver.tests.suite.v2.ModelList` | 0 | 6 | 0 | 6 | `app-server/tests/suite/v2/model_list.rs` | `appserver/tests/suite/v2/ModelList.kt` |
| 986 | `protocol.message_history` | `protocol.src.MessageHistory` | 0 | 0 | 1 | 1 | `protocol/src/message_history.rs` | `protocol/src/MessageHistory.kt` |
| 987 | `v2.memory_reset` | `appserver.tests.suite.v2.MemoryReset` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/memory_reset.rs` | `appserver/tests/suite/v2/MemoryReset.kt` |
| 988 | `protocol.network_policy` | `protocol.src.NetworkPolicy` | 0 | 1 | 1 | 2 | `protocol/src/network_policy.rs` | `protocol/src/NetworkPolicy.kt` |
| 989 | `protocol.openai_models` | `protocol.src.OpenaiModels` | 0 | 35 | 19 | 54 | `protocol/src/openai_models.rs` | `protocol/src/OpenaiModels.kt` |
| 990 | `v2.mcp_tool` | `appserver.tests.suite.v2.McpTool` | 0 | 9 | 1 | 10 | `app-server/tests/suite/v2/mcp_tool.rs` | `appserver/tests/suite/v2/McpTool.kt` |
| 991 | `protocol.plan_tool` | `protocol.src.PlanTool` | 0 | 0 | 3 | 3 | `protocol/src/plan_tool.rs` | `protocol/src/PlanTool.kt` |
| 992 | `v2.mcp_server_elicitation` | `appserver.tests.suite.v2.McpServerElicitation` | 0 | 7 | 2 | 9 | `app-server/tests/suite/v2/mcp_server_elicitation.rs` | `appserver/tests/suite/v2/McpServerElicitation.kt` |
| 993 | `protocol.request_permissions` | `protocol.src.RequestPermissions` | 0 | 3 | 5 | 8 | `protocol/src/request_permissions.rs` | `protocol/src/RequestPermissions.kt` |
| 994 | `protocol.request_user_input` | `protocol.src.RequestUserInput` | 0 | 0 | 6 | 6 | `protocol/src/request_user_input.rs` | `protocol/src/RequestUserInput.kt` |
| 995 | `v2.mcp_resource` | `appserver.tests.suite.v2.McpResource` | 0 | 7 | 1 | 8 | `app-server/tests/suite/v2/mcp_resource.rs` | `appserver/tests/suite/v2/McpResource.kt` |
| 996 | `v2.marketplace_upgrade` | `appserver.tests.suite.v2.MarketplaceUpgrade` | 0 | 15 | 0 | 15 | `app-server/tests/suite/v2/marketplace_upgrade.rs` | `appserver/tests/suite/v2/MarketplaceUpgrade.kt` |
| 997 | `v2.marketplace_remove` | `appserver.tests.suite.v2.MarketplaceRemove` | 0 | 5 | 0 | 5 | `app-server/tests/suite/v2/marketplace_remove.rs` | `appserver/tests/suite/v2/MarketplaceRemove.kt` |
| 998 | `realtime-webrtc.native` | `realtimewebrtc.src.Native` | 0 | 10 | 3 | 13 | `realtime-webrtc/src/native.rs` | `realtimewebrtc/src/Native.kt` |
| 999 | `responses-api-proxy.dump` | `responsesapiproxy.src.Dump` | 0 | 15 | 6 | 21 | `responses-api-proxy/src/dump.rs` | `responsesapiproxy/src/Dump.kt` |
| 1000 | `responses-api-proxy.main` | `responsesapiproxy.src.Main` | 0 | 2 | 0 | 2 | `responses-api-proxy/src/main.rs` | `responsesapiproxy/src/Main.kt` |
| 1001 | `responses-api-proxy.read_api_key` | `responsesapiproxy.src.ReadApiKey` | 0 | 15 | 0 | 15 | `responses-api-proxy/src/read_api_key.rs` | `responsesapiproxy/src/ReadApiKey.kt` |
| 1002 | `rmcp-client.auth_status` | `rmcpclient.src.AuthStatus` | 0 | 15 | 4 | 19 | `rmcp-client/src/auth_status.rs` | `rmcpclient/src/AuthStatus.kt` |
| 1003 | `bin.rmcp_test_server` | `rmcpclient.src.bin.RmcpTestServer` | 0 | 7 | 2 | 9 | `rmcp-client/src/bin/rmcp_test_server.rs` | `rmcpclient/src/bin/RmcpTestServer.kt` |
| 1004 | `bin.test_stdio_server` | `rmcpclient.src.bin.TestStdioServer` | 0 | 27 | 7 | 34 | `rmcp-client/src/bin/test_stdio_server.rs` | `rmcpclient/src/bin/TestStdioServer.kt` |
| 1005 | `bin.test_streamable_http_server` | `rmcpclient.src.bin.TestStreamableHttpServer` | 0 | 16 | 5 | 21 | `rmcp-client/src/bin/test_streamable_http_server.rs` | `rmcpclient/src/bin/TestStreamableHttpServer.kt` |
| 1006 | `v2.marketplace_add` | `appserver.tests.suite.v2.MarketplaceAdd` | 0 | 1 | 0 | 1 | `app-server/tests/suite/v2/marketplace_add.rs` | `appserver/tests/suite/v2/MarketplaceAdd.kt` |
| 1007 | `v2.initialize` | `appserver.tests.suite.v2.Initialize` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/initialize.rs` | `appserver/tests/suite/v2/Initialize.kt` |
| 1008 | `rmcp-client.http_client_adapter` | `rmcpclient.src.HttpClientAdapter` | 0 | 13 | 3 | 16 | `rmcp-client/src/http_client_adapter.rs` | `rmcpclient/src/HttpClientAdapter.kt` |
| 1009 | `v2.hooks_list` | `appserver.tests.suite.v2.HooksList` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/hooks_list.rs` | `appserver/tests/suite/v2/HooksList.kt` |
| 1010 | `rmcp-client.oauth` | `rmcpclient.src.Oauth` | 0 | 40 | 7 | 47 | `rmcp-client/src/oauth.rs` | `rmcpclient/src/Oauth.kt` |
| 1011 | `v2.external_agent_config` | `appserver.tests.suite.v2.ExternalAgentConfig` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/external_agent_config.rs` | `appserver/tests/suite/v2/ExternalAgentConfig.kt` |
| 1012 | `v2.experimental_feature_list` | `appserver.tests.suite.v2.ExperimentalFeatureList` | 0 | 11 | 0 | 11 | `app-server/tests/suite/v2/experimental_feature_list.rs` | `appserver/tests/suite/v2/ExperimentalFeatureList.kt` |
| 1013 | `rmcp-client.rmcp_client` | `rmcpclient.src.RmcpClient` | 0 | 32 | 13 | 45 | `rmcp-client/src/rmcp_client.rs` | `rmcpclient/src/RmcpClient.kt` |
| 1014 | `v2.experimental_api` | `appserver.tests.suite.v2.ExperimentalApi` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/experimental_api.rs` | `appserver/tests/suite/v2/ExperimentalApi.kt` |
| 1015 | `rmcp-client.utils` | `rmcpclient.src.Utils` | 0 | 15 | 1 | 16 | `rmcp-client/src/utils.rs` | `rmcpclient/src/Utils.kt` |
| 1016 | `tests.process_group_cleanup` | `rmcpclient.tests.ProcessGroupCleanup` | 0 | 7 | 0 | 7 | `rmcp-client/tests/process_group_cleanup.rs` | `rmcpclient/tests/ProcessGroupCleanup.kt` |
| 1017 | `tests.resources` | `rmcpclient.tests.Resources` | 0 | 3 | 0 | 3 | `rmcp-client/tests/resources.rs` | `rmcpclient/tests/Resources.kt` |
| 1018 | `tests.streamable_http_recovery` | `rmcpclient.tests.StreamableHttpRecovery` | 0 | 4 | 0 | 4 | `rmcp-client/tests/streamable_http_recovery.rs` | `rmcpclient/tests/StreamableHttpRecovery.kt` |
| 1019 | `tests.streamable_http_remote` | `rmcpclient.tests.StreamableHttpRemote` | 0 | 1 | 0 | 1 | `rmcp-client/tests/streamable_http_remote.rs` | `rmcpclient/tests/StreamableHttpRemote.kt` |
| 1020 | `tests.streamable_http_test_support` | `rmcpclient.tests.StreamableHttpTestSupport` | 0 | 12 | 1 | 13 | `rmcp-client/tests/streamable_http_test_support.rs` | `rmcpclient/tests/StreamableHttpTestSupport.kt` |
| 1021 | `rollout-trace.bundle` | `rollouttrace.src.Bundle` | 0 | 1 | 1 | 2 | `rollout-trace/src/bundle.rs` | `rollouttrace/src/Bundle.kt` |
| 1022 | `v2.dynamic_tools` | `appserver.tests.suite.v2.DynamicTools` | 0 | 12 | 0 | 12 | `app-server/tests/suite/v2/dynamic_tools.rs` | `appserver/tests/suite/v2/DynamicTools.kt` |
| 1023 | `v2.device_key` | `appserver.tests.suite.v2.DeviceKey` | 0 | 3 | 0 | 3 | `app-server/tests/suite/v2/device_key.rs` | `appserver/tests/suite/v2/DeviceKey.kt` |
| 1024 | `rollout-trace.inference` | `rollouttrace.src.Inference` | 0 | 16 | 7 | 23 | `rollout-trace/src/inference.rs` | `rollouttrace/src/Inference.kt` |
| 1025 | `model.conversation` | `rollouttrace.src.model.Conversation` | 0 | 0 | 9 | 9 | `rollout-trace/src/model/conversation.rs` | `rollouttrace/src/model/Conversation.kt` |
| 1026 | `model.runtime` | `rollouttrace.src.model.Runtime` | 0 | 0 | 18 | 18 | `rollout-trace/src/model/runtime.rs` | `rollouttrace/src/model/Runtime.kt` |
| 1027 | `model.session` | `rollouttrace.src.model.Session` | 0 | 0 | 6 | 6 | `rollout-trace/src/model/session.rs` | `rollouttrace/src/model/Session.kt` |
| 1028 | `v2.connection_handling_websocket_unix` | `appserver.tests.suite.v2.ConnectionHandlingWebsocketUnix` | 0 | 14 | 1 | 15 | `app-server/tests/suite/v2/connection_handling_websocket_unix.rs` | `appserver/tests/suite/v2/ConnectionHandlingWebsocketUnix.kt` |
| 1029 | `rollout-trace.protocol_event` | `rollouttrace.src.ProtocolEvent` | 0 | 7 | 4 | 11 | `rollout-trace/src/protocol_event.rs` | `rollouttrace/src/ProtocolEvent.kt` |
| 1030 | `rollout-trace.raw_event` | `rollouttrace.src.RawEvent` | 0 | 1 | 5 | 6 | `rollout-trace/src/raw_event.rs` | `rollouttrace/src/RawEvent.kt` |
| 1031 | `reducer.code_cell` | `rollouttrace.src.reducer.CodeCell` | 0 | 27 | 4 | 31 | `rollout-trace/src/reducer/code_cell.rs` | `rollouttrace/src/reducer/CodeCell.kt` |
| 1032 | `reducer.code_cell_tests` | `rollouttrace.src.reducer.CodeCellTests` | 0 | 5 | 0 | 5 | `rollout-trace/src/reducer/code_cell_tests.rs` | `rollouttrace/src/reducer/CodeCellTests.kt` |
| 1033 | `reducer.compaction` | `rollouttrace.src.reducer.Compaction` | 0 | 3 | 1 | 4 | `rollout-trace/src/reducer/compaction.rs` | `rollouttrace/src/reducer/Compaction.kt` |
| 1034 | `reducer.conversation` | `rollouttrace.src.reducer.conversation.Conversation` | 0 | 21 | 4 | 25 | `rollout-trace/src/reducer/conversation.rs` | `rollouttrace/src/reducer/conversation/Conversation.kt` |
| 1035 | `conversation.normalize` | `rollouttrace.src.reducer.conversation.Normalize` | 0 | 17 | 2 | 19 | `rollout-trace/src/reducer/conversation/normalize.rs` | `rollouttrace/src/reducer/conversation/Normalize.kt` |
| 1036 | `reducer.conversation_tests` | `rollouttrace.src.reducer.ConversationTests` | 0 | 17 | 0 | 17 | `rollout-trace/src/reducer/conversation_tests.rs` | `rollouttrace/src/reducer/ConversationTests.kt` |
| 1037 | `reducer.inference` | `rollouttrace.src.reducer.Inference` | 0 | 3 | 1 | 4 | `rollout-trace/src/reducer/inference.rs` | `rollouttrace/src/reducer/Inference.kt` |
| 1038 | `reducer.inference_tests` | `rollouttrace.src.reducer.InferenceTests` | 0 | 3 | 0 | 3 | `rollout-trace/src/reducer/inference_tests.rs` | `rollouttrace/src/reducer/InferenceTests.kt` |
| 1039 | `reducer.test_support` | `rollouttrace.src.reducer.TestSupport` | 0 | 18 | 0 | 18 | `rollout-trace/src/reducer/test_support.rs` | `rollouttrace/src/reducer/TestSupport.kt` |
| 1040 | `reducer.thread` | `rollouttrace.src.reducer.Thread` | 0 | 8 | 2 | 10 | `rollout-trace/src/reducer/thread.rs` | `rollouttrace/src/reducer/Thread.kt` |
| 1041 | `reducer.tool` | `rollouttrace.src.reducer.tool.Tool` | 0 | 14 | 1 | 15 | `rollout-trace/src/reducer/tool.rs` | `rollouttrace/src/reducer/tool/Tool.kt` |
| 1042 | `tool.agents` | `rollouttrace.src.reducer.tool.Agents` | 0 | 24 | 2 | 26 | `rollout-trace/src/reducer/tool/agents.rs` | `rollouttrace/src/reducer/tool/Agents.kt` |
| 1043 | `tool.agents_tests` | `rollouttrace.src.reducer.tool.AgentsTests` | 0 | 11 | 1 | 12 | `rollout-trace/src/reducer/tool/agents_tests.rs` | `rollouttrace/src/reducer/tool/AgentsTests.kt` |
| 1044 | `tool.terminal_tests` | `rollouttrace.src.reducer.tool.TerminalTests` | 0 | 6 | 0 | 6 | `rollout-trace/src/reducer/tool/terminal_tests.rs` | `rollouttrace/src/reducer/tool/TerminalTests.kt` |
| 1045 | `v2.connection_handling_websocket` | `appserver.tests.suite.v2.ConnectionHandlingWebsocket` | 0 | 34 | 2 | 36 | `app-server/tests/suite/v2/connection_handling_websocket.rs` | `appserver/tests/suite/v2/ConnectionHandlingWebsocket.kt` |
| 1046 | `rollout-trace.thread_tests` | `rollouttrace.src.ThreadTests` | 0 | 6 | 0 | 6 | `rollout-trace/src/thread_tests.rs` | `rollouttrace/src/ThreadTests.kt` |
| 1047 | `rollout-trace.tool_dispatch` | `rollouttrace.src.ToolDispatch` | 0 | 18 | 9 | 27 | `rollout-trace/src/tool_dispatch.rs` | `rollouttrace/src/ToolDispatch.kt` |
| 1048 | `rollout-trace.writer` | `rollouttrace.src.Writer` | 0 | 8 | 2 | 10 | `rollout-trace/src/writer.rs` | `rollouttrace/src/Writer.kt` |
| 1049 | `rollout.config` | `rollout.src.Config` | 0 | 16 | 3 | 19 | `rollout/src/config.rs` | `rollout/src/Config.kt` |
| 1050 | `v2.config_rpc` | `appserver.tests.suite.v2.ConfigRpc` | 0 | 14 | 0 | 14 | `app-server/tests/suite/v2/config_rpc.rs` | `appserver/tests/suite/v2/ConfigRpc.kt` |
| 1051 | `v2.compaction` | `appserver.tests.suite.v2.Compaction` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/compaction.rs` | `appserver/tests/suite/v2/Compaction.kt` |
| 1052 | `rollout.metadata_tests` | `rollout.src.MetadataTests` | 0 | 8 | 0 | 8 | `rollout/src/metadata_tests.rs` | `rollout/src/MetadataTests.kt` |
| 1053 | `v2.command_exec` | `appserver.tests.suite.v2.CommandExec` | 0 | 26 | 2 | 28 | `app-server/tests/suite/v2/command_exec.rs` | `appserver/tests/suite/v2/CommandExec.kt` |
| 1054 | `rollout.recorder` | `rollout.src.Recorder` | 0 | 61 | 10 | 71 | `rollout/src/recorder.rs` | `rollout/src/Recorder.kt` |
| 1055 | `rollout.recorder_tests` | `rollout.src.RecorderTests` | 0 | 19 | 0 | 19 | `rollout/src/recorder_tests.rs` | `rollout/src/RecorderTests.kt` |
| 1056 | `rollout.session_index` | `rollout.src.SessionIndex` | 0 | 11 | 1 | 12 | `rollout/src/session_index.rs` | `rollout/src/SessionIndex.kt` |
| 1057 | `rollout.session_index_tests` | `rollout.src.SessionIndexTests` | 0 | 10 | 0 | 10 | `rollout/src/session_index_tests.rs` | `rollout/src/SessionIndexTests.kt` |
| 1058 | `v2.collaboration_mode_list` | `appserver.tests.suite.v2.CollaborationModeList` | 0 | 1 | 0 | 1 | `app-server/tests/suite/v2/collaboration_mode_list.rs` | `appserver/tests/suite/v2/CollaborationModeList.kt` |
| 1059 | `rollout.state_db_tests` | `rollout.src.StateDbTests` | 0 | 3 | 0 | 3 | `rollout/src/state_db_tests.rs` | `rollout/src/StateDbTests.kt` |
| 1060 | `rollout.tests` | `rollout.src.Tests` | 0 | 24 | 0 | 24 | `rollout/src/tests.rs` | `rollout/src/Tests.kt` |
| 1061 | `sandboxing.bwrap` | `sandboxing.src.Bwrap` | 0 | 9 | 0 | 9 | `sandboxing/src/bwrap.rs` | `sandboxing/src/Bwrap.kt` |
| 1062 | `sandboxing.bwrap_tests` | `sandboxing.src.BwrapTests` | 0 | 10 | 0 | 10 | `sandboxing/src/bwrap_tests.rs` | `sandboxing/src/BwrapTests.kt` |
| 1063 | `sandboxing.landlock` | `sandboxing.src.Landlock` | 0 | 3 | 0 | 3 | `sandboxing/src/landlock.rs` | `sandboxing/src/Landlock.kt` |
| 1064 | `sandboxing.landlock_tests` | `sandboxing.src.LandlockTests` | 0 | 4 | 0 | 4 | `sandboxing/src/landlock_tests.rs` | `sandboxing/src/LandlockTests.kt` |
| 1065 | `v2.client_metadata` | `appserver.tests.suite.v2.ClientMetadata` | 0 | 6 | 0 | 6 | `app-server/tests/suite/v2/client_metadata.rs` | `appserver/tests/suite/v2/ClientMetadata.kt` |
| 1066 | `sandboxing.manager_tests` | `sandboxing.src.ManagerTests` | 0 | 11 | 0 | 11 | `sandboxing/src/manager_tests.rs` | `sandboxing/src/ManagerTests.kt` |
| 1067 | `sandboxing.policy_transforms` | `sandboxing.src.PolicyTransforms` | 0 | 20 | 1 | 21 | `sandboxing/src/policy_transforms.rs` | `sandboxing/src/PolicyTransforms.kt` |
| 1068 | `sandboxing.policy_transforms_tests` | `sandboxing.src.PolicyTransformsTests` | 0 | 26 | 0 | 26 | `sandboxing/src/policy_transforms_tests.rs` | `sandboxing/src/PolicyTransformsTests.kt` |
| 1069 | `sandboxing.seatbelt_tests` | `sandboxing.src.SeatbeltTests` | 0 | 35 | 2 | 37 | `sandboxing/src/seatbelt_tests.rs` | `sandboxing/src/SeatbeltTests.kt` |
| 1070 | `v2.app_list` | `appserver.tests.suite.v2.AppList` | 0 | 26 | 3 | 29 | `app-server/tests/suite/v2/app_list.rs` | `appserver/tests/suite/v2/AppList.kt` |
| 1071 | `secrets.sanitizer` | `secrets.src.Sanitizer` | 0 | 3 | 0 | 3 | `secrets/src/sanitizer.rs` | `secrets/src/Sanitizer.kt` |
| 1072 | `command_safety.powershell_parser` | `shellcommand.src.commandsafety.PowershellParser` | 0 | 15 | 4 | 19 | `shell-command/src/command_safety/powershell_parser.rs` | `shellcommand/src/commandsafety/PowershellParser.kt` |
| 1073 | `shell-command.parse_command` | `shellcommand.src.ParseCommand` | 0 | 117 | 0 | 117 | `shell-command/src/parse_command.rs` | `shellcommand/src/ParseCommand.kt` |
| 1074 | `shell-command.shell_detect` | `shellcommand.src.ShellDetect` | 0 | 1 | 1 | 2 | `shell-command/src/shell_detect.rs` | `shellcommand/src/ShellDetect.kt` |
| 1075 | `v2.analytics` | `appserver.tests.suite.v2.Analytics` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/analytics.rs` | `appserver/tests/suite/v2/Analytics.kt` |
| 1076 | `unix.escalate_client` | `shellescalation.src.unix.EscalateClient` | 0 | 4 | 0 | 4 | `shell-escalation/src/unix/escalate_client.rs` | `shellescalation/src/unix/EscalateClient.kt` |
| 1077 | `unix.escalate_protocol` | `shellescalation.src.unix.EscalateProtocol` | 0 | 3 | 7 | 10 | `shell-escalation/src/unix/escalate_protocol.rs` | `shellescalation/src/unix/EscalateProtocol.kt` |
| 1078 | `v2.account` | `appserver.tests.suite.v2.Account` | 0 | 31 | 1 | 32 | `app-server/tests/suite/v2/account.rs` | `appserver/tests/suite/v2/Account.kt` |
| 1079 | `suite.fuzzy_file_search` | `appserver.tests.suite.FuzzyFileSearch` | 0 | 18 | 1 | 19 | `app-server/tests/suite/fuzzy_file_search.rs` | `appserver/tests/suite/FuzzyFileSearch.kt` |
| 1080 | `unix.execve_wrapper` | `shellescalation.src.unix.ExecveWrapper` | 0 | 1 | 1 | 2 | `shell-escalation/src/unix/execve_wrapper.rs` | `shellescalation/src/unix/ExecveWrapper.kt` |
| 1081 | `suite.auth` | `appserver.tests.suite.Auth` | 0 | 13 | 0 | 13 | `app-server/tests/suite/auth.rs` | `appserver/tests/suite/Auth.kt` |
| 1082 | `common.rollout` | `appserver.tests.common.Rollout` | 0 | 5 | 0 | 5 | `app-server/tests/common/rollout.rs` | `appserver/tests/common/Rollout.kt` |
| 1083 | `skills.build` | `skills.Build` | 0 | 2 | 0 | 2 | `skills/build.rs` | `skills/Build.kt` |
| 1084 | `bin.logs_client` | `state.src.bin.LogsClient` | 0 | 20 | 3 | 23 | `state/src/bin/logs_client.rs` | `state/src/bin/LogsClient.kt` |
| 1085 | `state.extract` | `state.src.Extract` | 0 | 20 | 0 | 20 | `state/src/extract.rs` | `state/src/Extract.kt` |
| 1086 | `app-server.tests.common.responses` | `appserver.tests.common.Responses` | 0 | 6 | 0 | 6 | `app-server/tests/common/responses.rs` | `appserver/tests/common/Responses.kt` |
| 1087 | `state.migrations` | `state.src.Migrations` | 0 | 3 | 0 | 3 | `state/src/migrations.rs` | `state/src/Migrations.kt` |
| 1088 | `common.models_cache` | `appserver.tests.common.ModelsCache` | 0 | 3 | 0 | 3 | `app-server/tests/common/models_cache.rs` | `appserver/tests/common/ModelsCache.kt` |
| 1089 | `app-server.tests.common.mock_model_server` | `appserver.tests.common.MockModelServer` | 0 | 4 | 1 | 5 | `app-server/tests/common/mock_model_server.rs` | `appserver/tests/common/MockModelServer.kt` |
| 1090 | `model.graph` | `state.src.model.Graph` | 0 | 0 | 1 | 1 | `state/src/model/graph.rs` | `state/src/model/Graph.kt` |
| 1091 | `model.log` | `state.src.model.Log` | 0 | 0 | 3 | 3 | `state/src/model/log.rs` | `state/src/model/Log.kt` |
| 1092 | `model.memories` | `state.src.model.Memories` | 0 | 3 | 7 | 10 | `state/src/model/memories.rs` | `state/src/model/Memories.kt` |
| 1093 | `app-server.tests.common.mcp_process` | `appserver.tests.common.McpProcess` | 0 | 115 | 1 | 116 | `app-server/tests/common/mcp_process.rs` | `appserver/tests/common/McpProcess.kt` |
| 1094 | `common.config` | `appserver.tests.common.Config` | 0 | 2 | 0 | 2 | `app-server/tests/common/config.rs` | `appserver/tests/common/Config.kt` |
| 1095 | `state.paths` | `state.src.Paths` | 0 | 1 | 0 | 1 | `state/src/paths.rs` | `state/src/Paths.kt` |
| 1096 | `common.auth_fixtures` | `appserver.tests.common.AuthFixtures` | 0 | 16 | 2 | 18 | `app-server/tests/common/auth_fixtures.rs` | `appserver/tests/common/AuthFixtures.kt` |
| 1097 | `runtime.agent_jobs` | `state.src.runtime.AgentJobs` | 0 | 20 | 0 | 20 | `state/src/runtime/agent_jobs.rs` | `state/src/runtime/AgentJobs.kt` |
| 1098 | `runtime.backfill` | `state.src.runtime.Backfill` | 0 | 9 | 0 | 9 | `state/src/runtime/backfill.rs` | `state/src/runtime/Backfill.kt` |
| 1099 | `runtime.device_key` | `state.src.runtime.DeviceKey` | 0 | 2 | 1 | 3 | `state/src/runtime/device_key.rs` | `state/src/runtime/DeviceKey.kt` |
| 1100 | `runtime.device_key_tests` | `state.src.runtime.DeviceKeyTests` | 0 | 2 | 0 | 2 | `state/src/runtime/device_key_tests.rs` | `state/src/runtime/DeviceKeyTests.kt` |
| 1101 | `runtime.goals` | `state.src.runtime.Goals` | 0 | 30 | 3 | 33 | `state/src/runtime/goals.rs` | `state/src/runtime/Goals.kt` |
| 1102 | `runtime.logs` | `state.src.runtime.Logs` | 0 | 38 | 1 | 39 | `state/src/runtime/logs.rs` | `state/src/runtime/Logs.kt` |
| 1103 | `runtime.memories` | `state.src.runtime.Memories` | 0 | 59 | 0 | 59 | `state/src/runtime/memories.rs` | `state/src/runtime/Memories.kt` |
| 1104 | `runtime.remote_control` | `state.src.runtime.RemoteControl` | 0 | 7 | 1 | 8 | `state/src/runtime/remote_control.rs` | `state/src/runtime/RemoteControl.kt` |
| 1105 | `runtime.test_support` | `state.src.runtime.TestSupport` | 0 | 2 | 0 | 2 | `state/src/runtime/test_support.rs` | `state/src/runtime/TestSupport.kt` |
| 1106 | `runtime.threads` | `state.src.runtime.Threads` | 0 | 52 | 1 | 53 | `state/src/runtime/threads.rs` | `state/src/runtime/Threads.kt` |
| 1107 | `stdio-to-uds.main` | `stdiotouds.src.Main` | 0 | 1 | 0 | 1 | `stdio-to-uds/src/main.rs` | `stdiotouds/src/Main.kt` |
| 1108 | `tests.stdio_to_uds` | `stdiotouds.tests.StdioToUds` | 0 | 1 | 1 | 2 | `stdio-to-uds/tests/stdio_to_uds.rs` | `stdiotouds/tests/StdioToUds.kt` |
| 1109 | `terminal-detection.terminal_tests` | `terminaldetection.src.TerminalTests` | 0 | 26 | 1 | 27 | `terminal-detection/src/terminal_tests.rs` | `terminaldetection/src/TerminalTests.kt` |
| 1110 | `thread-manager-sample.main` | `threadmanagersample.src.Main` | 0 | 4 | 1 | 5 | `thread-manager-sample/src/main.rs` | `threadmanagersample/src/Main.kt` |
| 1111 | `thread-store.examples.generate-proto` | `threadstore.examples.Generate-proto` | 0 | 1 | 0 | 1 | `thread-store/examples/generate-proto.rs` | `threadstore/examples/Generate-proto.kt` |
| 1112 | `thread-store.in_memory` | `threadstore.src.InMemory` | 0 | 21 | 3 | 24 | `thread-store/src/in_memory.rs` | `threadstore/src/InMemory.kt` |
| 1113 | `common.analytics_server` | `appserver.tests.common.AnalyticsServer` | 0 | 1 | 0 | 1 | `app-server/tests/common/analytics_server.rs` | `appserver/tests/common/AnalyticsServer.kt` |
| 1114 | `local.archive_thread` | `threadstore.src.local.ArchiveThread` | 0 | 3 | 0 | 3 | `thread-store/src/local/archive_thread.rs` | `threadstore/src/local/ArchiveThread.kt` |
| 1115 | `app-server.tests.all` | `appserver.tests.All` | 0 | 0 | 0 | 0 | `app-server/tests/all.rs` | `appserver/tests/All.kt` |
| 1116 | `local.helpers` | `threadstore.src.local.Helpers` | 0 | 10 | 0 | 10 | `thread-store/src/local/helpers.rs` | `threadstore/src/local/Helpers.kt` |
| 1117 | `local.list_threads` | `threadstore.src.local.ListThreads` | 0 | 7 | 0 | 7 | `thread-store/src/local/list_threads.rs` | `threadstore/src/local/ListThreads.kt` |
| 1118 | `app-server.transport_tests` | `appserver.src.TransportTests` | 0 | 11 | 0 | 11 | `app-server/src/transport_tests.rs` | `appserver/src/TransportTests.kt` |
| 1119 | `app-server.server_request_error` | `appserver.src.ServerRequestError` | 0 | 3 | 0 | 3 | `app-server/src/server_request_error.rs` | `appserver/src/ServerRequestError.kt` |
| 1120 | `local.test_support` | `threadstore.src.local.TestSupport` | 0 | 5 | 0 | 5 | `thread-store/src/local/test_support.rs` | `threadstore/src/local/TestSupport.kt` |
| 1121 | `local.unarchive_thread` | `threadstore.src.local.UnarchiveThread` | 0 | 3 | 0 | 3 | `thread-store/src/local/unarchive_thread.rs` | `threadstore/src/local/UnarchiveThread.kt` |
| 1122 | `local.update_thread_metadata` | `threadstore.src.local.UpdateThreadMetadata` | 0 | 15 | 1 | 16 | `thread-store/src/local/update_thread_metadata.rs` | `threadstore/src/local/UpdateThreadMetadata.kt` |
| 1123 | `remote.helpers` | `threadstore.src.remote.Helpers` | 0 | 29 | 0 | 29 | `thread-store/src/remote/helpers.rs` | `threadstore/src/remote/Helpers.kt` |
| 1124 | `remote.list_threads` | `threadstore.src.remote.ListThreads` | 0 | 4 | 1 | 5 | `thread-store/src/remote/list_threads.rs` | `threadstore/src/remote/ListThreads.kt` |
| 1125 | `proto.codex.thread_store.v1` | `threadstore.src.remote.proto.Codex.threadStore.v1` | 0 | 56 | 34 | 90 | `thread-store/src/remote/proto/codex.thread_store.v1.rs` | `threadstore/src/remote/proto/Codex.threadStore.v1.kt` |
| 1126 | `thread-store.store` | `threadstore.src.Store` | 0 | 0 | 1 | 1 | `thread-store/src/store.rs` | `threadstore/src/Store.kt` |
| 1127 | `thread-store.types` | `threadstore.src.Types` | 0 | 0 | 19 | 19 | `thread-store/src/types.rs` | `threadstore/src/Types.kt` |
| 1128 | `tools.agent_job_tool` | `tools.src.AgentJobTool` | 0 | 2 | 0 | 2 | `tools/src/agent_job_tool.rs` | `tools/src/AgentJobTool.kt` |
| 1129 | `tools.agent_job_tool_tests` | `tools.src.AgentJobToolTests` | 0 | 2 | 0 | 2 | `tools/src/agent_job_tool_tests.rs` | `tools/src/AgentJobToolTests.kt` |
| 1130 | `tools.agent_tool` | `tools.src.AgentTool` | 0 | 29 | 2 | 31 | `tools/src/agent_tool.rs` | `tools/src/AgentTool.kt` |
| 1131 | `tools.agent_tool_tests` | `tools.src.AgentToolTests` | 0 | 8 | 0 | 8 | `tools/src/agent_tool_tests.rs` | `tools/src/AgentToolTests.kt` |
| 1132 | `tools.apply_patch_tool` | `tools.src.ApplyPatchTool` | 0 | 2 | 1 | 3 | `tools/src/apply_patch_tool.rs` | `tools/src/ApplyPatchTool.kt` |
| 1133 | `tools.apply_patch_tool_tests` | `tools.src.ApplyPatchToolTests` | 0 | 2 | 0 | 2 | `tools/src/apply_patch_tool_tests.rs` | `tools/src/ApplyPatchToolTests.kt` |
| 1134 | `tools.code_mode` | `tools.src.CodeMode` | 0 | 10 | 0 | 10 | `tools/src/code_mode.rs` | `tools/src/CodeMode.kt` |
| 1135 | `tools.code_mode_tests` | `tools.src.CodeModeTests` | 0 | 6 | 0 | 6 | `tools/src/code_mode_tests.rs` | `tools/src/CodeModeTests.kt` |
| 1136 | `tools.dynamic_tool` | `tools.src.DynamicTool` | 0 | 1 | 0 | 1 | `tools/src/dynamic_tool.rs` | `tools/src/DynamicTool.kt` |
| 1137 | `tools.dynamic_tool_tests` | `tools.src.DynamicToolTests` | 0 | 2 | 0 | 2 | `tools/src/dynamic_tool_tests.rs` | `tools/src/DynamicToolTests.kt` |
| 1138 | `tools.goal_tool` | `tools.src.GoalTool` | 0 | 4 | 0 | 4 | `tools/src/goal_tool.rs` | `tools/src/GoalTool.kt` |
| 1139 | `app-server.request_serialization` | `appserver.src.RequestSerialization` | 0 | 12 | 4 | 16 | `app-server/src/request_serialization.rs` | `appserver/src/RequestSerialization.kt` |
| 1140 | `tools.image_detail_tests` | `tools.src.ImageDetailTests` | 0 | 5 | 0 | 5 | `tools/src/image_detail_tests.rs` | `tools/src/ImageDetailTests.kt` |
| 1141 | `request_processors.windows_sandbox_processor` | `appserver.src.requestprocessors.WindowsSandboxProcessor` | 0 | 3 | 1 | 4 | `app-server/src/request_processors/windows_sandbox_processor.rs` | `appserver/src/requestprocessors/WindowsSandboxProcessor.kt` |
| 1142 | `tools.json_schema_tests` | `tools.src.JsonSchemaTests` | 0 | 22 | 0 | 22 | `tools/src/json_schema_tests.rs` | `tools/src/JsonSchemaTests.kt` |
| 1143 | `tools.local_tool` | `tools.src.LocalTool` | 0 | 12 | 2 | 14 | `tools/src/local_tool.rs` | `tools/src/LocalTool.kt` |
| 1144 | `tools.local_tool_tests` | `tools.src.LocalToolTests` | 0 | 7 | 0 | 7 | `tools/src/local_tool_tests.rs` | `tools/src/LocalToolTests.kt` |
| 1145 | `tools.mcp_resource_tool` | `tools.src.McpResourceTool` | 0 | 3 | 0 | 3 | `tools/src/mcp_resource_tool.rs` | `tools/src/McpResourceTool.kt` |
| 1146 | `tools.mcp_resource_tool_tests` | `tools.src.McpResourceToolTests` | 0 | 3 | 0 | 3 | `tools/src/mcp_resource_tool_tests.rs` | `tools/src/McpResourceToolTests.kt` |
| 1147 | `tools.mcp_tool` | `tools.src.McpTool` | 0 | 2 | 0 | 2 | `tools/src/mcp_tool.rs` | `tools/src/McpTool.kt` |
| 1148 | `tools.mcp_tool_tests` | `tools.src.McpToolTests` | 0 | 4 | 0 | 4 | `tools/src/mcp_tool_tests.rs` | `tools/src/McpToolTests.kt` |
| 1149 | `tools.request_plugin_install` | `tools.src.RequestPluginInstall` | 0 | 4 | 3 | 7 | `tools/src/request_plugin_install.rs` | `tools/src/RequestPluginInstall.kt` |
| 1150 | `tools.request_plugin_install_tests` | `tools.src.RequestPluginInstallTests` | 0 | 5 | 0 | 5 | `tools/src/request_plugin_install_tests.rs` | `tools/src/RequestPluginInstallTests.kt` |
| 1151 | `tools.request_user_input_tool` | `tools.src.RequestUserInputTool` | 0 | 6 | 0 | 6 | `tools/src/request_user_input_tool.rs` | `tools/src/RequestUserInputTool.kt` |
| 1152 | `tools.request_user_input_tool_tests` | `tools.src.RequestUserInputToolTests` | 0 | 5 | 0 | 5 | `tools/src/request_user_input_tool_tests.rs` | `tools/src/RequestUserInputToolTests.kt` |
| 1153 | `tools.responses_api` | `tools.src.ResponsesApi` | 0 | 7 | 6 | 13 | `tools/src/responses_api.rs` | `tools/src/ResponsesApi.kt` |
| 1154 | `tools.responses_api_tests` | `tools.src.ResponsesApiTests` | 0 | 4 | 0 | 4 | `tools/src/responses_api_tests.rs` | `tools/src/ResponsesApiTests.kt` |
| 1155 | `tools.tool_config` | `tools.src.ToolConfig` | 0 | 21 | 7 | 28 | `tools/src/tool_config.rs` | `tools/src/ToolConfig.kt` |
| 1156 | `tools.tool_config_tests` | `tools.src.ToolConfigTests` | 0 | 7 | 0 | 7 | `tools/src/tool_config_tests.rs` | `tools/src/ToolConfigTests.kt` |
| 1157 | `request_processors.turn_processor` | `appserver.src.requestprocessors.TurnProcessor` | 0 | 38 | 1 | 39 | `app-server/src/request_processors/turn_processor.rs` | `appserver/src/requestprocessors/TurnProcessor.kt` |
| 1158 | `tools.tool_definition_tests` | `tools.src.ToolDefinitionTests` | 0 | 3 | 0 | 3 | `tools/src/tool_definition_tests.rs` | `tools/src/ToolDefinitionTests.kt` |
| 1159 | `tools.tool_discovery` | `tools.src.ToolDiscovery` | 0 | 18 | 8 | 26 | `tools/src/tool_discovery.rs` | `tools/src/ToolDiscovery.kt` |
| 1160 | `tools.tool_discovery_tests` | `tools.src.ToolDiscoveryTests` | 0 | 4 | 0 | 4 | `tools/src/tool_discovery_tests.rs` | `tools/src/ToolDiscoveryTests.kt` |
| 1161 | `request_processors.token_usage_replay` | `appserver.src.requestprocessors.TokenUsageReplay` | 0 | 3 | 1 | 4 | `app-server/src/request_processors/token_usage_replay.rs` | `appserver/src/requestprocessors/TokenUsageReplay.kt` |
| 1162 | `tools.tool_registry_plan_tests` | `tools.src.ToolRegistryPlanTests` | 0 | 61 | 0 | 61 | `tools/src/tool_registry_plan_tests.rs` | `tools/src/ToolRegistryPlanTests.kt` |
| 1163 | `tools.tool_registry_plan_types` | `tools.src.ToolRegistryPlanTypes` | 0 | 4 | 7 | 11 | `tools/src/tool_registry_plan_types.rs` | `tools/src/ToolRegistryPlanTypes.kt` |
| 1164 | `request_processors.thread_summary_tests` | `appserver.src.requestprocessors.ThreadSummaryTests` | 0 | 1 | 0 | 1 | `app-server/src/request_processors/thread_summary_tests.rs` | `appserver/src/requestprocessors/ThreadSummaryTests.kt` |
| 1165 | `tools.tool_spec_tests` | `tools.src.ToolSpecTests` | 0 | 7 | 0 | 7 | `tools/src/tool_spec_tests.rs` | `tools/src/ToolSpecTests.kt` |
| 1166 | `tools.utility_tool` | `tools.src.UtilityTool` | 0 | 2 | 0 | 2 | `tools/src/utility_tool.rs` | `tools/src/UtilityTool.kt` |
| 1167 | `tools.utility_tool_tests` | `tools.src.UtilityToolTests` | 0 | 2 | 0 | 2 | `tools/src/utility_tool_tests.rs` | `tools/src/UtilityToolTests.kt` |
| 1168 | `tools.view_image` | `tools.src.ViewImage` | 0 | 2 | 1 | 3 | `tools/src/view_image.rs` | `tools/src/ViewImage.kt` |
| 1169 | `tui.additional_dirs` | `tui.src.AdditionalDirs` | 0 | 8 | 0 | 8 | `tui/src/additional_dirs.rs` | `tui/src/AdditionalDirs.kt` |
| 1170 | `request_processors.thread_summary` | `appserver.src.requestprocessors.ThreadSummary` | 0 | 12 | 0 | 12 | `app-server/src/request_processors/thread_summary.rs` | `appserver/src/requestprocessors/ThreadSummary.kt` |
| 1171 | `app.agent_navigation` | `tui.src.app.AgentNavigation` | 0 | 18 | 2 | 20 | `tui/src/app/agent_navigation.rs` | `tui/src/app/AgentNavigation.kt` |
| 1172 | `app.app_server_event_targets` | `tui.src.app.AppServerEventTargets` | 0 | 5 | 1 | 6 | `tui/src/app/app_server_event_targets.rs` | `tui/src/app/AppServerEventTargets.kt` |
| 1173 | `app.app_server_events` | `tui.src.app.AppServerEvents` | 0 | 4 | 0 | 4 | `tui/src/app/app_server_events.rs` | `tui/src/app/AppServerEvents.kt` |
| 1174 | `app.app_server_requests` | `tui.src.app.AppServerRequests` | 0 | 18 | 6 | 24 | `tui/src/app/app_server_requests.rs` | `tui/src/app/AppServerRequests.kt` |
| 1175 | `app.background_requests` | `tui.src.app.BackgroundRequests` | 0 | 48 | 1 | 49 | `tui/src/app/background_requests.rs` | `tui/src/app/BackgroundRequests.kt` |
| 1176 | `app.config_persistence` | `tui.src.app.ConfigPersistence` | 0 | 28 | 0 | 28 | `tui/src/app/config_persistence.rs` | `tui/src/app/ConfigPersistence.kt` |
| 1177 | `app.event_dispatch` | `tui.src.app.EventDispatch` | 0 | 4 | 0 | 4 | `tui/src/app/event_dispatch.rs` | `tui/src/app/EventDispatch.kt` |
| 1178 | `app.history_ui` | `tui.src.app.HistoryUi` | 0 | 7 | 0 | 7 | `tui/src/app/history_ui.rs` | `tui/src/app/HistoryUi.kt` |
| 1179 | `app.input` | `tui.src.app.Input` | 0 | 8 | 0 | 8 | `tui/src/app/input.rs` | `tui/src/app/Input.kt` |
| 1180 | `app.loaded_threads` | `tui.src.app.LoadedThreads` | 0 | 5 | 1 | 6 | `tui/src/app/loaded_threads.rs` | `tui/src/app/LoadedThreads.kt` |
| 1181 | `app.pending_interactive_replay` | `tui.src.app.PendingInteractiveReplay` | 0 | 38 | 3 | 41 | `tui/src/app/pending_interactive_replay.rs` | `tui/src/app/PendingInteractiveReplay.kt` |
| 1182 | `app.platform_actions` | `tui.src.app.PlatformActions` | 0 | 4 | 1 | 5 | `tui/src/app/platform_actions.rs` | `tui/src/app/PlatformActions.kt` |
| 1183 | `app.replay_filter` | `tui.src.app.ReplayFilter` | 0 | 2 | 0 | 2 | `tui/src/app/replay_filter.rs` | `tui/src/app/ReplayFilter.kt` |
| 1184 | `app.resize_reflow` | `tui.src.app.ResizeReflow` | 0 | 23 | 2 | 25 | `tui/src/app/resize_reflow.rs` | `tui/src/app/ResizeReflow.kt` |
| 1185 | `app.session_lifecycle` | `tui.src.app.SessionLifecycle` | 0 | 25 | 0 | 25 | `tui/src/app/session_lifecycle.rs` | `tui/src/app/SessionLifecycle.kt` |
| 1186 | `app.side` | `tui.src.app.Side` | 0 | 30 | 3 | 33 | `tui/src/app/side.rs` | `tui/src/app/Side.kt` |
| 1187 | `app.startup_prompts` | `tui.src.app.StartupPrompts` | 0 | 12 | 1 | 13 | `tui/src/app/startup_prompts.rs` | `tui/src/app/StartupPrompts.kt` |
| 1188 | `app.test_support` | `tui.src.app.TestSupport` | 0 | 3 | 0 | 3 | `tui/src/app/test_support.rs` | `tui/src/app/TestSupport.kt` |
| 1189 | `app.tests` | `tui.src.app.tests.Tests` | 0 | 133 | 1 | 134 | `tui/src/app/tests.rs` | `tui/src/app/tests/Tests.kt` |
| 1190 | `tests.model_catalog` | `tui.src.app.tests.ModelCatalog` | 0 | 12 | 0 | 12 | `tui/src/app/tests/model_catalog.rs` | `tui/src/app/tests/ModelCatalog.kt` |
| 1191 | `app.thread_events` | `tui.src.app.ThreadEvents` | 0 | 34 | 5 | 39 | `tui/src/app/thread_events.rs` | `tui/src/app/ThreadEvents.kt` |
| 1192 | `app.thread_goal_actions` | `tui.src.app.ThreadGoalActions` | 0 | 6 | 0 | 6 | `tui/src/app/thread_goal_actions.rs` | `tui/src/app/ThreadGoalActions.kt` |
| 1193 | `app.thread_routing` | `tui.src.app.ThreadRouting` | 0 | 54 | 0 | 54 | `tui/src/app/thread_routing.rs` | `tui/src/app/ThreadRouting.kt` |
| 1194 | `request_processors.thread_processor_tests` | `appserver.src.requestprocessors.ThreadProcessorTests` | 0 | 35 | 0 | 35 | `app-server/src/request_processors/thread_processor_tests.rs` | `appserver/src/requestprocessors/ThreadProcessorTests.kt` |
| 1195 | `tui.app_backtrack` | `tui.src.AppBacktrack` | 0 | 42 | 3 | 45 | `tui/src/app_backtrack.rs` | `tui/src/AppBacktrack.kt` |
| 1196 | `request_processors.thread_processor` | `appserver.src.requestprocessors.ThreadProcessor` | 0 | 123 | 5 | 128 | `app-server/src/request_processors/thread_processor.rs` | `appserver/src/requestprocessors/ThreadProcessor.kt` |
| 1197 | `tui.app_server_approval_conversions` | `tui.src.AppServerApprovalConversions` | 0 | 6 | 0 | 6 | `tui/src/app_server_approval_conversions.rs` | `tui/src/AppServerApprovalConversions.kt` |
| 1198 | `request_processors.thread_lifecycle` | `appserver.src.requestprocessors.ThreadLifecycle` | 0 | 18 | 4 | 22 | `app-server/src/request_processors/thread_lifecycle.rs` | `appserver/src/requestprocessors/ThreadLifecycle.kt` |
| 1199 | `tui.approval_events` | `tui.src.ApprovalEvents` | 0 | 3 | 2 | 5 | `tui/src/approval_events.rs` | `tui/src/ApprovalEvents.kt` |
| 1200 | `request_processors.thread_goal_processor` | `appserver.src.requestprocessors.ThreadGoalProcessor` | 0 | 18 | 1 | 19 | `app-server/src/request_processors/thread_goal_processor.rs` | `appserver/src/requestprocessors/ThreadGoalProcessor.kt` |
| 1201 | `tui.audio_device` | `tui.src.AudioDevice` | 0 | 12 | 0 | 12 | `tui/src/audio_device.rs` | `tui/src/AudioDevice.kt` |
| 1202 | `request_processors.search` | `appserver.src.requestprocessors.Search` | 0 | 7 | 1 | 8 | `app-server/src/request_processors/search.rs` | `appserver/src/requestprocessors/Search.kt` |
| 1203 | `bin.md-events` | `tui.src.bin.Md-events` | 0 | 1 | 0 | 1 | `tui/src/bin/md-events.rs` | `tui/src/bin/Md-events.kt` |
| 1204 | `bottom_pane.action_required_title` | `tui.src.bottompane.ActionRequiredTitle` | 0 | 1 | 0 | 1 | `tui/src/bottom_pane/action_required_title.rs` | `tui/src/bottompane/ActionRequiredTitle.kt` |
| 1205 | `request_processors.request_errors` | `appserver.src.requestprocessors.RequestErrors` | 0 | 1 | 0 | 1 | `app-server/src/request_processors/request_errors.rs` | `appserver/src/requestprocessors/RequestErrors.kt` |
| 1206 | `request_processors.plugins` | `appserver.src.requestprocessors.Plugins` | 0 | 42 | 1 | 43 | `app-server/src/request_processors/plugins.rs` | `appserver/src/requestprocessors/Plugins.kt` |
| 1207 | `request_processors.mcp_processor` | `appserver.src.requestprocessors.McpProcessor` | 0 | 18 | 1 | 19 | `app-server/src/request_processors/mcp_processor.rs` | `appserver/src/requestprocessors/McpProcessor.kt` |
| 1208 | `request_processors.marketplace_processor` | `appserver.src.requestprocessors.MarketplaceProcessor` | 0 | 8 | 1 | 9 | `app-server/src/request_processors/marketplace_processor.rs` | `appserver/src/requestprocessors/MarketplaceProcessor.kt` |
| 1209 | `chat_composer.history_search` | `tui.src.bottompane.chatcomposer.HistorySearch` | 0 | 28 | 2 | 30 | `tui/src/bottom_pane/chat_composer/history_search.rs` | `tui/src/bottompane/chatcomposer/HistorySearch.kt` |
| 1210 | `request_processors.initialize_processor` | `appserver.src.requestprocessors.InitializeProcessor` | 0 | 5 | 1 | 6 | `app-server/src/request_processors/initialize_processor.rs` | `appserver/src/requestprocessors/InitializeProcessor.kt` |
| 1211 | `request_processors.git_processor` | `appserver.src.requestprocessors.GitProcessor` | 0 | 3 | 1 | 4 | `app-server/src/request_processors/git_processor.rs` | `appserver/src/requestprocessors/GitProcessor.kt` |
| 1212 | `request_processors.fs_processor` | `appserver.src.requestprocessors.FsProcessor` | 0 | 12 | 1 | 13 | `app-server/src/request_processors/fs_processor.rs` | `appserver/src/requestprocessors/FsProcessor.kt` |
| 1213 | `request_processors.feedback_processor` | `appserver.src.requestprocessors.FeedbackProcessor` | 0 | 5 | 1 | 6 | `app-server/src/request_processors/feedback_processor.rs` | `appserver/src/requestprocessors/FeedbackProcessor.kt` |
| 1214 | `bottom_pane.feedback_view` | `tui.src.bottompane.FeedbackView` | 0 | 39 | 2 | 41 | `tui/src/bottom_pane/feedback_view.rs` | `tui/src/bottompane/FeedbackView.kt` |
| 1215 | `request_processors.external_agent_config_processor_tests` | `appserver.src.requestprocessors.ExternalAgentConfigProcessorTests` | 0 | 2 | 0 | 2 | `app-server/src/request_processors/external_agent_config_processor_tests.rs` | `appserver/src/requestprocessors/ExternalAgentConfigProcessorTests.kt` |
| 1216 | `bottom_pane.footer` | `tui.src.bottompane.Footer` | 0 | 48 | 13 | 61 | `tui/src/bottom_pane/footer.rs` | `tui/src/bottompane/Footer.kt` |
| 1217 | `request_processors.external_agent_config_processor` | `appserver.src.requestprocessors.ExternalAgentConfigProcessor` | 0 | 11 | 1 | 12 | `app-server/src/request_processors/external_agent_config_processor.rs` | `appserver/src/requestprocessors/ExternalAgentConfigProcessor.kt` |
| 1218 | `request_processors.device_key_processor` | `appserver.src.requestprocessors.DeviceKeyProcessor` | 0 | 22 | 2 | 24 | `app-server/src/request_processors/device_key_processor.rs` | `appserver/src/requestprocessors/DeviceKeyProcessor.kt` |
| 1219 | `bottom_pane.mcp_server_elicitation` | `tui.src.bottompane.McpServerElicitation` | 0 | 103 | 13 | 116 | `tui/src/bottom_pane/mcp_server_elicitation.rs` | `tui/src/bottompane/McpServerElicitation.kt` |
| 1220 | `request_processors.config_processor` | `appserver.src.requestprocessors.ConfigProcessor` | 0 | 28 | 1 | 29 | `app-server/src/request_processors/config_processor.rs` | `appserver/src/requestprocessors/ConfigProcessor.kt` |
| 1221 | `request_processors.config_errors` | `appserver.src.requestprocessors.ConfigErrors` | 0 | 2 | 0 | 2 | `app-server/src/request_processors/config_errors.rs` | `appserver/src/requestprocessors/ConfigErrors.kt` |
| 1222 | `request_processors.command_exec_processor_tests` | `appserver.src.requestprocessors.CommandExecProcessorTests` | 0 | 1 | 0 | 1 | `app-server/src/request_processors/command_exec_processor_tests.rs` | `appserver/src/requestprocessors/CommandExecProcessorTests.kt` |
| 1223 | `request_processors.command_exec_processor` | `appserver.src.requestprocessors.CommandExecProcessor` | 0 | 9 | 1 | 10 | `app-server/src/request_processors/command_exec_processor.rs` | `appserver/src/requestprocessors/CommandExecProcessor.kt` |
| 1224 | `request_processors.catalog_processor` | `appserver.src.requestprocessors.CatalogProcessor` | 0 | 21 | 1 | 22 | `app-server/src/request_processors/catalog_processor.rs` | `appserver/src/requestprocessors/CatalogProcessor.kt` |
| 1225 | `bottom_pane.popup_consts` | `tui.src.bottompane.PopupConsts` | 0 | 3 | 0 | 3 | `tui/src/bottom_pane/popup_consts.rs` | `tui/src/bottompane/PopupConsts.kt` |
| 1226 | `bottom_pane.prompt_args` | `tui.src.bottompane.PromptArgs` | 0 | 1 | 0 | 1 | `tui/src/bottom_pane/prompt_args.rs` | `tui/src/bottompane/PromptArgs.kt` |
| 1227 | `request_processors.apps_processor` | `appserver.src.requestprocessors.AppsProcessor` | 0 | 12 | 2 | 14 | `app-server/src/request_processors/apps_processor.rs` | `appserver/src/requestprocessors/AppsProcessor.kt` |
| 1228 | `request_user_input.render` | `tui.src.bottompane.requestuserinput.Render` | 0 | 14 | 3 | 17 | `tui/src/bottom_pane/request_user_input/render.rs` | `tui/src/bottompane/requestuserinput/Render.kt` |
| 1229 | `bottom_pane.selection_popup_common` | `tui.src.bottompane.SelectionPopupCommon` | 0 | 26 | 3 | 29 | `tui/src/bottom_pane/selection_popup_common.rs` | `tui/src/bottompane/SelectionPopupCommon.kt` |
| 1230 | `bottom_pane.selection_tabs` | `tui.src.bottompane.SelectionTabs` | 0 | 4 | 1 | 5 | `tui/src/bottom_pane/selection_tabs.rs` | `tui/src/bottompane/SelectionTabs.kt` |
| 1231 | `request_processors.account_processor` | `appserver.src.requestprocessors.AccountProcessor` | 0 | 42 | 4 | 46 | `app-server/src/request_processors/account_processor.rs` | `appserver/src/requestprocessors/AccountProcessor.kt` |
| 1232 | `app-server.request_processors` | `appserver.src.requestprocessors.RequestProcessors` | 0 | 1 | 0 | 1 | `app-server/src/request_processors.rs` | `appserver/src/requestprocessors/RequestProcessors.kt` |
| 1233 | `app-server.outgoing_message` | `appserver.src.OutgoingMessage` | 0 | 55 | 7 | 62 | `app-server/src/outgoing_message.rs` | `appserver/src/OutgoingMessage.kt` |
| 1234 | `bottom_pane.status_line_setup` | `tui.src.bottompane.StatusLineSetup` | 0 | 22 | 2 | 24 | `tui/src/bottom_pane/status_line_setup.rs` | `tui/src/bottompane/StatusLineSetup.kt` |
| 1235 | `bottom_pane.status_line_style` | `tui.src.bottompane.StatusLineStyle` | 0 | 16 | 1 | 17 | `tui/src/bottom_pane/status_line_style.rs` | `tui/src/bottompane/StatusLineStyle.kt` |
| 1236 | `bottom_pane.status_surface_preview` | `tui.src.bottompane.StatusSurfacePreview` | 0 | 8 | 3 | 11 | `tui/src/bottom_pane/status_surface_preview.rs` | `tui/src/bottompane/StatusSurfacePreview.kt` |
| 1237 | `app-server.message_processor_tracing_tests` | `appserver.src.MessageProcessorTracingTests` | 0 | 28 | 3 | 31 | `app-server/src/message_processor_tracing_tests.rs` | `appserver/src/MessageProcessorTracingTests.kt` |
| 1238 | `bottom_pane.title_setup` | `tui.src.bottompane.TitleSetup` | 0 | 23 | 2 | 25 | `tui/src/bottom_pane/title_setup.rs` | `tui/src/bottompane/TitleSetup.kt` |
| 1239 | `app-server.message_processor` | `appserver.src.MessageProcessor` | 0 | 35 | 5 | 40 | `app-server/src/message_processor.rs` | `appserver/src/MessageProcessor.kt` |
| 1240 | `app-server.main` | `appserver.src.Main` | 0 | 3 | 1 | 4 | `app-server/src/main.rs` | `appserver/src/Main.kt` |
| 1241 | `app-server.fuzzy_file_search` | `appserver.src.FuzzyFileSearch` | 0 | 9 | 3 | 12 | `app-server/src/fuzzy_file_search.rs` | `appserver/src/FuzzyFileSearch.kt` |
| 1242 | `chatwidget.goal_menu` | `tui.src.chatwidget.GoalMenu` | 0 | 5 | 0 | 5 | `tui/src/chatwidget/goal_menu.rs` | `tui/src/chatwidget/GoalMenu.kt` |
| 1243 | `chatwidget.goal_status` | `tui.src.chatwidget.GoalStatus` | 0 | 16 | 1 | 17 | `tui/src/chatwidget/goal_status.rs` | `tui/src/chatwidget/GoalStatus.kt` |
| 1244 | `chatwidget.hooks` | `tui.src.chatwidget.Hooks` | 0 | 2 | 0 | 2 | `tui/src/chatwidget/hooks.rs` | `tui/src/chatwidget/Hooks.kt` |
| 1245 | `chatwidget.ide_context` | `tui.src.chatwidget.IdeContext` | 0 | 9 | 1 | 10 | `tui/src/chatwidget/ide_context.rs` | `tui/src/chatwidget/IdeContext.kt` |
| 1246 | `chatwidget.interrupts` | `tui.src.chatwidget.Interrupts` | 0 | 18 | 2 | 20 | `tui/src/chatwidget/interrupts.rs` | `tui/src/chatwidget/Interrupts.kt` |
| 1247 | `chatwidget.keymap_picker` | `tui.src.chatwidget.KeymapPicker` | 0 | 8 | 0 | 8 | `tui/src/chatwidget/keymap_picker.rs` | `tui/src/chatwidget/KeymapPicker.kt` |
| 1248 | `chatwidget.mcp_startup` | `tui.src.chatwidget.McpStartup` | 0 | 5 | 1 | 6 | `tui/src/chatwidget/mcp_startup.rs` | `tui/src/chatwidget/McpStartup.kt` |
| 1249 | `chatwidget.plan_implementation` | `tui.src.chatwidget.PlanImplementation` | 0 | 1 | 0 | 1 | `tui/src/chatwidget/plan_implementation.rs` | `tui/src/chatwidget/PlanImplementation.kt` |
| 1250 | `chatwidget.plugins` | `tui.src.chatwidget.Plugins` | 0 | 71 | 3 | 74 | `tui/src/chatwidget/plugins.rs` | `tui/src/chatwidget/Plugins.kt` |
| 1251 | `chatwidget.realtime` | `tui.src.chatwidget.Realtime` | 0 | 36 | 3 | 39 | `tui/src/chatwidget/realtime.rs` | `tui/src/chatwidget/Realtime.kt` |
| 1252 | `chatwidget.reasoning_shortcuts` | `tui.src.chatwidget.ReasoningShortcuts` | 0 | 11 | 1 | 12 | `tui/src/chatwidget/reasoning_shortcuts.rs` | `tui/src/chatwidget/ReasoningShortcuts.kt` |
| 1253 | `app-server.fs_watch` | `appserver.src.FsWatch` | 0 | 13 | 5 | 18 | `app-server/src/fs_watch.rs` | `appserver/src/FsWatch.kt` |
| 1254 | `chatwidget.side` | `tui.src.chatwidget.Side` | 0 | 3 | 0 | 3 | `tui/src/chatwidget/side.rs` | `tui/src/chatwidget/Side.kt` |
| 1255 | `chatwidget.skills` | `tui.src.chatwidget.Skills` | 0 | 21 | 1 | 22 | `tui/src/chatwidget/skills.rs` | `tui/src/chatwidget/Skills.kt` |
| 1256 | `chatwidget.slash_dispatch` | `tui.src.chatwidget.SlashDispatch` | 0 | 16 | 3 | 19 | `tui/src/chatwidget/slash_dispatch.rs` | `tui/src/chatwidget/SlashDispatch.kt` |
| 1257 | `chatwidget.status_surfaces` | `tui.src.chatwidget.StatusSurfaces` | 0 | 49 | 3 | 52 | `tui/src/chatwidget/status_surfaces.rs` | `tui/src/chatwidget/StatusSurfaces.kt` |
| 1258 | `chatwidget.tests` | `tui.src.chatwidget.tests.Tests` | 0 | 1 | 0 | 1 | `tui/src/chatwidget/tests.rs` | `tui/src/chatwidget/tests/Tests.kt` |
| 1259 | `tests.app_server` | `tui.src.chatwidget.tests.AppServer` | 0 | 19 | 0 | 19 | `tui/src/chatwidget/tests/app_server.rs` | `tui/src/chatwidget/tests/AppServer.kt` |
| 1260 | `tests.approval_requests` | `tui.src.chatwidget.tests.ApprovalRequests` | 0 | 6 | 0 | 6 | `tui/src/chatwidget/tests/approval_requests.rs` | `tui/src/chatwidget/tests/ApprovalRequests.kt` |
| 1261 | `tests.composer_submission` | `tui.src.chatwidget.tests.ComposerSubmission` | 0 | 31 | 0 | 31 | `tui/src/chatwidget/tests/composer_submission.rs` | `tui/src/chatwidget/tests/ComposerSubmission.kt` |
| 1262 | `tests.exec_flow` | `tui.src.chatwidget.tests.ExecFlow` | 0 | 47 | 0 | 47 | `tui/src/chatwidget/tests/exec_flow.rs` | `tui/src/chatwidget/tests/ExecFlow.kt` |
| 1263 | `tests.goal_menu` | `tui.src.chatwidget.tests.GoalMenu` | 0 | 8 | 0 | 8 | `tui/src/chatwidget/tests/goal_menu.rs` | `tui/src/chatwidget/tests/GoalMenu.kt` |
| 1264 | `tests.guardian` | `tui.src.chatwidget.tests.Guardian` | 0 | 12 | 0 | 12 | `tui/src/chatwidget/tests/guardian.rs` | `tui/src/chatwidget/tests/Guardian.kt` |
| 1265 | `tests.helpers` | `tui.src.chatwidget.tests.Helpers` | 0 | 90 | 0 | 90 | `tui/src/chatwidget/tests/helpers.rs` | `tui/src/chatwidget/tests/Helpers.kt` |
| 1266 | `tests.history_replay` | `tui.src.chatwidget.tests.HistoryReplay` | 0 | 22 | 0 | 22 | `tui/src/chatwidget/tests/history_replay.rs` | `tui/src/chatwidget/tests/HistoryReplay.kt` |
| 1267 | `tests.mcp_startup` | `tui.src.chatwidget.tests.McpStartup` | 0 | 13 | 0 | 13 | `tui/src/chatwidget/tests/mcp_startup.rs` | `tui/src/chatwidget/tests/McpStartup.kt` |
| 1268 | `tests.permissions` | `tui.src.chatwidget.tests.Permissions` | 0 | 22 | 0 | 22 | `tui/src/chatwidget/tests/permissions.rs` | `tui/src/chatwidget/tests/Permissions.kt` |
| 1269 | `tests.plan_mode` | `tui.src.chatwidget.tests.PlanMode` | 0 | 63 | 0 | 63 | `tui/src/chatwidget/tests/plan_mode.rs` | `tui/src/chatwidget/tests/PlanMode.kt` |
| 1270 | `tests.popups_and_settings` | `tui.src.chatwidget.tests.PopupsAndSettings` | 0 | 69 | 0 | 69 | `tui/src/chatwidget/tests/popups_and_settings.rs` | `tui/src/chatwidget/tests/PopupsAndSettings.kt` |
| 1271 | `tests.review_mode` | `tui.src.chatwidget.tests.ReviewMode` | 0 | 39 | 0 | 39 | `tui/src/chatwidget/tests/review_mode.rs` | `tui/src/chatwidget/tests/ReviewMode.kt` |
| 1272 | `tests.side` | `tui.src.chatwidget.tests.Side` | 0 | 13 | 0 | 13 | `tui/src/chatwidget/tests/side.rs` | `tui/src/chatwidget/tests/Side.kt` |
| 1273 | `tests.slash_commands` | `tui.src.chatwidget.tests.SlashCommands` | 0 | 83 | 0 | 83 | `tui/src/chatwidget/tests/slash_commands.rs` | `tui/src/chatwidget/tests/SlashCommands.kt` |
| 1274 | `tests.status_and_layout` | `tui.src.chatwidget.tests.StatusAndLayout` | 0 | 102 | 1 | 103 | `tui/src/chatwidget/tests/status_and_layout.rs` | `tui/src/chatwidget/tests/StatusAndLayout.kt` |
| 1275 | `tests.status_command_tests` | `tui.src.chatwidget.tests.StatusCommandTests` | 0 | 6 | 0 | 6 | `tui/src/chatwidget/tests/status_command_tests.rs` | `tui/src/chatwidget/tests/StatusCommandTests.kt` |
| 1276 | `tests.status_surface_previews` | `tui.src.chatwidget.tests.StatusSurfacePreviews` | 0 | 18 | 0 | 18 | `tui/src/chatwidget/tests/status_surface_previews.rs` | `tui/src/chatwidget/tests/StatusSurfacePreviews.kt` |
| 1277 | `tests.terminal_title` | `tui.src.chatwidget.tests.TerminalTitle` | 0 | 4 | 0 | 4 | `tui/src/chatwidget/tests/terminal_title.rs` | `tui/src/chatwidget/tests/TerminalTitle.kt` |
| 1278 | `chatwidget.user_messages` | `tui.src.chatwidget.UserMessages` | 0 | 3 | 2 | 5 | `tui/src/chatwidget/user_messages.rs` | `tui/src/chatwidget/UserMessages.kt` |
| 1279 | `app-server.filters` | `appserver.src.Filters` | 0 | 7 | 0 | 7 | `app-server/src/filters.rs` | `appserver/src/Filters.kt` |
| 1280 | `tui.clipboard_copy` | `tui.src.ClipboardCopy` | 0 | 30 | 2 | 32 | `tui/src/clipboard_copy.rs` | `tui/src/ClipboardCopy.kt` |
| 1281 | `tui.clipboard_paste` | `tui.src.ClipboardPaste` | 0 | 26 | 3 | 29 | `tui/src/clipboard_paste.rs` | `tui/src/ClipboardPaste.kt` |
| 1282 | `app-server.dynamic_tools` | `appserver.src.DynamicTools` | 0 | 3 | 0 | 3 | `app-server/src/dynamic_tools.rs` | `appserver/src/DynamicTools.kt` |
| 1283 | `app-server.config_manager_service_tests` | `appserver.src.ConfigManagerServiceTests` | 0 | 17 | 0 | 17 | `app-server/src/config_manager_service_tests.rs` | `appserver/src/ConfigManagerServiceTests.kt` |
| 1284 | `app-server.config_manager_service` | `appserver.src.ConfigManagerService` | 0 | 27 | 2 | 29 | `app-server/src/config_manager_service.rs` | `appserver/src/ConfigManagerService.kt` |
| 1285 | `tui.debug_config` | `tui.src.DebugConfig` | 0 | 32 | 0 | 32 | `tui/src/debug_config.rs` | `tui/src/DebugConfig.kt` |
| 1286 | `tui.diff_model` | `tui.src.DiffModel` | 0 | 0 | 1 | 1 | `tui/src/diff_model.rs` | `tui/src/DiffModel.kt` |
| 1287 | `tui.diff_render` | `tui.src.DiffRender` | 0 | 97 | 8 | 105 | `tui/src/diff_render.rs` | `tui/src/DiffRender.kt` |
| 1288 | `exec_cell.model` | `tui.src.execcell.Model` | 0 | 14 | 3 | 17 | `tui/src/exec_cell/model.rs` | `tui/src/execcell/Model.kt` |
| 1289 | `exec_cell.render` | `tui.src.execcell.Render` | 0 | 30 | 4 | 34 | `tui/src/exec_cell/render.rs` | `tui/src/execcell/Render.kt` |
| 1290 | `tui.exec_command` | `tui.src.ExecCommand` | 0 | 8 | 0 | 8 | `tui/src/exec_command.rs` | `tui/src/ExecCommand.kt` |
| 1291 | `tui.external_agent_config_migration` | `tui.src.ExternalAgentConfigMigration` | 0 | 50 | 7 | 57 | `tui/src/external_agent_config_migration.rs` | `tui/src/ExternalAgentConfigMigration.kt` |
| 1292 | `tui.external_agent_config_migration_startup` | `tui.src.ExternalAgentConfigMigrationStartup` | 0 | 17 | 1 | 18 | `tui/src/external_agent_config_migration_startup.rs` | `tui/src/ExternalAgentConfigMigrationStartup.kt` |
| 1293 | `config.external_agent_config_tests` | `appserver.src.config.ExternalAgentConfigTests` | 0 | 47 | 0 | 47 | `app-server/src/config/external_agent_config_tests.rs` | `appserver/src/config/ExternalAgentConfigTests.kt` |
| 1294 | `tui.file_search` | `tui.src.FileSearch` | 0 | 7 | 3 | 10 | `tui/src/file_search.rs` | `tui/src/FileSearch.kt` |
| 1295 | `tui.frames` | `tui.src.Frames` | 0 | 0 | 0 | 0 | `tui/src/frames.rs` | `tui/src/Frames.kt` |
| 1296 | `config.external_agent_config` | `appserver.src.config.ExternalAgentConfig` | 0 | 53 | 10 | 63 | `app-server/src/config/external_agent_config.rs` | `appserver/src/config/ExternalAgentConfig.kt` |
| 1297 | `tui.goal_display` | `tui.src.GoalDisplay` | 0 | 6 | 0 | 6 | `tui/src/goal_display.rs` | `tui/src/GoalDisplay.kt` |
| 1298 | `app-server.command_exec` | `appserver.src.CommandExec` | 0 | 22 | 10 | 32 | `app-server/src/command_exec.rs` | `appserver/src/CommandExec.kt` |
| 1299 | `bin.test_notify_capture` | `appserver.src.bin.TestNotifyCapture` | 0 | 1 | 0 | 1 | `app-server/src/bin/test_notify_capture.rs` | `appserver/src/bin/TestNotifyCapture.kt` |
| 1300 | `bin.notify_capture` | `appserver.src.bin.NotifyCapture` | 0 | 1 | 0 | 1 | `app-server/src/bin/notify_capture.rs` | `appserver/src/bin/NotifyCapture.kt` |
| 1301 | `ide_context.ipc` | `tui.src.idecontext.Ipc` | 0 | 52 | 3 | 55 | `tui/src/ide_context/ipc.rs` | `tui/src/idecontext/Ipc.kt` |
| 1302 | `ide_context.prompt` | `tui.src.idecontext.Prompt` | 0 | 13 | 0 | 13 | `tui/src/ide_context/prompt.rs` | `tui/src/idecontext/Prompt.kt` |
| 1303 | `ide_context.windows_pipe` | `tui.src.idecontext.WindowsPipe` | 0 | 17 | 4 | 21 | `tui/src/ide_context/windows_pipe.rs` | `tui/src/idecontext/WindowsPipe.kt` |
| 1304 | `tui.insert_history` | `tui.src.InsertHistory` | 0 | 23 | 4 | 27 | `tui/src/insert_history.rs` | `tui/src/InsertHistory.kt` |
| 1305 | `tui.keymap` | `tui.src.Keymap` | 0 | 58 | 10 | 68 | `tui/src/keymap.rs` | `tui/src/Keymap.kt` |
| 1306 | `app-server.bespoke_event_handling` | `appserver.src.BespokeEventHandling` | 0 | 57 | 5 | 62 | `app-server/src/bespoke_event_handling.rs` | `appserver/src/BespokeEventHandling.kt` |
| 1307 | `app-server.app_server_tracing` | `appserver.src.AppServerTracing` | 0 | 10 | 0 | 10 | `app-server/src/app_server_tracing.rs` | `appserver/src/AppServerTracing.kt` |
| 1308 | `app-server.analytics_utils` | `appserver.src.AnalyticsUtils` | 0 | 1 | 0 | 1 | `app-server/src/analytics_utils.rs` | `appserver/src/AnalyticsUtils.kt` |
| 1309 | `keymap_setup.picker` | `tui.src.keymapsetup.Picker` | 0 | 16 | 2 | 18 | `tui/src/keymap_setup/picker.rs` | `tui/src/keymapsetup/Picker.kt` |
| 1310 | `tui.line_truncation` | `tui.src.LineTruncation` | 0 | 3 | 0 | 3 | `tui/src/line_truncation.rs` | `tui/src/LineTruncation.kt` |
| 1311 | `tui.live_wrap` | `tui.src.LiveWrap` | 0 | 17 | 2 | 19 | `tui/src/live_wrap.rs` | `tui/src/LiveWrap.kt` |
| 1312 | `tui.local_chatgpt_auth` | `tui.src.LocalChatgptAuth` | 0 | 8 | 2 | 10 | `tui/src/local_chatgpt_auth.rs` | `tui/src/LocalChatgptAuth.kt` |
| 1313 | `transport.websocket` | `appservertransport.src.transport.Websocket` | 0 | 15 | 3 | 18 | `app-server-transport/src/transport/websocket.rs` | `appservertransport/src/transport/Websocket.kt` |
| 1314 | `transport.unix_socket_tests` | `appservertransport.src.transport.UnixSocketTests` | 0 | 11 | 0 | 11 | `app-server-transport/src/transport/unix_socket_tests.rs` | `appservertransport/src/transport/UnixSocketTests.kt` |
| 1315 | `tui.markdown_render` | `tui.src.MarkdownRender` | 0 | 62 | 4 | 66 | `tui/src/markdown_render.rs` | `tui/src/MarkdownRender.kt` |
| 1316 | `tui.markdown_render_tests` | `tui.src.MarkdownRenderTests` | 0 | 89 | 0 | 89 | `tui/src/markdown_render_tests.rs` | `tui/src/MarkdownRenderTests.kt` |
| 1317 | `tui.markdown_stream` | `tui.src.MarkdownStream` | 0 | 34 | 1 | 35 | `tui/src/markdown_stream.rs` | `tui/src/MarkdownStream.kt` |
| 1318 | `tui.mention_codec` | `tui.src.MentionCodec` | 0 | 11 | 2 | 13 | `tui/src/mention_codec.rs` | `tui/src/MentionCodec.kt` |
| 1319 | `transport.unix_socket` | `appservertransport.src.transport.UnixSocket` | 0 | 6 | 1 | 7 | `app-server-transport/src/transport/unix_socket.rs` | `appservertransport/src/transport/UnixSocket.kt` |
| 1320 | `tui.model_migration` | `tui.src.ModelMigration` | 0 | 32 | 5 | 37 | `tui/src/model_migration.rs` | `tui/src/ModelMigration.kt` |
| 1321 | `tui.motion` | `tui.src.Motion` | 0 | 9 | 2 | 11 | `tui/src/motion.rs` | `tui/src/Motion.kt` |
| 1322 | `remote_control.websocket` | `appservertransport.src.transport.remotecontrol.Websocket` | 0 | 62 | 8 | 70 | `app-server-transport/src/transport/remote_control/websocket.rs` | `appservertransport/src/transport/remotecontrol/Websocket.kt` |
| 1323 | `notifications.bel` | `tui.src.notifications.Bel` | 0 | 4 | 2 | 6 | `tui/src/notifications/bel.rs` | `tui/src/notifications/Bel.kt` |
| 1324 | `notifications.osc9` | `tui.src.notifications.Osc9` | 0 | 10 | 2 | 12 | `tui/src/notifications/osc9.rs` | `tui/src/notifications/Osc9.kt` |
| 1325 | `remote_control.tests` | `appservertransport.src.transport.remotecontrol.Tests` | 0 | 28 | 3 | 31 | `app-server-transport/src/transport/remote_control/tests.rs` | `appservertransport/src/transport/remotecontrol/Tests.kt` |
| 1326 | `onboarding.auth` | `tui.src.onboarding.auth.Auth` | 0 | 59 | 6 | 65 | `tui/src/onboarding/auth.rs` | `tui/src/onboarding/auth/Auth.kt` |
| 1327 | `auth.headless_chatgpt_login` | `tui.src.onboarding.auth.HeadlessChatgptLogin` | 0 | 9 | 0 | 9 | `tui/src/onboarding/auth/headless_chatgpt_login.rs` | `tui/src/onboarding/auth/HeadlessChatgptLogin.kt` |
| 1328 | `remote_control.segment_tests` | `appservertransport.src.transport.remotecontrol.SegmentTests` | 0 | 8 | 0 | 8 | `app-server-transport/src/transport/remote_control/segment_tests.rs` | `appservertransport/src/transport/remotecontrol/SegmentTests.kt` |
| 1329 | `onboarding.onboarding_screen` | `tui.src.onboarding.OnboardingScreen` | 0 | 27 | 8 | 35 | `tui/src/onboarding/onboarding_screen.rs` | `tui/src/onboarding/OnboardingScreen.kt` |
| 1330 | `onboarding.trust_directory` | `tui.src.onboarding.TrustDirectory` | 0 | 8 | 2 | 10 | `tui/src/onboarding/trust_directory.rs` | `tui/src/onboarding/TrustDirectory.kt` |
| 1331 | `onboarding.welcome` | `tui.src.onboarding.Welcome` | 0 | 11 | 1 | 12 | `tui/src/onboarding/welcome.rs` | `tui/src/onboarding/Welcome.kt` |
| 1332 | `tui.oss_selection` | `tui.src.OssSelection` | 0 | 14 | 4 | 18 | `tui/src/oss_selection.rs` | `tui/src/OssSelection.kt` |
| 1333 | `tui.pager_overlay` | `tui.src.PagerOverlay` | 0 | 74 | 8 | 82 | `tui/src/pager_overlay.rs` | `tui/src/PagerOverlay.kt` |
| 1334 | `tui.permission_compat` | `tui.src.PermissionCompat` | 0 | 2 | 0 | 2 | `tui/src/permission_compat.rs` | `tui/src/PermissionCompat.kt` |
| 1335 | `remote_control.segment` | `appservertransport.src.transport.remotecontrol.Segment` | 0 | 13 | 6 | 19 | `app-server-transport/src/transport/remote_control/segment.rs` | `appservertransport/src/transport/remotecontrol/Segment.kt` |
| 1336 | `remote_control.protocol` | `appservertransport.src.transport.remotecontrol.Protocol` | 0 | 8 | 10 | 18 | `app-server-transport/src/transport/remote_control/protocol.rs` | `appservertransport/src/transport/remotecontrol/Protocol.kt` |
| 1337 | `render.line_utils` | `tui.src.render.LineUtils` | 0 | 4 | 0 | 4 | `tui/src/render/line_utils.rs` | `tui/src/render/LineUtils.kt` |
| 1338 | `tui.resize_reflow_cap` | `tui.src.ResizeReflowCap` | 0 | 9 | 0 | 9 | `tui/src/resize_reflow_cap.rs` | `tui/src/ResizeReflowCap.kt` |
| 1339 | `tui.resume_picker` | `tui.src.ResumePicker` | 0 | 73 | 20 | 93 | `tui/src/resume_picker.rs` | `tui/src/ResumePicker.kt` |
| 1340 | `tui.selection_list` | `tui.src.SelectionList` | 0 | 2 | 0 | 2 | `tui/src/selection_list.rs` | `tui/src/SelectionList.kt` |
| 1341 | `remote_control.enroll` | `appservertransport.src.transport.remotecontrol.Enroll` | 0 | 11 | 2 | 13 | `app-server-transport/src/transport/remote_control/enroll.rs` | `appservertransport/src/transport/remotecontrol/Enroll.kt` |
| 1342 | `tui.session_resume` | `tui.src.SessionResume` | 0 | 11 | 5 | 16 | `tui/src/session_resume.rs` | `tui/src/SessionResume.kt` |
| 1343 | `transport.auth` | `appservertransport.src.transport.Auth` | 0 | 33 | 11 | 44 | `app-server-transport/src/transport/auth.rs` | `appservertransport/src/transport/Auth.kt` |
| 1344 | `tui.shimmer` | `tui.src.Shimmer` | 0 | 3 | 0 | 3 | `tui/src/shimmer.rs` | `tui/src/Shimmer.kt` |
| 1345 | `tui.skills_helpers` | `tui.src.SkillsHelpers` | 0 | 4 | 0 | 4 | `tui/src/skills_helpers.rs` | `tui/src/SkillsHelpers.kt` |
| 1346 | `status.account` | `tui.src.status.Account` | 0 | 0 | 1 | 1 | `tui/src/status/account.rs` | `tui/src/status/Account.kt` |
| 1347 | `status.card` | `tui.src.status.Card` | 0 | 16 | 5 | 21 | `tui/src/status/card.rs` | `tui/src/status/Card.kt` |
| 1348 | `status.format` | `tui.src.status.Format` | 0 | 9 | 1 | 10 | `tui/src/status/format.rs` | `tui/src/status/Format.kt` |
| 1349 | `status.helpers` | `tui.src.status.Helpers` | 0 | 14 | 0 | 14 | `tui/src/status/helpers.rs` | `tui/src/status/Helpers.kt` |
| 1350 | `status.rate_limits` | `tui.src.status.RateLimits` | 0 | 13 | 6 | 19 | `tui/src/status/rate_limits.rs` | `tui/src/status/RateLimits.kt` |
| 1351 | `status.tests` | `tui.src.status.Tests` | 0 | 39 | 0 | 39 | `tui/src/status/tests.rs` | `tui/src/status/Tests.kt` |
| 1352 | `app-server-transport.outgoing_message` | `appservertransport.src.OutgoingMessage` | 0 | 2 | 5 | 7 | `app-server-transport/src/outgoing_message.rs` | `appservertransport/src/OutgoingMessage.kt` |
| 1353 | `streaming.chunking` | `tui.src.streaming.Chunking` | 0 | 20 | 5 | 25 | `tui/src/streaming/chunking.rs` | `tui/src/streaming/Chunking.kt` |
| 1354 | `streaming.commit_tick` | `tui.src.streaming.CommitTick` | 0 | 8 | 2 | 10 | `tui/src/streaming/commit_tick.rs` | `tui/src/streaming/CommitTick.kt` |
| 1355 | `streaming.controller` | `tui.src.streaming.Controller` | 0 | 47 | 3 | 50 | `tui/src/streaming/controller.rs` | `tui/src/streaming/Controller.kt` |
| 1356 | `tui.terminal_probe` | `tui.src.TerminalProbe` | 0 | 28 | 3 | 31 | `tui/src/terminal_probe.rs` | `tui/src/TerminalProbe.kt` |
| 1357 | `tui.terminal_title` | `tui.src.TerminalTitle` | 0 | 12 | 2 | 14 | `tui/src/terminal_title.rs` | `tui/src/TerminalTitle.kt` |
| 1358 | `app-server-test-client.main` | `appservertestclient.src.Main` | 0 | 1 | 0 | 1 | `app-server-test-client/src/main.rs` | `appservertestclient/src/Main.kt` |
| 1359 | `tests.schema_fixtures` | `appserverprotocol.tests.SchemaFixtures` | 0 | 6 | 0 | 6 | `app-server-protocol/tests/schema_fixtures.rs` | `appserverprotocol/tests/SchemaFixtures.kt` |
| 1360 | `app-server-protocol.schema_fixtures` | `appserverprotocol.src.SchemaFixtures` | 0 | 16 | 2 | 18 | `app-server-protocol/src/schema_fixtures.rs` | `appserverprotocol/src/SchemaFixtures.kt` |
| 1361 | `tui.theme_picker` | `tui.src.ThemePicker` | 0 | 25 | 4 | 29 | `tui/src/theme_picker.rs` | `tui/src/ThemePicker.kt` |
| 1362 | `protocol.thread_history` | `appserverprotocol.src.protocol.ThreadHistory` | 0 | 98 | 2 | 100 | `app-server-protocol/src/protocol/thread_history.rs` | `appserverprotocol/src/protocol/ThreadHistory.kt` |
| 1363 | `protocol.serde_helpers` | `appserverprotocol.src.protocol.SerdeHelpers` | 0 | 2 | 0 | 2 | `app-server-protocol/src/protocol/serde_helpers.rs` | `appserverprotocol/src/protocol/SerdeHelpers.kt` |
| 1364 | `tui.transcript_reflow` | `tui.src.TranscriptReflow` | 0 | 26 | 2 | 28 | `tui/src/transcript_reflow.rs` | `tui/src/TranscriptReflow.kt` |
| 1365 | `protocol.mappers` | `appserverprotocol.src.protocol.Mappers` | 0 | 1 | 0 | 1 | `app-server-protocol/src/protocol/mappers.rs` | `appserverprotocol/src/protocol/Mappers.kt` |
| 1366 | `protocol.item_builders` | `appserverprotocol.src.protocol.ItemBuilders` | 0 | 11 | 0 | 11 | `app-server-protocol/src/protocol/item_builders.rs` | `appserverprotocol/src/protocol/ItemBuilders.kt` |
| 1367 | `tui.job_control` | `tui.src.tui.JobControl` | 0 | 8 | 3 | 11 | `tui/src/tui/job_control.rs` | `tui/src/tui/JobControl.kt` |
| 1368 | `tui.keyboard_modes` | `tui.src.tui.KeyboardModes` | 0 | 26 | 2 | 28 | `tui/src/tui/keyboard_modes.rs` | `tui/src/tui/KeyboardModes.kt` |
| 1369 | `tui.update_prompt` | `tui.src.UpdatePrompt` | 0 | 18 | 3 | 21 | `tui/src/update_prompt.rs` | `tui/src/UpdatePrompt.kt` |
| 1370 | `tui.update_versions` | `tui.src.UpdateVersions` | 0 | 10 | 0 | 10 | `tui/src/update_versions.rs` | `tui/src/UpdateVersions.kt` |
| 1371 | `protocol.event_mapping` | `appserverprotocol.src.protocol.EventMapping` | 0 | 7 | 0 | 7 | `app-server-protocol/src/protocol/event_mapping.rs` | `appserverprotocol/src/protocol/EventMapping.kt` |
| 1372 | `protocol.common_tests` | `appserverprotocol.src.protocol.CommonTests` | 0 | 2 | 0 | 2 | `app-server-protocol/src/protocol/common_tests.rs` | `appserverprotocol/src/protocol/CommonTests.kt` |
| 1373 | `tui.voice` | `tui.src.Voice` | 0 | 23 | 3 | 26 | `tui/src/voice.rs` | `tui/src/Voice.kt` |
| 1374 | `tui.width` | `tui.src.Width` | 0 | 4 | 0 | 4 | `tui/src/width.rs` | `tui/src/Width.kt` |
| 1375 | `protocol.common` | `appserverprotocol.src.protocol.Common` | 0 | 47 | 15 | 62 | `app-server-protocol/src/protocol/common.rs` | `appserverprotocol/src/protocol/Common.kt` |
| 1376 | `tui.wrapping` | `tui.src.Wrapping` | 0 | 90 | 3 | 93 | `tui/src/wrapping.rs` | `tui/src/Wrapping.kt` |
| 1377 | `tests.all` | `tui.tests.All` | 0 | 0 | 0 | 0 | `tui/tests/all.rs` | `tui/tests/All.kt` |
| 1378 | `tests.manager_dependency_regression` | `tui.tests.ManagerDependencyRegression` | 0 | 2 | 0 | 2 | `tui/tests/manager_dependency_regression.rs` | `tui/tests/ManagerDependencyRegression.kt` |
| 1379 | `app-server-protocol.jsonrpc_lite` | `appserverprotocol.src.JsonrpcLite` | 0 | 1 | 8 | 9 | `app-server-protocol/src/jsonrpc_lite.rs` | `appserverprotocol/src/JsonrpcLite.kt` |
| 1380 | `suite.no_panic_on_startup` | `tui.tests.suite.NoPanicOnStartup` | 0 | 2 | 1 | 3 | `tui/tests/suite/no_panic_on_startup.rs` | `tui/tests/suite/NoPanicOnStartup.kt` |
| 1381 | `suite.resize_reflow` | `tui.tests.suite.ResizeReflow` | 0 | 17 | 1 | 18 | `tui/tests/suite/resize_reflow.rs` | `tui/tests/suite/ResizeReflow.kt` |
| 1382 | `suite.status_indicator` | `tui.tests.suite.StatusIndicator` | 0 | 1 | 0 | 1 | `tui/tests/suite/status_indicator.rs` | `tui/tests/suite/StatusIndicator.kt` |
| 1383 | `suite.vt100_history` | `tui.tests.suite.Vt100History` | 0 | 9 | 1 | 10 | `tui/tests/suite/vt100_history.rs` | `tui/tests/suite/Vt100History.kt` |
| 1384 | `suite.vt100_live_commit` | `tui.tests.suite.Vt100LiveCommit` | 0 | 1 | 0 | 1 | `tui/tests/suite/vt100_live_commit.rs` | `tui/tests/suite/Vt100LiveCommit.kt` |
| 1385 | `tests.test_backend` | `tui.tests.TestBackend` | 0 | 0 | 0 | 0 | `tui/tests/test_backend.rs` | `tui/tests/TestBackend.kt` |
| 1386 | `uds.lib_tests` | `uds.src.LibTests` | 0 | 5 | 0 | 5 | `uds/src/lib_tests.rs` | `uds/src/LibTests.kt` |
| 1387 | `absolute-path.absolutize` | `utils.absolutepath.src.Absolutize` | 0 | 14 | 0 | 14 | `utils/absolute-path/src/absolutize.rs` | `utils/absolutepath/src/Absolutize.kt` |
| 1388 | `cli.config_override` | `utils.cli.src.ConfigOverride` | 0 | 11 | 1 | 12 | `utils/cli/src/config_override.rs` | `utils/cli/src/ConfigOverride.kt` |
| 1389 | `cli.shared_options` | `utils.cli.src.SharedOptions` | 0 | 2 | 1 | 3 | `utils/cli/src/shared_options.rs` | `utils/cli/src/SharedOptions.kt` |
| 1390 | `image.error` | `utils.image.src.Error` | 0 | 2 | 1 | 3 | `utils/image/src/error.rs` | `utils/image/src/Error.kt` |
| 1391 | `output-truncation.truncate_tests` | `utils.outputtruncation.src.TruncateTests` | 0 | 15 | 0 | 15 | `utils/output-truncation/src/truncate_tests.rs` | `utils/outputtruncation/src/TruncateTests.kt` |
| 1392 | `app-server-protocol.export` | `appserverprotocol.src.Export` | 0 | 117 | 6 | 123 | `app-server-protocol/src/export.rs` | `appserverprotocol/src/Export.kt` |
| 1393 | `path-utils.path_utils_tests` | `utils.pathutils.src.PathUtilsTests` | 0 | 9 | 0 | 9 | `utils/path-utils/src/path_utils_tests.rs` | `utils/pathutils/src/PathUtilsTests.kt` |
| 1394 | `plugins.mcp_connector` | `utils.plugins.src.McpConnector` | 0 | 4 | 0 | 4 | `utils/plugins/src/mcp_connector.rs` | `utils/plugins/src/McpConnector.kt` |
| 1395 | `plugins.mention_syntax` | `utils.plugins.src.MentionSyntax` | 0 | 0 | 0 | 0 | `utils/plugins/src/mention_syntax.rs` | `utils/plugins/src/MentionSyntax.kt` |
| 1396 | `plugins.plugin_namespace` | `utils.plugins.src.PluginNamespace` | 0 | 5 | 1 | 6 | `utils/plugins/src/plugin_namespace.rs` | `utils/plugins/src/PluginNamespace.kt` |
| 1397 | `bin.export` | `appserverprotocol.src.bin.Export` | 0 | 1 | 1 | 2 | `app-server-protocol/src/bin/export.rs` | `appserverprotocol/src/bin/Export.kt` |
| 1398 | `app-server-client.remote` | `appserverclient.src.Remote` | 0 | 21 | 4 | 25 | `app-server-client/src/remote.rs` | `appserverclient/src/Remote.kt` |
| 1399 | `pty.process_group` | `utils.pty.src.ProcessGroup` | 0 | 14 | 0 | 14 | `utils/pty/src/process_group.rs` | `utils/pty/src/ProcessGroup.kt` |
| 1400 | `pty.pty` | `utils.pty.src.Pty` | 0 | 12 | 2 | 14 | `utils/pty/src/pty.rs` | `utils/pty/src/Pty.kt` |
| 1401 | `pty.tests` | `utils.pty.src.Tests` | 0 | 30 | 0 | 30 | `utils/pty/src/tests.rs` | `utils/pty/src/Tests.kt` |
| 1402 | `win.conpty` | `utils.pty.src.win.Conpty` | 0 | 11 | 5 | 16 | `utils/pty/src/win/conpty.rs` | `utils/pty/src/win/Conpty.kt` |
| 1403 | `win.procthreadattr` | `utils.pty.src.win.Procthreadattr` | 0 | 4 | 1 | 5 | `utils/pty/src/win/procthreadattr.rs` | `utils/pty/src/win/Procthreadattr.kt` |
| 1404 | `analytics.reducer` | `analytics.src.Reducer` | 0 | 42 | 11 | 53 | `analytics/src/reducer.rs` | `analytics/src/Reducer.kt` |
| 1405 | `sandbox-summary.config_summary` | `utils.sandboxsummary.src.ConfigSummary` | 0 | 1 | 0 | 1 | `utils/sandbox-summary/src/config_summary.rs` | `utils/sandboxsummary/src/ConfigSummary.kt` |
| 1406 | `sleep-inhibitor.dummy` | `utils.sleepinhibitor.src.Dummy` | 0 | 3 | 1 | 4 | `utils/sleep-inhibitor/src/dummy.rs` | `utils/sleepinhibitor/src/Dummy.kt` |
| 1407 | `sleep-inhibitor.iokit_bindings` | `utils.sleepinhibitor.src.IokitBindings` | 0 | 0 | 7 | 7 | `utils/sleep-inhibitor/src/iokit_bindings.rs` | `utils/sleepinhibitor/src/IokitBindings.kt` |
| 1408 | `sleep-inhibitor.linux_inhibitor` | `utils.sleepinhibitor.src.LinuxInhibitor` | 0 | 7 | 3 | 10 | `utils/sleep-inhibitor/src/linux_inhibitor.rs` | `utils/sleepinhibitor/src/LinuxInhibitor.kt` |
| 1409 | `sleep-inhibitor.macos` | `utils.sleepinhibitor.src.Macos` | 0 | 5 | 5 | 10 | `utils/sleep-inhibitor/src/macos.rs` | `utils/sleepinhibitor/src/Macos.kt` |
| 1410 | `sleep-inhibitor.windows_inhibitor` | `utils.sleepinhibitor.src.WindowsInhibitor` | 0 | 5 | 2 | 7 | `utils/sleep-inhibitor/src/windows_inhibitor.rs` | `utils/sleepinhibitor/src/WindowsInhibitor.kt` |
| 1411 | `stream-parser.assistant_text` | `utils.streamparser.src.AssistantText` | 0 | 7 | 2 | 9 | `utils/stream-parser/src/assistant_text.rs` | `utils/streamparser/src/AssistantText.kt` |
| 1412 | `stream-parser.citation` | `utils.streamparser.src.Citation` | 0 | 13 | 3 | 16 | `utils/stream-parser/src/citation.rs` | `utils/streamparser/src/Citation.kt` |
| 1413 | `stream-parser.inline_hidden_tag` | `utils.streamparser.src.InlineHiddenTag` | 0 | 14 | 6 | 20 | `utils/stream-parser/src/inline_hidden_tag.rs` | `utils/streamparser/src/InlineHiddenTag.kt` |
| 1414 | `stream-parser.proposed_plan` | `utils.streamparser.src.ProposedPlan` | 0 | 13 | 4 | 17 | `utils/stream-parser/src/proposed_plan.rs` | `utils/streamparser/src/ProposedPlan.kt` |
| 1415 | `stream-parser.stream_text` | `utils.streamparser.src.StreamText` | 0 | 2 | 2 | 4 | `utils/stream-parser/src/stream_text.rs` | `utils/streamparser/src/StreamText.kt` |
| 1416 | `analytics.facts` | `analytics.src.Facts` | 0 | 3 | 33 | 36 | `analytics/src/facts.rs` | `analytics/src/Facts.kt` |
| 1417 | `stream-parser.utf8_stream` | `utils.streamparser.src.Utf8Stream` | 0 | 13 | 2 | 15 | `utils/stream-parser/src/utf8_stream.rs` | `utils/streamparser/src/Utf8Stream.kt` |
| 1418 | `analytics.events` | `analytics.src.Events` | 0 | 17 | 35 | 52 | `analytics/src/events.rs` | `analytics/src/Events.kt` |
| 1419 | `truncate.tests` | `utils.string.src.truncate.Tests` | 0 | 10 | 0 | 10 | `utils/string/src/truncate/tests.rs` | `utils/string/src/truncate/Tests.kt` |
| 1420 | `windows-sandbox-rs.build` | `windowssandboxrs.Build` | 0 | 1 | 0 | 1 | `windows-sandbox-rs/build.rs` | `windowssandboxrs/Build.kt` |
| 1421 | `analytics.client_tests` | `analytics.src.ClientTests` | 0 | 13 | 0 | 13 | `analytics/src/client_tests.rs` | `analytics/src/ClientTests.kt` |
| 1422 | `windows-sandbox-rs.allow` | `windowssandboxrs.src.Allow` | 0 | 7 | 1 | 8 | `windows-sandbox-rs/src/allow.rs` | `windowssandboxrs/src/Allow.kt` |
| 1423 | `windows-sandbox-rs.audit` | `windowssandboxrs.src.Audit` | 0 | 7 | 0 | 7 | `windows-sandbox-rs/src/audit.rs` | `windowssandboxrs/src/Audit.kt` |
| 1424 | `bin.command_runner` | `windowssandboxrs.src.bin.CommandRunner` | 0 | 2 | 0 | 2 | `windows-sandbox-rs/src/bin/command_runner.rs` | `windowssandboxrs/src/bin/CommandRunner.kt` |
| 1425 | `bin.setup_main` | `windowssandboxrs.src.bin.SetupMain` | 0 | 2 | 0 | 2 | `windows-sandbox-rs/src/bin/setup_main.rs` | `windowssandboxrs/src/bin/SetupMain.kt` |
| 1426 | `windows-sandbox-rs.cap` | `windowssandboxrs.src.Cap` | 0 | 6 | 1 | 7 | `windows-sandbox-rs/src/cap.rs` | `windowssandboxrs/src/Cap.kt` |
| 1427 | `windows-sandbox-rs.desktop` | `windowssandboxrs.src.Desktop` | 0 | 5 | 2 | 7 | `windows-sandbox-rs/src/desktop.rs` | `windowssandboxrs/src/Desktop.kt` |
| 1428 | `analytics.client` | `analytics.src.Client` | 0 | 29 | 2 | 31 | `analytics/src/client.rs` | `analytics/src/Client.kt` |
| 1429 | `elevated.command_runner_win` | `windowssandboxrs.src.elevated.CommandRunnerWin` | 0 | 13 | 2 | 15 | `windows-sandbox-rs/src/elevated/command_runner_win.rs` | `windowssandboxrs/src/elevated/CommandRunnerWin.kt` |
| 1430 | `elevated.cwd_junction` | `windowssandboxrs.src.elevated.CwdJunction` | 0 | 3 | 0 | 3 | `windows-sandbox-rs/src/elevated/cwd_junction.rs` | `windowssandboxrs/src/elevated/CwdJunction.kt` |
| 1431 | `elevated.ipc_framed` | `windowssandboxrs.src.elevated.IpcFramed` | 0 | 5 | 11 | 16 | `windows-sandbox-rs/src/elevated/ipc_framed.rs` | `windowssandboxrs/src/elevated/IpcFramed.kt` |
| 1432 | `elevated.runner_client` | `windowssandboxrs.src.elevated.RunnerClient` | 0 | 7 | 1 | 8 | `windows-sandbox-rs/src/elevated/runner_client.rs` | `windowssandboxrs/src/elevated/RunnerClient.kt` |
| 1433 | `elevated.runner_pipe` | `windowssandboxrs.src.elevated.RunnerPipe` | 0 | 4 | 0 | 4 | `windows-sandbox-rs/src/elevated/runner_pipe.rs` | `windowssandboxrs/src/elevated/RunnerPipe.kt` |
| 1434 | `windows-sandbox-rs.elevated_impl` | `windowssandboxrs.src.ElevatedImpl` | 0 | 9 | 2 | 11 | `windows-sandbox-rs/src/elevated_impl.rs` | `windowssandboxrs/src/ElevatedImpl.kt` |
| 1435 | `windows-sandbox-rs.env` | `windowssandboxrs.src.Env` | 0 | 7 | 0 | 7 | `windows-sandbox-rs/src/env.rs` | `windowssandboxrs/src/Env.kt` |
| 1436 | `windows-sandbox-rs.firewall` | `windowssandboxrs.src.Firewall` | 0 | 11 | 1 | 12 | `windows-sandbox-rs/src/firewall.rs` | `windowssandboxrs/src/Firewall.kt` |
| 1437 | `windows-sandbox-rs.helper_materialization` | `windowssandboxrs.src.HelperMaterialization` | 0 | 26 | 2 | 28 | `windows-sandbox-rs/src/helper_materialization.rs` | `windowssandboxrs/src/HelperMaterialization.kt` |
| 1438 | `windows-sandbox-rs.hide_users` | `windowssandboxrs.src.HideUsers` | 0 | 5 | 0 | 5 | `windows-sandbox-rs/src/hide_users.rs` | `windowssandboxrs/src/HideUsers.kt` |
| 1439 | `windows-sandbox-rs.identity` | `windowssandboxrs.src.Identity` | 0 | 6 | 2 | 8 | `windows-sandbox-rs/src/identity.rs` | `windowssandboxrs/src/Identity.kt` |
| 1440 | `analytics.analytics_client_tests` | `analytics.src.AnalyticsClientTests` | 0 | 71 | 0 | 71 | `analytics/src/analytics_client_tests.rs` | `analytics/src/AnalyticsClientTests.kt` |
| 1441 | `windows-sandbox-rs.path_normalization` | `windowssandboxrs.src.PathNormalization` | 0 | 3 | 0 | 3 | `windows-sandbox-rs/src/path_normalization.rs` | `windowssandboxrs/src/PathNormalization.kt` |
| 1442 | `windows-sandbox-rs.policy` | `windowssandboxrs.src.Policy` | 0 | 4 | 0 | 4 | `windows-sandbox-rs/src/policy.rs` | `windowssandboxrs/src/Policy.kt` |
| 1443 | `windows-sandbox-rs.proc_thread_attr` | `windowssandboxrs.src.ProcThreadAttr` | 0 | 5 | 1 | 6 | `windows-sandbox-rs/src/proc_thread_attr.rs` | `windowssandboxrs/src/ProcThreadAttr.kt` |
| 1444 | `windows-sandbox-rs.process` | `windowssandboxrs.src.Process` | 0 | 5 | 4 | 9 | `windows-sandbox-rs/src/process.rs` | `windowssandboxrs/src/Process.kt` |
| 1445 | `windows-sandbox-rs.read_acl_mutex` | `windowssandboxrs.src.ReadAclMutex` | 0 | 3 | 1 | 4 | `windows-sandbox-rs/src/read_acl_mutex.rs` | `windowssandboxrs/src/ReadAclMutex.kt` |
| 1446 | `windows-sandbox-rs.sandbox_users` | `windowssandboxrs.src.SandboxUsers` | 0 | 14 | 3 | 17 | `windows-sandbox-rs/src/sandbox_users.rs` | `windowssandboxrs/src/SandboxUsers.kt` |
| 1447 | `windows-sandbox-rs.sandbox_utils` | `windowssandboxrs.src.SandboxUtils` | 0 | 3 | 0 | 3 | `windows-sandbox-rs/src/sandbox_utils.rs` | `windowssandboxrs/src/SandboxUtils.kt` |
| 1448 | `windows-sandbox-rs.setup_error` | `windowssandboxrs.src.SetupError` | 0 | 17 | 3 | 20 | `windows-sandbox-rs/src/setup_error.rs` | `windowssandboxrs/src/SetupError.kt` |
| 1449 | `windows-sandbox-rs.setup_main_win` | `windowssandboxrs.src.SetupMainWin` | 0 | 13 | 3 | 16 | `windows-sandbox-rs/src/setup_main_win.rs` | `windowssandboxrs/src/SetupMainWin.kt` |
| 1450 | `windows-sandbox-rs.setup_orchestrator` | `windowssandboxrs.src.SetupOrchestrator` | 0 | 60 | 8 | 68 | `windows-sandbox-rs/src/setup_orchestrator.rs` | `windowssandboxrs/src/SetupOrchestrator.kt` |
| 1451 | `windows-sandbox-rs.spawn_prep` | `windowssandboxrs.src.SpawnPrep` | 0 | 14 | 4 | 18 | `windows-sandbox-rs/src/spawn_prep.rs` | `windowssandboxrs/src/SpawnPrep.kt` |
| 1452 | `windows-sandbox-rs.ssh_config_dependencies` | `windowssandboxrs.src.SshConfigDependencies` | 0 | 9 | 0 | 9 | `windows-sandbox-rs/src/ssh_config_dependencies.rs` | `windowssandboxrs/src/SshConfigDependencies.kt` |
| 1453 | `agent-graph-store.types` | `agentgraphstore.src.Types` | 0 | 1 | 1 | 2 | `agent-graph-store/src/types.rs` | `agentgraphstore/src/Types.kt` |
| 1454 | `backends.elevated` | `windowssandboxrs.src.unifiedexec.backends.Elevated` | 0 | 1 | 0 | 1 | `windows-sandbox-rs/src/unified_exec/backends/elevated.rs` | `windowssandboxrs/src/unifiedexec/backends/Elevated.kt` |
| 1455 | `backends.legacy` | `windowssandboxrs.src.unifiedexec.backends.Legacy` | 0 | 7 | 1 | 8 | `windows-sandbox-rs/src/unified_exec/backends/legacy.rs` | `windowssandboxrs/src/unifiedexec/backends/Legacy.kt` |
| 1456 | `backends.windows_common` | `windowssandboxrs.src.unifiedexec.backends.WindowsCommon` | 0 | 7 | 0 | 7 | `windows-sandbox-rs/src/unified_exec/backends/windows_common.rs` | `windowssandboxrs/src/unifiedexec/backends/WindowsCommon.kt` |
| 1457 | `unified_exec.tests` | `windowssandboxrs.src.unifiedexec.Tests` | 0 | 18 | 0 | 18 | `windows-sandbox-rs/src/unified_exec/tests.rs` | `windowssandboxrs/src/unifiedexec/Tests.kt` |
| 1458 | `windows-sandbox-rs.wfp` | `windowssandboxrs.src.Wfp` | 0 | 21 | 3 | 24 | `windows-sandbox-rs/src/wfp.rs` | `windowssandboxrs/src/Wfp.kt` |
| 1459 | `windows-sandbox-rs.wfp_filter_specs` | `windowssandboxrs.src.WfpFilterSpecs` | 0 | 0 | 2 | 2 | `windows-sandbox-rs/src/wfp_filter_specs.rs` | `windowssandboxrs/src/WfpFilterSpecs.kt` |
| 1460 | `windows-sandbox-rs.wfp_setup` | `windowssandboxrs.src.WfpSetup` | 0 | 5 | 2 | 7 | `windows-sandbox-rs/src/wfp_setup.rs` | `windowssandboxrs/src/WfpSetup.kt` |
| 1461 | `windows-sandbox-rs.winutil` | `windowssandboxrs.src.Winutil` | 0 | 10 | 0 | 10 | `windows-sandbox-rs/src/winutil.rs` | `windowssandboxrs/src/Winutil.kt` |
| 1462 | `mcp-server.main` | `mcpserver.src.Main` | 0 | 1 | 0 | 1 | `mcp-server/src/main.rs` | `mcpserver/src/Main.kt` |

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

