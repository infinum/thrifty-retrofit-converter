import com.novoda.gradle.release.PublishExtension

plugins {
    id("java")
    kotlin("jvm") version "1.3.31"
    id("jacoco")
    id("com.novoda.bintray-release")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "co.infinum"
version = "0.9.2"

object Versions {
    const val thrifty_version = "1.0.0"
    const val retrofit_version = "2.5.0"
    const val okhttp_version = "3.14.2"
    const val okio_version = "2.2.2"
}

dependencies {
    compile("com.microsoft.thrifty:thrifty-runtime:${Versions.thrifty_version}")
    compile("com.microsoft.thrifty:thrifty-runtime-ktx:${Versions.thrifty_version}")
    compile("com.squareup.okio:okio:${Versions.okio_version}")
    compile("com.squareup.retrofit2:retrofit:${Versions.retrofit_version}")
    testCompile("com.squareup.okhttp3:mockwebserver:${Versions.okhttp_version}")
    testCompile("org.assertj:assertj-core:3.9.0")
    testCompile("junit:junit:4.12")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = false
    }
}

tasks {
    check {
        dependsOn(jacocoTestReport)
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
