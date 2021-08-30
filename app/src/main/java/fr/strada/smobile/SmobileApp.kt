package fr.strada.smobile

import android.Manifest
import android.app.*
import android.content.*
import android.location.LocationManager
import android.os.Build
import android.os.CountDownTimer
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.*
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.araujo.jordan.excuseme.ExcuseMe
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.data.repositories.PointeuseRepository
import fr.strada.smobile.data.repositories.UserRepository
import fr.strada.smobile.di.app.AppComponent
import fr.strada.smobile.di.app.DaggerAppComponent
import fr.strada.smobile.ui.activities.mensuel.Utils.fromIsoFormatToDate
import fr.strada.smobile.ui.notification_pointeuse.NotificationService
import fr.strada.smobile.ui.notification_pointeuse.NotificationService.Companion.CODE_COULEUR_ACTIVITIE_POINTEUSE_STARTED
import fr.strada.smobile.ui.notification_pointeuse.NotificationService.Companion.DATE_START_ACTIVITIE_POINTEUSE_STARTED
import fr.strada.smobile.ui.notification_pointeuse.NotificationService.Companion.ICON_ACTIVITIE_POINTEUSE_STARTED
import fr.strada.smobile.ui.notification_pointeuse.NotificationService.Companion.ID_ACTIVITIE_POINTEUSE_STARTED
import fr.strada.smobile.ui.notification_pointeuse.NotificationService.Companion.LIBELLE_ACTIVITIE_POINTEUSE_STARTED
import fr.strada.smobile.ui.pointeuse.PointeuseWorker
import fr.strada.smobile.ui.shortcut.ShortcutService
import fr.strada.smobile.ui.widget.SMobileWidget
import fr.strada.smobile.utils.NOTIFICATION_CHANNEL_ID
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.ext.checkPermissionLocation
import fr.strada.smobile.utils.ext.getLastLocation
import io.paperdb.Paper
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.abs

/**
 * Smobile app
 *
 * @constructor Create empty Smobile app
 */
class SmobileApp : Application(), Configuration.Provider, HasAndroidInjector {

    lateinit var appComponent: AppComponent

    @Inject
    @Named("api_gateway")
    lateinit var api: Api
    private val initAttributePointeuseReceiver = InitAttributePointeuseReceiver()
    //private val initGPSCheckReceiver = InitGPSCheckReceiver()
    val duration = MutableLiveData<Int>()
    var isChronoStarted = MutableLiveData<Boolean>()
    var dateStartActivite: Date? = null
    private var idActivitiePointeuseStarted = ""
    var codeCouleurActivitiePointeuseStarted = ""
    var libelleActivitiePointeuseStarted = ""
    var iconActivitiePointeuseStarted = ""

    @Inject
    internal lateinit var pointeuseRepository: PointeuseRepository

    @Inject
    internal lateinit var userRepository: UserRepository

    @Inject
    internal lateinit var workerFactory: WorkerFactory

    var cardread: Boolean = false

    companion object {
        var instance: SmobileApp? = null
            private set
        const val INIT_ATTRIBUTE_POINTEUSE = "fr.strada.smobile.INIT_ATTRIBUTE_POINTEUSE"
        var tonnentID = ""
    }

