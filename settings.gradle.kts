pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    val ktreesitterPluginLocal = file("ktreesitter-kotlin/ktreesitter-plugin")
    if (ktreesitterPluginLocal.exists()) {
        includeBuild(ktreesitterPluginLocal)
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "codex-kotlin"

val ktreesitterLocal = file("ktreesitter-kotlin")
if (ktreesitterLocal.exists()) {
    includeBuild(ktreesitterLocal) {
        dependencySubstitution {
            substitute(module("io.github.tree-sitter:ktreesitter")).using(project(":ktreesitter"))
            substitute(module("io.github.tree-sitter:ktreesitter-bash")).using(project(":languages:bash"))
        }
    }
}


