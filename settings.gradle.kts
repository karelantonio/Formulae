pluginManagement {
    repositories {
        // ============offline==========
        val offline_dir = File("/media/storage/MavenRepositories")
        if (offline_dir.isDirectory()) {
            offline_dir.listFiles().forEach {

                if(!it.isDirectory())
                    return@forEach

                val repo_url = it.toURI().toURL()
                maven {
                    println("Repo: ${repo_url}")
                    url = repo_url.toURI()
                }
            }
        }
        // ============end offline==========
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Formulae"
include(":app")
