package fr.strada.smobile.ui.notification_pointeuse

import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.android.AndroidInjection
import fr.strada.smobile.BuildConfig
import fr.strada.smobile.R
import fr.strada.smobile.ui.activities.mensuel.Utils
import fr.strada.smobile.ui.pointeuse.OpenPointeuseActivity
import fr.strada.smobile.utils.NOTIFICATION_CHANNEL_ID
import fr.strada.smobile.utils.NOTIFICATION_POINTEUSE_ID
import fr.strada.smobile.utils.ext.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.util.*

class NotificationService : Service(){

    companion object {
        const val ID_ACTIVITIE_POINTEUSE_STARTED = "ID_ACTIVITIE_POINTEUSE_STARTED"
        const val DATE_START_ACTIVITIE_POINTEUSE_STARTED = "DATE_START_ACTIVITIE_POINTEUSE_STARTED"
        const val CODE_COULEUR_ACTIVITIE_POINTEUSE_STARTED = "CODE_COULEUR_ACTIVITIE_POINTEUSE_STARTED"
        const val LIBELLE_ACTIVITIE_POINTEUSE_STARTED = "LIBELLE_ACTIVITIE_POINTEUSE_STARTED"
        const val ICON_ACTIVITIE_POINTEUSE_STARTED = "ICON_ACTIVITIE_POINTEUSE_STARTED"
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if(it.hasExtra(ID_ACTIVITIE_POINTEUSE_STARTED))
            {
                idActivitiePointeuseStarted =   it.getStringExtra(ID_ACTIVITIE_POINTEUSE_STARTED)!!
            }
            if(it.hasExtra(DATE_START_ACTIVITIE_POINTEUSE_STARTED))
            {
                dateStartActivitiePointeuseStarted = it.getStringExtra(DATE_START_ACTIVITIE_POINTEUSE_STARTED)!!
            }
            if(it.hasExtra(CODE_COULEUR_ACTIVITIE_POINTEUSE_STARTED))
            {
                codeCouleurActivitiePointeuseStarted = it.getStringExtra(CODE_COULEUR_ACTIVITIE_POINTEUSE_STARTED)!!
            }
            if(it.hasExtra(LIBELLE_ACTIVITIE_POINTEUSE_STARTED))
            {
                libelleActivitiePointeuseStarted = it.getStringExtra(LIBELLE_ACTIVITIE_POINTEUSE_STARTED)!!
            }
            if(it.hasExtra(ICON_ACTIVITIE_POINTEUSE_STARTED))
            {
                iconActivitiePointeuseStarted = it.getStringExtra(ICON_ACTIVITIE_POINTEUSE_STARTED)!!
            }
            if(!isTimerRunning){
                timer.start()
                isTimerRunning = true
            }
        }
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    var isTimerRunning = false
    var idActivitiePointeuseStarted = ""
    var codeCouleurActivitiePointeuseStarted = ""
    var libelleActivitiePointeuseStarted = ""
    var iconActivitiePointeuseStarted = ""
    var dateStartActivitiePointeuseStarted = ""

    private val timer = object : CountDownTimer(5000000, 1000) {

        override fun onFinish() {

        }

        override fun onTick(millisUntilFinished: Long) {
            var now: Date? = null
            try {
                now = Date()
                var dateStartActivite = Utils.fromIsoFormatToDate(dateStartActivitiePointeuseStarted)
                var duration  = ((abs(now.time - dateStartActivite!!.time)) / 1000).toInt()
                val hours: Int = duration / 3600
                val minutes: Int = (duration / 60) % 60
                val seconds: Int = duration  % 60
                val chronoText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                makeNotification(codeCouleurActivitiePointeuseStarted,libelleActivitiePointeuseStarted,iconActivitiePointeuseStarted,chronoText)
                if (chronoText == "02:00:00") {
                    makeNotificationTowHours()
                }
            } catch (ex: Exception) {
            }
        }

    }

    private fun makeNotification(codeCouleur:String,libelle:String,icon:String,duration:String)
    {
        GlobalScope.launch(Dispatchers.Main){
            try {
                val customView = RemoteViews(packageName, R.layout.notification_expanded)
                customView.setTextViewText(R.id.notif_txt_activity_title, libelle)
                customView.setTextViewText(R.id.notif_txt_timeclock_chrono, duration)
                var color : Int? = null
                try {
                    color = Color.parseColor(codeCouleur.take(7))
                }catch (ex:Exception){ }
                if(color != null){
                    customView.setTextColor(R.id.notif_txt_activity_title, color)
                    customView.setTextColor(R.id.notif_txt_timeclock_chrono, color)
                }
                var bitmap : Bitmap? = null
                try {
                    bitmap = loadImage(BuildConfig.URL_BASE_TIME +icon,applicationContext)
                }catch (ex:Exception){
                }
                if(bitmap != null){
                    customView.setImageViewBitmap(R.id.notif_play_pause_button, bitmap)
                }
                val resultIntent = Intent(this@NotificationService, OpenPointeuseActivity::class.java)
                val resultPendingIntent: PendingIntent? =
                    TaskStackBuilder.create(baseContext).run {
                        addNextIntentWithParentStack(resultIntent)
                        getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }
                val builder = NotificationCompat.Builder(
                    this@NotificationService,
                    NOTIFICATION_CHANNEL_ID
                )   .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(this@NotificationService.resources.getString(R.string.appwidget_text_start_pointage))
                    .setContentText(this@NotificationService.getString(R.string.appwidget_text_start_pointage))
                    .setShowWhen(false)
                    .setCustomContentView(customView)
                    .setCustomBigContentView(customView)
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)
                    .setContentIntent(resultPendingIntent)
                    .setChannelId(NOTIFICATION_CHANNEL_ID)
                    .setOnlyAlertOnce(true)
                    .setVibrate(longArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0))

                startForeground(NOTIFICATION_POINTEUSE_ID,builder.build())
            }catch (ex:Exception){

            }
        }
    }

    private fun makeNotificationTowHours() {
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Strada Smobile")
            .setContentText("N’oubliez pas de fermer votre pointeuse")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_POINTEUSE_ID + 1, builder.build())
        }
    }
}