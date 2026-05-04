# schemars (partial port, in-tree)

This folder is a **partial Kotlin port of the [schemars](https://github.com/GREsau/schemars) Rust crate**, carried in-tree under `codex-kotlin` so that protocol types whose Rust source declares `impl JsonSchema for X` can be transliterated faithfully without dropping the trait impl.

It uses the namespace it would use as a standalone kotlinmania project:

```
io.github.kotlinmania.schemars
```

When the full port lands as `schemars-kotlin` at the workspace root, this folder gets deleted and replaced with a Maven coordinate dependency. No call site needs to change because the package name is already correct.

## Why this exists

`codex-kotlin` is a transliteration of `codex-rs`. Several protocol types in `codex-rs/protocol/src/` declare `impl JsonSchema for X` and call into schemars (`schemars::r#gen::SchemaGenerator`, `schemars::schema::Schema`, `<T>::json_schema(gen)`). A faithful Kotlin transliteration needs the same boundary types to exist on the Kotlin side. Inventing them inline in each consumer file would create rolling stubs and import-path drift; placing them under the kotlinmania namespace they will eventually live at gives every consumer a stable target.

## What is ported

Just the schemars surface that current `codex-kotlin` consumers reference. Each file's `// port-lint: source` header points to the upstream schemars file it transliterates from, relative to `schemars/` in the upstream repo:

| Kotlin file | Upstream file | Notes |
|-------------|---------------|-------|
| `JsonSchema.kt` | `src/lib.rs` | The `JsonSchema` trait — `schemaName()` and `jsonSchema(SchemaGenerator)`. The supporting trait methods upstream (`is_referenceable`, `schema_id`, `_schemars_private_…`) are not yet ported. |
| `SchemaGenerator.kt` | `src/gen.rs` | An empty class. Upstream carries `SchemaSettings`, a definitions map, visitors, and recursion guards. None of that is reachable from a `<String>::json_schema(gen)` call, which is the only call shape currently used. |
| `Schema.kt` | `src/schema.rs` | The `Schema` enum (`Bool` + `Object` variants) and a `SchemaObject` data class with only the `instanceType` field. The full upstream `SchemaObject` carries every JSON Schema keyword (`format`, `enum_values`, `subschemas`, validations, metadata, …). |
| `Primitives.kt` | `src/json_schema_impls/primitives.rs` | `StringJsonSchema` — the `JsonSchema` impl for the `String` primitive. Upstream `primitives.rs` provides impls for every numeric type, `bool`, `char`, `()`, etc. None of those are wired up yet. |

## What is not ported

Everything else, including:

- The `JsonSchema` derive macro (`#[derive(JsonSchema)]`). Codex-kotlin consumers currently hand-write the trait impl on each type, exactly the way the Rust source's `derive` would expand to.
- `SchemaSettings` and the configuration knobs on `SchemaGenerator` (draft selection, definitions path, visitors, inlining behavior).
- The remaining `json_schema_impls/` modules: `array.rs`, `core.rs`, `decimal.rs`, `ffi.rs`, `nonzero_signed.rs`, `nonzero_unsigned.rs`, `primitives.rs` beyond `String`, `sequences.rs`, `serdejson.rs`, `time.rs`, `tuple.rs`, `uuid.rs`, etc.
- `flatten.rs`, `visit.rs`, `transform.rs` — the schema-merging and walking infrastructure.
- The `gen::add_schema` / `gen::definitions` plumbing that lets a sub-schema register itself in the root document.
- Schema validation. Schemars itself does not validate; that's already off-scope, but listed here for future readers who might expect it.

## How to extend

When a new consumer needs schemars surface that this port doesn't yet provide:

1. **Identify the upstream file** the new surface lives in (`schemars/src/...`).
2. **Create or extend** the matching Kotlin file here, with the `// port-lint: source <upstream-path>` header. One Rust source file → one Kotlin file; do not bundle multiple upstream files into one Kotlin file.
3. **Port faithfully** — translate the type/function declarations line-by-line from the upstream source. If the upstream type has fields you don't yet need, leave them off and note "added as call sites need them" in the KDoc rather than inventing fields.
4. **Don't widen the surface speculatively.** This is "the pieces we need (for now)"; growth comes from real call sites, not anticipated ones.

## Migration to standalone `schemars-kotlin`

When schemars becomes its own kotlinmania project:

1. Move every file in this folder into `schemars-kotlin/src/commonMain/kotlin/io/github/kotlinmania/schemars/` verbatim.
2. Delete this folder.
3. Add the `schemars-kotlin` dependency to `codex-kotlin/build.gradle.kts`.
4. Run `ast_distance` against the moved files using their new project root; the `// port-lint: source` headers are already relative to `schemars/`, so provenance still resolves.

No import statement in `codex-kotlin` changes — the package path was always `io.github.kotlinmania.schemars`.

## Current consumers

- `io.github.solaceharmony.codex.protocol.ConversationId` — `impl JsonSchema for ConversationId` returns `<String>::json_schema(gen)`.

Add to this list as new consumers wire in.
