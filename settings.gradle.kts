pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    // Include ktreesitter's custom Gradle plugin
    includeBuild("ktreesitter-kotlin/ktreesitter-plugin")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "codex-kotlin"

// Tree-sitter Kotlin bindings (vendored from wip/k2 branch)
includeBuild("ktreesitter-kotlin") {
    dependencySubstitution {
        substitute(module("io.github.tree-sitter:ktreesitter")).using(project(":ktreesitter"))
        substitute(module("io.github.tree-sitter:ktreesitter-bash")).using(project(":languages:bash"))
    }
}

// Use the local ratatui-kotlin checkout so we have the full API surface needed for strict
// transliteration (e.g., Paragraph/Wrap/WidgetRef/Terminal backend types).
includeBuild("/Volumes/stuff/Projects/kotlinmania/ratatui-kotlin") {
    dependencySubstitution {
        substitute(module("io.github.kotlinmania:ratatui-kotlin")).using(project(":"))
    }
}

// Use the local kasuari-kotlin checkout (constraint solver for TUI layouts).
// Needed because the Maven Central version (0.1.0) doesn't support all KMP targets.
includeBuild("/Volumes/stuff/Projects/kotlinmania/kasuari-kotlin") {
    dependencySubstitution {
        substitute(module("io.github.kotlinmania:kasuari-kotlin")).using(project(":"))
    }
}
