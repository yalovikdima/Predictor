package com.origins.predictor.base

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.origins.predictor.domain.config.ContestantEntity

actual class PredictorDataStorage(private val prefs: SharedPreferences) {

    private val gson = GsonBuilder().create()

    actual fun getContestant(): ContestantEntity? {
        return gson.fromJson(prefs.getString(PREFS_CONTESTANT, null), ContestantEntity::class.java)
    }

    actual fun storeContestant(contestant: ContestantEntity?) {
        prefs.edit()
            .putString(PREFS_CONTESTANT, gson.toJson(contestant))
            .apply()
    }

    companion object {
        private const val PREFS_CONTESTANT = "PREFS_CONTESTANT"
    }
}