plugins {
    id("framefork.java")
    `maven-publish`
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenStaging") {
            from(components["java"])

            pom {
                url = "https://github.com/framefork/airline-cli-spring-boot-starter"
                inceptionYear = "2024"
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://spdx.org/licenses/Apache-2.0.html"
                    }
                }
                organization {
                    name = "Framefork"
                    url = "https://github.com/framefork"
                }
                developers {
                    developer {
                        id = "fprochazka"
                        name = "Filip Procházka"
                        url = "https://filip-prochazka.com/"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/framefork/airline-cli-spring-boot-starter.git"
                    developerConnection = "scm:git:ssh://github.com/framefork/airline-cli-spring-boot-starter.git"
                    url = "https://github.com/framefork/airline-cli-spring-boot-starter"
                }
                issueManagement {
                    system = "GitHub"
                    url = "https://github.com/framefork/airline-cli-spring-boot-starter/issues"
                }
            }

            afterEvaluate {
                pom.name = "${project.group}:${project.name}"
                pom.description = project.description
            }
        }
    }

    repositories {
        maven {
            url = uri(rootProject.layout.buildDirectory.dir("staging-deploy"))
        }
    }
}

tasks.named("publish") {
    dependsOn(rootProject.tasks.named("cleanAllPublications"))
}
