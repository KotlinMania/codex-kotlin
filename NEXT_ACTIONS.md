# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Current Progress:** 22.2% (136/613 files)
- **Matched Files:** 134
- **Average Similarity:** 0.75
- **Critical Issues:** 17 files with <0.60 similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. core.terminal
- **Similarity:** 0.59 (needs 26% improvement)
- **Dependencies:** 24
- **Priority Score:** 9.9
- **Action:** Deep review - likely missing major functionality

### 2. tui.style
- **Similarity:** 0.79 (needs 6% improvement)
- **Dependencies:** 37
- **Priority Score:** 7.7
- **Action:** Minor refinements needed

### 3. ollama.parser
- **Similarity:** 0.73 (needs 12% improvement)
- **Dependencies:** 26
- **Priority Score:** 6.9
- **Action:** Review and complete missing sections

### 4. tui.color
- **Similarity:** 0.82 (needs 3% improvement)
- **Dependencies:** 36
- **Priority Score:** 6.5
- **Action:** Minor refinements needed

### 5. tui.history_cell
- **Similarity:** 0.77 (needs 8% improvement)
- **Dependencies:** 28
- **Priority Score:** 6.3
- **Action:** Minor refinements needed

### 6. protocol.conversation_id
- **Similarity:** 0.79 (needs 6% improvement)
- **Dependencies:** 27
- **Priority Score:** 5.7
- **Action:** Minor refinements needed

### 7. tools.context
- **Similarity:** 0.80 (needs 5% improvement)
- **Dependencies:** 28
- **Priority Score:** 5.5
- **Action:** Minor refinements needed

### 8. tui.app_event_sender
- **Similarity:** 0.78 (needs 7% improvement)
- **Dependencies:** 25
- **Priority Score:** 5.5
- **TODOs:** 1
- **Action:** Minor refinements needed

### 9. core.env
- **Similarity:** 0.65 (needs 20% improvement)
- **Dependencies:** 15
- **Priority Score:** 5.3
- **Action:** Review and complete missing sections

### 10. tui.app_event
- **Similarity:** 0.82 (needs 3% improvement)
- **Dependencies:** 29
- **Priority Score:** 5.1
- **Action:** Minor refinements needed

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **tui.frame_requester** (22 deps)
   - Path: `tui/src/tui/frame_requester.rs`
   - Essential for 22 other files

2. **config.constraint** (12 deps)
   - Path: `core/src/config/constraint.rs`
   - Essential for 12 other files

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
./ast_distance --init-tasks ../../tmp/codex/codex-rs rust ../../src kotlin tasks.json ../../AGENTS.md

# Get next high-priority task
./ast_distance --assign tasks.json <agent-id>
```
