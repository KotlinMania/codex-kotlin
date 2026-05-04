pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
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

// schemars (JsonSchema trait + Schema types) lives in the sibling
// schemars-kotlin repo. Build it from the local checkout until it
// publishes to Maven Central, substituting the unpublished
// io.github.kotlinmania:schemars-kotlin Maven coordinate.
includeBuild("../schemars-kotlin") {
    dependencySubstitution {
        substitute(module("io.github.kotlinmania:schemars-kotlin")).using(project(":"))
    }
}
