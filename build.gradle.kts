plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"
}

buildscript {
    repositories {
        maven { url = uri("https://plugins.gradle.org/m2/") }
        mavenCentral()
    }
    dependencies {
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

group = "com.infinum"
version = "3.0.0"

nexusPublishing {
    repositories {
        sonatype()
    }
}

//do not generate extra load on Nexus with new staging repository if signing fails
val initializeSonatypeStagingRepository by tasks.existing
subprojects {
    initializeSonatypeStagingRepository {
        shouldRunAfter(tasks.withType<Sign>())
    }
}