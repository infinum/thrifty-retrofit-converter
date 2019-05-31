buildscript {
    repositories {
        maven { url = uri("https://plugins.gradle.org/m2/") }
        jcenter()
    }
    dependencies {
        classpath("com.novoda:bintray-release:0.9.1")
    }
}

allprojects {
    repositories {
        jcenter()
    }
}
