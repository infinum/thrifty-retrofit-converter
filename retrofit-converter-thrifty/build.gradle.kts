import com.novoda.gradle.release.PublishExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.4.30"
    id("jacoco")
    id("com.novoda.bintray-release")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "co.infinum"
version = "2.0.0"

object Versions {
    const val thrifty_version = "2.0.1"
    const val retrofit_version = "2.9.0"
    const val okhttp_version = "4.7.2"
    const val okio_version = "2.6.0"
}

dependencies {
    api("com.microsoft.thrifty:thrifty-runtime:${Versions.thrifty_version}")
    api("com.microsoft.thrifty:thrifty-runtime-ktx:${Versions.thrifty_version}")
    api("com.squareup.retrofit2:retrofit:${Versions.retrofit_version}")
    implementation("com.squareup.okio:okio:${Versions.okio_version}")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp_version}")
    testImplementation("org.assertj:assertj-core:3.16.1")
    testImplementation("junit:junit:4.13")
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

configure<PublishExtension> {
    userOrg = "infinum"
    groupId = project.group.toString()
    artifactId = "retrofit-converter-thrifty"
    publishVersion = project.version.toString()
    desc = "Retrofit converter for Thrifty implementation of Apache Thrift"
    website = "https://github.com/infinum/thrifty-retrofit-converter"
    repoName = "android"
    setLicences("Apache-2.0")
}
