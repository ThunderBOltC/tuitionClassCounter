package ju.mad.tuitioncounter.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Show notification
        NotificationHelper.showClassReminder(context)

        // Reschedule for next day
        ClassReminderScheduler.scheduleReminder(context)
    }
}