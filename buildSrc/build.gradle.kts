plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://artifactory-blr.netcodev.com/artifactory/libs-release") {
        credentials {
            username = properties["repoUsername"].toString()
            password = properties["repoPassword"].toString()
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("com.android.tools.build:gradle:7.2.1")

    implementation("com.google.gms:google-services:4.3.13")
    implementation("com.google.firebase:firebase-crashlytics-gradle:2.9.1")
    implementation("com.google.firebase:firebase-appdistribution-gradle:3.0.2")

    implementation("com.origins.resources:origins-resources-plugin:2.1.3")
    implementation("com.origins-digital.security:security:0.5.4")

    implementation("org.jfrog.buildinfo:build-info-extractor-gradle:4.28.1")
}
