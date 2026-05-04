plugins {
    kotlin("multiplatform") version "2.3.21"
    kotlin("plugin.serialization") version "2.3.21"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
}

kotlin {
    applyDefaultHierarchyTemplate()

    sourceSets.all {
        languageSettings.optIn("kotlin.time.ExperimentalTime")
    }

    macosArm64 {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    macosX64 {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    // Define Linux target so we can confine certain dependencies/code to Linux only
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.5.4")

                // Ktor HTTP client core (engine wired in nativeMain via curl)
                implementation("io.ktor:ktor-client-core:3.4.3")
                implementation("io.ktor:ktor-client-content-negotiation:3.4.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.3")
                implementation("io.ktor:ktor-client-auth:3.4.3")

                // File I/O
                implementation("com.squareup.okio:okio:3.9.0")

                // Character encoding support (for legacy codepage conversion)
                implementation("com.fleeksoft.io:io-core:0.0.5")
                implementation("com.fleeksoft.charset:charset:0.0.5")
                implementation("com.fleeksoft.charset:charset-ext:0.0.5")

                // TUI libraries (from Maven Central) — published as KMP artifacts
                implementation("io.github.kotlinmania:ratatui-kotlin:0.1.9")
                implementation("io.github.kotlinmania:crossterm-kotlin:0.1.4")
                implementation("io.github.kotlinmania:ansi-to-tui-kotlin:0.1.4")
                implementation("io.github.kotlinmania:anstyle-kotlin:0.1.4")
                implementation("io.github.kotlinmania:kasuari-kotlin:0.1.2")
                implementation("io.github.kotlinmania:roff-kotlin:0.1.4")
                implementation("io.github.kotlinmania:cansi-kotlin:0.1.4")

                // JWT library (from Maven Central)
                implementation("io.github.kotlinmania:jwt-kmp:0.2.2")

                // JSON Schema types (Schema, SchemaObject, JsonSchema trait).
                // Resolved via sibling includeBuild; see settings.gradle.kts.
                implementation("io.github.kotlinmania:schemars-kotlin:0.1.0-SNAPSHOT")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
            }
        }

        val nativeMain by getting {
            dependencies {
                // Native HTTP engine for Ktor
                implementation("io.ktor:ktor-client-curl:3.4.3")

                // Tree-sitter parsing library bindings (cinterop, native-only)
                implementation("io.github.tree-sitter:ktreesitter:0.24.1")
                implementation("io.github.tree-sitter:ktreesitter-bash:0.23.3")
            }
        }

        val nativeTest by getting
    }
}
