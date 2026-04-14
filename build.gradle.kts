plugins {
    kotlin("multiplatform") version "2.3.20"
    kotlin("plugin.serialization") version "2.3.20"
}

// =============================================================================
// AST Distance Tool Build Tasks
// =============================================================================

val astDistanceDir = project.file("tools/ast_distance")
val astDistanceBuildDir = astDistanceDir.resolve("build")
val astDistanceBinary = astDistanceBuildDir.resolve("ast_distance")
val astDistanceOutput = project.file("tools/ast_distance")

tasks.register<Exec>("configureAstTool") {
    description = "Configure AST distance tool with CMake"
    group = "build"

    workingDir = astDistanceBuildDir
    commandLine("cmake", "..")

    doFirst {
        astDistanceBuildDir.mkdirs()
    }

    onlyIf { !astDistanceBuildDir.resolve("Makefile").exists() }
}

tasks.register<Exec>("buildAstTool") {
    description = "Build AST distance tool"
    group = "build"

    dependsOn("configureAstTool")
    workingDir = astDistanceBuildDir
    commandLine("cmake", "--build", ".", "-j8")

    doLast {
        // Copy binary to tools folder for easy access
        if (astDistanceBinary.exists()) {
            astDistanceBinary.copyTo(astDistanceOutput.resolve("ast_distance"), overwrite = true)
            println("AST distance tool built: ${astDistanceOutput.resolve("ast_distance")}")
        }
    }
}

// =============================================================================
// Lint Tasks
// =============================================================================

val kotlinSrcDir = project.file("src/nativeMain/kotlin")

tasks.register<Exec>("portLint") {
    description = "Run port-lint checks on Kotlin codebase"
    group = "verification"

    dependsOn("buildAstTool")
    workingDir = astDistanceBuildDir

    commandLine(
        "./ast_distance", "--lint",
        kotlinSrcDir.absolutePath
    )

    isIgnoreExitValue = true

    doLast {
        println("\nPort lint completed. See above for any issues.")
    }
}

tasks.register<Exec>("portTodos") {
    description = "Scan for TODOs in ported Kotlin code"
    group = "verification"

    dependsOn("buildAstTool")
    workingDir = astDistanceBuildDir

    commandLine(
        "./ast_distance", "--todos",
        kotlinSrcDir.absolutePath
    )

    isIgnoreExitValue = true
}

tasks.register<Exec>("portStats") {
    description = "Show porting statistics"
    group = "verification"

    dependsOn("buildAstTool")
    workingDir = astDistanceBuildDir

    commandLine(
        "./ast_distance", "--stats",
        kotlinSrcDir.absolutePath
    )

    isIgnoreExitValue = true
}

tasks.register<Exec>("portDeep") {
    description = "Run deep porting analysis (Rust -> Kotlin)"
    group = "verification"

    dependsOn("buildAstTool")
    workingDir = astDistanceBuildDir

    val codexRs = project.file("codex-rs")

    commandLine(
        "./ast_distance", "--deep",
        codexRs.absolutePath, "rust",
        kotlinSrcDir.absolutePath, "kotlin"
    )

    isIgnoreExitValue = true
}

tasks.register<Exec>("portMissing") {
    description = "Show files missing from Kotlin port"
    group = "verification"

    dependsOn("buildAstTool")
    workingDir = astDistanceBuildDir

    val codexRs = project.file("codex-rs")

    commandLine(
        "./ast_distance", "--missing",
        codexRs.absolutePath, "rust",
        kotlinSrcDir.absolutePath, "kotlin"
    )

    isIgnoreExitValue = true
}

tasks.register("lint") {
    description = "Run all lint checks (Kotlin compilation + port lints)"
    group = "verification"

    dependsOn("compileKotlinMacosArm64", "portLint")

    doLast {
        println("\n=== All lint checks completed ===")
    }
}

tasks.register("portAnalysis") {
    description = "Run full porting analysis (stats, TODOs, lint, deep analysis)"
    group = "verification"

    dependsOn("portStats", "portTodos", "portLint", "portDeep")
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
        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.5.4")

                // Ktor HTTP client for native platforms
                implementation("io.ktor:ktor-client-core:2.3.7")
                implementation("io.ktor:ktor-client-curl:2.3.7")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
                implementation("io.ktor:ktor-client-auth:2.3.7")
                
                // File I/O
                implementation("com.squareup.okio:okio:3.9.0")

                // Character encoding support (for legacy codepage conversion)
                // fleeksoft-io provides JDK-like IO classes for Kotlin Multiplatform
                implementation("com.fleeksoft.io:io-core:0.0.5")
                implementation("com.fleeksoft.charset:charset:0.0.5")
                implementation("com.fleeksoft.charset:charset-ext:0.0.5")

                // Tree-sitter parsing library bindings
                implementation("io.github.tree-sitter:ktreesitter:0.24.1")
                implementation("io.github.tree-sitter:ktreesitter-bash:0.23.3")

                // TUI libraries (from Maven Central)
                implementation("io.github.kotlinmania:ratatui-kotlin:0.1.7")
                implementation("io.github.kotlinmania:ansi-to-tui-kotlin:0.1.3")
                implementation("io.github.kotlinmania:anstyle-kotlin:0.1.3")
                implementation("io.github.kotlinmania:kasuari-kotlin:0.1.1")
                implementation("io.github.kotlinmania:roff-kotlin:0.1.3")
                implementation("io.github.kotlinmania:cansi-kotlin:0.1.3")

                // JWT library (from Maven Central)
                implementation("io.github.kotlinmania:jwt-kmp:0.2.1")
            }
        }
        
        val nativeTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
