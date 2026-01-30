# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Current Progress:** 36.7% (163/444 files)
- **Matched Files:** 150
- **Average Similarity:** 0.76
- **Critical Issues:** 20 files with <0.60 similarity

## Priority 1: Fix Incomplete High-Dependency Files

### 1. core.terminal
- **Similarity:** 0.55 (needs 30% improvement)
- **Dependencies:** 13
- **Priority Score:** 5.8
- **Action:** Deep review - likely missing major functionality

### 2. ollama.parser
- **Similarity:** 0.76 (needs 9% improvement)
- **Dependencies:** 23
- **Priority Score:** 5.5
- **Action:** Minor refinements needed

### 3. protocol.user_input
- **Similarity:** 0.75 (needs 10% improvement)
- **Dependencies:** 20
- **Priority Score:** 5.0
- **Action:** Minor refinements needed

### 4. tui.style
- **Similarity:** 0.75 (needs 10% improvement)
- **Dependencies:** 16
- **Priority Score:** 4.1
- **TODOs:** 1
- **Action:** Review and complete missing sections

### 5. tui.history_cell
- **Similarity:** 0.71 (needs 14% improvement)
- **Dependencies:** 11
- **Priority Score:** 3.2
- **TODOs:** 19
- **Action:** Review and complete missing sections

### 6. tui.app_event
- **Similarity:** 0.77 (needs 8% improvement)
- **Dependencies:** 14
- **Priority Score:** 3.2
- **Action:** Minor refinements needed

### 7. state.session
- **Similarity:** 0.83 (needs 2% improvement)
- **Dependencies:** 18
- **Priority Score:** 3.1
- **Action:** Minor refinements needed

### 8. tui.color
- **Similarity:** 0.82 (needs 3% improvement)
- **Dependencies:** 15
- **Priority Score:** 2.7
- **Action:** Minor refinements needed

### 9. tui.app_event_sender
- **Similarity:** 0.83 (needs 2% improvement)
- **Dependencies:** 12
- **Priority Score:** 2.1
- **TODOs:** 2
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
## Starlark Dependency

The exec-policy module requires a Kotlin port of Starlark for policy_parser.rs.
This is being ported separately at: https://github.com/KotlinMania/starlark-kotlin

Files blocked on starlark-kotlin completion:
- src/commonMain/kotlin/ai/solace/coder/execpolicy/PolicyParser.kt

Once starlark-kotlin reaches sufficient maturity, PolicyParser can be completed.
