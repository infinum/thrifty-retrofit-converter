import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    id("java")
    kotlin("jvm") version "1.4.30"
    id("jacoco")
    id("maven-publish")
    id("signing")
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

object Versions {
    const val thrifty_version = "2.1.2"
    const val retrofit_version = "2.9.0"
    const val okhttp_version = "4.9.1"
    const val okio_version = "2.10.0"
}

dependencies {
    api("com.microsoft.thrifty:thrifty-runtime:${Versions.thrifty_version}")
    api("com.microsoft.thrifty:thrifty-runtime-ktx:${Versions.thrifty_version}")
    api("com.squareup.retrofit2:retrofit:${Versions.retrofit_version}")
    implementation("com.squareup.okio:okio:${Versions.okio_version}")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp_version}")
    testImplementation("org.assertj:assertj-core:3.19.0")
    testImplementation("junit:junit:4.13.2")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

tasks {
    check {
        dependsOn(jacocoTestReport)
    }
}

// use new JVM IR https://blog.jetbrains.com/kotlin/2021/02/the-jvm-backend-is-in-beta-let-s-make-it-stable-together/
// TODO remove after it becomes the default in Kotlin 1.5
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        useIR = true
    }
}

publishing {
    repositories {
        maven {
            name = "MavenCentral"
            url = URI.create(project.property("sonatypeUrl") as String)
            credentials {
                username = project.property("sonatypeUsername") as String
                password = project.property("sonatypePassword") as String
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            groupId = rootProject.group as String?
            artifactId = project.name
            version = rootProject.version as String?

            from(components["java"])

            pom {
                name.set("Thrifty Retrofit converter")
                description.set("Retrofit converter for Thrifty implementation of Apache Thrift")
                url.set("https://github.com/infinum/thrifty-retrofit-converter")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("reisub")
                        name.set("Dino Kovač")
                        email.set("dino@infinum.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/infinum/thrifty-retrofit-converter.git")
                    developerConnection.set("scm:git:ssh://github.com/infinum/thrifty-retrofit-converter.git")
                    url.set("https://github.com/infinum/thrifty-retrofit-converter")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["library"])
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}