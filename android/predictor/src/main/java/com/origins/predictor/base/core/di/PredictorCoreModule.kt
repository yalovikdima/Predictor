package com.origins.predictor.base.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.origins.predictor.PredictorAnalytics
import com.origins.predictor.base.PredictorDataStorage
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.base.coreui.resources.ColorResourceProviderImpl
import com.origins.predictor.base.coreui.resources.ImageResourceProviderImpl
import com.origins.predictor.base.coreui.resources.StringResourceProviderImpl
import com.origins.predictor.base.coreui.resources.TextStyleResourceProviderImpl
import com.origins.predictor.base.coreui.utils.sharing.PredictorShareManager
import com.origins.predictor.di.PredictorDI
import com.origins.predictor.resources.ColorResourceProvider
import com.origins.predictor.resources.ImageResourceProvider
import com.origins.predictor.resources.StringResourceProvider
import com.origins.predictor.resources.TextStyleResourceProvider
import com.origins.predictor.utils.PredictorConfig
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun getPredictorCoreModule(
    config: PredictorConfig,
    analytics: PredictorAnalytics,
    isLogsEnabled: Boolean,
    qrCode: String?
) = module {
    single {
        PredictorDI(
            config = config,
            analytics = analytics,
            isLogsEnabled = isLogsEnabled,
            logger = PredictorLogger(isEnabled = isLogsEnabled),
            contestantStorage = PredictorDataStorage(PreferenceManager.getDefaultSharedPreferences(get())),
            stringResourceProvider = get(),
            imageResourceProvider = get(),
            textStyleResourceProvider = get(),
            colorResourceProvider = get()
        )
    }

    single<StringResourceProvider> { StringResourceProviderImpl() }
    single<ImageResourceProvider> { ImageResourceProviderImpl() }
    single<ColorResourceProvider> { ColorResourceProviderImpl() }
    single<TextStyleResourceProvider> { TextStyleResourceProviderImpl() }

    single { get<PredictorDI>().commonDataDI.analytics }
    single { get<PredictorDI>().commonDataDI.logger }
    single { get<PredictorDI>().predictorConfigDI.providePredictorConfigRepository() }
    factory { get<PredictorDI>().gameDataDI.provideGameRepository() }

    factory(PREDICTOR_BG_DISPATCHER_QUALIFIER) { Dispatchers.IO }
    factory(PREDICTOR_UI_DISPATCHER_QUALIFIER) { Dispatchers.Main }
    single(PREDICTOR_COMPUTATION_DISPATCHER_QUALIFIER) { Dispatchers.Default }

    single { provideSettingsPreferences(androidApplication()) }
    single<Gson> {
        GsonBuilder().create()
    }

    factory {
        PredictorShareManager(androidApplication(), qrCode, get(PREDICTOR_BG_DISPATCHER_QUALIFIER))
    }
}

val PREDICTOR_DEV_VERSION_QUALIFIER: StringQualifier get() = named("DEV_VERSION")
val PREDICTOR_BG_DISPATCHER_QUALIFIER: StringQualifier get() = named("BG_DISPATCHER")
val PREDICTOR_UI_DISPATCHER_QUALIFIER: StringQualifier get() = named("UI_DISPATCHER")
val PREDICTOR_COMPUTATION_DISPATCHER_QUALIFIER: StringQualifier get() = named("COMPUTATION_DISPATCHER")

private const val PREFERENCES_FILE_KEY = "com.andgaming.predictor_preferences"

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

