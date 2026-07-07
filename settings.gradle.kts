pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.framefork.build") version "0.4.0"
}

framefork {
    minJavaVersion = 17
    jdkVersion = 21
}

rootProject.name = "airline-cli-spring-boot-starter"
