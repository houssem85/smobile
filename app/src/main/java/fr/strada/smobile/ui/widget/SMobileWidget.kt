package fr.strada.smobile.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.provider.Settings
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RemoteViews
import android.widget.Toast
import com.auth0.android.authentication.storage.CredentialsManager
import dagger.android.AndroidInjection
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.access_functionalities.Functionality
import fr.strada.smobile.data.repositories.PointeuseRepository
import fr.strada.smobile.data.repositories.UserRepository
import fr.strada.smobile.ui.activities.mensuel.Utils
import fr.strada.smobile.ui.pointeuse.OpenPointeuseActivity
import fr.strada.smobile.ui.pointeuse.PointeuseWorker
import fr.strada.smobile.utils.Status
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.abs


class SMobileWidget : AppWidgetProvider() {

    companion object {
        const val STOP_POINTEUSE = "fr.strada.smobile.STOP_POINTEUSE"
        const val REFRESH_WIDGET = "fr.strada.smobile.REFRESH_WIDGET"
    }

    @Inject
    internal lateinit var userRepository : UserRepository
    @Inject
    internal lateinit var pointeuseRepository: PointeuseRepository
    @Inject
    internal lateinit var credentialsManager: CredentialsManager

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        refreshWidget(context)
        context.scheduleNextWidgetUpdate()
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onEnabled(context: Context) {
        initAttributesPointeuseFromServer(context)
        updateAuthorisedOrNot(context)
    }

    //-------------------- get Data ------------------------//

    fun initAttributesPointeuseFromServer(context: Context)
    {
        val userIsLoggedIn = credentialsManager.hasValidCredentials()
        if (userIsLoggedIn) {
            GlobalScope.launch(Dispatchers.IO){
                val res = pointeuseRepository.getDerniereActivitiePointeuse()
                if(res.status == Status.SUCCESS)
                {
                    val derniereActivitiePointeurse = res.data
                    val isActivitiePointeurseFinished = derniereActivitiePointeurse!!.finActivite != null
                    if (isActivitiePointeurseFinished) {
                        reset()
                        refreshWidget(context)
                    }else
                    {
                        Paper.book().write("isChronoStarted", true)
                        Paper.book().write("idActivitiePointeuseStarted", derniereActivitiePointeurse.id!!)
                        Paper.book().write("dateStartActivite", derniereActivitiePointeurse.dateActivite!!)
                        derniereActivitiePointeurse.typeActivite?.let {
                            it.libelle?.let { Paper.book().write("libelleActivitiePointeuseStarted", it)}
                            it.codeCouleur?.let { Paper.book().write("codeCouleurActivitiePointeuseStarted", it) }
                        }
                        refreshWidget(context)
                    }
                }else
                {
                    reset()
                    refreshWidget(context)
                }
            }
        } else {
            reset()
            refreshWidget(context)
        }
    }

    private fun reset() {
        Paper.book().write("isChronoStarted", false)
        Paper.book().delete("idActivitiePointeuseStarted")
        Paper.book().delete("dateStartActivite")
        Paper.book().delete("codeCouleurActivitiePointeuseStarted")
        Paper.book().delete("libelleActivitiePointeuseStarted")
    }

