import com.origins.resources.plugin.SwiftAccessModifier
import com.origins.resources.plugin.dsl.*

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://artifactory-blr.netcodev.com/artifactory/libs-release") {
            credentials {
                username = repoUsername
                password = repoPassword
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}


subprojects {
    afterEvaluate {
        try {
            project.extensions.getByName<com.android.build.gradle.BaseExtension>("android")
        } catch (ignore: Throwable) {
            null
        }?.apply {
            compileSdkVersion(AppConfigs.Android.compileSdk)

            defaultConfig {
                minSdk = AppConfigs.Android.minSdk
                targetSdk = AppConfigs.Android.targetSdk
                vectorDrawables.useSupportLibrary = true
            }

            compileOptions {
                sourceCompatibility = AppConfigs.javaVersion
                targetCompatibility = AppConfigs.javaVersion
            }
            tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = AppConfigs.javaVersion.toString()
                }
            }

            sourceSets["main"].apply {
                res.srcDirs("src/main/res-gen")
            }
        }
    }
}

apply(plugin = "origins-resources-plugin")

resourcesPluginConfig {
    shared {
        moduleName = "predictorShared"
        codeGenPackage = "com.origins.predictor.resources"
    }

    this.android {
        coreModuleName = "predictor"
        coreModuleCodeGenPackage = "com.origins.predictor.base.coreui.resources"
    }

    this.ios {
        alidadeModuleName = null
        codeGenPath = "iOS_Sources/Resources/Generated/KMM"
        localizationPath = "iOS_Sources/Resources/Resources/Localization"
        colorPath = "iOS_Sources/Resources/Generated/KMM/Colors"
        textStylePath = "iOS_Sources/Resources/Generated/KMM/TextStyle"
        imagePath = "iOS_Sources/Resources/Resources/ExportedAssets.xcassets"
        fontPath = "iOS_Sources/Resources/Resources/Fonts"
        accessModifier = SwiftAccessModifier.INTERNAL
    }
}

val defaultAppConfig = AppConfigs.whitelabel
resourcesConfig {
    projectName = "predictor"
    customizations {
        AppConfigs.configs.forEach { appConfig ->
            customization(appConfig.name) {
                android {
                    module = appConfig.name
                    forceUseRaster = setOf(
                        "predictor_share_app_logo"
                    )
                }
            }
        }
    }
}

l10nConfig {
    sheetId = defaultAppConfig.localization.spreadsheetId

    android {
        replaceDotsInKeys = false
    }

    customizations {
        AppConfigs.configs.forEach { appConfig ->
            customization(appConfig.name) {
                overrideSheetId = appConfig.localization.spreadsheetId

                android {
                    module = appConfig.name
                }

                translations {
                    appConfig.localization.langConfigs.forEach { langConfig ->
                        translation {
                            lang = langConfig.lang
                            valueColumn = langConfig.column
                            overriddenValueColumn = langConfig.overriddenColumn
                            default = langConfig.isDefault
                        }
                    }
                }
            }
        }
    }
}