plugins {
    id("com.android.library")
    kotlin("android")
    id("com.origins-digital.security")
}

android {
    compileSdk = AppConfigs.Android.compileSdk

    defaultConfig {
        consumerProguardFiles(*file("proguards").listFiles())
        minSdk = AppConfigs.Android.minSdk
        targetSdk = AppConfigs.Android.targetSdk
    }

    compileOptions {
        sourceCompatibility = AppConfigs.javaVersion
        targetCompatibility = AppConfigs.javaVersion
    }

    kotlinOptions {
        jvmTarget = AppConfigs.javaVersion.toString()
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), *file("proguards").listFiles())
        }
    }
}


dependencies {
    api(project(":predictorShared"))
    api(Deps.Coroutines.android)
    api(Deps.Resources.entitiesExt)
    api(Deps.Koin.core)
    api(Deps.Koin.android)
    implementation(Deps.AndroidX.browser)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.swipeRefresh)
    implementation(Deps.AndroidX.recycler)
    implementation(Deps.AndroidX.core)
    implementation(Deps.AndroidX.navigationFragments)
    implementation(Deps.Other.material)
    implementation(Deps.Glide.glide)
    implementation(Deps.Glide.glideCompiler)
    implementation(Deps.NetcoComponents.container)
    implementation(Deps.NetcoComponents.kotlinExtensions)
    implementation(Deps.NetcoComponents.archExtensions)
    implementation(Deps.Other.gson)
    implementation(Deps.QRcode.ZXing)

    debugImplementation(Deps.Other.leakCanary)
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
    artifactId = Publishing.getCoreArtifactId(project),
    addDependencies = true,
    extraDependencies = listOf(Publishing.getSharedArtifactId(project))
) {
    com.originsdigital.security.ReleaseUtils.buildProductGithubReleaseNotes(
        project,
        listOf("${Publishing.predictorGroupId}:${Publishing.getCustomizationArtifactId(project)}:${Publishing.getPredictorVersion()}")
    )
}




