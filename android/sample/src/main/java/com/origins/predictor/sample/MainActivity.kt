package com.origins.predictor.sample

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.netcosports.arch.extensions.getViewModel
import com.origins.predictor.sample.databinding.MainActivityBinding
import com.squareup.seismic.ShakeDetector

class MainActivity : AppCompatActivity(), ShakeDetector.Listener {

    private val shakeDetector by lazy { ShakeDetector(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.container)
        val mainViewModel: MainViewModel = getViewModel()

        val isRemoveCurrentUser = intent.getBooleanExtra(EXTRA_IS_REMOVE_CURRENT_USER, false)

        mainViewModel.optaId.observe(this) {
            if (it != null) {
                binding.errorMessage.isVisible = false
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        binding.fragmentContainer.id,
                        SamplePredictorFragment.newInstance(optaId = it, isClearUser = isRemoveCurrentUser)
                    )
                    .commit()
            } else {
                binding.errorMessage.isVisible = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        shakeDetector.start(getSystemService(SENSOR_SERVICE) as SensorManager)
    }

    override fun onStop() {
        shakeDetector.stop()
        super.onStop()
    }

    override fun hearShake() {
        startActivity(UserActivity.getLaunchIntent(this))
    }

    companion object {
        private const val EXTRA_IS_REMOVE_CURRENT_USER = "EXTRA_IS_REMOVE_CURRENT_USER"

        fun getLaunchIntent(context: Context, isRemoveCurrentUser: Boolean = false): Intent {
            return Intent(context, MainActivity::class.java)
                .putExtra(EXTRA_IS_REMOVE_CURRENT_USER, isRemoveCurrentUser)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}


