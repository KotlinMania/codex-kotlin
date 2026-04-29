// port-lint: source src/gen.rs
package io.github.kotlinmania.schemars

/**
 * The main type used to generate JSON Schemas.
 *
 * Holds the settings used during schema generation and tracks the
 * sub-schemas defined by the types encountered while generating a root
 * schema. The full upstream type carries `SchemaSettings` and a
 * definitions map; only the surface used by the current port is
 * preserved here.
 */
class SchemaGenerator
