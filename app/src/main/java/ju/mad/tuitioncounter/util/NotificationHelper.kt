package ju.mad.tuitioncounter.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ju.mad.tuitioncounter.MainActivity

object NotificationHelper {

    private const val CHANNEL_ID = "class_reminder_channel"
    const val NOTIFICATION_ID = 1002
    const val ACTION_NO = "ACTION_NO"

    fun showClassReminder(context: Context) {
        createNotificationChannel(context)

        // Intent for "Yes" - Open the app
        val yesIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("open_from_notification", true)
        }
        val yesPendingIntent = PendingIntent.getActivity(
            context,
            0,
            yesIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        // Intent for "No" - Dismiss notification
        val noIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_NO
        }
        val noPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            noIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        // Build notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Class Count Reminder")
            .setContentText("Did you go to any of your Tuition today?")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(yesPendingIntent) // Tapping notification opens app
            .addAction(
                android.R.drawable.ic_input_add,
                "Yes",
                yesPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "No",
                noPendingIntent
            )
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Class Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily reminder to log classes"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun cancelNotification(context: Context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
    }
}