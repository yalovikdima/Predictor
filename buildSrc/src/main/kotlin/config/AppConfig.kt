data class AppConfig(
    val name: String,
    val localization: L10n
)

data class L10n(
    val spreadsheetId: String,
    val langConfigs: List<LangConfig>
)
data class LangConfig(
    val lang: String,
    val column: String,
    val overriddenColumn: String = column,
    val isDefault: Boolean = false
)
