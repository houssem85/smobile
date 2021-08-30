package fr.strada.smobile.ui.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DateTimeWidgetUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.updateWidgets()
        context.scheduleNextWidgetUpdate()
    }
}
