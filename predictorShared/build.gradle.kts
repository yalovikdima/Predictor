import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version Deps.Kotlin.kotlinVersion
    id("com.google.devtools.ksp").version(Deps.kspVersion)
}

tasks.register<Copy>("copyReleaseXCframework") {
    from(layout.buildDirectory.dir("XCFrameworks/release"))
    into(layout.projectDirectory.dir("build"))
}

tasks.register<Copy>("copyDebugXCframework") {
    from(layout.buildDirectory.dir("XCFrameworks/debug"))
    into(layout.projectDirectory.dir("build"))
}

project.afterEvaluate {
    project.task("assembleReleaseXCFramework") {
        dependsOn("assemblePredictorSharedReleaseXCFramework")
    }
    project.getTasksByName("assemblePredictorSharedReleaseXCFramework", true).forEach {
        it.finalizedBy("copyReleaseXCframework")
    }
    project.task("assembleDebugXCFramework") {
        dependsOn("assemblePredictorSharedDebugXCFramework")
    }
    project.getTasksByName("assemblePredictorSharedDebugXCFramework", true).forEach {
        it.finalizedBy("copyDebugXCframework")
    }
}

val moduleName = "predictorShared"

version = "1.0.0"
kotlin {
    android()

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = moduleName
            export(Deps.Gaba.core)
            export(Deps.Resources.entities)
            transitiveExport = true
            xcf.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Ktor.core)
                implementation(Deps.Ktor.logging)
                implementation(Deps.Ktor.serialization)
                implementation(Deps.Ktor.json)
                implementation(Deps.Other.kotlinSerialization)
                implementation(Deps.Shared.klock)

                implementation(Deps.NetcoSign.curl)
                api(Deps.Gaba.core)

                implementation(Deps.Rooibos.rooibos)
                api(Deps.Resources.entities)

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.Other.material)
                implementation(Deps.Ktor.android)
                api(Deps.AndroidX.prefs)
                implementation(Deps.Other.gson)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(Deps.Ktor.ios)
            }
        }
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
dependencies {
    add("kspIosArm64", Deps.Rooibos.processor)
}

ksp {
    arg("rooibos.access.modifier", "internal")
    arg("rooibos.shared.module.name", moduleName)
    arg("rooibos.codegen.path", rootProject.projectDir.absolutePath + "/iOS_Sources/KMM/Generated")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

initArtifactory()

publishArtifacts(
    sourcesJar = sourcesJar,
    configurations = configurations,
    project = project,
    artifactId = Publishing.getSharedArtifactId(project),
    addDependencies = true,
    extraDependencies = emptyList()
)
