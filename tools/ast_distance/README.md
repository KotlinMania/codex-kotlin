# ASTDistance

Cross-language AST similarity measurement tool for verifying source code ports.

Inspired by the [ASTERIA paper](https://arxiv.org/abs/2108.06082) which uses Tree-LSTM for binary code similarity detection, this tool measures similarity between Rust, Kotlin, and C++ source files to help verify porting accuracy.

## Features

- **Tree-sitter parsing** for Rust, Kotlin, and C++ ASTs
- **Normalized node types** that map across languages
- **Dependency graph analysis** — find which files are most depended upon
- **Codebase comparison** — match files across codebases with fuzzy name matching
- **Multiple similarity metrics**:
  - Cosine similarity of node type histograms
  - Structure similarity (size, depth)
  - Jaccard similarity of node sets
  - Normalized tree edit distance
- **Function-level comparison** with similarity matrix
- **Porting priority ranking** — high dependents + low similarity = needs attention

## Building

```bash
mkdir build && cd build
cmake ..
cmake --build . -j8
```

## Usage

### Deep Analysis (recommended for porting projects)
```bash
./ast_distance --deep <src_dir> <src_lang> <tgt_dir> <tgt_lang>

# Example: Kotlin → C++
./ast_distance --deep /path/to/kotlin kotlin /path/to/cpp cpp

# Example: Rust → Kotlin
./ast_distance --deep /path/to/rust rust /path/to/kotlin kotlin
```

### Check Missing Files
```bash
./ast_distance --missing <src_dir> <src_lang> <tgt_dir> <tgt_lang>
```

### Dependency Graph
```bash
./ast_distance --deps <directory> <rust|kotlin|cpp>
./ast_distance --scan <directory> <rust|kotlin|cpp>
```

### Compare Two Files
```bash
./ast_distance <file1> <file2>
```

### Compare Functions
```bash
./ast_distance --compare-functions <file1> <file2>
```

### Dump AST Structure
```bash
./ast_distance --dump <file> <rust|kotlin|cpp>
```

## Example Output

### Deep Analysis
```
=== Deep Analysis: /src/kotlin (kotlin) -> /src/cpp (cpp) ===

Scanning source codebase (kotlin)...
  Files: 111
  Total imports: 364
  Most depended: flow.Channels (14 dependents)

Scanning target codebase (cpp)...
  Files: 260
  Total imports: 1049

Matched:   111 files
Unmatched: 0 source, 149 target

=== Matched Files (by porting priority) ===
Source                   Target                   Sim    Deps  Priority
flow.Flow                flow.Flow                0.48   13    6.8
flow.Channels            flow.Channels            0.74   14    3.7
...

=== Porting Recommendations ===
Incomplete ports (similarity < 60%): 52
Top priority to complete:
  flow.Flow                  sim=0.48 deps=13
```

### File Comparison
```
=== AST Similarity Report ===
Tree 1: size=3377, depth=23
Tree 2: size=1102, depth=25

Similarity Metrics:
  Cosine (histogram):    0.9901
  Structure:             0.6232
  Jaccard:               0.3228
  Edit Distance (norm):  0.2615
  Combined Score:        0.5647
```

## Similarity Thresholds

- `> 0.85` — Excellent port, likely complete
- `0.60–0.85` — Good port, may need refinement
- `0.40–0.60` — Partial port, significant gaps
- `< 0.40` — Stub or very different implementation

## Architecture

```
include/
  tree.hpp          - Tree data structure
  tensor.hpp        - Lightweight tensor ops
  tree_lstm.hpp     - Binary Tree-LSTM encoder
  node_types.hpp    - Normalized AST node types (Rust/Kotlin/C++)
  ast_parser.hpp    - Tree-sitter based parser
  similarity.hpp    - Similarity metrics
  imports.hpp       - Import/include extraction
  codebase.hpp      - Directory scanning, dependency graphs

src/
  main.cpp          - CLI tool
  ast_parser.cpp
  ast_normalizer.cpp
  similarity.cpp
```

## Extending

### Add a New Language

1. Add tree-sitter grammar to `CMakeLists.txt` (FetchContent)
2. Add `extern "C" { const TSLanguage* tree_sitter_<lang>(); }` to `ast_parser.hpp` and `imports.hpp`
3. Add `<LANG>` to `enum class Language` in `ast_parser.hpp`
4. Add `<lang>_node_to_type()` mapping in `node_types.hpp`
5. Add import/package extraction in `imports.hpp`
6. Update file extension checks in `codebase.hpp`

## References

- [ASTERIA: Deep Learning-based AST-Encoding for Cross-platform Binary Code Similarity Detection](https://arxiv.org/abs/2108.06082)
- [Stanford TreeLSTM](https://github.com/stanfordnlp/treelstm)
- [tree-sitter](https://tree-sitter.github.io/tree-sitter/)

## License

MIT
