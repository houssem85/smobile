package fr.strada.smobile.utils.brodcast_receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.strada.smobile.R
import fr.strada.smobile.utils.NOTIFICATION_CHANNEL_ID
import fr.strada.smobile.utils.NOTIFICATION_POINTEUSE_ID
import io.paperdb.Paper

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?)
    {
        val isChronoStartd: Boolean? = Paper.book().read<Boolean>("isChronoStarted", false)
        if(isChronoStartd!!)
        {
            showNotificationPointeuseIfNotShown(p0!!)
        }
    }

    private fun showNotificationPointeuseIfNotShown(context:Context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifications = notificationManager.activeNotifications
            for (notification in notifications) {
                if (notification.id == NOTIFICATION_POINTEUSE_ID) {
                    return
                }
            }
        }
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("SMobile")
            .setContentText(context.getString(R.string.appwidget_text_start_pointage))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setChannelId(NOTIFICATION_CHANNEL_ID)
            .setVibrate(longArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0))
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_POINTEUSE_ID, builder.build())
        }
    }
}