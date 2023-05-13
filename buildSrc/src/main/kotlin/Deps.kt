object Deps {

    const val easylauncherVersion = "3.10.2"
    const val kspVersion = "1.7.10-1.0.6"

    object Kotlin {
        const val kotlinVersion = "1.7.10"
        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    }

    object QRcode {
        private const val ZXingVersion = "3.4.0"
        const val ZXing = "com.google.zxing:core:$ZXingVersion"
    }

    object Koin {
        private const val version = "3.1.2"
        const val core = "io.insert-koin:koin-core:$version"
        const val android = "io.insert-koin:koin-android:$version"
    }

    object Other {

        const val material = "com.google.android.material:material:1.4.0"
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"
        const val kotlinSerialization =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3"
        const val gson = "com.google.code.gson:gson:2.8.6"
        const val shaking = "com.squareup:seismic:1.0.2"
    }

    object Glide {
        private const val glideVersion = "4.12.0"
        const val glide = "com.github.bumptech.glide:glide:$glideVersion"
        const val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"
    }

    object Shared {
        const val klock = "com.soywiz.korlibs.klock:klock:2.4.13"
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.3.1"
        const val browser = "androidx.browser:browser:1.3.0"
        const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val recycler = "androidx.recyclerview:recyclerview:1.2.1"
        const val core = "androidx.core:core-ktx:1.6.0"
        const val fragment = "androidx.fragment:fragment-ktx:1.4.1"
        const val navigationFragments = "androidx.navigation:navigation-fragment-ktx:2.3.5"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.2"
        const val prefs = "androidx.preference:preference-ktx:1.1.1"
    }

    object Coroutines {
        const val version = "1.6.4"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }


    object NetcoComponents {
        const val netcoComponentsVersion = "1.4.3"
        const val adapter = "com.netcosports.components:adapter2:${netcoComponentsVersion}"
        const val container =
            "com.netcosports.components:container-views:${netcoComponentsVersion}"
        const val kotlinExtensions =
            "com.netcosports.components:kotlinextensions:${netcoComponentsVersion}"
        const val archExtensions =
            "com.netcosports.components:arch-extensions:${netcoComponentsVersion}"
        const val bindingExtensions =
            "com.netcosports.components:binding-extensions:${netcoComponentsVersion}"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:30.0.1"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics"
    }

    object Ktor {
        private const val ktorVersion = "1.6.7"
        const val core = "io.ktor:ktor-client-core:${ktorVersion}"
        const val json = "io.ktor:ktor-client-json:$ktorVersion"
        const val logging = "io.ktor:ktor-client-logging:${ktorVersion}"
        const val android = "io.ktor:ktor-client-okhttp:${ktorVersion}"
        const val ios = "io.ktor:ktor-client-ios:${ktorVersion}"
        const val serialization = "io.ktor:ktor-client-serialization:${ktorVersion}"
    }

    object NetcoSign {
        const val sign = "0.0.7"
        const val curl = "com.netcosports.kmm.signing:curl:${sign}"
    }

    object Gaba {

        const val version = "3.1.2"
        const val core = "com.origins.kmm.gaba:core:${version}"
        const val pagination = "com.origins.kmm.gaba:pagination:${version}"
    }

    object Cache {
        const val cacheVersion = "2.0.0"
        val core = "com.netcosports.kmm.cache:cache-core:$cacheVersion"
        val coreKtx = "com.netcosports.kmm.cache:cache-core-ktx:${cacheVersion}"
        val ktorCacheData = "com.netcosports.kmm.cache:ktor-cache-data:${cacheVersion}"
    }

    object Rooibos {
        const val version = "2.4.0"
        const val rooibos = "com.origins.kmm.rooibos:annotations:$version"
        const val processor = "com.origins.kmm.rooibos:processor:$version"
    }

    object Resources {
        const val entities = "com.origins.resources:entity:2.2.0"
        const val entitiesExt = "com.origins.resources:entity-extensions:2.1.2"
    }
}