    private val timer = object : CountDownTimer(500000000000000, 1000) {

        override fun onFinish() {

        }

        override fun onTick(millisUntilFinished: Long) {
            val now: Date?
            try {
                now = Date()
                duration.value = ((abs(now.time - dateStartActivite!!.time)) / 1000).toInt()
                val hours: Int = duration.value!! / 3600
                val minutes: Int = (duration.value!! / 60) % 60
                val seconds: Int = duration.value!! % 60
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } catch (ex: Exception) {
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initComponent()
        createNotificationChannel()
        registerInitAttributePointeuseReceiver()
        //registerGPSCheckReceiver()
        //sendGPSCheckReceiver()
        initRealm(this)
        Paper.init(this)
        collectTonnent()
        getDeviceSuperInfo()
        val uniqueId = UUID.randomUUID().toString()
        Timber.tag("UUID").e(uniqueId)
    }

    private fun collectTonnent() {
        GlobalScope.launch {
            userRepository.getTenantFlow().collect {
                tonnentID = it
            }
        }
    }

    /**
     * Init attributes pointeuse
     *
     */
    fun initAttributesPointeuse() {
        GlobalScope.launch(Dispatchers.IO) {
            val res = pointeuseRepository.getDerniereActivitiePointeuse()
            if (res.status == Status.SUCCESS) {
                val derniereActivitiePointeurse = res.data
                val isActivitiePointeurseFinished =
                    derniereActivitiePointeurse!!.finActivite != null
                if (isActivitiePointeurseFinished) {
                    reset()
                } else {
                    try {
                        idActivitiePointeuseStarted = derniereActivitiePointeurse.id
                        dateStartActivite =
                            fromIsoFormatToDate(derniereActivitiePointeurse.dateActivite!!)
                        derniereActivitiePointeurse.typeActivite?.let { typeActivitePointeuseModel ->
                            typeActivitePointeuseModel.codeCouleur?.let {
                                codeCouleurActivitiePointeuseStarted = it
                            }
                            typeActivitePointeuseModel.libelle?.let {
                                libelleActivitiePointeuseStarted = it
                            }
                            typeActivitePointeuseModel.icone?.let {
                                iconActivitiePointeuseStarted = it
                            }
                        }
                    } catch (ex: Exception) {
                        dateStartActivite = null
                    }
                    isChronoStarted.postValue(true)
                    timer.start()
                    val isUserModeBorne = userRepository.isUserModeBorne()
                    if (!isUserModeBorne) {
                        startNotificationService(
                            idActivitiePointeuseStarted,
                            derniereActivitiePointeurse.dateActivite!!,
                            codeCouleurActivitiePointeuseStarted,
                            libelleActivitiePointeuseStarted,
                            iconActivitiePointeuseStarted
                        )
                    }
                }
            } else {
                reset()
            }
        }
    }

    /**
     * Reset
     *
     */
    private fun reset() {
        GlobalScope.launch(Dispatchers.Main) {
            duration.value = 0
            isChronoStarted.value = false
            timer.cancel()
            val isUserModeBorne = userRepository.isUserModeBorne()
            if (!isUserModeBorne) {
                stopNotificationService()
            }
        }
    }

    /**
     * Init component
     *
     */
    fun initComponent() {
        appComponent = DaggerAppComponent.builder().application(this@SmobileApp).build()
        appComponent.inject(this@SmobileApp)

    }

    @VisibleForTesting
    fun setComponent(appComponent : AppComponent){
        this.appComponent = appComponent
    }

    private fun initRealm(context: Context) {
        Realm.init(context)
        val configuration = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name(resources.getString(R.string.app_name) + ".realm")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(configuration)
    }

    /**
     * Start
     *
     * @param context
     * @param typeActivitePointeuseId
     * @return
     */
    suspend fun start(
        context: Context,
        typeActivitePointeuseId: String = "1b53ba3c-954d-489a-8bb7-999f9fed504b"
    ): Boolean {
        val isPermissionGranted = checkPermissionLocation(context)
        if (!isPermissionGranted) {
            showDialogPermissionsAreDenied(context)
            return false
        }
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationActive = LocationManagerCompat.isLocationEnabled(lm)
        if (!locationActive) {
            showDialogActiverLocalisation(context)
            return false
        }
        var latitude = 0.0
        var longitude = 0.0
        val location = getLastLocation(context)
        location?.let {
            latitude = it.latitude
            longitude = it.longitude
        }
        val isTimeAutomatic = isTimeAutomatic(baseContext)
        if (isTimeAutomatic) {
            if (!isChronoStarted.value!!) {
                val res = pointeuseRepository.startActivitiePointeuse(
                    typeActivitePointeuseId,
                    latitude,
                    longitude
                )
                if (res.status == Status.SUCCESS) {
                    if (res.data == PointeuseWorker.FutureStatus.ENQUEUED) {
                        PointeuseWorker.enqueue(this@SmobileApp)
                    }
                    initAttributesPointeuse()
                    sendRefreshListActivitiesPointeuseReceiver()
                    refreshDataWidget()
                    return true
                }
            }
        } else {
            Toast.makeText(
                baseContext,
                baseContext.resources.getString(R.string.veuillez_activer_l_heure_automatique_sur_votre_appareil),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return false
    }


    /**
     * Stop
     *
     */
    fun stop(context: Context) {
        val isTimeAutomatic = isTimeAutomatic(baseContext)
        if (isTimeAutomatic) {
            if (isChronoStarted.value!!) {
                GlobalScope.launch(Dispatchers.Main) {
                    val isPermissionGranted = checkPermissionLocation(context)
                    if (!isPermissionGranted) {
                        showDialogPermissionsAreDenied(context)
                        return@launch
                    }
                    val lm: LocationManager =
                        getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val locationActive = LocationManagerCompat.isLocationEnabled(lm)
                    if (!locationActive) {
                        showDialogActiverLocalisation(context)
                        return@launch
                    }
                    var latitude = 0.0
                    var longitude = 0.0
                    val location = getLastLocation(context)
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude
                    }
                    val res = pointeuseRepository.stopActivitiePointeuse(
                        idActivitiePointeuseStarted,
                        latitude,
                        longitude
                    )
                    if (res.status == Status.SUCCESS) {
                        if (res.data == PointeuseWorker.FutureStatus.ENQUEUED) {
                            PointeuseWorker.enqueue(this@SmobileApp)
                        }
                        initAttributesPointeuse()
                        sendRefreshListActivitiesPointeuseReceiver()
                        refreshDataWidget()
                    }
                }
            }
        } else {
            Toast.makeText(
                baseContext,
                baseContext.resources.getString(R.string.veuillez_activer_l_heure_automatique_sur_votre_appareil),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Refresh data widget
     *
     */
    fun refreshDataWidget() {
        val intent = Intent(this, SMobileWidget::class.java)
        intent.action = SMobileWidget.REFRESH_WIDGET
        sendBroadcast(intent)
    }

    /**
     * Init attribute pointeuse receiver
     *
     * @constructor Create empty Init attribute pointeuse receiver
     */
    inner class InitAttributePointeuseReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            initAttributesPointeuse()
            sendRefreshListActivitiesPointeuseReceiver()
        }
    }

    private fun registerInitAttributePointeuseReceiver() {
        val filter = IntentFilter(INIT_ATTRIBUTE_POINTEUSE)
        this.registerReceiver(initAttributePointeuseReceiver, filter)
    }

    /*inner class InitGPSCheckReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            registerGPSCheckReceiver()
        }
    }

    private fun registerGPSCheckReceiver() {
        val filter = IntentFilter("checkGPSReceiver")
        this.registerReceiver(initGPSCheckReceiver, filter)
    }*/

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
                vibrationPattern = longArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
            }
            channel.enableVibration(false)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /*fun sendGPSCheckReceiver() {
        val intent = Intent("checkGPSReceiver")
        this.sendBroadcast(intent)
    }*/

    fun sendRefreshListActivitiesPointeuseReceiver() {
        val intent = Intent("refreshListActivitiesPointeuseReceiver")
        this.sendBroadcast(intent)
    }



    private fun isTimeAutomatic(c: Context): Boolean {
        return Settings.Global.getInt(c.contentResolver, Settings.Global.AUTO_TIME, 0) == 1
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }


    private fun showDialogActiverLocalisation(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Attention")
        builder.setMessage("Il faut activer la position pour lancer l'activité")
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
        builder.show()
    }


    private suspend fun showDialogPermissionsAreDenied(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ExcuseMe.couldYouGive(context).please(
                explainAgainTitle = "Une autorisation est nécessaire",
                explainAgainExplanation = "L'application a besoin de cette autorisation pour Lancer une activité",
                showSettingsTitle = "Définir l'autorisation dans les paramètres",
                showSettingsExplanation = "L'application ouvrira les paramètres pour modifier l'autorisation à partir de là"
            )
                .permissionFor(Manifest.permission.ACCESS_FINE_LOCATION) && ExcuseMe.permissionFor(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) && ExcuseMe.permissionFor(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        } else {
            ExcuseMe.couldYouGive(context).please(
                explainAgainTitle = "Une autorisation est nécessaire",
                explainAgainExplanation = "L'application a besoin de cette autorisation pour Lancer une activité",
                showSettingsTitle = "Définir l'autorisation dans les paramètres",
                showSettingsExplanation = "L'application ouvrira les paramètres pour modifier l'autorisation à partir de là"
            )
                .permissionFor(Manifest.permission.ACCESS_FINE_LOCATION) && ExcuseMe.permissionFor(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    private fun startNotificationService(
        idActivitiePointeuseStarted: String, dateActivite: String,
        codeCouleurActivitiePointeuseStarted: String,
        libelleActivitiePointeuseStarted: String,
        iconActivitiePointeuseStarted: String
    ) {
        val intent = Intent(this@SmobileApp, NotificationService::class.java)
        intent.putExtra(ID_ACTIVITIE_POINTEUSE_STARTED, idActivitiePointeuseStarted)
        intent.putExtra(DATE_START_ACTIVITIE_POINTEUSE_STARTED, dateActivite)
        intent.putExtra(
            CODE_COULEUR_ACTIVITIE_POINTEUSE_STARTED,
            codeCouleurActivitiePointeuseStarted
        )
        intent.putExtra(LIBELLE_ACTIVITIE_POINTEUSE_STARTED, libelleActivitiePointeuseStarted)
        intent.putExtra(ICON_ACTIVITIE_POINTEUSE_STARTED, iconActivitiePointeuseStarted)
        ContextCompat.startForegroundService(this@SmobileApp, intent)
    }

    fun stopNotificationService() {
        val intent = Intent(this@SmobileApp, NotificationService::class.java)
        stopService(intent)
    }

    fun startShortCutService() {
        val intent = Intent(this, ShortcutService::class.java)
        startService(intent)
    }

    private fun getDeviceSuperInfo() {
        Timber.e("getDeviceSuperInfo")
        try {
            var s = "Debug-infos:"
            s += """
 OS Version: ${System.getProperty("os.version")}(${Build.VERSION.INCREMENTAL})"""
            s += """
 OS API Level: ${Build.VERSION.SDK_INT}"""
            s += """
 Device: ${Build.DEVICE}"""
            s += """
 Model (and Product): ${Build.MODEL} (${Build.PRODUCT})"""
            s += """
 RELEASE: ${Build.VERSION.RELEASE}"""
            s += """
 BRAND: ${Build.BRAND}"""
            s += """
 DISPLAY: ${Build.DISPLAY}"""
            s += """
 CPU_ABI: ${Build.CPU_ABI}"""
            s += """
 CPU_ABI2: ${Build.CPU_ABI2}"""
            s += """
 UNKNOWN: ${Build.UNKNOWN}"""
            s += """
 HARDWARE: ${Build.HARDWARE}"""
            s += """
 Build ID: ${Build.ID}"""
            s += """
 MANUFACTURER: ${Build.MANUFACTURER}"""
            s += """
 SERIAL: ${Build.SERIAL}"""
            s += """
 USER: ${Build.USER}"""
            s += """
 HOST: ${Build.HOST}"""
           Timber.e(s)
        } catch (e: java.lang.Exception) {
           Timber.e("Error getting Device INFO")
        }
    } //end getDeviceSuperInfo

}
