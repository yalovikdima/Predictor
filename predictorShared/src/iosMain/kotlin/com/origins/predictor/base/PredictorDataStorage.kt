package com.origins.predictor.base

import com.origins.predictor.domain.config.ContestantEntity
import kotlinx.coroutines.flow.*
import platform.Foundation.NSUserDefaults

actual class PredictorDataStorage {
    private val prefs = NSUserDefaults(suiteName = "PredictorConfig")

    actual fun getContestant(): ContestantEntity? {
        val externalId = prefs.stringForKey(PREFS_CONTESTANT_EXTERNAL_ID)
        val firstName = prefs.stringForKey(PREFS_CONTESTANT_FIRST_NAME)
        val lastName = prefs.stringForKey(PREFS_CONTESTANT_LAST_NAME)
        return ContestantEntity(
            externalId = externalId,
            firstName = firstName,
            lastName = lastName
        )
    }

    actual fun storeContestant(contestant: ContestantEntity?) {
        prefs.setObject(contestant?.externalId, PREFS_CONTESTANT_EXTERNAL_ID)
        prefs.setObject(contestant?.firstName, PREFS_CONTESTANT_FIRST_NAME)
        prefs.setObject(contestant?.lastName, PREFS_CONTESTANT_LAST_NAME)
        prefs.synchronize()
    }

    companion object {
        private const val PREFS_CONTESTANT_FIRST_NAME = "PREFS_CONTESTANT_FIRST_NAME"
        private const val PREFS_CONTESTANT_LAST_NAME = "PREFS_CONTESTANT_LAST_NAME"
        private const val PREFS_CONTESTANT_EXTERNAL_ID = "PREFS_CONTESTANT_EXTERNAL_ID"
    }
}