pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins { kotlin("multiplatform") version "2.3.21" }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0" }

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "codex-kotlin"

fun includeSiblingPort(name: String) {
    val sibling = file("../$name")
    if (sibling.exists()) {
        includeBuild(sibling) {
            dependencySubstitution {
                substitute(module("io.github.kotlinmania:$name")).using(project(":"))
            }
        }
    }
}

listOf(
    "base64-kotlin",
    "bytes-kotlin",
    "eventsource-stream-kotlin",
    "http-kotlin",
    "reqwest-kotlin",
    "schemars-kotlin",
    "serde-json-kotlin",
    "serde-kotlin",
    "tokio-kotlin",
    "tokio-tungstenite-kotlin",
    "tokio-util-kotlin",
    "tree-sitter-bash-kotlin",
    "tree-sitter-kotlin",
    "url-kotlin",
).forEach(::includeSiblingPort)
