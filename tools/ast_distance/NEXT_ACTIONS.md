# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Current Progress:** 26.8% (119/444 files)
- **Matched Files:** 116
- **Average Similarity:** 0.74
- **Critical Issues:** 14 files with <0.60 similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. ollama.parser
- **Similarity:** 0.73 (needs 12% improvement)
- **Dependencies:** 23
- **Priority Score:** 6.1
- **Action:** Review and complete missing sections

### 2. protocol.conversation_id
- **Similarity:** 0.79 (needs 6% improvement)
- **Dependencies:** 25
- **Priority Score:** 5.3
- **Action:** Minor refinements needed

### 3. protocol.user_input
- **Similarity:** 0.74 (needs 11% improvement)
- **Dependencies:** 20
- **Priority Score:** 5.1
- **Action:** Review and complete missing sections

### 4. tui.history_cell
- **Similarity:** 0.64 (needs 21% improvement)
- **Dependencies:** 11
- **Priority Score:** 4.0
- **TODOs:** 1
- **Action:** Review and complete missing sections

### 5. state.session
- **Similarity:** 0.79 (needs 6% improvement)
- **Dependencies:** 18
- **Priority Score:** 3.9
- **Action:** Minor refinements needed

### 6. tools.context
- **Similarity:** 0.80 (needs 5% improvement)
- **Dependencies:** 19
- **Priority Score:** 3.8
- **Action:** Minor refinements needed

### 7. tui.key_hint
- **Similarity:** 0.68 (needs 17% improvement)
- **Dependencies:** 11
- **Priority Score:** 3.6
- **Action:** Review and complete missing sections

### 8. tui.style
- **Similarity:** 0.79 (needs 6% improvement)
- **Dependencies:** 16
- **Priority Score:** 3.3
- **Action:** Minor refinements needed

### 9. tui.app_event
- **Similarity:** 0.80 (needs 5% improvement)
- **Dependencies:** 14
- **Priority Score:** 2.9
- **Action:** Minor refinements needed

### 10. tui.color
- **Similarity:** 0.82 (needs 3% improvement)
- **Dependencies:** 15
- **Priority Score:** 2.7
- **Action:** Minor refinements needed

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

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
./ast_distance --init-tasks ../../../../codex-rs rust ../../../../src kotlin tasks.json ../../AGENTS.md

# Get next high-priority task
./ast_distance --assign tasks.json <agent-id>
```
