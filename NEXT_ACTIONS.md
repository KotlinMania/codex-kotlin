# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Current Progress:** 34.5% (153/444 files)
- **Matched Files:** 141
- **Average Similarity:** 0.78
- **Critical Issues:** 14 files with <0.60 similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. ollama.parser
- **Similarity:** 0.76 (needs 9% improvement)
- **Dependencies:** 23
- **Priority Score:** 5.5
- **Action:** Minor refinements needed

### 2. protocol.user_input
- **Similarity:** 0.75 (needs 10% improvement)
- **Dependencies:** 20
- **Priority Score:** 5.0
- **Action:** Minor refinements needed

### 3. tui.style
- **Similarity:** 0.75 (needs 10% improvement)
- **Dependencies:** 16
- **Priority Score:** 4.1
- **TODOs:** 1
- **Action:** Review and complete missing sections

### 4. tui.app_event
- **Similarity:** 0.77 (needs 8% improvement)
- **Dependencies:** 14
- **Priority Score:** 3.2
- **Action:** Minor refinements needed

### 5. state.session
- **Similarity:** 0.83 (needs 2% improvement)
- **Dependencies:** 18
- **Priority Score:** 3.1
- **Action:** Minor refinements needed

### 6. tui.color
- **Similarity:** 0.82 (needs 3% improvement)
- **Dependencies:** 15
- **Priority Score:** 2.7
- **Action:** Minor refinements needed

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **core.terminal** (13 deps)
   - Path: `core/src/terminal.rs`
   - Essential for 13 other files

2. **tui.app_event_sender** (12 deps)
   - Path: `tui/src/app_event_sender.rs`
   - Essential for 12 other files

3. **tui.history_cell** (11 deps)
   - Path: `tui/src/history_cell.rs`
   - Essential for 11 other files

4. **tui.key_hint** (11 deps)
   - Path: `tui/src/key_hint.rs`
   - Essential for 11 other files

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Next Commands

```bash
# Initialize task queue for systematic porting
cd tools/ast_distance
./ast_distance --init-tasks ../../codex-rs rust ../../src kotlin tasks.json ../../AGENTS.md

# Get next high-priority task
./ast_distance --assign tasks.json <agent-id>
```
