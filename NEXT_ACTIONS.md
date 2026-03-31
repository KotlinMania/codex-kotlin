# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Current Progress:** 33.3% (148/444 files)
- **Matched Files:** 143
- **Average Similarity:** 0.75
- **Critical Issues:** 15 files with <0.60 similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. ollama.parser
- **Similarity:** 0.73 (needs 12% improvement)
- **Dependencies:** 23
- **Priority Score:** 6.1
- **Action:** Review and complete missing sections

### 2. protocol.user_input
- **Similarity:** 0.71 (needs 14% improvement)
- **Dependencies:** 20
- **Priority Score:** 5.9
- **Action:** Review and complete missing sections

### 3. protocol.conversation_id
- **Similarity:** 0.79 (needs 6% improvement)
- **Dependencies:** 25
- **Priority Score:** 5.3
- **Action:** Minor refinements needed

### 4. core.terminal
- **Similarity:** 0.62 (needs 23% improvement)
- **Dependencies:** 13
- **Priority Score:** 4.9
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

### 7. tui.style
- **Similarity:** 0.79 (needs 6% improvement)
- **Dependencies:** 16
- **Priority Score:** 3.3
- **Action:** Minor refinements needed

### 8. tui.app_event
- **Similarity:** 0.80 (needs 5% improvement)
- **Dependencies:** 14
- **Priority Score:** 2.9
- **Action:** Minor refinements needed

### 9. tui.color
- **Similarity:** 0.82 (needs 3% improvement)
- **Dependencies:** 15
- **Priority Score:** 2.7
- **Action:** Minor refinements needed

### 10. tui.app_event_sender
- **Similarity:** 0.78 (needs 7% improvement)
- **Dependencies:** 12
- **Priority Score:** 2.6
- **TODOs:** 1
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
./ast_distance --init-tasks ../../codex-rs rust ../../src kotlin tasks.json ../../AGENTS.md

# Get next high-priority task
./ast_distance --assign tasks.json <agent-id>
```
