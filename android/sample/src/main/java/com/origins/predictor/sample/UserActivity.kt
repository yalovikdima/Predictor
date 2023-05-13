package com.origins.predictor.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.preference.PreferenceManager
import com.origins.predictor.sample.databinding.UserActivityBinding

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = UserActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val defaultDeviceID = getDefaultDeviceId(this)

        binding.realDeviceId.text = defaultDeviceID
        binding.deviceId.setText(defaultDeviceID)
        binding.realDeviceId.setOnClickListener {
            binding.deviceId.setText(defaultDeviceID)
        }

        val apis = ApiEntity.Type.values()
            .map { type -> ApiEntity(type) }
        apis.forEach { api ->
            val button = AppCompatRadioButton(this).apply {
                id = api.type.ordinal
                gravity = Gravity.CENTER
                text = api.type.name
                TextViewCompat.setTextAppearance(this, R.style.TextAppearance_MaterialComponents_Headline6)
                textSize = 12f
                setTextColor(ContextCompat.getColor(context, R.color.predictor_black))
            }
            binding.apiRadioGroup.addView(
                button,
                RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT, 1f)
            )
        }
        binding.apiRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val api = apis.first { api -> api.type.ordinal == checkedId }
            binding.currentApi.text = "accountKey=${api.type.accountKey}"
        }

        if (savedInstanceState == null) {
            val currentUser = getUser(this)
            binding.deviceId.setText(currentUser.deviceId ?: defaultDeviceID)
            binding.firstName.setText(currentUser.firstName)
            binding.lastName.setText(currentUser.lastName)
            binding.apiRadioGroup.check(getApi(this).type.ordinal)
        }

        binding.done.setOnClickListener {
            val newUser = UserEntity(
                deviceId = binding.deviceId.text?.toString()
                    ?.let { text -> if (text.isBlank()) defaultDeviceID else text },
                firstName = binding.firstName.text?.toString()?.let { text -> if (text.isBlank()) null else text },
                lastName = binding.lastName.text?.toString()?.let { text -> if (text.isBlank()) null else text },
            )

            setUser(this, newUser)
            val api = apis.first { api -> api.type.ordinal == binding.apiRadioGroup.checkedRadioButtonId }
            setApi(this, api)

            startActivity(MainActivity.getLaunchIntent(this))
            finish()
        }

        binding.doneWithoutUser.setOnClickListener {
            val api = apis.first { api -> api.type.ordinal == binding.apiRadioGroup.checkedRadioButtonId }
            setApi(this, api)
            clearUser(this)
            startActivity(MainActivity.getLaunchIntent(this, true))
            finish()
        }

//        if ("debug".equals(BuildConfig.BUILD_TYPE, true)) {
//            val firstName = "firstName"
//            val lastName = "lastName"
//            binding.firstName.setText(firstName)
//            binding.lastName.setText(lastName)
//            binding.deviceId.setText(defaultDeviceID)
//        }
    }

    companion object {
        private const val PREFS_DEVICE_ID = "PREFS_DEVICE_ID"
        private const val PREFS_FIRST_NAME = "PREFS_FIRST_NAME"
        private const val PREFS_LAST_NAME = "PREFS_LAST_NAME"
        private const val PREFS_API = "RPEFS_API"

        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }

        fun getUser(context: Context): UserEntity {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return UserEntity(
                deviceId = prefs.getString(PREFS_DEVICE_ID, null),
                firstName = prefs.getString(PREFS_FIRST_NAME, null),
                lastName = prefs.getString(PREFS_LAST_NAME, null),
            )
        }

        private fun setUser(context: Context, user: UserEntity) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREFS_DEVICE_ID, user.deviceId)
                .putString(PREFS_FIRST_NAME, user.firstName)
                .putString(PREFS_LAST_NAME, user.lastName)
                .apply()
        }

        private fun clearUser(context: Context) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREFS_DEVICE_ID)
                .remove(PREFS_FIRST_NAME)
                .remove(PREFS_LAST_NAME)
                .apply()
        }

        fun getApi(context: Context): ApiEntity {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = prefs.getInt(PREFS_API, ApiEntity.Type.PROD.ordinal)
            return ApiEntity(ApiEntity.Type.values().first { type -> type.ordinal == ordinal })
        }

        fun setApi(context: Context, api: ApiEntity) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREFS_API, api.type.ordinal)
                .apply()
        }

        private fun getDefaultDeviceId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }
    }
}
