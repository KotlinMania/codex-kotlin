import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    kotlin("multiplatform") version "2.3.21"
    kotlin("plugin.serialization") version "2.3.21"
    id("com.android.kotlin.multiplatform.library") version "9.2.0"
    id("com.vanniktech.maven.publish") version "0.36.0"
}

group = "io.github.kotlinmania"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
}

val androidSdkDir: String? =
    providers.environmentVariable("ANDROID_SDK_ROOT").orNull
        ?: providers.environmentVariable("ANDROID_HOME").orNull

if (androidSdkDir != null && file(androidSdkDir).exists()) {
    val localProperties = rootProject.file("local.properties")
    if (!localProperties.exists()) {
        val sdkDirPropertyValue = file(androidSdkDir).absolutePath.replace("\\", "/")
        localProperties.writeText("sdk.dir=$sdkDirPropertyValue")
    }
}

kotlin {
    applyDefaultHierarchyTemplate()

    sourceSets.all {
        languageSettings.optIn("kotlin.time.ExperimentalTime")
        languageSettings.optIn("kotlin.concurrent.atomics.ExperimentalAtomicApi")
    }

    compilerOptions {
        allWarningsAsErrors.set(true)
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    val xcf = XCFramework("Codex")

    macosArm64 {
        binaries.framework {
            baseName = "Codex"
            xcf.add(this)
        }
    }
    linuxX64()
    mingwX64()
    iosArm64 {
        binaries.framework {
            baseName = "Codex"
            xcf.add(this)
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "Codex"
            xcf.add(this)
        }
    }
    js {
        browser()
        nodejs()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
    }

    swiftExport {
        moduleName = "Codex"
        flattenPackage = "io.github.solaceharmony.codex"
    }

    android {
        namespace = "io.github.solaceharmony.codex"
        compileSdk = 34
        minSdk = 24
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.11.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4.0")
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

                // JWT library (from Maven Central — sibling JWT-Kotlin)
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

    jvmToolchain(21)
}

rootProject.extensions.configure<YarnRootExtension>("kotlinYarn") {
    resolution("diff", "8.0.3")
    resolution("serialize-javascript", "7.0.5")
    resolution("webpack", "5.106.2")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(group.toString(), "codex-kotlin", version.toString())

    pom {
        name.set("codex-kotlin")
        description.set("Kotlin Multiplatform port of openai/codex — coding agent CLI")
        inceptionYear.set("2026")
        url.set("https://github.com/KotlinMania/codex-kotlin")

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://opensource.org/licenses/Apache-2.0")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("sydneyrenee")
                name.set("Sydney Renee")
                email.set("sydney@solace.ofharmony.ai")
                url.set("https://github.com/sydneyrenee")
            }
        }

        scm {
            url.set("https://github.com/KotlinMania/codex-kotlin")
            connection.set("scm:git:git://github.com/KotlinMania/codex-kotlin.git")
            developerConnection.set("scm:git:ssh://github.com/KotlinMania/codex-kotlin.git")
        }
    }
}
