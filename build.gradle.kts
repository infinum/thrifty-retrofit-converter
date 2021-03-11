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
