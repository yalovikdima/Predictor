import org.gradle.api.Project

object Publishing {
    const val contextUrl = "https://artifactory-blr.netcodev.com/artifactory"

    const val predictorGroupId = "com.netcosports.andgaming"
    const val predictorArtifactId = "predictor"

    const val publishingName = "predictor"

    fun getPredictorVersion(): String {
        return "${getCustomVersionName()}.${getCustomVersionCode()}-ktor-1.6"
    }

    fun getCustomVersionName(): String {
        return System.getenv("PRODUCT_VERSION") ?: AppConfigs.Android.versionName
    }

    fun getCustomVersionCode(): Int {
        val versionCode = System.getenv("VERSION_CODE")?.toIntOrNull()
        if (versionCode != null) {
            return versionCode
        }
        val buildNumber = System.getenv("BUILD_NUMBER")?.toIntOrNull()
        if (buildNumber != null) {
            return buildNumber
        }
        return 1
    }

    fun getCustomizationArtifactId(project: Project): String {
        val customizationModuleName = getModuleName()
        return getArtifactId(name = customizationModuleName)
    }

    fun getModuleName(default: String = "whitelabel"): String {
        return System.getenv("CUSTOMIZATION") ?: default
    }

    fun getCoreArtifactId(project: Project): String {
        return "${getCustomizationArtifactId(project)}-core"
    }

    fun getSharedArtifactId(project: Project): String {
        return "${getCustomizationArtifactId(project)}-shared"
    }

    private fun getCoreArtifactName(customizationModuleName: String): String {
        return "$customizationModuleName-core"
    }

    private fun getArtifactId(name: String): String {
        return "$predictorArtifactId-${fixCustomizationName(name)}"
    }

    private fun fixCustomizationName(projectName: String): String {
        return projectName.replace("_", "-")
    }
}