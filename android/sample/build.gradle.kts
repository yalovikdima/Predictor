import com.originsdigital.security.setupSigningConfig

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization") version Deps.Kotlin.kotlinVersion
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("com.starter.easylauncher") version Deps.easylauncherVersion
}

fun getCustomizationModuleName(): String {
    return Publishing.getModuleName() //"demo"
}


android {
    compileSdk = AppConfigs.Android.compileSdk
    defaultConfig {
        minSdk = AppConfigs.Android.minSdk
        targetSdk = AppConfigs.Android.targetSdk
        versionCode = AppConfigs.Android.getAppVersionCode()
        versionName = AppConfigs.Android.getAppVersionName()
        versionNameSuffix = AppConfigs.Android.getAppVersionSuffix(module = getCustomizationModuleName())
    }

    signingConfigs {
        create("release") {
            storeFile = file("${project.properties["keystoreLocation"]}/libs/Predictor/predictor.jks")
            storePassword = "rm|M3kXM1uyyCbJd"
            keyAlias = "netcosports"
            keyPassword = "rm|M3kXM1uyyCbJd"
        }
    }

    buildTypes {
        getByName("debug") {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }
        getByName("release") {
            isMinifyEnabled = false
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
            signingConfig = signingConfigs.getByName("release")
                .setupSigningConfig(project = project, keystoreId = "android_keystore_common_app")

            firebaseAppDistribution {
                releaseNotesFile = "ReleaseNotes.txt"
                groups = "netco-sports-belarus, product-team"
            }
        }
    }

    flavorDimensions += "env"
    productFlavors {
        create("development") {
            dimension = "env"
            applicationId = AppConfigs.Android.packageNameDev
        }

        create("production") {
            dimension = "env"
            applicationId = AppConfigs.Android.packageNameProd
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":android:customizations:${getCustomizationModuleName()}"))

    implementation(Deps.Kotlin.kotlin)
    implementation(Deps.Koin.android)

    implementation(Deps.Other.material)
    implementation(Deps.Other.gson)
    implementation(Deps.Other.kotlinSerialization)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.prefs)
    implementation(Deps.NetcoComponents.archExtensions)

    implementation(platform(Deps.Firebase.bom))
    implementation(Deps.Firebase.analytics)
    implementation(Deps.Firebase.crashlytics)
    implementation(Deps.Other.shaking)
}

tasks.register("updateConfig") {
    doLast {
        //EMPTY
    }
}

easylauncher {
    iconNames.add("@mipmap/ic_launcher")
    productFlavors.apply {
        register("development") {
            setFilters(
                customRibbon(
                    "v${Publishing.getCustomVersionName()}(${Publishing.getCustomVersionCode()})",
                    "#FFCD00",
                    "#1D1D1B",
                    com.project.starter.easylauncher.filter.ColorRibbonFilter.Gravity.BOTTOM
                )
            )
        }
    }
}