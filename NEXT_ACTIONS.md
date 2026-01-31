# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Current Progress:** 25.0% (111/444 files)
- **Matched Files:** 108
- **Average Similarity:** 0.74
- **Critical Issues:** 13 files with <0.60 similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. otel.config
- **Similarity:** 0.85 (needs 0% improvement)
- **Dependencies:** 45
- **Priority Score:** 6.9
- **Action:** Minor refinements needed

### 2. ollama.parser
- **Similarity:** 0.73 (needs 12% improvement)
- **Dependencies:** 23
- **Priority Score:** 6.1
- **Action:** Review and complete missing sections

### 3. tui.history_cell
- **Similarity:** 0.47 (needs 38% improvement)
- **Dependencies:** 11
- **Priority Score:** 5.8
- **Action:** Deep review - likely missing major functionality

### 4. protocol.user_input
- **Similarity:** 0.75 (needs 10% improvement)
- **Dependencies:** 20
- **Priority Score:** 5.1
- **Action:** Review and complete missing sections

### 5. protocol.conversation_id
- **Similarity:** 0.81 (needs 4% improvement)
- **Dependencies:** 25
- **Priority Score:** 4.8
- **Action:** Minor refinements needed

### 6. state.session
- **Similarity:** 0.78 (needs 7% improvement)
- **Dependencies:** 18
- **Priority Score:** 3.9
- **Action:** Minor refinements needed

### 7. tools.context
- **Similarity:** 0.80 (needs 5% improvement)
- **Dependencies:** 19
- **Priority Score:** 3.8
- **Action:** Minor refinements needed

### 8. tui.key_hint
- **Similarity:** 0.68 (needs 17% improvement)
- **Dependencies:** 11
- **Priority Score:** 3.6
- **Action:** Review and complete missing sections

### 9. tui.style
- **Similarity:** 0.79 (needs 6% improvement)
- **Dependencies:** 16
- **Priority Score:** 3.4
- **Action:** Minor refinements needed

### 10. tui.app_event
- **Similarity:** 0.80 (needs 5% improvement)
- **Dependencies:** 14
- **Priority Score:** 2.9
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
