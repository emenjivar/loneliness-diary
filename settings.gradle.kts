pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            // Used for `Material 3 adaptive layouts for Navigation 3` dependency
            url = uri("https://androidx.dev/snapshots/builds/13752615/artifacts/repository")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            // Used for `Material 3 adaptive layouts for Navigation 3` dependency
            url = uri("https://androidx.dev/snapshots/builds/13752615/artifacts/repository")
        }
    }
}

rootProject.name = "Loneliness diary"
include(":app")
include(":core:database")
include(":core:datastore")
include(":core:backup")
include(":core:ui")
include(":feature:diary")
include(":core:data")
