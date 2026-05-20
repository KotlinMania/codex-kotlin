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

// schemars (JsonSchema trait + Schema types) lives in the sibling
// schemars-kotlin repo. When that sibling checkout is present (local dev),
// composite-build it and substitute the unpublished
// io.github.kotlinmania:schemars-kotlin Maven coordinate. On CI runners
// (which only check out this repo), fall through to the declared remote
// repositories so the published Maven artifact resolves instead.
val schemarsLocal = file("../schemars-kotlin")
if (schemarsLocal.exists()) {
    includeBuild(schemarsLocal) {
        dependencySubstitution {
            substitute(module("io.github.kotlinmania:schemars-kotlin")).using(project(":"))
        }
    }
}