    private fun performUpdate(
        context: Context,
        duration: Int,
        isChoronoStarted: Boolean,
        hasValidSession: Boolean,
        codeCouleur: String,
        libelleActivitiePointeuseStarted: String
    ) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.getAppWidgetIds(getComponentName(context)).forEach {
            RemoteViews(context.packageName, R.layout.smobile_widget).apply {
                GlobalScope.launch(Dispatchers.Main){
                    val isUserModeBorne = userRepository.isUserModeBorne()
                    if (!isUserModeBorne) {
                        updateUserLoggedInOrNot(this@apply, hasValidSession)
                        updateText(context, this@apply, duration, isChoronoStarted, libelleActivitiePointeuseStarted)
                        updateButton( this@apply, isChoronoStarted, codeCouleur)
                        setUpActions(context, this@apply, isChoronoStarted)
                        appWidgetManager.updateAppWidget(it, this@apply)
                    }else
                    {
                        updateUserLoggedInOrNot(this@apply, hasValidSession)
                        this@apply.setText(R.id.txt_time, "Mode Tablette en Cours ")
                        setViewVisibility(R.id.btn_action, GONE)
                        setViewVisibility(R.id.txt_start_pointage, GONE)
                        appWidgetManager.updateAppWidget(it, this@apply)
                    }
                }
            }
        }
    }

    private fun updateUserLoggedInOrNot(views: RemoteViews, hasValidSession: Boolean) {

        views.apply {
            if (hasValidSession) {
                setViewVisibility(R.id.txtUserLoggedIn, GONE)
            } else {
                setViewVisibility(R.id.txtUserLoggedIn, VISIBLE)
            }
        }
    }

    private fun updateAuthorisedOrNot(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.getAppWidgetIds(getComponentName(context)).forEach {
            val remoteViews = RemoteViews(context.packageName, R.layout.smobile_widget)
            GlobalScope.launch(Dispatchers.Main){
                val res = userRepository.getUserFunctionalities()
                if(res.status == Status.SUCCESS){
                    val authorised = res.data!!.any { it.code == Functionality.POINTEUSE.code }
                    if (authorised) {
                        remoteViews.setViewVisibility(R.id.txt_not_authorised, GONE)
                    } else {
                        remoteViews.setViewVisibility(R.id.txt_not_authorised, VISIBLE)
                    }
                }else{
                    remoteViews.setViewVisibility(R.id.txt_not_authorised, VISIBLE)
                }
                appWidgetManager.updateAppWidget(it, remoteViews)
            }
        }
    }

    private fun updateButton(
        views: RemoteViews,
        isChoronoStarted: Boolean,
        codeCouleur: String
    ) {

        views.apply {
            if (!isChoronoStarted) {
                setImage(R.id.imgStopPlay, R.drawable.ic_play_pointeuse_white)
                val bitmap: Bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                val paint = Paint()
                paint.color = Color.parseColor("#5793CE")
                paint.strokeWidth = 200F
                paint.style = Paint.Style.STROKE
                canvas.drawCircle(250F, 250F, 150F, paint)
                this.setImageViewBitmap(R.id.circleView, bitmap)
            } else if (isChoronoStarted) {
                setImage(R.id.imgStopPlay, R.drawable.ic_stop_pointeuse)
                try {
                    val bitmap: Bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    val paint = Paint()
                    paint.color = Color.parseColor(codeCouleur.take(7))
                    paint.strokeWidth = 200F
                    paint.style = Paint.Style.STROKE
                    canvas.drawCircle(250F, 250F, 150F, paint)
                    this.setImageViewBitmap(R.id.circleView, bitmap)
                } catch (ex: Exception) {
                    val bitmap: Bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    val paint = Paint()
                    paint.color = Color.parseColor("#5793CE")
                    paint.strokeWidth = 200F
                    paint.style = Paint.Style.STROKE
                    canvas.drawCircle(250F, 250F, 150F, paint)
                    this.setImageViewBitmap(R.id.circleView, bitmap)
                }
            }
        }
    }

    private fun updateText(
        context: Context,
        views: RemoteViews,
        duration: Int,
        isChoronoStarted: Boolean,
        libelleActivitiePointeuseStarted: String
    ) {

        views.apply {
            val widgetImageView = R.drawable.logo_widget
            val hours: Int = duration / 3600
            val minutes: Int = (duration / 60) % 60
            val seconds: Int = duration % 60
            val str = String.format("%02d:%02d", hours, minutes, seconds)
              setText(R.id.txt_time, str)
            if (isChoronoStarted) {
                setText(
                    R.id.txt_start_pointage,
                    String.format(
                        context.resources.getString(R.string.appwidget_text_start_pointage),
                        libelleActivitiePointeuseStarted
                    )
                )
            } else {
                setText(
                    R.id.txt_start_pointage,
                    context.resources.getString(R.string.appwidget_text_end_pointage)
                )
            }
            setImage(R.id.img_logo_widget, widgetImageView)
        }
    }

    private fun setUpActions(context: Context, views: RemoteViews, isChronoStarted: Boolean) {
        var openAppIntent = Intent(context, OpenPointeuseActivity::class.java)
        val openAppPendingIntent = PendingIntent.getActivity(context, 0, openAppIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        views.apply {
            if (isChronoStarted) {
                setOnClickPendingIntent(
                    R.id.btn_action,
                    getPendingSelfIntent(context, STOP_POINTEUSE)
                )
            } else {
                setOnClickPendingIntent(
                    R.id.btn_action,
                    openAppPendingIntent
                )
            }
        }
    }

    private fun getPendingSelfIntent(context: Context?, action: String?): PendingIntent? {
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    private fun getComponentName(context: Context) = ComponentName(context, this::class.java)


    override fun onDisabled(context: Context) {
    }

    override fun onReceive(context: Context, intent: Intent?) {
        AndroidInjection.inject(this, context)
        super.onReceive(context, intent)
        when (intent?.action) {
            STOP_POINTEUSE -> {
                stop(context)
            }
            REFRESH_WIDGET -> {
                initAttributesPointeuseFromServer(context)
            }
        }
    }

    private fun refreshWidget(context: Context)
    {
        val isChronoStarted = Paper.book().read<Boolean>("isChronoStarted", false)
        val strDateStartActivite = Paper.book().read<String>("dateStartActivite", "")
        val codeCouleurActivitiePointeuseStarted =
            Paper.book().read<String>("codeCouleurActivitiePointeuseStarted", "#5793CE")
        val libelleActivitiePointeuseStarted =
            Paper.book().read<String>("libelleActivitiePointeuseStarted", "")
        var duration = 0
        if (isChronoStarted) {
            var now: Date?
            var dateStartActivite: Date?
            try {
                now = Date()
                dateStartActivite = Utils.fromIsoFormatToDate(strDateStartActivite)
            } catch (ex: Exception) {
                now = Date()
                dateStartActivite = Date()
            }
            duration = ((abs(now!!.time - dateStartActivite!!.time)) / 1000).toInt()
        }
        performUpdate(
            context,
            duration,
            isChronoStarted,
            credentialsManager.hasValidCredentials(),
            codeCouleurActivitiePointeuseStarted,
            libelleActivitiePointeuseStarted
        )
    }

    fun stop(context: Context)
    {
        val isTimeAutomatic = isTimeAutomatic(context)
        if (isTimeAutomatic) {
                val userIsLoggedIn = credentialsManager.hasValidCredentials()
                if (userIsLoggedIn) {
                    GlobalScope.launch (Dispatchers.Main){
                        val idActivitiePointeuseStarted = Paper.book().read<String>("idActivitiePointeuseStarted", "")
                        val res = pointeuseRepository.stopActivitiePointeuse(idActivitiePointeuseStarted,0.0,0.0)
                        if(res.status == Status.SUCCESS)
                        {
                            if(res.data == PointeuseWorker.FutureStatus.ENQUEUED)
                            {
                                PointeuseWorker.enqueue(context)
                            }
                            initAttributesPointeuseFromServer(context)
                            initAttributePointeuseInApplication(context)
                        }
                    }
                }
        } else {
            Toast.makeText(context, context.resources.getString(R.string.veuillez_activer_l_heure_automatique_sur_votre_appareil), Toast.LENGTH_LONG).show()
        }
    }

    private fun isTimeAutomatic(c: Context): Boolean {
        return Settings.Global.getInt(c.contentResolver, Settings.Global.AUTO_TIME, 0) == 1
    }

    private fun initAttributePointeuseInApplication(context: Context)
    {
        val intent = Intent(SmobileApp.INIT_ATTRIBUTE_POINTEUSE)
        context.sendBroadcast(intent)
    }
}



