# codex-kotlin

This repository is the Kotlin Multiplatform port. Upstream sources are read-only
translation references; project verification runs through Gradle and Kotlin
targets only.

- Do not add build, CI, script, or test hooks that invoke upstream toolchains.
- Keep Kotlin code multiplatform-pure; avoid JVM-only APIs in shared code.
- Prefer focused target checks such as `./gradlew macosArm64Test`,
  `./gradlew linuxX64Test`.
- When touching shared protocol/core code, run the relevant Kotlin target tests
  before finalizing.

## TUI style conventions

See `codex-rs/tui/styles.md`.

## TUI code conventions

- Use concise styling helpers from ratatui’s Stylize trait.
  - Basic spans: use "text".into()
  - Styled spans: use "text".red(), "text".green(), "text".magenta(), "text".dim(), etc.
  - Prefer these over constructing styles with `Span::styled` and `Style` directly.
  - Example: patch summary file lines
    - Desired: vec!["  └ ".into(), "M".red(), " ".dim(), "tui/src/app.rs".dim()]

### TUI Styling (ratatui)

- Prefer Stylize helpers: use "text".dim(), .bold(), .cyan(), .italic(), .underlined() instead of manual Style where possible.
- Prefer simple conversions: use "text".into() for spans and vec![…].into() for lines; when inference is ambiguous (e.g., Paragraph::new/Cell::from), use Line::from(spans) or Span::from(text).
- Computed styles: if the Style is computed at runtime, using `Span::styled` is OK (`Span::from(text).set_style(style)` is also acceptable).
- Avoid hardcoded white: do not use `.white()`; prefer the default foreground (no color).
- Chaining: combine helpers by chaining for readability (e.g., url.cyan().underlined()).
- Single items: prefer "text".into(); use Line::from(text) or Span::from(text) only when the target type isn’t obvious from context, or when using .into() would require extra type annotations.
- Building lines: use vec![…].into() to construct a Line when the target type is obvious and no extra type annotations are needed; otherwise use Line::from(vec![…]).
- Avoid churn: don’t refactor between equivalent forms (Span::styled ↔ set_style, Line::from ↔ .into()) without a clear readability or functional gain; follow file‑local conventions and do not introduce type annotations solely to satisfy .into().
- Compactness: prefer the form that stays on one line after rustfmt; if only one of Line::from(vec![…]) or vec![…].into() avoids wrapping, choose that. If both wrap, pick the one with fewer wrapped lines.

### Text wrapping

- Always use textwrap::wrap to wrap plain strings.
- If you have a ratatui Line and you want to wrap it, use the helpers in tui/src/wrapping.rs, e.g. word_wrap_lines / word_wrap_line.
- If you need to indent wrapped lines, use the initial_indent / subsequent_indent options from RtOptions if you can, rather than writing custom logic.
- If you have a list of lines and you need to prefix them all with some prefix (optionally different on the first vs subsequent lines), use the `prefix_lines` helper from line_utils.

## Tests

### Test assertions

- Tests should use pretty_assertions::assert_eq for clearer diffs. Import this at the top of the test module if it isn't already.

### Integration tests (core)

- Prefer the utilities in `core_test_support::responses` when writing end-to-end Codex tests.

- All `mount_sse*` helpers return a `ResponseMock`; hold onto it so you can assert against outbound `/responses` POST bodies.
- Use `ResponseMock::single_request()` when a test should only issue one POST, or `ResponseMock::requests()` to inspect every captured `ResponsesRequest`.
- `ResponsesRequest` exposes helpers (`body_json`, `input`, `function_call_output`, `custom_tool_call_output`, `call_output`, `header`, `path`, `query_param`) so assertions can target structured payloads instead of manual JSON digging.
- Build SSE payloads with the provided `ev_*` constructors and the `sse(...)`.
- Prefer `wait_for_event` over `wait_for_event_with_timeout`.
- Prefer `mount_sse_once` over `mount_sse_once_match` or `mount_sse_sequence`

- Typical pattern:

  ```rust
  let mock = responses::mount_sse_once(&server, responses::sse(vec![
      responses::ev_response_created("resp-1"),
      responses::ev_function_call(call_id, "shell", &serde_json::to_string(&args)?),
      responses::ev_completed("resp-1"),
  ])).await;

  codex.submit(Op::UserTurn { ... }).await?;

  // Assert request body if needed.
  let request = mock.single_request();
  // assert using request.function_call_output(call_id) or request.json_body() or other helpers.
  ```

## Kotlin Porting Guidelines

### Semantic Parity (The "Dishonest Code" Rule)
- **Rule:** Port the *intent* and *behavior* of the code, not just the syntax.
- **Context:** Rust's `Display` trait often implies specific formatting contracts (e.g., ANSI codes, truncation, padding) that are critical for user experience.
- **Warning:** Do **not** oversimplify `impl Display` to a simple `toString()` that returns a constant or a raw value if the original code performed formatting.
    - *Bad Example:* Replacing a `Display` impl that handles ANSI reset codes with `fun toString() = "RESET"`.
    - *Good Example:* `TruncationPolicy.kt` faithfully reproducing the `formattedTruncateText` logic to preserve output structure.
