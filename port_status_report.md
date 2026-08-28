# Code Port - Progress Report

**Generated:** 2026-08-28
**Source:** codex-rs
**Target:** src/commonMain/kotlin/io/github/kotlinmania/codex

## Executive Summary

| Metric | Count | Percentage |
|--------|-------|------------|
| Function parity | 302/24552 matched (target 825) | 1.2% |
| Class/type parity | 188/4246 matched (target 800) | 4.4% |
| Combined symbol parity | 490/28798 matched (target 1625) | 1.7% |
| Average function body similarity | 0.21 | inline-code cosine |
| Average documentation similarity | 0.30 | doc text cosine |
| Missing source functions | 23299 | 0% parity until ported |
| Missing source classes/types | 3798 | 0% parity until ported |
| Missing source symbol files | 1435 | 27097 symbols |
| Cheat/scoring failures | 29 | forced to 0% |
| Total source files | 1756 | 100% |
| Target units (paired) | 172 | - |
| Target files (total) | 172 | - |
| Porting progress | 115 | 6.5% (matched) |
| Missing files | 1462 | 83.3% |
| Reexport/wiring files | 179 | consult-only |

## Port Quality Analysis

**Average Function Similarity:** 0.21

Similarity in this report is the required function-by-function body/parameter score. Class/type parity and symbol deficits are reported beside it; whole-file shape is diagnostic only.

**Work Distribution:**
- Critical (<0.60): 103 files (89.6% of matched)
- Needs review (0.60-0.84): 7 files (6.1% of matched)

## Worst Function Scores First

Every matched file is listed from lowest function body/parameter similarity upward. Missing symbol names are not capped.

| Rank | Source | Target | Function similarity | Functions | Missing functions | Types | Missing types | Tests | Symbol deficit | Priority |
|------|--------|--------|---------------------|-----------|-------------------|-------|---------------|-------|----------------|----------|
| 1 | `protocol.user_input` | `protocol.UserInput [ZERO]` | 0.00 | 0/6 matched (target 0) | `new`, `map_range`, `set_placeholder`, `_placeholder_for_conversion_only`, `placeholder`, `from` | 1/3 matched (target 4) | `TextElement`, `ByteRange` | - | 8 | 114080904.0 |
| 2 | `protocol.parse_command` | `protocol.ParseCommand [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/0 matched (target 1) | _none_ | 1/1 matched (target 5) | _none_ | - | 0 | 6000110.0 |
| 3 | `protocol.account` | `protocol.Account [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/8 matched (target 0) | `is_team_like`, `is_business_like`, `is_workspace_account`, `from`, `usage_based_plan_types_use_expected_wire_names`, `plan_family_helpers_group_usage_based_variants_with_existing_plans`, `workspace_account_helper_includes_usage_based_workspace_plans`, `auth_plan_type_converts_to_account_plan_type` | 1/2 matched (target 1) | `ProviderAccount` | 0/4 | 9 | 3091010.0 |
| 4 | `command_safety.is_safe_command` | `commandsafety.IsSafeCommand [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 3/25 matched (target 3) | `is_safe_powershell_words`, `git_branch_is_read_only`, `git_has_unsafe_global_option`, `git_subcommand_args_are_read_only`, `vec_str`, `known_safe_examples`, `git_branch_mutating_flags_are_not_safe`, `git_branch_global_options_respect_safety_rules`, `git_first_positional_is_the_subcommand`, `git_output_flags_are_not_safe`, `git_global_override_flags_are_not_safe`, `cargo_check_is_not_safe`, `zsh_lc_safe_command_sequence`, `unknown_or_partial`, `base64_output_options_are_unsafe`, `ripgrep_rules`, `windows_powershell_full_path_is_safe`, `windows_git_full_path_is_safe`, `bash_lc_safe_examples`, `bash_lc_safe_examples_with_operators`, `bash_lc_unsafe_examples`, `direct_powershell_words_use_windows_safelist` | 0/0 matched | _none_ | 0/18 | 22 | 1222510.0 |
| 5 | `core.compact` | `endpoint.Compact [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0/15 matched (target 4) | `should_use_remote_compact_task`, `run_inline_auto_compact_task`, `run_compact_task`, `run_compact_task_inner`, `run_compact_task_inner_impl`, `begin`, `track`, `compaction_status_from_result`, `content_items_to_text`, `collect_user_messages`, `is_summary_message`, `insert_initial_context_before_last_real_user_or_summary`, `build_compacted_history`, `build_compacted_history_with_limit`, `drain_to_completed` | 0/2 matched | `InitialContextInjection`, `CompactionAnalyticsAttempt` | - | 17 | 1171710.0 |
| 6 | `command_safety.is_dangerous_command` | `commandsafety.IsDangerousCommand [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 2/13 matched (target 3) | `is_dangerous_powershell_words`, `is_git_global_option_with_value`, `is_git_global_option_with_inline_value`, `git_global_option_requires_prompt`, `executable_name_lookup_key`, `find_git_subcommand`, `vec_str`, `rm_rf_is_dangerous`, `rm_f_is_dangerous`, `git_dash_c_requires_prompt`, `direct_powershell_words_reuse_windows_dangerous_detection` | 0/0 matched | _none_ | 0/5 | 11 | 1111310.0 |
| 7 | `core.spawn` | `core.Spawn [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/1 matched (target 0) | `spawn_child_async` | 1/2 matched | `SpawnChildRequest` | - | 2 | 1020310.0 |
| 8 | `core-plugins.loader` | `config.ConfigLoader [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0/40 matched (target 5) | `log_plugin_load_errors`, `into_mcp_servers`, `load_plugins_from_layer_stack`, `remote_installed_plugins_to_config`, `refresh_curated_plugin_cache`, `curated_plugin_cache_version`, `refresh_non_curated_plugin_cache`, `refresh_non_curated_plugin_cache_force_reinstall`, `refresh_non_curated_plugin_cache_with_mode`, `configured_plugins_from_stack`, `is_full_git_sha`, `configured_plugins_from_user_config_value`, `configured_plugins_from_codex_home`, `configured_plugin_ids`, `curated_plugin_ids_from_config_keys`, `non_curated_plugin_ids_from_config_keys`, `configured_curated_plugin_ids_from_codex_home`, `load_plugin`, `apply_plugin_mcp_server_policy`, `has_enabled_skills`, `load_plugin_skills`, `plugin_skill_roots`, `default_skill_roots`, `plugin_mcp_config_paths`, `default_mcp_config_paths`, `load_plugin_apps`, `plugin_app_config_paths`, `default_app_config_paths`, `load_plugin_hooks`, `append_plugin_hook_file`, `load_apps_from_paths`, `plugin_telemetry_metadata_from_root`, `load_plugin_mcp_servers`, `installed_plugin_telemetry_metadata`, `load_mcp_servers_from_file`, `normalize_plugin_mcp_servers`, `normalize_plugin_mcp_server_value`, `materialize_marketplace_plugin_source`, `clone_git_plugin_source`, `run_git` | 0/8 matched (target 2) | `NonCuratedCacheRefreshMode`, `PluginMcpServersFile`, `PluginMcpFile`, `PluginAppFile`, `PluginAppConfig`, `ResolvedPluginSkills`, `PluginMcpDiscovery`, `MaterializedMarketplacePluginSource` | - | 48 | 484810.0 |
| 9 | `protocol.config_types` | `protocol.ConfigTypes [ZERO]` | 0.00 | 0/27 matched (target 0) | `schema_name`, `json_schema`, `default`, `string_enum_schema_with_description`, `merge`, `from`, `timeout`, `refresh_interval`, `default_provider_auth_timeout_ms`, `default_provider_auth_refresh_interval_ms`, `non_zero_u64`, `default_provider_auth_cwd`, `is_default_provider_auth_cwd`, `display_name`, `is_tui_visible`, `allows_request_user_input`, `settings_ref`, `model`, `reasoning_effort`, `with_updates`, `apply_mask`, `apply_mask_can_clear_optional_fields`, `mode_kind_deserializes_alias_values_to_default`, `approvals_reviewer_serializes_auto_review_and_accepts_legacy_guardian_subagent`, `tui_visible_collaboration_modes_match_mode_kind_visibility`, `web_search_location_merge_prefers_overlay_values`, `web_search_tool_config_merge_prefers_overlay_values` | 5/26 matched (target 6) | `ApprovalsReviewer`, `ShellEnvironmentPolicyInherit`, `EnvironmentVariablePattern`, `ShellEnvironmentPolicy`, `WindowsSandboxLevel`, `Personality`, `WebSearchMode`, `WebSearchContextSize`, `WebSearchLocation`, `WebSearchToolConfig`, `WebSearchFilters`, `WebSearchUserLocationType`, `WebSearchUserLocation`, `WebSearchConfig`, `ServiceTier`, `ModelProviderAuthInfo`, `AltScreenMode`, `ModeKind`, `CollaborationMode`, `Settings`, `CollaborationModeMask` | 0/6 | 48 | 485310.0 |
| 10 | `command_safety.windows_dangerous_commands` | `commandsafety.WindowsDangerousCommands [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 10/56 matched (target 11) | `is_dangerous_powershell_words`, `split_embedded_cmd_operators`, `has_force_delete_cmdlet`, `has_force_flag_cmd`, `has_recursive_flag_cmd`, `has_quiet_flag_cmd`, `vec_str`, `powershell_start_process_url_is_dangerous`, `powershell_start_process_url_with_trailing_semicolon_is_dangerous`, `powershell_start_process_local_is_not_flagged`, `cmd_start_with_url_is_dangerous`, `msedge_with_url_is_dangerous`, `explorer_with_directory_is_not_flagged`, `powershell_remove_item_force_is_dangerous`, `powershell_remove_item_recurse_force_is_dangerous`, `powershell_ri_alias_force_is_dangerous`, `powershell_remove_item_without_force_is_not_flagged`, `cmd_del_force_is_dangerous`, `cmd_erase_force_is_dangerous`, `cmd_del_without_force_is_not_flagged`, `cmd_rd_recursive_is_dangerous`, `cmd_rd_without_quiet_is_not_flagged`, `cmd_rmdir_recursive_is_dangerous`, `powershell_remove_item_path_recurse_force_is_dangerous`, `powershell_remove_item_force_with_semicolon_is_dangerous`, `powershell_remove_item_force_inside_block_is_dangerous`, `powershell_remove_item_force_inside_brackets_is_dangerous`, `cmd_del_path_containing_f_is_not_flagged`, `cmd_rd_path_containing_s_is_not_flagged`, `cmd_bypass_chained_del_is_dangerous`, `powershell_chained_no_space_is_dangerous`, `powershell_comma_separated_is_dangerous`, `cmd_echo_del_is_not_dangerous`, `cmd_del_single_string_argument_is_dangerous`, `cmd_del_chained_single_string_argument_is_dangerous`, `cmd_chained_no_space_del_is_dangerous`, `cmd_chained_andand_no_space_del_is_dangerous`, `cmd_chained_oror_no_space_del_is_dangerous`, `cmd_start_url_single_string_is_dangerous`, `cmd_chained_no_space_rmdir_is_dangerous`, `cmd_del_force_uppercase_flag_is_dangerous`, `cmdexe_r_del_force_is_dangerous`, `cmd_start_quoted_url_single_string_is_dangerous`, `cmd_start_title_then_url_is_dangerous`, `powershell_rm_alias_force_is_dangerous`, `powershell_benign_force_separate_command_is_not_dangerous` | 1/1 matched | _none_ | 0/40 | 46 | 465710.0 |
| 11 | `backend-client.types` | `config.Types [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/19 matched (target 7) | `text`, `text_values`, `diff_text`, `unified_diff`, `message_texts`, `user_prompt`, `error_summary`, `is_assistant`, `summary`, `assistant_text_messages`, `user_text_prompt`, `assistant_error_message`, `deserialize_vec`, `fixture`, `unified_diff_prefers_current_diff_task_turn`, `unified_diff_falls_back_to_pr_output_diff`, `assistant_text_messages_extracts_text_content`, `user_text_prompt_joins_parts_with_spacing`, `assistant_error_message_combines_code_and_message` | 0/13 matched (target 28) | `CodeTaskDetailsResponse`, `Turn`, `TurnItem`, `ContentFragment`, `StructuredContent`, `DiffPayload`, `Worklog`, `WorklogMessage`, `Author`, `WorklogContent`, `TurnError`, `CodeTaskDetailsResponseExt`, `TurnAttemptsSiblingTurnsResponse` | 0/6 | 32 | 323210.0 |
| 12 | `sandboxing.seatbelt` | `core.Seatbelt [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 2/24 matched (target 2) | `is_loopback_host`, `proxy_scheme_default_port`, `proxy_loopback_ports_from_env`, `default`, `proxy_policy_inputs`, `normalize_path_for_sandbox`, `unix_socket_path_params`, `unix_socket_path_param_key`, `unix_socket_dir_params`, `unix_socket_policy`, `dynamic_network_policy`, `dynamic_network_policy_for_network`, `root_absolute_path`, `build_seatbelt_access_policy`, `seatbelt_protected_metadata_name_regex`, `protected_metadata_names_for_writable_root`, `build_seatbelt_unreadable_glob_policy`, `canonicalize_glob_static_prefix_for_sandbox`, `seatbelt_regex_for_unreadable_glob`, `create_seatbelt_command_args_for_legacy_policy`, `confstr`, `confstr_path` | 0/5 matched (target 0) | `ProxyPolicyInputs`, `UnixDomainSocketPolicy`, `UnixSocketPathParam`, `SeatbeltAccessRoot`, `CreateSeatbeltCommandArgsParams` | - | 27 | 272910.0 |
| 13 | `lmstudio.client` | `client.ModelClient [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0/16 matched (target 34) | `try_from_provider`, `check_server`, `load_model`, `fetch_models`, `find_lms`, `find_lms_with_home_dir`, `download_model`, `from_host_root`, `test_fetch_models_happy_path`, `test_fetch_models_no_data_array`, `test_fetch_models_server_error`, `test_check_server_happy_path`, `test_check_server_error`, `test_find_lms`, `test_find_lms_with_mock_home`, `test_from_host_root` | 0/1 matched (target 5) | `LMStudioClient` | 0/9 | 17 | 171710.0 |
| 14 | `command_safety.windows_safe_commands` | `commandsafety.WindowsSafeCommands [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 7/19 matched (target 10) | `join_arguments_as_script`, `quote_argument`, `is_safe_powershell_words`, `vec_str`, `recognizes_safe_powershell_wrappers`, `accepts_full_path_powershell_invocations`, `allows_read_only_pipelines_and_git_usage`, `rejects_git_global_override_options`, `rejects_powershell_commands_with_side_effects`, `accepts_constant_expression_arguments`, `rejects_dynamic_arguments`, `uses_invoked_powershell_variant_for_parsing` | 0/0 matched | _none_ | 0/8 | 12 | 121910.0 |
| 15 | `core.message_history` | `protocol.MessageHistory [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/10 matched (target 0) | `history_filepath`, `append_entry`, `enforce_history_limit`, `trim_target_bytes`, `history_metadata`, `lookup`, `ensure_owner_only_permissions`, `history_metadata_for_file`, `lookup_history_entry`, `history_log_id` | 1/1 matched | _none_ | - | 10 | 101110.0 |
| 16 | `tools.sandboxing` | `tools.Sandboxing [ZERO]` | 0.00 | 8/16 matched (target 12) | `bash`, `proposed_execpolicy_amendment`, `default_exec_approval_requirement`, `sandbox_override_for_first_attempt`, `managed_network_for_sandbox_permissions`, `exec_approval_requirement`, `permission_request_payload`, `network_approval_spec` | 9/11 matched (target 19) | `PermissionRequestPayload`, `ExecApprovalRequirement` | - | 10 | 102710.0 |
| 17 | `write.storage` | `auth.Storage [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0/10 matched (target 30) | `rebuild_raw_memories_file_from_memories`, `sync_rollout_summaries_from_memories`, `rebuild_raw_memories_file`, `prune_rollout_summaries`, `write_rollout_summary_for_thread`, `retained_memories`, `raw_memories_format_error`, `rollout_summary_format_error`, `rollout_summary_file_stem`, `rollout_summary_file_stem_from_parts` | 0/0 matched (target 8) | _none_ | - | 10 | 101010.0 |
| 18 | `runtimes.unified_exec` | `runtimes.UnifiedExec [ZERO]` | 0.00 | 5/12 matched (target 8) | `unified_exec_options`, `new`, `approval_keys`, `exec_approval_requirement`, `permission_request_payload`, `network_approval_spec`, `unified_exec_options_combines_default_timeout_with_network_denial_cancellation` | 3/4 matched (target 3) | `ApprovalKey` | 0/1 | 8 | 81610.0 |
| 19 | `terminal-detection.lib` | `terminal.TerminalDetection [STUB]` | 0.00 | 20/27 matched (target 23) | `new`, `var_non_empty`, `var`, `user_agent`, `terminal_info`, `tmux_display_message`, `none_if_whitespace` | 5/6 matched (target 8) | `ProcessEnvironment` | - | 8 | 83310.0 |
| 20 | `execpolicy.error` | `execpolicy.ExecPolicyError [ZERO]` | 0.00 | 0/2 matched (target 0) | `with_location`, `location` | 0/5 matched (target 7) | `Result`, `TextPosition`, `TextRange`, `ErrorLocation`, `Error` | - | 7 | 70710.0 |
| 21 | `aws-auth.config` | `otel.Config [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/3 matched (target 0) | `load_sdk_config`, `credentials_provider`, `resolved_region` | 0/0 matched (target 7) | _none_ | - | 3 | 30310.0 |
| 22 | `mcp-server.tests.common.responses` | `requests.ResponsesRequest [STUB] [PROVENANCE-FALLBACK]` | 0.00 | 0/3 matched (target 14) | `create_shell_command_sse_response`, `create_final_assistant_message_sse_response`, `create_apply_patch_sse_response` | 0/0 matched (target 2) | _none_ | - | 3 | 30310.0 |
| 23 | `thread-store.error` | `core.ErrorTest [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/0 matched (target 21) | _none_ | 0/2 matched (target 1) | `ThreadStoreResult`, `ThreadStoreError` | - | 2 | 20210.0 |
| 24 | `render.mod` | `render.Render [STUB]` | 0.00 | 3/3 matched (target 5) | _none_ | 1/2 matched (target 1) | `RectExt` | - | 1 | 10510.0 |
| 25 | `tools.plan_tool` | `protocol.PlanTool [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/1 matched (target 0) | `create_update_plan_tool` | 0/0 matched (target 3) | _none_ | - | 1 | 10110.0 |
| 26 | `codex-client.telemetry` | `telemetry.Telemetry [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/0 matched (target 1) | _none_ | 1/1 matched (target 2) | _none_ | - | 0 | 110.0 |
| 27 | `core.function_tool` | `core.FunctionTool [ZERO]` | 0.00 | 0/0 matched (target 4) | _none_ | 1/1 matched (target 5) | _none_ | - | 0 | 110.0 |
| 28 | `models-manager.model_presets` | `common.ModelPresets [ZERO] [PROVENANCE-FALLBACK]` | 0.00 | 0/0 matched (target 2) | _none_ | 0/0 matched (target 3) | _none_ | - | 0 | 10.0 |
| 29 | `requests.headers` | `requests.Headers [STUB]` | 0.00 | 3/3 matched | _none_ | 0/0 matched | _none_ | - | 0 | 310.0 |
| 30 | `network-proxy.responses` | `endpoint.Responses [PROVENANCE-FALLBACK]` | 0.00 | 0/8 matched (target 4) | `text_response`, `json_response`, `blocked_header_value`, `blocked_message`, `blocked_text_response`, `blocked_message_with_policy`, `blocked_text_response_with_policy`, `blocked_message_with_policy_returns_human_message` | 0/1 matched (target 2) | `PolicyDecisionDetails` | 0/1 | 9 | 51090912.0 |
| 31 | `tests.features` | `features.Features [PROVENANCE-FALLBACK]` | 0.00 | 0/5 matched (target 14) | `codex_command`, `features_enable_writes_feature_flag_to_config`, `features_disable_writes_feature_flag_to_config`, `features_enable_under_development_feature_prints_warning`, `features_list_is_sorted_alphabetically_by_feature_name` | 0/0 matched (target 5) | _none_ | - | 5 | 33050510.0 |
| 32 | `tool.terminal` | `core.Terminal [PROVENANCE-FALLBACK]` | 0.00 | 0/16 matched (target 6) | `start_terminal_operation_from_invocation`, `start_terminal_operation_from_runtime`, `insert_terminal_operation`, `end_terminal_operation`, `ensure_terminal_session`, `sync_terminal_model_observation`, `next_terminal_operation_id`, `terminal_operation_kind`, `parse_protocol_terminal_request`, `parse_dispatch_terminal_request`, `parse_terminal_response_payload`, `parse_protocol_terminal_response`, `parse_dispatch_terminal_response`, `parse_code_mode_exec_result`, `json_text_content`, `terminal_id_from_json` | 0/10 matched (target 0) | `TerminalOperationStart`, `ParsedTerminalRequest`, `ParsedTerminalResponse`, `ExecCommandBeginPayload`, `ExecCommandEndPayload`, `DispatchedToolTraceRequestPayload`, `DispatchedToolPayload`, `DispatchedWriteStdinArgs`, `DispatchedToolTraceResponsePayload`, `CodeModeExecResult` | - | 26 | 17262610.0 |
| 33 | `context.environment_context` | `utils.Environment [PROVENANCE-FALLBACK]` | 0.00 | 0/13 matched (target 2) | `legacy`, `from_turn_environments`, `from_vec`, `equals_except_shell`, `new`, `new_with_environments`, `diff_from_turn_context_item`, `from_turn_context`, `from_turn_context_item`, `with_subagents`, `network_from_turn_context`, `network_from_turn_context_item`, `body` | 0/4 matched (target 1) | `EnvironmentContext`, `EnvironmentContextEnvironment`, `EnvironmentContextEnvironments`, `NetworkContext` | - | 17 | 3171710.0 |
| 34 | `context.user_instructions` | `session.UserInstructions [PROVENANCE-FALLBACK]` | 0.00 | 0/1 matched (target 4) | `body` | 1/1 matched (target 2) | _none_ | - | 1 | 3010210.0 |
| 35 | `protocol.auth` | `core.Auth [PROVENANCE-FALLBACK]` | 0.00 | 0/6 matched (target 37) | `from_raw_value`, `display_name`, `raw_value`, `is_workspace_account`, `new`, `plan_type_deserializes_raw_aliases` | 2/4 matched (target 17) | `RefreshTokenFailedError`, `RefreshTokenFailedReason` | 0/1 | 8 | 2081010.0 |
| 36 | `cli.sandbox_mode_cli_arg` | `common.SandboxModeCliArg` | 0.00 | 0/2 matched (target 1) | `from`, `maps_cli_args_to_protocol_modes` | 1/1 matched | _none_ | 0/1 | 2 | 2020310.0 |
| 37 | `cli.approval_mode_cli_arg` | `common.ApprovalModeCliArg` | 0.00 | 0/1 matched | `from` | 1/1 matched | _none_ | - | 1 | 2010210.0 |
| 38 | `sse.responses` | `streaming.SseParser` | 0.00 | 0/55 matched (target 2) | `stream_from_fixture`, `spawn_response_stream`, `from`, `kind`, `response_model`, `model_verifications`, `header_openai_model_value_from_json`, `model_verifications_from_json_value`, `parse_model_verification`, `json_value_as_string`, `into_api_error`, `process_responses_event`, `process_sse`, `try_parse_retry_after`, `is_context_window_error`, `is_quota_exceeded_error`, `is_usage_not_included`, `is_invalid_prompt_error`, `is_cyber_policy_error`, `is_server_overloaded_error`, `cyber_policy_fallback_message`, `cyber_policy_message`, `rate_limit_regex`, `collect_events`, `run_sse`, `idle_timeout`, `parses_items_and_completed`, `error_when_missing_completed`, `parses_tool_search_call_items`, `parses_tool_call_input_deltas`, `emits_completed_without_stream_end`, `error_when_error_event`, `context_window_error_is_fatal`, `context_window_error_with_newline_is_fatal`, `quota_exceeded_error_is_fatal`, `cyber_policy_error_is_fatal`, `cyber_policy_error_uses_fallback_for_empty_message`, `invalid_prompt_without_type_is_invalid_request`, `table_driven_event_kinds`, `is_created`, `is_output`, `is_completed`, `spawn_response_stream_emits_header_events`, `spawn_response_stream_ignores_model_verification_header`, `process_sse_ignores_response_model_field_in_payload`, `process_sse_emits_server_model_from_response_headers_payload`, `process_sse_emits_model_verification_field`, `responses_stream_event_response_model_reads_top_level_headers`, `responses_stream_event_response_model_prefers_response_headers`, `responses_stream_event_model_verification_reads_metadata_field`, `responses_stream_event_model_verification_ignores_unknown_field`, `responses_stream_event_model_verification_ignores_non_array_field`, `test_try_parse_retry_after`, `test_try_parse_retry_after_no_delay`, `test_try_parse_retry_after_azure` | 0/8 matched (target 2) | `Error`, `ResponseCompleted`, `ResponseCompletedUsage`, `ResponseCompletedInputTokensDetails`, `ResponseCompletedOutputTokensDetails`, `ResponsesStreamEvent`, `ResponsesEventError`, `TestCase` | 0/32 | 63 | 636310.0 |
| 39 | `exec-server.protocol` | `protocol.Protocol [PROVENANCE-FALLBACK]` | 0.00 | 0/5 matched (target 31) | `into_inner`, `from`, `serialize`, `deserialize`, `http_request_timeout_treats_omitted_and_null_as_no_timeout` | 1/37 matched (target 188) | `ByteChunk`, `InitializeParams`, `InitializeResponse`, `ExecParams`, `ExecEnvPolicy`, `ExecResponse`, `ReadParams`, `ProcessOutputChunk`, `ReadResponse`, `WriteParams`, `WriteStatus`, `WriteResponse`, `TerminateParams`, `TerminateResponse`, `FsReadFileParams`, `FsReadFileResponse`, `FsWriteFileParams`, `FsWriteFileResponse`, `FsCreateDirectoryParams`, `FsCreateDirectoryResponse`, `FsGetMetadataParams`, `FsGetMetadataResponse`, `FsReadDirectoryParams`, `FsReadDirectoryEntry`, `FsReadDirectoryResponse`, `FsRemoveParams`, `FsRemoveResponse`, `FsCopyParams`, `FsCopyResponse`, `HttpHeader`, `HttpRequestParams`, `HttpRequestResponse`, `HttpRequestBodyDeltaNotification`, `ExecOutputDeltaNotification`, `ExecExitedNotification`, `ExecClosedNotification` | 0/1 | 41 | 414210.0 |
| 40 | `core.exec_policy` | `execpolicy.ExecPolicy` | 0.00 | 0/29 matched (target 2) | `child_uses_parent_exec_policy`, `exec_policy_config_folders`, `is_policy_match`, `prompt_is_rejected_by_policy`, `new`, `load`, `current`, `create_exec_approval_requirement_for_command`, `append_amendment_and_update`, `append_network_rule_and_update`, `default`, `check_execpolicy_for_warnings`, `exec_policy_message_for_display`, `parse_starlark_line_from_message`, `format_exec_policy_error_with_source`, `load_exec_policy_with_warning`, `load_exec_policy`, `render_decision_for_unmatched_command`, `profile_is_managed_read_only`, `default_policy_path`, `commands_for_exec_policy`, `try_derive_execpolicy_amendment_for_prompt_rules`, `try_derive_execpolicy_amendment_for_allow_rules`, `derive_requested_execpolicy_amendment_from_prefix_rule`, `prefix_rule_would_approve_all_commands`, `derive_prompt_reason`, `render_shlex_command`, `derive_forbidden_reason`, `collect_policy_files` | 0/7 matched (target 0) | `ExecPolicyCommandOrigin`, `UnmatchedCommandContext`, `ExecPolicyCommands`, `ExecPolicyError`, `ExecPolicyUpdateError`, `ExecPolicyManager`, `ExecApprovalRequest` | - | 36 | 363610.0 |
| 41 | `model-provider.provider` | `provider.Provider [PROVENANCE-FALLBACK]` | 0.00 | 0/28 matched (target 4) | `default`, `fmt`, `capabilities`, `api_provider`, `runtime_base_url`, `api_auth`, `create_model_provider`, `new`, `info`, `auth_manager`, `auth`, `account_state`, `models_manager`, `provider_info_with_command_auth`, `test_codex_home`, `provider_for`, `remote_model`, `configured_provider_uses_default_capabilities`, `configured_provider_runtime_base_url_uses_configured_base_url`, `create_model_provider_builds_command_auth_manager_without_base_manager`, `create_model_provider_does_not_use_openai_auth_manager_for_amazon_bedrock_provider`, `openai_provider_returns_unauthenticated_openai_account_state`, `openai_provider_returns_api_key_account_state`, `custom_non_openai_provider_returns_no_account_state`, `amazon_bedrock_provider_returns_bedrock_account_state`, `amazon_bedrock_provider_creates_static_models_manager`, `amazon_bedrock_provider_uses_configured_static_catalog_when_present`, `configured_provider_models_manager_uses_provider_bearer_token` | 0/7 matched (target 3) | `ProviderCapabilities`, `ProviderAccountState`, `ProviderAccountError`, `ProviderAccountResult`, `ModelProvider`, `SharedModelProvider`, `ConfiguredModelProvider` | 0/15 | 35 | 353510.0 |
| 42 | `protocol.approvals` | `protocol.Approvals [PROVENANCE-FALLBACK]` | 0.00 | 0/9 matched (target 1) | `new`, `command`, `from`, `effective_approval_id`, `effective_available_decisions`, `default_available_decisions`, `message`, `guardian_assessment_action_deserializes_command_shape`, `guardian_assessment_action_round_trips_execve_shape` | 4/20 matched (target 6) | `ResolvedPermissionProfile`, `EscalationPermissions`, `ExecPolicyAmendment`, `NetworkApprovalProtocol`, `NetworkApprovalContext`, `NetworkPolicyRuleAction`, `GuardianRiskLevel`, `GuardianUserAuthorization`, `GuardianAssessmentOutcome`, `GuardianAssessmentStatus`, `GuardianAssessmentDecisionSource`, `GuardianCommandSource`, `GuardianAssessmentAction`, `NetworkPolicyAmendment`, `GuardianAssessmentEvent`, `ElicitationRequest` | 0/2 | 25 | 252910.0 |
| 43 | `mcp-server.codex_tool_config` | `config.DurationSerializers` | 0.00 | 0/9 matched (target 2) | `from`, `create_tool_for_codex_tool_call_param`, `codex_tool_output_schema`, `into_config`, `get_thread_id`, `create_tool_for_codex_tool_call_reply_param`, `create_tool_input_schema`, `verify_codex_tool_json_schema`, `verify_codex_tool_reply_json_schema` | 0/4 matched (target 0) | `CodexToolCallParam`, `CodexToolCallApprovalPolicy`, `CodexToolCallSandboxMode`, `CodexToolCallReplyParam` | 0/2 | 13 | 131310.0 |
| 44 | `login.device_code_auth` | `auth.Hashing` | 0.00 | 0/7 matched (target 18) | `deserialize_interval`, `request_user_code`, `poll_for_token`, `print_device_code_prompt`, `request_device_code`, `complete_device_code_login`, `run_device_code_login` | 0/5 matched (target 1) | `DeviceCode`, `UserCodeResp`, `UserCodeReq`, `TokenPollReq`, `CodeSuccessResp` | - | 12 | 121210.0 |
| 45 | `suite.exec` | `core.ExecExpiration [PROVENANCE-FALLBACK]` | 0.00 | 0/8 matched (target 3) | `skip_test`, `run_test_cmd`, `exit_code_0_succeeds`, `truncates_output_lines`, `truncates_output_bytes`, `exit_command_not_found_is_ok`, `openpty_works_under_real_exec_seatbelt_path`, `write_file_fails_as_sandbox_error` | 0/0 matched (target 6) | _none_ | - | 8 | 80810.0 |
| 46 | `tools.spec` | `tools.Spec` | 0.00 | 0/3 matched (target 17) | `tool_user_shell_type`, `map_mcp_tools_for_plan`, `build_specs_with_discoverable_tools` | 0/1 matched (target 11) | `McpToolPlanInputs` | - | 4 | 40410.0 |
| 47 | `app-server.models` | `protocol.Models [PROVENANCE-FALLBACK]` | 0.00 | 0/3 matched (target 12) | `supported_models`, `model_from_preset`, `reasoning_efforts_from_preset` | 0/0 matched (target 56) | _none_ | - | 3 | 30310.0 |
| 48 | `codex-api.error` | `core.Error [PROVENANCE-FALLBACK]` | 0.00 | 0/1 matched (target 62) | `from` | 0/1 matched (target 42) | `ApiError` | - | 2 | 20210.0 |
| 49 | `unified_exec.session` | `unifiedexec.Session [PROVENANCE-FALLBACK]` | 0.00 | 0/2 matched (target 15) | `spawn_windows_sandbox_session_legacy`, `spawn_windows_sandbox_session_elevated` | 0/0 matched (target 6) | _none_ | - | 2 | 20210.0 |
| 50 | `core.landlock` | `core.Landlock` | 0.00 | 0/1 matched | `spawn_command_under_linux_sandbox` | 0/0 matched | _none_ | - | 1 | 10110.0 |
| 51 | `suite.user_notification` | `core.UserNotification` | 0.00 | 0/1 matched (target 4) | `summarize_context_three_requests_and_instructions` | 0/0 matched (target 4) | _none_ | - | 1 | 10110.0 |
| 52 | `core.exec` | `core.Exec` | 0.02 | 1/34 matched (target 3) | `windows_sandbox_uses_elevated_backend`, `select_process_exec_tool_sandbox_type`, `from`, `wait_with_outcome`, `timeout_ms`, `with_cancellation`, `cancel_when_either`, `retained_bytes_cap`, `io_drain_timeout`, `uses_expiration`, `process_exec_tool_call`, `build_exec_request`, `execute_exec_request`, `get_raw_output_result`, `extract_create_process_as_user_error_code`, `windowsapps_path_kind`, `record_windows_sandbox_spawn_failure`, `exec_windows_sandbox`, `finalize_exec_result`, `append_capped`, `aggregate_output`, `exec`, `should_use_windows_restricted_token_sandbox`, `unsupported_windows_restricted_token_sandbox_reason`, `resolve_windows_restricted_token_filesystem_overrides`, `normalize_windows_override_path`, `resolve_windows_elevated_filesystem_overrides`, `has_reopened_writable_descendant`, `consume_output`, `await_output`, `read_output`, `synthetic_exit_status`, `synthetic_exit_status_for_code` | 2/7 matched (target 5) | `WindowsSandboxFilesystemOverrides`, `ExecCapturePolicy`, `ExecExpiration`, `ExecExpirationOutcome`, `RawExecToolCallOutput` | - | 38 | 384109.8 |
| 53 | `core.turn_diff_tracker` | `session.TurnDiffTracker` | 0.03 | 1/15 matched (target 10) | `new`, `get_path_for_internal`, `find_git_root_cached`, `relative_to_git_root_str`, `git_blob_oid_for_path`, `get_unified_diff`, `get_file_diff`, `git_blob_sha1_hex_bytes`, `as_str`, `fmt`, `file_mode_for_path`, `blob_bytes`, `symlink_blob_bytes`, `is_windows_drive_or_unc_root` | 3/3 matched (target 5) | _none_ | - | 14 | 11141810.0 |
| 54 | `shell-command.bash` | `bash.Bash` | 0.04 | 3/43 matched (target 8) | `try_parse_shell`, `try_parse_word_only_commands_sequence`, `parse_plain_command_from_node`, `parse_heredoc_command_words`, `is_literal_word_or_number`, `is_allowed_heredoc_attachment_kind`, `find_single_command_node`, `has_named_descendant_kind`, `parse_double_quoted_string`, `parse_raw_string`, `parse_seq`, `accepts_single_simple_command`, `accepts_multiple_commands_with_allowed_operators`, `extracts_double_and_single_quoted_strings`, `accepts_double_quoted_strings_with_newlines`, `accepts_mixed_quote_concatenation`, `rejects_double_quoted_strings_with_expansions`, `accepts_numbers_as_words`, `rejects_parentheses_and_subshells`, `rejects_redirections_and_unsupported_operators`, `rejects_command_and_process_substitutions_and_expansions`, `rejects_variable_assignment_prefix`, `rejects_trailing_operator_parse_error`, `rejects_empty_command_position_with_leading_operator`, `rejects_empty_command_position_with_double_separator`, `rejects_empty_command_position_with_empty_pipeline_segment`, `parse_zsh_lc_plain_commands`, `accepts_concatenated_flag_and_value`, `accepts_concatenated_flag_with_single_quotes`, `rejects_concatenation_with_variable_substitution`, `rejects_concatenation_with_command_substitution`, `parse_shell_lc_single_command_prefix_supports_heredoc`, `parse_shell_lc_single_command_prefix_rejects_multi_command_scripts`, `parse_shell_lc_single_command_prefix_rejects_non_heredoc_redirects`, `parse_shell_lc_single_command_prefix_rejects_heredoc_with_extra_file_redirect`, `parse_shell_lc_single_command_prefix_rejects_heredoc_with_variable_assignment`, `parse_shell_lc_single_command_prefix_rejects_herestring_with_chaining`, `parse_shell_lc_single_command_prefix_rejects_herestring_with_substitution`, `parse_shell_lc_single_command_prefix_rejects_arithmetic_shift_non_heredoc_script`, `parse_shell_lc_single_command_prefix_rejects_heredoc_command_with_word_expansion` | 0/0 matched | _none_ | 0/30 | 40 | 404309.6 |
| 55 | `handlers.plan` | `handlers.Plan` | 0.05 | 1/8 matched (target 1) | `log_preview`, `success_for_logging`, `to_response_item`, `code_mode_result`, `kind`, `handle_update_plan`, `parse_update_plan_arguments` | 1/3 matched (target 1) | `PlanToolOutput`, `Output` | - | 9 | 91109.5 |
| 56 | `tools.context` | `tools.Context` | 0.05 | 4/16 matched (target 10) | `post_tool_use_response`, `code_mode_result`, `to_response_item`, `response_payload`, `from_text`, `from_content`, `into_text`, `truncated_output`, `response_text`, `response_input_to_code_mode_result`, `content_items_to_code_mode_result`, `function_tool_response` | 4/12 matched (target 9) | `ToolCallSource`, `McpToolOutput`, `ToolSearchOutput`, `FunctionToolOutput`, `ApplyPatchToolOutput`, `AbortedToolOutput`, `ExecCommandToolOutput`, `UnifiedExecCodeModeResult` | - | 20 | 113202808.0 |
| 57 | `tui.app_event_sender` | `tui.AppEventSender` | 0.06 | 1/13 matched (target 1) | `new`, `interrupt`, `compact`, `set_thread_name`, `review`, `list_skills`, `realtime_conversation_audio`, `user_input_answer`, `exec_approval`, `request_permissions_response`, `patch_approval`, `resolve_elicitation` | 1/1 matched | _none_ | - | 12 | 26121410.0 |
| 58 | `handlers.view_image` | `handlers.ViewImage` | 0.06 | 1/7 matched (target 1) | `kind`, `log_preview`, `success_for_logging`, `to_response_item`, `code_mode_result`, `code_mode_result_returns_image_url_object` | 2/5 matched (target 2) | `ViewImageDetail`, `Output`, `ViewImageOutput` | 0/1 | 9 | 91209.4 |
| 59 | `model-provider.auth` | `api.Auth [PROVENANCE-FALLBACK]` | 0.06 | 1/7 matched (target 2) | `unauthenticated_auth_provider`, `auth_manager_for_provider`, `resolve_provider_auth`, `bearer_auth_for_provider`, `auth_provider_from_auth`, `unauthenticated_auth_provider_adds_no_headers` | 0/2 matched (target 1) | `AgentIdentityAuthProvider`, `UnauthenticatedAuthProvider` | 0/1 | 8 | 80909.4 |
| 60 | `shell-command.powershell` | `core.PowerShell [PROVENANCE-FALLBACK]` | 0.06 | 1/13 matched (target 1) | `prefix_powershell_script_with_utf8`, `parse_powershell_command_into_plain_commands`, `try_find_powershell_executable_blocking`, `try_find_pwsh_executable_blocking`, `try_find_powershellish_executable_in_path`, `is_powershellish_executable_available`, `extracts_basic_powershell_command`, `extracts_lowercase_flags`, `extracts_full_path_powershell_command`, `extracts_with_noprofile_and_alias`, `parses_plain_powershell_commands`, `parses_multiple_plain_powershell_commands` | 0/0 matched | _none_ | 0/6 | 12 | 121309.4 |
| 61 | `core.client_common` | `prompt.Prompt` | 0.07 | 1/9 matched (target 2) | `default`, `reserialize_shell_outputs`, `is_shell_tool_name`, `parse_structured_shell_output`, `build_structured_output`, `strip_total_output_header`, `poll_next`, `drop` | 1/5 matched (target 1) | `ExecOutputJson`, `ExecOutputMetadataJson`, `ResponseStream`, `Item` | - | 12 | 121409.3 |
| 62 | `handlers.mcp` | `handlers.Mcp` | 0.07 | 1/8 matched (target 1) | `kind`, `pre_tool_use_payload`, `post_tool_use_payload`, `mcp_hook_tool_input`, `mcp_pre_tool_use_payload_uses_model_tool_name_and_raw_args`, `mcp_post_tool_use_payload_uses_model_tool_name_args_and_result`, `mcp_hook_tool_input_defaults_empty_args_to_object` | 1/2 matched (target 1) | `Output` | 0/3 | 8 | 81009.3 |
| 63 | `execpolicy.policy` | `execpolicy.Policy` | 0.09 | 5/24 matched (target 5) | `new`, `from_parts`, `network_rules`, `host_executables`, `get_allowed_prefixes`, `add_prefix_rule`, `add_network_rule`, `set_host_executable_paths`, `merge_overlay`, `compiled_network_domains`, `check_with_options`, `check_multiple_with_options`, `matches_for_command`, `matches_for_command_with_options`, `match_exact_rules`, `match_host_executable_rules`, `upsert_domain`, `render_pattern_token`, `from_matches` | 2/4 matched | `HeuristicsFallback`, `MatchOptions` | - | 21 | 212809.1 |
| 64 | `protocol.num_format` | `protocol.NumFormat` | 0.10 | 3/8 matched (target 4) | `make_local_formatter`, `make_en_us_formatter`, `formatter`, `format_with_separators_with_formatter`, `format_si_suffix_with_formatter` | 0/0 matched (target 1) | _none_ | 1/1 | 5 | 50809.0 |
| 65 | `handlers.apply_patch` | `handlers.ApplyPatch` | 0.11 | 3/20 matched (target 12) | `consume_diff`, `finish`, `push_delta`, `finish_update_on_complete`, `convert_apply_patch_hunks_to_protocol`, `hunk_source_path`, `format_update_chunks_for_progress`, `file_paths_for_action`, `to_abs_path`, `write_permissions_for_paths`, `apply_patch_payload_command`, `effective_patch_permissions`, `kind`, `create_diff_consumer`, `pre_tool_use_payload`, `post_tool_use_payload`, `intercept_apply_patch` | 1/3 matched (target 12) | `ApplyPatchArgumentDiffConsumer`, `Output` | - | 19 | 192308.9 |
| 66 | `protocol.exec_output` | `protocol.ExecOutput` | 0.12 | 2/8 matched (target 3) | `new`, `bytes_to_string_smart`, `detect_encoding`, `decode_bytes`, `looks_like_windows_1252_punctuation`, `is_windows_1252_punct` | 2/2 matched | _none_ | - | 6 | 61008.8 |
| 67 | `sandbox-summary.sandbox_summary` | `common.SandboxSummary` | 0.13 | 1/6 matched (target 1) | `summarize_permission_profile`, `summarizes_external_sandbox_without_network_access_suffix`, `summarizes_external_sandbox_with_enabled_network`, `summarizes_read_only_with_enabled_network`, `workspace_write_summary_still_includes_network_access` | 0/0 matched | _none_ | 0/4 | 5 | 50608.7 |
| 68 | `tui.slash_command` | `tui.SlashCommand` | 0.15 | 4/11 matched (target 4) | `command`, `supports_inline_args`, `available_in_side_conversation`, `stop_command_is_canonical_name`, `clean_alias_parses_to_stop_command`, `certain_commands_are_available_during_task`, `auto_review_command_is_approve` | 1/1 matched | _none_ | 0/4 | 7 | 4071208.5 |
| 69 | `codex-api.common` | `common.Common [PROVENANCE-FALLBACK]` | 0.15 | 1/4 matched (target 2) | `from`, `response_create_client_metadata`, `poll_next` | 8/16 matched (target 9) | `MemorySummarizeInput`, `RawMemory`, `RawMemoryMetadata`, `MemorySummarizeOutput`, `ResponseEvent`, `ResponseCreateWsRequest`, `ResponsesWsRequest`, `Item` | - | 11 | 6112008.5 |
| 70 | `cli.format_env_display` | `common.FormatEnvDisplay` | 0.15 | 1/5 matched (target 1) | `returns_dash_when_empty`, `formats_sorted_env_pairs`, `formats_env_vars_with_dollar_prefix`, `combines_env_pairs_and_vars` | 0/0 matched | _none_ | 0/4 | 4 | 3040508.5 |
| 71 | `tui.terminal_palette` | `tui.TerminalPalette` | 0.18 | 7/14 matched (target 7) | `stdout_color_level`, `rgb_color`, `indexed_color`, `default`, `get_or_init_with`, `default_colors_cache`, `color_to_tuple` | 1/3 matched (target 1) | `StdoutColorLevel`, `Cache` | - | 9 | 91708.2 |
| 72 | `tui.frame_requester` | `tui.FrameRequester` | 0.18 | 5/13 matched (target 6) | `test_schedule_frame_immediate_triggers_once`, `test_schedule_frame_in_triggers_at_delay`, `test_coalesces_multiple_requests_into_single_draw`, `test_coalesces_mixed_immediate_and_delayed_requests`, `test_limits_draw_notifications_to_120fps`, `test_rate_limit_clamps_early_delayed_requests`, `test_rate_limit_does_not_delay_future_draws`, `test_multiple_delayed_requests_coalesce_to_earliest` | 2/2 matched (target 6) | _none_ | 0/8 | 8 | 17081508.0 |
| 73 | `codex-api.rate_limits` | `ratelimits.RateLimits [PROVENANCE-FALLBACK]` | 0.19 | 5/21 matched (target 6) | `fmt`, `parse_default_rate_limit`, `parse_all_rate_limits`, `parse_rate_limit_for_limit`, `parse_rate_limit_event`, `map_event_window`, `parse_promo_message`, `parse_header_str`, `has_rate_limit_data`, `header_name_to_limit_id`, `normalize_limit_id`, `parse_rate_limit_for_limit_defaults_to_codex_headers`, `parse_rate_limit_for_limit_reads_secondary_headers`, `parse_rate_limit_for_limit_prefers_limit_name_header`, `parse_all_rate_limits_reads_all_limit_families`, `parse_all_rate_limits_includes_default_codex_snapshot` | 1/5 matched (target 1) | `RateLimitEventWindow`, `RateLimitEventDetails`, `RateLimitEventCredits`, `RateLimitEvent` | 0/5 | 20 | 202608.1 |
| 74 | `core.util` | `core.Util [PROVENANCE-FALLBACK]` | 0.19 | 2/7 matched (target 3) | `from_optional_fields`, `emit_feedback_auth_recovery_tags`, `resolve_path`, `normalize_thread_name`, `resume_command` | 0/1 matched (target 0) | `Auth401FeedbackSnapshot` | - | 6 | 60808.1 |
| 75 | `tools.router` | `tools.Router` | 0.21 | 4/10 matched (target 6) | `model_visible_specs`, `find_spec`, `create_diff_consumer`, `configured_tool_supports_parallel`, `dispatch_tool_call_with_code_mode_result`, `filter_deferred_dynamic_tool_spec` | 2/3 matched (target 2) | `ToolRouterParams` | - | 7 | 10071308.0 |
| 76 | `state.turn` | `session.Turn` | 0.22 | 8/26 matched (target 13) | `default`, `insert_pending_request_permissions`, `remove_pending_request_permissions`, `insert_pending_user_input`, `remove_pending_user_input`, `insert_pending_elicitation`, `remove_pending_elicitation`, `insert_pending_dynamic_tool`, `remove_pending_dynamic_tool`, `prepend_pending_input`, `has_pending_input`, `accept_mailbox_delivery_for_current_turn`, `accepts_mailbox_delivery_for_current_turn`, `set_mailbox_delivery_phase`, `record_granted_permissions`, `granted_permissions`, `enable_strict_auto_review`, `strict_auto_review_enabled` | 4/7 matched (target 10) | `MailboxDeliveryPhase`, `RemovedTask`, `PendingRequestPermissions` | - | 21 | 15213308.0 |
| 77 | `handlers.unified_exec` | `handlers.UnifiedExec` | 0.22 | 4/12 matched (target 5) | `default_exec_yield_time_ms`, `default_write_stdin_yield_time_ms`, `default_tty`, `effective_max_output_tokens`, `kind`, `pre_tool_use_payload`, `post_tool_use_payload`, `emit_unified_exec_tty_metric` | 2/4 matched (target 2) | `WriteStdinArgs`, `Output` | - | 10 | 101607.8 |
| 78 | `tui.update_action` | `tui.UpdateAction` | 0.22 | 3/6 matched (target 5) | `from_install_context`, `maps_install_context_to_update_action`, `standalone_update_commands_rerun_latest_installer` | 1/1 matched | _none_ | 0/2 | 3 | 6030708.0 |
| 79 | `tools.parallel` | `tools.Parallel` | 0.22 | 3/8 matched (target 5) | `new`, `find_spec`, `create_diff_consumer`, `handle_tool_call_with_source`, `failure_response` | 1/1 matched (target 2) | _none_ | - | 5 | 50907.8 |
| 80 | `ollama.parser` | `ollama.Parser` | 0.22 | 1/3 matched (target 1) | `test_pull_events_decoder_status_and_success`, `test_pull_events_decoder_progress` | 0/0 matched | _none_ | 0/2 | 2 | 32020308.0 |
| 81 | `tools.registry` | `tools.Registry` | 0.25 | 9/23 matched (target 11) | `pre_tool_use_payload`, `post_tool_use_payload`, `create_diff_consumer`, `finish`, `into_response`, `code_mode_result`, `handle_any`, `empty_for_test`, `with_handler_for_test`, `has_handler`, `dispatch_any`, `from`, `hook_tool_kind`, `dispatch_after_tool_use_hook` | 4/10 matched (target 5) | `ToolArgumentDiffConsumer`, `AnyToolResult`, `PreToolUsePayload`, `PostToolUsePayload`, `AnyToolHandler`, `AfterToolUseHookDispatch` | 0/3 | 20 | 203307.5 |
| 82 | `handlers.test_sync` | `handlers.TestSync` | 0.26 | 2/5 matched (target 2) | `default_timeout_ms`, `barrier_map`, `kind` | 4/5 matched (target 4) | `Output` | - | 4 | 41007.4 |
| 83 | `tools.orchestrator` | `tools.Orchestrator` | 0.26 | 2/6 matched (target 2) | `new`, `run_attempt`, `request_approval`, `reject_if_not_approved` | 1/2 matched | `OrchestratorRunResult` | - | 5 | 50807.4 |
| 84 | `protocol.items` | `protocol.Items [PROVENANCE-FALLBACK]` | 0.26 | 6/19 matched (target 10) | `default`, `text_elements`, `local_image_paths`, `from_fragments`, `from_single_hook`, `build_hook_prompt_message`, `parse_hook_prompt_message`, `parse_hook_prompt_fragment`, `serialize_hook_prompt_fragment`, `as_legacy_begin_event`, `as_legacy_end_event`, `hook_prompt_roundtrips_multiple_fragments`, `hook_prompt_parses_legacy_single_hook_run_id` | 6/17 matched (target 11) | `HookPromptItem`, `HookPromptFragment`, `HookPromptXml`, `PlanItem`, `ImageViewItem`, `ImageGenerationItem`, `FileChangeItem`, `McpToolCallItem`, `McpToolCallStatus`, `McpToolCallError`, `ContextCompactionItem` | 0/2 | 24 | 243607.4 |
| 85 | `state.session` | `state.SessionState` | 0.26 | 10/33 matched (target 10) | `new`, `previous_turn_settings`, `set_previous_turn_settings`, `set_next_turn_is_first`, `take_next_turn_is_first`, `set_reference_context_item`, `reference_context_item`, `set_server_reasoning_included`, `server_reasoning_included`, `record_mcp_dependency_prompted`, `mcp_dependency_prompted`, `set_dependency_env`, `dependency_env`, `set_session_startup_prewarm`, `take_session_startup_prewarm`, `merge_connector_selection`, `get_connector_selection`, `clear_connector_selection`, `set_pending_session_start_source`, `take_pending_session_start_source`, `record_granted_permissions`, `granted_permissions`, `merge_rate_limit_fields` | 1/1 matched | _none_ | - | 23 | 48233408.0 |
| 86 | `handlers.shell` | `handlers.Shell` | 0.28 | 5/14 matched (target 9) | `shell_payload_command`, `shell_command_payload_command`, `shell_runtime_backend`, `resolve_use_login_shell`, `base_command`, `from`, `kind`, `pre_tool_use_payload`, `post_tool_use_payload` | 2/5 matched (target 2) | `ShellCommandBackend`, `RunExecLikeArgs`, `Output` | - | 12 | 121907.2 |
| 87 | `tui.key_hint` | `tui.KeyHint` | 0.29 | 10/29 matched (target 11) | `from_event`, `parts`, `display_label`, `normalize_key_parts`, `c0_control_char_to_ctrl_char`, `is_pressed`, `ctrl_alt`, `from`, `is_press_accepts_press_and_repeat_but_rejects_release`, `keybinding_list_ext_matches_any_binding`, `shifted_letter_binding_matches_uppercase_char_events`, `shift_letter_binding_preserves_other_modifiers_with_uppercase_compat`, `shift_letter_binding_does_not_match_plain_lowercase_or_other_uppercase`, `ctrl_letter_binding_matches_c0_control_char_events`, `ctrl_bindings_match_all_supported_c0_control_char_events`, `ctrl_binding_does_not_match_ambiguous_c0_escape_or_delete`, `history_search_ctrl_bindings_match_c0_control_char_events`, `ctrl_alt_sets_both_modifiers`, `has_ctrl_or_alt_checks_supported_modifier_combinations` | 1/2 matched (target 1) | `KeyBindingListExt` | 0/11 | 20 | 27203108.0 |
| 88 | `cli.exit_status` | `cli.ExitStatus` | 0.30 | 1/1 matched | _none_ | 0/0 matched (target 1) | _none_ | - | 0 | 5000107.0 |
| 89 | `unified_exec.errors` | `unifiedexec.Errors` | 0.31 | 1/3 matched (target 2) | `create_process`, `process_failed` | 1/1 matched (target 7) | _none_ | - | 2 | 20406.9 |
| 90 | `core.user_shell_command` | `session.UserShellCommand` | 0.33 | 1/3 matched (target 6) | `user_shell_command_fragment`, `format_user_shell_command_record` | 0/0 matched | _none_ | 0/1 | 2 | 3020306.8 |
| 91 | `handlers.list_dir` | `handlers.ListDir` | 0.34 | 5/11 matched (target 6) | `default_offset`, `default_limit`, `default_depth`, `kind`, `list_dir_slice_with_policy`, `from` | 4/5 matched (target 4) | `Output` | - | 7 | 71606.6 |
| 92 | `runtimes.apply_patch` | `runtimes.ApplyPatch` | 0.34 | 5/11 matched | `new`, `build_guardian_review_request`, `file_system_sandbox_context_for_attempt`, `approval_keys`, `exec_approval_requirement`, `permission_request_payload` | 3/3 matched | _none_ | - | 6 | 61406.6 |
| 93 | `ollama.client` | `ollama.Client` | 0.38 | 6/14 matched (target 10) | `try_from_provider_with_base_url`, `fetch_version`, `from_host_root`, `test_fetch_models_happy_path`, `test_fetch_version`, `test_probe_server_happy_path_openai_compat_and_native`, `test_try_from_oss_provider_ok_when_server_running`, `test_try_from_oss_provider_err_when_server_missing` | 1/1 matched | _none_ | 0/7 | 8 | 81506.2 |
| 94 | `runtimes.shell` | `runtimes.Shell` | 0.39 | 6/12 matched (target 10) | `new`, `for_shell_command`, `approval_keys`, `exec_approval_requirement`, `permission_request_payload`, `network_approval_spec` | 2/4 matched (target 3) | `ShellRuntimeBackend`, `ApprovalKey` | - | 8 | 81606.1 |
| 95 | `tui.style` | `tui.Style` | 0.41 | 3/6 matched (target 3) | `proposed_plan_style`, `proposed_plan_style_for`, `proposed_plan_bg` | 0/0 matched | _none_ | - | 3 | 21030606.0 |
| 96 | `tui.frame_rate_limiter` | `tui.FrameRateLimiter` | 0.41 | 2/4 matched (target 2) | `default_does_not_clamp`, `clamps_to_min_interval_since_last_emit` | 1/1 matched | _none_ | 0/2 | 2 | 1020505.9 |
| 97 | `handlers.mcp_resource` | `handlers.McpResource` | 0.42 | 13/17 matched (target 13) | `new`, `from_single_server`, `from_all_servers`, `kind` | 9/10 matched (target 9) | `Output` | - | 5 | 52705.8 |
| 98 | `execpolicy.rule` | `execpolicy.Rule` | 0.48 | 7/12 matched (target 9) | `with_resolved_program`, `parse`, `as_policy_string`, `normalize_network_rule_host`, `as_any` | 5/8 matched | `NetworkRuleProtocol`, `NetworkRule`, `RuleRef` | - | 8 | 1082005.2 |
| 99 | `ollama.url` | `ollama.Url` | 0.53 | 2/3 matched (target 2) | `test_base_url_to_host_root` | 0/0 matched | _none_ | 0/1 | 1 | 25010304.0 |
| 100 | `string.truncate` | `context.TruncationPolicy [PROVENANCE-FALLBACK]` | 0.55 | 8/11 matched (target 13) | `truncate_middle_chars`, `truncate_middle_with_token_budget`, `removed_units` | 0/0 matched (target 3) | _none_ | - | 3 | 1031104.5 |
| 101 | `core.review_format` | `core.ReviewFormat` | 0.57 | 2/3 matched (target 2) | `render_review_output_text` | 0/0 matched | _none_ | - | 1 | 10304.3 |
| 102 | `core.shell` | `shell.ShellDetector` | 0.57 | 15/19 matched (target 24) | `shell_snapshot`, `empty_shell_snapshot_receiver`, `eq`, `test_detect_shell_type` | 2/2 matched (target 3) | _none_ | 0/1 | 4 | 11042104.0 |
| 103 | `execpolicy.decision` | `execpolicy.Decision` | 0.58 | 1/1 matched | _none_ | 1/1 matched | _none_ | - | 0 | 17000204.0 |
| 104 | `tui.color` | `tui.Color` | 0.61 | 7/7 matched | _none_ | 0/0 matched | _none_ | - | 0 | 19000704.0 |
| 105 | `bottom_pane.scroll_state` | `bottompane.ScrollState` | 0.64 | 6/7 matched (target 6) | `wrap_navigation_and_visibility` | 1/1 matched | _none_ | 0/1 | 1 | 14010804.0 |
| 106 | `tui.app_event` | `tui.AppEvent` | 0.69 | 2/2 matched | _none_ | 9/11 matched (target 70) | `ConnectorsSnapshot`, `RealtimeWebrtcOffer` | - | 2 | 36021304.0 |
| 107 | `render.renderable` | `render.Renderable` | 0.70 | 9/10 matched (target 48) | `cursor_style` | 7/8 matched (target 15) | `RenderableExt` | - | 2 | 41021804.0 |
| 108 | `ollama.pull` | `ollama.Pull` | 0.73 | 3/3 matched (target 4) | _none_ | 4/4 matched (target 9) | _none_ | - | 0 | 702.7 |
| 109 | `tools.events` | `tools.Events` | 0.74 | 11/12 matched (target 11) | `new` | 6/6 matched (target 14) | _none_ | - | 1 | 11802.6 |
| 110 | `login.pkce` | `login.Pkce` | 0.75 | 1/1 matched (target 2) | _none_ | 1/1 matched | _none_ | - | 0 | 202.5 |
| 111 | `exec.exec_events` | `exec.ExecEvents` | 1.00 | 0/0 matched | _none_ | 28/33 matched (target 43) | `CollabToolCallStatus`, `CollabTool`, `CollabAgentStatus`, `CollabAgentState`, `CollabToolCallItem` | - | 5 | 53300.0 |
| 112 | `agent-graph-store.error` | `error.TransportError [PROVENANCE-FALLBACK]` | 1.00 | 0/0 matched | _none_ | 0/2 matched (target 8) | `AgentGraphStoreResult`, `AgentGraphStoreError` | - | 2 | 20200.0 |
| 113 | `execpolicy-legacy.error` | `error.ApiError [PROVENANCE-FALLBACK]` | 1.00 | 0/0 matched | _none_ | 0/2 matched (target 9) | `Result`, `Error` | - | 2 | 20200.0 |
| 114 | `core.flags` | `core.Flags` | 1.00 | 0/0 matched | _none_ | 0/0 matched | _none_ | - | 0 | 0.0 |
| 115 | `tui.ui_consts` | `tui.UiConsts` | 1.00 | 0/0 matched | _none_ | 0/0 matched | _none_ | - | 0 | 0.0 |

## Cheat Detection / Scoring Failures

- `protocol.user_input` -> `protocol.UserInput [ZERO]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `protocol.parse_command` -> `protocol.ParseCommand [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only
- `protocol.account` -> `protocol.Account [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `command_safety.is_safe_command` -> `commandsafety.IsSafeCommand [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. IsSafeCommand.kt: Rust lifetime explanation in Kotlin comments
- `core.compact` -> `endpoint.Compact [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `command_safety.is_dangerous_command` -> `commandsafety.IsDangerousCommand [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. IsDangerousCommand.kt: Rust `let` binding in Kotlin comments
- `core.spawn` -> `core.Spawn [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `core-plugins.loader` -> `config.ConfigLoader [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `protocol.config_types` -> `protocol.ConfigTypes [ZERO]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `command_safety.windows_dangerous_commands` -> `commandsafety.WindowsDangerousCommands [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. WindowsDangerousCommands.kt: Rust lifetime explanation in Kotlin comments
- `backend-client.types` -> `config.Types [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. Types.kt: snake_case identifier `hideGpt5_1MigrationPrompt` in Kotlin code
- `sandboxing.seatbelt` -> `core.Seatbelt [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `lmstudio.client` -> `client.ModelClient [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `command_safety.windows_safe_commands` -> `commandsafety.WindowsSafeCommands [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. WindowsSafeCommands.kt: Rust lifetime explanation in Kotlin comments; WindowsSafeCommands.kt: Rust-only type/unsafe terminology in Kotlin comments
- `core.message_history` -> `protocol.MessageHistory [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `tools.sandboxing` -> `tools.Sandboxing [ZERO]`: function-by-function score forced to 0. Sandboxing.kt: score-padding suppression annotation `@Suppress` in Kotlin code
- `write.storage` -> `auth.Storage [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `runtimes.unified_exec` -> `runtimes.UnifiedExec [ZERO]`: function-by-function score forced to 0. UnifiedExec.kt: score-padding suppression annotation `@Suppress` in Kotlin code
- `terminal-detection.lib` -> `terminal.TerminalDetection [STUB]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `execpolicy.error` -> `execpolicy.ExecPolicyError [ZERO]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `aws-auth.config` -> `otel.Config [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `mcp-server.tests.common.responses` -> `requests.ResponsesRequest [STUB] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies; ResponsesRequest.kt: Rust lifetime explanation in Kotlin comments
- `thread-store.error` -> `core.ErrorTest [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only
- `render.mod` -> `render.Render [STUB]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies
- `tools.plan_tool` -> `protocol.PlanTool [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no target functions found; report scoring is function-by-function only
- `codex-client.telemetry` -> `telemetry.Telemetry [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only
- `core.function_tool` -> `core.FunctionTool [ZERO]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only
- `models-manager.model_presets` -> `common.ModelPresets [ZERO] [PROVENANCE-FALLBACK]`: function-by-function score forced to 0. no source functions found; target defines functions; report scoring is function-by-function only
- `requests.headers` -> `requests.Headers [STUB]`: function-by-function score forced to 0. target contains TODO/stub/placeholder markers in function bodies

### Critical Ports (Similarity < 0.60, Worst First)

These files need significant work:

- `protocol.user_input` -> `protocol.UserInput [ZERO]` (0.00, 114 deps)
- `protocol.parse_command` -> `protocol.ParseCommand [ZERO] [PROVENANCE-FALLBACK]` (0.00, 6 deps)
- `protocol.account` -> `protocol.Account [ZERO] [PROVENANCE-FALLBACK]` (0.00, 3 deps)
- `command_safety.is_safe_command` -> `commandsafety.IsSafeCommand [ZERO] [PROVENANCE-FALLBACK]` (0.00, 1 deps)
- `core.compact` -> `endpoint.Compact [STUB] [PROVENANCE-FALLBACK]` (0.00, 1 deps)
- `command_safety.is_dangerous_command` -> `commandsafety.IsDangerousCommand [ZERO] [PROVENANCE-FALLBACK]` (0.00, 1 deps)
- `core.spawn` -> `core.Spawn [ZERO] [PROVENANCE-FALLBACK]` (0.00, 1 deps)
- `core-plugins.loader` -> `config.ConfigLoader [STUB] [PROVENANCE-FALLBACK]` (0.00)
- `protocol.config_types` -> `protocol.ConfigTypes [ZERO]` (0.00)
- `command_safety.windows_dangerous_commands` -> `commandsafety.WindowsDangerousCommands [ZERO] [PROVENANCE-FALLBACK]` (0.00)
- `backend-client.types` -> `config.Types [ZERO] [PROVENANCE-FALLBACK]` (0.00)
- `sandboxing.seatbelt` -> `core.Seatbelt [STUB] [PROVENANCE-FALLBACK]` (0.00)
- `lmstudio.client` -> `client.ModelClient [STUB] [PROVENANCE-FALLBACK]` (0.00)
- `command_safety.windows_safe_commands` -> `commandsafety.WindowsSafeCommands [ZERO] [PROVENANCE-FALLBACK]` (0.00)
- `core.message_history` -> `protocol.MessageHistory [ZERO] [PROVENANCE-FALLBACK]` (0.00)
- `tools.sandboxing` -> `tools.Sandboxing [ZERO]` (0.00)
- `write.storage` -> `auth.Storage [STUB] [PROVENANCE-FALLBACK]` (0.00)
- `runtimes.unified_exec` -> `runtimes.UnifiedExec [ZERO]` (0.00)
- `terminal-detection.lib` -> `terminal.TerminalDetection [STUB]` (0.00)
- `execpolicy.error` -> `execpolicy.ExecPolicyError [ZERO]` (0.00)
- `aws-auth.config` -> `otel.Config [ZERO] [PROVENANCE-FALLBACK]` (0.00)
- `mcp-server.tests.common.responses` -> `requests.ResponsesRequest [STUB] [PROVENANCE-FALLBACK]` (0.00)
- `thread-store.error` -> `core.ErrorTest [ZERO] [PROVENANCE-FALLBACK]` (0.00)
- `render.mod` -> `render.Render [STUB]` (0.00)
- `tools.plan_tool` -> `protocol.PlanTool [ZERO] [PROVENANCE-FALLBACK]` (0.00)
- `codex-client.telemetry` -> `telemetry.Telemetry [ZERO] [PROVENANCE-FALLBACK]` (0.00)
- `core.function_tool` -> `core.FunctionTool [ZERO]` (0.00)
- `models-manager.model_presets` -> `common.ModelPresets [ZERO] [PROVENANCE-FALLBACK]` (0.00)
- `requests.headers` -> `requests.Headers [STUB]` (0.00)
- `network-proxy.responses` -> `endpoint.Responses [PROVENANCE-FALLBACK]` (0.00, 51 deps)
- `tests.features` -> `features.Features [PROVENANCE-FALLBACK]` (0.00, 33 deps)
- `tool.terminal` -> `core.Terminal [PROVENANCE-FALLBACK]` (0.00, 17 deps)
- `context.environment_context` -> `utils.Environment [PROVENANCE-FALLBACK]` (0.00, 3 deps)
- `context.user_instructions` -> `session.UserInstructions [PROVENANCE-FALLBACK]` (0.00, 3 deps)
- `protocol.auth` -> `core.Auth [PROVENANCE-FALLBACK]` (0.00, 2 deps)
- `cli.sandbox_mode_cli_arg` -> `common.SandboxModeCliArg` (0.00, 2 deps)
- `cli.approval_mode_cli_arg` -> `common.ApprovalModeCliArg` (0.00, 2 deps)
- `sse.responses` -> `streaming.SseParser` (0.00)
- `exec-server.protocol` -> `protocol.Protocol [PROVENANCE-FALLBACK]` (0.00)
- `core.exec_policy` -> `execpolicy.ExecPolicy` (0.00)
- `model-provider.provider` -> `provider.Provider [PROVENANCE-FALLBACK]` (0.00)
- `protocol.approvals` -> `protocol.Approvals [PROVENANCE-FALLBACK]` (0.00)
- `mcp-server.codex_tool_config` -> `config.DurationSerializers` (0.00)
- `login.device_code_auth` -> `auth.Hashing` (0.00)
- `suite.exec` -> `core.ExecExpiration [PROVENANCE-FALLBACK]` (0.00)
- `tools.spec` -> `tools.Spec` (0.00)
- `app-server.models` -> `protocol.Models [PROVENANCE-FALLBACK]` (0.00)
- `codex-api.error` -> `core.Error [PROVENANCE-FALLBACK]` (0.00)
- `unified_exec.session` -> `unifiedexec.Session [PROVENANCE-FALLBACK]` (0.00)
- `core.landlock` -> `core.Landlock` (0.00)
- `suite.user_notification` -> `core.UserNotification` (0.00)
- `core.exec` -> `core.Exec` (0.02)
- `core.turn_diff_tracker` -> `session.TurnDiffTracker` (0.03, 11 deps)
- `shell-command.bash` -> `bash.Bash` (0.04)
- `handlers.plan` -> `handlers.Plan` (0.05)
- `tools.context` -> `tools.Context` (0.05, 113 deps)
- `tui.app_event_sender` -> `tui.AppEventSender` (0.06, 26 deps)
- `handlers.view_image` -> `handlers.ViewImage` (0.06)
- `model-provider.auth` -> `api.Auth [PROVENANCE-FALLBACK]` (0.06)
- `shell-command.powershell` -> `core.PowerShell [PROVENANCE-FALLBACK]` (0.06)
- `core.client_common` -> `prompt.Prompt` (0.07)
- `handlers.mcp` -> `handlers.Mcp` (0.07)
- `execpolicy.policy` -> `execpolicy.Policy` (0.09)
- `protocol.num_format` -> `protocol.NumFormat` (0.10)
- `handlers.apply_patch` -> `handlers.ApplyPatch` (0.11)
- `protocol.exec_output` -> `protocol.ExecOutput` (0.12)
- `sandbox-summary.sandbox_summary` -> `common.SandboxSummary` (0.13)
- `tui.slash_command` -> `tui.SlashCommand` (0.15, 4 deps)
- `codex-api.common` -> `common.Common [PROVENANCE-FALLBACK]` (0.15, 6 deps)
- `cli.format_env_display` -> `common.FormatEnvDisplay` (0.15, 3 deps)
- `tui.terminal_palette` -> `tui.TerminalPalette` (0.18)
- `tui.frame_requester` -> `tui.FrameRequester` (0.18, 17 deps)
- `codex-api.rate_limits` -> `ratelimits.RateLimits [PROVENANCE-FALLBACK]` (0.19)
- `core.util` -> `core.Util [PROVENANCE-FALLBACK]` (0.19)
- `tools.router` -> `tools.Router` (0.21, 10 deps)
- `state.turn` -> `session.Turn` (0.22, 15 deps)
- `handlers.unified_exec` -> `handlers.UnifiedExec` (0.22)
- `tui.update_action` -> `tui.UpdateAction` (0.22, 6 deps)
- `tools.parallel` -> `tools.Parallel` (0.22)
- `ollama.parser` -> `ollama.Parser` (0.22, 32 deps)
- `tools.registry` -> `tools.Registry` (0.25)
- `handlers.test_sync` -> `handlers.TestSync` (0.26)
- `tools.orchestrator` -> `tools.Orchestrator` (0.26)
- `protocol.items` -> `protocol.Items [PROVENANCE-FALLBACK]` (0.26)
- `state.session` -> `state.SessionState` (0.26, 48 deps)
- `handlers.shell` -> `handlers.Shell` (0.28)
- `tui.key_hint` -> `tui.KeyHint` (0.29, 27 deps)
- `cli.exit_status` -> `cli.ExitStatus` (0.30, 5 deps)
- `unified_exec.errors` -> `unifiedexec.Errors` (0.31)
- `core.user_shell_command` -> `session.UserShellCommand` (0.33, 3 deps)
- `handlers.list_dir` -> `handlers.ListDir` (0.34)
- `runtimes.apply_patch` -> `runtimes.ApplyPatch` (0.34)
- `ollama.client` -> `ollama.Client` (0.38)
- `runtimes.shell` -> `runtimes.Shell` (0.39)
- `tui.style` -> `tui.Style` (0.41, 21 deps)
- `tui.frame_rate_limiter` -> `tui.FrameRateLimiter` (0.41, 1 deps)
- `handlers.mcp_resource` -> `handlers.McpResource` (0.42)
- `execpolicy.rule` -> `execpolicy.Rule` (0.48, 1 deps)
- `ollama.url` -> `ollama.Url` (0.53, 25 deps)
- `string.truncate` -> `context.TruncationPolicy [PROVENANCE-FALLBACK]` (0.55, 1 deps)
- `core.review_format` -> `core.ReviewFormat` (0.57)
- `core.shell` -> `shell.ShellDetector` (0.57, 11 deps)
- `execpolicy.decision` -> `execpolicy.Decision` (0.58, 17 deps)

## Incorrect Ports (Missing Types)

These files are matched (often via `// port-lint`) but appear to be missing one or more type declarations
present in the Rust source file.

| Source | Target | Missing types | Examples |
|--------|--------|---------------|----------|
| `protocol.user_input` | `protocol.UserInput [ZERO]` | 2/3 | `TextElement`, `ByteRange` |
| `tools.context` | `tools.Context` | 8/12 | `ToolCallSource`, `McpToolOutput`, `ToolSearchOutput`, `FunctionToolOutput`, `ApplyPatchToolOutput`, `AbortedToolOutput`, `ExecCommandToolOutput`, `UnifiedExecCodeModeResult` |
| `network-proxy.responses` | `endpoint.Responses [PROVENANCE-FALLBACK]` | 1/1 | `PolicyDecisionDetails` |
| `render.renderable` | `render.Renderable` | 1/8 | `RenderableExt` |
| `tui.app_event` | `tui.AppEvent` | 2/11 | `ConnectorsSnapshot`, `RealtimeWebrtcOffer` |
| `tui.key_hint` | `tui.KeyHint` | 1/2 | `KeyBindingListExt` |
| `tool.terminal` | `core.Terminal [PROVENANCE-FALLBACK]` | 10/10 | `TerminalOperationStart`, `ParsedTerminalRequest`, `ParsedTerminalResponse`, `ExecCommandBeginPayload`, `ExecCommandEndPayload`, `DispatchedToolTraceRequestPayload`, `DispatchedToolPayload`, `DispatchedWriteStdinArgs`, `DispatchedToolTraceResponsePayload`, `CodeModeExecResult` |
| `state.turn` | `session.Turn` | 3/7 | `MailboxDeliveryPhase`, `RemovedTask`, `PendingRequestPermissions` |
| `tools.router` | `tools.Router` | 1/3 | `ToolRouterParams` |
| `codex-api.common` | `common.Common [PROVENANCE-FALLBACK]` | 8/16 | `MemorySummarizeInput`, `RawMemory`, `RawMemoryMetadata`, `MemorySummarizeOutput`, `ResponseEvent`, `ResponseCreateWsRequest`, `ResponsesWsRequest`, `Item` |
| `context.environment_context` | `utils.Environment [PROVENANCE-FALLBACK]` | 4/4 | `EnvironmentContext`, `EnvironmentContextEnvironment`, `EnvironmentContextEnvironments`, `NetworkContext` |
| `protocol.account` | `protocol.Account [ZERO] [PROVENANCE-FALLBACK]` | 1/2 | `ProviderAccount` |
| `protocol.auth` | `core.Auth [PROVENANCE-FALLBACK]` | 2/4 | `RefreshTokenFailedError`, `RefreshTokenFailedReason` |
| `core.compact` | `endpoint.Compact [STUB] [PROVENANCE-FALLBACK]` | 2/2 | `InitialContextInjection`, `CompactionAnalyticsAttempt` |
| `execpolicy.rule` | `execpolicy.Rule` | 3/8 | `NetworkRuleProtocol`, `NetworkRule`, `RuleRef` |
| `core.spawn` | `core.Spawn [ZERO] [PROVENANCE-FALLBACK]` | 1/2 | `SpawnChildRequest` |
| `sse.responses` | `streaming.SseParser` | 8/8 | `Error`, `ResponseCompleted`, `ResponseCompletedUsage`, `ResponseCompletedInputTokensDetails`, `ResponseCompletedOutputTokensDetails`, `ResponsesStreamEvent`, `ResponsesEventError`, `TestCase` |
| `protocol.config_types` | `protocol.ConfigTypes [ZERO]` | 21/26 | `ApprovalsReviewer`, `ShellEnvironmentPolicyInherit`, `EnvironmentVariablePattern`, `ShellEnvironmentPolicy`, `WindowsSandboxLevel`, `Personality`, `WebSearchMode`, `WebSearchContextSize`, `WebSearchLocation`, `WebSearchToolConfig`, `WebSearchFilters`, `WebSearchUserLocationType`, `WebSearchUserLocation`, `WebSearchConfig`, `ServiceTier`, `ModelProviderAuthInfo`, `AltScreenMode`, `ModeKind`, `CollaborationMode`, `Settings`, `CollaborationModeMask` |
| `core-plugins.loader` | `config.ConfigLoader [STUB] [PROVENANCE-FALLBACK]` | 8/8 | `NonCuratedCacheRefreshMode`, `PluginMcpServersFile`, `PluginMcpFile`, `PluginAppFile`, `PluginAppConfig`, `ResolvedPluginSkills`, `PluginMcpDiscovery`, `MaterializedMarketplacePluginSource` |
| `exec-server.protocol` | `protocol.Protocol [PROVENANCE-FALLBACK]` | 36/37 | `ByteChunk`, `InitializeParams`, `InitializeResponse`, `ExecParams`, `ExecEnvPolicy`, `ExecResponse`, `ReadParams`, `ProcessOutputChunk`, `ReadResponse`, `WriteParams`, `WriteStatus`, `WriteResponse`, `TerminateParams`, `TerminateResponse`, `FsReadFileParams`, `FsReadFileResponse`, `FsWriteFileParams`, `FsWriteFileResponse`, `FsCreateDirectoryParams`, `FsCreateDirectoryResponse`, `FsGetMetadataParams`, `FsGetMetadataResponse`, `FsReadDirectoryParams`, `FsReadDirectoryEntry`, `FsReadDirectoryResponse`, `FsRemoveParams`, `FsRemoveResponse`, `FsCopyParams`, `FsCopyResponse`, `HttpHeader`, `HttpRequestParams`, `HttpRequestResponse`, `HttpRequestBodyDeltaNotification`, `ExecOutputDeltaNotification`, `ExecExitedNotification`, `ExecClosedNotification` |
| `core.exec` | `core.Exec` | 5/7 | `WindowsSandboxFilesystemOverrides`, `ExecCapturePolicy`, `ExecExpiration`, `ExecExpirationOutcome`, `RawExecToolCallOutput` |
| `core.exec_policy` | `execpolicy.ExecPolicy` | 7/7 | `ExecPolicyCommandOrigin`, `UnmatchedCommandContext`, `ExecPolicyCommands`, `ExecPolicyError`, `ExecPolicyUpdateError`, `ExecPolicyManager`, `ExecApprovalRequest` |
| `model-provider.provider` | `provider.Provider [PROVENANCE-FALLBACK]` | 7/7 | `ProviderCapabilities`, `ProviderAccountState`, `ProviderAccountError`, `ProviderAccountResult`, `ModelProvider`, `SharedModelProvider`, `ConfiguredModelProvider` |
| `backend-client.types` | `config.Types [ZERO] [PROVENANCE-FALLBACK]` | 13/13 | `CodeTaskDetailsResponse`, `Turn`, `TurnItem`, `ContentFragment`, `StructuredContent`, `DiffPayload`, `Worklog`, `WorklogMessage`, `Author`, `WorklogContent`, `TurnError`, `CodeTaskDetailsResponseExt`, `TurnAttemptsSiblingTurnsResponse` |
| `sandboxing.seatbelt` | `core.Seatbelt [STUB] [PROVENANCE-FALLBACK]` | 5/5 | `ProxyPolicyInputs`, `UnixDomainSocketPolicy`, `UnixSocketPathParam`, `SeatbeltAccessRoot`, `CreateSeatbeltCommandArgsParams` |
| `protocol.approvals` | `protocol.Approvals [PROVENANCE-FALLBACK]` | 16/20 | `ResolvedPermissionProfile`, `EscalationPermissions`, `ExecPolicyAmendment`, `NetworkApprovalProtocol`, `NetworkApprovalContext`, `NetworkPolicyRuleAction`, `GuardianRiskLevel`, `GuardianUserAuthorization`, `GuardianAssessmentOutcome`, `GuardianAssessmentStatus`, `GuardianAssessmentDecisionSource`, `GuardianCommandSource`, `GuardianAssessmentAction`, `NetworkPolicyAmendment`, `GuardianAssessmentEvent`, `ElicitationRequest` |
| `protocol.items` | `protocol.Items [PROVENANCE-FALLBACK]` | 11/17 | `HookPromptItem`, `HookPromptFragment`, `HookPromptXml`, `PlanItem`, `ImageViewItem`, `ImageGenerationItem`, `FileChangeItem`, `McpToolCallItem`, `McpToolCallStatus`, `McpToolCallError`, `ContextCompactionItem` |
| `execpolicy.policy` | `execpolicy.Policy` | 2/4 | `HeuristicsFallback`, `MatchOptions` |
| `tools.registry` | `tools.Registry` | 6/10 | `ToolArgumentDiffConsumer`, `AnyToolResult`, `PreToolUsePayload`, `PostToolUsePayload`, `AnyToolHandler`, `AfterToolUseHookDispatch` |
| `codex-api.rate_limits` | `ratelimits.RateLimits [PROVENANCE-FALLBACK]` | 4/5 | `RateLimitEventWindow`, `RateLimitEventDetails`, `RateLimitEventCredits`, `RateLimitEvent` |
| `handlers.apply_patch` | `handlers.ApplyPatch` | 2/3 | `ApplyPatchArgumentDiffConsumer`, `Output` |
| `lmstudio.client` | `client.ModelClient [STUB] [PROVENANCE-FALLBACK]` | 1/1 | `LMStudioClient` |
| `mcp-server.codex_tool_config` | `config.DurationSerializers` | 4/4 | `CodexToolCallParam`, `CodexToolCallApprovalPolicy`, `CodexToolCallSandboxMode`, `CodexToolCallReplyParam` |
| `handlers.shell` | `handlers.Shell` | 3/5 | `ShellCommandBackend`, `RunExecLikeArgs`, `Output` |
| `core.client_common` | `prompt.Prompt` | 4/5 | `ExecOutputJson`, `ExecOutputMetadataJson`, `ResponseStream`, `Item` |
| `login.device_code_auth` | `auth.Hashing` | 5/5 | `DeviceCode`, `UserCodeResp`, `UserCodeReq`, `TokenPollReq`, `CodeSuccessResp` |
| `tools.sandboxing` | `tools.Sandboxing [ZERO]` | 2/11 | `PermissionRequestPayload`, `ExecApprovalRequirement` |
| `handlers.unified_exec` | `handlers.UnifiedExec` | 2/4 | `WriteStdinArgs`, `Output` |
| `tui.terminal_palette` | `tui.TerminalPalette` | 2/3 | `StdoutColorLevel`, `Cache` |
| `handlers.view_image` | `handlers.ViewImage` | 3/5 | `ViewImageDetail`, `Output`, `ViewImageOutput` |
| `handlers.plan` | `handlers.Plan` | 2/3 | `PlanToolOutput`, `Output` |
| `terminal-detection.lib` | `terminal.TerminalDetection [STUB]` | 1/6 | `ProcessEnvironment` |
| `runtimes.unified_exec` | `runtimes.UnifiedExec [ZERO]` | 1/4 | `ApprovalKey` |
| `runtimes.shell` | `runtimes.Shell` | 2/4 | `ShellRuntimeBackend`, `ApprovalKey` |
| `handlers.mcp` | `handlers.Mcp` | 1/2 | `Output` |
| `model-provider.auth` | `api.Auth [PROVENANCE-FALLBACK]` | 2/2 | `AgentIdentityAuthProvider`, `UnauthenticatedAuthProvider` |
| `handlers.list_dir` | `handlers.ListDir` | 1/5 | `Output` |
| `execpolicy.error` | `execpolicy.ExecPolicyError [ZERO]` | 5/5 | `Result`, `TextPosition`, `TextRange`, `ErrorLocation`, `Error` |
| `core.util` | `core.Util [PROVENANCE-FALLBACK]` | 1/1 | `Auth401FeedbackSnapshot` |
| `exec.exec_events` | `exec.ExecEvents` | 5/33 | `CollabToolCallStatus`, `CollabTool`, `CollabAgentStatus`, `CollabAgentState`, `CollabToolCallItem` |
| `handlers.mcp_resource` | `handlers.McpResource` | 1/10 | `Output` |
| `tools.orchestrator` | `tools.Orchestrator` | 1/2 | `OrchestratorRunResult` |
| `handlers.test_sync` | `handlers.TestSync` | 1/5 | `Output` |
| `tools.spec` | `tools.Spec` | 1/1 | `McpToolPlanInputs` |
| `thread-store.error` | `core.ErrorTest [ZERO] [PROVENANCE-FALLBACK]` | 2/2 | `ThreadStoreResult`, `ThreadStoreError` |
| `codex-api.error` | `core.Error [PROVENANCE-FALLBACK]` | 1/1 | `ApiError` |
| `execpolicy-legacy.error` | `error.ApiError [PROVENANCE-FALLBACK]` | 2/2 | `Result`, `Error` |
| `agent-graph-store.error` | `error.TransportError [PROVENANCE-FALLBACK]` | 2/2 | `AgentGraphStoreResult`, `AgentGraphStoreError` |
| `render.mod` | `render.Render [STUB]` | 1/2 | `RectExt` |

## High Priority Missing Files

| Rank | Source file | Expected target | Deps | Functions | Classes/types | Symbols | Source path | Expected path |
|------|-------------|-----------------|------|-----------|---------------|---------|-------------|---------------|
| 1 | `protocol.v2` | `appserverprotocol.src.protocol.V2` | 3 | 228 | 469 | 697 | `app-server-protocol/src/protocol/v2.rs` | `appserverprotocol/src/protocol/V2.kt` |
| 2 | `tui.chatwidget` | `tui.src.chatwidget.Chatwidget` | 10 | 493 | 38 | 531 | `tui/src/chatwidget.rs` | `tui/src/chatwidget/Chatwidget.kt` |
| 3 | `bottom_pane.chat_composer` | `tui.src.bottompane.chatcomposer.ChatComposer` | 5 | 345 | 10 | 355 | `tui/src/bottom_pane/chat_composer.rs` | `tui/src/bottompane/chatcomposer/ChatComposer.kt` |
| 4 | `protocol.protocol` | `protocol.src.Protocol` | 4 | 152 | 173 | 325 | `protocol/src/protocol.rs` | `protocol/src/Protocol.kt` |
| 5 | `tui.history_cell` | `tui.src.historycell.HistoryCell` | 19 | 222 | 30 | 252 | `tui/src/history_cell.rs` | `tui/src/historycell/HistoryCell.kt` |
| 6 | `config.config_tests` | `core.src.config.ConfigTests` | 0 | 235 | 3 | 238 | `core/src/config/config_tests.rs` | `core/src/config/ConfigTests.kt` |
| 7 | `session.tests` | `core.src.session.tests.Tests` | 0 | 217 | 6 | 223 | `core/src/session/tests.rs` | `core/src/session/tests/Tests.kt` |
| 8 | `bottom_pane.textarea` | `tui.src.bottompane.Textarea` | 3 | 177 | 10 | 187 | `tui/src/bottom_pane/textarea.rs` | `tui/src/bottompane/Textarea.kt` |
| 9 | `protocol.models` | `protocol.src.Models` | 8 | 123 | 35 | 158 | `protocol/src/models.rs` | `protocol/src/Models.kt` |
| 10 | `app.tests` | `tui.src.app.tests.Tests` | 0 | 133 | 1 | 134 | `tui/src/app/tests.rs` | `tui/src/app/tests/Tests.kt` |
| 11 | `cli.main` | `cli.src.Main` | 0 | 99 | 35 | 134 | `cli/src/main.rs` | `cli/src/Main.kt` |
| 12 | `auth.manager` | `login.src.auth.Manager` | 0 | 105 | 23 | 128 | `login/src/auth/manager.rs` | `login/src/auth/Manager.kt` |
| 13 | `request_processors.thread_processor` | `appserver.src.requestprocessors.ThreadProcessor` | 0 | 123 | 5 | 128 | `app-server/src/request_processors/thread_processor.rs` | `appserver/src/requestprocessors/ThreadProcessor.kt` |
| 14 | `config.config_requirements` | `config.src.ConfigRequirements` | 14 | 95 | 29 | 124 | `config/src/config_requirements.rs` | `config/src/ConfigRequirements.kt` |
| 15 | `protocol.permissions` | `protocol.src.Permissions` | 4 | 114 | 10 | 124 | `protocol/src/permissions.rs` | `protocol/src/Permissions.kt` |
| 16 | `common.responses` | `core.tests.common.Responses` | 0 | 114 | 10 | 124 | `core/tests/common/responses.rs` | `core/tests/common/Responses.kt` |
| 17 | `app-server-protocol.export` | `appserverprotocol.src.Export` | 0 | 117 | 6 | 123 | `app-server-protocol/src/export.rs` | `appserverprotocol/src/Export.kt` |
| 18 | `network-proxy.runtime` | `networkproxy.src.Runtime` | 0 | 110 | 11 | 121 | `network-proxy/src/runtime.rs` | `networkproxy/src/Runtime.kt` |
| 19 | `shell-command.parse_command` | `shellcommand.src.ParseCommand` | 0 | 117 | 0 | 117 | `shell-command/src/parse_command.rs` | `shellcommand/src/ParseCommand.kt` |
| 20 | `app-server.tests.common.mcp_process` | `appserver.tests.common.McpProcess` | 0 | 115 | 1 | 116 | `app-server/tests/common/mcp_process.rs` | `appserver/tests/common/McpProcess.kt` |
| 21 | `bottom_pane.mcp_server_elicitation` | `tui.src.bottompane.McpServerElicitation` | 0 | 103 | 13 | 116 | `tui/src/bottom_pane/mcp_server_elicitation.rs` | `tui/src/bottompane/McpServerElicitation.kt` |
| 22 | `bottom_pane.list_selection_view` | `tui.src.bottompane.ListSelectionView` | 3 | 95 | 12 | 107 | `tui/src/bottom_pane/list_selection_view.rs` | `tui/src/bottompane/ListSelectionView.kt` |
| 23 | `tui.diff_render` | `tui.src.DiffRender` | 0 | 97 | 8 | 105 | `tui/src/diff_render.rs` | `tui/src/DiffRender.kt` |
| 24 | `tests.status_and_layout` | `tui.src.chatwidget.tests.StatusAndLayout` | 0 | 102 | 1 | 103 | `tui/src/chatwidget/tests/status_and_layout.rs` | `tui/src/chatwidget/tests/StatusAndLayout.kt` |
| 25 | `protocol.thread_history` | `appserverprotocol.src.protocol.ThreadHistory` | 0 | 98 | 2 | 100 | `app-server-protocol/src/protocol/thread_history.rs` | `appserverprotocol/src/protocol/ThreadHistory.kt` |
| 26 | `linux-sandbox.bwrap` | `linuxsandbox.src.Bwrap` | 0 | 85 | 8 | 93 | `linux-sandbox/src/bwrap.rs` | `linuxsandbox/src/Bwrap.kt` |
| 27 | `tui.resume_picker` | `tui.src.ResumePicker` | 0 | 73 | 20 | 93 | `tui/src/resume_picker.rs` | `tui/src/ResumePicker.kt` |
| 28 | `tui.wrapping` | `tui.src.Wrapping` | 0 | 90 | 3 | 93 | `tui/src/wrapping.rs` | `tui/src/Wrapping.kt` |
| 29 | `tui.app_server_session` | `tui.src.AppServerSession` | 8 | 88 | 4 | 92 | `tui/src/app_server_session.rs` | `tui/src/AppServerSession.kt` |
| 30 | `proto.codex.thread_store.v1` | `threadstore.src.remote.proto.Codex.threadStore.v1` | 0 | 56 | 34 | 90 | `thread-store/src/remote/proto/codex.thread_store.v1.rs` | `threadstore/src/remote/proto/Codex.threadStore.v1.kt` |
| 31 | `tests.helpers` | `tui.src.chatwidget.tests.Helpers` | 0 | 90 | 0 | 90 | `tui/src/chatwidget/tests/helpers.rs` | `tui/src/chatwidget/tests/Helpers.kt` |
| 32 | `core.exec_policy_tests` | `core.src.ExecPolicyTests` | 0 | 88 | 1 | 89 | `core/src/exec_policy_tests.rs` | `core/src/ExecPolicyTests.kt` |
| 33 | `tui.markdown_render_tests` | `tui.src.MarkdownRenderTests` | 0 | 89 | 0 | 89 | `tui/src/markdown_render_tests.rs` | `tui/src/MarkdownRenderTests.kt` |
| 34 | `realtime_websocket.methods` | `codexapi.src.endpoint.realtimewebsocket.Methods` | 0 | 78 | 7 | 85 | `codex-api/src/endpoint/realtime_websocket/methods.rs` | `codexapi/src/endpoint/realtimewebsocket/Methods.kt` |
| 35 | `common.test_codex` | `core.tests.common.TestCodex` | 77 | 74 | 9 | 83 | `core/tests/common/test_codex.rs` | `core/tests/common/TestCodex.kt` |
| 36 | `tests.slash_commands` | `tui.src.chatwidget.tests.SlashCommands` | 0 | 83 | 0 | 83 | `tui/src/chatwidget/tests/slash_commands.rs` | `tui/src/chatwidget/tests/SlashCommands.kt` |
| 37 | `render.highlight` | `tui.src.render.Highlight` | 1 | 80 | 2 | 82 | `tui/src/render/highlight.rs` | `tui/src/render/Highlight.kt` |
| 38 | `tui.pager_overlay` | `tui.src.PagerOverlay` | 0 | 74 | 8 | 82 | `tui/src/pager_overlay.rs` | `tui/src/PagerOverlay.kt` |
| 39 | `bottom_pane.approval_overlay` | `tui.src.bottompane.ApprovalOverlay` | 1 | 76 | 5 | 81 | `tui/src/bottom_pane/approval_overlay.rs` | `tui/src/bottompane/ApprovalOverlay.kt` |
| 40 | `tui.keymap_setup` | `tui.src.keymapsetup.KeymapSetup` | 1 | 79 | 2 | 81 | `tui/src/keymap_setup.rs` | `tui/src/keymapsetup/KeymapSetup.kt` |
| 41 | `core-plugins.manager` | `coreplugins.src.Manager` | 0 | 55 | 25 | 80 | `core-plugins/src/manager.rs` | `coreplugins/src/Manager.kt` |
| 42 | `core-skills.render` | `coreskills.src.Render` | 0 | 70 | 9 | 79 | `core-skills/src/render.rs` | `coreskills/src/Render.kt` |
| 43 | `context_manager.history_tests` | `core.src.contextmanager.HistoryTests` | 0 | 77 | 0 | 77 | `core/src/context_manager/history_tests.rs` | `core/src/contextmanager/HistoryTests.kt` |
| 44 | `config.edit` | `core.src.config.Edit` | 0 | 69 | 6 | 75 | `core/src/config/edit.rs` | `core/src/config/Edit.kt` |
| 45 | `chatwidget.plugins` | `tui.src.chatwidget.Plugins` | 0 | 71 | 3 | 74 | `tui/src/chatwidget/plugins.rs` | `tui/src/chatwidget/Plugins.kt` |
| 46 | `core.mcp_tool_call_tests` | `core.src.McpToolCallTests` | 0 | 74 | 0 | 74 | `core/src/mcp_tool_call_tests.rs` | `core/src/McpToolCallTests.kt` |
| 47 | `handlers.multi_agents_tests` | `core.src.tools.handlers.MultiAgentsTests` | 0 | 71 | 3 | 74 | `core/src/tools/handlers/multi_agents_tests.rs` | `core/src/tools/handlers/MultiAgentsTests.kt` |
| 48 | `core.thread_manager` | `core.src.ThreadManager` | 21 | 61 | 12 | 73 | `core/src/thread_manager.rs` | `core/src/ThreadManager.kt` |
| 49 | `linux-sandbox.linux_run_main` | `linuxsandbox.src.LinuxRunMain` | 0 | 61 | 11 | 72 | `linux-sandbox/src/linux_run_main.rs` | `linuxsandbox/src/LinuxRunMain.kt` |
| 50 | `network-proxy.proxy` | `networkproxy.src.Proxy` | 0 | 65 | 7 | 72 | `network-proxy/src/proxy.rs` | `networkproxy/src/Proxy.kt` |
| 51 | `core.client` | `core.src.Client` | 6 | 57 | 14 | 71 | `core/src/client.rs` | `core/src/Client.kt` |
| 52 | `analytics.analytics_client_tests` | `analytics.src.AnalyticsClientTests` | 0 | 71 | 0 | 71 | `analytics/src/analytics_client_tests.rs` | `analytics/src/AnalyticsClientTests.kt` |
| 53 | `rollout.recorder` | `rollout.src.Recorder` | 0 | 61 | 10 | 71 | `rollout/src/recorder.rs` | `rollout/src/Recorder.kt` |
| 54 | `remote_control.websocket` | `appservertransport.src.transport.remotecontrol.Websocket` | 0 | 62 | 8 | 70 | `app-server-transport/src/transport/remote_control/websocket.rs` | `appservertransport/src/transport/remotecontrol/Websocket.kt` |
| 55 | `v2.realtime_conversation` | `appserver.tests.suite.v2.RealtimeConversation` | 0 | 61 | 9 | 70 | `app-server/tests/suite/v2/realtime_conversation.rs` | `appserver/tests/suite/v2/RealtimeConversation.kt` |
| 56 | `tests.popups_and_settings` | `tui.src.chatwidget.tests.PopupsAndSettings` | 0 | 69 | 0 | 69 | `tui/src/chatwidget/tests/popups_and_settings.rs` | `tui/src/chatwidget/tests/PopupsAndSettings.kt` |
| 57 | `tui.custom_terminal` | `tui.src.CustomTerminal` | 1 | 63 | 5 | 68 | `tui/src/custom_terminal.rs` | `tui/src/CustomTerminal.kt` |
| 58 | `core.goals` | `core.src.Goals` | 0 | 59 | 9 | 68 | `core/src/goals.rs` | `core/src/Goals.kt` |
| 59 | `tui.keymap` | `tui.src.Keymap` | 0 | 58 | 10 | 68 | `tui/src/keymap.rs` | `tui/src/Keymap.kt` |
| 60 | `windows-sandbox-rs.setup_orchestrator` | `windowssandboxrs.src.SetupOrchestrator` | 0 | 60 | 8 | 68 | `windows-sandbox-rs/src/setup_orchestrator.rs` | `windowssandboxrs/src/SetupOrchestrator.kt` |
| 61 | `exec-server.client` | `execserver.src.client.Client` | 0 | 59 | 7 | 66 | `exec-server/src/client.rs` | `execserver/src/client/Client.kt` |
| 62 | `tui.markdown_render` | `tui.src.MarkdownRender` | 0 | 62 | 4 | 66 | `tui/src/markdown_render.rs` | `tui/src/MarkdownRender.kt` |
| 63 | `network-proxy.network_policy` | `networkproxy.src.NetworkPolicy` | 0 | 52 | 13 | 65 | `network-proxy/src/network_policy.rs` | `networkproxy/src/NetworkPolicy.kt` |
| 64 | `onboarding.auth` | `tui.src.onboarding.auth.Auth` | 0 | 59 | 6 | 65 | `tui/src/onboarding/auth.rs` | `tui/src/onboarding/auth/Auth.kt` |
| 65 | `core-plugins.manager_tests` | `coreplugins.src.ManagerTests` | 0 | 64 | 0 | 64 | `core-plugins/src/manager_tests.rs` | `coreplugins/src/ManagerTests.kt` |
| 66 | `core.mcp_tool_call` | `core.src.McpToolCall` | 0 | 56 | 8 | 64 | `core/src/mcp_tool_call.rs` | `core/src/McpToolCall.kt` |
| 67 | `network-proxy.config` | `networkproxy.src.Config` | 0 | 52 | 12 | 64 | `network-proxy/src/config.rs` | `networkproxy/src/Config.kt` |
| 68 | `config.external_agent_config` | `appserver.src.config.ExternalAgentConfig` | 0 | 53 | 10 | 63 | `app-server/src/config/external_agent_config.rs` | `appserver/src/config/ExternalAgentConfig.kt` |
| 69 | `tests.plan_mode` | `tui.src.chatwidget.tests.PlanMode` | 0 | 63 | 0 | 63 | `tui/src/chatwidget/tests/plan_mode.rs` | `tui/src/chatwidget/tests/PlanMode.kt` |
| 70 | `app-server.bespoke_event_handling` | `appserver.src.BespokeEventHandling` | 0 | 57 | 5 | 62 | `app-server/src/bespoke_event_handling.rs` | `appserver/src/BespokeEventHandling.kt` |
| 71 | `app-server.outgoing_message` | `appserver.src.OutgoingMessage` | 0 | 55 | 7 | 62 | `app-server/src/outgoing_message.rs` | `appserver/src/OutgoingMessage.kt` |
| 72 | `core.realtime_conversation` | `core.src.RealtimeConversation` | 0 | 49 | 13 | 62 | `core/src/realtime_conversation.rs` | `core/src/RealtimeConversation.kt` |
| 73 | `protocol.common` | `appserverprotocol.src.protocol.Common` | 0 | 47 | 15 | 62 | `app-server-protocol/src/protocol/common.rs` | `appserverprotocol/src/protocol/Common.kt` |
| 74 | `bottom_pane.footer` | `tui.src.bottompane.Footer` | 0 | 48 | 13 | 61 | `tui/src/bottom_pane/footer.rs` | `tui/src/bottompane/Footer.kt` |
| 75 | `guardian.review_session` | `core.src.guardian.ReviewSession` | 0 | 52 | 9 | 61 | `core/src/guardian/review_session.rs` | `core/src/guardian/ReviewSession.kt` |
| 76 | `tools.tool_registry_plan_tests` | `tools.src.ToolRegistryPlanTests` | 0 | 61 | 0 | 61 | `tools/src/tool_registry_plan_tests.rs` | `tools/src/ToolRegistryPlanTests.kt` |
| 77 | `history_cell.hook_cell` | `tui.src.historycell.HookCell` | 2 | 54 | 5 | 59 | `tui/src/history_cell/hook_cell.rs` | `tui/src/historycell/HookCell.kt` |
| 78 | `runtime.memories` | `state.src.runtime.Memories` | 0 | 59 | 0 | 59 | `state/src/runtime/memories.rs` | `state/src/runtime/Memories.kt` |
| 79 | `rollout.list` | `rollout.src.List` | 2 | 42 | 16 | 58 | `rollout/src/list.rs` | `rollout/src/List.kt` |
| 80 | `state.log_db` | `state.src.LogDb` | 2 | 48 | 10 | 58 | `state/src/log_db.rs` | `state/src/LogDb.kt` |
| 81 | `app-server.thread_status` | `appserver.src.ThreadStatus` | 14 | 52 | 5 | 57 | `app-server/src/thread_status.rs` | `appserver/src/ThreadStatus.kt` |
| 82 | `tui.external_agent_config_migration` | `tui.src.ExternalAgentConfigMigration` | 0 | 50 | 7 | 57 | `tui/src/external_agent_config_migration.rs` | `tui/src/ExternalAgentConfigMigration.kt` |
| 83 | `suite.hooks` | `core.tests.suite.Hooks` | 5 | 56 | 0 | 56 | `core/tests/suite/hooks.rs` | `core/tests/suite/Hooks.kt` |
| 84 | `bottom_pane.hooks_browser_view` | `tui.src.bottompane.HooksBrowserView` | 2 | 53 | 3 | 56 | `tui/src/bottom_pane/hooks_browser_view.rs` | `tui/src/bottompane/HooksBrowserView.kt` |
| 85 | `agent.control_tests` | `core.src.agent.ControlTests` | 0 | 55 | 1 | 56 | `core/src/agent/control_tests.rs` | `core/src/agent/ControlTests.kt` |
| 86 | `config.config_loader_tests` | `core.src.config.ConfigLoaderTests` | 0 | 56 | 0 | 56 | `core/src/config/config_loader_tests.rs` | `core/src/config/ConfigLoaderTests.kt` |
| 87 | `tui.tui` | `tui.src.tui.Tui` | 15 | 47 | 8 | 55 | `tui/src/tui.rs` | `tui/src/tui/Tui.kt` |
| 88 | `core-plugins.remote` | `coreplugins.src.remote.Remote` | 0 | 34 | 21 | 55 | `core-plugins/src/remote.rs` | `coreplugins/src/remote/Remote.kt` |
| 89 | `guardian.tests` | `core.src.guardian.Tests` | 0 | 55 | 0 | 55 | `core/src/guardian/tests.rs` | `core/src/guardian/Tests.kt` |
| 90 | `ide_context.ipc` | `tui.src.idecontext.Ipc` | 0 | 52 | 3 | 55 | `tui/src/ide_context/ipc.rs` | `tui/src/idecontext/Ipc.kt` |
| 91 | `app.thread_routing` | `tui.src.app.ThreadRouting` | 0 | 54 | 0 | 54 | `tui/src/app/thread_routing.rs` | `tui/src/app/ThreadRouting.kt` |
| 92 | `config.types` | `config.src.Types` | 0 | 15 | 39 | 54 | `config/src/types.rs` | `config/src/Types.kt` |
| 93 | `core-skills.loader_tests` | `coreskills.src.LoaderTests` | 0 | 53 | 1 | 54 | `core-skills/src/loader_tests.rs` | `coreskills/src/LoaderTests.kt` |
| 94 | `protocol.openai_models` | `protocol.src.OpenaiModels` | 0 | 35 | 19 | 54 | `protocol/src/openai_models.rs` | `protocol/src/OpenaiModels.kt` |
| 95 | `suite.rmcp_client` | `core.tests.suite.RmcpClient` | 0 | 48 | 6 | 54 | `core/tests/suite/rmcp_client.rs` | `core/tests/suite/RmcpClient.kt` |
| 96 | `analytics.reducer` | `analytics.src.Reducer` | 0 | 42 | 11 | 53 | `analytics/src/reducer.rs` | `analytics/src/Reducer.kt` |
| 97 | `runtime.threads` | `state.src.runtime.Threads` | 0 | 52 | 1 | 53 | `state/src/runtime/threads.rs` | `state/src/runtime/Threads.kt` |
| 98 | `core.file_watcher` | `core.src.FileWatcher` | 3 | 35 | 17 | 52 | `core/src/file_watcher.rs` | `core/src/FileWatcher.kt` |
| 99 | `analytics.events` | `analytics.src.Events` | 0 | 17 | 35 | 52 | `analytics/src/events.rs` | `analytics/src/Events.kt` |
| 100 | `chatwidget.status_surfaces` | `tui.src.chatwidget.StatusSurfaces` | 0 | 49 | 3 | 52 | `tui/src/chatwidget/status_surfaces.rs` | `tui/src/chatwidget/StatusSurfaces.kt` |
| 101 | `cloud-tasks-client.http` | `cloudtasksclient.src.Http` | 0 | 48 | 4 | 52 | `cloud-tasks-client/src/http.rs` | `cloudtasksclient/src/Http.kt` |
| 102 | `suite.code_mode` | `core.tests.suite.CodeMode` | 0 | 52 | 0 | 52 | `core/tests/suite/code_mode.rs` | `core/tests/suite/CodeMode.kt` |
| 103 | `hooks.schema` | `hooks.src.Schema` | 5 | 26 | 25 | 51 | `hooks/src/schema.rs` | `hooks/src/Schema.kt` |
| 104 | `apply-patch.invocation` | `applypatch.src.Invocation` | 0 | 48 | 3 | 51 | `apply-patch/src/invocation.rs` | `applypatch/src/Invocation.kt` |
| 105 | `streaming.controller` | `tui.src.streaming.Controller` | 0 | 47 | 3 | 50 | `tui/src/streaming/controller.rs` | `tui/src/streaming/Controller.kt` |
| 106 | `suite.client_websockets` | `core.tests.suite.ClientWebsockets` | 0 | 49 | 1 | 50 | `core/tests/suite/client_websockets.rs` | `core/tests/suite/ClientWebsockets.kt` |
| 107 | `suite.realtime_conversation` | `core.tests.suite.RealtimeConversation` | 0 | 49 | 1 | 50 | `core/tests/suite/realtime_conversation.rs` | `core/tests/suite/RealtimeConversation.kt` |
| 108 | `app.background_requests` | `tui.src.app.BackgroundRequests` | 0 | 48 | 1 | 49 | `tui/src/app/background_requests.rs` | `tui/src/app/BackgroundRequests.kt` |
| 109 | `auth.auth_tests` | `login.src.auth.AuthTests` | 0 | 45 | 4 | 49 | `login/src/auth/auth_tests.rs` | `login/src/auth/AuthTests.kt` |
| 110 | `tools.network_approval` | `core.src.tools.NetworkApproval` | 0 | 38 | 11 | 49 | `core/src/tools/network_approval.rs` | `core/src/tools/NetworkApproval.kt` |
| 111 | `login.server` | `login.src.Server` | 5 | 40 | 8 | 48 | `login/src/server.rs` | `login/src/Server.kt` |
| 112 | `bottom_pane.chat_composer_history` | `tui.src.bottompane.ChatComposerHistory` | 1 | 39 | 8 | 47 | `tui/src/bottom_pane/chat_composer_history.rs` | `tui/src/bottompane/ChatComposerHistory.kt` |
| 113 | `tui.multi_agents` | `tui.src.MultiAgents` | 1 | 43 | 4 | 47 | `tui/src/multi_agents.rs` | `tui/src/MultiAgents.kt` |
| 114 | `backend-client.client` | `backendclient.src.Client` | 0 | 42 | 5 | 47 | `backend-client/src/client.rs` | `backendclient/src/Client.kt` |
| 115 | `config.external_agent_config_tests` | `appserver.src.config.ExternalAgentConfigTests` | 0 | 47 | 0 | 47 | `app-server/src/config/external_agent_config_tests.rs` | `appserver/src/config/ExternalAgentConfigTests.kt` |
| 116 | `network-proxy.policy` | `networkproxy.src.Policy` | 0 | 44 | 3 | 47 | `network-proxy/src/policy.rs` | `networkproxy/src/Policy.kt` |
| 117 | `rmcp-client.oauth` | `rmcpclient.src.Oauth` | 0 | 40 | 7 | 47 | `rmcp-client/src/oauth.rs` | `rmcpclient/src/Oauth.kt` |
| 118 | `tests.exec_flow` | `tui.src.chatwidget.tests.ExecFlow` | 0 | 47 | 0 | 47 | `tui/src/chatwidget/tests/exec_flow.rs` | `tui/src/chatwidget/tests/ExecFlow.kt` |
| 119 | `v2.plugin_install` | `appserver.tests.suite.v2.PluginInstall` | 0 | 44 | 3 | 47 | `app-server/tests/suite/v2/plugin_install.rs` | `appserver/tests/suite/v2/PluginInstall.kt` |
| 120 | `bottom_pane.app_link_view` | `tui.src.bottompane.AppLinkView` | 1 | 41 | 5 | 46 | `tui/src/bottom_pane/app_link_view.rs` | `tui/src/bottompane/AppLinkView.kt` |
| 121 | `core-plugins.marketplace` | `coreplugins.src.Marketplace` | 0 | 29 | 17 | 46 | `core-plugins/src/marketplace.rs` | `coreplugins/src/Marketplace.kt` |
| 122 | `request_processors.account_processor` | `appserver.src.requestprocessors.AccountProcessor` | 0 | 42 | 4 | 46 | `app-server/src/request_processors/account_processor.rs` | `appserver/src/requestprocessors/AccountProcessor.kt` |
| 123 | `suite.client` | `core.tests.suite.Client` | 0 | 45 | 1 | 46 | `core/tests/suite/client.rs` | `core/tests/suite/Client.kt` |
| 124 | `agent.control` | `core.src.agent.Control` | 0 | 40 | 5 | 45 | `core/src/agent/control.rs` | `core/src/agent/Control.kt` |
| 125 | `rmcp-client.rmcp_client` | `rmcpclient.src.RmcpClient` | 0 | 32 | 13 | 45 | `rmcp-client/src/rmcp_client.rs` | `rmcpclient/src/RmcpClient.kt` |
| 126 | `suite.compact_remote` | `core.tests.suite.CompactRemote` | 0 | 45 | 0 | 45 | `core/tests/suite/compact_remote.rs` | `core/tests/suite/CompactRemote.kt` |
| 127 | `tools.spec_tests` | `core.src.tools.SpecTests` | 0 | 45 | 0 | 45 | `core/src/tools/spec_tests.rs` | `core/src/tools/SpecTests.kt` |
| 128 | `tui.app_backtrack` | `tui.src.AppBacktrack` | 0 | 42 | 3 | 45 | `tui/src/app_backtrack.rs` | `tui/src/AppBacktrack.kt` |
| 129 | `exec-server.environment` | `execserver.src.Environment` | 9 | 41 | 3 | 44 | `exec-server/src/environment.rs` | `execserver/src/Environment.kt` |
| 130 | `exec-server.local_process` | `execserver.src.LocalProcess` | 2 | 37 | 7 | 44 | `exec-server/src/local_process.rs` | `execserver/src/LocalProcess.kt` |
| 131 | `transport.auth` | `appservertransport.src.transport.Auth` | 0 | 33 | 11 | 44 | `app-server-transport/src/transport/auth.rs` | `appservertransport/src/transport/Auth.kt` |
| 132 | `events.session_telemetry` | `otel.src.events.SessionTelemetry` | 25 | 40 | 3 | 43 | `otel/src/events/session_telemetry.rs` | `otel/src/events/SessionTelemetry.kt` |
| 133 | `bottom_pane.multi_select_picker` | `tui.src.bottompane.MultiSelectPicker` | 2 | 34 | 9 | 43 | `tui/src/bottom_pane/multi_select_picker.rs` | `tui/src/bottompane/MultiSelectPicker.kt` |
| 134 | `models-manager.manager_tests` | `modelsmanager.src.ManagerTests` | 0 | 39 | 4 | 43 | `models-manager/src/manager_tests.rs` | `modelsmanager/src/ManagerTests.kt` |
| 135 | `request_processors.plugins` | `appserver.src.requestprocessors.Plugins` | 0 | 42 | 1 | 43 | `app-server/src/request_processors/plugins.rs` | `appserver/src/requestprocessors/Plugins.kt` |
| 136 | `core.codex_thread` | `core.src.CodexThread` | 22 | 39 | 3 | 42 | `core/src/codex_thread.rs` | `core/src/CodexThread.kt` |
| 137 | `suite.unified_exec` | `core.tests.suite.UnifiedExec` | 1 | 41 | 1 | 42 | `core/tests/suite/unified_exec.rs` | `core/tests/suite/UnifiedExec.kt` |
| 138 | `git-utils.baseline` | `gitutils.src.Baseline` | 0 | 38 | 4 | 42 | `git-utils/src/baseline.rs` | `gitutils/src/Baseline.kt` |
| 139 | `linux-sandbox.proxy_routing` | `linuxsandbox.src.ProxyRouting` | 0 | 38 | 4 | 42 | `linux-sandbox/src/proxy_routing.rs` | `linuxsandbox/src/ProxyRouting.kt` |
| 140 | `session.turn` | `core.src.session.Turn` | 0 | 36 | 6 | 42 | `core/src/session/turn.rs` | `core/src/session/Turn.kt` |
| 141 | `unix.escalate_server` | `shellescalation.src.unix.EscalateServer` | 3 | 29 | 12 | 41 | `shell-escalation/src/unix/escalate_server.rs` | `shellescalation/src/unix/EscalateServer.kt` |
| 142 | `app.pending_interactive_replay` | `tui.src.app.PendingInteractiveReplay` | 0 | 38 | 3 | 41 | `tui/src/app/pending_interactive_replay.rs` | `tui/src/app/PendingInteractiveReplay.kt` |
| 143 | `bottom_pane.feedback_view` | `tui.src.bottompane.FeedbackView` | 0 | 39 | 2 | 41 | `tui/src/bottom_pane/feedback_view.rs` | `tui/src/bottompane/FeedbackView.kt` |
| 144 | `config.edit_tests` | `core.src.config.EditTests` | 0 | 41 | 0 | 41 | `core/src/config/edit_tests.rs` | `core/src/config/EditTests.kt` |
| 145 | `core-plugins.startup_sync` | `coreplugins.src.StartupSync` | 0 | 37 | 4 | 41 | `core-plugins/src/startup_sync.rs` | `coreplugins/src/StartupSync.kt` |
| 146 | `suite.apply_patch_cli` | `core.tests.suite.ApplyPatchCli` | 0 | 40 | 1 | 41 | `core/tests/suite/apply_patch_cli.rs` | `core/tests/suite/ApplyPatchCli.kt` |
| 147 | `v2.thread_resume` | `appserver.tests.suite.v2.ThreadResume` | 0 | 39 | 2 | 41 | `app-server/tests/suite/v2/thread_resume.rs` | `appserver/tests/suite/v2/ThreadResume.kt` |
| 148 | `context_manager.history` | `core.src.contextmanager.History` | 4 | 38 | 2 | 40 | `core/src/context_manager/history.rs` | `core/src/contextmanager/History.kt` |
| 149 | `core-skills.loader` | `coreskills.src.Loader` | 1 | 30 | 10 | 40 | `core-skills/src/loader.rs` | `coreskills/src/Loader.kt` |
| 150 | `app-server.message_processor` | `appserver.src.MessageProcessor` | 0 | 35 | 5 | 40 | `app-server/src/message_processor.rs` | `appserver/src/MessageProcessor.kt` |
| 151 | `code-mode.description` | `codemode.src.Description` | 0 | 34 | 6 | 40 | `code-mode/src/description.rs` | `codemode/src/Description.kt` |
| 152 | `codex-mcp.connection_manager_tests` | `codexmcp.src.ConnectionManagerTests` | 0 | 40 | 0 | 40 | `codex-mcp/src/connection_manager_tests.rs` | `codexmcp/src/ConnectionManagerTests.kt` |
| 153 | `core.connectors_tests` | `core.src.ConnectorsTests` | 0 | 40 | 0 | 40 | `core/src/connectors_tests.rs` | `core/src/ConnectorsTests.kt` |
| 154 | `core.exec_tests` | `core.src.ExecTests` | 0 | 40 | 0 | 40 | `core/src/exec_tests.rs` | `core/src/ExecTests.kt` |
| 155 | `features.tests` | `features.src.Tests` | 0 | 40 | 0 | 40 | `features/src/tests.rs` | `features/src/Tests.kt` |
| 156 | `exec-server.local_file_system` | `execserver.src.LocalFileSystem` | 4 | 36 | 3 | 39 | `exec-server/src/local_file_system.rs` | `execserver/src/LocalFileSystem.kt` |
| 157 | `rmcp-client.perform_oauth_login` | `rmcpclient.src.PerformOauthLogin` | 3 | 31 | 8 | 39 | `rmcp-client/src/perform_oauth_login.rs` | `rmcpclient/src/PerformOauthLogin.kt` |
| 158 | `unix.socket` | `shellescalation.src.unix.Socket` | 1 | 36 | 3 | 39 | `shell-escalation/src/unix/socket.rs` | `shellescalation/src/unix/Socket.kt` |
| 159 | `app.thread_events` | `tui.src.app.ThreadEvents` | 0 | 34 | 5 | 39 | `tui/src/app/thread_events.rs` | `tui/src/app/ThreadEvents.kt` |
| 160 | `chatwidget.realtime` | `tui.src.chatwidget.Realtime` | 0 | 36 | 3 | 39 | `tui/src/chatwidget/realtime.rs` | `tui/src/chatwidget/Realtime.kt` |
| 161 | `exec-server.rpc` | `execserver.src.Rpc` | 0 | 29 | 10 | 39 | `exec-server/src/rpc.rs` | `execserver/src/Rpc.kt` |
| 162 | `models-manager.manager` | `modelsmanager.src.Manager` | 0 | 32 | 7 | 39 | `models-manager/src/manager.rs` | `modelsmanager/src/Manager.kt` |
| 163 | `proto.codex.thread_config.v1` | `config.src.threadconfig.proto.Codex.threadConfig.v1` | 0 | 22 | 17 | 39 | `config/src/thread_config/proto/codex.thread_config.v1.rs` | `config/src/threadconfig/proto/Codex.threadConfig.v1.kt` |
| 164 | `request_processors.turn_processor` | `appserver.src.requestprocessors.TurnProcessor` | 0 | 38 | 1 | 39 | `app-server/src/request_processors/turn_processor.rs` | `appserver/src/requestprocessors/TurnProcessor.kt` |
| 165 | `runtime.logs` | `state.src.runtime.Logs` | 0 | 38 | 1 | 39 | `state/src/runtime/logs.rs` | `state/src/runtime/Logs.kt` |
| 166 | `status.tests` | `tui.src.status.Tests` | 0 | 39 | 0 | 39 | `tui/src/status/tests.rs` | `tui/src/status/Tests.kt` |
| 167 | `tests.review_mode` | `tui.src.chatwidget.tests.ReviewMode` | 0 | 39 | 0 | 39 | `tui/src/chatwidget/tests/review_mode.rs` | `tui/src/chatwidget/tests/ReviewMode.kt` |
| 168 | `config.config_toml` | `config.src.ConfigToml` | 23 | 20 | 18 | 38 | `config/src/config_toml.rs` | `config/src/ConfigToml.kt` |
| 169 | `rmcp-client.stdio_server_launcher` | `rmcpclient.src.StdioServerLauncher` | 3 | 26 | 12 | 38 | `rmcp-client/src/stdio_server_launcher.rs` | `rmcpclient/src/StdioServerLauncher.kt` |
| 170 | `tui.branch_summary` | `tui.src.BranchSummary` | 1 | 28 | 10 | 38 | `tui/src/branch_summary.rs` | `tui/src/BranchSummary.kt` |
| 171 | `core-plugins.remote_bundle` | `coreplugins.src.RemoteBundle` | 0 | 36 | 2 | 38 | `core-plugins/src/remote_bundle.rs` | `coreplugins/src/RemoteBundle.kt` |
| 172 | `suite.approvals` | `core.tests.suite.Approvals` | 0 | 31 | 7 | 38 | `core/tests/suite/approvals.rs` | `core/tests/suite/Approvals.kt` |
| 173 | `suite.compact` | `core.tests.suite.Compact` | 0 | 38 | 0 | 38 | `core/tests/suite/compact.rs` | `core/tests/suite/Compact.kt` |
| 174 | `v2.plugin_list` | `appserver.tests.suite.v2.PluginList` | 0 | 38 | 0 | 38 | `app-server/tests/suite/v2/plugin_list.rs` | `appserver/tests/suite/v2/PluginList.kt` |
| 175 | `cloud-tasks.app` | `cloudtasks.src.App` | 0 | 26 | 11 | 37 | `cloud-tasks/src/app.rs` | `cloudtasks/src/App.kt` |
| 176 | `common.streaming_sse` | `core.tests.common.StreamingSse` | 0 | 34 | 3 | 37 | `core/tests/common/streaming_sse.rs` | `core/tests/common/StreamingSse.kt` |
| 177 | `config.permissions` | `core.src.config.Permissions` | 0 | 37 | 0 | 37 | `core/src/config/permissions.rs` | `core/src/config/Permissions.kt` |
| 178 | `handlers.agent_jobs` | `core.src.tools.handlers.AgentJobs` | 0 | 28 | 9 | 37 | `core/src/tools/handlers/agent_jobs.rs` | `core/src/tools/handlers/AgentJobs.kt` |
| 179 | `sandboxing.seatbelt_tests` | `sandboxing.src.SeatbeltTests` | 0 | 35 | 2 | 37 | `sandboxing/src/seatbelt_tests.rs` | `sandboxing/src/SeatbeltTests.kt` |
| 180 | `tui.model_migration` | `tui.src.ModelMigration` | 0 | 32 | 5 | 37 | `tui/src/model_migration.rs` | `tui/src/ModelMigration.kt` |
| 181 | `tui.event_stream` | `tui.src.tui.EventStream` | 1 | 26 | 10 | 36 | `tui/src/tui/event_stream.rs` | `tui/src/tui/EventStream.kt` |
| 182 | `analytics.facts` | `analytics.src.Facts` | 0 | 3 | 33 | 36 | `analytics/src/facts.rs` | `analytics/src/Facts.kt` |
| 183 | `tests.basic` | `execpolicy.tests.Basic` | 0 | 35 | 1 | 36 | `execpolicy/tests/basic.rs` | `execpolicy/tests/Basic.kt` |
| 184 | `unified_exec.process_manager` | `core.src.unifiedexec.ProcessManager` | 0 | 34 | 2 | 36 | `core/src/unified_exec/process_manager.rs` | `core/src/unifiedexec/ProcessManager.kt` |
| 185 | `v2.connection_handling_websocket` | `appserver.tests.suite.v2.ConnectionHandlingWebsocket` | 0 | 34 | 2 | 36 | `app-server/tests/suite/v2/connection_handling_websocket.rs` | `appserver/tests/suite/v2/ConnectionHandlingWebsocket.kt` |
| 186 | `v2.turn_start` | `appserver.tests.suite.v2.TurnStart` | 0 | 35 | 1 | 36 | `app-server/tests/suite/v2/turn_start.rs` | `appserver/tests/suite/v2/TurnStart.kt` |
| 187 | `codex-client.custom_ca` | `codexclient.src.CustomCa` | 0 | 28 | 7 | 35 | `codex-client/src/custom_ca.rs` | `codexclient/src/CustomCa.kt` |
| 188 | `onboarding.onboarding_screen` | `tui.src.onboarding.OnboardingScreen` | 0 | 27 | 8 | 35 | `tui/src/onboarding/onboarding_screen.rs` | `tui/src/onboarding/OnboardingScreen.kt` |
| 189 | `request_processors.thread_processor_tests` | `appserver.src.requestprocessors.ThreadProcessorTests` | 0 | 35 | 0 | 35 | `app-server/src/request_processors/thread_processor_tests.rs` | `appserver/src/requestprocessors/ThreadProcessorTests.kt` |
| 190 | `tui.markdown_stream` | `tui.src.MarkdownStream` | 0 | 34 | 1 | 35 | `tui/src/markdown_stream.rs` | `tui/src/MarkdownStream.kt` |
| 191 | `code-mode.service` | `codemode.src.Service` | 6 | 26 | 8 | 34 | `code-mode/src/service.rs` | `codemode/src/Service.kt` |
| 192 | `auth.storage` | `login.src.auth.Storage` | 0 | 27 | 7 | 34 | `login/src/auth/storage.rs` | `login/src/auth/Storage.kt` |
| 193 | `bin.test_stdio_server` | `rmcpclient.src.bin.TestStdioServer` | 0 | 27 | 7 | 34 | `rmcp-client/src/bin/test_stdio_server.rs` | `rmcpclient/src/bin/TestStdioServer.kt` |
| 194 | `exec_cell.render` | `tui.src.execcell.Render` | 0 | 30 | 4 | 34 | `tui/src/exec_cell/render.rs` | `tui/src/execcell/Render.kt` |
| 195 | `suite.landlock` | `linuxsandbox.tests.suite.Landlock` | 0 | 34 | 0 | 34 | `linux-sandbox/tests/suite/landlock.rs` | `linuxsandbox/tests/suite/Landlock.kt` |
| 196 | `tests.file_system` | `execserver.tests.FileSystem` | 0 | 33 | 1 | 34 | `exec-server/tests/file_system.rs` | `execserver/tests/FileSystem.kt` |
| 197 | `app.side` | `tui.src.app.Side` | 0 | 30 | 3 | 33 | `tui/src/app/side.rs` | `tui/src/app/Side.kt` |
| 198 | `endpoint.responses_websocket` | `codexapi.src.endpoint.ResponsesWebsocket` | 0 | 27 | 6 | 33 | `codex-api/src/endpoint/responses_websocket.rs` | `codexapi/src/endpoint/ResponsesWebsocket.kt` |
| 199 | `linux-sandbox.linux_run_main_tests` | `linuxsandbox.src.LinuxRunMainTests` | 0 | 33 | 0 | 33 | `linux-sandbox/src/linux_run_main_tests.rs` | `linuxsandbox/src/LinuxRunMainTests.kt` |
| 200 | `runtime.goals` | `state.src.runtime.Goals` | 0 | 30 | 3 | 33 | `state/src/runtime/goals.rs` | `state/src/runtime/Goals.kt` |
| 201 | `protocol.error` | `protocol.src.Error` | 105 | 23 | 9 | 32 | `protocol/src/error.rs` | `protocol/src/Error.kt` |
| 202 | `local.read_thread` | `threadstore.src.local.ReadThread` | 1 | 32 | 0 | 32 | `thread-store/src/local/read_thread.rs` | `threadstore/src/local/ReadThread.kt` |
| 203 | `tui.text_formatting` | `tui.src.TextFormatting` | 1 | 31 | 1 | 32 | `tui/src/text_formatting.rs` | `tui/src/TextFormatting.kt` |
| 204 | `app-server.command_exec` | `appserver.src.CommandExec` | 0 | 22 | 10 | 32 | `app-server/src/command_exec.rs` | `appserver/src/CommandExec.kt` |
| 205 | `cli.debug_sandbox` | `cli.src.debugsandbox.DebugSandbox` | 0 | 28 | 4 | 32 | `cli/src/debug_sandbox.rs` | `cli/src/debugsandbox/DebugSandbox.kt` |
| 206 | `session.handlers` | `core.src.session.Handlers` | 0 | 32 | 0 | 32 | `core/src/session/handlers.rs` | `core/src/session/Handlers.kt` |
| 207 | `suite.request_permissions` | `core.tests.suite.RequestPermissions` | 0 | 31 | 1 | 32 | `core/tests/suite/request_permissions.rs` | `core/tests/suite/RequestPermissions.kt` |
| 208 | `tests.clients` | `codexapi.tests.Clients` | 0 | 26 | 6 | 32 | `codex-api/tests/clients.rs` | `codexapi/tests/Clients.kt` |
| 209 | `tui.clipboard_copy` | `tui.src.ClipboardCopy` | 0 | 30 | 2 | 32 | `tui/src/clipboard_copy.rs` | `tui/src/ClipboardCopy.kt` |
| 210 | `tui.debug_config` | `tui.src.DebugConfig` | 0 | 32 | 0 | 32 | `tui/src/debug_config.rs` | `tui/src/DebugConfig.kt` |
| 211 | `v2.account` | `appserver.tests.suite.v2.Account` | 0 | 31 | 1 | 32 | `app-server/tests/suite/v2/account.rs` | `appserver/tests/suite/v2/Account.kt` |
| 212 | `v2.thread_list` | `appserver.tests.suite.v2.ThreadList` | 0 | 32 | 0 | 32 | `app-server/tests/suite/v2/thread_list.rs` | `appserver/tests/suite/v2/ThreadList.kt` |
| 213 | `network-proxy.http_proxy` | `networkproxy.src.HttpProxy` | 1 | 30 | 1 | 31 | `network-proxy/src/http_proxy.rs` | `networkproxy/src/HttpProxy.kt` |
| 214 | `analytics.client` | `analytics.src.Client` | 0 | 29 | 2 | 31 | `analytics/src/client.rs` | `analytics/src/Client.kt` |
| 215 | `app-server.message_processor_tracing_tests` | `appserver.src.MessageProcessorTracingTests` | 0 | 28 | 3 | 31 | `app-server/src/message_processor_tracing_tests.rs` | `appserver/src/MessageProcessorTracingTests.kt` |
| 216 | `exec-server.fs_sandbox` | `execserver.src.FsSandbox` | 0 | 30 | 1 | 31 | `exec-server/src/fs_sandbox.rs` | `execserver/src/FsSandbox.kt` |
| 217 | `git-utils.apply` | `gitutils.src.Apply` | 0 | 29 | 2 | 31 | `git-utils/src/apply.rs` | `gitutils/src/Apply.kt` |
| 218 | `reducer.code_cell` | `rollouttrace.src.reducer.CodeCell` | 0 | 27 | 4 | 31 | `rollout-trace/src/reducer/code_cell.rs` | `rollouttrace/src/reducer/CodeCell.kt` |
| 219 | `remote_control.tests` | `appservertransport.src.transport.remotecontrol.Tests` | 0 | 28 | 3 | 31 | `app-server-transport/src/transport/remote_control/tests.rs` | `appservertransport/src/transport/remotecontrol/Tests.kt` |
| 220 | `tests.composer_submission` | `tui.src.chatwidget.tests.ComposerSubmission` | 0 | 31 | 0 | 31 | `tui/src/chatwidget/tests/composer_submission.rs` | `tui/src/chatwidget/tests/ComposerSubmission.kt` |
| 221 | `tools.agent_tool` | `tools.src.AgentTool` | 0 | 29 | 2 | 31 | `tools/src/agent_tool.rs` | `tools/src/AgentTool.kt` |
| 222 | `tui.terminal_probe` | `tui.src.TerminalProbe` | 0 | 28 | 3 | 31 | `tui/src/terminal_probe.rs` | `tui/src/TerminalProbe.kt` |
| 223 | `unified_exec.process` | `core.src.unifiedexec.Process` | 0 | 24 | 7 | 31 | `core/src/unified_exec/process.rs` | `core/src/unifiedexec/Process.kt` |
| 224 | `session.turn_context` | `core.src.session.TurnContext` | 51 | 27 | 3 | 30 | `core/src/session/turn_context.rs` | `core/src/session/TurnContext.kt` |
| 225 | `config.constraint` | `config.src.Constraint` | 15 | 24 | 6 | 30 | `config/src/constraint.rs` | `config/src/Constraint.kt` |
| 226 | `engine.output_parser` | `hooks.src.engine.OutputParser` | 6 | 22 | 8 | 30 | `hooks/src/engine/output_parser.rs` | `hooks/src/engine/OutputParser.kt` |
| 227 | `app-server.in_process` | `appserver.src.InProcess` | 3 | 23 | 7 | 30 | `app-server/src/in_process.rs` | `appserver/src/InProcess.kt` |
| 228 | `app-server.thread_state` | `appserver.src.ThreadState` | 2 | 22 | 8 | 30 | `app-server/src/thread_state.rs` | `appserver/src/ThreadState.kt` |
| 229 | `tests.event_processor_with_json_output` | `exec.tests.EventProcessorWithJsonOutput` | 1 | 30 | 0 | 30 | `exec/tests/event_processor_with_json_output.rs` | `exec/tests/EventProcessorWithJsonOutput.kt` |
| 230 | `tui.tooltips` | `tui.src.Tooltips` | 1 | 26 | 4 | 30 | `tui/src/tooltips.rs` | `tui/src/Tooltips.kt` |
| 231 | `chat_composer.history_search` | `tui.src.bottompane.chatcomposer.HistorySearch` | 0 | 28 | 2 | 30 | `tui/src/bottom_pane/chat_composer/history_search.rs` | `tui/src/bottompane/chatcomposer/HistorySearch.kt` |
| 232 | `codex-mcp.connection_manager` | `codexmcp.src.ConnectionManager` | 0 | 29 | 1 | 30 | `codex-mcp/src/connection_manager.rs` | `codexmcp/src/ConnectionManager.kt` |
| 233 | `core-plugins.startup_sync_tests` | `coreplugins.src.StartupSyncTests` | 0 | 30 | 0 | 30 | `core-plugins/src/startup_sync_tests.rs` | `coreplugins/src/StartupSyncTests.kt` |
| 234 | `core.hook_runtime` | `core.src.HookRuntime` | 0 | 26 | 4 | 30 | `core/src/hook_runtime.rs` | `core/src/HookRuntime.kt` |
| 235 | `endpoint.realtime_call` | `codexapi.src.endpoint.RealtimeCall` | 0 | 25 | 5 | 30 | `codex-api/src/endpoint/realtime_call.rs` | `codexapi/src/endpoint/RealtimeCall.kt` |
| 236 | `execpolicy.parser` | `execpolicy.src.Parser` | 0 | 27 | 3 | 30 | `execpolicy/src/parser.rs` | `execpolicy/src/Parser.kt` |
| 237 | `protocol.error_tests` | `protocol.src.ErrorTests` | 0 | 30 | 0 | 30 | `protocol/src/error_tests.rs` | `protocol/src/ErrorTests.kt` |
| 238 | `pty.tests` | `utils.pty.src.Tests` | 0 | 30 | 0 | 30 | `utils/pty/src/tests.rs` | `utils/pty/src/Tests.kt` |
| 239 | `tests.ca_env` | `codexclient.tests.CaEnv` | 0 | 26 | 4 | 30 | `codex-client/tests/ca_env.rs` | `codexclient/tests/CaEnv.kt` |
| 240 | `core.connectors` | `core.src.Connectors` | 10 | 25 | 4 | 29 | `core/src/connectors.rs` | `core/src/Connectors.kt` |
| 241 | `tui.status_indicator_widget` | `tui.src.StatusIndicatorWidget` | 1 | 27 | 2 | 29 | `tui/src/status_indicator_widget.rs` | `tui/src/StatusIndicatorWidget.kt` |
| 242 | `app-server.config_manager_service` | `appserver.src.ConfigManagerService` | 0 | 27 | 2 | 29 | `app-server/src/config_manager_service.rs` | `appserver/src/ConfigManagerService.kt` |
| 243 | `bottom_pane.selection_popup_common` | `tui.src.bottompane.SelectionPopupCommon` | 0 | 26 | 3 | 29 | `tui/src/bottom_pane/selection_popup_common.rs` | `tui/src/bottompane/SelectionPopupCommon.kt` |
| 244 | `config.diagnostics` | `config.src.Diagnostics` | 0 | 24 | 5 | 29 | `config/src/diagnostics.rs` | `config/src/Diagnostics.kt` |
| 245 | `core-plugins.marketplace_tests` | `coreplugins.src.MarketplaceTests` | 0 | 29 | 0 | 29 | `core-plugins/src/marketplace_tests.rs` | `coreplugins/src/MarketplaceTests.kt` |
| 246 | `core.windows_sandbox` | `core.src.WindowsSandbox` | 0 | 26 | 3 | 29 | `core/src/windows_sandbox.rs` | `core/src/WindowsSandbox.kt` |
| 247 | `marketplace_add.source` | `coreplugins.src.marketplaceadd.Source` | 0 | 28 | 1 | 29 | `core-plugins/src/marketplace_add/source.rs` | `coreplugins/src/marketplaceadd/Source.kt` |
| 248 | `remote.helpers` | `threadstore.src.remote.Helpers` | 0 | 29 | 0 | 29 | `thread-store/src/remote/helpers.rs` | `threadstore/src/remote/Helpers.kt` |
| 249 | `request_processors.config_processor` | `appserver.src.requestprocessors.ConfigProcessor` | 0 | 28 | 1 | 29 | `app-server/src/request_processors/config_processor.rs` | `appserver/src/requestprocessors/ConfigProcessor.kt` |
| 250 | `tui.clipboard_paste` | `tui.src.ClipboardPaste` | 0 | 26 | 3 | 29 | `tui/src/clipboard_paste.rs` | `tui/src/ClipboardPaste.kt` |
| 251 | `tui.theme_picker` | `tui.src.ThemePicker` | 0 | 25 | 4 | 29 | `tui/src/theme_picker.rs` | `tui/src/ThemePicker.kt` |
| 252 | `v2.app_list` | `appserver.tests.suite.v2.AppList` | 0 | 26 | 3 | 29 | `app-server/tests/suite/v2/app_list.rs` | `appserver/tests/suite/v2/AppList.kt` |
| 253 | `rollout-trace.thread` | `rollouttrace.src.Thread` | 29 | 23 | 5 | 28 | `rollout-trace/src/thread.rs` | `rollouttrace/src/Thread.kt` |
| 254 | `config.state` | `config.src.State` | 8 | 24 | 4 | 28 | `config/src/state.rs` | `config/src/State.kt` |
| 255 | `bottom_pane.memories_settings_view` | `tui.src.bottompane.MemoriesSettingsView` | 2 | 24 | 4 | 28 | `tui/src/bottom_pane/memories_settings_view.rs` | `tui/src/bottompane/MemoriesSettingsView.kt` |
| 256 | `bottom_pane.paste_burst` | `tui.src.bottompane.PasteBurst` | 1 | 24 | 4 | 28 | `tui/src/bottom_pane/paste_burst.rs` | `tui/src/bottompane/PasteBurst.kt` |
| 257 | `network-proxy.mitm` | `networkproxy.src.Mitm` | 1 | 19 | 9 | 28 | `network-proxy/src/mitm.rs` | `networkproxy/src/Mitm.kt` |
| 258 | `app.config_persistence` | `tui.src.app.ConfigPersistence` | 0 | 28 | 0 | 28 | `tui/src/app/config_persistence.rs` | `tui/src/app/ConfigPersistence.kt` |
| 259 | `suite.auth_refresh` | `login.tests.suite.AuthRefresh` | 0 | 25 | 3 | 28 | `login/tests/suite/auth_refresh.rs` | `login/tests/suite/AuthRefresh.kt` |
| 260 | `suite.otel` | `core.tests.suite.Otel` | 0 | 28 | 0 | 28 | `core/tests/suite/otel.rs` | `core/tests/suite/Otel.kt` |
| 261 | `tools.tool_config` | `tools.src.ToolConfig` | 0 | 21 | 7 | 28 | `tools/src/tool_config.rs` | `tools/src/ToolConfig.kt` |
| 262 | `tui.keyboard_modes` | `tui.src.tui.KeyboardModes` | 0 | 26 | 2 | 28 | `tui/src/tui/keyboard_modes.rs` | `tui/src/tui/KeyboardModes.kt` |
| 263 | `tui.transcript_reflow` | `tui.src.TranscriptReflow` | 0 | 26 | 2 | 28 | `tui/src/transcript_reflow.rs` | `tui/src/TranscriptReflow.kt` |
| 264 | `v2.command_exec` | `appserver.tests.suite.v2.CommandExec` | 0 | 26 | 2 | 28 | `app-server/tests/suite/v2/command_exec.rs` | `appserver/tests/suite/v2/CommandExec.kt` |
| 265 | `v2.plugin_read` | `appserver.tests.suite.v2.PluginRead` | 0 | 26 | 2 | 28 | `app-server/tests/suite/v2/plugin_read.rs` | `appserver/tests/suite/v2/PluginRead.kt` |
| 266 | `windows-sandbox-rs.helper_materialization` | `windowssandboxrs.src.HelperMaterialization` | 0 | 26 | 2 | 28 | `windows-sandbox-rs/src/helper_materialization.rs` | `windowssandboxrs/src/HelperMaterialization.kt` |
| 267 | `protocol.agent_path` | `protocol.src.AgentPath` | 20 | 23 | 4 | 27 | `protocol/src/agent_path.rs` | `protocol/src/AgentPath.kt` |
| 268 | `config.network_proxy_spec` | `core.src.config.NetworkProxySpec` | 3 | 24 | 3 | 27 | `core/src/config/network_proxy_spec.rs` | `core/src/config/NetworkProxySpec.kt` |
| 269 | `pty.process` | `utils.pty.src.Process` | 2 | 17 | 10 | 27 | `utils/pty/src/process.rs` | `utils/pty/src/Process.kt` |
| 270 | `bottom_pane.command_popup` | `tui.src.bottompane.CommandPopup` | 1 | 24 | 3 | 27 | `tui/src/bottom_pane/command_popup.rs` | `tui/src/bottompane/CommandPopup.kt` |
| 271 | `write.phase1` | `memories.write.src.Phase1` | 1 | 23 | 4 | 27 | `memories/write/src/phase1.rs` | `memories/write/src/Phase1.kt` |
| 272 | `core-skills.injection_tests` | `coreskills.src.InjectionTests` | 0 | 27 | 0 | 27 | `core-skills/src/injection_tests.rs` | `coreskills/src/InjectionTests.kt` |
| 273 | `core.agents_md_tests` | `core.src.AgentsMdTests` | 0 | 27 | 0 | 27 | `core/src/agents_md_tests.rs` | `core/src/AgentsMdTests.kt` |
| 274 | `core.thread_manager_tests` | `core.src.ThreadManagerTests` | 0 | 27 | 0 | 27 | `core/src/thread_manager_tests.rs` | `core/src/ThreadManagerTests.kt` |
| 275 | `mcp.local_tests` | `memories.mcp.src.LocalTests` | 0 | 27 | 0 | 27 | `memories/mcp/src/local_tests.rs` | `memories/mcp/src/LocalTests.kt` |
| 276 | `realtime_websocket.protocol` | `codexapi.src.endpoint.realtimewebsocket.Protocol` | 0 | 1 | 26 | 27 | `codex-api/src/endpoint/realtime_websocket/protocol.rs` | `codexapi/src/endpoint/realtimewebsocket/Protocol.kt` |
| 277 | `rollout-trace.tool_dispatch` | `rollouttrace.src.ToolDispatch` | 0 | 18 | 9 | 27 | `rollout-trace/src/tool_dispatch.rs` | `rollouttrace/src/ToolDispatch.kt` |
| 278 | `runtimes.mod_tests` | `core.src.tools.runtimes.ModTests` | 0 | 26 | 1 | 27 | `core/src/tools/runtimes/mod_tests.rs` | `core/src/tools/runtimes/ModTests.kt` |
| 279 | `terminal-detection.terminal_tests` | `terminaldetection.src.TerminalTests` | 0 | 26 | 1 | 27 | `terminal-detection/src/terminal_tests.rs` | `terminaldetection/src/TerminalTests.kt` |
| 280 | `tui.insert_history` | `tui.src.InsertHistory` | 0 | 23 | 4 | 27 | `tui/src/insert_history.rs` | `tui/src/InsertHistory.kt` |
| 281 | `git-utils.info` | `gitutils.src.Info` | 45 | 23 | 3 | 26 | `git-utils/src/info.rs` | `gitutils/src/Info.kt` |
| 282 | `model.thread_metadata` | `state.src.model.ThreadMetadata` | 9 | 16 | 10 | 26 | `state/src/model/thread_metadata.rs` | `state/src/model/ThreadMetadata.kt` |
| 283 | `tui.app_command` | `tui.src.AppCommand` | 9 | 25 | 1 | 26 | `tui/src/app_command.rs` | `tui/src/AppCommand.kt` |
| 284 | `tui.app` | `tui.src.app.App` | 8 | 16 | 10 | 26 | `tui/src/app.rs` | `tui/src/app/App.kt` |
| 285 | `common.context_snapshot` | `core.tests.common.ContextSnapshot` | 7 | 24 | 2 | 26 | `core/tests/common/context_snapshot.rs` | `core/tests/common/ContextSnapshot.kt` |
| 286 | `secrets.local` | `secrets.src.Local` | 7 | 24 | 2 | 26 | `secrets/src/local.rs` | `secrets/src/Local.kt` |
| 287 | `tests.exec_process` | `execserver.tests.ExecProcess` | 7 | 24 | 2 | 26 | `exec-server/tests/exec_process.rs` | `execserver/tests/ExecProcess.kt` |
| 288 | `mcp-server.message_processor` | `mcpserver.src.MessageProcessor` | 4 | 25 | 1 | 26 | `mcp-server/src/message_processor.rs` | `mcpserver/src/MessageProcessor.kt` |
| 289 | `shell.unix_escalation` | `core.src.tools.runtimes.shell.UnixEscalation` | 1 | 17 | 9 | 26 | `core/src/tools/runtimes/shell/unix_escalation.rs` | `core/src/tools/runtimes/shell/UnixEscalation.kt` |
| 290 | `engine.discovery` | `hooks.src.engine.Discovery` | 0 | 24 | 2 | 26 | `hooks/src/engine/discovery.rs` | `hooks/src/engine/Discovery.kt` |
| 291 | `remote.share` | `coreplugins.src.remote.share.Share` | 0 | 19 | 7 | 26 | `core-plugins/src/remote/share.rs` | `coreplugins/src/remote/share/Share.kt` |
| 292 | `sandboxing.policy_transforms_tests` | `sandboxing.src.PolicyTransformsTests` | 0 | 26 | 0 | 26 | `sandboxing/src/policy_transforms_tests.rs` | `sandboxing/src/PolicyTransformsTests.kt` |
| 293 | `tool.agents` | `rollouttrace.src.reducer.tool.Agents` | 0 | 24 | 2 | 26 | `rollout-trace/src/reducer/tool/agents.rs` | `rollouttrace/src/reducer/tool/Agents.kt` |
| 294 | `tools.tool_discovery` | `tools.src.ToolDiscovery` | 0 | 18 | 8 | 26 | `tools/src/tool_discovery.rs` | `tools/src/ToolDiscovery.kt` |
| 295 | `tui.voice` | `tui.src.Voice` | 0 | 23 | 3 | 26 | `tui/src/voice.rs` | `tui/src/Voice.kt` |
| 296 | `config.managed_features` | `core.src.config.ManagedFeatures` | 4 | 23 | 2 | 25 | `core/src/config/managed_features.rs` | `core/src/config/ManagedFeatures.kt` |
| 297 | `app-server-client.remote` | `appserverclient.src.Remote` | 0 | 21 | 4 | 25 | `app-server-client/src/remote.rs` | `appserverclient/src/Remote.kt` |
| 298 | `app.resize_reflow` | `tui.src.app.ResizeReflow` | 0 | 23 | 2 | 25 | `tui/src/app/resize_reflow.rs` | `tui/src/app/ResizeReflow.kt` |
| 299 | `app.session_lifecycle` | `tui.src.app.SessionLifecycle` | 0 | 25 | 0 | 25 | `tui/src/app/session_lifecycle.rs` | `tui/src/app/SessionLifecycle.kt` |
| 300 | `bottom_pane.title_setup` | `tui.src.bottompane.TitleSetup` | 0 | 23 | 2 | 25 | `tui/src/bottom_pane/title_setup.rs` | `tui/src/bottompane/TitleSetup.kt` |
| 301 | `core-plugins.manifest` | `coreplugins.src.Manifest` | 0 | 16 | 9 | 25 | `core-plugins/src/manifest.rs` | `coreplugins/src/Manifest.kt` |
| 302 | `core-plugins.store` | `coreplugins.src.Store` | 0 | 21 | 4 | 25 | `core-plugins/src/store.rs` | `coreplugins/src/Store.kt` |
| 303 | `events.common` | `hooks.src.events.Common` | 0 | 25 | 0 | 25 | `hooks/src/events/common.rs` | `hooks/src/events/Common.kt` |
| 304 | `exec.lib_tests` | `exec.src.LibTests` | 0 | 25 | 0 | 25 | `exec/src/lib_tests.rs` | `exec/src/LibTests.kt` |
| 305 | `reducer.conversation` | `rollouttrace.src.reducer.conversation.Conversation` | 0 | 21 | 4 | 25 | `rollout-trace/src/reducer/conversation.rs` | `rollouttrace/src/reducer/conversation/Conversation.kt` |
| 306 | `streaming.chunking` | `tui.src.streaming.Chunking` | 0 | 20 | 5 | 25 | `tui/src/streaming/chunking.rs` | `tui/src/streaming/Chunking.kt` |
| 307 | `v2.thread_start` | `appserver.tests.suite.v2.ThreadStart` | 0 | 25 | 0 | 25 | `app-server/tests/suite/v2/thread_start.rs` | `appserver/tests/suite/v2/ThreadStart.kt` |
| 308 | `tools.json_schema` | `tools.src.JsonSchema` | 75 | 20 | 4 | 24 | `tools/src/json_schema.rs` | `tools/src/JsonSchema.kt` |
| 309 | `app-server.config_manager` | `appserver.src.ConfigManager` | 8 | 23 | 1 | 24 | `app-server/src/config_manager.rs` | `appserver/src/ConfigManager.kt` |
| 310 | `bottom_pane.skill_popup` | `tui.src.bottompane.SkillPopup` | 1 | 22 | 2 | 24 | `tui/src/bottom_pane/skill_popup.rs` | `tui/src/bottompane/SkillPopup.kt` |
| 311 | `agent.registry` | `core.src.agent.Registry` | 0 | 20 | 4 | 24 | `core/src/agent/registry.rs` | `core/src/agent/Registry.kt` |
| 312 | `agent.role_tests` | `core.src.agent.RoleTests` | 0 | 24 | 0 | 24 | `core/src/agent/role_tests.rs` | `core/src/agent/RoleTests.kt` |
| 313 | `app.app_server_requests` | `tui.src.app.AppServerRequests` | 0 | 18 | 6 | 24 | `tui/src/app/app_server_requests.rs` | `tui/src/app/AppServerRequests.kt` |
| 314 | `bottom_pane.status_line_setup` | `tui.src.bottompane.StatusLineSetup` | 0 | 22 | 2 | 24 | `tui/src/bottom_pane/status_line_setup.rs` | `tui/src/bottompane/StatusLineSetup.kt` |
| 315 | `core-skills.manager_tests` | `coreskills.src.ManagerTests` | 0 | 24 | 0 | 24 | `core-skills/src/manager_tests.rs` | `coreskills/src/ManagerTests.kt` |
| 316 | `core.git_info_tests` | `core.src.GitInfoTests` | 0 | 24 | 0 | 24 | `core/src/git_info_tests.rs` | `core/src/GitInfoTests.kt` |
| 317 | `debug-client.client` | `debugclient.src.Client` | 0 | 23 | 1 | 24 | `debug-client/src/client.rs` | `debugclient/src/Client.kt` |
| 318 | `exec.cli` | `exec.src.Cli` | 0 | 16 | 8 | 24 | `exec/src/cli.rs` | `exec/src/Cli.kt` |
| 319 | `metrics.client` | `otel.src.metrics.Client` | 0 | 21 | 3 | 24 | `otel/src/metrics/client.rs` | `otel/src/metrics/Client.kt` |
| 320 | `request_processors.device_key_processor` | `appserver.src.requestprocessors.DeviceKeyProcessor` | 0 | 22 | 2 | 24 | `app-server/src/request_processors/device_key_processor.rs` | `appserver/src/requestprocessors/DeviceKeyProcessor.kt` |
| 321 | `rollout.tests` | `rollout.src.Tests` | 0 | 24 | 0 | 24 | `rollout/src/tests.rs` | `rollout/src/Tests.kt` |
| 322 | `server.handler` | `execserver.src.server.handler.Handler` | 0 | 23 | 1 | 24 | `exec-server/src/server/handler.rs` | `execserver/src/server/handler/Handler.kt` |
| 323 | `thread-store.in_memory` | `threadstore.src.InMemory` | 0 | 21 | 3 | 24 | `thread-store/src/in_memory.rs` | `threadstore/src/InMemory.kt` |
| 324 | `thread_config.remote` | `config.src.threadconfig.Remote` | 0 | 22 | 2 | 24 | `config/src/thread_config/remote.rs` | `config/src/threadconfig/Remote.kt` |
| 325 | `windows-sandbox-rs.wfp` | `windowssandboxrs.src.Wfp` | 0 | 21 | 3 | 24 | `windows-sandbox-rs/src/wfp.rs` | `windowssandboxrs/src/Wfp.kt` |
| 326 | `codex-mcp.rmcp_client` | `codexmcp.src.RmcpClient` | 4 | 19 | 4 | 23 | `codex-mcp/src/rmcp_client.rs` | `codexmcp/src/RmcpClient.kt` |
| 327 | `server.session_registry` | `execserver.src.server.SessionRegistry` | 3 | 17 | 6 | 23 | `exec-server/src/server/session_registry.rs` | `execserver/src/server/SessionRegistry.kt` |
| 328 | `core-skills.injection` | `coreskills.src.Injection` | 2 | 18 | 5 | 23 | `core-skills/src/injection.rs` | `coreskills/src/Injection.kt` |
| 329 | `protocol.v1` | `appserverprotocol.src.protocol.V1` | 2 | 0 | 23 | 23 | `app-server-protocol/src/protocol/v1.rs` | `appserverprotocol/src/protocol/V1.kt` |
| 330 | `auth.storage_tests` | `login.src.auth.StorageTests` | 0 | 22 | 1 | 23 | `login/src/auth/storage_tests.rs` | `login/src/auth/StorageTests.kt` |
| 331 | `bin.logs_client` | `state.src.bin.LogsClient` | 0 | 20 | 3 | 23 | `state/src/bin/logs_client.rs` | `state/src/bin/LogsClient.kt` |
| 332 | `core.network_proxy_loader` | `core.src.NetworkProxyLoader` | 0 | 20 | 3 | 23 | `core/src/network_proxy_loader.rs` | `core/src/NetworkProxyLoader.kt` |
| 333 | `core.shell_snapshot_tests` | `core.src.ShellSnapshotTests` | 0 | 22 | 1 | 23 | `core/src/shell_snapshot_tests.rs` | `core/src/ShellSnapshotTests.kt` |
| 334 | `exec.event_processor_with_jsonl_output` | `exec.src.EventProcessorWithJsonlOutput` | 0 | 20 | 3 | 23 | `exec/src/event_processor_with_jsonl_output.rs` | `exec/src/EventProcessorWithJsonlOutput.kt` |
| 335 | `rollout-trace.inference` | `rollouttrace.src.Inference` | 0 | 16 | 7 | 23 | `rollout-trace/src/inference.rs` | `rollouttrace/src/Inference.kt` |
| 336 | `suite.compact_resume_fork` | `core.tests.suite.CompactResumeFork` | 0 | 23 | 0 | 23 | `core/tests/suite/compact_resume_fork.rs` | `core/tests/suite/CompactResumeFork.kt` |
| 337 | `v2.thread_read` | `appserver.tests.suite.v2.ThreadRead` | 0 | 22 | 1 | 23 | `app-server/tests/suite/v2/thread_read.rs` | `appserver/tests/suite/v2/ThreadRead.kt` |
| 338 | `mcp-server.outgoing_message` | `mcpserver.src.OutgoingMessage` | 15 | 13 | 9 | 22 | `mcp-server/src/outgoing_message.rs` | `mcpserver/src/OutgoingMessage.kt` |
| 339 | `otel.provider` | `otel.src.Provider` | 15 | 20 | 2 | 22 | `otel/src/provider.rs` | `otel/src/Provider.kt` |
| 340 | `guardian.prompt` | `core.src.guardian.Prompt` | 11 | 14 | 8 | 22 | `core/src/guardian/prompt.rs` | `core/src/guardian/Prompt.kt` |
| 341 | `tests.http_client` | `execserver.tests.HttpClient` | 9 | 20 | 2 | 22 | `exec-server/tests/http_client.rs` | `execserver/tests/HttpClient.kt` |
| 342 | `config.permissions_toml` | `config.src.PermissionsToml` | 5 | 12 | 10 | 22 | `config/src/permissions_toml.rs` | `config/src/PermissionsToml.kt` |
| 343 | `hooks.registry` | `hooks.src.Registry` | 1 | 19 | 3 | 22 | `hooks/src/registry.rs` | `hooks/src/Registry.kt` |
| 344 | `tui.cwd_prompt` | `tui.src.CwdPrompt` | 1 | 18 | 4 | 22 | `tui/src/cwd_prompt.rs` | `tui/src/CwdPrompt.kt` |
| 345 | `chatwidget.skills` | `tui.src.chatwidget.Skills` | 0 | 21 | 1 | 22 | `tui/src/chatwidget/skills.rs` | `tui/src/chatwidget/Skills.kt` |
| 346 | `cli.mcp_cmd` | `cli.src.McpCmd` | 0 | 11 | 11 | 22 | `cli/src/mcp_cmd.rs` | `cli/src/McpCmd.kt` |
| 347 | `cloud-tasks.ui` | `cloudtasks.src.Ui` | 0 | 21 | 1 | 22 | `cloud-tasks/src/ui.rs` | `cloudtasks/src/Ui.kt` |
| 348 | `codex-client.default_client` | `codexclient.src.DefaultClient` | 0 | 18 | 4 | 22 | `codex-client/src/default_client.rs` | `codexclient/src/DefaultClient.kt` |
| 349 | `config.mcp_types` | `config.src.McpTypes` | 0 | 14 | 8 | 22 | `config/src/mcp_types.rs` | `config/src/McpTypes.kt` |
| 350 | `config.mcp_types_tests` | `config.src.McpTypesTests` | 0 | 22 | 0 | 22 | `config/src/mcp_types_tests.rs` | `config/src/McpTypesTests.kt` |
| 351 | `config.thread_config` | `config.src.threadconfig.ThreadConfig` | 0 | 13 | 9 | 22 | `config/src/thread_config.rs` | `config/src/threadconfig/ThreadConfig.kt` |
| 352 | `core.client_tests` | `core.src.ClientTests` | 0 | 18 | 4 | 22 | `core/src/client_tests.rs` | `core/src/ClientTests.kt` |
| 353 | `events.pre_tool_use` | `hooks.src.events.PreToolUse` | 0 | 19 | 3 | 22 | `hooks/src/events/pre_tool_use.rs` | `hooks/src/events/PreToolUse.kt` |
| 354 | `guardian.review` | `core.src.guardian.Review` | 0 | 20 | 2 | 22 | `core/src/guardian/review.rs` | `core/src/guardian/Review.kt` |
| 355 | `request_processors.catalog_processor` | `appserver.src.requestprocessors.CatalogProcessor` | 0 | 21 | 1 | 22 | `app-server/src/request_processors/catalog_processor.rs` | `appserver/src/requestprocessors/CatalogProcessor.kt` |
| 356 | `request_processors.thread_lifecycle` | `appserver.src.requestprocessors.ThreadLifecycle` | 0 | 18 | 4 | 22 | `app-server/src/request_processors/thread_lifecycle.rs` | `appserver/src/requestprocessors/ThreadLifecycle.kt` |
| 357 | `session.rollout_reconstruction_tests` | `core.src.session.RolloutReconstructionTests` | 0 | 22 | 0 | 22 | `core/src/session/rollout_reconstruction_tests.rs` | `core/src/session/RolloutReconstructionTests.kt` |
| 358 | `suite.pending_input` | `core.tests.suite.PendingInput` | 0 | 22 | 0 | 22 | `core/tests/suite/pending_input.rs` | `core/tests/suite/PendingInput.kt` |
| 359 | `suite.shell_snapshot` | `core.tests.suite.ShellSnapshot` | 0 | 20 | 2 | 22 | `core/tests/suite/shell_snapshot.rs` | `core/tests/suite/ShellSnapshot.kt` |
| 360 | `tests.history_replay` | `tui.src.chatwidget.tests.HistoryReplay` | 0 | 22 | 0 | 22 | `tui/src/chatwidget/tests/history_replay.rs` | `tui/src/chatwidget/tests/HistoryReplay.kt` |
| 361 | `tests.permissions` | `tui.src.chatwidget.tests.Permissions` | 0 | 22 | 0 | 22 | `tui/src/chatwidget/tests/permissions.rs` | `tui/src/chatwidget/tests/Permissions.kt` |
| 362 | `tools.json_schema_tests` | `tools.src.JsonSchemaTests` | 0 | 22 | 0 | 22 | `tools/src/json_schema_tests.rs` | `tools/src/JsonSchemaTests.kt` |
| 363 | `v2.fs` | `appserver.tests.suite.v2.Fs` | 132 | 21 | 0 | 21 | `app-server/tests/suite/v2/fs.rs` | `appserver/tests/suite/v2/Fs.kt` |
| 364 | `bottom_pane.bottom_pane_view` | `tui.src.bottompane.BottomPaneView` | 17 | 19 | 2 | 21 | `tui/src/bottom_pane/bottom_pane_view.rs` | `tui/src/bottompane/BottomPaneView.kt` |
| 365 | `config.tui_keymap` | `config.src.TuiKeymap` | 11 | 9 | 12 | 21 | `config/src/tui_keymap.rs` | `config/src/TuiKeymap.kt` |
| 366 | `rollout.state_db` | `rollout.src.StateDb` | 6 | 20 | 1 | 21 | `rollout/src/state_db.rs` | `rollout/src/StateDb.kt` |
| 367 | `bottom_pane.skills_toggle_view` | `tui.src.bottompane.SkillsToggleView` | 2 | 19 | 2 | 21 | `tui/src/bottom_pane/skills_toggle_view.rs` | `tui/src/bottompane/SkillsToggleView.kt` |
| 368 | `bin.test_streamable_http_server` | `rmcpclient.src.bin.TestStreamableHttpServer` | 0 | 16 | 5 | 21 | `rmcp-client/src/bin/test_streamable_http_server.rs` | `rmcpclient/src/bin/TestStreamableHttpServer.kt` |
| 369 | `core.file_watcher_tests` | `core.src.FileWatcherTests` | 0 | 21 | 0 | 21 | `core/src/file_watcher_tests.rs` | `core/src/FileWatcherTests.kt` |
| 370 | `external-agent-sessions.records` | `externalagentsessions.src.Records` | 0 | 19 | 2 | 21 | `external-agent-sessions/src/records.rs` | `externalagentsessions/src/Records.kt` |
| 371 | `ide_context.windows_pipe` | `tui.src.idecontext.WindowsPipe` | 0 | 17 | 4 | 21 | `tui/src/ide_context/windows_pipe.rs` | `tui/src/idecontext/WindowsPipe.kt` |
| 372 | `responses-api-proxy.dump` | `responsesapiproxy.src.Dump` | 0 | 15 | 6 | 21 | `responses-api-proxy/src/dump.rs` | `responsesapiproxy/src/Dump.kt` |
| 373 | `sandboxing.policy_transforms` | `sandboxing.src.PolicyTransforms` | 0 | 20 | 1 | 21 | `sandboxing/src/policy_transforms.rs` | `sandboxing/src/PolicyTransforms.kt` |
| 374 | `shell.unix_escalation_tests` | `core.src.tools.runtimes.shell.UnixEscalationTests` | 0 | 21 | 0 | 21 | `core/src/tools/runtimes/shell/unix_escalation_tests.rs` | `core/src/tools/runtimes/shell/UnixEscalationTests.kt` |
| 375 | `status.card` | `tui.src.status.Card` | 0 | 16 | 5 | 21 | `tui/src/status/card.rs` | `tui/src/status/Card.kt` |
| 376 | `suite.remote_models` | `core.tests.suite.RemoteModels` | 0 | 21 | 0 | 21 | `core/tests/suite/remote_models.rs` | `core/tests/suite/RemoteModels.kt` |
| 377 | `suite.view_image` | `core.tests.suite.ViewImage` | 0 | 21 | 0 | 21 | `core/tests/suite/view_image.rs` | `core/tests/suite/ViewImage.kt` |
| 378 | `tui.update_prompt` | `tui.src.UpdatePrompt` | 0 | 18 | 3 | 21 | `tui/src/update_prompt.rs` | `tui/src/UpdatePrompt.kt` |
| 379 | `client.http_response_body_stream` | `execserver.src.client.HttpResponseBodyStream` | 6 | 17 | 3 | 20 | `exec-server/src/client/http_response_body_stream.rs` | `execserver/src/client/HttpResponseBodyStream.kt` |
| 380 | `core.shell_snapshot` | `core.src.ShellSnapshot` | 6 | 19 | 1 | 20 | `core/src/shell_snapshot.rs` | `core/src/ShellSnapshot.kt` |
| 381 | `guardian.approval_request` | `core.src.guardian.ApprovalRequest` | 5 | 11 | 9 | 20 | `core/src/guardian/approval_request.rs` | `core/src/guardian/ApprovalRequest.kt` |
| 382 | `core-skills.model` | `coreskills.src.Model` | 3 | 12 | 8 | 20 | `core-skills/src/model.rs` | `coreskills/src/Model.kt` |
| 383 | `bottom_pane.pending_input_preview` | `tui.src.bottompane.PendingInputPreview` | 1 | 19 | 1 | 20 | `tui/src/bottom_pane/pending_input_preview.rs` | `tui/src/bottompane/PendingInputPreview.kt` |
| 384 | `remote_control.client_tracker` | `appservertransport.src.transport.remotecontrol.ClientTracker` | 1 | 17 | 3 | 20 | `app-server-transport/src/transport/remote_control/client_tracker.rs` | `appservertransport/src/transport/remotecontrol/ClientTracker.kt` |
| 385 | `rollout-trace.compaction` | `rollouttrace.src.Compaction` | 1 | 12 | 8 | 20 | `rollout-trace/src/compaction.rs` | `rollouttrace/src/Compaction.kt` |
| 386 | `app.agent_navigation` | `tui.src.app.AgentNavigation` | 0 | 18 | 2 | 20 | `tui/src/app/agent_navigation.rs` | `tui/src/app/AgentNavigation.kt` |
| 387 | `chatwidget.interrupts` | `tui.src.chatwidget.Interrupts` | 0 | 18 | 2 | 20 | `tui/src/chatwidget/interrupts.rs` | `tui/src/chatwidget/Interrupts.kt` |
| 388 | `config.plugin_edit` | `config.src.PluginEdit` | 0 | 19 | 1 | 20 | `config/src/plugin_edit.rs` | `config/src/PluginEdit.kt` |
| 389 | `events.post_tool_use` | `hooks.src.events.PostToolUse` | 0 | 17 | 3 | 20 | `hooks/src/events/post_tool_use.rs` | `hooks/src/events/PostToolUse.kt` |
| 390 | `exec.event_processor_with_human_output_tests` | `exec.src.EventProcessorWithHumanOutputTests` | 0 | 20 | 0 | 20 | `exec/src/event_processor_with_human_output_tests.rs` | `exec/src/EventProcessorWithHumanOutputTests.kt` |
| 391 | `linux-sandbox.launcher` | `linuxsandbox.src.Launcher` | 0 | 17 | 3 | 20 | `linux-sandbox/src/launcher.rs` | `linuxsandbox/src/Launcher.kt` |
| 392 | `runtime.agent_jobs` | `state.src.runtime.AgentJobs` | 0 | 20 | 0 | 20 | `state/src/runtime/agent_jobs.rs` | `state/src/runtime/AgentJobs.kt` |
| 393 | `state.extract` | `state.src.Extract` | 0 | 20 | 0 | 20 | `state/src/extract.rs` | `state/src/Extract.kt` |
| 394 | `stream-parser.inline_hidden_tag` | `utils.streamparser.src.InlineHiddenTag` | 0 | 14 | 6 | 20 | `utils/stream-parser/src/inline_hidden_tag.rs` | `utils/streamparser/src/InlineHiddenTag.kt` |
| 395 | `suite.search_tool` | `core.tests.suite.SearchTool` | 0 | 20 | 0 | 20 | `core/tests/suite/search_tool.rs` | `core/tests/suite/SearchTool.kt` |
| 396 | `unified_exec.mod_tests` | `core.src.unifiedexec.ModTests` | 0 | 19 | 1 | 20 | `core/src/unified_exec/mod_tests.rs` | `core/src/unifiedexec/ModTests.kt` |
| 397 | `windows-sandbox-rs.setup_error` | `windowssandboxrs.src.SetupError` | 0 | 17 | 3 | 20 | `windows-sandbox-rs/src/setup_error.rs` | `windowssandboxrs/src/SetupError.kt` |
| 398 | `tui.test_backend` | `tui.src.TestBackend` | 7 | 18 | 1 | 19 | `tui/src/test_backend.rs` | `tui/src/TestBackend.kt` |
| 399 | `common.exec_server` | `execserver.tests.common.ExecServer` | 6 | 17 | 2 | 19 | `exec-server/tests/common/exec_server.rs` | `execserver/tests/common/ExecServer.kt` |
| 400 | `codex-mcp.tools` | `codexmcp.src.Tools` | 2 | 16 | 3 | 19 | `codex-mcp/src/tools.rs` | `codexmcp/src/Tools.kt` |
| 401 | `exec.event_processor_with_human_output` | `exec.src.EventProcessorWithHumanOutput` | 2 | 18 | 1 | 19 | `exec/src/event_processor_with_human_output.rs` | `exec/src/EventProcessorWithHumanOutput.kt` |
| 402 | `sandboxing.manager` | `sandboxing.src.Manager` | 2 | 12 | 7 | 19 | `sandboxing/src/manager.rs` | `sandboxing/src/Manager.kt` |
| 403 | `apply-patch.parser` | `applypatch.src.Parser` | 0 | 15 | 4 | 19 | `apply-patch/src/parser.rs` | `applypatch/src/Parser.kt` |
| 404 | `chatwidget.slash_dispatch` | `tui.src.chatwidget.SlashDispatch` | 0 | 16 | 3 | 19 | `tui/src/chatwidget/slash_dispatch.rs` | `tui/src/chatwidget/SlashDispatch.kt` |
| 405 | `command_safety.powershell_parser` | `shellcommand.src.commandsafety.PowershellParser` | 0 | 15 | 4 | 19 | `shell-command/src/command_safety/powershell_parser.rs` | `shellcommand/src/commandsafety/PowershellParser.kt` |
| 406 | `context.permissions_instructions_tests` | `core.src.context.PermissionsInstructionsTests` | 0 | 19 | 0 | 19 | `core/src/context/permissions_instructions_tests.rs` | `core/src/context/PermissionsInstructionsTests.kt` |
| 407 | `conversation.normalize` | `rollouttrace.src.reducer.conversation.Normalize` | 0 | 17 | 2 | 19 | `rollout-trace/src/reducer/conversation/normalize.rs` | `rollouttrace/src/reducer/conversation/Normalize.kt` |
| 408 | `endpoint.memories` | `codexapi.src.endpoint.Memories` | 0 | 14 | 5 | 19 | `codex-api/src/endpoint/memories.rs` | `codexapi/src/endpoint/Memories.kt` |
| 409 | `mcp.local` | `memories.mcp.src.Local` | 0 | 17 | 2 | 19 | `memories/mcp/src/local.rs` | `memories/mcp/src/Local.kt` |
| 410 | `mcp.server` | `memories.mcp.src.Server` | 0 | 15 | 4 | 19 | `memories/mcp/src/server.rs` | `memories/mcp/src/Server.kt` |
| 411 | `model-provider-info.model_provider_info_tests` | `modelproviderinfo.src.ModelProviderInfoTests` | 0 | 19 | 0 | 19 | `model-provider-info/src/model_provider_info_tests.rs` | `modelproviderinfo/src/ModelProviderInfoTests.kt` |
| 412 | `remote_control.segment` | `appservertransport.src.transport.remotecontrol.Segment` | 0 | 13 | 6 | 19 | `app-server-transport/src/transport/remote_control/segment.rs` | `appservertransport/src/transport/remotecontrol/Segment.kt` |
| 413 | `request_processors.mcp_processor` | `appserver.src.requestprocessors.McpProcessor` | 0 | 18 | 1 | 19 | `app-server/src/request_processors/mcp_processor.rs` | `appserver/src/requestprocessors/McpProcessor.kt` |
| 414 | `request_processors.thread_goal_processor` | `appserver.src.requestprocessors.ThreadGoalProcessor` | 0 | 18 | 1 | 19 | `app-server/src/request_processors/thread_goal_processor.rs` | `appserver/src/requestprocessors/ThreadGoalProcessor.kt` |
| 415 | `rmcp-client.auth_status` | `rmcpclient.src.AuthStatus` | 0 | 15 | 4 | 19 | `rmcp-client/src/auth_status.rs` | `rmcpclient/src/AuthStatus.kt` |
| 416 | `rollout.config` | `rollout.src.Config` | 0 | 16 | 3 | 19 | `rollout/src/config.rs` | `rollout/src/Config.kt` |
| 417 | `rollout.recorder_tests` | `rollout.src.RecorderTests` | 0 | 19 | 0 | 19 | `rollout/src/recorder_tests.rs` | `rollout/src/RecorderTests.kt` |
| 418 | `status.rate_limits` | `tui.src.status.RateLimits` | 0 | 13 | 6 | 19 | `tui/src/status/rate_limits.rs` | `tui/src/status/RateLimits.kt` |
| 419 | `suite.fuzzy_file_search` | `appserver.tests.suite.FuzzyFileSearch` | 0 | 18 | 1 | 19 | `app-server/tests/suite/fuzzy_file_search.rs` | `appserver/tests/suite/FuzzyFileSearch.kt` |
| 420 | `tests.app_server` | `tui.src.chatwidget.tests.AppServer` | 0 | 19 | 0 | 19 | `tui/src/chatwidget/tests/app_server.rs` | `tui/src/chatwidget/tests/AppServer.kt` |
| 421 | `thread-store.types` | `threadstore.src.Types` | 0 | 0 | 19 | 19 | `thread-store/src/types.rs` | `threadstore/src/Types.kt` |
| 422 | `tui.live_wrap` | `tui.src.LiveWrap` | 0 | 17 | 2 | 19 | `tui/src/live_wrap.rs` | `tui/src/LiveWrap.kt` |
| 423 | `app-server-protocol.experimental_api` | `appserverprotocol.src.ExperimentalApi` | 4 | 11 | 7 | 18 | `app-server-protocol/src/experimental_api.rs` | `appserverprotocol/src/ExperimentalApi.kt` |
| 424 | `desktop_app.mac` | `cli.src.desktopapp.Mac` | 3 | 18 | 0 | 18 | `cli/src/desktop_app/mac.rs` | `cli/src/desktopapp/Mac.kt` |
| 425 | `model.agent_job` | `state.src.model.AgentJob` | 3 | 8 | 10 | 18 | `state/src/model/agent_job.rs` | `state/src/model/AgentJob.kt` |
| 426 | `rmcp-client.executor_process_transport` | `rmcpclient.src.ExecutorProcessTransport` | 1 | 16 | 2 | 18 | `rmcp-client/src/executor_process_transport.rs` | `rmcpclient/src/ExecutorProcessTransport.kt` |
| 427 | `agent.role` | `core.src.agent.Role` | 0 | 18 | 0 | 18 | `core/src/agent/role.rs` | `core/src/agent/Role.kt` |
| 428 | `app-server-protocol.schema_fixtures` | `appserverprotocol.src.SchemaFixtures` | 0 | 16 | 2 | 18 | `app-server-protocol/src/schema_fixtures.rs` | `appserverprotocol/src/SchemaFixtures.kt` |
| 429 | `app-server.fs_watch` | `appserver.src.FsWatch` | 0 | 13 | 5 | 18 | `app-server/src/fs_watch.rs` | `appserver/src/FsWatch.kt` |
| 430 | `common.auth_fixtures` | `appserver.tests.common.AuthFixtures` | 0 | 16 | 2 | 18 | `app-server/tests/common/auth_fixtures.rs` | `appserver/tests/common/AuthFixtures.kt` |
| 431 | `config.agent_roles` | `core.src.config.AgentRoles` | 0 | 16 | 2 | 18 | `core/src/config/agent_roles.rs` | `core/src/config/AgentRoles.kt` |
| 432 | `core-plugins.loader_tests` | `coreplugins.src.LoaderTests` | 0 | 18 | 0 | 18 | `core-plugins/src/loader_tests.rs` | `coreplugins/src/LoaderTests.kt` |
| 433 | `core-skills.manager` | `coreskills.src.Manager` | 0 | 15 | 3 | 18 | `core-skills/src/manager.rs` | `coreskills/src/Manager.kt` |
| 434 | `core.turn_metadata` | `core.src.TurnMetadata` | 0 | 14 | 4 | 18 | `core/src/turn_metadata.rs` | `core/src/TurnMetadata.kt` |
| 435 | `core.util_tests` | `core.src.UtilTests` | 0 | 15 | 3 | 18 | `core/src/util_tests.rs` | `core/src/UtilTests.kt` |
| 436 | `events.stop` | `hooks.src.events.Stop` | 0 | 15 | 3 | 18 | `hooks/src/events/stop.rs` | `hooks/src/events/Stop.kt` |
| 437 | `handlers.multi_agents_common` | `core.src.tools.handlers.MultiAgentsCommon` | 0 | 18 | 0 | 18 | `core/src/tools/handlers/multi_agents_common.rs` | `core/src/tools/handlers/MultiAgentsCommon.kt` |
| 438 | `handlers.tool_search` | `core.src.tools.handlers.ToolSearch` | 0 | 16 | 2 | 18 | `core/src/tools/handlers/tool_search.rs` | `core/src/tools/handlers/ToolSearch.kt` |
| 439 | `keymap_setup.picker` | `tui.src.keymapsetup.Picker` | 0 | 16 | 2 | 18 | `tui/src/keymap_setup/picker.rs` | `tui/src/keymapsetup/Picker.kt` |
| 440 | `marketplace_add.metadata` | `coreplugins.src.marketplaceadd.Metadata` | 0 | 16 | 2 | 18 | `core-plugins/src/marketplace_add/metadata.rs` | `coreplugins/src/marketplaceadd/Metadata.kt` |
| 441 | `model.runtime` | `rollouttrace.src.model.Runtime` | 0 | 0 | 18 | 18 | `rollout-trace/src/model/runtime.rs` | `rollouttrace/src/model/Runtime.kt` |
| 442 | `network-proxy.upstream` | `networkproxy.src.Upstream` | 0 | 14 | 4 | 18 | `network-proxy/src/upstream.rs` | `networkproxy/src/Upstream.kt` |
| 443 | `protocol.mcp` | `protocol.src.Mcp` | 0 | 9 | 9 | 18 | `protocol/src/mcp.rs` | `protocol/src/Mcp.kt` |
| 444 | `reducer.test_support` | `rollouttrace.src.reducer.TestSupport` | 0 | 18 | 0 | 18 | `rollout-trace/src/reducer/test_support.rs` | `rollouttrace/src/reducer/TestSupport.kt` |
| 445 | `remote_control.protocol` | `appservertransport.src.transport.remotecontrol.Protocol` | 0 | 8 | 10 | 18 | `app-server-transport/src/transport/remote_control/protocol.rs` | `appservertransport/src/transport/remotecontrol/Protocol.kt` |
| 446 | `suite.personality_migration` | `core.tests.suite.PersonalityMigration` | 0 | 18 | 0 | 18 | `core/tests/suite/personality_migration.rs` | `core/tests/suite/PersonalityMigration.kt` |
| 447 | `suite.resize_reflow` | `tui.tests.suite.ResizeReflow` | 0 | 17 | 1 | 18 | `tui/tests/suite/resize_reflow.rs` | `tui/tests/suite/ResizeReflow.kt` |
| 448 | `suite.shell_serialization` | `core.tests.suite.ShellSerialization` | 0 | 18 | 0 | 18 | `core/tests/suite/shell_serialization.rs` | `core/tests/suite/ShellSerialization.kt` |
| 449 | `tests.status_surface_previews` | `tui.src.chatwidget.tests.StatusSurfacePreviews` | 0 | 18 | 0 | 18 | `tui/src/chatwidget/tests/status_surface_previews.rs` | `tui/src/chatwidget/tests/StatusSurfacePreviews.kt` |
| 450 | `transport.websocket` | `appservertransport.src.transport.Websocket` | 0 | 15 | 3 | 18 | `app-server-transport/src/transport/websocket.rs` | `appservertransport/src/transport/Websocket.kt` |
| 451 | `tui.external_agent_config_migration_startup` | `tui.src.ExternalAgentConfigMigrationStartup` | 0 | 17 | 1 | 18 | `tui/src/external_agent_config_migration_startup.rs` | `tui/src/ExternalAgentConfigMigrationStartup.kt` |
| 452 | `tui.oss_selection` | `tui.src.OssSelection` | 0 | 14 | 4 | 18 | `tui/src/oss_selection.rs` | `tui/src/OssSelection.kt` |
| 453 | `unified_exec.tests` | `windowssandboxrs.src.unifiedexec.Tests` | 0 | 18 | 0 | 18 | `windows-sandbox-rs/src/unified_exec/tests.rs` | `windowssandboxrs/src/unifiedexec/Tests.kt` |
| 454 | `windows-sandbox-rs.spawn_prep` | `windowssandboxrs.src.SpawnPrep` | 0 | 14 | 4 | 18 | `windows-sandbox-rs/src/spawn_prep.rs` | `windowssandboxrs/src/SpawnPrep.kt` |
| 455 | `keymap_setup.debug` | `tui.src.keymapsetup.Debug` | 32 | 15 | 2 | 17 | `tui/src/keymap_setup/debug.rs` | `tui/src/keymapsetup/Debug.kt` |
| 456 | `suite.tool` | `applypatch.tests.suite.Tool` | 20 | 17 | 0 | 17 | `apply-patch/tests/suite/tool.rs` | `applypatch/tests/suite/Tool.kt` |
| 457 | `engine.dispatcher` | `hooks.src.engine.Dispatcher` | 7 | 16 | 1 | 17 | `hooks/src/engine/dispatcher.rs` | `hooks/src/engine/Dispatcher.kt` |
| 458 | `public_widgets.composer_input` | `tui.src.publicwidgets.ComposerInput` | 3 | 15 | 2 | 17 | `tui/src/public_widgets/composer_input.rs` | `tui/src/publicwidgets/ComposerInput.kt` |
| 459 | `thread-store.live_thread` | `threadstore.src.LiveThread` | 3 | 15 | 2 | 17 | `thread-store/src/live_thread.rs` | `threadstore/src/LiveThread.kt` |
| 460 | `cloud-tasks.scrollable_diff` | `cloudtasks.src.ScrollableDiff` | 1 | 15 | 2 | 17 | `cloud-tasks/src/scrollable_diff.rs` | `cloudtasks/src/ScrollableDiff.kt` |
| 461 | `windows-sandbox-rs.token` | `windowssandboxrs.src.Token` | 1 | 15 | 2 | 17 | `windows-sandbox-rs/src/token.rs` | `windowssandboxrs/src/Token.kt` |
| 462 | `agent.registry_tests` | `core.src.agent.RegistryTests` | 0 | 17 | 0 | 17 | `core/src/agent/registry_tests.rs` | `core/src/agent/RegistryTests.kt` |
| 463 | `app-server.config_manager_service_tests` | `appserver.src.ConfigManagerServiceTests` | 0 | 17 | 0 | 17 | `app-server/src/config_manager_service_tests.rs` | `appserver/src/ConfigManagerServiceTests.kt` |
| 464 | `auth.agent_identity` | `login.src.auth.AgentIdentity` | 0 | 15 | 2 | 17 | `login/src/auth/agent_identity.rs` | `login/src/auth/AgentIdentity.kt` |
| 465 | `bottom_pane.status_line_style` | `tui.src.bottompane.StatusLineStyle` | 0 | 16 | 1 | 17 | `tui/src/bottom_pane/status_line_style.rs` | `tui/src/bottompane/StatusLineStyle.kt` |
| 466 | `chatwidget.goal_status` | `tui.src.chatwidget.GoalStatus` | 0 | 16 | 1 | 17 | `tui/src/chatwidget/goal_status.rs` | `tui/src/chatwidget/GoalStatus.kt` |
| 467 | `cli.login` | `cli.src.Login` | 0 | 17 | 0 | 17 | `cli/src/login.rs` | `cli/src/Login.kt` |
| 468 | `codex-client.chatgpt_cloudflare_cookies` | `codexclient.src.ChatgptCloudflareCookies` | 0 | 16 | 1 | 17 | `codex-client/src/chatgpt_cloudflare_cookies.rs` | `codexclient/src/ChatgptCloudflareCookies.kt` |
| 469 | `codex-mcp.codex_apps` | `codexmcp.src.CodexApps` | 0 | 13 | 4 | 17 | `codex-mcp/src/codex_apps.rs` | `codexmcp/src/CodexApps.kt` |
| 470 | `core-plugins.store_tests` | `coreplugins.src.StoreTests` | 0 | 17 | 0 | 17 | `core-plugins/src/store_tests.rs` | `coreplugins/src/StoreTests.kt` |
| 471 | `core.stream_events_utils` | `core.src.StreamEventsUtils` | 0 | 14 | 3 | 17 | `core/src/stream_events_utils.rs` | `core/src/StreamEventsUtils.kt` |
| 472 | `core.stream_events_utils_tests` | `core.src.StreamEventsUtilsTests` | 0 | 17 | 0 | 17 | `core/src/stream_events_utils_tests.rs` | `core/src/StreamEventsUtilsTests.kt` |
| 473 | `core.turn_timing` | `core.src.TurnTiming` | 0 | 15 | 2 | 17 | `core/src/turn_timing.rs` | `core/src/TurnTiming.kt` |
| 474 | `exec_cell.model` | `tui.src.execcell.Model` | 0 | 14 | 3 | 17 | `tui/src/exec_cell/model.rs` | `tui/src/execcell/Model.kt` |
| 475 | `external-agent-sessions.export` | `externalagentsessions.src.Export` | 0 | 17 | 0 | 17 | `external-agent-sessions/src/export.rs` | `externalagentsessions/src/Export.kt` |
| 476 | `handlers.goal` | `core.src.tools.handlers.Goal` | 0 | 11 | 6 | 17 | `core/src/tools/handlers/goal.rs` | `core/src/tools/handlers/Goal.kt` |
| 477 | `hooks.types` | `hooks.src.Types` | 0 | 6 | 11 | 17 | `hooks/src/types.rs` | `hooks/src/Types.kt` |
| 478 | `mcp.auth` | `codexmcp.src.mcp.Auth` | 0 | 12 | 5 | 17 | `codex-mcp/src/mcp/auth.rs` | `codexmcp/src/mcp/Auth.kt` |
| 479 | `reducer.conversation_tests` | `rollouttrace.src.reducer.ConversationTests` | 0 | 17 | 0 | 17 | `rollout-trace/src/reducer/conversation_tests.rs` | `rollouttrace/src/reducer/ConversationTests.kt` |
| 480 | `remote.remote_installed_plugin_sync` | `coreplugins.src.remote.RemoteInstalledPluginSync` | 0 | 12 | 5 | 17 | `core-plugins/src/remote/remote_installed_plugin_sync.rs` | `coreplugins/src/remote/RemoteInstalledPluginSync.kt` |
| 481 | `request_user_input.render` | `tui.src.bottompane.requestuserinput.Render` | 0 | 14 | 3 | 17 | `tui/src/bottom_pane/request_user_input/render.rs` | `tui/src/bottompane/requestuserinput/Render.kt` |
| 482 | `share.tests` | `coreplugins.src.remote.share.Tests` | 0 | 17 | 0 | 17 | `core-plugins/src/remote/share/tests.rs` | `coreplugins/src/remote/share/Tests.kt` |
| 483 | `stream-parser.proposed_plan` | `utils.streamparser.src.ProposedPlan` | 0 | 13 | 4 | 17 | `utils/stream-parser/src/proposed_plan.rs` | `utils/streamparser/src/ProposedPlan.kt` |
| 484 | `suite.collaboration_instructions` | `core.tests.suite.CollaborationInstructions` | 0 | 17 | 0 | 17 | `core/tests/suite/collaboration_instructions.rs` | `core/tests/suite/CollaborationInstructions.kt` |
| 485 | `suite.items` | `core.tests.suite.Items` | 0 | 17 | 0 | 17 | `core/tests/suite/items.rs` | `core/tests/suite/Items.kt` |
| 486 | `suite.subagent_notifications` | `core.tests.suite.SubagentNotifications` | 0 | 17 | 0 | 17 | `core/tests/suite/subagent_notifications.rs` | `core/tests/suite/SubagentNotifications.kt` |
| 487 | `tools.network_approval_tests` | `core.src.tools.NetworkApprovalTests` | 0 | 17 | 0 | 17 | `core/src/tools/network_approval_tests.rs` | `core/src/tools/NetworkApprovalTests.kt` |
| 488 | `windows-sandbox-rs.sandbox_users` | `windowssandboxrs.src.SandboxUsers` | 0 | 14 | 3 | 17 | `windows-sandbox-rs/src/sandbox_users.rs` | `windowssandboxrs/src/SandboxUsers.kt` |
| 489 | `write.startup_tests` | `memories.write.src.StartupTests` | 0 | 17 | 0 | 17 | `memories/write/src/startup_tests.rs` | `memories/write/src/StartupTests.kt` |
| 490 | `login.token_data` | `login.src.TokenData` | 12 | 9 | 7 | 16 | `login/src/token_data.rs` | `login/src/TokenData.kt` |
| 491 | `execpolicy-legacy.policy_parser` | `execpolicylegacy.src.PolicyParser` | 9 | 13 | 3 | 16 | `execpolicy-legacy/src/policy_parser.rs` | `execpolicylegacy/src/PolicyParser.kt` |
| 492 | `context.permissions_instructions` | `core.src.context.PermissionsInstructions` | 4 | 14 | 2 | 16 | `core/src/context/permissions_instructions.rs` | `core/src/context/PermissionsInstructions.kt` |
| 493 | `win.psuedocon` | `utils.pty.src.win.Psuedocon` | 4 | 14 | 2 | 16 | `utils/pty/src/win/psuedocon.rs` | `utils/pty/src/win/Psuedocon.kt` |
| 494 | `bottom_pane.slash_commands` | `tui.src.bottompane.SlashCommands` | 3 | 15 | 1 | 16 | `tui/src/bottom_pane/slash_commands.rs` | `tui/src/bottompane/SlashCommands.kt` |
| 495 | `bottom_pane.experimental_features_view` | `tui.src.bottompane.ExperimentalFeaturesView` | 2 | 14 | 2 | 16 | `tui/src/bottom_pane/experimental_features_view.rs` | `tui/src/bottompane/ExperimentalFeaturesView.kt` |
| 496 | `debug_sandbox.pid_tracker` | `cli.src.debugsandbox.PidTracker` | 1 | 14 | 2 | 16 | `cli/src/debug_sandbox/pid_tracker.rs` | `cli/src/debugsandbox/PidTracker.kt` |
| 497 | `keymap_setup.actions` | `tui.src.keymapsetup.Actions` | 1 | 11 | 5 | 16 | `tui/src/keymap_setup/actions.rs` | `tui/src/keymapsetup/Actions.kt` |
| 498 | `stream-parser.tagged_line_parser` | `utils.streamparser.src.TaggedLineParser` | 1 | 12 | 4 | 16 | `utils/stream-parser/src/tagged_line_parser.rs` | `utils/streamparser/src/TaggedLineParser.kt` |
| 499 | `app-server.request_serialization` | `appserver.src.RequestSerialization` | 0 | 12 | 4 | 16 | `app-server/src/request_serialization.rs` | `appserver/src/RequestSerialization.kt` |
| 500 | `apply-patch.streaming_parser` | `applypatch.src.StreamingParser` | 0 | 13 | 3 | 16 | `apply-patch/src/streaming_parser.rs` | `applypatch/src/StreamingParser.kt` |
| 501 | `chatgpt.connectors` | `chatgpt.src.Connectors` | 0 | 16 | 0 | 16 | `chatgpt/src/connectors.rs` | `chatgpt/src/Connectors.kt` |
| 502 | `config.network_proxy_spec_tests` | `core.src.config.NetworkProxySpecTests` | 0 | 16 | 0 | 16 | `core/src/config/network_proxy_spec_tests.rs` | `core/src/config/NetworkProxySpecTests.kt` |
| 503 | `core.arc_monitor` | `core.src.ArcMonitor` | 0 | 7 | 9 | 16 | `core/src/arc_monitor.rs` | `core/src/ArcMonitor.kt` |
| 504 | `core.mcp_tool_approval_templates` | `core.src.McpToolApprovalTemplates` | 0 | 11 | 5 | 16 | `core/src/mcp_tool_approval_templates.rs` | `core/src/McpToolApprovalTemplates.kt` |
| 505 | `elevated.ipc_framed` | `windowssandboxrs.src.elevated.IpcFramed` | 0 | 5 | 11 | 16 | `windows-sandbox-rs/src/elevated/ipc_framed.rs` | `windowssandboxrs/src/elevated/IpcFramed.kt` |
| 506 | `endpoint.models` | `codexapi.src.endpoint.Models` | 0 | 13 | 3 | 16 | `codex-api/src/endpoint/models.rs` | `codexapi/src/endpoint/Models.kt` |
| 507 | `exec-server.process` | `execserver.src.Process` | 0 | 8 | 8 | 16 | `exec-server/src/process.rs` | `execserver/src/Process.kt` |
| 508 | `external-agent-sessions.detect` | `externalagentsessions.src.Detect` | 0 | 15 | 1 | 16 | `external-agent-sessions/src/detect.rs` | `externalagentsessions/src/Detect.kt` |
| 509 | `handlers.read_file_tests` | `core.src.tools.handlers.ReadFileTests` | 0 | 16 | 0 | 16 | `core/src/tools/handlers/read_file_tests.rs` | `core/src/tools/handlers/ReadFileTests.kt` |
| 510 | `local.update_thread_metadata` | `threadstore.src.local.UpdateThreadMetadata` | 0 | 15 | 1 | 16 | `thread-store/src/local/update_thread_metadata.rs` | `threadstore/src/local/UpdateThreadMetadata.kt` |
| 511 | `marketplace_upgrade.git` | `coreplugins.src.marketplaceupgrade.Git` | 0 | 16 | 0 | 16 | `core-plugins/src/marketplace_upgrade/git.rs` | `coreplugins/src/marketplaceupgrade/Git.kt` |
| 512 | `plugin.load_outcome` | `plugin.src.LoadOutcome` | 0 | 13 | 3 | 16 | `plugin/src/load_outcome.rs` | `plugin/src/LoadOutcome.kt` |
| 513 | `rmcp-client.http_client_adapter` | `rmcpclient.src.HttpClientAdapter` | 0 | 13 | 3 | 16 | `rmcp-client/src/http_client_adapter.rs` | `rmcpclient/src/HttpClientAdapter.kt` |
| 514 | `rmcp-client.utils` | `rmcpclient.src.Utils` | 0 | 15 | 1 | 16 | `rmcp-client/src/utils.rs` | `rmcpclient/src/Utils.kt` |
| 515 | `stream-parser.citation` | `utils.streamparser.src.Citation` | 0 | 13 | 3 | 16 | `utils/stream-parser/src/citation.rs` | `utils/streamparser/src/Citation.kt` |
| 516 | `tui.session_resume` | `tui.src.SessionResume` | 0 | 11 | 5 | 16 | `tui/src/session_resume.rs` | `tui/src/SessionResume.kt` |
| 517 | `win.conpty` | `utils.pty.src.win.Conpty` | 0 | 11 | 5 | 16 | `utils/pty/src/win/conpty.rs` | `utils/pty/src/win/Conpty.kt` |
| 518 | `windows-sandbox-rs.setup_main_win` | `windowssandboxrs.src.SetupMainWin` | 0 | 13 | 3 | 16 | `windows-sandbox-rs/src/setup_main_win.rs` | `windowssandboxrs/src/SetupMainWin.kt` |
| 519 | `write.runtime` | `memories.write.src.Runtime` | 0 | 13 | 3 | 16 | `memories/write/src/runtime.rs` | `memories/write/src/Runtime.kt` |
| 520 | `suite.personality` | `core.tests.suite.Personality` | 27 | 15 | 0 | 15 | `core/tests/suite/personality.rs` | `core/tests/suite/Personality.kt` |
| 521 | `tools.tool_spec` | `tools.src.ToolSpec` | 24 | 10 | 5 | 15 | `tools/src/tool_spec.rs` | `tools/src/ToolSpec.kt` |
| 522 | `debug-client.output` | `debugclient.src.Output` | 9 | 12 | 3 | 15 | `debug-client/src/output.rs` | `debugclient/src/Output.kt` |
| 523 | `auth.default_client` | `login.src.auth.DefaultClient` | 1 | 13 | 2 | 15 | `login/src/auth/default_client.rs` | `login/src/auth/DefaultClient.kt` |
| 524 | `core-skills.remote` | `coreskills.src.Remote` | 1 | 9 | 6 | 15 | `core-skills/src/remote.rs` | `coreskills/src/Remote.kt` |
| 525 | `exec-server.remote_file_system` | `execserver.src.RemoteFileSystem` | 1 | 14 | 1 | 15 | `exec-server/src/remote_file_system.rs` | `execserver/src/RemoteFileSystem.kt` |
| 526 | `state.runtime` | `state.src.runtime.Runtime` | 1 | 14 | 1 | 15 | `state/src/runtime.rs` | `state/src/runtime/Runtime.kt` |
| 527 | `write.phase2` | `memories.write.src.Phase2` | 1 | 13 | 2 | 15 | `memories/write/src/phase2.rs` | `memories/write/src/Phase2.kt` |
| 528 | `agent-graph-store.local` | `agentgraphstore.src.Local` | 0 | 13 | 2 | 15 | `agent-graph-store/src/local.rs` | `agentgraphstore/src/Local.kt` |
| 529 | `amazon_bedrock.auth` | `modelprovider.src.amazonbedrock.Auth` | 0 | 13 | 2 | 15 | `model-provider/src/amazon_bedrock/auth.rs` | `modelprovider/src/amazonbedrock/Auth.kt` |
| 530 | `cloud-tasks-client.api` | `cloudtasksclient.src.Api` | 0 | 1 | 14 | 15 | `cloud-tasks-client/src/api.rs` | `cloudtasksclient/src/Api.kt` |
| 531 | `core.compact_tests` | `core.src.CompactTests` | 0 | 15 | 0 | 15 | `core/src/compact_tests.rs` | `core/src/CompactTests.kt` |
| 532 | `core.realtime_context` | `core.src.RealtimeContext` | 0 | 15 | 0 | 15 | `core/src/realtime_context.rs` | `core/src/RealtimeContext.kt` |
| 533 | `elevated.command_runner_win` | `windowssandboxrs.src.elevated.CommandRunnerWin` | 0 | 13 | 2 | 15 | `windows-sandbox-rs/src/elevated/command_runner_win.rs` | `windowssandboxrs/src/elevated/CommandRunnerWin.kt` |
| 534 | `exec-server.fs_helper` | `execserver.src.FsHelper` | 0 | 12 | 3 | 15 | `exec-server/src/fs_helper.rs` | `execserver/src/FsHelper.kt` |
| 535 | `handlers.unified_exec_tests` | `core.src.tools.handlers.UnifiedExecTests` | 0 | 15 | 0 | 15 | `core/src/tools/handlers/unified_exec_tests.rs` | `core/src/tools/handlers/UnifiedExecTests.kt` |
| 536 | `network-proxy.certs` | `networkproxy.src.Certs` | 0 | 14 | 1 | 15 | `network-proxy/src/certs.rs` | `networkproxy/src/Certs.kt` |
| 537 | `output-truncation.truncate_tests` | `utils.outputtruncation.src.TruncateTests` | 0 | 15 | 0 | 15 | `utils/output-truncation/src/truncate_tests.rs` | `utils/outputtruncation/src/TruncateTests.kt` |
| 538 | `reducer.tool` | `rollouttrace.src.reducer.tool.Tool` | 0 | 14 | 1 | 15 | `rollout-trace/src/reducer/tool.rs` | `rollouttrace/src/reducer/tool/Tool.kt` |
| 539 | `responses-api-proxy.read_api_key` | `responsesapiproxy.src.ReadApiKey` | 0 | 15 | 0 | 15 | `responses-api-proxy/src/read_api_key.rs` | `responsesapiproxy/src/ReadApiKey.kt` |
| 540 | `stream-parser.utf8_stream` | `utils.streamparser.src.Utf8Stream` | 0 | 13 | 2 | 15 | `utils/stream-parser/src/utf8_stream.rs` | `utils/streamparser/src/Utf8Stream.kt` |
| 541 | `suite.agent_jobs` | `core.tests.suite.AgentJobs` | 0 | 13 | 2 | 15 | `core/tests/suite/agent_jobs.rs` | `core/tests/suite/AgentJobs.kt` |
| 542 | `suite.shell_command` | `core.tests.suite.ShellCommand` | 0 | 15 | 0 | 15 | `core/tests/suite/shell_command.rs` | `core/tests/suite/ShellCommand.kt` |
| 543 | `tests.http_request` | `execserver.tests.HttpRequest` | 0 | 14 | 1 | 15 | `exec-server/tests/http_request.rs` | `execserver/tests/HttpRequest.kt` |
| 544 | `v2.connection_handling_websocket_unix` | `appserver.tests.suite.v2.ConnectionHandlingWebsocketUnix` | 0 | 14 | 1 | 15 | `app-server/tests/suite/v2/connection_handling_websocket_unix.rs` | `appserver/tests/suite/v2/ConnectionHandlingWebsocketUnix.kt` |
| 545 | `v2.marketplace_upgrade` | `appserver.tests.suite.v2.MarketplaceUpgrade` | 0 | 15 | 0 | 15 | `app-server/tests/suite/v2/marketplace_upgrade.rs` | `appserver/tests/suite/v2/MarketplaceUpgrade.kt` |
| 546 | `v2.plugin_uninstall` | `appserver.tests.suite.v2.PluginUninstall` | 0 | 15 | 0 | 15 | `app-server/tests/suite/v2/plugin_uninstall.rs` | `appserver/tests/suite/v2/PluginUninstall.kt` |
| 547 | `protocol.thread_id` | `protocol.src.ThreadId` | 154 | 12 | 2 | 14 | `protocol/src/thread_id.rs` | `protocol/src/ThreadId.kt` |
| 548 | `codex-client.request` | `codexclient.src.Request` | 32 | 9 | 5 | 14 | `codex-client/src/request.rs` | `codexclient/src/Request.kt` |
| 549 | `mcp.backend` | `memories.mcp.src.Backend` | 6 | 2 | 12 | 14 | `memories/mcp/src/backend.rs` | `memories/mcp/src/Backend.kt` |
| 550 | `config.requirements_exec_policy` | `config.src.RequirementsExecPolicy` | 3 | 8 | 6 | 14 | `config/src/requirements_exec_policy.rs` | `config/src/RequirementsExecPolicy.kt` |
| 551 | `network-proxy.socks5` | `networkproxy.src.Socks5` | 1 | 13 | 1 | 14 | `network-proxy/src/socks5.rs` | `networkproxy/src/Socks5.kt` |
| 552 | `rmcp-client.elicitation_client_service` | `rmcpclient.src.ElicitationClientService` | 1 | 12 | 2 | 14 | `rmcp-client/src/elicitation_client_service.rs` | `rmcpclient/src/ElicitationClientService.kt` |
| 553 | `tui.workspace_command` | `tui.src.WorkspaceCommand` | 1 | 8 | 6 | 14 | `tui/src/workspace_command.rs` | `tui/src/WorkspaceCommand.kt` |
| 554 | `absolute-path.absolutize` | `utils.absolutepath.src.Absolutize` | 0 | 14 | 0 | 14 | `utils/absolute-path/src/absolutize.rs` | `utils/absolutepath/src/Absolutize.kt` |
| 555 | `config.marketplace_edit` | `config.src.MarketplaceEdit` | 0 | 12 | 2 | 14 | `config/src/marketplace_edit.rs` | `config/src/MarketplaceEdit.kt` |
| 556 | `core.codex_delegate` | `core.src.CodexDelegate` | 0 | 14 | 0 | 14 | `core/src/codex_delegate.rs` | `core/src/CodexDelegate.kt` |
| 557 | `core.event_mapping_tests` | `core.src.EventMappingTests` | 0 | 14 | 0 | 14 | `core/src/event_mapping_tests.rs` | `core/src/EventMappingTests.kt` |
| 558 | `core.realtime_context_tests` | `core.src.RealtimeContextTests` | 0 | 14 | 0 | 14 | `core/src/realtime_context_tests.rs` | `core/src/RealtimeContextTests.kt` |
| 559 | `core.test_support` | `core.src.TestSupport` | 0 | 14 | 0 | 14 | `core/src/test_support.rs` | `core/src/TestSupport.kt` |
| 560 | `events.session_start` | `hooks.src.events.SessionStart` | 0 | 10 | 4 | 14 | `hooks/src/events/session_start.rs` | `hooks/src/events/SessionStart.kt` |
| 561 | `handler.tests` | `execserver.src.server.handler.Tests` | 0 | 14 | 0 | 14 | `exec-server/src/server/handler/tests.rs` | `execserver/src/server/handler/Tests.kt` |
| 562 | `linux-sandbox.landlock` | `linuxsandbox.src.Landlock` | 0 | 13 | 1 | 14 | `linux-sandbox/src/landlock.rs` | `linuxsandbox/src/Landlock.kt` |
| 563 | `pty.process_group` | `utils.pty.src.ProcessGroup` | 0 | 14 | 0 | 14 | `utils/pty/src/process_group.rs` | `utils/pty/src/ProcessGroup.kt` |
| 564 | `pty.pty` | `utils.pty.src.Pty` | 0 | 12 | 2 | 14 | `utils/pty/src/pty.rs` | `utils/pty/src/Pty.kt` |
| 565 | `request_processors.apps_processor` | `appserver.src.requestprocessors.AppsProcessor` | 0 | 12 | 2 | 14 | `app-server/src/request_processors/apps_processor.rs` | `appserver/src/requestprocessors/AppsProcessor.kt` |
| 566 | `session.session` | `core.src.session.Session` | 0 | 10 | 4 | 14 | `core/src/session/session.rs` | `core/src/session/Session.kt` |
| 567 | `status.helpers` | `tui.src.status.Helpers` | 0 | 14 | 0 | 14 | `tui/src/status/helpers.rs` | `tui/src/status/Helpers.kt` |
| 568 | `suite.plugins` | `core.tests.suite.Plugins` | 0 | 14 | 0 | 14 | `core/tests/suite/plugins.rs` | `core/tests/suite/Plugins.kt` |
| 569 | `suite.request_permissions_tool` | `core.tests.suite.RequestPermissionsTool` | 0 | 14 | 0 | 14 | `core/tests/suite/request_permissions_tool.rs` | `core/tests/suite/RequestPermissionsTool.kt` |
| 570 | `suite.tools` | `core.tests.suite.Tools` | 0 | 14 | 0 | 14 | `core/tests/suite/tools.rs` | `core/tests/suite/Tools.kt` |
| 571 | `tasks.review` | `core.src.tasks.Review` | 0 | 13 | 1 | 14 | `core/src/tasks/review.rs` | `core/src/tasks/Review.kt` |
| 572 | `tools.context_tests` | `core.src.tools.ContextTests` | 0 | 14 | 0 | 14 | `core/src/tools/context_tests.rs` | `core/src/tools/ContextTests.kt` |
| 573 | `tools.local_tool` | `tools.src.LocalTool` | 0 | 12 | 2 | 14 | `tools/src/local_tool.rs` | `tools/src/LocalTool.kt` |
| 574 | `tui.terminal_title` | `tui.src.TerminalTitle` | 0 | 12 | 2 | 14 | `tui/src/terminal_title.rs` | `tui/src/TerminalTitle.kt` |
| 575 | `v2.config_rpc` | `appserver.tests.suite.v2.ConfigRpc` | 0 | 14 | 0 | 14 | `app-server/tests/suite/v2/config_rpc.rs` | `appserver/tests/suite/v2/ConfigRpc.kt` |
| 576 | `v2.skills_list` | `appserver.tests.suite.v2.SkillsList` | 0 | 14 | 0 | 14 | `app-server/tests/suite/v2/skills_list.rs` | `appserver/tests/suite/v2/SkillsList.kt` |
| 577 | `common.mcp_process` | `mcpserver.tests.common.McpProcess` | 65 | 12 | 1 | 13 | `mcp-server/tests/common/mcp_process.rs` | `mcpserver/tests/common/McpProcess.kt` |
| 578 | `exec-server.process_id` | `execserver.src.ProcessId` | 12 | 11 | 2 | 13 | `exec-server/src/process_id.rs` | `execserver/src/ProcessId.kt` |
| 579 | `tui.cli` | `tui.src.Cli` | 7 | 10 | 3 | 13 | `tui/src/cli.rs` | `tui/src/Cli.kt` |
| 580 | `v2.mcp_server_status` | `appserver.tests.suite.v2.McpServerStatus` | 4 | 11 | 2 | 13 | `app-server/tests/suite/v2/mcp_server_status.rs` | `appserver/tests/suite/v2/McpServerStatus.kt` |
| 581 | `otel.trace_context` | `otel.src.TraceContext` | 3 | 13 | 0 | 13 | `otel/src/trace_context.rs` | `otel/src/TraceContext.kt` |
| 582 | `agent.mailbox` | `core.src.agent.Mailbox` | 2 | 11 | 2 | 13 | `core/src/agent/mailbox.rs` | `core/src/agent/Mailbox.kt` |
| 583 | `bottom_pane.custom_prompt_view` | `tui.src.bottompane.CustomPromptView` | 2 | 11 | 2 | 13 | `tui/src/bottom_pane/custom_prompt_view.rs` | `tui/src/bottompane/CustomPromptView.kt` |
| 584 | `exec-server.environment_provider` | `execserver.src.EnvironmentProvider` | 2 | 11 | 2 | 13 | `exec-server/src/environment_provider.rs` | `execserver/src/EnvironmentProvider.kt` |
| 585 | `windows-sandbox-rs.acl` | `windowssandboxrs.src.Acl` | 2 | 13 | 0 | 13 | `windows-sandbox-rs/src/acl.rs` | `windowssandboxrs/src/Acl.kt` |
| 586 | `rollout-trace.code_cell` | `rollouttrace.src.CodeCell` | 1 | 9 | 4 | 13 | `rollout-trace/src/code_cell.rs` | `rollouttrace/src/CodeCell.kt` |
| 587 | `analytics.client_tests` | `analytics.src.ClientTests` | 0 | 13 | 0 | 13 | `analytics/src/client_tests.rs` | `analytics/src/ClientTests.kt` |
| 588 | `app.startup_prompts` | `tui.src.app.StartupPrompts` | 0 | 12 | 1 | 13 | `tui/src/app/startup_prompts.rs` | `tui/src/app/StartupPrompts.kt` |
| 589 | `cli.marketplace_cmd` | `cli.src.MarketplaceCmd` | 0 | 8 | 5 | 13 | `cli/src/marketplace_cmd.rs` | `cli/src/MarketplaceCmd.kt` |
| 590 | `codex-api.files` | `codexapi.src.Files` | 0 | 8 | 5 | 13 | `codex-api/src/files.rs` | `codexapi/src/Files.kt` |
| 591 | `config.hook_config` | `config.src.HookConfig` | 0 | 6 | 7 | 13 | `config/src/hook_config.rs` | `config/src/HookConfig.kt` |
| 592 | `config.mcp_edit` | `config.src.McpEdit` | 0 | 12 | 1 | 13 | `config/src/mcp_edit.rs` | `config/src/McpEdit.kt` |
| 593 | `config.permissions_tests` | `core.src.config.PermissionsTests` | 0 | 13 | 0 | 13 | `core/src/config/permissions_tests.rs` | `core/src/config/PermissionsTests.kt` |
| 594 | `core-plugins.marketplace_add` | `coreplugins.src.marketplaceadd.MarketplaceAdd` | 0 | 10 | 3 | 13 | `core-plugins/src/marketplace_add.rs` | `coreplugins/src/marketplaceadd/MarketplaceAdd.kt` |
| 595 | `core.config_lock` | `core.src.ConfigLock` | 0 | 12 | 1 | 13 | `core/src/config_lock.rs` | `core/src/ConfigLock.kt` |
| 596 | `core.exec_env_tests` | `core.src.ExecEnvTests` | 0 | 13 | 0 | 13 | `core/src/exec_env_tests.rs` | `core/src/ExecEnvTests.kt` |
| 597 | `core.thread_rollout_truncation_tests` | `core.src.ThreadRolloutTruncationTests` | 0 | 13 | 0 | 13 | `core/src/thread_rollout_truncation_tests.rs` | `core/src/ThreadRolloutTruncationTests.kt` |
| 598 | `debug-client.commands` | `debugclient.src.Commands` | 0 | 10 | 3 | 13 | `debug-client/src/commands.rs` | `debugclient/src/Commands.kt` |
| 599 | `engine.mod_tests` | `hooks.src.engine.ModTests` | 0 | 13 | 0 | 13 | `hooks/src/engine/mod_tests.rs` | `hooks/src/engine/ModTests.kt` |
| 600 | `events.user_prompt_submit` | `hooks.src.events.UserPromptSubmit` | 0 | 10 | 3 | 13 | `hooks/src/events/user_prompt_submit.rs` | `hooks/src/events/UserPromptSubmit.kt` |
| 601 | `ide_context.prompt` | `tui.src.idecontext.Prompt` | 0 | 13 | 0 | 13 | `tui/src/ide_context/prompt.rs` | `tui/src/idecontext/Prompt.kt` |
| 602 | `login.token_data_tests` | `login.src.TokenDataTests` | 0 | 12 | 1 | 13 | `login/src/token_data_tests.rs` | `login/src/TokenDataTests.kt` |
| 603 | `metrics.runtime_metrics` | `otel.src.metrics.RuntimeMetrics` | 0 | 11 | 2 | 13 | `otel/src/metrics/runtime_metrics.rs` | `otel/src/metrics/RuntimeMetrics.kt` |
| 604 | `multi_agents_v2.wait` | `core.src.tools.handlers.multiagentsv2.Wait` | 0 | 9 | 4 | 13 | `core/src/tools/handlers/multi_agents_v2/wait.rs` | `core/src/tools/handlers/multiagentsv2/Wait.kt` |
| 605 | `realtime-webrtc.native` | `realtimewebrtc.src.Native` | 0 | 10 | 3 | 13 | `realtime-webrtc/src/native.rs` | `realtimewebrtc/src/Native.kt` |
| 606 | `remote_control.enroll` | `appservertransport.src.transport.remotecontrol.Enroll` | 0 | 11 | 2 | 13 | `app-server-transport/src/transport/remote_control/enroll.rs` | `appservertransport/src/transport/remotecontrol/Enroll.kt` |
| 607 | `request_processors.fs_processor` | `appserver.src.requestprocessors.FsProcessor` | 0 | 12 | 1 | 13 | `app-server/src/request_processors/fs_processor.rs` | `appserver/src/requestprocessors/FsProcessor.kt` |
| 608 | `server.processor` | `execserver.src.server.Processor` | 0 | 12 | 1 | 13 | `exec-server/src/server/processor.rs` | `execserver/src/server/Processor.kt` |
| 609 | `suite.auth` | `appserver.tests.suite.Auth` | 0 | 13 | 0 | 13 | `app-server/tests/suite/auth.rs` | `appserver/tests/suite/Auth.kt` |
| 610 | `suite.model_switching` | `core.tests.suite.ModelSwitching` | 0 | 13 | 0 | 13 | `core/tests/suite/model_switching.rs` | `core/tests/suite/ModelSwitching.kt` |
| 611 | `suite.prompt_caching` | `core.tests.suite.PromptCaching` | 0 | 13 | 0 | 13 | `core/tests/suite/prompt_caching.rs` | `core/tests/suite/PromptCaching.kt` |
| 612 | `suite.review` | `core.tests.suite.Review` | 0 | 13 | 0 | 13 | `core/tests/suite/review.rs` | `core/tests/suite/Review.kt` |
| 613 | `tests.mcp_startup` | `tui.src.chatwidget.tests.McpStartup` | 0 | 13 | 0 | 13 | `tui/src/chatwidget/tests/mcp_startup.rs` | `tui/src/chatwidget/tests/McpStartup.kt` |
| 614 | `tests.side` | `tui.src.chatwidget.tests.Side` | 0 | 13 | 0 | 13 | `tui/src/chatwidget/tests/side.rs` | `tui/src/chatwidget/tests/Side.kt` |
| 615 | `tests.streamable_http_test_support` | `rmcpclient.tests.StreamableHttpTestSupport` | 0 | 12 | 1 | 13 | `rmcp-client/tests/streamable_http_test_support.rs` | `rmcpclient/tests/StreamableHttpTestSupport.kt` |
| 616 | `tools.responses_api` | `tools.src.ResponsesApi` | 0 | 7 | 6 | 13 | `tools/src/responses_api.rs` | `tools/src/ResponsesApi.kt` |
| 617 | `tui.mention_codec` | `tui.src.MentionCodec` | 0 | 11 | 2 | 13 | `tui/src/mention_codec.rs` | `tui/src/MentionCodec.kt` |
| 618 | `cloud-tasks-mock-client.mock` | `cloudtasksmockclient.src.Mock` | 46 | 11 | 1 | 12 | `cloud-tasks-mock-client/src/mock.rs` | `cloudtasksmockclient/src/Mock.kt` |
| 619 | `request_user_input.layout` | `tui.src.bottompane.requestuserinput.Layout` | 15 | 7 | 5 | 12 | `tui/src/bottom_pane/request_user_input/layout.rs` | `tui/src/bottompane/requestuserinput/Layout.kt` |
| 620 | `unix.stopwatch` | `shellescalation.src.unix.Stopwatch` | 3 | 10 | 2 | 12 | `shell-escalation/src/unix/stopwatch.rs` | `shellescalation/src/unix/Stopwatch.kt` |
| 621 | `app-server.connection_rpc_gate` | `appserver.src.ConnectionRpcGate` | 2 | 11 | 1 | 12 | `app-server/src/connection_rpc_gate.rs` | `appserver/src/ConnectionRpcGate.kt` |
| 622 | `feedback.feedback_diagnostics` | `feedback.src.FeedbackDiagnostics` | 2 | 10 | 2 | 12 | `feedback/src/feedback_diagnostics.rs` | `feedback/src/FeedbackDiagnostics.kt` |
| 623 | `bottom_pane.file_search_popup` | `tui.src.bottompane.FileSearchPopup` | 1 | 11 | 1 | 12 | `tui/src/bottom_pane/file_search_popup.rs` | `tui/src/bottompane/FileSearchPopup.kt` |
| 624 | `bottom_pane.pending_thread_approvals` | `tui.src.bottompane.PendingThreadApprovals` | 1 | 11 | 1 | 12 | `tui/src/bottom_pane/pending_thread_approvals.rs` | `tui/src/bottompane/PendingThreadApprovals.kt` |
| 625 | `exec-server.sandboxed_file_system` | `execserver.src.SandboxedFileSystem` | 1 | 11 | 1 | 12 | `exec-server/src/sandboxed_file_system.rs` | `execserver/src/SandboxedFileSystem.kt` |
| 626 | `rmcp-client.program_resolver` | `rmcpclient.src.ProgramResolver` | 1 | 11 | 1 | 12 | `rmcp-client/src/program_resolver.rs` | `rmcpclient/src/ProgramResolver.kt` |
| 627 | `app-server.fuzzy_file_search` | `appserver.src.FuzzyFileSearch` | 0 | 9 | 3 | 12 | `app-server/src/fuzzy_file_search.rs` | `appserver/src/FuzzyFileSearch.kt` |
| 628 | `auth.revoke` | `login.src.auth.Revoke` | 0 | 10 | 2 | 12 | `login/src/auth/revoke.rs` | `login/src/auth/Revoke.kt` |
| 629 | `chatwidget.reasoning_shortcuts` | `tui.src.chatwidget.ReasoningShortcuts` | 0 | 11 | 1 | 12 | `tui/src/chatwidget/reasoning_shortcuts.rs` | `tui/src/chatwidget/ReasoningShortcuts.kt` |
| 630 | `cli.config_override` | `utils.cli.src.ConfigOverride` | 0 | 11 | 1 | 12 | `utils/cli/src/config_override.rs` | `utils/cli/src/ConfigOverride.kt` |
| 631 | `context_manager.updates` | `core.src.contextmanager.Updates` | 0 | 12 | 0 | 12 | `core/src/context_manager/updates.rs` | `core/src/contextmanager/Updates.kt` |
| 632 | `core-plugins.marketplace_remove` | `coreplugins.src.MarketplaceRemove` | 0 | 9 | 3 | 12 | `core-plugins/src/marketplace_remove.rs` | `coreplugins/src/MarketplaceRemove.kt` |
| 633 | `core-plugins.marketplace_upgrade` | `coreplugins.src.marketplaceupgrade.MarketplaceUpgrade` | 0 | 9 | 3 | 12 | `core-plugins/src/marketplace_upgrade.rs` | `coreplugins/src/marketplaceupgrade/MarketplaceUpgrade.kt` |
| 634 | `core-plugins.remote_legacy` | `coreplugins.src.RemoteLegacy` | 0 | 8 | 4 | 12 | `core-plugins/src/remote_legacy.rs` | `coreplugins/src/RemoteLegacy.kt` |
| 635 | `core.compact_remote_v2` | `core.src.CompactRemoteV2` | 0 | 12 | 0 | 12 | `core/src/compact_remote_v2.rs` | `core/src/CompactRemoteV2.kt` |
| 636 | `core.environment_selection` | `core.src.EnvironmentSelection` | 0 | 11 | 1 | 12 | `core/src/environment_selection.rs` | `core/src/EnvironmentSelection.kt` |
| 637 | `core.windows_sandbox_tests` | `core.src.WindowsSandboxTests` | 0 | 12 | 0 | 12 | `core/src/windows_sandbox_tests.rs` | `core/src/WindowsSandboxTests.kt` |
| 638 | `events.permission_request` | `hooks.src.events.PermissionRequest` | 0 | 8 | 4 | 12 | `hooks/src/events/permission_request.rs` | `hooks/src/events/PermissionRequest.kt` |
| 639 | `exec.tests.suite.resume` | `exec.tests.suite.Resume` | 0 | 12 | 0 | 12 | `exec/tests/suite/resume.rs` | `exec/tests/suite/Resume.kt` |
| 640 | `model-provider.models_endpoint` | `modelprovider.src.ModelsEndpoint` | 0 | 10 | 2 | 12 | `model-provider/src/models_endpoint.rs` | `modelprovider/src/ModelsEndpoint.kt` |
| 641 | `models-manager.cache` | `modelsmanager.src.Cache` | 0 | 10 | 2 | 12 | `models-manager/src/cache.rs` | `modelsmanager/src/Cache.kt` |
| 642 | `multi_agents.resume_agent` | `core.src.tools.handlers.multiagents.ResumeAgent` | 0 | 8 | 4 | 12 | `core/src/tools/handlers/multi_agents/resume_agent.rs` | `core/src/tools/handlers/multiagents/ResumeAgent.kt` |
| 643 | `multi_agents.wait` | `core.src.tools.handlers.multiagents.Wait` | 0 | 8 | 4 | 12 | `core/src/tools/handlers/multi_agents/wait.rs` | `core/src/tools/handlers/multiagents/Wait.kt` |
| 644 | `multi_agents_v2.spawn` | `core.src.tools.handlers.multiagentsv2.Spawn` | 0 | 8 | 4 | 12 | `core/src/tools/handlers/multi_agents_v2/spawn.rs` | `core/src/tools/handlers/multiagentsv2/Spawn.kt` |
| 645 | `network-proxy.connect_policy` | `networkproxy.src.ConnectPolicy` | 0 | 7 | 5 | 12 | `network-proxy/src/connect_policy.rs` | `networkproxy/src/ConnectPolicy.kt` |
| 646 | `notifications.osc9` | `tui.src.notifications.Osc9` | 0 | 10 | 2 | 12 | `tui/src/notifications/osc9.rs` | `tui/src/notifications/Osc9.kt` |
| 647 | `onboarding.welcome` | `tui.src.onboarding.Welcome` | 0 | 11 | 1 | 12 | `tui/src/onboarding/welcome.rs` | `tui/src/onboarding/Welcome.kt` |
| 648 | `otel.otlp` | `otel.src.Otlp` | 0 | 12 | 0 | 12 | `otel/src/otlp.rs` | `otel/src/Otlp.kt` |
| 649 | `request_processors.external_agent_config_processor` | `appserver.src.requestprocessors.ExternalAgentConfigProcessor` | 0 | 11 | 1 | 12 | `app-server/src/request_processors/external_agent_config_processor.rs` | `appserver/src/requestprocessors/ExternalAgentConfigProcessor.kt` |
| 650 | `request_processors.thread_summary` | `appserver.src.requestprocessors.ThreadSummary` | 0 | 12 | 0 | 12 | `app-server/src/request_processors/thread_summary.rs` | `appserver/src/requestprocessors/ThreadSummary.kt` |
| 651 | `rollout.session_index` | `rollout.src.SessionIndex` | 0 | 11 | 1 | 12 | `rollout/src/session_index.rs` | `rollout/src/SessionIndex.kt` |
| 652 | `session.mcp` | `core.src.session.Mcp` | 0 | 12 | 0 | 12 | `core/src/session/mcp.rs` | `core/src/session/Mcp.kt` |
| 653 | `suite.device_code_login` | `login.tests.suite.DeviceCodeLogin` | 0 | 12 | 0 | 12 | `login/tests/suite/device_code_login.rs` | `login/tests/suite/DeviceCodeLogin.kt` |
| 654 | `suite.otel_export_routing_policy` | `otel.tests.suite.OtelExportRoutingPolicy` | 0 | 12 | 0 | 12 | `otel/tests/suite/otel_export_routing_policy.rs` | `otel/tests/suite/OtelExportRoutingPolicy.kt` |
| 655 | `tests.guardian` | `tui.src.chatwidget.tests.Guardian` | 0 | 12 | 0 | 12 | `tui/src/chatwidget/tests/guardian.rs` | `tui/src/chatwidget/tests/Guardian.kt` |
| 656 | `tests.model_catalog` | `tui.src.app.tests.ModelCatalog` | 0 | 12 | 0 | 12 | `tui/src/app/tests/model_catalog.rs` | `tui/src/app/tests/ModelCatalog.kt` |
| 657 | `tool.agents_tests` | `rollouttrace.src.reducer.tool.AgentsTests` | 0 | 11 | 1 | 12 | `rollout-trace/src/reducer/tool/agents_tests.rs` | `rollouttrace/src/reducer/tool/AgentsTests.kt` |
| 658 | `tools.tool_dispatch_trace_tests` | `core.src.tools.ToolDispatchTraceTests` | 0 | 10 | 2 | 12 | `core/src/tools/tool_dispatch_trace_tests.rs` | `core/src/tools/ToolDispatchTraceTests.kt` |
| 659 | `tui.audio_device` | `tui.src.AudioDevice` | 0 | 12 | 0 | 12 | `tui/src/audio_device.rs` | `tui/src/AudioDevice.kt` |
| 660 | `unified_exec.process_tests` | `core.src.unifiedexec.ProcessTests` | 0 | 11 | 1 | 12 | `core/src/unified_exec/process_tests.rs` | `core/src/unifiedexec/ProcessTests.kt` |
| 661 | `v2.dynamic_tools` | `appserver.tests.suite.v2.DynamicTools` | 0 | 12 | 0 | 12 | `app-server/tests/suite/v2/dynamic_tools.rs` | `appserver/tests/suite/v2/DynamicTools.kt` |
| 662 | `v2.plugin_share` | `appserver.tests.suite.v2.PluginShare` | 0 | 12 | 0 | 12 | `app-server/tests/suite/v2/plugin_share.rs` | `appserver/tests/suite/v2/PluginShare.kt` |
| 663 | `v2.thread_fork` | `appserver.tests.suite.v2.ThreadFork` | 0 | 12 | 0 | 12 | `app-server/tests/suite/v2/thread_fork.rs` | `appserver/tests/suite/v2/ThreadFork.kt` |
| 664 | `windows-sandbox-rs.firewall` | `windowssandboxrs.src.Firewall` | 0 | 11 | 1 | 12 | `windows-sandbox-rs/src/firewall.rs` | `windowssandboxrs/src/Firewall.kt` |
| 665 | `unified_exec.head_tail_buffer` | `core.src.unifiedexec.HeadTailBuffer` | 5 | 10 | 1 | 11 | `core/src/unified_exec/head_tail_buffer.rs` | `core/src/unifiedexec/HeadTailBuffer.kt` |
| 666 | `client.reqwest_http_client` | `execserver.src.client.ReqwestHttpClient` | 4 | 8 | 3 | 11 | `exec-server/src/client/reqwest_http_client.rs` | `execserver/src/client/ReqwestHttpClient.kt` |
| 667 | `app-server.transport` | `appserver.src.Transport` | 2 | 9 | 2 | 11 | `app-server/src/transport.rs` | `appserver/src/Transport.kt` |
| 668 | `bottom_pane.unified_exec_footer` | `tui.src.bottompane.UnifiedExecFooter` | 1 | 10 | 1 | 11 | `tui/src/bottom_pane/unified_exec_footer.rs` | `tui/src/bottompane/UnifiedExecFooter.kt` |
| 669 | `exec-server.remote_process` | `execserver.src.RemoteProcess` | 1 | 9 | 2 | 11 | `exec-server/src/remote_process.rs` | `execserver/src/RemoteProcess.kt` |
| 670 | `rmcp-client.logging_client_handler` | `rmcpclient.src.LoggingClientHandler` | 1 | 10 | 1 | 11 | `rmcp-client/src/logging_client_handler.rs` | `rmcpclient/src/LoggingClientHandler.kt` |
| 671 | `server.file_system_handler` | `execserver.src.server.FileSystemHandler` | 1 | 10 | 1 | 11 | `exec-server/src/server/file_system_handler.rs` | `execserver/src/server/FileSystemHandler.kt` |
| 672 | `suite.truncation` | `core.tests.suite.Truncation` | 1 | 11 | 0 | 11 | `core/tests/suite/truncation.rs` | `core/tests/suite/Truncation.kt` |
| 673 | `tui.external_editor` | `tui.src.ExternalEditor` | 1 | 9 | 2 | 11 | `tui/src/external_editor.rs` | `tui/src/ExternalEditor.kt` |
| 674 | `tui.session_log` | `tui.src.SessionLog` | 1 | 10 | 1 | 11 | `tui/src/session_log.rs` | `tui/src/SessionLog.kt` |
| 675 | `app-server.transport_tests` | `appserver.src.TransportTests` | 0 | 11 | 0 | 11 | `app-server/src/transport_tests.rs` | `appserver/src/TransportTests.kt` |
| 676 | `auth.external_bearer` | `login.src.auth.ExternalBearer` | 0 | 8 | 3 | 11 | `login/src/auth/external_bearer.rs` | `login/src/auth/ExternalBearer.kt` |
| 677 | `bottom_pane.status_surface_preview` | `tui.src.bottompane.StatusSurfacePreview` | 0 | 8 | 3 | 11 | `tui/src/bottom_pane/status_surface_preview.rs` | `tui/src/bottompane/StatusSurfacePreview.kt` |
| 678 | `config.cloud_requirements` | `config.src.CloudRequirements` | 0 | 8 | 3 | 11 | `config/src/cloud_requirements.rs` | `config/src/CloudRequirements.kt` |
| 679 | `endpoint.compact` | `codexapi.src.endpoint.Compact` | 0 | 8 | 3 | 11 | `codex-api/src/endpoint/compact.rs` | `codexapi/src/endpoint/Compact.kt` |
| 680 | `execpolicy.amend` | `execpolicy.src.Amend` | 0 | 10 | 1 | 11 | `execpolicy/src/amend.rs` | `execpolicy/src/Amend.kt` |
| 681 | `handlers.apply_patch_tests` | `core.src.tools.handlers.ApplyPatchTests` | 0 | 11 | 0 | 11 | `core/src/tools/handlers/apply_patch_tests.rs` | `core/src/tools/handlers/ApplyPatchTests.kt` |
| 682 | `handlers.request_plugin_install` | `core.src.tools.handlers.RequestPluginInstall` | 0 | 9 | 2 | 11 | `core/src/tools/handlers/request_plugin_install.rs` | `core/src/tools/handlers/RequestPluginInstall.kt` |
| 683 | `mcp.mod_tests` | `codexmcp.src.mcp.ModTests` | 0 | 11 | 0 | 11 | `codex-mcp/src/mcp/mod_tests.rs` | `codexmcp/src/mcp/ModTests.kt` |
| 684 | `multi_agents.close_agent` | `core.src.tools.handlers.multiagents.CloseAgent` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents/close_agent.rs` | `core/src/tools/handlers/multiagents/CloseAgent.kt` |
| 685 | `multi_agents.send_input` | `core.src.tools.handlers.multiagents.SendInput` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents/send_input.rs` | `core/src/tools/handlers/multiagents/SendInput.kt` |
| 686 | `multi_agents.spawn` | `core.src.tools.handlers.multiagents.Spawn` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents/spawn.rs` | `core/src/tools/handlers/multiagents/Spawn.kt` |
| 687 | `multi_agents_v2.close_agent` | `core.src.tools.handlers.multiagentsv2.CloseAgent` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents_v2/close_agent.rs` | `core/src/tools/handlers/multiagentsv2/CloseAgent.kt` |
| 688 | `multi_agents_v2.list_agents` | `core.src.tools.handlers.multiagentsv2.ListAgents` | 0 | 7 | 4 | 11 | `core/src/tools/handlers/multi_agents_v2/list_agents.rs` | `core/src/tools/handlers/multiagentsv2/ListAgents.kt` |
| 689 | `network-proxy.state` | `networkproxy.src.State` | 0 | 7 | 4 | 11 | `network-proxy/src/state.rs` | `networkproxy/src/State.kt` |
| 690 | `protocol.item_builders` | `appserverprotocol.src.protocol.ItemBuilders` | 0 | 11 | 0 | 11 | `app-server-protocol/src/protocol/item_builders.rs` | `appserverprotocol/src/protocol/ItemBuilders.kt` |
| 691 | `rollout-trace.protocol_event` | `rollouttrace.src.ProtocolEvent` | 0 | 7 | 4 | 11 | `rollout-trace/src/protocol_event.rs` | `rollouttrace/src/ProtocolEvent.kt` |
| 692 | `sandboxing.manager_tests` | `sandboxing.src.ManagerTests` | 0 | 11 | 0 | 11 | `sandboxing/src/manager_tests.rs` | `sandboxing/src/ManagerTests.kt` |
| 693 | `session.config_lock` | `core.src.session.ConfigLock` | 0 | 11 | 0 | 11 | `core/src/session/config_lock.rs` | `core/src/session/ConfigLock.kt` |
| 694 | `suite.codex_tool` | `mcpserver.tests.suite.CodexTool` | 0 | 10 | 1 | 11 | `mcp-server/tests/suite/codex_tool.rs` | `mcpserver/tests/suite/CodexTool.kt` |
| 695 | `suite.remote_env` | `core.tests.suite.RemoteEnv` | 0 | 11 | 0 | 11 | `core/tests/suite/remote_env.rs` | `core/tests/suite/RemoteEnv.kt` |
| 696 | `suite.rollout_list_find` | `core.tests.suite.RolloutListFind` | 0 | 11 | 0 | 11 | `core/tests/suite/rollout_list_find.rs` | `core/tests/suite/RolloutListFind.kt` |
| 697 | `suite.sandbox` | `exec.tests.suite.Sandbox` | 0 | 11 | 0 | 11 | `exec/tests/suite/sandbox.rs` | `exec/tests/suite/Sandbox.kt` |
| 698 | `tests.guardian_tests` | `core.src.session.tests.GuardianTests` | 0 | 9 | 2 | 11 | `core/src/session/tests/guardian_tests.rs` | `core/src/session/tests/GuardianTests.kt` |
| 699 | `tools.tool_registry_plan_types` | `tools.src.ToolRegistryPlanTypes` | 0 | 4 | 7 | 11 | `tools/src/tool_registry_plan_types.rs` | `tools/src/ToolRegistryPlanTypes.kt` |
| 700 | `transport.unix_socket_tests` | `appservertransport.src.transport.UnixSocketTests` | 0 | 11 | 0 | 11 | `app-server-transport/src/transport/unix_socket_tests.rs` | `appservertransport/src/transport/UnixSocketTests.kt` |
| 701 | `tui.job_control` | `tui.src.tui.JobControl` | 0 | 8 | 3 | 11 | `tui/src/tui/job_control.rs` | `tui/src/tui/JobControl.kt` |
| 702 | `tui.motion` | `tui.src.Motion` | 0 | 9 | 2 | 11 | `tui/src/motion.rs` | `tui/src/Motion.kt` |
| 703 | `unified_exec.process_manager_tests` | `core.src.unifiedexec.ProcessManagerTests` | 0 | 11 | 0 | 11 | `core/src/unified_exec/process_manager_tests.rs` | `core/src/unifiedexec/ProcessManagerTests.kt` |
| 704 | `v2.experimental_feature_list` | `appserver.tests.suite.v2.ExperimentalFeatureList` | 0 | 11 | 0 | 11 | `app-server/tests/suite/v2/experimental_feature_list.rs` | `appserver/tests/suite/v2/ExperimentalFeatureList.kt` |
| 705 | `windows-sandbox-rs.elevated_impl` | `windowssandboxrs.src.ElevatedImpl` | 0 | 9 | 2 | 11 | `windows-sandbox-rs/src/elevated_impl.rs` | `windowssandboxrs/src/ElevatedImpl.kt` |
| 706 | `execpolicy-legacy.valid_exec` | `execpolicylegacy.src.ValidExec` | 10 | 6 | 4 | 10 | `execpolicy-legacy/src/valid_exec.rs` | `execpolicylegacy/src/ValidExec.kt` |
| 707 | `model.thread_goal` | `state.src.model.ThreadGoal` | 8 | 6 | 4 | 10 | `state/src/model/thread_goal.rs` | `state/src/model/ThreadGoal.kt` |
| 708 | `rollout.metadata` | `rollout.src.Metadata` | 6 | 9 | 1 | 10 | `rollout/src/metadata.rs` | `rollout/src/Metadata.kt` |
| 709 | `windows-sandbox-rs.logging` | `windowssandboxrs.src.Logging` | 2 | 10 | 0 | 10 | `windows-sandbox-rs/src/logging.rs` | `windowssandboxrs/src/Logging.kt` |
| 710 | `config.host_name` | `config.src.HostName` | 1 | 10 | 0 | 10 | `config/src/host_name.rs` | `config/src/HostName.kt` |
| 711 | `core.review_prompts` | `core.src.ReviewPrompts` | 1 | 9 | 1 | 10 | `core/src/review_prompts.rs` | `core/src/ReviewPrompts.kt` |
| 712 | `shell.zsh_fork_backend` | `core.src.tools.runtimes.shell.ZshForkBackend` | 1 | 8 | 2 | 10 | `core/src/tools/runtimes/shell/zsh_fork_backend.rs` | `core/src/tools/runtimes/shell/ZshForkBackend.kt` |
| 713 | `tui.npm_registry` | `tui.src.NpmRegistry` | 1 | 7 | 3 | 10 | `tui/src/npm_registry.rs` | `tui/src/NpmRegistry.kt` |
| 714 | `tui.updates` | `tui.src.Updates` | 1 | 7 | 3 | 10 | `tui/src/updates.rs` | `tui/src/Updates.kt` |
| 715 | `app-server.app_server_tracing` | `appserver.src.AppServerTracing` | 0 | 10 | 0 | 10 | `app-server/src/app_server_tracing.rs` | `appserver/src/AppServerTracing.kt` |
| 716 | `chatwidget.ide_context` | `tui.src.chatwidget.IdeContext` | 0 | 9 | 1 | 10 | `tui/src/chatwidget/ide_context.rs` | `tui/src/chatwidget/IdeContext.kt` |
| 717 | `codex-api.provider` | `codexapi.src.Provider` | 0 | 8 | 2 | 10 | `codex-api/src/provider.rs` | `codexapi/src/Provider.kt` |
| 718 | `codex-client.transport` | `codexclient.src.Transport` | 0 | 6 | 4 | 10 | `codex-client/src/transport.rs` | `codexclient/src/Transport.kt` |
| 719 | `context.environment_context_tests` | `core.src.context.EnvironmentContextTests` | 0 | 10 | 0 | 10 | `core/src/context/environment_context_tests.rs` | `core/src/context/EnvironmentContextTests.kt` |
| 720 | `core.agents_md` | `core.src.AgentsMd` | 0 | 8 | 2 | 10 | `core/src/agents_md.rs` | `core/src/AgentsMd.kt` |
| 721 | `core.compact_remote` | `core.src.CompactRemote` | 0 | 9 | 1 | 10 | `core/src/compact_remote.rs` | `core/src/CompactRemote.kt` |
| 722 | `core.mcp_skill_dependencies` | `core.src.McpSkillDependencies` | 0 | 10 | 0 | 10 | `core/src/mcp_skill_dependencies.rs` | `core/src/McpSkillDependencies.kt` |
| 723 | `core.shell_tests` | `core.src.ShellTests` | 0 | 10 | 0 | 10 | `core/src/shell_tests.rs` | `core/src/ShellTests.kt` |
| 724 | `core.turn_diff_tracker_tests` | `core.src.TurnDiffTrackerTests` | 0 | 10 | 0 | 10 | `core/src/turn_diff_tracker_tests.rs` | `core/src/TurnDiffTrackerTests.kt` |
| 725 | `external-agent-sessions.ledger` | `externalagentsessions.src.Ledger` | 0 | 8 | 2 | 10 | `external-agent-sessions/src/ledger.rs` | `externalagentsessions/src/Ledger.kt` |
| 726 | `git-utils.branch` | `gitutils.src.Branch` | 0 | 10 | 0 | 10 | `git-utils/src/branch.rs` | `gitutils/src/Branch.kt` |
| 727 | `loader.macos` | `config.src.loader.Macos` | 0 | 9 | 1 | 10 | `config/src/loader/macos.rs` | `config/src/loader/Macos.kt` |
| 728 | `local.helpers` | `threadstore.src.local.Helpers` | 0 | 10 | 0 | 10 | `thread-store/src/local/helpers.rs` | `threadstore/src/local/Helpers.kt` |
| 729 | `model.memories` | `state.src.model.Memories` | 0 | 3 | 7 | 10 | `state/src/model/memories.rs` | `state/src/model/Memories.kt` |
| 730 | `onboarding.trust_directory` | `tui.src.onboarding.TrustDirectory` | 0 | 8 | 2 | 10 | `tui/src/onboarding/trust_directory.rs` | `tui/src/onboarding/TrustDirectory.kt` |
| 731 | `plugins.discoverable_tests` | `core.src.plugins.DiscoverableTests` | 0 | 10 | 0 | 10 | `core/src/plugins/discoverable_tests.rs` | `core/src/plugins/DiscoverableTests.kt` |
| 732 | `plugins.mentions_tests` | `core.src.plugins.MentionsTests` | 0 | 10 | 0 | 10 | `core/src/plugins/mentions_tests.rs` | `core/src/plugins/MentionsTests.kt` |
| 733 | `reducer.thread` | `rollouttrace.src.reducer.Thread` | 0 | 8 | 2 | 10 | `rollout-trace/src/reducer/thread.rs` | `rollouttrace/src/reducer/Thread.kt` |
| 734 | `request_processors.command_exec_processor` | `appserver.src.requestprocessors.CommandExecProcessor` | 0 | 9 | 1 | 10 | `app-server/src/request_processors/command_exec_processor.rs` | `appserver/src/requestprocessors/CommandExecProcessor.kt` |
| 735 | `rollout-trace.writer` | `rollouttrace.src.Writer` | 0 | 8 | 2 | 10 | `rollout-trace/src/writer.rs` | `rollouttrace/src/Writer.kt` |
| 736 | `rollout.session_index_tests` | `rollout.src.SessionIndexTests` | 0 | 10 | 0 | 10 | `rollout/src/session_index_tests.rs` | `rollout/src/SessionIndexTests.kt` |
| 737 | `runtime.callbacks` | `codemode.src.runtime.Callbacks` | 0 | 10 | 0 | 10 | `code-mode/src/runtime/callbacks.rs` | `codemode/src/runtime/Callbacks.kt` |
| 738 | `sandboxing.bwrap_tests` | `sandboxing.src.BwrapTests` | 0 | 10 | 0 | 10 | `sandboxing/src/bwrap_tests.rs` | `sandboxing/src/BwrapTests.kt` |
| 739 | `share.local_paths` | `coreplugins.src.remote.share.LocalPaths` | 0 | 9 | 1 | 10 | `core-plugins/src/remote/share/local_paths.rs` | `coreplugins/src/remote/share/LocalPaths.kt` |
| 740 | `sleep-inhibitor.linux_inhibitor` | `utils.sleepinhibitor.src.LinuxInhibitor` | 0 | 7 | 3 | 10 | `utils/sleep-inhibitor/src/linux_inhibitor.rs` | `utils/sleepinhibitor/src/LinuxInhibitor.kt` |
| 741 | `sleep-inhibitor.macos` | `utils.sleepinhibitor.src.Macos` | 0 | 5 | 5 | 10 | `utils/sleep-inhibitor/src/macos.rs` | `utils/sleepinhibitor/src/Macos.kt` |
| 742 | `status.format` | `tui.src.status.Format` | 0 | 9 | 1 | 10 | `tui/src/status/format.rs` | `tui/src/status/Format.kt` |
| 743 | `streaming.commit_tick` | `tui.src.streaming.CommitTick` | 0 | 8 | 2 | 10 | `tui/src/streaming/commit_tick.rs` | `tui/src/streaming/CommitTick.kt` |
| 744 | `suite.managed_proxy` | `linuxsandbox.tests.suite.ManagedProxy` | 0 | 10 | 0 | 10 | `linux-sandbox/tests/suite/managed_proxy.rs` | `linuxsandbox/tests/suite/ManagedProxy.kt` |
| 745 | `suite.model_visible_layout` | `core.tests.suite.ModelVisibleLayout` | 0 | 10 | 0 | 10 | `core/tests/suite/model_visible_layout.rs` | `core/tests/suite/ModelVisibleLayout.kt` |
| 746 | `suite.models_cache_ttl` | `core.tests.suite.ModelsCacheTtl` | 0 | 9 | 1 | 10 | `core/tests/suite/models_cache_ttl.rs` | `core/tests/suite/ModelsCacheTtl.kt` |
| 747 | `suite.skill_approval` | `core.tests.suite.SkillApproval` | 0 | 10 | 0 | 10 | `core/tests/suite/skill_approval.rs` | `core/tests/suite/SkillApproval.kt` |
| 748 | `suite.vt100_history` | `tui.tests.suite.Vt100History` | 0 | 9 | 1 | 10 | `tui/tests/suite/vt100_history.rs` | `tui/tests/suite/Vt100History.kt` |
| 749 | `tools.code_mode` | `tools.src.CodeMode` | 0 | 10 | 0 | 10 | `tools/src/code_mode.rs` | `tools/src/CodeMode.kt` |
| 750 | `truncate.tests` | `utils.string.src.truncate.Tests` | 0 | 10 | 0 | 10 | `utils/string/src/truncate/tests.rs` | `utils/string/src/truncate/Tests.kt` |
| 751 | `tui.file_search` | `tui.src.FileSearch` | 0 | 7 | 3 | 10 | `tui/src/file_search.rs` | `tui/src/FileSearch.kt` |
| 752 | `tui.local_chatgpt_auth` | `tui.src.LocalChatgptAuth` | 0 | 8 | 2 | 10 | `tui/src/local_chatgpt_auth.rs` | `tui/src/LocalChatgptAuth.kt` |
| 753 | `tui.update_versions` | `tui.src.UpdateVersions` | 0 | 10 | 0 | 10 | `tui/src/update_versions.rs` | `tui/src/UpdateVersions.kt` |
| 754 | `unix.escalate_protocol` | `shellescalation.src.unix.EscalateProtocol` | 0 | 3 | 7 | 10 | `shell-escalation/src/unix/escalate_protocol.rs` | `shellescalation/src/unix/EscalateProtocol.kt` |
| 755 | `v2.compaction` | `appserver.tests.suite.v2.Compaction` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/compaction.rs` | `appserver/tests/suite/v2/Compaction.kt` |
| 756 | `v2.experimental_api` | `appserver.tests.suite.v2.ExperimentalApi` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/experimental_api.rs` | `appserver/tests/suite/v2/ExperimentalApi.kt` |
| 757 | `v2.external_agent_config` | `appserver.tests.suite.v2.ExternalAgentConfig` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/external_agent_config.rs` | `appserver/tests/suite/v2/ExternalAgentConfig.kt` |
| 758 | `v2.mcp_tool` | `appserver.tests.suite.v2.McpTool` | 0 | 9 | 1 | 10 | `app-server/tests/suite/v2/mcp_tool.rs` | `appserver/tests/suite/v2/McpTool.kt` |
| 759 | `v2.rate_limits` | `appserver.tests.suite.v2.RateLimits` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/rate_limits.rs` | `appserver/tests/suite/v2/RateLimits.kt` |
| 760 | `v2.review` | `appserver.tests.suite.v2.Review` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/review.rs` | `appserver/tests/suite/v2/Review.kt` |
| 761 | `v2.safety_check_downgrade` | `appserver.tests.suite.v2.SafetyCheckDowngrade` | 0 | 10 | 0 | 10 | `app-server/tests/suite/v2/safety_check_downgrade.rs` | `appserver/tests/suite/v2/SafetyCheckDowngrade.kt` |
| 762 | `windows-sandbox-rs.winutil` | `windowssandboxrs.src.Winutil` | 0 | 10 | 0 | 10 | `windows-sandbox-rs/src/winutil.rs` | `windowssandboxrs/src/Winutil.kt` |
| 763 | `runtime.value` | `codemode.src.runtime.Value` | 152 | 9 | 0 | 9 | `code-mode/src/runtime/value.rs` | `codemode/src/runtime/Value.kt` |
| 764 | `tui.token_usage` | `tui.src.TokenUsage` | 31 | 7 | 2 | 9 | `tui/src/token_usage.rs` | `tui/src/TokenUsage.kt` |
| 765 | `execpolicy-legacy.arg_matcher` | `execpolicylegacy.src.ArgMatcher` | 7 | 5 | 4 | 9 | `execpolicy-legacy/src/arg_matcher.rs` | `execpolicylegacy/src/ArgMatcher.kt` |
| 766 | `common.apps_test_server` | `core.tests.common.AppsTestServer` | 6 | 7 | 2 | 9 | `core/tests/common/apps_test_server.rs` | `core/tests/common/AppsTestServer.kt` |
| 767 | `local.live_writer` | `threadstore.src.local.LiveWriter` | 2 | 9 | 0 | 9 | `thread-store/src/local/live_writer.rs` | `threadstore/src/local/LiveWriter.kt` |
| 768 | `pty.pipe` | `utils.pty.src.Pipe` | 1 | 7 | 2 | 9 | `utils/pty/src/pipe.rs` | `utils/pty/src/Pipe.kt` |
| 769 | `suite.logout` | `login.tests.suite.Logout` | 1 | 8 | 1 | 9 | `login/tests/suite/logout.rs` | `login/tests/suite/Logout.kt` |
| 770 | `amazon_bedrock.mantle` | `modelprovider.src.amazonbedrock.Mantle` | 0 | 9 | 0 | 9 | `model-provider/src/amazon_bedrock/mantle.rs` | `modelprovider/src/amazonbedrock/Mantle.kt` |
| 771 | `app-server-protocol.jsonrpc_lite` | `appserverprotocol.src.JsonrpcLite` | 0 | 1 | 8 | 9 | `app-server-protocol/src/jsonrpc_lite.rs` | `appserverprotocol/src/JsonrpcLite.kt` |
| 772 | `auth.headless_chatgpt_login` | `tui.src.onboarding.auth.HeadlessChatgptLogin` | 0 | 9 | 0 | 9 | `tui/src/onboarding/auth/headless_chatgpt_login.rs` | `tui/src/onboarding/auth/HeadlessChatgptLogin.kt` |
| 773 | `bin.rmcp_test_server` | `rmcpclient.src.bin.RmcpTestServer` | 0 | 7 | 2 | 9 | `rmcp-client/src/bin/rmcp_test_server.rs` | `rmcpclient/src/bin/RmcpTestServer.kt` |
| 774 | `cloud-tasks.cli` | `cloudtasks.src.Cli` | 0 | 2 | 7 | 9 | `cloud-tasks/src/cli.rs` | `cloudtasks/src/Cli.kt` |
| 775 | `cloud-tasks.env_detect` | `cloudtasks.src.EnvDetect` | 0 | 7 | 2 | 9 | `cloud-tasks/src/env_detect.rs` | `cloudtasks/src/EnvDetect.kt` |
| 776 | `codex-api.api_bridge_tests` | `codexapi.src.ApiBridgeTests` | 0 | 9 | 0 | 9 | `codex-api/src/api_bridge_tests.rs` | `codexapi/src/ApiBridgeTests.kt` |
| 777 | `core-skills.invocation_utils` | `coreskills.src.InvocationUtils` | 0 | 9 | 0 | 9 | `core-skills/src/invocation_utils.rs` | `coreskills/src/InvocationUtils.kt` |
| 778 | `core.personality_migration_tests` | `core.src.PersonalityMigrationTests` | 0 | 9 | 0 | 9 | `core/src/personality_migration_tests.rs` | `core/src/PersonalityMigrationTests.kt` |
| 779 | `core.realtime_conversation_tests` | `core.src.RealtimeConversationTests` | 0 | 9 | 0 | 9 | `core/src/realtime_conversation_tests.rs` | `core/src/RealtimeConversationTests.kt` |
| 780 | `core.safety_tests` | `core.src.SafetyTests` | 0 | 9 | 0 | 9 | `core/src/safety_tests.rs` | `core/src/SafetyTests.kt` |
| 781 | `desktop_app.windows` | `cli.src.desktopapp.Windows` | 0 | 9 | 0 | 9 | `cli/src/desktop_app/windows.rs` | `cli/src/desktopapp/Windows.kt` |
| 782 | `exec-server.connection` | `execserver.src.Connection` | 0 | 7 | 2 | 9 | `exec-server/src/connection.rs` | `execserver/src/Connection.kt` |
| 783 | `execpolicy-legacy.main` | `execpolicylegacy.src.Main` | 0 | 4 | 5 | 9 | `execpolicy-legacy/src/main.rs` | `execpolicylegacy/src/Main.kt` |
| 784 | `execpolicy-legacy.program` | `execpolicylegacy.src.Program` | 0 | 4 | 5 | 9 | `execpolicy-legacy/src/program.rs` | `execpolicylegacy/src/Program.kt` |
| 785 | `handlers.list_dir_tests` | `core.src.tools.handlers.ListDirTests` | 0 | 9 | 0 | 9 | `core/src/tools/handlers/list_dir_tests.rs` | `core/src/tools/handlers/ListDirTests.kt` |
| 786 | `handlers.shell_tests` | `core.src.tools.handlers.ShellTests` | 0 | 9 | 0 | 9 | `core/src/tools/handlers/shell_tests.rs` | `core/src/tools/handlers/ShellTests.kt` |
| 787 | `model.conversation` | `rollouttrace.src.model.Conversation` | 0 | 0 | 9 | 9 | `rollout-trace/src/model/conversation.rs` | `rollouttrace/src/model/Conversation.kt` |
| 788 | `path-utils.path_utils_tests` | `utils.pathutils.src.PathUtilsTests` | 0 | 9 | 0 | 9 | `utils/path-utils/src/path_utils_tests.rs` | `utils/pathutils/src/PathUtilsTests.kt` |
| 789 | `protocol.exec_output_tests` | `protocol.src.ExecOutputTests` | 0 | 9 | 0 | 9 | `protocol/src/exec_output_tests.rs` | `protocol/src/ExecOutputTests.kt` |
| 790 | `request_processors.marketplace_processor` | `appserver.src.requestprocessors.MarketplaceProcessor` | 0 | 8 | 1 | 9 | `app-server/src/request_processors/marketplace_processor.rs` | `appserver/src/requestprocessors/MarketplaceProcessor.kt` |
| 791 | `runtime.backfill` | `state.src.runtime.Backfill` | 0 | 9 | 0 | 9 | `state/src/runtime/backfill.rs` | `state/src/runtime/Backfill.kt` |
| 792 | `sandboxing.bwrap` | `sandboxing.src.Bwrap` | 0 | 9 | 0 | 9 | `sandboxing/src/bwrap.rs` | `sandboxing/src/Bwrap.kt` |
| 793 | `server.transport_tests` | `execserver.src.server.TransportTests` | 0 | 9 | 0 | 9 | `exec-server/src/server/transport_tests.rs` | `execserver/src/server/TransportTests.kt` |
| 794 | `stream-parser.assistant_text` | `utils.streamparser.src.AssistantText` | 0 | 7 | 2 | 9 | `utils/stream-parser/src/assistant_text.rs` | `utils/streamparser/src/AssistantText.kt` |
| 795 | `suite.cli_stream` | `core.tests.suite.CliStream` | 0 | 9 | 0 | 9 | `core/tests/suite/cli_stream.rs` | `core/tests/suite/CliStream.kt` |
| 796 | `suite.login_server_e2e` | `login.tests.suite.LoginServerE2e` | 0 | 8 | 1 | 9 | `login/tests/suite/login_server_e2e.rs` | `login/tests/suite/LoginServerE2e.kt` |
| 797 | `suite.ls` | `execpolicylegacy.tests.suite.Ls` | 0 | 9 | 0 | 9 | `execpolicy-legacy/tests/suite/ls.rs` | `execpolicylegacy/tests/suite/Ls.kt` |
| 798 | `suite.request_user_input` | `core.tests.suite.RequestUserInput` | 0 | 9 | 0 | 9 | `core/tests/suite/request_user_input.rs` | `core/tests/suite/RequestUserInput.kt` |
| 799 | `suite.tool_parallelism` | `core.tests.suite.ToolParallelism` | 0 | 9 | 0 | 9 | `core/tests/suite/tool_parallelism.rs` | `core/tests/suite/ToolParallelism.kt` |
| 800 | `tests.realtime_websocket_e2e` | `codexapi.tests.RealtimeWebsocketE2e` | 0 | 8 | 1 | 9 | `codex-api/tests/realtime_websocket_e2e.rs` | `codexapi/tests/RealtimeWebsocketE2e.kt` |
| 801 | `tests.sse_end_to_end` | `codexapi.tests.SseEndToEnd` | 0 | 7 | 2 | 9 | `codex-api/tests/sse_end_to_end.rs` | `codexapi/tests/SseEndToEnd.kt` |
| 802 | `tui.resize_reflow_cap` | `tui.src.ResizeReflowCap` | 0 | 9 | 0 | 9 | `tui/src/resize_reflow_cap.rs` | `tui/src/ResizeReflowCap.kt` |
| 803 | `v2.mcp_server_elicitation` | `appserver.tests.suite.v2.McpServerElicitation` | 0 | 7 | 2 | 9 | `app-server/tests/suite/v2/mcp_server_elicitation.rs` | `appserver/tests/suite/v2/McpServerElicitation.kt` |
| 804 | `v2.thread_shell_command` | `appserver.tests.suite.v2.ThreadShellCommand` | 0 | 9 | 0 | 9 | `app-server/tests/suite/v2/thread_shell_command.rs` | `appserver/tests/suite/v2/ThreadShellCommand.kt` |
| 805 | `windows-sandbox-rs.process` | `windowssandboxrs.src.Process` | 0 | 5 | 4 | 9 | `windows-sandbox-rs/src/process.rs` | `windowssandboxrs/src/Process.kt` |
| 806 | `windows-sandbox-rs.ssh_config_dependencies` | `windowssandboxrs.src.SshConfigDependencies` | 0 | 9 | 0 | 9 | `windows-sandbox-rs/src/ssh_config_dependencies.rs` | `windowssandboxrs/src/SshConfigDependencies.kt` |
| 807 | `string.json` | `utils.string.src.Json` | 183 | 5 | 3 | 8 | `utils/string/src/json.rs` | `utils/string/src/Json.kt` |
| 808 | `protocol.tool_name` | `protocol.src.ToolName` | 26 | 7 | 1 | 8 | `protocol/src/tool_name.rs` | `protocol/src/ToolName.kt` |
| 809 | `rollout.policy` | `rollout.src.Policy` | 22 | 7 | 1 | 8 | `rollout/src/policy.rs` | `rollout/src/Policy.kt` |
| 810 | `app.thread_session_state` | `tui.src.app.ThreadSessionState` | 4 | 8 | 0 | 8 | `tui/src/app/thread_session_state.rs` | `tui/src/app/ThreadSessionState.kt` |
| 811 | `core.skills_watcher` | `core.src.SkillsWatcher` | 3 | 6 | 2 | 8 | `core/src/skills_watcher.rs` | `core/src/SkillsWatcher.kt` |
| 812 | `execpolicy-legacy.opt` | `execpolicylegacy.src.Opt` | 3 | 4 | 4 | 8 | `execpolicy-legacy/src/opt.rs` | `execpolicylegacy/src/Opt.kt` |
| 813 | `protocol.shell_environment` | `protocol.src.ShellEnvironment` | 3 | 8 | 0 | 8 | `protocol/src/shell_environment.rs` | `protocol/src/ShellEnvironment.kt` |
| 814 | `chatgpt.workspace_settings` | `chatgpt.src.WorkspaceSettings` | 2 | 4 | 4 | 8 | `chatgpt/src/workspace_settings.rs` | `chatgpt/src/WorkspaceSettings.kt` |
| 815 | `execpolicy-legacy.execv_checker` | `execpolicylegacy.src.ExecvChecker` | 1 | 7 | 1 | 8 | `execpolicy-legacy/src/execv_checker.rs` | `execpolicylegacy/src/ExecvChecker.kt` |
| 816 | `server.process_handler` | `execserver.src.server.ProcessHandler` | 1 | 7 | 1 | 8 | `exec-server/src/server/process_handler.rs` | `execserver/src/server/ProcessHandler.kt` |
| 817 | `tui.ascii_animation` | `tui.src.AsciiAnimation` | 1 | 7 | 1 | 8 | `tui/src/ascii_animation.rs` | `tui/src/AsciiAnimation.kt` |
| 818 | `tui.auto_review_denials` | `tui.src.AutoReviewDenials` | 1 | 7 | 1 | 8 | `tui/src/auto_review_denials.rs` | `tui/src/AutoReviewDenials.kt` |
| 819 | `app.input` | `tui.src.app.Input` | 0 | 8 | 0 | 8 | `tui/src/app/input.rs` | `tui/src/app/Input.kt` |
| 820 | `backends.legacy` | `windowssandboxrs.src.unifiedexec.backends.Legacy` | 0 | 7 | 1 | 8 | `windows-sandbox-rs/src/unified_exec/backends/legacy.rs` | `windowssandboxrs/src/unifiedexec/backends/Legacy.kt` |
| 821 | `chatwidget.keymap_picker` | `tui.src.chatwidget.KeymapPicker` | 0 | 8 | 0 | 8 | `tui/src/chatwidget/keymap_picker.rs` | `tui/src/chatwidget/KeymapPicker.kt` |
| 822 | `cloud-tasks.util` | `cloudtasks.src.Util` | 0 | 8 | 0 | 8 | `cloud-tasks/src/util.rs` | `cloudtasks/src/Util.kt` |
| 823 | `codex-api.auth` | `codexapi.src.Auth` | 0 | 4 | 4 | 8 | `codex-api/src/auth.rs` | `codexapi/src/Auth.kt` |
| 824 | `config.template_interpolation` | `core.src.config.TemplateInterpolation` | 0 | 8 | 0 | 8 | `core/src/config/template_interpolation.rs` | `core/src/config/TemplateInterpolation.kt` |
| 825 | `context.fragment` | `core.src.context.Fragment` | 0 | 5 | 3 | 8 | `core/src/context/fragment.rs` | `core/src/context/Fragment.kt` |
| 826 | `core.mcp_openai_file` | `core.src.McpOpenaiFile` | 0 | 8 | 0 | 8 | `core/src/mcp_openai_file.rs` | `core/src/McpOpenaiFile.kt` |
| 827 | `core.mcp_tool_exposure_test` | `core.src.McpToolExposureTest` | 0 | 8 | 0 | 8 | `core/src/mcp_tool_exposure_test.rs` | `core/src/McpToolExposureTest.kt` |
| 828 | `core.sandbox_tags_tests` | `core.src.SandboxTagsTests` | 0 | 8 | 0 | 8 | `core/src/sandbox_tags_tests.rs` | `core/src/SandboxTagsTests.kt` |
| 829 | `core.session_startup_prewarm` | `core.src.SessionStartupPrewarm` | 0 | 6 | 2 | 8 | `core/src/session_startup_prewarm.rs` | `core/src/SessionStartupPrewarm.kt` |
| 830 | `core.thread_rollout_truncation` | `core.src.ThreadRolloutTruncation` | 0 | 8 | 0 | 8 | `core/src/thread_rollout_truncation.rs` | `core/src/ThreadRolloutTruncation.kt` |
| 831 | `elevated.runner_client` | `windowssandboxrs.src.elevated.RunnerClient` | 0 | 7 | 1 | 8 | `windows-sandbox-rs/src/elevated/runner_client.rs` | `windowssandboxrs/src/elevated/RunnerClient.kt` |
| 832 | `endpoint.session` | `codexapi.src.endpoint.Session` | 0 | 7 | 1 | 8 | `codex-api/src/endpoint/session.rs` | `codexapi/src/endpoint/Session.kt` |
| 833 | `features.legacy` | `features.src.Legacy` | 0 | 6 | 2 | 8 | `features/src/legacy.rs` | `features/src/Legacy.kt` |
| 834 | `git-utils.operations` | `gitutils.src.Operations` | 0 | 7 | 1 | 8 | `git-utils/src/operations.rs` | `gitutils/src/Operations.kt` |
| 835 | `handlers.mcp_resource_tests` | `core.src.tools.handlers.McpResourceTests` | 0 | 8 | 0 | 8 | `core/src/tools/handlers/mcp_resource_tests.rs` | `core/src/tools/handlers/McpResourceTests.kt` |
| 836 | `protocol.dynamic_tools` | `protocol.src.DynamicTools` | 0 | 3 | 5 | 8 | `protocol/src/dynamic_tools.rs` | `protocol/src/DynamicTools.kt` |
| 837 | `protocol.request_permissions` | `protocol.src.RequestPermissions` | 0 | 3 | 5 | 8 | `protocol/src/request_permissions.rs` | `protocol/src/RequestPermissions.kt` |
| 838 | `remote_control.segment_tests` | `appservertransport.src.transport.remotecontrol.SegmentTests` | 0 | 8 | 0 | 8 | `app-server-transport/src/transport/remote_control/segment_tests.rs` | `appservertransport/src/transport/remotecontrol/SegmentTests.kt` |
| 839 | `request_processors.search` | `appserver.src.requestprocessors.Search` | 0 | 7 | 1 | 8 | `app-server/src/request_processors/search.rs` | `appserver/src/requestprocessors/Search.kt` |
| 840 | `rollout.metadata_tests` | `rollout.src.MetadataTests` | 0 | 8 | 0 | 8 | `rollout/src/metadata_tests.rs` | `rollout/src/MetadataTests.kt` |
| 841 | `runtime.module_loader` | `codemode.src.runtime.ModuleLoader` | 0 | 8 | 0 | 8 | `code-mode/src/runtime/module_loader.rs` | `codemode/src/runtime/ModuleLoader.kt` |
| 842 | `runtime.remote_control` | `state.src.runtime.RemoteControl` | 0 | 7 | 1 | 8 | `state/src/runtime/remote_control.rs` | `state/src/runtime/RemoteControl.kt` |
| 843 | `server.transport` | `execserver.src.server.Transport` | 0 | 6 | 2 | 8 | `exec-server/src/server/transport.rs` | `execserver/src/server/Transport.kt` |
| 844 | `suite.exec_policy` | `core.tests.suite.ExecPolicy` | 0 | 8 | 0 | 8 | `core/tests/suite/exec_policy.rs` | `core/tests/suite/ExecPolicy.kt` |
| 845 | `suite.head` | `execpolicylegacy.tests.suite.Head` | 0 | 8 | 0 | 8 | `execpolicy-legacy/tests/suite/head.rs` | `execpolicylegacy/tests/suite/Head.kt` |
| 846 | `suite.override_updates` | `core.tests.suite.OverrideUpdates` | 0 | 8 | 0 | 8 | `core/tests/suite/override_updates.rs` | `core/tests/suite/OverrideUpdates.kt` |
| 847 | `suite.permissions_messages` | `core.tests.suite.PermissionsMessages` | 0 | 8 | 0 | 8 | `core/tests/suite/permissions_messages.rs` | `core/tests/suite/PermissionsMessages.kt` |
| 848 | `suite.responses_api_proxy_headers` | `core.tests.suite.ResponsesApiProxyHeaders` | 0 | 8 | 0 | 8 | `core/tests/suite/responses_api_proxy_headers.rs` | `core/tests/suite/ResponsesApiProxyHeaders.kt` |
| 849 | `suite.safety_check_downgrade` | `core.tests.suite.SafetyCheckDowngrade` | 0 | 8 | 0 | 8 | `core/tests/suite/safety_check_downgrade.rs` | `core/tests/suite/SafetyCheckDowngrade.kt` |
| 850 | `suite.skills` | `core.tests.suite.Skills` | 0 | 8 | 0 | 8 | `core/tests/suite/skills.rs` | `core/tests/suite/Skills.kt` |
| 851 | `tasks.mod_tests` | `core.src.tasks.ModTests` | 0 | 8 | 0 | 8 | `core/src/tasks/mod_tests.rs` | `core/src/tasks/ModTests.kt` |
| 852 | `tasks.user_shell` | `core.src.tasks.UserShell` | 0 | 6 | 2 | 8 | `core/src/tasks/user_shell.rs` | `core/src/tasks/UserShell.kt` |
| 853 | `tests.goal_menu` | `tui.src.chatwidget.tests.GoalMenu` | 0 | 8 | 0 | 8 | `tui/src/chatwidget/tests/goal_menu.rs` | `tui/src/chatwidget/tests/GoalMenu.kt` |
| 854 | `tools.agent_tool_tests` | `tools.src.AgentToolTests` | 0 | 8 | 0 | 8 | `tools/src/agent_tool_tests.rs` | `tools/src/AgentToolTests.kt` |
| 855 | `tools.sandboxing_tests` | `core.src.tools.SandboxingTests` | 0 | 8 | 0 | 8 | `core/src/tools/sandboxing_tests.rs` | `core/src/tools/SandboxingTests.kt` |
| 856 | `tui.additional_dirs` | `tui.src.AdditionalDirs` | 0 | 8 | 0 | 8 | `tui/src/additional_dirs.rs` | `tui/src/AdditionalDirs.kt` |
| 857 | `tui.exec_command` | `tui.src.ExecCommand` | 0 | 8 | 0 | 8 | `tui/src/exec_command.rs` | `tui/src/ExecCommand.kt` |
| 858 | `unified_exec.async_watcher` | `core.src.unifiedexec.AsyncWatcher` | 0 | 8 | 0 | 8 | `core/src/unified_exec/async_watcher.rs` | `core/src/unifiedexec/AsyncWatcher.kt` |
| 859 | `v2.analytics` | `appserver.tests.suite.v2.Analytics` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/analytics.rs` | `appserver/tests/suite/v2/Analytics.kt` |
| 860 | `v2.hooks_list` | `appserver.tests.suite.v2.HooksList` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/hooks_list.rs` | `appserver/tests/suite/v2/HooksList.kt` |
| 861 | `v2.initialize` | `appserver.tests.suite.v2.Initialize` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/initialize.rs` | `appserver/tests/suite/v2/Initialize.kt` |
| 862 | `v2.mcp_resource` | `appserver.tests.suite.v2.McpResource` | 0 | 7 | 1 | 8 | `app-server/tests/suite/v2/mcp_resource.rs` | `appserver/tests/suite/v2/McpResource.kt` |
| 863 | `v2.thread_archive` | `appserver.tests.suite.v2.ThreadArchive` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/thread_archive.rs` | `appserver/tests/suite/v2/ThreadArchive.kt` |
| 864 | `v2.thread_metadata_update` | `appserver.tests.suite.v2.ThreadMetadataUpdate` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/thread_metadata_update.rs` | `appserver/tests/suite/v2/ThreadMetadataUpdate.kt` |
| 865 | `v2.turn_start_zsh_fork` | `appserver.tests.suite.v2.TurnStartZshFork` | 0 | 8 | 0 | 8 | `app-server/tests/suite/v2/turn_start_zsh_fork.rs` | `appserver/tests/suite/v2/TurnStartZshFork.kt` |
| 866 | `windows-sandbox-rs.allow` | `windowssandboxrs.src.Allow` | 0 | 7 | 1 | 8 | `windows-sandbox-rs/src/allow.rs` | `windowssandboxrs/src/Allow.kt` |
| 867 | `windows-sandbox-rs.identity` | `windowssandboxrs.src.Identity` | 0 | 6 | 2 | 8 | `windows-sandbox-rs/src/identity.rs` | `windowssandboxrs/src/Identity.kt` |
| 868 | `write.workspace` | `memories.write.src.Workspace` | 0 | 8 | 0 | 8 | `memories/write/src/workspace.rs` | `memories/write/src/Workspace.kt` |
| 869 | `otel.config` | `otel.src.Config` | 104 | 2 | 5 | 7 | `otel/src/config.rs` | `otel/src/Config.kt` |
| 870 | `common.test_codex_exec` | `core.tests.common.TestCodexExec` | 10 | 6 | 1 | 7 | `core/tests/common/test_codex_exec.rs` | `core/tests/common/TestCodexExec.kt` |
| 871 | `config.skills_config` | `config.src.SkillsConfig` | 6 | 3 | 4 | 7 | `config/src/skills_config.rs` | `config/src/SkillsConfig.kt` |
| 872 | `model-provider.bearer_auth_provider` | `modelprovider.src.BearerAuthProvider` | 4 | 6 | 1 | 7 | `model-provider/src/bearer_auth_provider.rs` | `modelprovider/src/BearerAuthProvider.kt` |
| 873 | `model.backfill_state` | `state.src.model.BackfillState` | 3 | 5 | 2 | 7 | `state/src/model/backfill_state.rs` | `state/src/model/BackfillState.kt` |
| 874 | `codex-mcp.elicitation` | `codexmcp.src.Elicitation` | 2 | 5 | 2 | 7 | `codex-mcp/src/elicitation.rs` | `codexmcp/src/Elicitation.kt` |
| 875 | `core.event_mapping` | `core.src.EventMapping` | 1 | 7 | 0 | 7 | `core/src/event_mapping.rs` | `core/src/EventMapping.kt` |
| 876 | `tools.tool_dispatch_trace` | `core.src.tools.ToolDispatchTrace` | 1 | 6 | 1 | 7 | `core/src/tools/tool_dispatch_trace.rs` | `core/src/tools/ToolDispatchTrace.kt` |
| 877 | `tools.tool_search_entry` | `core.src.tools.ToolSearchEntry` | 1 | 6 | 1 | 7 | `core/src/tools/tool_search_entry.rs` | `core/src/tools/ToolSearchEntry.kt` |
| 878 | `tui.collaboration_modes` | `tui.src.CollaborationModes` | 1 | 7 | 0 | 7 | `tui/src/collaboration_modes.rs` | `tui/src/CollaborationModes.kt` |
| 879 | `tui.markdown` | `tui.src.Markdown` | 1 | 7 | 0 | 7 | `tui/src/markdown.rs` | `tui/src/Markdown.kt` |
| 880 | `amazon_bedrock.catalog` | `modelprovider.src.amazonbedrock.Catalog` | 0 | 7 | 0 | 7 | `model-provider/src/amazon_bedrock/catalog.rs` | `modelprovider/src/amazonbedrock/Catalog.kt` |
| 881 | `app-server-transport.outgoing_message` | `appservertransport.src.OutgoingMessage` | 0 | 2 | 5 | 7 | `app-server-transport/src/outgoing_message.rs` | `appservertransport/src/OutgoingMessage.kt` |
| 882 | `app-server.filters` | `appserver.src.Filters` | 0 | 7 | 0 | 7 | `app-server/src/filters.rs` | `appserver/src/Filters.kt` |
| 883 | `app.history_ui` | `tui.src.app.HistoryUi` | 0 | 7 | 0 | 7 | `tui/src/app/history_ui.rs` | `tui/src/app/HistoryUi.kt` |
| 884 | `apply-patch.seek_sequence` | `applypatch.src.SeekSequence` | 0 | 7 | 0 | 7 | `apply-patch/src/seek_sequence.rs` | `applypatch/src/SeekSequence.kt` |
| 885 | `auth.default_client_tests` | `login.src.auth.DefaultClientTests` | 0 | 7 | 0 | 7 | `login/src/auth/default_client_tests.rs` | `login/src/auth/DefaultClientTests.kt` |
| 886 | `backends.windows_common` | `windowssandboxrs.src.unifiedexec.backends.WindowsCommon` | 0 | 7 | 0 | 7 | `windows-sandbox-rs/src/unified_exec/backends/windows_common.rs` | `windowssandboxrs/src/unifiedexec/backends/WindowsCommon.kt` |
| 887 | `code_mode.wait_handler` | `core.src.tools.codemode.WaitHandler` | 0 | 4 | 3 | 7 | `core/src/tools/code_mode/wait_handler.rs` | `core/src/tools/codemode/WaitHandler.kt` |
| 888 | `codex-api.api_bridge` | `codexapi.src.ApiBridge` | 0 | 5 | 2 | 7 | `codex-api/src/api_bridge.rs` | `codexapi/src/ApiBridge.kt` |
| 889 | `codex-api.telemetry` | `codexapi.src.Telemetry` | 0 | 4 | 3 | 7 | `codex-api/src/telemetry.rs` | `codexapi/src/Telemetry.kt` |
| 890 | `common.zsh_fork` | `core.tests.common.ZshFork` | 0 | 6 | 1 | 7 | `core/tests/common/zsh_fork.rs` | `core/tests/common/ZshFork.kt` |
| 891 | `core-plugins.test_support` | `coreplugins.src.TestSupport` | 0 | 7 | 0 | 7 | `core-plugins/src/test_support.rs` | `coreplugins/src/TestSupport.kt` |
| 892 | `core-skills.invocation_utils_tests` | `coreskills.src.InvocationUtilsTests` | 0 | 7 | 0 | 7 | `core-skills/src/invocation_utils_tests.rs` | `coreskills/src/InvocationUtilsTests.kt` |
| 893 | `core.arc_monitor_tests` | `core.src.ArcMonitorTests` | 0 | 6 | 1 | 7 | `core/src/arc_monitor_tests.rs` | `core/src/ArcMonitorTests.kt` |
| 894 | `debug-client.reader` | `debugclient.src.Reader` | 0 | 7 | 0 | 7 | `debug-client/src/reader.rs` | `debugclient/src/Reader.kt` |
| 895 | `debug_sandbox.seatbelt` | `cli.src.debugsandbox.Seatbelt` | 0 | 5 | 2 | 7 | `cli/src/debug_sandbox/seatbelt.rs` | `cli/src/debugsandbox/Seatbelt.kt` |
| 896 | `endpoint.responses` | `codexapi.src.endpoint.Responses` | 0 | 5 | 2 | 7 | `codex-api/src/endpoint/responses.rs` | `codexapi/src/endpoint/Responses.kt` |
| 897 | `handlers.grep_files_tests` | `core.src.tools.handlers.GrepFilesTests` | 0 | 7 | 0 | 7 | `core/src/tools/handlers/grep_files_tests.rs` | `core/src/tools/handlers/GrepFilesTests.kt` |
| 898 | `loader.layer_io` | `config.src.loader.LayerIo` | 0 | 4 | 3 | 7 | `config/src/loader/layer_io.rs` | `config/src/loader/LayerIo.kt` |
| 899 | `local.list_threads` | `threadstore.src.local.ListThreads` | 0 | 7 | 0 | 7 | `thread-store/src/local/list_threads.rs` | `threadstore/src/local/ListThreads.kt` |
| 900 | `metrics.config` | `otel.src.metrics.Config` | 0 | 5 | 2 | 7 | `otel/src/metrics/config.rs` | `otel/src/metrics/Config.kt` |
| 901 | `metrics.validation` | `otel.src.metrics.Validation` | 0 | 7 | 0 | 7 | `otel/src/metrics/validation.rs` | `otel/src/metrics/Validation.kt` |
| 902 | `multi_agents_v2.message_tool` | `core.src.tools.handlers.multiagentsv2.MessageTool` | 0 | 4 | 3 | 7 | `core/src/tools/handlers/multi_agents_v2/message_tool.rs` | `core/src/tools/handlers/multiagentsv2/MessageTool.kt` |
| 903 | `plugins.test_support` | `core.src.plugins.TestSupport` | 0 | 7 | 0 | 7 | `core/src/plugins/test_support.rs` | `core/src/plugins/TestSupport.kt` |
| 904 | `protocol.event_mapping` | `appserverprotocol.src.protocol.EventMapping` | 0 | 7 | 0 | 7 | `app-server-protocol/src/protocol/event_mapping.rs` | `appserverprotocol/src/protocol/EventMapping.kt` |
| 905 | `realtime_websocket.protocol_v2` | `codexapi.src.endpoint.realtimewebsocket.ProtocolV2` | 0 | 7 | 0 | 7 | `codex-api/src/endpoint/realtime_websocket/protocol_v2.rs` | `codexapi/src/endpoint/realtimewebsocket/ProtocolV2.kt` |
| 906 | `runtime.globals` | `codemode.src.runtime.Globals` | 0 | 7 | 0 | 7 | `code-mode/src/runtime/globals.rs` | `codemode/src/runtime/Globals.kt` |
| 907 | `sleep-inhibitor.iokit_bindings` | `utils.sleepinhibitor.src.IokitBindings` | 0 | 0 | 7 | 7 | `utils/sleep-inhibitor/src/iokit_bindings.rs` | `utils/sleepinhibitor/src/IokitBindings.kt` |
| 908 | `sleep-inhibitor.windows_inhibitor` | `utils.sleepinhibitor.src.WindowsInhibitor` | 0 | 5 | 2 | 7 | `utils/sleep-inhibitor/src/windows_inhibitor.rs` | `utils/sleepinhibitor/src/WindowsInhibitor.kt` |
| 909 | `suite.agent_websocket` | `core.tests.suite.AgentWebsocket` | 0 | 7 | 0 | 7 | `core/tests/suite/agent_websocket.rs` | `core/tests/suite/AgentWebsocket.kt` |
| 910 | `suite.hooks_mcp` | `core.tests.suite.HooksMcp` | 0 | 7 | 0 | 7 | `core/tests/suite/hooks_mcp.rs` | `core/tests/suite/HooksMcp.kt` |
| 911 | `suite.otlp_http_loopback` | `otel.tests.suite.OtlpHttpLoopback` | 0 | 6 | 1 | 7 | `otel/tests/suite/otlp_http_loopback.rs` | `otel/tests/suite/OtlpHttpLoopback.kt` |
| 912 | `suite.user_shell_cmd` | `core.tests.suite.UserShellCmd` | 0 | 7 | 0 | 7 | `core/tests/suite/user_shell_cmd.rs` | `core/tests/suite/UserShellCmd.kt` |
| 913 | `tests.mcp_add_remove` | `cli.tests.McpAddRemove` | 0 | 7 | 0 | 7 | `cli/tests/mcp_add_remove.rs` | `cli/tests/McpAddRemove.kt` |
| 914 | `tests.process_group_cleanup` | `rmcpclient.tests.ProcessGroupCleanup` | 0 | 7 | 0 | 7 | `rmcp-client/tests/process_group_cleanup.rs` | `rmcpclient/tests/ProcessGroupCleanup.kt` |
| 915 | `tools.local_tool_tests` | `tools.src.LocalToolTests` | 0 | 7 | 0 | 7 | `tools/src/local_tool_tests.rs` | `tools/src/LocalToolTests.kt` |
| 916 | `tools.request_plugin_install` | `tools.src.RequestPluginInstall` | 0 | 4 | 3 | 7 | `tools/src/request_plugin_install.rs` | `tools/src/RequestPluginInstall.kt` |
| 917 | `tools.tool_config_tests` | `tools.src.ToolConfigTests` | 0 | 7 | 0 | 7 | `tools/src/tool_config_tests.rs` | `tools/src/ToolConfigTests.kt` |
| 918 | `tools.tool_spec_tests` | `tools.src.ToolSpecTests` | 0 | 7 | 0 | 7 | `tools/src/tool_spec_tests.rs` | `tools/src/ToolSpecTests.kt` |
| 919 | `transport.unix_socket` | `appservertransport.src.transport.UnixSocket` | 0 | 6 | 1 | 7 | `app-server-transport/src/transport/unix_socket.rs` | `appservertransport/src/transport/UnixSocket.kt` |
| 920 | `v2.thread_name_websocket` | `appserver.tests.suite.v2.ThreadNameWebsocket` | 0 | 7 | 0 | 7 | `app-server/tests/suite/v2/thread_name_websocket.rs` | `appserver/tests/suite/v2/ThreadNameWebsocket.kt` |
| 921 | `v2.thread_unsubscribe` | `appserver.tests.suite.v2.ThreadUnsubscribe` | 0 | 7 | 0 | 7 | `app-server/tests/suite/v2/thread_unsubscribe.rs` | `appserver/tests/suite/v2/ThreadUnsubscribe.kt` |
| 922 | `windows-sandbox-rs.audit` | `windowssandboxrs.src.Audit` | 0 | 7 | 0 | 7 | `windows-sandbox-rs/src/audit.rs` | `windowssandboxrs/src/Audit.kt` |
| 923 | `windows-sandbox-rs.cap` | `windowssandboxrs.src.Cap` | 0 | 6 | 1 | 7 | `windows-sandbox-rs/src/cap.rs` | `windowssandboxrs/src/Cap.kt` |
| 924 | `windows-sandbox-rs.desktop` | `windowssandboxrs.src.Desktop` | 0 | 5 | 2 | 7 | `windows-sandbox-rs/src/desktop.rs` | `windowssandboxrs/src/Desktop.kt` |
| 925 | `windows-sandbox-rs.env` | `windowssandboxrs.src.Env` | 0 | 7 | 0 | 7 | `windows-sandbox-rs/src/env.rs` | `windowssandboxrs/src/Env.kt` |
| 926 | `windows-sandbox-rs.wfp_setup` | `windowssandboxrs.src.WfpSetup` | 0 | 5 | 2 | 7 | `windows-sandbox-rs/src/wfp_setup.rs` | `windowssandboxrs/src/WfpSetup.kt` |
| 927 | `plugin.plugin_id` | `plugin.src.PluginId` | 14 | 4 | 2 | 6 | `plugin/src/plugin_id.rs` | `plugin/src/PluginId.kt` |
| 928 | `v2.remote_thread_store` | `appserver.tests.suite.v2.RemoteThreadStore` | 3 | 5 | 1 | 6 | `app-server/tests/suite/v2/remote_thread_store.rs` | `appserver/tests/suite/v2/RemoteThreadStore.kt` |
| 929 | `suite.conversation_summary` | `appserver.tests.suite.ConversationSummary` | 2 | 6 | 0 | 6 | `app-server/tests/suite/conversation_summary.rs` | `appserver/tests/suite/ConversationSummary.kt` |
| 930 | `suite.validation` | `otel.tests.suite.Validation` | 2 | 6 | 0 | 6 | `otel/tests/suite/validation.rs` | `otel/tests/suite/Validation.kt` |
| 931 | `tui.ide_context` | `tui.src.idecontext.IdeContext` | 2 | 1 | 5 | 6 | `tui/src/ide_context.rs` | `tui/src/idecontext/IdeContext.kt` |
| 932 | `chatgpt.get_task` | `chatgpt.src.GetTask` | 1 | 1 | 5 | 6 | `chatgpt/src/get_task.rs` | `chatgpt/src/GetTask.kt` |
| 933 | `runtime.timers` | `codemode.src.runtime.Timers` | 1 | 5 | 1 | 6 | `code-mode/src/runtime/timers.rs` | `codemode/src/runtime/Timers.kt` |
| 934 | `v2.plan_item` | `appserver.tests.suite.v2.PlanItem` | 1 | 6 | 0 | 6 | `app-server/tests/suite/v2/plan_item.rs` | `appserver/tests/suite/v2/PlanItem.kt` |
| 935 | `app-server.tests.common.responses` | `appserver.tests.common.Responses` | 0 | 6 | 0 | 6 | `app-server/tests/common/responses.rs` | `appserver/tests/common/Responses.kt` |
| 936 | `app.app_server_event_targets` | `tui.src.app.AppServerEventTargets` | 0 | 5 | 1 | 6 | `tui/src/app/app_server_event_targets.rs` | `tui/src/app/AppServerEventTargets.kt` |
| 937 | `app.loaded_threads` | `tui.src.app.LoadedThreads` | 0 | 5 | 1 | 6 | `tui/src/app/loaded_threads.rs` | `tui/src/app/LoadedThreads.kt` |
| 938 | `app.thread_goal_actions` | `tui.src.app.ThreadGoalActions` | 0 | 6 | 0 | 6 | `tui/src/app/thread_goal_actions.rs` | `tui/src/app/ThreadGoalActions.kt` |
| 939 | `chatwidget.mcp_startup` | `tui.src.chatwidget.McpStartup` | 0 | 5 | 1 | 6 | `tui/src/chatwidget/mcp_startup.rs` | `tui/src/chatwidget/McpStartup.kt` |
| 940 | `code_mode.execute_handler` | `core.src.tools.codemode.ExecuteHandler` | 0 | 4 | 2 | 6 | `core/src/tools/code_mode/execute_handler.rs` | `core/src/tools/codemode/ExecuteHandler.kt` |
| 941 | `codex-mcp.runtime` | `codexmcp.src.Runtime` | 0 | 4 | 2 | 6 | `codex-mcp/src/runtime.rs` | `codexmcp/src/Runtime.kt` |
| 942 | `config.schema` | `config.src.Schema` | 0 | 6 | 0 | 6 | `config/src/schema.rs` | `config/src/Schema.kt` |
| 943 | `core-skills.config_rules` | `coreskills.src.ConfigRules` | 0 | 3 | 3 | 6 | `core-skills/src/config_rules.rs` | `coreskills/src/ConfigRules.kt` |
| 944 | `core.client_common_tests` | `core.src.ClientCommonTests` | 0 | 6 | 0 | 6 | `core/src/client_common_tests.rs` | `core/src/ClientCommonTests.kt` |
| 945 | `core.codex_delegate_tests` | `core.src.CodexDelegateTests` | 0 | 6 | 0 | 6 | `core/src/codex_delegate_tests.rs` | `core/src/CodexDelegateTests.kt` |
| 946 | `core.network_policy_decision_tests` | `core.src.NetworkPolicyDecisionTests` | 0 | 6 | 0 | 6 | `core/src/network_policy_decision_tests.rs` | `core/src/NetworkPolicyDecisionTests.kt` |
| 947 | `core.network_proxy_loader_tests` | `core.src.NetworkProxyLoaderTests` | 0 | 6 | 0 | 6 | `core/src/network_proxy_loader_tests.rs` | `core/src/NetworkProxyLoaderTests.kt` |
| 948 | `core.realtime_prompt` | `core.src.RealtimePrompt` | 0 | 6 | 0 | 6 | `core/src/realtime_prompt.rs` | `core/src/RealtimePrompt.kt` |
| 949 | `core.turn_metadata_tests` | `core.src.TurnMetadataTests` | 0 | 6 | 0 | 6 | `core/src/turn_metadata_tests.rs` | `core/src/TurnMetadataTests.kt` |
| 950 | `debug-client.main` | `debugclient.src.Main` | 0 | 5 | 1 | 6 | `debug-client/src/main.rs` | `debugclient/src/Main.kt` |
| 951 | `device-key.platform` | `devicekey.src.Platform` | 0 | 5 | 1 | 6 | `device-key/src/platform.rs` | `devicekey/src/Platform.kt` |
| 952 | `features.feature_configs` | `features.src.FeatureConfigs` | 0 | 4 | 2 | 6 | `features/src/feature_configs.rs` | `features/src/FeatureConfigs.kt` |
| 953 | `handlers.dynamic` | `core.src.tools.handlers.Dynamic` | 0 | 4 | 2 | 6 | `core/src/tools/handlers/dynamic.rs` | `core/src/tools/handlers/Dynamic.kt` |
| 954 | `handlers.request_plugin_install_tests` | `core.src.tools.handlers.RequestPluginInstallTests` | 0 | 6 | 0 | 6 | `core/src/tools/handlers/request_plugin_install_tests.rs` | `core/src/tools/handlers/RequestPluginInstallTests.kt` |
| 955 | `hooks.legacy_notify` | `hooks.src.LegacyNotify` | 0 | 5 | 1 | 6 | `hooks/src/legacy_notify.rs` | `hooks/src/LegacyNotify.kt` |
| 956 | `marketplace_add.install` | `coreplugins.src.marketplaceadd.Install` | 0 | 6 | 0 | 6 | `core-plugins/src/marketplace_add/install.rs` | `coreplugins/src/marketplaceadd/Install.kt` |
| 957 | `marketplace_upgrade.activation` | `coreplugins.src.marketplaceupgrade.Activation` | 0 | 5 | 1 | 6 | `core-plugins/src/marketplace_upgrade/activation.rs` | `coreplugins/src/marketplaceupgrade/Activation.kt` |
| 958 | `model.session` | `rollouttrace.src.model.Session` | 0 | 0 | 6 | 6 | `rollout-trace/src/model/session.rs` | `rollouttrace/src/model/Session.kt` |
| 959 | `notifications.bel` | `tui.src.notifications.Bel` | 0 | 4 | 2 | 6 | `tui/src/notifications/bel.rs` | `tui/src/notifications/Bel.kt` |
| 960 | `plugins.mentions` | `core.src.plugins.Mentions` | 0 | 5 | 1 | 6 | `core/src/plugins/mentions.rs` | `core/src/plugins/Mentions.kt` |
| 961 | `plugins.plugin_namespace` | `utils.plugins.src.PluginNamespace` | 0 | 5 | 1 | 6 | `utils/plugins/src/plugin_namespace.rs` | `utils/plugins/src/PluginNamespace.kt` |
| 962 | `protocol.request_user_input` | `protocol.src.RequestUserInput` | 0 | 0 | 6 | 6 | `protocol/src/request_user_input.rs` | `protocol/src/RequestUserInput.kt` |
| 963 | `realtime_websocket.methods_common` | `codexapi.src.endpoint.realtimewebsocket.MethodsCommon` | 0 | 6 | 0 | 6 | `codex-api/src/endpoint/realtime_websocket/methods_common.rs` | `codexapi/src/endpoint/realtimewebsocket/MethodsCommon.kt` |
| 964 | `request_processors.feedback_processor` | `appserver.src.requestprocessors.FeedbackProcessor` | 0 | 5 | 1 | 6 | `app-server/src/request_processors/feedback_processor.rs` | `appserver/src/requestprocessors/FeedbackProcessor.kt` |
| 965 | `request_processors.initialize_processor` | `appserver.src.requestprocessors.InitializeProcessor` | 0 | 5 | 1 | 6 | `app-server/src/request_processors/initialize_processor.rs` | `appserver/src/requestprocessors/InitializeProcessor.kt` |
| 966 | `rollout-trace.raw_event` | `rollouttrace.src.RawEvent` | 0 | 1 | 5 | 6 | `rollout-trace/src/raw_event.rs` | `rollouttrace/src/RawEvent.kt` |
| 967 | `rollout-trace.thread_tests` | `rollouttrace.src.ThreadTests` | 0 | 6 | 0 | 6 | `rollout-trace/src/thread_tests.rs` | `rollouttrace/src/ThreadTests.kt` |
| 968 | `session.rollout_reconstruction` | `core.src.session.RolloutReconstruction` | 0 | 3 | 3 | 6 | `core/src/session/rollout_reconstruction.rs` | `core/src/session/RolloutReconstruction.kt` |
| 969 | `suite.apply_command_e2e` | `chatgpt.tests.suite.ApplyCommandE2e` | 0 | 5 | 1 | 6 | `chatgpt/tests/suite/apply_command_e2e.rs` | `chatgpt/tests/suite/ApplyCommandE2e.kt` |
| 970 | `suite.image_rollout` | `core.tests.suite.ImageRollout` | 0 | 6 | 0 | 6 | `core/tests/suite/image_rollout.rs` | `core/tests/suite/ImageRollout.kt` |
| 971 | `suite.prompt_stdin` | `exec.tests.suite.PromptStdin` | 0 | 6 | 0 | 6 | `exec/tests/suite/prompt_stdin.rs` | `exec/tests/suite/PromptStdin.kt` |
| 972 | `suite.scenarios` | `applypatch.tests.suite.Scenarios` | 0 | 5 | 1 | 6 | `apply-patch/tests/suite/scenarios.rs` | `applypatch/tests/suite/Scenarios.kt` |
| 973 | `suite.sqlite_state` | `core.tests.suite.SqliteState` | 0 | 6 | 0 | 6 | `core/tests/suite/sqlite_state.rs` | `core/tests/suite/SqliteState.kt` |
| 974 | `suite.tool_harness` | `core.tests.suite.ToolHarness` | 0 | 6 | 0 | 6 | `core/tests/suite/tool_harness.rs` | `core/tests/suite/ToolHarness.kt` |
| 975 | `suite.web_search` | `core.tests.suite.WebSearch` | 0 | 6 | 0 | 6 | `core/tests/suite/web_search.rs` | `core/tests/suite/WebSearch.kt` |
| 976 | `tasks.regular` | `core.src.tasks.Regular` | 0 | 5 | 1 | 6 | `core/src/tasks/regular.rs` | `core/src/tasks/Regular.kt` |
| 977 | `tests.approval_requests` | `tui.src.chatwidget.tests.ApprovalRequests` | 0 | 6 | 0 | 6 | `tui/src/chatwidget/tests/approval_requests.rs` | `tui/src/chatwidget/tests/ApprovalRequests.kt` |
| 978 | `tests.schema_fixtures` | `appserverprotocol.tests.SchemaFixtures` | 0 | 6 | 0 | 6 | `app-server-protocol/tests/schema_fixtures.rs` | `appserverprotocol/tests/SchemaFixtures.kt` |
| 979 | `tests.status_command_tests` | `tui.src.chatwidget.tests.StatusCommandTests` | 0 | 6 | 0 | 6 | `tui/src/chatwidget/tests/status_command_tests.rs` | `tui/src/chatwidget/tests/StatusCommandTests.kt` |
| 980 | `tool.terminal_tests` | `rollouttrace.src.reducer.tool.TerminalTests` | 0 | 6 | 0 | 6 | `rollout-trace/src/reducer/tool/terminal_tests.rs` | `rollouttrace/src/reducer/tool/TerminalTests.kt` |
| 981 | `tools.code_mode_tests` | `tools.src.CodeModeTests` | 0 | 6 | 0 | 6 | `tools/src/code_mode_tests.rs` | `tools/src/CodeModeTests.kt` |
| 982 | `tools.hook_names` | `core.src.tools.HookNames` | 0 | 5 | 1 | 6 | `core/src/tools/hook_names.rs` | `core/src/tools/HookNames.kt` |
| 983 | `tools.request_user_input_tool` | `tools.src.RequestUserInputTool` | 0 | 6 | 0 | 6 | `tools/src/request_user_input_tool.rs` | `tools/src/RequestUserInputTool.kt` |
| 984 | `tui.app_server_approval_conversions` | `tui.src.AppServerApprovalConversions` | 0 | 6 | 0 | 6 | `tui/src/app_server_approval_conversions.rs` | `tui/src/AppServerApprovalConversions.kt` |
| 985 | `tui.goal_display` | `tui.src.GoalDisplay` | 0 | 6 | 0 | 6 | `tui/src/goal_display.rs` | `tui/src/GoalDisplay.kt` |
| 986 | `unified_exec.head_tail_buffer_tests` | `core.src.unifiedexec.HeadTailBufferTests` | 0 | 6 | 0 | 6 | `core/src/unified_exec/head_tail_buffer_tests.rs` | `core/src/unifiedexec/HeadTailBufferTests.kt` |
| 987 | `v2.client_metadata` | `appserver.tests.suite.v2.ClientMetadata` | 0 | 6 | 0 | 6 | `app-server/tests/suite/v2/client_metadata.rs` | `appserver/tests/suite/v2/ClientMetadata.kt` |
| 988 | `v2.model_list` | `appserver.tests.suite.v2.ModelList` | 0 | 6 | 0 | 6 | `app-server/tests/suite/v2/model_list.rs` | `appserver/tests/suite/v2/ModelList.kt` |
| 989 | `windows-sandbox-rs.proc_thread_attr` | `windowssandboxrs.src.ProcThreadAttr` | 0 | 5 | 1 | 6 | `windows-sandbox-rs/src/proc_thread_attr.rs` | `windowssandboxrs/src/ProcThreadAttr.kt` |
| 990 | `write.storage_tests` | `memories.write.src.StorageTests` | 0 | 6 | 0 | 6 | `memories/write/src/storage_tests.rs` | `memories/write/src/StorageTests.kt` |
| 991 | `core.network_policy_decision` | `core.src.NetworkPolicyDecision` | 5 | 4 | 1 | 5 | `core/src/network_policy_decision.rs` | `core/src/NetworkPolicyDecision.kt` |
| 992 | `login.auth_env_telemetry` | `login.src.AuthEnvTelemetry` | 5 | 4 | 1 | 5 | `login/src/auth_env_telemetry.rs` | `login/src/AuthEnvTelemetry.kt` |
| 993 | `exec.event_processor` | `exec.src.EventProcessor` | 4 | 3 | 2 | 5 | `exec/src/event_processor.rs` | `exec/src/EventProcessor.kt` |
| 994 | `models.rate_limit_status_payload` | `codexbackendopenapimodels.src.models.RateLimitStatusPayload` | 3 | 1 | 4 | 5 | `codex-backend-openapi-models/src/models/rate_limit_status_payload.rs` | `codexbackendopenapimodels/src/models/RateLimitStatusPayload.kt` |
| 995 | `context_manager.normalize` | `core.src.contextmanager.Normalize` | 1 | 5 | 0 | 5 | `core/src/context_manager/normalize.rs` | `core/src/contextmanager/Normalize.kt` |
| 996 | `hooks.config_rules` | `hooks.src.ConfigRules` | 1 | 5 | 0 | 5 | `hooks/src/config_rules.rs` | `hooks/src/ConfigRules.kt` |
| 997 | `models-manager.collaboration_mode_presets` | `modelsmanager.src.CollaborationModePresets` | 1 | 5 | 0 | 5 | `models-manager/src/collaboration_mode_presets.rs` | `modelsmanager/src/CollaborationModePresets.kt` |
| 998 | `tui.test_support` | `tui.src.TestSupport` | 1 | 5 | 0 | 5 | `tui/src/test_support.rs` | `tui/src/TestSupport.kt` |
| 999 | `app-server.tests.common.mock_model_server` | `appserver.tests.common.MockModelServer` | 0 | 4 | 1 | 5 | `app-server/tests/common/mock_model_server.rs` | `appserver/tests/common/MockModelServer.kt` |
| 1000 | `app.platform_actions` | `tui.src.app.PlatformActions` | 0 | 4 | 1 | 5 | `tui/src/app/platform_actions.rs` | `tui/src/app/PlatformActions.kt` |
| 1001 | `bottom_pane.selection_tabs` | `tui.src.bottompane.SelectionTabs` | 0 | 4 | 1 | 5 | `tui/src/bottom_pane/selection_tabs.rs` | `tui/src/bottompane/SelectionTabs.kt` |
| 1002 | `chatwidget.goal_menu` | `tui.src.chatwidget.GoalMenu` | 0 | 5 | 0 | 5 | `tui/src/chatwidget/goal_menu.rs` | `tui/src/chatwidget/GoalMenu.kt` |
| 1003 | `chatwidget.user_messages` | `tui.src.chatwidget.UserMessages` | 0 | 3 | 2 | 5 | `tui/src/chatwidget/user_messages.rs` | `tui/src/chatwidget/UserMessages.kt` |
| 1004 | `codex-client.retry` | `codexclient.src.Retry` | 0 | 3 | 2 | 5 | `codex-client/src/retry.rs` | `codexclient/src/Retry.kt` |
| 1005 | `common.rollout` | `appserver.tests.common.Rollout` | 0 | 5 | 0 | 5 | `app-server/tests/common/rollout.rs` | `appserver/tests/common/Rollout.kt` |
| 1006 | `connectors.metadata` | `connectors.src.Metadata` | 0 | 5 | 0 | 5 | `connectors/src/metadata.rs` | `connectors/src/Metadata.kt` |
| 1007 | `context.contextual_user_message_tests` | `core.src.context.ContextualUserMessageTests` | 0 | 5 | 0 | 5 | `core/src/context/contextual_user_message_tests.rs` | `core/src/context/ContextualUserMessageTests.kt` |
| 1008 | `core.exec_policy_windows_tests` | `core.src.ExecPolicyWindowsTests` | 0 | 5 | 0 | 5 | `core/src/exec_policy_windows_tests.rs` | `core/src/ExecPolicyWindowsTests.kt` |
| 1009 | `core.mcp` | `core.src.Mcp` | 0 | 4 | 1 | 5 | `core/src/mcp.rs` | `core/src/Mcp.kt` |
| 1010 | `core.personality_migration` | `core.src.PersonalityMigration` | 0 | 4 | 1 | 5 | `core/src/personality_migration.rs` | `core/src/PersonalityMigration.kt` |
| 1011 | `core.rollout` | `core.src.Rollout` | 0 | 5 | 0 | 5 | `core/src/rollout.rs` | `core/src/Rollout.kt` |
| 1012 | `core.safety` | `core.src.Safety` | 0 | 4 | 1 | 5 | `core/src/safety.rs` | `core/src/Safety.kt` |
| 1013 | `core.turn_timing_tests` | `core.src.TurnTimingTests` | 0 | 5 | 0 | 5 | `core/src/turn_timing_tests.rs` | `core/src/TurnTimingTests.kt` |
| 1014 | `core.unavailable_tool` | `core.src.UnavailableTool` | 0 | 5 | 0 | 5 | `core/src/unavailable_tool.rs` | `core/src/UnavailableTool.kt` |
| 1015 | `execpolicy-legacy.arg_resolver` | `execpolicylegacy.src.ArgResolver` | 0 | 3 | 2 | 5 | `execpolicy-legacy/src/arg_resolver.rs` | `execpolicylegacy/src/ArgResolver.kt` |
| 1016 | `execpolicy-legacy.policy` | `execpolicylegacy.src.Policy` | 0 | 4 | 1 | 5 | `execpolicy-legacy/src/policy.rs` | `execpolicylegacy/src/Policy.kt` |
| 1017 | `execpolicy.execpolicycheck` | `execpolicy.src.Execpolicycheck` | 0 | 3 | 2 | 5 | `execpolicy/src/execpolicycheck.rs` | `execpolicy/src/Execpolicycheck.kt` |
| 1018 | `file-search.main` | `filesearch.src.Main` | 0 | 4 | 1 | 5 | `file-search/src/main.rs` | `filesearch/src/Main.kt` |
| 1019 | `handlers.agent_jobs_tests` | `core.src.tools.handlers.AgentJobsTests` | 0 | 5 | 0 | 5 | `core/src/tools/handlers/agent_jobs_tests.rs` | `core/src/tools/handlers/AgentJobsTests.kt` |
| 1020 | `handlers.unavailable_tool` | `core.src.tools.handlers.UnavailableTool` | 0 | 3 | 2 | 5 | `core/src/tools/handlers/unavailable_tool.rs` | `core/src/tools/handlers/UnavailableTool.kt` |
| 1021 | `linux-sandbox.vendored_bwrap` | `linuxsandbox.src.VendoredBwrap` | 0 | 5 | 0 | 5 | `linux-sandbox/src/vendored_bwrap.rs` | `linuxsandbox/src/VendoredBwrap.kt` |
| 1022 | `local.test_support` | `threadstore.src.local.TestSupport` | 0 | 5 | 0 | 5 | `thread-store/src/local/test_support.rs` | `threadstore/src/local/TestSupport.kt` |
| 1023 | `mcp-server.codex_tool_runner` | `mcpserver.src.CodexToolRunner` | 0 | 5 | 0 | 5 | `mcp-server/src/codex_tool_runner.rs` | `mcpserver/src/CodexToolRunner.kt` |
| 1024 | `metrics.tags` | `otel.src.metrics.Tags` | 0 | 4 | 1 | 5 | `otel/src/metrics/tags.rs` | `otel/src/metrics/Tags.kt` |
| 1025 | `models-manager.model_info_tests` | `modelsmanager.src.ModelInfoTests` | 0 | 5 | 0 | 5 | `models-manager/src/model_info_tests.rs` | `modelsmanager/src/ModelInfoTests.kt` |
| 1026 | `multi_agents_v2.followup_task` | `core.src.tools.handlers.multiagentsv2.FollowupTask` | 0 | 3 | 2 | 5 | `core/src/tools/handlers/multi_agents_v2/followup_task.rs` | `core/src/tools/handlers/multiagentsv2/FollowupTask.kt` |
| 1027 | `multi_agents_v2.send_message` | `core.src.tools.handlers.multiagentsv2.SendMessage` | 0 | 3 | 2 | 5 | `core/src/tools/handlers/multi_agents_v2/send_message.rs` | `core/src/tools/handlers/multiagentsv2/SendMessage.kt` |
| 1028 | `read.citations` | `memories.read.src.Citations` | 0 | 5 | 0 | 5 | `memories/read/src/citations.rs` | `memories/read/src/Citations.kt` |
| 1029 | `realtime_websocket.methods_v2` | `codexapi.src.endpoint.realtimewebsocket.MethodsV2` | 0 | 5 | 0 | 5 | `codex-api/src/endpoint/realtime_websocket/methods_v2.rs` | `codexapi/src/endpoint/realtimewebsocket/MethodsV2.kt` |
| 1030 | `realtime_websocket.protocol_common` | `codexapi.src.endpoint.realtimewebsocket.ProtocolCommon` | 0 | 5 | 0 | 5 | `codex-api/src/endpoint/realtime_websocket/protocol_common.rs` | `codexapi/src/endpoint/realtimewebsocket/ProtocolCommon.kt` |
| 1031 | `reducer.code_cell_tests` | `rollouttrace.src.reducer.CodeCellTests` | 0 | 5 | 0 | 5 | `rollout-trace/src/reducer/code_cell_tests.rs` | `rollouttrace/src/reducer/CodeCellTests.kt` |
| 1032 | `remote.list_threads` | `threadstore.src.remote.ListThreads` | 0 | 4 | 1 | 5 | `thread-store/src/remote/list_threads.rs` | `threadstore/src/remote/ListThreads.kt` |
| 1033 | `runtimes.apply_patch_tests` | `core.src.tools.runtimes.ApplyPatchTests` | 0 | 5 | 0 | 5 | `core/src/tools/runtimes/apply_patch_tests.rs` | `core/src/tools/runtimes/ApplyPatchTests.kt` |
| 1034 | `server.jsonrpc` | `execserver.src.server.Jsonrpc` | 0 | 5 | 0 | 5 | `exec-server/src/server/jsonrpc.rs` | `execserver/src/server/Jsonrpc.kt` |
| 1035 | `state.session_tests` | `core.src.state.SessionTests` | 0 | 5 | 0 | 5 | `core/src/state/session_tests.rs` | `core/src/state/SessionTests.kt` |
| 1036 | `suite.cp` | `execpolicylegacy.tests.suite.Cp` | 0 | 5 | 0 | 5 | `execpolicy-legacy/tests/suite/cp.rs` | `execpolicylegacy/tests/suite/Cp.kt` |
| 1037 | `suite.deprecation_notice` | `core.tests.suite.DeprecationNotice` | 0 | 5 | 0 | 5 | `core/tests/suite/deprecation_notice.rs` | `core/tests/suite/DeprecationNotice.kt` |
| 1038 | `suite.live_cli` | `core.tests.suite.LiveCli` | 0 | 5 | 0 | 5 | `core/tests/suite/live_cli.rs` | `core/tests/suite/LiveCli.kt` |
| 1039 | `suite.live_reload` | `core.tests.suite.LiveReload` | 0 | 5 | 0 | 5 | `core/tests/suite/live_reload.rs` | `core/tests/suite/LiveReload.kt` |
| 1040 | `suite.pwd` | `execpolicylegacy.tests.suite.Pwd` | 0 | 5 | 0 | 5 | `execpolicy-legacy/tests/suite/pwd.rs` | `execpolicylegacy/tests/suite/Pwd.kt` |
| 1041 | `suite.resume` | `core.tests.suite.Resume` | 0 | 5 | 0 | 5 | `core/tests/suite/resume.rs` | `core/tests/suite/Resume.kt` |
| 1042 | `suite.sed` | `execpolicylegacy.tests.suite.Sed` | 0 | 5 | 0 | 5 | `execpolicy-legacy/tests/suite/sed.rs` | `execpolicylegacy/tests/suite/Sed.kt` |
| 1043 | `suite.send` | `otel.tests.suite.Send` | 0 | 5 | 0 | 5 | `otel/tests/suite/send.rs` | `otel/tests/suite/Send.kt` |
| 1044 | `suite.window_headers` | `core.tests.suite.WindowHeaders` | 0 | 5 | 0 | 5 | `core/tests/suite/window_headers.rs` | `core/tests/suite/WindowHeaders.kt` |
| 1045 | `tests.login` | `cli.tests.Login` | 0 | 5 | 0 | 5 | `cli/tests/login.rs` | `cli/tests/Login.kt` |
| 1046 | `tests.marketplace_add` | `cli.tests.MarketplaceAdd` | 0 | 5 | 0 | 5 | `cli/tests/marketplace_add.rs` | `cli/tests/MarketplaceAdd.kt` |
| 1047 | `tests.marketplace_remove` | `cli.tests.MarketplaceRemove` | 0 | 5 | 0 | 5 | `cli/tests/marketplace_remove.rs` | `cli/tests/MarketplaceRemove.kt` |
| 1048 | `tests.responses_headers` | `core.tests.ResponsesHeaders` | 0 | 5 | 0 | 5 | `core/tests/responses_headers.rs` | `core/tests/ResponsesHeaders.kt` |
| 1049 | `thread-manager-sample.main` | `threadmanagersample.src.Main` | 0 | 4 | 1 | 5 | `thread-manager-sample/src/main.rs` | `threadmanagersample/src/Main.kt` |
| 1050 | `tools.image_detail_tests` | `tools.src.ImageDetailTests` | 0 | 5 | 0 | 5 | `tools/src/image_detail_tests.rs` | `tools/src/ImageDetailTests.kt` |
| 1051 | `tools.registry_tests` | `core.src.tools.RegistryTests` | 0 | 3 | 2 | 5 | `core/src/tools/registry_tests.rs` | `core/src/tools/RegistryTests.kt` |
| 1052 | `tools.request_plugin_install_tests` | `tools.src.RequestPluginInstallTests` | 0 | 5 | 0 | 5 | `tools/src/request_plugin_install_tests.rs` | `tools/src/RequestPluginInstallTests.kt` |
| 1053 | `tools.request_user_input_tool_tests` | `tools.src.RequestUserInputToolTests` | 0 | 5 | 0 | 5 | `tools/src/request_user_input_tool_tests.rs` | `tools/src/RequestUserInputToolTests.kt` |
| 1054 | `tools.router_tests` | `core.src.tools.RouterTests` | 0 | 5 | 0 | 5 | `core/src/tools/router_tests.rs` | `core/src/tools/RouterTests.kt` |
| 1055 | `tui.approval_events` | `tui.src.ApprovalEvents` | 0 | 3 | 2 | 5 | `tui/src/approval_events.rs` | `tui/src/ApprovalEvents.kt` |
| 1056 | `uds.lib_tests` | `uds.src.LibTests` | 0 | 5 | 0 | 5 | `uds/src/lib_tests.rs` | `uds/src/LibTests.kt` |
| 1057 | `v2.marketplace_remove` | `appserver.tests.suite.v2.MarketplaceRemove` | 0 | 5 | 0 | 5 | `app-server/tests/suite/v2/marketplace_remove.rs` | `appserver/tests/suite/v2/MarketplaceRemove.kt` |
| 1058 | `win.procthreadattr` | `utils.pty.src.win.Procthreadattr` | 0 | 4 | 1 | 5 | `utils/pty/src/win/procthreadattr.rs` | `utils/pty/src/win/Procthreadattr.kt` |
| 1059 | `windows-sandbox-rs.hide_users` | `windowssandboxrs.src.HideUsers` | 0 | 5 | 0 | 5 | `windows-sandbox-rs/src/hide_users.rs` | `windowssandboxrs/src/HideUsers.kt` |
| 1060 | `write.guard_tests` | `memories.write.src.GuardTests` | 0 | 5 | 0 | 5 | `memories/write/src/guard_tests.rs` | `memories/write/src/GuardTests.kt` |
| 1061 | `execpolicy-legacy.arg_type` | `execpolicylegacy.src.ArgType` | 11 | 2 | 2 | 4 | `execpolicy-legacy/src/arg_type.rs` | `execpolicylegacy/src/ArgType.kt` |
| 1062 | `metrics.timer` | `otel.src.metrics.Timer` | 3 | 3 | 1 | 4 | `otel/src/metrics/timer.rs` | `otel/src/metrics/Timer.kt` |
| 1063 | `read.usage` | `memories.read.src.Usage` | 3 | 3 | 1 | 4 | `memories/read/src/usage.rs` | `memories/read/src/Usage.kt` |
| 1064 | `apps.render` | `core.src.apps.Render` | 2 | 4 | 0 | 4 | `core/src/apps/render.rs` | `core/src/apps/Render.kt` |
| 1065 | `app-server.error_code` | `appserver.src.ErrorCode` | 1 | 4 | 0 | 4 | `app-server/src/error_code.rs` | `appserver/src/ErrorCode.kt` |
| 1066 | `chatgpt.apply_command` | `chatgpt.src.ApplyCommand` | 1 | 3 | 1 | 4 | `chatgpt/src/apply_command.rs` | `chatgpt/src/ApplyCommand.kt` |
| 1067 | `core.apply_patch` | `core.src.ApplyPatch` | 1 | 2 | 2 | 4 | `core/src/apply_patch.rs` | `core/src/ApplyPatch.kt` |
| 1068 | `tui.get_git_diff` | `tui.src.GetGitDiff` | 1 | 4 | 0 | 4 | `tui/src/get_git_diff.rs` | `tui/src/GetGitDiff.kt` |
| 1069 | `write.guard` | `memories.write.src.Guard` | 1 | 4 | 0 | 4 | `memories/write/src/guard.rs` | `memories/write/src/Guard.kt` |
| 1070 | `app-server.main` | `appserver.src.Main` | 0 | 3 | 1 | 4 | `app-server/src/main.rs` | `appserver/src/Main.kt` |
| 1071 | `app.app_server_events` | `tui.src.app.AppServerEvents` | 0 | 4 | 0 | 4 | `tui/src/app/app_server_events.rs` | `tui/src/app/AppServerEvents.kt` |
| 1072 | `app.event_dispatch` | `tui.src.app.EventDispatch` | 0 | 4 | 0 | 4 | `tui/src/app/event_dispatch.rs` | `tui/src/app/EventDispatch.kt` |
| 1073 | `bin.custom_ca_probe` | `codexclient.src.bin.CustomCaProbe` | 0 | 4 | 0 | 4 | `codex-client/src/bin/custom_ca_probe.rs` | `codexclient/src/bin/CustomCaProbe.kt` |
| 1074 | `cli.wsl_paths` | `cli.src.WslPaths` | 0 | 4 | 0 | 4 | `cli/src/wsl_paths.rs` | `cli/src/WslPaths.kt` |
| 1075 | `client.rpc_http_client` | `execserver.src.client.RpcHttpClient` | 0 | 4 | 0 | 4 | `exec-server/src/client/rpc_http_client.rs` | `execserver/src/client/RpcHttpClient.kt` |
| 1076 | `code_mode.execute_handler_tests` | `core.src.tools.codemode.ExecuteHandlerTests` | 0 | 4 | 0 | 4 | `core/src/tools/code_mode/execute_handler_tests.rs` | `core/src/tools/codemode/ExecuteHandlerTests.kt` |
| 1077 | `code_mode.response_adapter` | `core.src.tools.codemode.ResponseAdapter` | 0 | 3 | 1 | 4 | `core/src/tools/code_mode/response_adapter.rs` | `core/src/tools/codemode/ResponseAdapter.kt` |
| 1078 | `common.process` | `core.tests.common.Process` | 0 | 4 | 0 | 4 | `core/tests/common/process.rs` | `core/tests/common/Process.kt` |
| 1079 | `config.hooks_tests` | `config.src.HooksTests` | 0 | 4 | 0 | 4 | `config/src/hooks_tests.rs` | `config/src/HooksTests.kt` |
| 1080 | `config.merge_tests` | `config.src.MergeTests` | 0 | 4 | 0 | 4 | `config/src/merge_tests.rs` | `config/src/MergeTests.kt` |
| 1081 | `config.types_tests` | `config.src.TypesTests` | 0 | 4 | 0 | 4 | `config/src/types_tests.rs` | `config/src/TypesTests.kt` |
| 1082 | `connectors.filter` | `connectors.src.Filter` | 0 | 4 | 0 | 4 | `connectors/src/filter.rs` | `connectors/src/Filter.kt` |
| 1083 | `connectors.merge` | `connectors.src.Merge` | 0 | 4 | 0 | 4 | `connectors/src/merge.rs` | `connectors/src/Merge.kt` |
| 1084 | `core-plugins.startup_remote_sync` | `coreplugins.src.StartupRemoteSync` | 0 | 4 | 0 | 4 | `core-plugins/src/startup_remote_sync.rs` | `coreplugins/src/StartupRemoteSync.kt` |
| 1085 | `core.command_canonicalization_tests` | `core.src.CommandCanonicalizationTests` | 0 | 4 | 0 | 4 | `core/src/command_canonicalization_tests.rs` | `core/src/CommandCanonicalizationTests.kt` |
| 1086 | `core.commit_attribution_tests` | `core.src.CommitAttributionTests` | 0 | 4 | 0 | 4 | `core/src/commit_attribution_tests.rs` | `core/src/CommitAttributionTests.kt` |
| 1087 | `core.installation_id` | `core.src.InstallationId` | 0 | 4 | 0 | 4 | `core/src/installation_id.rs` | `core/src/InstallationId.kt` |
| 1088 | `core.message_history_tests` | `core.src.MessageHistoryTests` | 0 | 4 | 0 | 4 | `core/src/message_history_tests.rs` | `core/src/MessageHistoryTests.kt` |
| 1089 | `core.skills` | `core.src.Skills` | 0 | 4 | 0 | 4 | `core/src/skills.rs` | `core/src/Skills.kt` |
| 1090 | `core.windows_sandbox_read_grants_tests` | `core.src.WindowsSandboxReadGrantsTests` | 0 | 4 | 0 | 4 | `core/src/windows_sandbox_read_grants_tests.rs` | `core/src/WindowsSandboxReadGrantsTests.kt` |
| 1091 | `elevated.runner_pipe` | `windowssandboxrs.src.elevated.RunnerPipe` | 0 | 4 | 0 | 4 | `windows-sandbox-rs/src/elevated/runner_pipe.rs` | `windowssandboxrs/src/elevated/RunnerPipe.kt` |
| 1092 | `engine.command_runner` | `hooks.src.engine.CommandRunner` | 0 | 3 | 1 | 4 | `hooks/src/engine/command_runner.rs` | `hooks/src/engine/CommandRunner.kt` |
| 1093 | `engine.schema_loader` | `hooks.src.engine.SchemaLoader` | 0 | 3 | 1 | 4 | `hooks/src/engine/schema_loader.rs` | `hooks/src/engine/SchemaLoader.kt` |
| 1094 | `exec-server.runtime_paths` | `execserver.src.RuntimePaths` | 0 | 3 | 1 | 4 | `exec-server/src/runtime_paths.rs` | `execserver/src/RuntimePaths.kt` |
| 1095 | `exec.cli_tests` | `exec.src.CliTests` | 0 | 4 | 0 | 4 | `exec/src/cli_tests.rs` | `exec/src/CliTests.kt` |
| 1096 | `handlers.request_permissions` | `core.src.tools.handlers.RequestPermissions` | 0 | 2 | 2 | 4 | `core/src/tools/handlers/request_permissions.rs` | `core/src/tools/handlers/RequestPermissions.kt` |
| 1097 | `handlers.request_user_input` | `core.src.tools.handlers.RequestUserInput` | 0 | 2 | 2 | 4 | `core/src/tools/handlers/request_user_input.rs` | `core/src/tools/handlers/RequestUserInput.kt` |
| 1098 | `mcp-server.exec_approval` | `mcpserver.src.ExecApproval` | 0 | 2 | 2 | 4 | `mcp-server/src/exec_approval.rs` | `mcpserver/src/ExecApproval.kt` |
| 1099 | `mcp-server.patch_approval` | `mcpserver.src.PatchApproval` | 0 | 2 | 2 | 4 | `mcp-server/src/patch_approval.rs` | `mcpserver/src/PatchApproval.kt` |
| 1100 | `network-proxy.mitm_tests` | `networkproxy.src.MitmTests` | 0 | 4 | 0 | 4 | `network-proxy/src/mitm_tests.rs` | `networkproxy/src/MitmTests.kt` |
| 1101 | `plugin.plugin_namespace` | `plugin.src.PluginNamespace` | 0 | 3 | 1 | 4 | `plugin/src/plugin_namespace.rs` | `plugin/src/PluginNamespace.kt` |
| 1102 | `plugins.mcp_connector` | `utils.plugins.src.McpConnector` | 0 | 4 | 0 | 4 | `utils/plugins/src/mcp_connector.rs` | `utils/plugins/src/McpConnector.kt` |
| 1103 | `realtime_websocket.methods_v1` | `codexapi.src.endpoint.realtimewebsocket.MethodsV1` | 0 | 4 | 0 | 4 | `codex-api/src/endpoint/realtime_websocket/methods_v1.rs` | `codexapi/src/endpoint/realtimewebsocket/MethodsV1.kt` |
| 1104 | `reducer.compaction` | `rollouttrace.src.reducer.Compaction` | 0 | 3 | 1 | 4 | `rollout-trace/src/reducer/compaction.rs` | `rollouttrace/src/reducer/Compaction.kt` |
| 1105 | `reducer.inference` | `rollouttrace.src.reducer.Inference` | 0 | 3 | 1 | 4 | `rollout-trace/src/reducer/inference.rs` | `rollouttrace/src/reducer/Inference.kt` |
| 1106 | `render.line_utils` | `tui.src.render.LineUtils` | 0 | 4 | 0 | 4 | `tui/src/render/line_utils.rs` | `tui/src/render/LineUtils.kt` |
| 1107 | `request_processors.git_processor` | `appserver.src.requestprocessors.GitProcessor` | 0 | 3 | 1 | 4 | `app-server/src/request_processors/git_processor.rs` | `appserver/src/requestprocessors/GitProcessor.kt` |
| 1108 | `request_processors.token_usage_replay` | `appserver.src.requestprocessors.TokenUsageReplay` | 0 | 3 | 1 | 4 | `app-server/src/request_processors/token_usage_replay.rs` | `appserver/src/requestprocessors/TokenUsageReplay.kt` |
| 1109 | `request_processors.windows_sandbox_processor` | `appserver.src.requestprocessors.WindowsSandboxProcessor` | 0 | 3 | 1 | 4 | `app-server/src/request_processors/windows_sandbox_processor.rs` | `appserver/src/requestprocessors/WindowsSandboxProcessor.kt` |
| 1110 | `sandboxing.landlock_tests` | `sandboxing.src.LandlockTests` | 0 | 4 | 0 | 4 | `sandboxing/src/landlock_tests.rs` | `sandboxing/src/LandlockTests.kt` |
| 1111 | `sleep-inhibitor.dummy` | `utils.sleepinhibitor.src.Dummy` | 0 | 3 | 1 | 4 | `utils/sleep-inhibitor/src/dummy.rs` | `utils/sleepinhibitor/src/Dummy.kt` |
| 1112 | `stream-parser.stream_text` | `utils.streamparser.src.StreamText` | 0 | 2 | 2 | 4 | `utils/stream-parser/src/stream_text.rs` | `utils/streamparser/src/StreamText.kt` |
| 1113 | `suite.agents_md` | `core.tests.suite.AgentsMd` | 0 | 4 | 0 | 4 | `core/tests/suite/agents_md.rs` | `core/tests/suite/AgentsMd.kt` |
| 1114 | `suite.openai_file_mcp` | `core.tests.suite.OpenaiFileMcp` | 0 | 4 | 0 | 4 | `core/tests/suite/openai_file_mcp.rs` | `core/tests/suite/OpenaiFileMcp.kt` |
| 1115 | `suite.request_plugin_install` | `core.tests.suite.RequestPluginInstall` | 0 | 4 | 0 | 4 | `core/tests/suite/request_plugin_install.rs` | `core/tests/suite/RequestPluginInstall.kt` |
| 1116 | `suite.spawn_agent_description` | `core.tests.suite.SpawnAgentDescription` | 0 | 4 | 0 | 4 | `core/tests/suite/spawn_agent_description.rs` | `core/tests/suite/SpawnAgentDescription.kt` |
| 1117 | `suite.websocket_fallback` | `core.tests.suite.WebsocketFallback` | 0 | 4 | 0 | 4 | `core/tests/suite/websocket_fallback.rs` | `core/tests/suite/WebsocketFallback.kt` |
| 1118 | `tasks.compact` | `core.src.tasks.Compact` | 0 | 3 | 1 | 4 | `core/src/tasks/compact.rs` | `core/src/tasks/Compact.kt` |
| 1119 | `tests.mcp_list` | `cli.tests.McpList` | 0 | 4 | 0 | 4 | `cli/tests/mcp_list.rs` | `cli/tests/McpList.kt` |
| 1120 | `tests.models_integration` | `codexapi.tests.ModelsIntegration` | 0 | 3 | 1 | 4 | `codex-api/tests/models_integration.rs` | `codexapi/tests/ModelsIntegration.kt` |
| 1121 | `tests.streamable_http_recovery` | `rmcpclient.tests.StreamableHttpRecovery` | 0 | 4 | 0 | 4 | `rmcp-client/tests/streamable_http_recovery.rs` | `rmcpclient/tests/StreamableHttpRecovery.kt` |
| 1122 | `tests.terminal_title` | `tui.src.chatwidget.tests.TerminalTitle` | 0 | 4 | 0 | 4 | `tui/src/chatwidget/tests/terminal_title.rs` | `tui/src/chatwidget/tests/TerminalTitle.kt` |
| 1123 | `tools.goal_tool` | `tools.src.GoalTool` | 0 | 4 | 0 | 4 | `tools/src/goal_tool.rs` | `tools/src/GoalTool.kt` |
| 1124 | `tools.mcp_tool_tests` | `tools.src.McpToolTests` | 0 | 4 | 0 | 4 | `tools/src/mcp_tool_tests.rs` | `tools/src/McpToolTests.kt` |
| 1125 | `tools.responses_api_tests` | `tools.src.ResponsesApiTests` | 0 | 4 | 0 | 4 | `tools/src/responses_api_tests.rs` | `tools/src/ResponsesApiTests.kt` |
| 1126 | `tools.tool_discovery_tests` | `tools.src.ToolDiscoveryTests` | 0 | 4 | 0 | 4 | `tools/src/tool_discovery_tests.rs` | `tools/src/ToolDiscoveryTests.kt` |
| 1127 | `tui.skills_helpers` | `tui.src.SkillsHelpers` | 0 | 4 | 0 | 4 | `tui/src/skills_helpers.rs` | `tui/src/SkillsHelpers.kt` |
| 1128 | `tui.width` | `tui.src.Width` | 0 | 4 | 0 | 4 | `tui/src/width.rs` | `tui/src/Width.kt` |
| 1129 | `unix.escalate_client` | `shellescalation.src.unix.EscalateClient` | 0 | 4 | 0 | 4 | `shell-escalation/src/unix/escalate_client.rs` | `shellescalation/src/unix/EscalateClient.kt` |
| 1130 | `v2.memory_reset` | `appserver.tests.suite.v2.MemoryReset` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/memory_reset.rs` | `appserver/tests/suite/v2/MemoryReset.kt` |
| 1131 | `v2.thread_inject_items` | `appserver.tests.suite.v2.ThreadInjectItems` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/thread_inject_items.rs` | `appserver/tests/suite/v2/ThreadInjectItems.kt` |
| 1132 | `v2.thread_loaded_list` | `appserver.tests.suite.v2.ThreadLoadedList` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/thread_loaded_list.rs` | `appserver/tests/suite/v2/ThreadLoadedList.kt` |
| 1133 | `v2.thread_memory_mode_set` | `appserver.tests.suite.v2.ThreadMemoryModeSet` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/thread_memory_mode_set.rs` | `appserver/tests/suite/v2/ThreadMemoryModeSet.kt` |
| 1134 | `v2.thread_unarchive` | `appserver.tests.suite.v2.ThreadUnarchive` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/thread_unarchive.rs` | `appserver/tests/suite/v2/ThreadUnarchive.kt` |
| 1135 | `v2.turn_interrupt` | `appserver.tests.suite.v2.TurnInterrupt` | 0 | 4 | 0 | 4 | `app-server/tests/suite/v2/turn_interrupt.rs` | `appserver/tests/suite/v2/TurnInterrupt.kt` |
| 1136 | `windows-sandbox-rs.policy` | `windowssandboxrs.src.Policy` | 0 | 4 | 0 | 4 | `windows-sandbox-rs/src/policy.rs` | `windowssandboxrs/src/Policy.kt` |
| 1137 | `windows-sandbox-rs.read_acl_mutex` | `windowssandboxrs.src.ReadAclMutex` | 0 | 3 | 1 | 4 | `windows-sandbox-rs/src/read_acl_mutex.rs` | `windowssandboxrs/src/ReadAclMutex.kt` |
| 1138 | `windows-sandbox-rs.workspace_acl` | `windowssandboxrs.src.WorkspaceAcl` | 0 | 4 | 0 | 4 | `windows-sandbox-rs/src/workspace_acl.rs` | `windowssandboxrs/src/WorkspaceAcl.kt` |
| 1139 | `write.control` | `memories.write.src.Control` | 0 | 4 | 0 | 4 | `memories/write/src/control.rs` | `memories/write/src/Control.kt` |
| 1140 | `write.prompts` | `memories.write.src.Prompts` | 0 | 4 | 0 | 4 | `memories/write/src/prompts.rs` | `memories/write/src/Prompts.kt` |
| 1141 | `write.workspace_tests` | `memories.write.src.WorkspaceTests` | 0 | 4 | 0 | 4 | `memories/write/src/workspace_tests.rs` | `memories/write/src/WorkspaceTests.kt` |
| 1142 | `models-manager.model_info` | `modelsmanager.src.ModelInfo` | 33 | 3 | 0 | 3 | `models-manager/src/model_info.rs` | `modelsmanager/src/ModelInfo.kt` |
| 1143 | `execpolicy-legacy.exec_call` | `execpolicylegacy.src.ExecCall` | 14 | 2 | 1 | 3 | `execpolicy-legacy/src/exec_call.rs` | `execpolicylegacy/src/ExecCall.kt` |
| 1144 | `tools.tool_definition` | `tools.src.ToolDefinition` | 11 | 2 | 1 | 3 | `tools/src/tool_definition.rs` | `tools/src/ToolDefinition.kt` |
| 1145 | `tools.image_detail` | `tools.src.ImageDetail` | 8 | 3 | 0 | 3 | `tools/src/image_detail.rs` | `tools/src/ImageDetail.kt` |
| 1146 | `context.subagent_notification` | `core.src.context.SubagentNotification` | 4 | 2 | 1 | 3 | `core/src/context/subagent_notification.rs` | `core/src/context/SubagentNotification.kt` |
| 1147 | `tui.model_catalog` | `tui.src.ModelCatalog` | 4 | 2 | 1 | 3 | `tui/src/model_catalog.rs` | `tui/src/ModelCatalog.kt` |
| 1148 | `context.apps_instructions` | `core.src.context.AppsInstructions` | 3 | 2 | 1 | 3 | `core/src/context/apps_instructions.rs` | `core/src/context/AppsInstructions.kt` |
| 1149 | `context.available_plugins_instructions` | `core.src.context.AvailablePluginsInstructions` | 3 | 2 | 1 | 3 | `core/src/context/available_plugins_instructions.rs` | `core/src/context/AvailablePluginsInstructions.kt` |
| 1150 | `context.collaboration_mode_instructions` | `core.src.context.CollaborationModeInstructions` | 3 | 2 | 1 | 3 | `core/src/context/collaboration_mode_instructions.rs` | `core/src/context/CollaborationModeInstructions.kt` |
| 1151 | `context.personality_spec_instructions` | `core.src.context.PersonalitySpecInstructions` | 3 | 2 | 1 | 3 | `core/src/context/personality_spec_instructions.rs` | `core/src/context/PersonalitySpecInstructions.kt` |
| 1152 | `context.turn_aborted` | `core.src.context.TurnAborted` | 3 | 2 | 1 | 3 | `core/src/context/turn_aborted.rs` | `core/src/context/TurnAborted.kt` |
| 1153 | `context.approved_command_prefix_saved` | `core.src.context.ApprovedCommandPrefixSaved` | 2 | 2 | 1 | 3 | `core/src/context/approved_command_prefix_saved.rs` | `core/src/context/ApprovedCommandPrefixSaved.kt` |
| 1154 | `context.available_skills_instructions` | `core.src.context.AvailableSkillsInstructions` | 2 | 2 | 1 | 3 | `core/src/context/available_skills_instructions.rs` | `core/src/context/AvailableSkillsInstructions.kt` |
| 1155 | `context.hook_additional_context` | `core.src.context.HookAdditionalContext` | 2 | 2 | 1 | 3 | `core/src/context/hook_additional_context.rs` | `core/src/context/HookAdditionalContext.kt` |
| 1156 | `context.image_generation_instructions` | `core.src.context.ImageGenerationInstructions` | 2 | 2 | 1 | 3 | `core/src/context/image_generation_instructions.rs` | `core/src/context/ImageGenerationInstructions.kt` |
| 1157 | `context.model_switch_instructions` | `core.src.context.ModelSwitchInstructions` | 2 | 2 | 1 | 3 | `core/src/context/model_switch_instructions.rs` | `core/src/context/ModelSwitchInstructions.kt` |
| 1158 | `context.network_rule_saved` | `core.src.context.NetworkRuleSaved` | 2 | 2 | 1 | 3 | `core/src/context/network_rule_saved.rs` | `core/src/context/NetworkRuleSaved.kt` |
| 1159 | `context.plugin_instructions` | `core.src.context.PluginInstructions` | 2 | 2 | 1 | 3 | `core/src/context/plugin_instructions.rs` | `core/src/context/PluginInstructions.kt` |
| 1160 | `context.realtime_end_instructions` | `core.src.context.RealtimeEndInstructions` | 2 | 2 | 1 | 3 | `core/src/context/realtime_end_instructions.rs` | `core/src/context/RealtimeEndInstructions.kt` |
| 1161 | `context.realtime_start_with_instructions` | `core.src.context.RealtimeStartWithInstructions` | 2 | 2 | 1 | 3 | `core/src/context/realtime_start_with_instructions.rs` | `core/src/context/RealtimeStartWithInstructions.kt` |
| 1162 | `context.skill_instructions` | `core.src.context.SkillInstructions` | 2 | 2 | 1 | 3 | `core/src/context/skill_instructions.rs` | `core/src/context/SkillInstructions.kt` |
| 1163 | `chatwidget.session_header` | `tui.src.chatwidget.SessionHeader` | 1 | 2 | 1 | 3 | `tui/src/chatwidget/session_header.rs` | `tui/src/chatwidget/SessionHeader.kt` |
| 1164 | `rollout-trace.payload` | `rollouttrace.src.Payload` | 1 | 0 | 3 | 3 | `rollout-trace/src/payload.rs` | `rollouttrace/src/Payload.kt` |
| 1165 | `tools.tool_registry_plan` | `tools.src.ToolRegistryPlan` | 1 | 3 | 0 | 3 | `tools/src/tool_registry_plan.rs` | `tools/src/ToolRegistryPlan.kt` |
| 1166 | `tui.main` | `tui.src.Main` | 1 | 2 | 1 | 3 | `tui/src/main.rs` | `tui/src/Main.kt` |
| 1167 | `unified_exec.process_state` | `core.src.unifiedexec.ProcessState` | 1 | 2 | 1 | 3 | `core/src/unified_exec/process_state.rs` | `core/src/unifiedexec/ProcessState.kt` |
| 1168 | `windows-sandbox-rs.dpapi` | `windowssandboxrs.src.Dpapi` | 1 | 3 | 0 | 3 | `windows-sandbox-rs/src/dpapi.rs` | `windowssandboxrs/src/Dpapi.kt` |
| 1169 | `app-server.dynamic_tools` | `appserver.src.DynamicTools` | 0 | 3 | 0 | 3 | `app-server/src/dynamic_tools.rs` | `appserver/src/DynamicTools.kt` |
| 1170 | `app-server.server_request_error` | `appserver.src.ServerRequestError` | 0 | 3 | 0 | 3 | `app-server/src/server_request_error.rs` | `appserver/src/ServerRequestError.kt` |
| 1171 | `app.test_support` | `tui.src.app.TestSupport` | 0 | 3 | 0 | 3 | `tui/src/app/test_support.rs` | `tui/src/app/TestSupport.kt` |
| 1172 | `auth.util` | `login.src.auth.Util` | 0 | 3 | 0 | 3 | `login/src/auth/util.rs` | `login/src/auth/Util.kt` |
| 1173 | `bottom_pane.popup_consts` | `tui.src.bottompane.PopupConsts` | 0 | 3 | 0 | 3 | `tui/src/bottom_pane/popup_consts.rs` | `tui/src/bottompane/PopupConsts.kt` |
| 1174 | `chatwidget.side` | `tui.src.chatwidget.Side` | 0 | 3 | 0 | 3 | `tui/src/chatwidget/side.rs` | `tui/src/chatwidget/Side.kt` |
| 1175 | `cli.shared_options` | `utils.cli.src.SharedOptions` | 0 | 2 | 1 | 3 | `utils/cli/src/shared_options.rs` | `utils/cli/src/SharedOptions.kt` |
| 1176 | `cloud-tasks.new_task` | `cloudtasks.src.NewTask` | 0 | 2 | 1 | 3 | `cloud-tasks/src/new_task.rs` | `cloudtasks/src/NewTask.kt` |
| 1177 | `common.mock_model_server` | `mcpserver.tests.common.MockModelServer` | 0 | 2 | 1 | 3 | `mcp-server/tests/common/mock_model_server.rs` | `mcpserver/tests/common/MockModelServer.kt` |
| 1178 | `common.models_cache` | `appserver.tests.common.ModelsCache` | 0 | 3 | 0 | 3 | `app-server/tests/common/models_cache.rs` | `appserver/tests/common/ModelsCache.kt` |
| 1179 | `config.fingerprint` | `config.src.Fingerprint` | 0 | 3 | 0 | 3 | `config/src/fingerprint.rs` | `config/src/Fingerprint.kt` |
| 1180 | `config.key_aliases` | `config.src.KeyAliases` | 0 | 2 | 1 | 3 | `config/src/key_aliases.rs` | `config/src/KeyAliases.kt` |
| 1181 | `config.overrides` | `config.src.Overrides` | 0 | 3 | 0 | 3 | `config/src/overrides.rs` | `config/src/Overrides.kt` |
| 1182 | `config.schema_tests` | `core.src.config.SchemaTests` | 0 | 3 | 0 | 3 | `core/src/config/schema_tests.rs` | `core/src/config/SchemaTests.kt` |
| 1183 | `context.contextual_user_message` | `core.src.context.ContextualUserMessage` | 0 | 3 | 0 | 3 | `core/src/context/contextual_user_message.rs` | `core/src/context/ContextualUserMessage.kt` |
| 1184 | `context.user_shell_command` | `core.src.context.UserShellCommand` | 0 | 2 | 1 | 3 | `core/src/context/user_shell_command.rs` | `core/src/context/UserShellCommand.kt` |
| 1185 | `core-plugins.installed_marketplaces` | `coreplugins.src.InstalledMarketplaces` | 0 | 3 | 0 | 3 | `core-plugins/src/installed_marketplaces.rs` | `coreplugins/src/InstalledMarketplaces.kt` |
| 1186 | `core-plugins.toggles` | `coreplugins.src.Toggles` | 0 | 3 | 0 | 3 | `core-plugins/src/toggles.rs` | `coreplugins/src/Toggles.kt` |
| 1187 | `core.commit_attribution` | `core.src.CommitAttribution` | 0 | 3 | 0 | 3 | `core/src/commit_attribution.rs` | `core/src/CommitAttribution.kt` |
| 1188 | `core.exec_env` | `core.src.ExecEnv` | 0 | 3 | 0 | 3 | `core/src/exec_env.rs` | `core/src/ExecEnv.kt` |
| 1189 | `core.mcp_tool_exposure` | `core.src.McpToolExposure` | 0 | 2 | 1 | 3 | `core/src/mcp_tool_exposure.rs` | `core/src/McpToolExposure.kt` |
| 1190 | `core.sandbox_tags` | `core.src.SandboxTags` | 0 | 3 | 0 | 3 | `core/src/sandbox_tags.rs` | `core/src/SandboxTags.kt` |
| 1191 | `core.user_shell_command_tests` | `core.src.UserShellCommandTests` | 0 | 3 | 0 | 3 | `core/src/user_shell_command_tests.rs` | `core/src/UserShellCommandTests.kt` |
| 1192 | `core.web_search` | `core.src.WebSearch` | 0 | 3 | 0 | 3 | `core/src/web_search.rs` | `core/src/WebSearch.kt` |
| 1193 | `debug-client.state` | `debugclient.src.State` | 0 | 0 | 3 | 3 | `debug-client/src/state.rs` | `debugclient/src/State.kt` |
| 1194 | `elevated.cwd_junction` | `windowssandboxrs.src.elevated.CwdJunction` | 0 | 3 | 0 | 3 | `windows-sandbox-rs/src/elevated/cwd_junction.rs` | `windowssandboxrs/src/elevated/CwdJunction.kt` |
| 1195 | `exec-server.client_api` | `execserver.src.ClientApi` | 0 | 0 | 3 | 3 | `exec-server/src/client_api.rs` | `execserver/src/ClientApi.kt` |
| 1196 | `extensions.prune` | `memories.write.src.extensions.Prune` | 0 | 3 | 0 | 3 | `memories/write/src/extensions/prune.rs` | `memories/write/src/extensions/Prune.kt` |
| 1197 | `image.error` | `utils.image.src.Error` | 0 | 2 | 1 | 3 | `utils/image/src/error.rs` | `utils/image/src/Error.kt` |
| 1198 | `linux-sandbox.build` | `linuxsandbox.Build` | 0 | 3 | 0 | 3 | `linux-sandbox/build.rs` | `linuxsandbox/Build.kt` |
| 1199 | `local.archive_thread` | `threadstore.src.local.ArchiveThread` | 0 | 3 | 0 | 3 | `thread-store/src/local/archive_thread.rs` | `threadstore/src/local/ArchiveThread.kt` |
| 1200 | `local.unarchive_thread` | `threadstore.src.local.UnarchiveThread` | 0 | 3 | 0 | 3 | `thread-store/src/local/unarchive_thread.rs` | `threadstore/src/local/UnarchiveThread.kt` |
| 1201 | `mcp.schema` | `memories.mcp.src.Schema` | 0 | 3 | 0 | 3 | `memories/mcp/src/schema.rs` | `memories/mcp/src/Schema.kt` |
| 1202 | `model.log` | `state.src.model.Log` | 0 | 0 | 3 | 3 | `state/src/model/log.rs` | `state/src/model/Log.kt` |
| 1203 | `protocol.plan_tool` | `protocol.src.PlanTool` | 0 | 0 | 3 | 3 | `protocol/src/plan_tool.rs` | `protocol/src/PlanTool.kt` |
| 1204 | `read.citations_tests` | `memories.read.src.CitationsTests` | 0 | 3 | 0 | 3 | `memories/read/src/citations_tests.rs` | `memories/read/src/CitationsTests.kt` |
| 1205 | `reducer.inference_tests` | `rollouttrace.src.reducer.InferenceTests` | 0 | 3 | 0 | 3 | `rollout-trace/src/reducer/inference_tests.rs` | `rollouttrace/src/reducer/InferenceTests.kt` |
| 1206 | `rollout.state_db_tests` | `rollout.src.StateDbTests` | 0 | 3 | 0 | 3 | `rollout/src/state_db_tests.rs` | `rollout/src/StateDbTests.kt` |
| 1207 | `runtime.device_key` | `state.src.runtime.DeviceKey` | 0 | 2 | 1 | 3 | `state/src/runtime/device_key.rs` | `state/src/runtime/DeviceKey.kt` |
| 1208 | `sandboxing.landlock` | `sandboxing.src.Landlock` | 0 | 3 | 0 | 3 | `sandboxing/src/landlock.rs` | `sandboxing/src/Landlock.kt` |
| 1209 | `secrets.sanitizer` | `secrets.src.Sanitizer` | 0 | 3 | 0 | 3 | `secrets/src/sanitizer.rs` | `secrets/src/Sanitizer.kt` |
| 1210 | `state.migrations` | `state.src.Migrations` | 0 | 3 | 0 | 3 | `state/src/migrations.rs` | `state/src/Migrations.kt` |
| 1211 | `suite.abort_tasks` | `core.tests.suite.AbortTasks` | 0 | 3 | 0 | 3 | `core/tests/suite/abort_tasks.rs` | `core/tests/suite/AbortTasks.kt` |
| 1212 | `suite.apply_patch` | `exec.tests.suite.ApplyPatch` | 0 | 3 | 0 | 3 | `exec/tests/suite/apply_patch.rs` | `exec/tests/suite/ApplyPatch.kt` |
| 1213 | `suite.cli` | `applypatch.tests.suite.Cli` | 0 | 3 | 0 | 3 | `apply-patch/tests/suite/cli.rs` | `applypatch/tests/suite/Cli.kt` |
| 1214 | `suite.codex_delegate` | `core.tests.suite.CodexDelegate` | 0 | 3 | 0 | 3 | `core/tests/suite/codex_delegate.rs` | `core/tests/suite/CodexDelegate.kt` |
| 1215 | `suite.ephemeral` | `exec.tests.suite.Ephemeral` | 0 | 3 | 0 | 3 | `exec/tests/suite/ephemeral.rs` | `exec/tests/suite/Ephemeral.kt` |
| 1216 | `suite.fork_thread` | `core.tests.suite.ForkThread` | 0 | 3 | 0 | 3 | `core/tests/suite/fork_thread.rs` | `core/tests/suite/ForkThread.kt` |
| 1217 | `suite.json_result` | `core.tests.suite.JsonResult` | 0 | 3 | 0 | 3 | `core/tests/suite/json_result.rs` | `core/tests/suite/JsonResult.kt` |
| 1218 | `suite.manager_metrics` | `otel.tests.suite.ManagerMetrics` | 0 | 3 | 0 | 3 | `otel/tests/suite/manager_metrics.rs` | `otel/tests/suite/ManagerMetrics.kt` |
| 1219 | `suite.no_panic_on_startup` | `tui.tests.suite.NoPanicOnStartup` | 0 | 2 | 1 | 3 | `tui/tests/suite/no_panic_on_startup.rs` | `tui/tests/suite/NoPanicOnStartup.kt` |
| 1220 | `tests.debug_models` | `cli.tests.DebugModels` | 0 | 3 | 0 | 3 | `cli/tests/debug_models.rs` | `cli/tests/DebugModels.kt` |
| 1221 | `tests.marketplace_upgrade` | `cli.tests.MarketplaceUpgrade` | 0 | 3 | 0 | 3 | `cli/tests/marketplace_upgrade.rs` | `cli/tests/MarketplaceUpgrade.kt` |
| 1222 | `tests.process` | `execserver.tests.Process` | 0 | 3 | 0 | 3 | `exec-server/tests/process.rs` | `execserver/tests/Process.kt` |
| 1223 | `tests.resources` | `rmcpclient.tests.Resources` | 0 | 3 | 0 | 3 | `rmcp-client/tests/resources.rs` | `rmcpclient/tests/Resources.kt` |
| 1224 | `tools.apply_patch_tool` | `tools.src.ApplyPatchTool` | 0 | 2 | 1 | 3 | `tools/src/apply_patch_tool.rs` | `tools/src/ApplyPatchTool.kt` |
| 1225 | `tools.mcp_resource_tool` | `tools.src.McpResourceTool` | 0 | 3 | 0 | 3 | `tools/src/mcp_resource_tool.rs` | `tools/src/McpResourceTool.kt` |
| 1226 | `tools.mcp_resource_tool_tests` | `tools.src.McpResourceToolTests` | 0 | 3 | 0 | 3 | `tools/src/mcp_resource_tool_tests.rs` | `tools/src/McpResourceToolTests.kt` |
| 1227 | `tools.tool_definition_tests` | `tools.src.ToolDefinitionTests` | 0 | 3 | 0 | 3 | `tools/src/tool_definition_tests.rs` | `tools/src/ToolDefinitionTests.kt` |
| 1228 | `tools.view_image` | `tools.src.ViewImage` | 0 | 2 | 1 | 3 | `tools/src/view_image.rs` | `tools/src/ViewImage.kt` |
| 1229 | `tui.line_truncation` | `tui.src.LineTruncation` | 0 | 3 | 0 | 3 | `tui/src/line_truncation.rs` | `tui/src/LineTruncation.kt` |
| 1230 | `tui.shimmer` | `tui.src.Shimmer` | 0 | 3 | 0 | 3 | `tui/src/shimmer.rs` | `tui/src/Shimmer.kt` |
| 1231 | `unified_exec.async_watcher_tests` | `core.src.unifiedexec.AsyncWatcherTests` | 0 | 3 | 0 | 3 | `core/src/unified_exec/async_watcher_tests.rs` | `core/src/unifiedexec/AsyncWatcherTests.kt` |
| 1232 | `v2.device_key` | `appserver.tests.suite.v2.DeviceKey` | 0 | 3 | 0 | 3 | `app-server/tests/suite/v2/device_key.rs` | `appserver/tests/suite/v2/DeviceKey.kt` |
| 1233 | `v2.output_schema` | `appserver.tests.suite.v2.OutputSchema` | 0 | 3 | 0 | 3 | `app-server/tests/suite/v2/output_schema.rs` | `appserver/tests/suite/v2/OutputSchema.kt` |
| 1234 | `v2.thread_status` | `appserver.tests.suite.v2.ThreadStatus` | 0 | 3 | 0 | 3 | `app-server/tests/suite/v2/thread_status.rs` | `appserver/tests/suite/v2/ThreadStatus.kt` |
| 1235 | `v2.turn_steer` | `appserver.tests.suite.v2.TurnSteer` | 0 | 3 | 0 | 3 | `app-server/tests/suite/v2/turn_steer.rs` | `appserver/tests/suite/v2/TurnSteer.kt` |
| 1236 | `windows-sandbox-rs.path_normalization` | `windowssandboxrs.src.PathNormalization` | 0 | 3 | 0 | 3 | `windows-sandbox-rs/src/path_normalization.rs` | `windowssandboxrs/src/PathNormalization.kt` |
| 1237 | `windows-sandbox-rs.sandbox_utils` | `windowssandboxrs.src.SandboxUtils` | 0 | 3 | 0 | 3 | `windows-sandbox-rs/src/sandbox_utils.rs` | `windowssandboxrs/src/SandboxUtils.kt` |
| 1238 | `write.prompts_tests` | `memories.write.src.PromptsTests` | 0 | 3 | 0 | 3 | `memories/write/src/prompts_tests.rs` | `memories/write/src/PromptsTests.kt` |
| 1239 | `transport.stdio` | `appservertransport.src.transport.Stdio` | 33 | 2 | 0 | 2 | `app-server-transport/src/transport/stdio.rs` | `appservertransport/src/transport/Stdio.kt` |
| 1240 | `code-mode.response` | `codemode.src.Response` | 28 | 0 | 2 | 2 | `code-mode/src/response.rs` | `codemode/src/Response.kt` |
| 1241 | `suite.originator` | `exec.tests.suite.Originator` | 14 | 2 | 0 | 2 | `exec/tests/suite/originator.rs` | `exec/tests/suite/Originator.kt` |
| 1242 | `models.config_file_response` | `codexbackendopenapimodels.src.models.ConfigFileResponse` | 4 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/config_file_response.rs` | `codexbackendopenapimodels/src/models/ConfigFileResponse.kt` |
| 1243 | `models.paginated_list_task_list_item_` | `codexbackendopenapimodels.src.models.PaginatedListTaskListItem` | 4 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/paginated_list_task_list_item_.rs` | `codexbackendopenapimodels/src/models/PaginatedListTaskListItem.kt` |
| 1244 | `otel.targets` | `otel.src.Targets` | 4 | 2 | 0 | 2 | `otel/src/targets.rs` | `otel/src/Targets.kt` |
| 1245 | `suite.request_compression` | `core.tests.suite.RequestCompression` | 4 | 2 | 0 | 2 | `core/tests/suite/request_compression.rs` | `core/tests/suite/RequestCompression.kt` |
| 1246 | `agent.status` | `core.src.agent.Status` | 3 | 2 | 0 | 2 | `core/src/agent/status.rs` | `core/src/agent/Status.kt` |
| 1247 | `bin.write_schema_fixtures` | `appserverprotocol.src.bin.WriteSchemaFixtures` | 3 | 1 | 1 | 2 | `app-server-protocol/src/bin/write_schema_fixtures.rs` | `appserverprotocol/src/bin/WriteSchemaFixtures.kt` |
| 1248 | `models.code_task_details_response` | `codexbackendopenapimodels.src.models.CodeTaskDetailsResponse` | 3 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/code_task_details_response.rs` | `codexbackendopenapimodels/src/models/CodeTaskDetailsResponse.kt` |
| 1249 | `models.task_list_item` | `codexbackendopenapimodels.src.models.TaskListItem` | 3 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/task_list_item.rs` | `codexbackendopenapimodels/src/models/TaskListItem.kt` |
| 1250 | `protocol.memory_citation` | `protocol.src.MemoryCitation` | 3 | 0 | 2 | 2 | `protocol/src/memory_citation.rs` | `protocol/src/MemoryCitation.kt` |
| 1251 | `context.guardian_followup_review_reminder` | `core.src.context.GuardianFollowupReviewReminder` | 2 | 1 | 1 | 2 | `core/src/context/guardian_followup_review_reminder.rs` | `core/src/context/GuardianFollowupReviewReminder.kt` |
| 1252 | `context.realtime_start_instructions` | `core.src.context.RealtimeStartInstructions` | 2 | 1 | 1 | 2 | `core/src/context/realtime_start_instructions.rs` | `core/src/context/RealtimeStartInstructions.kt` |
| 1253 | `models.additional_rate_limit_details` | `codexbackendopenapimodels.src.models.AdditionalRateLimitDetails` | 2 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/additional_rate_limit_details.rs` | `codexbackendopenapimodels/src/models/AdditionalRateLimitDetails.kt` |
| 1254 | `models.credit_status_details` | `codexbackendopenapimodels.src.models.CreditStatusDetails` | 2 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/credit_status_details.rs` | `codexbackendopenapimodels/src/models/CreditStatusDetails.kt` |
| 1255 | `models.rate_limit_status_details` | `codexbackendopenapimodels.src.models.RateLimitStatusDetails` | 2 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/rate_limit_status_details.rs` | `codexbackendopenapimodels/src/models/RateLimitStatusDetails.kt` |
| 1256 | `models.rate_limit_window_snapshot` | `codexbackendopenapimodels.src.models.RateLimitWindowSnapshot` | 2 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/rate_limit_window_snapshot.rs` | `codexbackendopenapimodels/src/models/RateLimitWindowSnapshot.kt` |
| 1257 | `suite.parse_sed_command` | `execpolicylegacy.tests.suite.ParseSedCommand` | 2 | 2 | 0 | 2 | `execpolicy-legacy/tests/suite/parse_sed_command.rs` | `execpolicylegacy/tests/suite/ParseSedCommand.kt` |
| 1258 | `suite.turn_state` | `core.tests.suite.TurnState` | 2 | 2 | 0 | 2 | `core/tests/suite/turn_state.rs` | `core/tests/suite/TurnState.kt` |
| 1259 | `tui.session_state` | `tui.src.SessionState` | 2 | 0 | 2 | 2 | `tui/src/session_state.rs` | `tui/src/SessionState.kt` |
| 1260 | `local.create_thread` | `threadstore.src.local.CreateThread` | 1 | 2 | 0 | 2 | `thread-store/src/local/create_thread.rs` | `threadstore/src/local/CreateThread.kt` |
| 1261 | `models.external_pull_request_response` | `codexbackendopenapimodels.src.models.ExternalPullRequestResponse` | 1 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/external_pull_request_response.rs` | `codexbackendopenapimodels/src/models/ExternalPullRequestResponse.kt` |
| 1262 | `models.git_pull_request` | `codexbackendopenapimodels.src.models.GitPullRequest` | 1 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/git_pull_request.rs` | `codexbackendopenapimodels/src/models/GitPullRequest.kt` |
| 1263 | `models.task_response` | `codexbackendopenapimodels.src.models.TaskResponse` | 1 | 1 | 1 | 2 | `codex-backend-openapi-models/src/models/task_response.rs` | `codexbackendopenapimodels/src/models/TaskResponse.kt` |
| 1264 | `agent-graph-store.types` | `agentgraphstore.src.Types` | 0 | 1 | 1 | 2 | `agent-graph-store/src/types.rs` | `agentgraphstore/src/Types.kt` |
| 1265 | `agent.agent_resolver` | `core.src.agent.AgentResolver` | 0 | 2 | 0 | 2 | `core/src/agent/agent_resolver.rs` | `core/src/agent/AgentResolver.kt` |
| 1266 | `app.replay_filter` | `tui.src.app.ReplayFilter` | 0 | 2 | 0 | 2 | `tui/src/app/replay_filter.rs` | `tui/src/app/ReplayFilter.kt` |
| 1267 | `apply-patch.standalone_executable` | `applypatch.src.StandaloneExecutable` | 0 | 2 | 0 | 2 | `apply-patch/src/standalone_executable.rs` | `applypatch/src/StandaloneExecutable.kt` |
| 1268 | `aws-auth.signing` | `awsauth.src.Signing` | 0 | 2 | 0 | 2 | `aws-auth/src/signing.rs` | `awsauth/src/Signing.kt` |
| 1269 | `bin.command_runner` | `windowssandboxrs.src.bin.CommandRunner` | 0 | 2 | 0 | 2 | `windows-sandbox-rs/src/bin/command_runner.rs` | `windowssandboxrs/src/bin/CommandRunner.kt` |
| 1270 | `bin.config_schema` | `core.src.bin.ConfigSchema` | 0 | 1 | 1 | 2 | `core/src/bin/config_schema.rs` | `core/src/bin/ConfigSchema.kt` |
| 1271 | `bin.export` | `appserverprotocol.src.bin.Export` | 0 | 1 | 1 | 2 | `app-server-protocol/src/bin/export.rs` | `appserverprotocol/src/bin/Export.kt` |
| 1272 | `bin.setup_main` | `windowssandboxrs.src.bin.SetupMain` | 0 | 2 | 0 | 2 | `windows-sandbox-rs/src/bin/setup_main.rs` | `windowssandboxrs/src/bin/SetupMain.kt` |
| 1273 | `chatgpt.chatgpt_client` | `chatgpt.src.ChatgptClient` | 0 | 2 | 0 | 2 | `chatgpt/src/chatgpt_client.rs` | `chatgpt/src/ChatgptClient.kt` |
| 1274 | `chatgpt.workspace_settings_tests` | `chatgpt.src.WorkspaceSettingsTests` | 0 | 2 | 0 | 2 | `chatgpt/src/workspace_settings_tests.rs` | `chatgpt/src/WorkspaceSettingsTests.kt` |
| 1275 | `chatwidget.hooks` | `tui.src.chatwidget.Hooks` | 0 | 2 | 0 | 2 | `tui/src/chatwidget/hooks.rs` | `tui/src/chatwidget/Hooks.kt` |
| 1276 | `cli.app_cmd` | `cli.src.AppCmd` | 0 | 1 | 1 | 2 | `cli/src/app_cmd.rs` | `cli/src/AppCmd.kt` |
| 1277 | `codex-client.chatgpt_hosts` | `codexclient.src.ChatgptHosts` | 0 | 2 | 0 | 2 | `codex-client/src/chatgpt_hosts.rs` | `codexclient/src/ChatgptHosts.kt` |
| 1278 | `codex-client.error` | `codexclient.src.Error` | 0 | 0 | 2 | 2 | `codex-client/src/error.rs` | `codexclient/src/Error.kt` |
| 1279 | `common.config` | `appserver.tests.common.Config` | 0 | 2 | 0 | 2 | `app-server/tests/common/config.rs` | `appserver/tests/common/Config.kt` |
| 1280 | `common.tracing` | `core.tests.common.Tracing` | 0 | 1 | 1 | 2 | `core/tests/common/tracing.rs` | `core/tests/common/Tracing.kt` |
| 1281 | `config.merge` | `config.src.Merge` | 0 | 2 | 0 | 2 | `config/src/merge.rs` | `config/src/Merge.kt` |
| 1282 | `config.profile_toml` | `config.src.ProfileToml` | 0 | 1 | 1 | 2 | `config/src/profile_toml.rs` | `config/src/ProfileToml.kt` |
| 1283 | `config.project_root_markers` | `config.src.ProjectRootMarkers` | 0 | 2 | 0 | 2 | `config/src/project_root_markers.rs` | `config/src/ProjectRootMarkers.kt` |
| 1284 | `connectors.accessible` | `connectors.src.Accessible` | 0 | 1 | 1 | 2 | `connectors/src/accessible.rs` | `connectors/src/Accessible.kt` |
| 1285 | `core-skills.env_var_dependencies` | `coreskills.src.EnvVarDependencies` | 0 | 1 | 1 | 2 | `core-skills/src/env_var_dependencies.rs` | `coreskills/src/EnvVarDependencies.kt` |
| 1286 | `core.memory_usage` | `core.src.MemoryUsage` | 0 | 2 | 0 | 2 | `core/src/memory_usage.rs` | `core/src/MemoryUsage.kt` |
| 1287 | `core.otel_init` | `core.src.OtelInit` | 0 | 2 | 0 | 2 | `core/src/otel_init.rs` | `core/src/OtelInit.kt` |
| 1288 | `core.prompt_debug` | `core.src.PromptDebug` | 0 | 2 | 0 | 2 | `core/src/prompt_debug.rs` | `core/src/PromptDebug.kt` |
| 1289 | `core.session_prefix` | `core.src.SessionPrefix` | 0 | 2 | 0 | 2 | `core/src/session_prefix.rs` | `core/src/SessionPrefix.kt` |
| 1290 | `core.session_rollout_init_error` | `core.src.SessionRolloutInitError` | 0 | 2 | 0 | 2 | `core/src/session_rollout_init_error.rs` | `core/src/SessionRolloutInitError.kt` |
| 1291 | `exec-server.fs_helper_main` | `execserver.src.FsHelperMain` | 0 | 2 | 0 | 2 | `exec-server/src/fs_helper_main.rs` | `execserver/src/FsHelperMain.kt` |
| 1292 | `exec.main` | `exec.src.Main` | 0 | 1 | 1 | 2 | `exec/src/main.rs` | `exec/src/Main.kt` |
| 1293 | `execpolicy.executable_name` | `execpolicy.src.ExecutableName` | 0 | 2 | 0 | 2 | `execpolicy/src/executable_name.rs` | `execpolicy/src/ExecutableName.kt` |
| 1294 | `execpolicy.main` | `execpolicy.src.Main` | 0 | 1 | 1 | 2 | `execpolicy/src/main.rs` | `execpolicy/src/Main.kt` |
| 1295 | `extensions.prune_tests` | `memories.write.src.extensions.PruneTests` | 0 | 2 | 0 | 2 | `memories/write/src/extensions/prune_tests.rs` | `memories/write/src/extensions/PruneTests.kt` |
| 1296 | `git-utils.platform` | `gitutils.src.Platform` | 0 | 2 | 0 | 2 | `git-utils/src/platform.rs` | `gitutils/src/Platform.kt` |
| 1297 | `handlers.multi_agents` | `core.src.tools.handlers.multiagents.MultiAgents` | 0 | 2 | 0 | 2 | `core/src/tools/handlers/multi_agents.rs` | `core/src/tools/handlers/multiagents/MultiAgents.kt` |
| 1298 | `metrics.error` | `otel.src.metrics.Error` | 0 | 0 | 2 | 2 | `otel/src/metrics/error.rs` | `otel/src/metrics/Error.kt` |
| 1299 | `models-manager.collaboration_mode_presets_tests` | `modelsmanager.src.CollaborationModePresetsTests` | 0 | 2 | 0 | 2 | `models-manager/src/collaboration_mode_presets_tests.rs` | `modelsmanager/src/CollaborationModePresetsTests.kt` |
| 1300 | `models-manager.model_info_overrides_tests` | `modelsmanager.src.ModelInfoOverridesTests` | 0 | 2 | 0 | 2 | `models-manager/src/model_info_overrides_tests.rs` | `modelsmanager/src/ModelInfoOverridesTests.kt` |
| 1301 | `models-manager.test_support` | `modelsmanager.src.TestSupport` | 0 | 2 | 0 | 2 | `models-manager/src/test_support.rs` | `modelsmanager/src/TestSupport.kt` |
| 1302 | `plugins.render` | `core.src.plugins.Render` | 0 | 2 | 0 | 2 | `core/src/plugins/render.rs` | `core/src/plugins/Render.kt` |
| 1303 | `plugins.render_tests` | `core.src.plugins.RenderTests` | 0 | 2 | 0 | 2 | `core/src/plugins/render_tests.rs` | `core/src/plugins/RenderTests.kt` |
| 1304 | `protocol.common_tests` | `appserverprotocol.src.protocol.CommonTests` | 0 | 2 | 0 | 2 | `app-server-protocol/src/protocol/common_tests.rs` | `appserverprotocol/src/protocol/CommonTests.kt` |
| 1305 | `protocol.network_policy` | `protocol.src.NetworkPolicy` | 0 | 1 | 1 | 2 | `protocol/src/network_policy.rs` | `protocol/src/NetworkPolicy.kt` |
| 1306 | `protocol.serde_helpers` | `appserverprotocol.src.protocol.SerdeHelpers` | 0 | 2 | 0 | 2 | `app-server-protocol/src/protocol/serde_helpers.rs` | `appserverprotocol/src/protocol/SerdeHelpers.kt` |
| 1307 | `read.prompts` | `memories.read.src.Prompts` | 0 | 2 | 0 | 2 | `memories/read/src/prompts.rs` | `memories/read/src/Prompts.kt` |
| 1308 | `realtime_websocket.protocol_v1` | `codexapi.src.endpoint.realtimewebsocket.ProtocolV1` | 0 | 2 | 0 | 2 | `codex-api/src/endpoint/realtime_websocket/protocol_v1.rs` | `codexapi/src/endpoint/realtimewebsocket/ProtocolV1.kt` |
| 1309 | `request_processors.config_errors` | `appserver.src.requestprocessors.ConfigErrors` | 0 | 2 | 0 | 2 | `app-server/src/request_processors/config_errors.rs` | `appserver/src/requestprocessors/ConfigErrors.kt` |
| 1310 | `request_processors.external_agent_config_processor_tests` | `appserver.src.requestprocessors.ExternalAgentConfigProcessorTests` | 0 | 2 | 0 | 2 | `app-server/src/request_processors/external_agent_config_processor_tests.rs` | `appserver/src/requestprocessors/ExternalAgentConfigProcessorTests.kt` |
| 1311 | `requests.responses` | `codexapi.src.requests.Responses` | 0 | 1 | 1 | 2 | `codex-api/src/requests/responses.rs` | `codexapi/src/requests/Responses.kt` |
| 1312 | `responses-api-proxy.main` | `responsesapiproxy.src.Main` | 0 | 2 | 0 | 2 | `responses-api-proxy/src/main.rs` | `responsesapiproxy/src/Main.kt` |
| 1313 | `rollout-trace.bundle` | `rollouttrace.src.Bundle` | 0 | 1 | 1 | 2 | `rollout-trace/src/bundle.rs` | `rollouttrace/src/Bundle.kt` |
| 1314 | `runtime.device_key_tests` | `state.src.runtime.DeviceKeyTests` | 0 | 2 | 0 | 2 | `state/src/runtime/device_key_tests.rs` | `state/src/runtime/DeviceKeyTests.kt` |
| 1315 | `runtime.test_support` | `state.src.runtime.TestSupport` | 0 | 2 | 0 | 2 | `state/src/runtime/test_support.rs` | `state/src/runtime/TestSupport.kt` |
| 1316 | `shell-command.shell_detect` | `shellcommand.src.ShellDetect` | 0 | 1 | 1 | 2 | `shell-command/src/shell_detect.rs` | `shellcommand/src/ShellDetect.kt` |
| 1317 | `skills.build` | `skills.Build` | 0 | 2 | 0 | 2 | `skills/build.rs` | `skills/Build.kt` |
| 1318 | `suite.add_dir` | `exec.tests.suite.AddDir` | 0 | 2 | 0 | 2 | `exec/tests/suite/add_dir.rs` | `exec/tests/suite/AddDir.kt` |
| 1319 | `suite.hierarchical_agents` | `core.tests.suite.HierarchicalAgents` | 0 | 2 | 0 | 2 | `core/tests/suite/hierarchical_agents.rs` | `core/tests/suite/HierarchicalAgents.kt` |
| 1320 | `suite.model_overrides` | `core.tests.suite.ModelOverrides` | 0 | 2 | 0 | 2 | `core/tests/suite/model_overrides.rs` | `core/tests/suite/ModelOverrides.kt` |
| 1321 | `suite.resume_warning` | `core.tests.suite.ResumeWarning` | 0 | 2 | 0 | 2 | `core/tests/suite/resume_warning.rs` | `core/tests/suite/ResumeWarning.kt` |
| 1322 | `suite.snapshot` | `otel.tests.suite.Snapshot` | 0 | 2 | 0 | 2 | `otel/tests/suite/snapshot.rs` | `otel/tests/suite/Snapshot.kt` |
| 1323 | `suite.stream_no_completed` | `core.tests.suite.StreamNoCompleted` | 0 | 2 | 0 | 2 | `core/tests/suite/stream_no_completed.rs` | `core/tests/suite/StreamNoCompleted.kt` |
| 1324 | `suite.timing` | `otel.tests.suite.Timing` | 0 | 2 | 0 | 2 | `otel/tests/suite/timing.rs` | `otel/tests/suite/Timing.kt` |
| 1325 | `suite.unstable_features_warning` | `core.tests.suite.UnstableFeaturesWarning` | 0 | 2 | 0 | 2 | `core/tests/suite/unstable_features_warning.rs` | `core/tests/suite/UnstableFeaturesWarning.kt` |
| 1326 | `tests.debug_clear_memories` | `cli.tests.DebugClearMemories` | 0 | 2 | 0 | 2 | `cli/tests/debug_clear_memories.rs` | `cli/tests/DebugClearMemories.kt` |
| 1327 | `tests.execpolicy` | `cli.tests.Execpolicy` | 0 | 2 | 0 | 2 | `cli/tests/execpolicy.rs` | `cli/tests/Execpolicy.kt` |
| 1328 | `tests.manager_dependency_regression` | `tui.tests.ManagerDependencyRegression` | 0 | 2 | 0 | 2 | `tui/tests/manager_dependency_regression.rs` | `tui/tests/ManagerDependencyRegression.kt` |
| 1329 | `tests.stdio_to_uds` | `stdiotouds.tests.StdioToUds` | 0 | 1 | 1 | 2 | `stdio-to-uds/tests/stdio_to_uds.rs` | `stdiotouds/tests/StdioToUds.kt` |
| 1330 | `tests.update` | `cli.tests.Update` | 0 | 2 | 0 | 2 | `cli/tests/update.rs` | `cli/tests/Update.kt` |
| 1331 | `tools.agent_job_tool` | `tools.src.AgentJobTool` | 0 | 2 | 0 | 2 | `tools/src/agent_job_tool.rs` | `tools/src/AgentJobTool.kt` |
| 1332 | `tools.agent_job_tool_tests` | `tools.src.AgentJobToolTests` | 0 | 2 | 0 | 2 | `tools/src/agent_job_tool_tests.rs` | `tools/src/AgentJobToolTests.kt` |
| 1333 | `tools.apply_patch_tool_tests` | `tools.src.ApplyPatchToolTests` | 0 | 2 | 0 | 2 | `tools/src/apply_patch_tool_tests.rs` | `tools/src/ApplyPatchToolTests.kt` |
| 1334 | `tools.dynamic_tool_tests` | `tools.src.DynamicToolTests` | 0 | 2 | 0 | 2 | `tools/src/dynamic_tool_tests.rs` | `tools/src/DynamicToolTests.kt` |
| 1335 | `tools.mcp_tool` | `tools.src.McpTool` | 0 | 2 | 0 | 2 | `tools/src/mcp_tool.rs` | `tools/src/McpTool.kt` |
| 1336 | `tools.utility_tool` | `tools.src.UtilityTool` | 0 | 2 | 0 | 2 | `tools/src/utility_tool.rs` | `tools/src/UtilityTool.kt` |
| 1337 | `tools.utility_tool_tests` | `tools.src.UtilityToolTests` | 0 | 2 | 0 | 2 | `tools/src/utility_tool_tests.rs` | `tools/src/UtilityToolTests.kt` |
| 1338 | `tui.permission_compat` | `tui.src.PermissionCompat` | 0 | 2 | 0 | 2 | `tui/src/permission_compat.rs` | `tui/src/PermissionCompat.kt` |
| 1339 | `tui.selection_list` | `tui.src.SelectionList` | 0 | 2 | 0 | 2 | `tui/src/selection_list.rs` | `tui/src/SelectionList.kt` |
| 1340 | `unix.execve_wrapper` | `shellescalation.src.unix.ExecveWrapper` | 0 | 1 | 1 | 2 | `shell-escalation/src/unix/execve_wrapper.rs` | `shellescalation/src/unix/ExecveWrapper.kt` |
| 1341 | `v2.model_provider_capabilities_read` | `appserver.tests.suite.v2.ModelProviderCapabilitiesRead` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/model_provider_capabilities_read.rs` | `appserver/tests/suite/v2/ModelProviderCapabilitiesRead.kt` |
| 1342 | `v2.request_permissions` | `appserver.tests.suite.v2.RequestPermissions` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/request_permissions.rs` | `appserver/tests/suite/v2/RequestPermissions.kt` |
| 1343 | `v2.request_user_input` | `appserver.tests.suite.v2.RequestUserInput` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/request_user_input.rs` | `appserver/tests/suite/v2/RequestUserInput.kt` |
| 1344 | `v2.thread_rollback` | `appserver.tests.suite.v2.ThreadRollback` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/thread_rollback.rs` | `appserver/tests/suite/v2/ThreadRollback.kt` |
| 1345 | `v2.windows_sandbox_setup` | `appserver.tests.suite.v2.WindowsSandboxSetup` | 0 | 2 | 0 | 2 | `app-server/tests/suite/v2/windows_sandbox_setup.rs` | `appserver/tests/suite/v2/WindowsSandboxSetup.kt` |
| 1346 | `windows-sandbox-rs.wfp_filter_specs` | `windowssandboxrs.src.WfpFilterSpecs` | 0 | 0 | 2 | 2 | `windows-sandbox-rs/src/wfp_filter_specs.rs` | `windowssandboxrs/src/WfpFilterSpecs.kt` |
| 1347 | `codex-client.sse` | `codexclient.src.Sse` | 66 | 1 | 0 | 1 | `codex-client/src/sse.rs` | `codexclient/src/Sse.kt` |
| 1348 | `path-utils.env` | `utils.pathutils.src.Env` | 23 | 1 | 0 | 1 | `utils/path-utils/src/env.rs` | `utils/pathutils/src/Env.kt` |
| 1349 | `tests.env_filter` | `cloudtasks.tests.EnvFilter` | 7 | 1 | 0 | 1 | `cloud-tasks/tests/env_filter.rs` | `cloudtasks/tests/EnvFilter.kt` |
| 1350 | `unix.escalation_policy` | `shellescalation.src.unix.EscalationPolicy` | 4 | 0 | 1 | 1 | `shell-escalation/src/unix/escalation_policy.rs` | `shellescalation/src/unix/EscalationPolicy.kt` |
| 1351 | `events.shared` | `otel.src.events.Shared` | 3 | 1 | 0 | 1 | `otel/src/events/shared.rs` | `otel/src/events/Shared.kt` |
| 1352 | `suite.model_availability_nux` | `tui.tests.suite.ModelAvailabilityNux` | 3 | 1 | 0 | 1 | `tui/tests/suite/model_availability_nux.rs` | `tui/tests/suite/ModelAvailabilityNux.kt` |
| 1353 | `bin.main_execve_wrapper` | `shellescalation.src.bin.MainExecveWrapper` | 2 | 1 | 0 | 1 | `shell-escalation/src/bin/main_execve_wrapper.rs` | `shellescalation/src/bin/MainExecveWrapper.kt` |
| 1354 | `core-skills.system` | `coreskills.src.System` | 1 | 1 | 0 | 1 | `core-skills/src/system.rs` | `coreskills/src/System.kt` |
| 1355 | `tests.websocket` | `execserver.tests.Websocket` | 1 | 1 | 0 | 1 | `exec-server/tests/websocket.rs` | `execserver/tests/Websocket.kt` |
| 1356 | `agent-graph-store.store` | `agentgraphstore.src.Store` | 0 | 0 | 1 | 1 | `agent-graph-store/src/store.rs` | `agentgraphstore/src/Store.kt` |
| 1357 | `app-server-test-client.main` | `appservertestclient.src.Main` | 0 | 1 | 0 | 1 | `app-server-test-client/src/main.rs` | `appservertestclient/src/Main.kt` |
| 1358 | `app-server.analytics_utils` | `appserver.src.AnalyticsUtils` | 0 | 1 | 0 | 1 | `app-server/src/analytics_utils.rs` | `appserver/src/AnalyticsUtils.kt` |
| 1359 | `app-server.request_processors` | `appserver.src.requestprocessors.RequestProcessors` | 0 | 1 | 0 | 1 | `app-server/src/request_processors.rs` | `appserver/src/requestprocessors/RequestProcessors.kt` |
| 1360 | `apply-patch.main` | `applypatch.src.Main` | 0 | 1 | 0 | 1 | `apply-patch/src/main.rs` | `applypatch/src/Main.kt` |
| 1361 | `backends.elevated` | `windowssandboxrs.src.unifiedexec.backends.Elevated` | 0 | 1 | 0 | 1 | `windows-sandbox-rs/src/unified_exec/backends/elevated.rs` | `windowssandboxrs/src/unifiedexec/backends/Elevated.kt` |
| 1362 | `bin.md-events` | `tui.src.bin.Md-events` | 0 | 1 | 0 | 1 | `tui/src/bin/md-events.rs` | `tui/src/bin/Md-events.kt` |
| 1363 | `bin.notify_capture` | `appserver.src.bin.NotifyCapture` | 0 | 1 | 0 | 1 | `app-server/src/bin/notify_capture.rs` | `appserver/src/bin/NotifyCapture.kt` |
| 1364 | `bin.test_notify_capture` | `appserver.src.bin.TestNotifyCapture` | 0 | 1 | 0 | 1 | `app-server/src/bin/test_notify_capture.rs` | `appserver/src/bin/TestNotifyCapture.kt` |
| 1365 | `bin.write_hooks_schema_fixtures` | `hooks.src.bin.WriteHooksSchemaFixtures` | 0 | 1 | 0 | 1 | `hooks/src/bin/write_hooks_schema_fixtures.rs` | `hooks/src/bin/WriteHooksSchemaFixtures.kt` |
| 1366 | `bottom_pane.action_required_title` | `tui.src.bottompane.ActionRequiredTitle` | 0 | 1 | 0 | 1 | `tui/src/bottom_pane/action_required_title.rs` | `tui/src/bottompane/ActionRequiredTitle.kt` |
| 1367 | `bottom_pane.prompt_args` | `tui.src.bottompane.PromptArgs` | 0 | 1 | 0 | 1 | `tui/src/bottom_pane/prompt_args.rs` | `tui/src/bottompane/PromptArgs.kt` |
| 1368 | `chatwidget.plan_implementation` | `tui.src.chatwidget.PlanImplementation` | 0 | 1 | 0 | 1 | `tui/src/chatwidget/plan_implementation.rs` | `tui/src/chatwidget/PlanImplementation.kt` |
| 1369 | `chatwidget.tests` | `tui.src.chatwidget.tests.Tests` | 0 | 1 | 0 | 1 | `tui/src/chatwidget/tests.rs` | `tui/src/chatwidget/tests/Tests.kt` |
| 1370 | `cli.build` | `cli.Build` | 0 | 1 | 0 | 1 | `cli/build.rs` | `cli/Build.kt` |
| 1371 | `common.analytics_server` | `appserver.tests.common.AnalyticsServer` | 0 | 1 | 0 | 1 | `app-server/tests/common/analytics_server.rs` | `appserver/tests/common/AnalyticsServer.kt` |
| 1372 | `config.mcp_edit_tests` | `config.src.McpEditTests` | 0 | 1 | 0 | 1 | `config/src/mcp_edit_tests.rs` | `config/src/McpEditTests.kt` |
| 1373 | `config.state_tests` | `config.src.StateTests` | 0 | 1 | 0 | 1 | `config/src/state_tests.rs` | `config/src/StateTests.kt` |
| 1374 | `core-plugins.startup_remote_sync_tests` | `coreplugins.src.StartupRemoteSyncTests` | 0 | 1 | 0 | 1 | `core-plugins/src/startup_remote_sync_tests.rs` | `coreplugins/src/StartupRemoteSyncTests.kt` |
| 1375 | `core-skills.mention_counts` | `coreskills.src.MentionCounts` | 0 | 1 | 0 | 1 | `core-skills/src/mention_counts.rs` | `coreskills/src/MentionCounts.kt` |
| 1376 | `core.apply_patch_tests` | `core.src.ApplyPatchTests` | 0 | 1 | 0 | 1 | `core/src/apply_patch_tests.rs` | `core/src/ApplyPatchTests.kt` |
| 1377 | `core.command_canonicalization` | `core.src.CommandCanonicalization` | 0 | 1 | 0 | 1 | `core/src/command_canonicalization.rs` | `core/src/CommandCanonicalization.kt` |
| 1378 | `core.shell_detect` | `core.src.ShellDetect` | 0 | 1 | 0 | 1 | `core/src/shell_detect.rs` | `core/src/ShellDetect.kt` |
| 1379 | `core.state_db_bridge` | `core.src.StateDbBridge` | 0 | 1 | 0 | 1 | `core/src/state_db_bridge.rs` | `core/src/StateDbBridge.kt` |
| 1380 | `core.windows_sandbox_read_grants` | `core.src.WindowsSandboxReadGrants` | 0 | 1 | 0 | 1 | `core/src/windows_sandbox_read_grants.rs` | `core/src/WindowsSandboxReadGrants.kt` |
| 1381 | `examples.generate-proto` | `config.examples.Generate-proto` | 0 | 1 | 0 | 1 | `config/examples/generate-proto.rs` | `config/examples/Generate-proto.kt` |
| 1382 | `exec-server.server` | `execserver.src.server.Server` | 0 | 1 | 0 | 1 | `exec-server/src/server.rs` | `execserver/src/server/Server.kt` |
| 1383 | `exec.event_processor_with_jsonl_output_tests` | `exec.src.EventProcessorWithJsonlOutputTests` | 0 | 1 | 0 | 1 | `exec/src/event_processor_with_jsonl_output_tests.rs` | `exec/src/EventProcessorWithJsonlOutputTests.kt` |
| 1384 | `exec.main_tests` | `exec.src.MainTests` | 0 | 1 | 0 | 1 | `exec/src/main_tests.rs` | `exec/src/MainTests.kt` |
| 1385 | `execpolicy-legacy.build` | `execpolicylegacy.Build` | 0 | 1 | 0 | 1 | `execpolicy-legacy/build.rs` | `execpolicylegacy/Build.kt` |
| 1386 | `execpolicy-legacy.sed_command` | `execpolicylegacy.src.SedCommand` | 0 | 1 | 0 | 1 | `execpolicy-legacy/src/sed_command.rs` | `execpolicylegacy/src/SedCommand.kt` |
| 1387 | `extensions.ad_hoc` | `memories.write.src.extensions.AdHoc` | 0 | 1 | 0 | 1 | `memories/write/src/extensions/ad_hoc.rs` | `memories/write/src/extensions/AdHoc.kt` |
| 1388 | `extensions.ad_hoc_tests` | `memories.write.src.extensions.AdHocTests` | 0 | 1 | 0 | 1 | `memories/write/src/extensions/ad_hoc_tests.rs` | `memories/write/src/extensions/AdHocTests.kt` |
| 1389 | `file-search.cli` | `filesearch.src.Cli` | 0 | 0 | 1 | 1 | `file-search/src/cli.rs` | `filesearch/src/Cli.kt` |
| 1390 | `git-utils.errors` | `gitutils.src.Errors` | 0 | 0 | 1 | 1 | `git-utils/src/errors.rs` | `gitutils/src/Errors.kt` |
| 1391 | `handlers.request_user_input_tests` | `core.src.tools.handlers.RequestUserInputTests` | 0 | 1 | 0 | 1 | `core/src/tools/handlers/request_user_input_tests.rs` | `core/src/tools/handlers/RequestUserInputTests.kt` |
| 1392 | `linux-sandbox.main` | `linuxsandbox.src.Main` | 0 | 1 | 0 | 1 | `linux-sandbox/src/main.rs` | `linuxsandbox/src/Main.kt` |
| 1393 | `mcp-server.main` | `mcpserver.src.Main` | 0 | 1 | 0 | 1 | `mcp-server/src/main.rs` | `mcpserver/src/Main.kt` |
| 1394 | `model.graph` | `state.src.model.Graph` | 0 | 0 | 1 | 1 | `state/src/model/graph.rs` | `state/src/model/Graph.kt` |
| 1395 | `models-manager.config` | `modelsmanager.src.Config` | 0 | 0 | 1 | 1 | `models-manager/src/config.rs` | `modelsmanager/src/Config.kt` |
| 1396 | `plugins.discoverable` | `core.src.plugins.Discoverable` | 0 | 1 | 0 | 1 | `core/src/plugins/discoverable.rs` | `core/src/plugins/Discoverable.kt` |
| 1397 | `plugins.injection` | `core.src.plugins.Injection` | 0 | 1 | 0 | 1 | `core/src/plugins/injection.rs` | `core/src/plugins/Injection.kt` |
| 1398 | `protocol.mappers` | `appserverprotocol.src.protocol.Mappers` | 0 | 1 | 0 | 1 | `app-server-protocol/src/protocol/mappers.rs` | `appserverprotocol/src/protocol/Mappers.kt` |
| 1399 | `protocol.message_history` | `protocol.src.MessageHistory` | 0 | 0 | 1 | 1 | `protocol/src/message_history.rs` | `protocol/src/MessageHistory.kt` |
| 1400 | `read.prompts_tests` | `memories.read.src.PromptsTests` | 0 | 1 | 0 | 1 | `memories/read/src/prompts_tests.rs` | `memories/read/src/PromptsTests.kt` |
| 1401 | `request_processors.command_exec_processor_tests` | `appserver.src.requestprocessors.CommandExecProcessorTests` | 0 | 1 | 0 | 1 | `app-server/src/request_processors/command_exec_processor_tests.rs` | `appserver/src/requestprocessors/CommandExecProcessorTests.kt` |
| 1402 | `request_processors.request_errors` | `appserver.src.requestprocessors.RequestErrors` | 0 | 1 | 0 | 1 | `app-server/src/request_processors/request_errors.rs` | `appserver/src/requestprocessors/RequestErrors.kt` |
| 1403 | `request_processors.thread_summary_tests` | `appserver.src.requestprocessors.ThreadSummaryTests` | 0 | 1 | 0 | 1 | `app-server/src/request_processors/thread_summary_tests.rs` | `appserver/src/requestprocessors/ThreadSummaryTests.kt` |
| 1404 | `sandbox-summary.config_summary` | `utils.sandboxsummary.src.ConfigSummary` | 0 | 1 | 0 | 1 | `utils/sandbox-summary/src/config_summary.rs` | `utils/sandboxsummary/src/ConfigSummary.kt` |
| 1405 | `server.registry` | `execserver.src.server.Registry` | 0 | 1 | 0 | 1 | `exec-server/src/server/registry.rs` | `execserver/src/server/Registry.kt` |
| 1406 | `session.multi_agents` | `core.src.session.MultiAgents` | 0 | 1 | 0 | 1 | `core/src/session/multi_agents.rs` | `core/src/session/MultiAgents.kt` |
| 1407 | `session.review` | `core.src.session.Review` | 0 | 1 | 0 | 1 | `core/src/session/review.rs` | `core/src/session/Review.kt` |
| 1408 | `state.paths` | `state.src.Paths` | 0 | 1 | 0 | 1 | `state/src/paths.rs` | `state/src/Paths.kt` |
| 1409 | `state.service` | `core.src.state.Service` | 0 | 0 | 1 | 1 | `core/src/state/service.rs` | `core/src/state/Service.kt` |
| 1410 | `status.account` | `tui.src.status.Account` | 0 | 0 | 1 | 1 | `tui/src/status/account.rs` | `tui/src/status/Account.kt` |
| 1411 | `stdio-to-uds.main` | `stdiotouds.src.Main` | 0 | 1 | 0 | 1 | `stdio-to-uds/src/main.rs` | `stdiotouds/src/Main.kt` |
| 1412 | `suite.auth_env` | `exec.tests.suite.AuthEnv` | 0 | 1 | 0 | 1 | `exec/tests/suite/auth_env.rs` | `exec/tests/suite/AuthEnv.kt` |
| 1413 | `suite.bad` | `execpolicylegacy.tests.suite.Bad` | 0 | 1 | 0 | 1 | `execpolicy-legacy/tests/suite/bad.rs` | `execpolicylegacy/tests/suite/Bad.kt` |
| 1414 | `suite.good` | `execpolicylegacy.tests.suite.Good` | 0 | 1 | 0 | 1 | `execpolicy-legacy/tests/suite/good.rs` | `execpolicylegacy/tests/suite/Good.kt` |
| 1415 | `suite.literal` | `execpolicylegacy.tests.suite.Literal` | 0 | 1 | 0 | 1 | `execpolicy-legacy/tests/suite/literal.rs` | `execpolicylegacy/tests/suite/Literal.kt` |
| 1416 | `suite.mcp_required_exit` | `exec.tests.suite.McpRequiredExit` | 0 | 1 | 0 | 1 | `exec/tests/suite/mcp_required_exit.rs` | `exec/tests/suite/McpRequiredExit.kt` |
| 1417 | `suite.models_etag_responses` | `core.tests.suite.ModelsEtagResponses` | 0 | 1 | 0 | 1 | `core/tests/suite/models_etag_responses.rs` | `core/tests/suite/ModelsEtagResponses.kt` |
| 1418 | `suite.output_schema` | `exec.tests.suite.OutputSchema` | 0 | 1 | 0 | 1 | `exec/tests/suite/output_schema.rs` | `exec/tests/suite/OutputSchema.kt` |
| 1419 | `suite.prompt_debug_tests` | `core.tests.suite.PromptDebugTests` | 0 | 1 | 0 | 1 | `core/tests/suite/prompt_debug_tests.rs` | `core/tests/suite/PromptDebugTests.kt` |
| 1420 | `suite.quota_exceeded` | `core.tests.suite.QuotaExceeded` | 0 | 1 | 0 | 1 | `core/tests/suite/quota_exceeded.rs` | `core/tests/suite/QuotaExceeded.kt` |
| 1421 | `suite.runtime_summary` | `otel.tests.suite.RuntimeSummary` | 0 | 1 | 0 | 1 | `otel/tests/suite/runtime_summary.rs` | `otel/tests/suite/RuntimeSummary.kt` |
| 1422 | `suite.server_error_exit` | `exec.tests.suite.ServerErrorExit` | 0 | 1 | 0 | 1 | `exec/tests/suite/server_error_exit.rs` | `exec/tests/suite/ServerErrorExit.kt` |
| 1423 | `suite.status_indicator` | `tui.tests.suite.StatusIndicator` | 0 | 1 | 0 | 1 | `tui/tests/suite/status_indicator.rs` | `tui/tests/suite/StatusIndicator.kt` |
| 1424 | `suite.stream_error_allows_next_turn` | `core.tests.suite.StreamErrorAllowsNextTurn` | 0 | 1 | 0 | 1 | `core/tests/suite/stream_error_allows_next_turn.rs` | `core/tests/suite/StreamErrorAllowsNextTurn.kt` |
| 1425 | `suite.vt100_live_commit` | `tui.tests.suite.Vt100LiveCommit` | 0 | 1 | 0 | 1 | `tui/tests/suite/vt100_live_commit.rs` | `tui/tests/suite/Vt100LiveCommit.kt` |
| 1426 | `tests.initialize` | `execserver.tests.Initialize` | 0 | 1 | 0 | 1 | `exec-server/tests/initialize.rs` | `execserver/tests/Initialize.kt` |
| 1427 | `tests.streamable_http_remote` | `rmcpclient.tests.StreamableHttpRemote` | 0 | 1 | 0 | 1 | `rmcp-client/tests/streamable_http_remote.rs` | `rmcpclient/tests/StreamableHttpRemote.kt` |
| 1428 | `thread-store.examples.generate-proto` | `threadstore.examples.Generate-proto` | 0 | 1 | 0 | 1 | `thread-store/examples/generate-proto.rs` | `threadstore/examples/Generate-proto.kt` |
| 1429 | `thread-store.store` | `threadstore.src.Store` | 0 | 0 | 1 | 1 | `thread-store/src/store.rs` | `threadstore/src/Store.kt` |
| 1430 | `tools.dynamic_tool` | `tools.src.DynamicTool` | 0 | 1 | 0 | 1 | `tools/src/dynamic_tool.rs` | `tools/src/DynamicTool.kt` |
| 1431 | `tui.diff_model` | `tui.src.DiffModel` | 0 | 0 | 1 | 1 | `tui/src/diff_model.rs` | `tui/src/DiffModel.kt` |
| 1432 | `v2.collaboration_mode_list` | `appserver.tests.suite.v2.CollaborationModeList` | 0 | 1 | 0 | 1 | `app-server/tests/suite/v2/collaboration_mode_list.rs` | `appserver/tests/suite/v2/CollaborationModeList.kt` |
| 1433 | `v2.marketplace_add` | `appserver.tests.suite.v2.MarketplaceAdd` | 0 | 1 | 0 | 1 | `app-server/tests/suite/v2/marketplace_add.rs` | `appserver/tests/suite/v2/MarketplaceAdd.kt` |
| 1434 | `windows-sandbox-rs.build` | `windowssandboxrs.Build` | 0 | 1 | 0 | 1 | `windows-sandbox-rs/build.rs` | `windowssandboxrs/Build.kt` |
| 1435 | `write.start` | `memories.write.src.Start` | 0 | 1 | 0 | 1 | `memories/write/src/start.rs` | `memories/write/src/Start.kt` |
| 1436 | `utils.path_utils` | `core.src.utils.PathUtils` | 5 | 0 | 0 | 0 | `core/src/utils/path_utils.rs` | `core/src/utils/PathUtils.kt` |
| 1437 | `onboarding.keys` | `tui.src.onboarding.Keys` | 4 | 0 | 0 | 0 | `tui/src/onboarding/keys.rs` | `tui/src/onboarding/Keys.kt` |
| 1438 | `tui.version` | `tui.src.Version` | 2 | 0 | 0 | 0 | `tui/src/version.rs` | `tui/src/Version.kt` |
| 1439 | `core.mention_syntax` | `core.src.MentionSyntax` | 1 | 0 | 0 | 0 | `core/src/mention_syntax.rs` | `core/src/MentionSyntax.kt` |
| 1440 | `app-server.tests.all` | `appserver.tests.All` | 0 | 0 | 0 | 0 | `app-server/tests/all.rs` | `appserver/tests/All.kt` |
| 1441 | `apply-patch.tests.all` | `applypatch.tests.All` | 0 | 0 | 0 | 0 | `apply-patch/tests/all.rs` | `applypatch/tests/All.kt` |
| 1442 | `auth.error` | `login.src.auth.Error` | 0 | 0 | 0 | 0 | `login/src/auth/error.rs` | `login/src/auth/Error.kt` |
| 1443 | `chatgpt.tests.all` | `chatgpt.tests.All` | 0 | 0 | 0 | 0 | `chatgpt/tests/all.rs` | `chatgpt/tests/All.kt` |
| 1444 | `client.http_client` | `execserver.src.client.HttpClient` | 0 | 0 | 0 | 0 | `exec-server/src/client/http_client.rs` | `execserver/src/client/HttpClient.kt` |
| 1445 | `core.config.schema` | `core.src.config.Schema` | 0 | 0 | 0 | 0 | `core/src/config/schema.rs` | `core/src/config/Schema.kt` |
| 1446 | `core.original_image_detail` | `core.src.OriginalImageDetail` | 0 | 0 | 0 | 0 | `core/src/original_image_detail.rs` | `core/src/OriginalImageDetail.kt` |
| 1447 | `core.tests.all` | `core.tests.All` | 0 | 0 | 0 | 0 | `core/tests/all.rs` | `core/tests/All.kt` |
| 1448 | `exec.tests.all` | `exec.tests.All` | 0 | 0 | 0 | 0 | `exec/tests/all.rs` | `exec/tests/All.kt` |
| 1449 | `execpolicy-legacy.tests.all` | `execpolicylegacy.tests.All` | 0 | 0 | 0 | 0 | `execpolicy-legacy/tests/all.rs` | `execpolicylegacy/tests/All.kt` |
| 1450 | `handlers.multi_agents_v2` | `core.src.tools.handlers.multiagentsv2.MultiAgentsV2` | 0 | 0 | 0 | 0 | `core/src/tools/handlers/multi_agents_v2.rs` | `core/src/tools/handlers/multiagentsv2/MultiAgentsV2.kt` |
| 1451 | `linux-sandbox.tests.all` | `linuxsandbox.tests.All` | 0 | 0 | 0 | 0 | `linux-sandbox/tests/all.rs` | `linuxsandbox/tests/All.kt` |
| 1452 | `login.tests.all` | `login.tests.All` | 0 | 0 | 0 | 0 | `login/tests/all.rs` | `login/tests/All.kt` |
| 1453 | `mcp-server.tests.all` | `mcpserver.tests.All` | 0 | 0 | 0 | 0 | `mcp-server/tests/all.rs` | `mcpserver/tests/All.kt` |
| 1454 | `metrics.names` | `otel.src.metrics.Names` | 0 | 0 | 0 | 0 | `otel/src/metrics/names.rs` | `otel/src/metrics/Names.kt` |
| 1455 | `network-proxy.reasons` | `networkproxy.src.Reasons` | 0 | 0 | 0 | 0 | `network-proxy/src/reasons.rs` | `networkproxy/src/Reasons.kt` |
| 1456 | `plugins.mention_syntax` | `utils.plugins.src.MentionSyntax` | 0 | 0 | 0 | 0 | `utils/plugins/src/mention_syntax.rs` | `utils/plugins/src/MentionSyntax.kt` |
| 1457 | `read.metrics` | `memories.read.src.Metrics` | 0 | 0 | 0 | 0 | `memories/read/src/metrics.rs` | `memories/read/src/Metrics.kt` |
| 1458 | `tests.all` | `tui.tests.All` | 0 | 0 | 0 | 0 | `tui/tests/all.rs` | `tui/tests/All.kt` |
| 1459 | `tests.test_backend` | `tui.tests.TestBackend` | 0 | 0 | 0 | 0 | `tui/tests/test_backend.rs` | `tui/tests/TestBackend.kt` |
| 1460 | `tests.tests` | `otel.tests.Tests` | 0 | 0 | 0 | 0 | `otel/tests/tests.rs` | `otel/tests/Tests.kt` |
| 1461 | `tui.frames` | `tui.src.Frames` | 0 | 0 | 0 | 0 | `tui/src/frames.rs` | `tui/src/Frames.kt` |
| 1462 | `write.metrics` | `memories.write.src.Metrics` | 0 | 0 | 0 | 0 | `memories/write/src/metrics.rs` | `memories/write/src/Metrics.kt` |

## Documentation Gaps

There is missing documentation that is hurting overall scoring.

**Documentation coverage:** 1755 / 2854 lines (61%)

Documentation gaps (>20%), complete list:

- `tui.app_event` - 86% gap (436 → 60 lines)
- `terminal-detection.lib` - 87% gap (156 → 21 lines)
- `protocol.config_types` - 84% gap (158 → 25 lines)
- `core.message_history` - 96% gap (114 → 5 lines)
- `core.exec` - 99% gap (88 → 1 lines)
- `protocol.approvals` - 94% gap (84 → 5 lines)
- `mcp-server.codex_tool_config` - 100% gap (78 → 0 lines)
- `tools.sandboxing` - 100% gap (78 → 0 lines)
- `exec-server.protocol` - 80% gap (82 → 16 lines)
- `tui.frame_requester` - 78% gap (74 → 16 lines)
- `core.exec_policy` - 79% gap (72 → 15 lines)
- `model-provider.provider` - 95% gap (58 → 3 lines)
- `shell-command.powershell` - 100% gap (50 → 0 lines)
- `protocol.user_input` - 89% gap (56 → 6 lines)
- `tui.key_hint` - 100% gap (48 → 0 lines)
- `protocol.exec_output` - 100% gap (42 → 0 lines)
- `runtimes.shell` - 100% gap (38 → 0 lines)
- `tool.terminal` - 82% gap (40 → 7 lines)
- `core.util` - 100% gap (32 → 0 lines)
- `core.compact` - 66% gap (38 → 13 lines)
- `tools.context` - 100% gap (24 → 0 lines)
- `core.review_format` - 100% gap (22 → 0 lines)
- `thread-store.error` - 100% gap (20 → 0 lines)
- `tui.frame_rate_limiter` - 82% gap (22 → 4 lines)
- `ollama.client` - 71% gap (24 → 7 lines)
- `execpolicy.policy` - 94% gap (18 → 1 lines)
- `core.spawn` - 47% gap (36 → 19 lines)
- `unified_exec.session` - 100% gap (16 → 0 lines)
- `codex-api.common` - 44% gap (34 → 19 lines)
- `shell-command.bash` - 54% gap (28 → 13 lines)
- `cli.sandbox_mode_cli_arg` - 100% gap (14 → 0 lines)
- `backend-client.types` - 100% gap (14 → 0 lines)
- `runtimes.unified_exec` - 100% gap (14 → 0 lines)
- `protocol.items` - 72% gap (18 → 5 lines)
- `codex-api.rate_limits` - 86% gap (14 → 2 lines)
- `core.landlock` - 79% gap (14 → 3 lines)
- `execpolicy.rule` - 61% gap (18 → 7 lines)
- `suite.exec` - 100% gap (10 → 0 lines)
- `runtimes.apply_patch` - 100% gap (10 → 0 lines)
- `bottom_pane.scroll_state` - 42% gap (24 → 14 lines)
- `command_safety.is_dangerous_command` - 62% gap (16 → 6 lines)
- `core.client_common` - 41% gap (22 → 13 lines)
- `state.session` - 57% gap (14 → 6 lines)
- `ollama.pull` - 35% gap (20 → 13 lines)
- `sandboxing.seatbelt` - 39% gap (18 → 11 lines)
- `cli.approval_mode_cli_arg` - 32% gap (22 → 15 lines)
- `tui.slash_command` - 38% gap (16 → 10 lines)
- `tui.app_event_sender` - 42% gap (12 → 7 lines)
- `tui.ui_consts` - 25% gap (16 → 12 lines)
- `protocol.num_format` - 25% gap (16 → 12 lines)
- `tui.terminal_palette` - 29% gap (14 → 10 lines)
- `execpolicy.decision` - 50% gap (6 → 3 lines)

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

