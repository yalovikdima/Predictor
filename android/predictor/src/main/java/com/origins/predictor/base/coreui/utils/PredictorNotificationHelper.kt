package com.origins.predictor.base.coreui.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.origins.predictor.base.core.di.PredictorKoinComponent
import com.origins.predictor.base.coreui.utils.web.ActivityHolder
import org.koin.core.component.inject

internal class PredictorNotificationHelper(private val app: Application) : PredictorKoinComponent {

    var isNotify = false
    private val activityHolder: ActivityHolder by inject()
    private val launchIntent: Intent
        get() {
            val currentActivity = activityHolder.currentActivity
            return if (currentActivity == null) {
                app.packageManager?.getLaunchIntentForPackage(app.packageName)!!
            } else {
                Intent(app, currentActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }

    private val notificationChannelId: String
        get() {
            return "${app.packageName}.notification"
        }

    private val notificationChannelName: String
        get() = "Notification"

    fun showNotification(context: Context, title: String, body: String, icon: Int) {
        val notificationId = 10
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, getNotification(context, title, body, icon))
        isNotify = true
    }

    private fun getNotification(context: Context, title: String?, body: String?, icon: Int): Notification {
        val channelId = createNotificationChannel(context)

        val pendingIntent = PendingIntent.getActivity(
            context, NOTIFICATION_REQUEST_CODE, launchIntent, PendingIntent.FLAG_ONE_SHOT
        )
        return NotificationCompat.Builder(context, channelId)
            .setContentIntent(pendingIntent)
            .setContentText(body)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setSmallIcon(icon).build()
    }

    private fun createNotificationChannel(context: Context): String {
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            service.createNotificationChannel(channel)
            notificationChannelId
        } else {
            ""
        }
    }


    companion object {
        private const val NOTIFICATION_REQUEST_CODE = 10
    }

}