- **Action:** When porting `Display` or `Debug`, check if the Rust code does more than just return a field. If so, the Kotlin `toString()` (or a helper method) must replicate that logic.

### Research First
- **Rule:** Do not guess at the behavior of Rust functions or traits.
- **Action:** Use the browser to look up the official Rust documentation (e.g., `std::process::ChildStdin`, `core::fmt::Formatter`) if you are not 100% sure of the semantics.
- **Context:** Rust's type system and traits often carry subtle behaviors (buffering, blocking, formatting state) that are not obvious from the function signature alone.
- **Example:** `core::fmt::Formatter` manages padding, alignment, and flags. A simple string concatenation in Kotlin is often an insufficient port if the original Rust code used these features.

---

## Trait default methods with `where` clauses → method-level Kotlin generic bounds

Rust traits routinely declare a default method whose body only typechecks
when the type parameter satisfies a stricter bound:

```rust
pub trait RangeBounds<T> {
    fn start_bound(&self) -> Bound<&T>;
    fn end_bound(&self) -> Bound<&T>;

    fn is_empty(&self) -> bool
    where T: PartialOrd,
    { /* default body uses < */ }
}
```

The trait stays unconstrained; the *method* picks up the bound via its
own `where` clause. Kotlin has no per-method `where` on an interface
member. Three obvious mappings fail:

1. **Tighten the interface to `<T : Comparable<T>>`.** Breaks every
   caller that holds the unbounded interface type.
2. **Make the method abstract on the interface.** Forces every concrete
   impl to invent a body and pile on `override` boilerplate, even when
   the Rust counterpart inherits the default unchanged.
3. **Runtime cast helper** — `if (left is Comparable<*> ...) ... else throw IllegalStateException(...)`.
   Compile-time bounds become runtime crashes; the cheat detector flags
   this and zeros the file's score.

### The faithful pattern

Translate the default to a Kotlin **extension function whose own type
parameter carries the bound**:

```kotlin
interface RangeBounds<T> {
    fun startBound(): Bound<T>
    fun endBound(): Bound<T>
}

fun <T : Comparable<T>> RangeBounds<T>.isEmpty(): Boolean { /* default body */ }
```

Concrete impls that want to specialise the default supply a same-named
**member function**. Kotlin resolves `range.isEmpty()` to the member
when the static receiver type is the concrete class and to the
extension when it is the interface — exactly mirroring Rust's
"default method, per-impl override". No `override` keyword on the
member; there is nothing on the interface to override.

Recipe:

1. Interface keeps only the methods declared without where-clauses.
2. Each default-method-with-where-clause becomes a Kotlin extension
   whose own type-parameter bound mirrors the where-clause.
3. Concrete subtypes specialise by declaring a same-named member.
4. Callers holding the unbounded interface type cannot invoke the
   comparison-using methods — correct, Rust would reject the same
   call without the bound.

### Pair with the dual-overload pattern when both paths are needed

When a function has to work in both the comparator-aware and natural-order
paths, expose two overloads — the unbounded one takes the comparator
explicitly, the bounded one is sugar:

```kotlin
internal fun <Q> Tree.search(key: Q, compare: (Stored, Q) -> Int): Hit { /* heavy */ }

internal fun <Q : Comparable<Q>> Tree.search(key: Q): Hit
    where Stored : Comparable<Q> =
    search(key) { stored, query -> stored.compareTo(query) }
```

Heavy lifting in the comparator overload; natural-order overload is a
one-line delegation. The canonical implementation lives in
[`btree-kotlin`](../btree-kotlin/) `Search.kt::searchTree` /
`searchNode` / `findLowerBoundEdge` / `findUpperBoundEdge` and
`Navigate.kt::searchTreeForBifurcation` / `lowerBound` / `upperBound`.

### Why this is faithful, not engineering

- Interface mirrors Rust's trait declaration shape exactly.
- Extension's bound mirrors Rust's `where` clause exactly.
- Concrete-class members shadow the extension exactly the way Rust
  inherent-impl methods override a trait default.
- "Unbounded callers can't use these methods" mirrors Rust's
  compile-time rejection without the bound.
- No runtime casts, no `IllegalStateException`, no `is Comparable<*>`.

### When you cannot apply this

When the bound is on a *class* type parameter (e.g. `impl<K: Ord> Map<K, V>`),
Kotlin has no method-level analog — class type parameters bind for the
whole class. Use the `Comparator<in K>` field pattern with a
`compareKeys(a, b)` dispatch helper that prefers the supplied
comparator and falls back to a `Comparable<K>`-based path. The fallback
is the design contract, not a translation hack.

