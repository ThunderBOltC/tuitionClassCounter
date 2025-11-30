package ju.mad.tuitioncounter.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            NotificationHelper.ACTION_NO -> {
                // Just dismiss the notification
                NotificationHelper.cancelNotification(context)
            }
        }

    }
}