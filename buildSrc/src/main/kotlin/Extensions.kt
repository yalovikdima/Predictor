import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

val Project.repoUsername: String get() = properties["repoUsername"].toString()
val Project.repoPassword: String get() = properties["repoPassword"].toString()

fun Project.setupAndroid() {
    (extensions.getByName("android") as? LibraryExtension)?.apply {
        compileSdk = AppConfigs.Android.compileSdk

        defaultConfig {
            minSdk = AppConfigs.Android.minSdk
            targetSdk = AppConfigs.Android.targetSdk

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

        lint {
            checkReleaseBuilds = false
            abortOnError = false
        }
    }
}