---

## AST Distance Tool

A vendored cross-language AST comparison tool for analyzing port progress and identifying priority files.

### Location
```
tools/ast_distance/
├── CMakeLists.txt
├── README.md
├── include/
│   ├── ast_parser.hpp      # Tree-sitter parsing for Rust/Kotlin/C++
│   ├── codebase.hpp        # Directory scanning, dependency graphs, matching
│   ├── imports.hpp         # Import/include extraction, package detection
│   ├── node_types.hpp      # Normalized AST node type mappings
│   ├── similarity.hpp      # Cosine similarity, combined scoring
│   └── tree.hpp            # Tree data structure
└── src/
    ├── main.cpp            # CLI entry point
    ├── ast_parser.cpp
    ├── ast_normalizer.cpp
    └── similarity.cpp
```

### Build
```bash
cd tools/ast_distance
mkdir -p build && cd build
cmake .. && make -j8
```

### Commands

**Analyze this project (Rust → Kotlin):**
```bash
./ast_distance --deep ../../../codex-rs rust ../../../src kotlin
```

**Check what's missing:**
```bash
./ast_distance --missing <src_dir> <src_lang> <tgt_dir> <tgt_lang>
```

**Scan a codebase (dependency graph):**
```bash
./ast_distance --deps <directory> <rust|kotlin|cpp>
```

**Compare two files directly:**
```bash
./ast_distance file1.rs file2.kt
```

**Dump AST structure:**
```bash
./ast_distance --dump <file> <rust|kotlin|cpp>
```

### Output Interpretation

The `--deep` command outputs:
- **Matched files** with similarity scores (0.0–1.0)
- **Priority score** = dependents × (1 - similarity) — high priority = many dependents + low similarity
- **Incomplete ports** (similarity < 60%)
- **Missing files** not yet ported

Similarity thresholds:
- `> 0.85` — Excellent port, likely complete
- `0.60–0.85` — Good port, may need refinement
- `0.40–0.60` — Partial port, significant gaps
- `< 0.40` — Stub or very different implementation

### Extending the Tool

**Add a new language:**
1. Add tree-sitter grammar to `CMakeLists.txt` (FetchContent)
2. Add `extern "C" { const TSLanguage* tree_sitter_<lang>(); }` to `ast_parser.hpp` and `imports.hpp`
3. Add `<LANG>` to `enum class Language` in `ast_parser.hpp`
4. Add `<lang>_node_to_type()` mapping in `node_types.hpp`
5. Add import/package extraction in `imports.hpp`
6. Update file extension checks in `codebase.hpp`

**Improve matching:**
- Edit `name_match_score()` in `codebase.hpp` for better fuzzy matching
- Edit `PackageDecl::similarity_to()` in `imports.hpp` for package-aware matching

### Using for Porting Decisions

Run periodically to track progress:
```bash
# Save baseline
./ast_distance --deep ... > baseline.txt

# After porting work
./ast_distance --deep ... > current.txt
diff baseline.txt current.txt
```

Focus porting effort on files with:
1. High dependent count (core infrastructure)
2. Low similarity score (incomplete)
3. Listed in "Top priority to complete" output

---

## Swarm Task Management

The AST distance tool includes a task assignment system for coordinating multiple agents porting files in parallel.

### Initialize Tasks

Generate a task file from missing/incomplete ports:
```bash
./ast_distance --init-tasks <src_dir> <src_lang> <tgt_dir> <tgt_lang> <task_file> [agents_md_path]
```

Example:
```bash
./ast_distance --init-tasks ../../../codex-rs rust ../../../src kotlin tasks.json ../../../AGENTS.md
```

### View Task Status

```bash
./ast_distance --tasks tasks.json
```

### Assign a Task (for Swarm Agents)

Each agent requests the next highest-priority unassigned task:
```bash
./ast_distance --assign tasks.json agent-001
```

This command:
- Assigns the highest-priority pending task (by dependent count)
- Prevents duplicate assignments (one task per agent)
- Outputs complete porting instructions including AGENTS.md guidelines
- Updates the task file with assignment timestamp

### Complete a Task

After successfully porting a file:
```bash
./ast_distance --complete tasks.json core.error
```

### Release a Task

If an agent cannot complete a task, release it back to pending:
```bash
./ast_distance --release tasks.json core.error
```

### Task Workflow for Swarm Agents

1. **Get assignment**: `ast_distance --assign tasks.json <agent-id>`
2. **Read source file** at the path shown
3. **Create target file** with port-lint header
4. **Transliterate** following the guidelines above
5. **Verify**: `ast_distance <source.rs> rust <target.kt> kotlin` (aim for >= 0.85 similarity)
6. **Complete**: `ast_distance --complete tasks.json <source_qualified>`
7. **Repeat** from step 1
