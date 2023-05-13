import org.gradle.api.JavaVersion

object AppConfigs {

    val javaVersion = JavaVersion.VERSION_11

    object Android {
        const val compileSdk = 31
        const val minSdk = 21
        const val targetSdk = compileSdk
        const val packageNameDev = "com.demo.andgaming"
        const val packageNameProd = "com.demo.andgaming_prod"
        const val versionName = "2.0.0"
        const val versionOffset = 0
        const val masterBranch = "main"

        fun getAppVersionName(): String {
            return System.getenv("VERSION_NAME") ?: versionName
        }

        fun getAppVersionCode(): Int {
            val versionCode = System.getenv("VERSION_CODE")
            if (versionCode != null) {
                return versionCode.toInt()
            }
            val buildNumber = System.getenv("BUILD_NUMBER")
            if (buildNumber != null) {
                return buildNumber.toInt() + versionOffset
            }
            return 1
        }

        fun getBranchName(): String {
            return System.getenv("BRANCH_NAME") ?: masterBranch
        }

        fun getAppVersionSuffix(module: String): String {
            return when (val branchName = getBranchName()) {
                masterBranch, null -> ""
                else -> "-${listOfNotNull(module, branchName).joinToString("-")}"
            }
        }
    }


    val whitelabel = AppConfig(
        name = "whitelabel",
        localization = L10n(
            spreadsheetId = "1Oqov29M3yXi3FIaHyRNPAtn5Hj-RpTdlIfqYxxsaTW4",
            langConfigs = listOf(
                LangConfig("en", "C", isDefault = true),
                LangConfig("fr", "D")
            )
        )
    )

    private val fff = AppConfig(
        name = "fff",
        localization = L10n(
            spreadsheetId = "14M6AZS4UMdP_nkYUlHnyAYObpRE47bvqiXM5I-_TgR8",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("fr", "D", isDefault = true)
            )
        )
    )

    private val infrontWhitelabel = AppConfig(
        name = "infront_whitelabel",
        localization = L10n(
            spreadsheetId = "1Oqov29M3yXi3FIaHyRNPAtn5Hj-RpTdlIfqYxxsaTW4",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("it", "E", isDefault = true)
            )
        )
    )

    private val infrontGenoa = AppConfig(
        name = "infront_genoa",
        localization = L10n(
            spreadsheetId = "1R6X_q0Vt5tLgEt4npLR7NDKprS1Bw0mxPrl4l6Rw8Tc",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("it", "E", isDefault = true)
            )
        )
    )

    private val infrontHellas = AppConfig(
        name = "infront_hellas",
        localization = L10n(
            spreadsheetId = "1wGAlUYaB4qgrERBAbVDtRiSV-FdWGj5cMqWQQNPM5b8",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("it", "E", isDefault = true)
            )
        )
    )

    private val infrontLazio = AppConfig(
        name = "infront_lazio",
        localization = L10n(
            spreadsheetId = "1613aSYOfX1rfeC6QDbT6Zp3ld6a1jtntMknERl6jSSg",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("it", "E", isDefault = true)
            )
        )
    )

    private val infrontSampdoria = AppConfig(
        name = "infront_sampdoria",
        localization = L10n(
            spreadsheetId = "1CBnfKr3jIX7jXHlryZ4biLDXXojUgdzUkYP-qiX4f5A",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("it", "E", isDefault = true)
            )
        )
    )

    private val infrontUdinese = AppConfig(
        name = "infront_udinese",
        localization = L10n(
            spreadsheetId = "1tZVMRegTU6p_RXY7uc1Rrq2Bin56cMLcsTPsWMfzdqU",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("it", "E", isDefault = true)
            )
        )
    )

    private val fiorentina = AppConfig(
        name = "fiorentina",
        localization = L10n(
            spreadsheetId = "1d5I6XO0wzXb-Q-wxLz2-EtNmXzRSG_VoE-ocszFgK80",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("it", "E", isDefault = true)
            )
        )
    )

    private val clubappWhitelabel = AppConfig(
        name = "clubapp_whitelabel",
        localization = L10n(
            spreadsheetId = "1pDNeNGJq5waTcDXYy-8V9hR3pW4OHdDzqvvx77_Ial0",
            langConfigs = listOf(
                LangConfig("en", "C", isDefault = true),
                LangConfig("fr", "D"),
                LangConfig("pt", "F")
            )
        )
    )


    private val clubappVitoria = AppConfig(
        name = "clubapp_vitoria",
        localization = L10n(
            spreadsheetId = "1VSoFeDTL9f0yi5zd9Ek-lOqG1kw-_zCk_k6gCvKk98E",
            langConfigs = listOf(
                LangConfig("en", "C", isDefault = true),
                LangConfig("pt", "F")
            )
        )
    )

    private val clubappLeHavre = AppConfig(
        name = "clubapp_le_havre",
        localization = L10n(
            spreadsheetId = "1eufA3kmdHcrBVWEV0SZSF7PtfrKwTMFM-nv2mn-sjBA",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("fr", "D", isDefault = true)
            )
        )
    )

    private val clubappAsse = AppConfig(
        name = "clubapp_asse",
        localization = L10n(
            spreadsheetId = "1a9w6zZ3wr8PxYWALrzEJUjq3RSr-60asxnrTm23gkTw",
            langConfigs = listOf(
                LangConfig("fr", "D", isDefault = true),
                LangConfig("en", "C"),
                LangConfig("pt", "D"),
                LangConfig("it", "D"),
            )
        )
    )

    private val clubappClermont = AppConfig(
        name = "clubapp_clermont",
        localization = L10n(
            spreadsheetId = "1CgBN2StD548ZDyftiJ6aMIIRp6NpRDPa8Tmi7F7gsfY",
            langConfigs = listOf(
                LangConfig("en", "C"),
                LangConfig("fr", "D", isDefault = true)
            )
        )
    )

    val configs = listOf(
        whitelabel,
        fff,
        infrontWhitelabel,
        infrontGenoa,
        infrontHellas,
        infrontLazio,
        infrontUdinese,
        infrontSampdoria,
        fiorentina,
        clubappWhitelabel,
        clubappVitoria,
        clubappLeHavre,
        clubappAsse,
        clubappClermont
    )
}



