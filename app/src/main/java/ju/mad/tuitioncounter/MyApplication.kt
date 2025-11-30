package ju.mad.tuitioncounter

import android.app.Application
import ju.mad.tuitioncounter.util.ClassReminderScheduler

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Schedule daily reminder
        ClassReminderScheduler.scheduleReminder(applicationContext)
    }
}