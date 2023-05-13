package com.origins.predictor.sample

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.origins.predictor.config.ConfigDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(), LifecycleObserver {

    private val config = ConfigDataSource()
    val optaId = MutableLiveData<String>()
    private var job: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        job = viewModelScope.launch {
            val configDeferred = async(Dispatchers.IO) {
                config.getConfig()
            }
            try {
                optaId.value = configDeferred.await()?.eventId
            } catch (e: Throwable) {
            }
        }
    }

}