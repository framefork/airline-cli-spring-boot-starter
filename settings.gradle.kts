import kotlin.io.path.listDirectoryEntries

pluginManagement {
}

dependencyResolutionManagement {
    versionCatalogs {
    }
}

rootProject.name = "airline-cli-spring-boot-starter"

file("${rootProject.projectDir}/modules").toPath().listDirectoryEntries().forEach { moduleDir ->
    include("${moduleDir.fileName}")
    project(":${moduleDir.fileName}").projectDir = moduleDir.toFile()
}