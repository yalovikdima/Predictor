package com.origins.predictor.base.coreui.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

internal inline fun <T> Flow<T>.observe(lifecycleOwner: LifecycleOwner, crossinline delegate: (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launchWhenStarted {
        this@observe.collect { delegate(it) }
    }
}
