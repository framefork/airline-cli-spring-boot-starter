plugins {
    id("framefork.java-public")
}

dependencies {
    api(libs.airline)

    api(libs.spring.boot.starter)
    api(libs.spring.boot.autoconfigure)
    annotationProcessor(libs.spring.boot.configuration.processor)

    testImplementation(libs.logback.classic)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.junit.systemExit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    jvmArgumentProviders.add(CommandLineArgumentProvider {
        listOf("-javaagent:${configurations.testRuntimeClasspath.get().files.find {
            it.name.contains("junit5-system-exit") }
        }")
    })
}

project.description = "Starter that integrates Airline CLI into Spring Boot"
