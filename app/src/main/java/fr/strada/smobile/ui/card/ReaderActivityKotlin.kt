package fr.strada.smobile.ui.card


import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.usb.*
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jaredrummler.android.device.DeviceName
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ramijemli.percentagechartview.PercentageChartView
import fr.strada.smobile.BuildConfig
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.local.SettingsPreferences
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.data.network.TachoFileAPI
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.utils.*
import fr.strada.smobile.utils.Convert.HexStringToByteArray
import fr.strada.smobile.utils.Convert.PerformHashOfFileByte
import fr.strada.smobile.utils.Convert.ToByte
import fr.strada.smobile.utils.Convert.hexStringToByteArray
import fr.strada.smobile.utils.Convert.toHexString
import fr.strada.smobile.utils.cardlib.BinaryReader
import fr.strada.smobile.utils.cardlib.DataParsing.*
import fr.strada.smobile.utils.cardlib.DataParsingG2
import fr.strada.smobile.utils.cardlib.models.*
import fr.strada.smobile.utils.data_stores.UserPreferences
import kotlinx.android.synthetic.main.activity_reader.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.experimental.and


class ReaderActivityKotlin : AppCompatActivity(), CoroutineScope by MainScope() {
    @Inject
    internal lateinit var userPreferences: UserPreferences

    @Inject
    internal lateinit var settingsPreferences: SettingsPreferences

    @Inject
    @Named("api_gateway")
    internal lateinit var apiNew : Api

    var uniqueId = UUID.randomUUID().toString()
    internal var identification: Identification? = null
    private var result: StringBuilder? = null
    private val buffer = ByteArray(300)
    internal var i = 0
    var log = fr.strada.smobile.data.models.Log()
    private var scanMode = false
    private lateinit var api: TachoFileAPI
    lateinit var storage: StorageCardTemporaryManager
    private var databaseRef: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("HistoryReponceSendFiles")
    private val stateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val action = intent.action ?: return
            if (action == STATE) {
                val extras = intent.extras
                if (extras != null) {

                    state =
                        extras.get(EXTRA_STATE) as State?
                    if (state != null) {
                        val sb = StringBuilder()
                        sb.append("Reader: stateChangeReceiver = ")
                        sb.append(state)
                        when (state!!.ordinal + 1) {
                            1 -> {

                                Timber.tag("Smart Card").d("state_connect_card_reader")
                                this@ReaderActivityKotlin.textViewState!!.setText(R.string.state_connect_card_reader)
                                this@ReaderActivityKotlin.imageViewState!!.setImageResource(R.drawable.connecter_le_lecteur_de_carte)
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "Connecter le lecteur de carte",
                                        "else"
                                    )
                                }
                                return
                            }
                            2 -> {
                                Timber.tag("Smart Card").d("state_grant_usb_device")
                                this@ReaderActivityKotlin.textViewState!!.setText(R.string.state_grant_usb_device)
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "Autoriser l’utilisation de l’outil USB",
                                        "else"
                                    )
                                }
                                return
                            }
                            3 -> {
                                Timber.tag("Smart Card").d("state_grant_refused")
                                this@ReaderActivityKotlin.textViewState!!.setText(R.string.state_grant_refused)
                                this@ReaderActivityKotlin.imageViewState!!.setImageResource(R.drawable.demande_authorisation_refuse)
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "Demande d’autorisation refusée",
                                        "else"
                                    )
                                }

                                return
                            }
                            4 -> {
                                Timber.tag("Smart Card").d("state_connect_card_reader")
                                this@ReaderActivityKotlin.textViewState!!.setText(R.string.state_connect_card_reader)
                                this@ReaderActivityKotlin.imageViewState!!.setImageResource(R.drawable.connecter_le_lecteur_de_carte)
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "Connecter le lecteur de carte",
                                        "else"
                                    )
                                }
                                return
                            }
                            5 -> {
                                Timber.tag("Smart Card").d("state_insert_driver_card")
                                this@ReaderActivityKotlin.textViewState!!.setText(R.string.state_insert_driver_card)
                                progressBar!!.setProgress(0f, false)

                                keyy = getRandomString(10)
                                this@ReaderActivityKotlin.imageViewState!!.setImageResource(R.drawable.inserer_la_carte)
                                index = 1
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    if (log.messages.isNotEmpty()) {
                                        if (log.messages[log.messages.size - 1] != "Insérer la carte conducteur dans le lecteur") {
                                            optimizeFireBase(
                                                read(),
                                                uniqueId,
                                                "Insérer la carte conducteur dans le lecteur",
                                                "else"
                                            )
                                        }
                                    }
                                }
                                return
                            }
                            7 -> {
                                SmobileApp.instance?.cardread = true
                                Timber.tag("Smart Card").d("state_reading")
                                this@ReaderActivityKotlin.textViewState!!.setText(R.string.state_reading)
                                this@ReaderActivityKotlin.imageViewState!!.setImageResource(R.drawable.lecture_en_cour)
                                start()
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "Lecture de carte en cours",
                                        "else"
                                    )
                                }
                                return

                            }
                            6 -> {
                                Timber.tag("Smart Card").d("state_use_driver_card")
                                this@ReaderActivityKotlin.textViewState!!.setText(R.string.state_use_driver_card)
                                this@ReaderActivityKotlin.imageViewState!!.setImageResource(R.drawable.carte_incompatible)
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "La carte inséré est incompatible !! Veuillez la retirer",
                                        "else"
                                    )
                                }
                                return
                            }
                            8 -> {
                                Timber.tag("Smart Card").d("state_remove_driver_card")
                                this@ReaderActivityKotlin.textViewState!!.setText(R.string.state_remove_driver_card)
                                this@ReaderActivityKotlin.imageViewState!!.setImageResource(R.drawable.retier_la_carte)
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "Retirer la carte du lecteur!",
                                        "else"
                                    )
                                }
                                return
                            }
                            else -> return
                        }
                    }
                }
            } else if (action == STATE_READ) {

            }
        }
    }
    private var reading: Boolean = false
    private var isNewCard: Boolean = false
    private lateinit var applicationIdentification: ApplicationIdentification
    private val cardInsertationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return

            if (action == SMART_CARD_PUT_IN) {

                powerOff()
                powerOn()
                SETSTATE(this@ReaderActivityKotlin, State.reading)


            } else if (action != SMART_CARD_GET_OUT) {
                Timber.tag("Smart Card").d("SMART_CARD_GET_OUT")
                progressBar!!.visibility = View.INVISIBLE
                imageViewState!!.visibility = View.VISIBLE
                SETSTATE(this@ReaderActivityKotlin, State.insert_driver_card)

            } else {
                Timber.tag("Smart Card").d("SMART_CARD_GET_OUT")
                if (state != State.connect_card_reader) {
                    progressBar!!.visibility = View.INVISIBLE
                    imageViewState!!.visibility = View.VISIBLE
                    SETSTATE(
                        this@ReaderActivityKotlin,
                        State.insert_driver_card
                    )
                    return
                }

            }
        }
    }
    private var textViewState: TextView? = null
    private var imageViewState: ImageView? = null
    private var progressBar: PercentageChartView? = null
    internal lateinit var loader: Loader
    private val usbAttachmentReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null) {
                val sb = StringBuilder()
                sb.append("LobolReader: usbAttachmentReceiver = ")
                sb.append(action)
                val extras = intent.extras
                var c: Char = 65535.toChar()
                val hashCode = action.hashCode()
                if (hashCode != -2114103349) {
                    if (hashCode != -1608292967) {
                        if (hashCode == -1555371901 && action == ACTION_USB_REQUEST_PERMISSION) {
                            c = 2.toChar()
                        }
                    } else if (action == "android.hardware.usb.action.USB_DEVICE_DETACHED") {
                        c = 1.toChar()
                    }
                } else if (action == "android.hardware.usb.action.USB_DEVICE_ATTACHED") {
                    c = 0.toChar()
                }
                val str = "device"
                if (c.toInt() != 0) {
                    if (c.toInt() != 1) {
                        if (c.toInt() == 2 && extras != null) {
                            val usbDevice = extras.get(str) as UsbDevice?
                            val booleanValue =
                                (extras.get(EXTRA_ACTION_USB_REQUEST_PERMISSION_ONCREATE) as Boolean)
                            if (usbDevice == null) {
                                return
                            }
                            if (!usbmanager!!.hasPermission(usbDevice)) {
                                usbdevice = null
                                usbdeviceconnection = null
                                SETSTATE(this@ReaderActivityKotlin, State.grant_refused)
                            } else if (this@ReaderActivityKotlin.openusbdevice(usbDevice)) {
                                SETSTATE(this@ReaderActivityKotlin, State.insert_driver_card)
                                write(
                                    openDevice!!,
                                    usbEndpoint,
                                    hexStringToByteArray("63000000000000000000")
                                )
                                val bArr = readPowerOFF(openDevice!!, usbEndpoint2!!)
                                val b = bArr[0]
                                if (b.toInt() == -127 || b == java.lang.Byte.MIN_VALUE || b.toInt() == -126) {
                                    val b2 = bArr[7]
                                    val b3 = (b2 and 3)
                                    if (b3.toInt() == 0) {
                                        ICCstate = iccsstatus.ICC_PRESENT_AND_ACTIVE
                                    }
                                    if (b3.toInt() == 1) {
                                        ICCstate = iccsstatus.ICC_PRESENT_AND_INACTIVE
                                    }
                                    if (b3.toInt() == 2) {
                                        ICCstate = iccsstatus.ICC_NOT_PRESENT
                                    }
                                    if (b3.toInt() == 3) {
                                        ICCstate = iccsstatus.RFU
                                    }
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        usbDevice.productName.toString(),
                                        "8"
                                    )
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        usbDevice.serialNumber.toString(),
                                        "7"
                                    )
                                }
                                if (booleanValue) {

                                    if (ICCstate == iccsstatus.ICC_PRESENT_AND_ACTIVE || ICCstate == iccsstatus.ICC_PRESENT_AND_INACTIVE) {
                                        this@ReaderActivityKotlin.sendBroadcast(
                                            Intent(
                                                SMART_CARD_PUT_IN
                                            )
                                        )
                                    }
                                    if (ICCstate == iccsstatus.ICC_NOT_PRESENT) {
                                        this@ReaderActivityKotlin.sendBroadcast(
                                            Intent(
                                                SMART_CARD_GET_OUT
                                            )
                                        )
                                    }

                                }
                            } else {
                                SETSTATE(
                                    this@ReaderActivityKotlin,
                                    State.use_ccid_card_reader
                                )
                            }
                        }
                    } else if (extras != null) {
                        val usbDevice2 = extras.get(str) as UsbDevice?
                        if (usbDevice2 != null && usbDevice2 == usbdevice) {
                            usbdevice = null
                            usbdeviceconnection = null
                            SETSTATE(this@ReaderActivityKotlin, State.connect_card_reader)
                            this@ReaderActivityKotlin.sendBroadcast(Intent(SMART_CARD_GET_OUT))
                        }
                    }
                } else if (extras != null) {
                    val usbDevice3 = extras.get(str) as UsbDevice?
                    if (usbDevice3 != null) {
                        usbdevice = null
                        usbdeviceconnection = null
                        SETSTATE(this@ReaderActivityKotlin, State.grant_usb_device)
                        this@ReaderActivityKotlin.requestpermission(usbDevice3, false)
                    }
                }
            }
        }
    }
    internal var openDevice: UsbDeviceConnection? = null
    internal var usbEndpoint: UsbEndpoint? = null
    internal var usbEndpoint2: UsbEndpoint? = null
    lateinit var keyy: String
    internal var br = ByteArray(0)
    override fun onStart() {
        super.onStart()
        if (intent.hasExtra("from")) {
            if (intent.extras!!.containsKey("from")) {
                if (intent.getStringExtra("from") == "scan") {
                    scanMode = true
                }
            }
        }
        validatePermissions()
    }

    fun setDownloadTime(j: Long): Boolean {
        return if (selectFile(1294)) {
            doUpdateBinary(j)
        } else false
    }

    private fun doUpdateBinary(j: Long): Boolean {

        val holder = Holder()
        if (!this.io(this.xferBlock(UpdateBinary(j)), holder)) {
            return false
        }
        val getbytearray = holder.GetByteArray()
        val holder2 = Holder()
        val holder3 = Holder()
        return dataBlockInfo(
            getbytearray,
            getbytearray.size,
            holder2,
            holder3,
            Holder(),
            Holder()
        )
    }

    var resultData: String = ""
    var readCardThread: Thread? = null
    var t1: Thread? = null
    var index = 1
    private var status = "ok"
    private fun start() {
        Timber.tag("TestThreads").i("start")
        reading = true
        progressBar!!.visibility = View.VISIBLE
        imageViewState!!.visibility = View.VISIBLE

        readCardThread = object : Thread() {
            override fun run() {
                try {
                    val calendarUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    val time = calendarUtc.timeInMillis / 1000
                    Timber.tag("TIME").e(time.toString())
                    setDownloadTime(time)
                    val tachoCardDownload = readFile(FILE_TACHO_Card_Download, 4, true)
                    val cardDownload = ReadCardDownload(
                        BinaryReader(
                            hexStringToByteArray(
                                tachoCardDownload.resultData.substring(
                                    20,
                                    tachoCardDownload.resultData.length - 4
                                )
                            )
                        )
                    )
                    val tachoDrivingLicenceInfo =
                        readFile(FILE_TACHO_Driving_Licence_Info, 53, true)
                    val (_) = ReadDrivingLicenceInfo(
                        BinaryReader(
                            hexStringToByteArray(
                                tachoDrivingLicenceInfo.resultData.substring(
                                    20,
                                    tachoDrivingLicenceInfo.resultData.length - 4
                                )
                            )
                        )
                    )
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Driving Licence Info : $status",
                                    "1"
                                )
                            }
                            progressBar!!.setProgress(35f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    val tachoEventsData = readFile(FILE_TACHO_Events_Data, 1728, true)
                    val (cardEventData) = ReadEventsData(
                        BinaryReader(
                            hexStringToByteArray(
                                tachoEventsData.resultData
                            )
                        ),
                        applicationIdentification.driverCardApplicationIdentification.noOfEventsPerType
                    )
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Events Data : $status",
                                    "1"
                                )

                            }
                            progressBar!!.setProgress(41f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    val tachoFaultsData = readFile(FILE_TACHO_Faults_Data, 1152, true)
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Faults Data : $status",
                                    "1"
                                )
                            }
                            progressBar!!.setProgress(45f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    val tachoDriverActivityData =
                        readFile(FILE_TACHO_Driver_Activity_Data, 13780, true)
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Driver Activity Data : $status",
                                    "1"
                                )
                            }
                            progressBar!!.setProgress(50f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    val (cardDriverActivity) = ReadDriverActivityData(
                        BinaryReader(
                            hexStringToByteArray(tachoDriverActivityData.resultData)
                        ), 13776
                    )
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Driver Activity Data : $status",
                                    "1"
                                )
                            }
                            progressBar!!.setProgress(63f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    val tachoVehiclesUsed = readFile(FILE_TACHO_Vehicles_Used, 6202, true)
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                //  log.readingCardSteps.add("TACHO Vehicles Used : $status")
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Vehicles Used : $status",
                                    "1"
                                )
                            }
                            progressBar!!.setProgress(65f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    val cardVehiclesUsed = ReadCardVehiclesUsed(
                        BinaryReader(hexStringToByteArray(tachoVehiclesUsed.resultData)),
                        applicationIdentification.driverCardApplicationIdentification.noOfCardVehicleRecords
                    )
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Vehicles Used : $status",
                                    "1"
                                )
                            }
                            progressBar!!.setProgress(72f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    val tachoPlaces = readFile(FILE_TACHO_Places, 1121, true)
                    val tachoCurrentUsage = readFile(FILE_TACHO_Current_Usage, 19, true)
                    val (cardCurrentUse) = ReadCurrentUsage(
                        BinaryReader(
                            hexStringToByteArray(
                                tachoCurrentUsage.resultData.substring(
                                    20,
                                    tachoCurrentUsage.resultData.length - 4
                                )
                            )
                        )
                    )
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {

                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Current Usage : $status",
                                    "1"
                                )
                            }
                            progressBar!!.setProgress(79f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    val tachoControlActivityData =
                        readFile(FILE_TACHO_Control_Activity_Data, 46, true)
                    val (cardControlActivityDataRecord) = ReadControlActivityData(
                        BinaryReader(
                            hexStringToByteArray(
                                tachoControlActivityData.resultData.substring(
                                    20,
                                    tachoControlActivityData.resultData.length - 4
                                )
                            )
                        )
                    )
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Control Activity Data : $status",
                                    "1"
                                )
                            }
                            progressBar!!.setProgress(85f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    val tachoSpecificConditions =
                        readFile(FILE_TACHO_Specific_Conditions, 280, true)
                    runOnUiThread {
                        try {
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Specific Conditions : $status",
                                    "1"
                                )
                            }
                            progressBar!!.setProgress(93f, false)
                        } catch (ex: Exception) {

                        }
                    }

                    resultData = resultData +
                            tachoCardDownload.toString(4) +
                            tachoDrivingLicenceInfo.toString(53) +
                            tachoEventsData.toString(1728) +
                            tachoFaultsData.toString(1152) +
                            tachoDriverActivityData.toString(13780) +
                            tachoVehiclesUsed.toString(6202) +
                            tachoPlaces.toString(1121) +
                            tachoCurrentUsage.toString(19) +
                            tachoControlActivityData.toString(46) +
                            tachoSpecificConditions.toString(280)

                    if (changeDirectoryTACHOG2()) {
                        val icc = readFile(FILE_ICC, 25, false, "SMRDT")
                        val ic = readFile(FILE_IC, 8, false, "SMRDT")
                        val tachoG2ApplicationIdentifier =
                            readFile(FILE_TACHO_Application_Identifier, 17, true, "SMRDT")
                        val test = hexStringToByteArray(
                            tachoG2ApplicationIdentifier.resultData.substring(
                                20,
                                tachoG2ApplicationIdentifier.resultData.length - 4
                            )
                        )
                        val applicationIdentifierG2 =
                            DataParsingG2.ReadApplicationIdentification(BinaryReader(test))

                        val tachoG2CardCertificate =
                            readFile(FILE_TACHOG2_CardMA_Certificate, 205, false, "SMRDT")
                        val tachoG2CardSignCertificate =
                            readFile(FILE_TACHOG2_CardSignCertificate, 205, false, "SMRDT")
                        val tachoG2CACertificate =
                            readFile(FILE_TACHO_CA_Certificate, 205, false, "SMRDT")
                        val tachoG2LinkCertificat =
                            readFile(FILE_TACHOG2_Link_Certificat, 204, false, "SMRDT")

                        val tachoG2Identifier =
                            readFile(FILE_TACHO_Identification, 143, true, "SMRDT")
                        runOnUiThread {
                            try {
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "TACHOG2 Identifier : $status",
                                        "1"
                                    )
                                }
                                progressBar!!.setProgress(95f, false)
                            } catch (ex: Exception) {

                            }

                        }
                        val tachoG2CardDownload =
                            readFile(FILE_TACHO_Card_Download, 4, true, "SMRDT")
                        val tachoG2DrivingLicenceInfo =
                            readFile(FILE_TACHO_Driving_Licence_Info, 53, true, "SMRDT")
                        val tachoG2EventsData =
                            readFile(FILE_TACHO_Events_Data, 3168, true, "SMRDT")
                        val tachoG2FaultsData =
                            readFile(FILE_TACHO_Faults_Data, 1152, true, "SMRDT")

                        val tachoG2DriverActivityData =
                            readFile(FILE_TACHO_Driver_Activity_Data, 13780, true, "SMRDT")
                        runOnUiThread {
                            try {
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    //  log.readingCardSteps.add("TACHOG2 Driver Activity Data : $status")
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "TACHOG2 Driver Activity Data : $status",
                                        "1"
                                    )
                                }
                                progressBar!!.setProgress(96f, false)
                            } catch (ex: Exception) {

                            }
                        }

                        val tachoG2VehiclesUsed =
                            readFile(FILE_TACHO_Vehicles_Used, 9602, true, "SMRDT")
                        runOnUiThread {
                            try {
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    //  log.readingCardSteps.add("TACHOG2 Vehicles Used : $status")
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "TACHOG2 Vehicles Used : $status",
                                        "1"
                                    )
                                }
                                progressBar!!.setProgress(97f, false)
                            } catch (ex: Exception) {

                            }
                        }

                        val tachoG2SpecificConditions =
                            readFile(FILE_TACHO_Specific_Conditions, 562, true, "SMRDT")
                        val tachoG2VehicleUnitsUsed =
                            readFile(FILE_TACHO_VehiclesUNITIS_Used, 2002, true, "SMRDT")
                        runOnUiThread {
                            try {
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "TACHOG2 Vehicle Units Used : $status",
                                        "1"
                                    )
                                }
                                progressBar!!.setProgress(98f, false)
                            } catch (ex: Exception) {

                            }
                        }

                        val tachoG2PLACES = readFile(FILE_TACHO_Places, 2354, true, "SMRDT")
                        runOnUiThread {
                            try {
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "TACHOG2 PLACES : $status",
                                        "1"
                                    )
                                }
                                progressBar!!.setProgress(99f, false)
                            } catch (ex: Exception) {

                            }
                        }
                        val tachoG2CurrentUsage =
                            readFile(FILE_TACHO_Current_Usage, 19, true, "SMRDT")

                        val tachoGNSSPLACES =
                            readFile(FILE_TACHOG2_Gnss_Places, 6050, true, "SMRDT")
                        var gnssPLACES = DataParsingG2.ReadGnssPlaces(
                            BinaryReader(
                                hexStringToByteArrayy(tachoGNSSPLACES.resultData)
                            ),
                            applicationIdentifierG2.driverCardApplicationIdentification!!.noOfGNSSCDRecords
                        )
                        val tachoG2ControlActivityData =
                            readFile(FILE_TACHO_Control_Activity_Data, 46, true, "SMRDT")

                        resultData = resultData +
                                icc.toStringDataG2(17) +
                                ic.toStringDataG2(8) +
                                tachoG2CardCertificate.toStringDataG2(205) +
                                tachoG2CardSignCertificate.toStringDataG2(205) +
                                tachoG2CACertificate.toStringDataG2(205) +
                                tachoG2LinkCertificat.toStringDataG2(204) +
                                tachoG2CardDownload.toString(4, 3) +
                                tachoG2DrivingLicenceInfo.toString(53, 3) +
                                tachoG2EventsData.toString(3168, 3) +
                                tachoG2FaultsData.toString(1152, 3) +
                                tachoG2ApplicationIdentifier.toString(17, 3) +
                                tachoG2Identifier.toString(143, 3) +
                                tachoG2PLACES.toString(2354, 3) +
                                tachoGNSSPLACES.toString(6050, 3) +
                                tachoG2DriverActivityData.toString(13780, 3) +
                                tachoG2VehiclesUsed.toString(9602, 3) +
                                tachoG2SpecificConditions.toString(562, 3) +
                                tachoG2VehicleUnitsUsed.toString(2002, 3) +
                                tachoG2CurrentUsage.toString(19, 3) +
                                tachoG2ControlActivityData.toString(46, 3)

                    }

                    powerOff()
                    runOnUiThread {
                        try {
                            progressBar!!.setProgress(97f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    runOnUiThread {

                        if (isNewCard) {

                            createUpdateFile(
                                "F_" + identification!!.cardIdentification.cardNumber!!.driverIdentification + SimpleDateFormat(
                                    "yyMMddHHmm"
                                ).format(Date()) + "." + storage.getFileType(), resultData
                            )
                            storage.saveUserName(
                                identification!!.driverCardHolderIdentification.cardHolderName.holderSurname + ";" + identification!!.driverCardHolderIdentification.cardHolderName.holderFirstName,
                                identification!!.cardIdentification.cardNumber!!.driverIdentification
                            )
                            storage.saveCardDate(identification!!.driverCardHolderIdentification.cardHolderBirthDate)
                            val emailUser = storage.getUser().email
                         /*   val documentArray: RealmResults<LectureHistory> =
                                RealmManager.loadHistoryByUser(emailUser)*/
                            val lectureHistory = LectureHistory(
                                file.path,
                                identification!!.cardIdentification.cardNumber!!.driverIdentification,
                                Date().toString(),
                                emailUser
                            )
                            val auth0: Auth0 = Auth0(
                                BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN
                            )
                            var authenticationAPIClient = AuthenticationAPIClient(auth0)
                            RealmManager.clearCard()
                            RealmManager.saveHistory(lectureHistory)
                            cardDriverActivity.CardId =
                                identification!!.cardIdentification.cardNumber!!.driverIdentification
                            RealmManager.saveCardDriverActivity(cardDriverActivity)
                            RealmManager.saveCardIdentification(identification!!.cardIdentification)
                            RealmManager.saveCardVehiclesUsed(cardVehiclesUsed)
                            RealmManager.saveCardDownload(cardDownload)

                            try {
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    // for log
                                    /*  log.endDate = Date().toString()
                                       log.messages.add("Lecture Terminée") */
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        Date().toString(),
                                        "2"
                                    )
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "Lecture Terminée",
                                        "3"
                                    )
                                    //
                                }
                                progressBar!!.setProgress(100f, false)
                                lifecycleScope.launch {
                                    delay(1000)
                                    progressBar!!.visibility = View.GONE
                                }
                                imageViewState!!.setImageResource(R.drawable.lecture_terminer)
                                textViewState!!.text = getString(R.string.lecture_termine)
                            } catch (ex: Exception) {

                            }
                            Toast.makeText(
                                this@ReaderActivityKotlin,
                                getString(R.string.lecture_termine),
                                Toast.LENGTH_SHORT
                            ).show()
                            Snackbar.make(
                                textViewState!!.rootView,
                                getString(R.string.lecture_termine),
                                Snackbar.LENGTH_SHORT
                            ).show()

                            //envoi fichier
                            api(lectureHistory)
                            startActivity(
                                Intent(
                                    this@ReaderActivityKotlin,
                                    MainActivity::class.java
                                ).apply { action = OPEN_ACCUEIL }
                            )
                            finish()

                        } else {
                            createUpdateFile(
                                "F_" + identification!!.cardIdentification.cardNumber!!.driverIdentification + SimpleDateFormat(
                                    "yyMMddHHmm", Locale.getDefault()
                                ).format(Date()) + "." + storage.getFileType(), resultData
                            )
                            storage.setScannedFirstTime(true)
                            storage.saveUserName(
                                identification!!.driverCardHolderIdentification.cardHolderName.holderSurname + ";" + identification!!.driverCardHolderIdentification.cardHolderName.holderFirstName,
                                identification!!.cardIdentification.cardNumber!!.driverIdentification
                            )
                            storage.saveCardDate(identification!!.driverCardHolderIdentification.cardHolderBirthDate)
                            RealmManager.clearActivitiesAndVehicule()
                            val emailUser = storage.getUser().email
                         /*   val documentArray: RealmResults<LectureHistory> =
                                RealmManager.loadHistoryByUser(emailUser)*/
                            val lectureHistory = LectureHistory(
                                file.path,
                                identification!!.cardIdentification.cardNumber!!.driverIdentification,
                                Date().toString(),
                                emailUser
                            )
                            val auth0: Auth0 = Auth0(
                                BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN
                            )
                            AuthenticationAPIClient(auth0)

                            RealmManager.saveHistory(lectureHistory)
                            cardDriverActivity.CardId =
                                identification!!.cardIdentification.cardNumber!!.driverIdentification
                            RealmManager.saveCardDriverActivity(cardDriverActivity)
                            RealmManager.saveCardIdentification(identification!!.cardIdentification)
                            RealmManager.saveCardVehiclesUsed(cardVehiclesUsed)
                            RealmManager.saveCardDownload(cardDownload)
                            try {
                                if (SharedPreferencesUtils.isLoggingEnabled) {
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        Date().toString(),
                                        "2"
                                    )
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        "Lecture Terminée",
                                        "3"
                                    )
                                    //
                                }
                                progressBar!!.setProgress(100f, false)
                                lifecycleScope.launch {
                                    delay(1000)
                                    progressBar!!.visibility = View.GONE
                                }
                                imageViewState!!.setImageResource(R.drawable.lecture_terminer)
                                textViewState!!.text = getString(R.string.lecture_termine)
                            } catch (ex: Exception) {

                            }
                            val s = Snackbar.make(
                                textViewState!!.rootView,
                                "Lecture terminé",
                                Snackbar.LENGTH_SHORT
                            )
                            s.show()
                            //envoi fichier
                            api(lectureHistory)
                        }

                    }
                } catch (e: Exception) {

                    runOnUiThread {
                        try {
                            progressBar!!.visibility = View.INVISIBLE
                            imageViewState!!.visibility = View.VISIBLE
                            SETSTATE(
                                this@ReaderActivityKotlin,
                                ReaderActivityKotlin.State.use_driver_card
                            )
                        } catch (ex: Exception) {

                        }
                    }
                }

            }
        }

        t1 = object : Thread() {
            override fun run() {
                try {

                    var icc = getEmptyICCDataCard()
                    try {
                        icc = readFile(FILE_ICC, 25, false)
                        val card = ReadCardIccIdentification(
                            BinaryReader(
                                HexStringToByteArray(
                                    icc.resultData.substring(
                                        20,
                                        icc.resultData.length - 4
                                    )
                                )
                            )
                        )
                        status = "ok"
                    } catch (ex: Exception) {
                        icc = getEmptyICCDataCard()
                        if (ex.message!! == "NO CARD DETECTED" || ex.message!! == "holder3.GetString() must not be null") {
                            throw Exception()
                        }
                        status = "empty"
                    }
                    runOnUiThread {
                        try {
                            loader.dismiss()
                            progressBar!!.setProgress(2f, false)
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    Date().toString(),
                                    "4"
                                )
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "ICC : $status",
                                    "1"
                                )
                                //
                            }
                        } catch (ex: Exception) {

                        }
                    }
                    // init ic
                    var ic = getEmptyICDataCard()
                    try {
                        ic = readFile(FILE_IC, 8, false)
                        val ic = ReadIc(
                            BinaryReader(
                                HexStringToByteArray(
                                    ic.resultData.substring(
                                        20,
                                        ic.resultData.length - 4
                                    )
                                )
                            )
                        )
                        status = "ok"
                    } catch (ex: Exception) {
                        ic = getEmptyICDataCard()
                        if (ex.message!! == "NO CARD DETECTED" || ex.message!! == "holder3.GetString() must not be null") {
                            throw Exception()
                        }
                        status = "empty"
                    }
                    runOnUiThread {
                        try {
                            progressBar!!.setProgress(6f, false)
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "IC : $status",
                                    "1"
                                )                                //
                            }
                        } catch (ex: Exception) {

                        }
                    }
                    changeDirectoryTACHO()
                    runOnUiThread {
                        try {
                            progressBar!!.setProgress(10f, false)
                        } catch (ex: Exception) {

                        }
                    }
                    var TACHOCardCertificate = getEmptyTachographCardCertificate()
                    try {
                        TACHOCardCertificate = readFile(FILE_TACHO_Card_Certificate, 194, false)
                        status = "ok"
                    } catch (ex: Exception) {
                        TACHOCardCertificate = getEmptyTachographCardCertificate()
                        if (ex.message!! == "NO CARD DETECTED" || ex.message!! == "holder3.GetString() must not be null") {
                            throw Exception()
                        }
                        status = "empty"
                    }
                    var tachoCACertificate = getEmptyTachographCaCertificate()
                    try {
                        tachoCACertificate = readFile(FILE_TACHO_CA_Certificate, 194, false)
                        status = "ok"
                    } catch (ex: Exception) {
                        tachoCACertificate = getEmptyTachographCaCertificate()
                        if (ex.message!! == "NO CARD DETECTED" || ex.message!! == "holder3.GetString() must not be null") {
                            throw Exception()
                        }
                        status = "empty"
                    }
                    runOnUiThread {
                        try {
                            progressBar!!.setProgress(15f, false)
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Card Certificate : $status",
                                    "1"
                                )                             //
                            }
                        } catch (ex: Exception) {

                        }
                    }
                    var tachoApplicationIdentifier = getEmptyTachographApplicationIdentification()
                    try {
                        tachoApplicationIdentifier =
                            readFile(FILE_TACHO_Application_Identifier, 10, true)
                        status = "ok"
                    } catch (ex: Exception) {
                        tachoApplicationIdentifier = getEmptyTachographApplicationIdentification()
                        if (ex.message!! == "NO CARD DETECTED" || ex.message!! == "holder3.GetString() must not be null") {
                            throw Exception()
                        }
                        status = "empty"
                    }
                    //
                    val hexIden = tachoApplicationIdentifier.resultData.substring(
                        20,
                        tachoApplicationIdentifier.resultData.length - 4
                    )
                    val byteIden = hexStringToByteArrayy(hexIden)
                    applicationIdentification =
                        ReadApplicationIdentification(BinaryReader(byteIden))
                    runOnUiThread {
                        try {
                            progressBar!!.setProgress(23f, false)
                            if (SharedPreferencesUtils.isLoggingEnabled) {
                                optimizeFireBase(
                                    read(),
                                    uniqueId,
                                    "TACHO Application Identifier : $status",
                                    "1"
                                )                                //
                            }
                        } catch (ex: Exception) {

                        }
                    }
                    var tachoidentification = getEmptyTachographIdentification()
                    try {
                        tachoidentification = readFile(FILE_TACHO_Identification, 143, true)
                        status = "ok"
                    } catch (ex: Exception) {
                        tachoidentification = getEmptyTachographIdentification()
                        if (ex.message!! == "NO CARD DETECTED" || ex.message!! == "holder3.GetString() must not be null") {
                            throw Exception()
                        }
                        status = "empty"
                    }
                    identification = ReadIdentification(
                        BinaryReader(
                            HexStringToByteArray(
                                tachoidentification.resultData.substring(
                                    20,
                                    tachoidentification.resultData.length - 4
                                )
                            )
                        )
                    )
                    // correcte identification
                    try {
                        var identificationdata = tachoidentification.resultData.substring(
                            20,
                            tachoidentification.resultData.length - 4
                        )
                        identificationdata =
                            identificationdata.substring(identificationdata.length - 12)
                        identification!!.driverCardHolderIdentification.cardHolderBirthDate =
                            identificationdata.substring(
                                6,
                                8
                            ) + "/" + identificationdata.substring(
                                4,
                                6
                            ) + "/" + identificationdata.substring(0, 4)
                        if (SharedPreferencesUtils.isLoggingEnabled) {
                            optimizeFireBase(
                                read(),
                                uniqueId,
                                "TACHO Identification : $status",
                                "1"
                            )                            //
                        }
                    } catch (ex: java.lang.Exception) {

                    }
                    runOnUiThread {
                        resultData = icc.toStringData(25) +
                                ic.toStringData(8) +
                                TACHOCardCertificate.toStringData(194) +
                                tachoCACertificate.toStringData(194) +
                                tachoApplicationIdentifier.toString(10) +
                                tachoidentification.toString(143)

                        try {
                            progressBar!!.setProgress(29f, false)
                        } catch (ex: Exception) {

                        }

                        if (SharedPreferencesUtils.isSCANNEDInFirstTime && storage.getUser().cardNumber != identification!!.cardIdentification.cardNumber!!.driverIdentification) {
                            val datePickerView = Dialog(this@ReaderActivityKotlin)
                            datePickerView.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            datePickerView.setCancelable(false)
                            datePickerView.setContentView(R.layout.dialog_detect_new)
                            datePickerView.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            datePickerView.findViewById<View>(R.id.btnDone).setOnClickListener {
                                if (!(readCardThread as Thread).isAlive) {

                                    isNewCard = true
                                    (readCardThread as Thread).start()
                                }
                                datePickerView.dismiss()
                            }

                            datePickerView.findViewById<View>(R.id.btnCancel).setOnClickListener {
                                finish()
                                datePickerView.dismiss()
                            }
                            if (index == 1) { // for fixe bug
                                index = 2
                                datePickerView.show()
                            }
                        } else {
                            // same carte or first scan
                            if (!(readCardThread as Thread).isAlive) {
                                (readCardThread as Thread).start()
                            }
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        try {
                            progressBar!!.visibility = View.INVISIBLE
                            imageViewState!!.visibility = View.VISIBLE
                            SETSTATE(
                                this@ReaderActivityKotlin,
                                ReaderActivityKotlin.State.use_driver_card
                            )
                        } catch (ex: Exception) {

                        }

                    }

                }

            }
        }
        (t1 as Thread).start()
    }

    private fun convertHex4(n: Int): String {
        return String.format("%04X", n)
    }

    lateinit var file: File
    private fun createUpdateFile(filenameExternal: String, cashback: String) {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED != state) {
            return
        }
        file = File(filesDir.parent + "/files/", filenameExternal)
        var outputStream: FileOutputStream? = null
        try {
            file.createNewFile()
            outputStream = FileOutputStream(file, true)
            val bytes = org.apache.commons.codec.binary.Hex.decodeHex(
                cashback.toLowerCase(Locale.ROOT)
                    .toCharArray()
            )
            outputStream.write(bytes)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun xferBlock(bArr: ByteArray): ByteArray {
        val bArr2 = ByteArray(bArr.size + 10)
        bArr2[0] = PC_to_RDR_XfrBlock
        bArr2[1] = ToByte(bArr.size and 255)
        bArr2[2] = ToByte((bArr.size and MotionEventCompat.ACTION_POINTER_INDEX_MASK).ushr(8))
        bArr2[3] = ToByte((bArr.size and 16711680).ushr(16))
        bArr2[4] = ToByte((bArr.size and ViewCompat.MEASURED_STATE_MASK).ushr(24))
        bArr2[5] = 0
        bArr2[6] = sequence_number
        bArr2[7] = -1
        bArr2[8] = 0
        bArr2[9] = 0
        System.arraycopy(bArr, 0, bArr2, 10, bArr.size)
        IncreaseSequenceNumber()
        return bArr2
    }

    private fun toHexString(i: Int): String {
        return String.format("%08X", Integer.valueOf(i))
    }

    private fun selectFile(i: Int): Boolean {
        val sb = StringBuilder()
        sb.append("SelectFile=")
        sb.append(toHexString(i))
        Timber.tag("CardReaderTest").i(sb.toString())
        val holder = Holder()
        return this.io(this.xferBlock(Selectfile(Integer.valueOf(i))), holder)
    }

    @Throws(Exception::class)
    fun readFile(
        fileId: Int,
        expectedLength: Int,
        computeSignature: Boolean,
        CardPath: String = "Tacho"
    ): DataCard {

        val holder = Holder()
        val holder2 = Holder()
        val holder3 = Holder()

        val resSelect: Boolean = selectFile(fileId)
        val resRead: Boolean = readFile(expectedLength, holder, holder2)
        if (!resRead || !resSelect) {
            if (CardPath == "SMRDT")
                holder.Put(ByteArray(0))
            else
                throw Exception("NO CARD DETECTED")
        }

        if (computeSignature) {

            var ok = performHashOfFile()
            if (ok) {
                computeDigitalSignature(holder3, CardPath)
            } else {
                do {
                    ok = performHashOfFile()
                    if (ok) {
                        computeDigitalSignature(holder3, CardPath)
                    }
                } while (!ok)
            }

        }

        br = ByteArray(0)

        return if (expectedLength > 255)
            if (computeSignature)
                DataCard(
                    convertHex4(fileId),
                    convertHex4(expectedLength),
                    holder2.GetString(),
                    holder3.GetString(),
                    CardPath
                )
            else
                DataCard(
                    convertHex4(fileId),
                    convertHex4(expectedLength),
                    holder2.GetString(),
                    "",
                    CardPath
                )
        else {
            if (computeSignature)
                DataCard(
                    convertHex4(fileId),
                    convertHex4(expectedLength),
                    toHexString(holder.GetByteArray(), 0, holder.GetByteArray().size),
                    holder3.GetString(),
                    CardPath
                )
            else
                DataCard(
                    convertHex4(fileId),
                    convertHex4(expectedLength),
                    toHexString(holder.GetByteArray(), 0, holder.GetByteArray().size),
                    "",
                    CardPath
                )

        }


    }

    private fun validatePermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (!report!!.areAllPermissionsGranted()) {
                        validatePermissions()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {

                }
            })
            .check()
    }

    private fun performHashOfFile(): Boolean {
        Timber.tag("CardReaderTest").i("PerformHashOfFile")

        val holder = Holder()
        if (this.io(xferBlock(PerformHashOfFileByte(0)), holder)) {
            val getbytearray = holder.GetByteArray()
            val holder2 = Holder()
            val holder3 = Holder()
            val holder4 = Holder()
            val holder5 = Holder()
            if (dataBlockInfo(
                    getbytearray,
                    getbytearray.size,
                    holder2,
                    holder3,
                    holder4,
                    holder5
                )
            ) {
                val holder6 = Holder()
                val holder7 = Holder()
                val holder8 = Holder()
                if (info(holder5.GetByteArray(), holder6, holder7, holder8)) {
                    val getbyte = holder7.GetByte()
                    val getbyte2 = holder8.GetByte()
                    if (getbyte.toInt() == -112 && getbyte2.toInt() == 0) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun computeDigitalSignature(holder: Holder, cardPath: String): Boolean {
        Timber.tag("CardReaderTest").i("ComputeDigitalSignature")
        var b = Byte.MIN_VALUE
        if (cardPath == "SMRDT") {
            b = 64
        }
        val holder2 = Holder()
        if (this.io(this.xferBlock(Convert.ComputeDigitalSignature(b)), holder2)) {
            val getbytearray = holder2.GetByteArray()
            val holder3 = Holder()
            val holder4 = Holder()
            val holder5 = Holder()
            val holder6 = Holder()
            if (dataBlockInfo(
                    getbytearray,
                    getbytearray.size,
                    holder3,
                    holder4,
                    holder5,
                    holder6
                )
            ) {
                val holder7 = Holder()
                val holder8 = Holder()
                if (info(holder6.GetByteArray(), holder, holder7, holder8)) {
                    val getbyte = holder7.GetByte()
                    val getbyte2 = holder8.GetByte()
                    if (getbyte.toInt() == -112 && getbyte2.toInt() == 0) {
                        holder.Put(
                            toHexString(getbytearray).substring(
                                20,
                                toHexString(getbytearray).length - 4
                            )
                        )
                        return true
                    }
                }
            }
        }
        return false
    }

    //function api
    fun api(document: LectureHistory) {
        try {
            loader.show(this@ReaderActivityKotlin)
            // ici si l'extention de fichier est changer donc il faut changer l'extention dans le file provider et il faut changer le lien dans la base de donne
            val actualFile = File(document.file) // log contenue file
            if (actualFile.extension != storage.getFileType()) // ici il faut renommer le fichier
            {
                val newFile = File(
                    actualFile.path.replace(
                        actualFile.extension,
                        storage.getFileType()!!
                    )
                )
                val renamed = actualFile.renameTo(newFile)
                if (renamed) {
                    RealmManager.updateLectureHistory(
                        document.time,
                        document.file.replace(
                            actualFile.extension,
                            storage.getFileType()!!
                        )
                    )
                }
            }
            var strFile = toHexString(actualFile.inputStream().readBytes())
            var hashStrFile = toHexString(Hash256.SHA256.checksum(actualFile))
            Timber.tag("ServiceGenerator").d(hashStrFile)
            val items = arrayListOf<String>()
            val itemsHash = arrayListOf<String>()
            for (i in 0..(strFile.length - 2)) {
                if (i % 2 == 0) {
                    items.add(strFile.substring(i, i + 2))
                }
            }
            for (j in 0..(hashStrFile.length - 2)) {
                if (j % 2 == 0) {
                    itemsHash.add(hashStrFile.substring(j, j + 2))
                }
            }
            strFile = TextUtils.join("-", items)
            hashStrFile = TextUtils.join("-", itemsHash)
            var cardNumber = document.cardNumber
            val nameFile = actualFile.name.toString()
                .replace(actualFile.extension, storage.getFileType().toString())
            val auth0 = Auth0(
                BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN
            )
            val authenticationAPIClient = AuthenticationAPIClient(auth0)
            val manager =
                CredentialsManager(authenticationAPIClient, SharedPreferencesStorage(this))
            manager.getCredentials(object :
                BaseCallback<Credentials, CredentialsManagerException> {
                override fun onSuccess(payload: Credentials?) {
                    launch(Dispatchers.Main) {
                        val requestFile: RequestBody =
                            create("multipart/form-data".toMediaTypeOrNull(), actualFile)
                        val body: MultipartBody.Part =
                            createFormData("file", actualFile.name, requestFile)

                      val req = apiNew.uploadTachofile("Bearer " + payload!!.idToken,body)
                          if (req.code() ==200)
                          {

                              optimizeFireBase(
                                  read(),
                                  uniqueId,
                                  HistoryReponceSendFile(
                                      "le_fichier_est_envoye_avec_succes"
                                  ).message,
                                  "13"
                              )
                              Timber.tag("FichierC1Benvoyer")
                                  .e("respo 200")

                          }
                        else {
                              optimizeFireBase(
                                  read(),
                                  uniqueId,
                                  HistoryReponceSendFile("Problem inconnue").message,
                                  "13"
                              )
                              Timber.tag("FichierC1BcontainError")
                                  .e("respo unknown")
                        }

                        delay(2000)
                        finish()
                       /* Timber.tag("onAuthentication").i(payload!!.expiresAt.toString())
                        Timber.tag("onAuthentication").i(payload.idToken.toString())
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("checksum", hashStrFile)
                        jsonObject.addProperty("data", strFile)
                        jsonObject.addProperty("fileName", nameFile)
                        api.fileSending(jsonObject, "Bearer " + payload.idToken)
                            .enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(
                                    call: Call<ResponseBody>?,
                                    response: Response<ResponseBody>?
                                ) {
                                    loader.dismiss()
                                    val code = response!!.code()
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        code.toString(),
                                        "5"
                                    )
                                    when (code) {
                                        200 -> {
                                            optimizeFireBase(
                                                read(),
                                                uniqueId,
                                                HistoryReponceSendFile(
                                                    "le_fichier_est_envoye_avec_succes"
                                                ).message,
                                                "13"
                                            )
                                            Timber.tag("FichierC1Benvoyer")
                                                .d("respo 200")
                                        }
                                        428 -> {
                                            optimizeFireBase(
                                                read(),
                                                uniqueId,
                                                HistoryReponceSendFile(
                                                    this@ReaderActivityKotlin.getString(R.string.Precondition_Required_no_associated_driver)
                                                ).message,
                                                "13"
                                            )
                                            Timber.tag("FichierC1Bnonenvoye").d("respo 428")
                                        }
                                        else -> {
                                            optimizeFireBase(
                                                read(),
                                                uniqueId,
                                                HistoryReponceSendFile("Problem inconnue").message,
                                                "13"
                                            )
                                            Timber.tag("FichierC1BcontainError")
                                                .d("respo unknown")
                                        }
                                    }
                                    Handler().postDelayed({
                                        finish()
                                    }, 2000)
                                }
                                override fun onFailure(
                                    call: Call<ResponseBody>?,
                                    t: Throwable?
                                ) {
                                    loader.dismiss()
                                    optimizeFireBase(
                                        read(),
                                        uniqueId,
                                        HistoryReponceSendFile(t.toString()).message,
                                        "13"
                                    )
                                    Timber.tag("echec envoie ").d("failed")
                                    Handler().postDelayed({
                                        finish()
                                    }, 2000)
                                }
                            })*/
                    }
                }

                override fun onFailure(error: CredentialsManagerException?) {
                    Timber.tag("OnFailure ").d("failed")
                    loader.dismiss()
                    optimizeFireBase(
                        read(),
                        uniqueId,
                        HistoryReponceSendFile(error!!.message!!).message,
                        "13"
                    )
                }
            })
        } catch (ex: Exception) {
            Timber.tag("exceptionFileC1B").e(ex)
        }
    }

    private fun readFile(i: Int, holder: Holder, holder2: Holder): Boolean {
        val bArr = ByteArray(i)
        var i5 = 0
        var hex = ""

        do {
            val i6 = i - i5
            val b = if (i6 < 254) i6.toByte() else -2

            if (!this.io(this.xferBlock(readFile(Integer.valueOf(i5), b)), holder)) {
                return false
            }
            val getbytearray = holder.GetByteArray()
            if (i > 255) {
                hex += toHexString(getbytearray).substring(20, toHexString(getbytearray).length - 4)
                holder2.Put(hex)
            }

            val holder3 = Holder()
            val holder4 = Holder()
            val holder5 = Holder()
            val holder6 = Holder()

            if (dataBlockInfo(
                    getbytearray,
                    getbytearray.size,
                    holder3,
                    holder4,
                    holder5,
                    holder6
                )
            ) {
                val holder7 = Holder()
                val holder8 = Holder()
                val holder9 = Holder()
                if (info(holder6.GetByteArray(), holder7, holder8, holder9)) {
                    val getbyte = holder8.GetByte()
                    val getbyte2 = holder9.GetByte()
                    val getbytearray2 = holder7.GetByteArray()
                    val length = getbytearray2.size
                    return if (getbyte.toInt() != -112) {
                        if (getbyte.toInt() == 97 || getbyte.toInt() != 103) {
                        }
                        false
                    } else if (getbyte2.toInt() != 0) {
                        continue
                    } else if (length <= 0) {
                        false
                    } else {
                        System.arraycopy(getbytearray2, 0, bArr, i5, length)
                        i5 += length
                        continue
                    }
                } else {
                    continue
                }
            }
        } while (i5 != i)



        return true
    }

    private fun dataBlockInfo(
        bArr: ByteArray,
        i: Int,
        holder: Holder,
        holder2: Holder,
        holder3: Holder,
        holder4: Holder
    ): Boolean {
        val holder5 = Holder()
        val holder6 = Holder()
        val holder7 = Holder()
        if (!info(
                bArr,
                i,
                holder5,
                holder,
                holder2,
                holder6,
                holder7
            ) || holder5.GetByte() != java.lang.Byte.MIN_VALUE
        ) {
            holder4.Put(ByteArray(0))
            return false
        }
        holder4.Put(bArr.copyOfRange(10, holder7.GetInt() + 10))
        val getbyte = holder6.GetByte()
        return if (getbyte >= 1 && getbyte <= java.lang.Byte.MAX_VALUE || getbyte.toInt() == 0) {
            true
        } else true

    }

    private fun info(bArr: ByteArray, holder: Holder, holder2: Holder, holder3: Holder): Boolean {
        val length = bArr.size
        if (length < 2) {
            return false
        }
        val i = length - 2
        holder2.Put(bArr[i])
        holder3.Put(bArr[length - 1])
        holder.Put(bArr.copyOfRange(0, i))
        return true
    }

    private fun info(
        bArr: ByteArray,
        i: Int,
        holder: Holder,
        holder2: Holder,
        holder3: Holder,
        holder4: Holder,
        holder5: Holder
    ): Boolean {
        if (i >= 10) {
            val b = bArr[0]
            holder.Put(b)
            if (b.toInt() == -127 || b == java.lang.Byte.MIN_VALUE || b.toInt() == -126) {
                holder5.Put(toInt(bArr, 1))
                val b2 = bArr[7]
                val b3 = (b2 and 3).toByte()
                holder4.Put(bArr[8])
                return true
            }
        }
        return false
    }

    private fun toInt(bArr: ByteArray, i: Int): Int {
        return toInt(bArr[i + 3]) shl 24 or toInt(bArr[i]) or (toInt(bArr[i + 1]) shl 8) or (toInt(
            bArr[i + 2]
        ) shl 16)
    }

    fun toInt(b: Byte): Int {
        return b.toInt() and 255
    }

    private fun changeDirectoryTACHO(): Boolean {
        val sb = StringBuilder()
        sb.append("CardReader: ChangeDirectory = ")
        val str = "TACHO"
        sb.append(str)
        Timber.tag("CardReaderTest").i(sb.toString())

        val holder = Holder()
        if (this.io(this.xferBlock(ChangeDirectoryd(str)), holder)) {
            val getbytearray = holder.GetByteArray()
            val holder2 = Holder()
            val holder3 = Holder()
            val holder4 = Holder()
            val holder5 = Holder()
            if (dataBlockInfo(
                    getbytearray,
                    getbytearray.size,
                    holder2,
                    holder3,
                    holder4,
                    holder5
                )
            ) {
                val holder6 = Holder()
                val holder7 = Holder()
                val holder8 = Holder()
                if (info(holder5.GetByteArray(), holder6, holder7, holder8)) {
                    val getbyte = holder7.GetByte()
                    val getbyte2 = holder8.GetByte()
                    if (getbyte.toInt() == -112 && getbyte2.toInt() == 0 || getbyte.toInt() == 106) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun changeDirectoryTACHOG2(): Boolean {
        val sb = StringBuilder()
        sb.append("CardReader: ChangeDirectory = ")
        val str = "SMRDT"
        sb.append(str)
        Timber.tag("CardReaderTest").i(sb.toString())

        val holder = Holder()
        if (this.io(this.xferBlock(ChangeDirectoryd(str)), holder)) {
            val getbytearray = holder.GetByteArray()
            val holder2 = Holder()
            val holder3 = Holder()
            val holder4 = Holder()
            val holder5 = Holder()
            if (dataBlockInfo(
                    getbytearray,
                    getbytearray.size,
                    holder2,
                    holder3,
                    holder4,
                    holder5
                )
            ) {
                val holder6 = Holder()
                val holder7 = Holder()
                val holder8 = Holder()
                if (info(holder5.GetByteArray(), holder6, holder7, holder8)) {
                    val getbyte = holder7.GetByte()
                    val getbyte2 = holder8.GetByte()
                    if (getbyte.toInt() == -112 && getbyte2.toInt() == 0 || getbyte.toInt() == 106) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun powerOff(): Boolean {
        Timber.tag("CardReader:").w(" PowerOff")
        val holder = Holder()
        return this.io(this.iccPowerOff(), holder)
    }

    private fun iccPowerOff(): ByteArray {
        val bArr = byteArrayOf(PC_to_RDR_IccPowerOff, 0, 0, 0, 0, 0, sequence_number, 0, 0, 0)
        IncreaseSequenceNumber()
        return bArr
    }

    enum class VoltageSelection {
        AUTOMATIC,
        V50,
        V30,
        V18
    }

    private fun iccPowerOn(voltageSelection: VoltageSelection): ByteArray {
        val bArr = ByteArray(10)
        bArr[0] = PC_to_RDR_IccPowerOn
        bArr[1] = 0
        bArr[2] = 0
        bArr[3] = 0
        bArr[4] = 0
        bArr[5] = 0
        bArr[6] = sequence_number
        if (voltageSelection == VoltageSelection.AUTOMATIC) {
            bArr[7] = 0
        }
        if (voltageSelection == VoltageSelection.V50) {
            bArr[7] = 1
        }
        if (voltageSelection == VoltageSelection.V18) {
            bArr[7] = 2
        }
        if (voltageSelection == VoltageSelection.V30) {
            bArr[7] = 3
        }
        bArr[8] = 0
        bArr[9] = 0
        IncreaseSequenceNumber()
        return bArr
    }

    fun powerOn(): Boolean {
        Timber.tag("CardReader:").w(" PowerON")
        val holder = Holder()
        for (voltageSelection in arrayOf(
            VoltageSelection.AUTOMATIC,
            VoltageSelection.V18,
            VoltageSelection.V30,
            VoltageSelection.V50
        )) {
            if (this.io(this.iccPowerOn(voltageSelection), holder)) {
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {

        if (t1 != null) {
            t1!!.interrupt()
        }

        if (readCardThread != null) {
            readCardThread!!.interrupt()
        }

        finish()
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SmobileApp.instance!!.appComponent.inject(this)
        var from: String = "Mob"
        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from").toString()
            if (from == "Mob") setContentView(R.layout.activity_reader) else setContentView(R.layout.activity_reader)
        } else {
            setContentView(R.layout.activity_reader)
        }
        RealmManager.open()
        loader = Loader.getInstance()
        textViewState = findViewById(R.id.textViewState)
        imageViewState = findViewById(R.id.imageViewState)
        progressBar = findViewById(R.id.progressBar)
        usbmanager = null
        usbdevice = null
        usbdeviceconnection = null
        val intentFilter = IntentFilter()
        intentFilter.addAction(STATE)
        intentFilter.addAction(STATE_READ)
        registerReceiver(this.stateChangeReceiver, intentFilter)
        val intentFilter2 = IntentFilter()
        intentFilter2.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED")
        intentFilter2.addAction("android.hardware.usb.action.USB_ACCESSORY_DETACHED")
        intentFilter2.addAction(ACTION_USB_REQUEST_PERMISSION)
        registerReceiver(this.usbAttachmentReceiver, intentFilter2)
        val intentFilter3 = IntentFilter()
        intentFilter3.addAction(SMART_CARD_PUT_IN)
        intentFilter3.addAction(SMART_CARD_GET_OUT)
        registerReceiver(this.cardInsertationReceiver, intentFilter3)
        SETSTATE(this, State.connect_card_reader)
        usbmanager = getSystemService(Context.USB_SERVICE) as UsbManager
        val usbManager = usbmanager
        if (usbManager != null) {
            for (requestpermission in usbManager.deviceList.values) {
                requestpermission(requestpermission, true)
            }
        }
        api = TachoFileAPI.create()
        storage = StorageCardTemporaryManager(application)
        val prefs = getSharedPreferences("CARD_PREFS", Context.MODE_PRIVATE)
        lifecycleScope.launch {
            userPreferences.userEmail.collect {
                uniqueId = Date().toString() + "-" + it.replace(".", "-")
                log.email = it
            }
        }
        back_cardReader.setOnClickListener {
            finish()
        }
        val enabled = read()
        //// add comentaire fireBase
        val pInfo: PackageInfo = this@ReaderActivityKotlin.packageManager.getPackageInfo(
            this@ReaderActivityKotlin.packageName,
            0
        )
        val udid = Settings.Secure.getString(
            this@ReaderActivityKotlin.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val version: String = pInfo.versionName
        DeviceName.init(this@ReaderActivityKotlin)
        optimizeFireBase(read(), uniqueId, version, "9")
        optimizeFireBase(read(), uniqueId, DeviceName.getDeviceName(), "10")
        optimizeFireBase(read(), uniqueId, udid, "11")
    }

    internal enum class State {
        connect_card_reader,
        grant_usb_device,
        grant_refused,
        use_ccid_card_reader,
        insert_driver_card,
        use_driver_card,
        reading,
        remove_driver_card
    }

    internal enum class iccsstatus {
        ICC_PRESENT_AND_ACTIVE,
        ICC_PRESENT_AND_INACTIVE,
        ICC_NOT_PRESENT,
        RFU
    }

    fun openusbdevice(usbDevice: UsbDevice?): Boolean {
        val usbManager = usbmanager
        if (usbManager != null) {
            openDevice = usbManager.openDevice(usbDevice)
            if (openDevice != null) {

                var usbEndpoint3: UsbEndpoint? = null
                for (i in 0 until usbDevice!!.interfaceCount) {
                    val usbInterface = usbDevice.getInterface(i)
                    if ((usbInterface.interfaceClass == 11 || usbInterface.interfaceClass == 0) && (openDevice!!.claimInterface(
                            usbInterface,
                            false
                        ) || openDevice!!.claimInterface(usbInterface, true))
                    ) {
                        var usbEndpoint4 = usbEndpoint3
                        var usbEndpoint5 = usbEndpoint2
                        var usbEndpoint6 = usbEndpoint
                        for (i2 in 0 until usbInterface.endpointCount) {
                            val endpoint = usbInterface.getEndpoint(i2)
                            if (endpoint.type == 2 && endpoint.direction == 0) {
                                usbEndpoint6 = endpoint
                            }
                            if (endpoint.type == 2 && endpoint.direction == 128) {
                                usbEndpoint5 = endpoint
                            }
                            if (endpoint.type == 3 && endpoint.direction == 128) {
                                usbEndpoint4 = endpoint
                            }
                        }
                        usbEndpoint = usbEndpoint6
                        usbEndpoint2 = usbEndpoint5
                        usbEndpoint3 = usbEndpoint4
                    }
                }
                if (usbEndpoint == null || usbEndpoint2 == null || usbEndpoint3 == null) {
                    openDevice!!.close()
                    usbdevice = null
                    usbdeviceconnection = null
                    return false
                }

                val maxPacketSize = usbEndpoint3.maxPacketSize
                val allocate = ByteBuffer.allocate(maxPacketSize)
                val usbRequest = UsbRequest()
                usbRequest.initialize(openDevice, usbEndpoint3)
                usbRequest.clientData = allocate
                val usbDeviceConnection = openDevice
                val r3 = object : Thread() {
                    override fun run() {
                        while (true) {
                            usbRequest.queue(allocate, maxPacketSize)
                            val requestWait = usbDeviceConnection!!.requestWait()
                            if (requestWait == null) {
                                usbDeviceConnection.close()
                                return
                            } else if (requestWait == usbRequest) {
                                val byteBuffer = usbRequest.clientData as ByteBuffer
                                val bArr = ByteArray(byteBuffer.position())
                                byteBuffer.rewind()
                                byteBuffer.get(bArr)
                                if (bArr.size == 2) {
                                    var z = false
                                    if (bArr[0].toInt() == 80) {
                                        if (bArr[1].toInt() and 1 == 1) {
                                            z = true
                                        }
                                        if (z) {
                                            this@ReaderActivityKotlin.sendBroadcast(
                                                Intent(
                                                    SMART_CARD_PUT_IN
                                                )
                                            )
                                        }
                                        if (!z) {
                                            this@ReaderActivityKotlin.sendBroadcast(
                                                Intent(
                                                    SMART_CARD_GET_OUT
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                r3.start()
                usbdevice = usbDevice
                usbdeviceconnection = openDevice
                return true
            }
            usbdevice = null
            usbdeviceconnection = null
            return false
        }
        usbdevice = null
        usbdeviceconnection = null
        return false
    }

    fun write(connection: UsbDeviceConnection, epOut: UsbEndpoint?, command: ByteArray) {
        result = StringBuilder()
        connection.bulkTransfer(epOut, command, command.size, 5000)
        //For Printing logs you can use result variable
        for (bb in command) {
            result!!.append(String.format(" %02X ", bb))
        }
        Timber.tag("Smart Card send was").d(result!!.toString())

    }

    fun readPowerOFF(connection: UsbDeviceConnection, epIn: UsbEndpoint): ByteArray {
        result = StringBuilder()
        val buffer = ByteArray(epIn.maxPacketSize)
        var byteCount = 0
        byteCount = connection.bulkTransfer(epIn, buffer, buffer.size, 5000)
        if (byteCount >= 0) {
            for (bb in buffer) {
                result!!.append(String.format(" %02X ", bb))
            }

            Timber.tag("Smart Card received was").d(toHexString(buffer, 0, byteCount))

        }
        return buffer
    }

    @SuppressLint("WrongConstant")
    fun requestpermission(usbDevice: UsbDevice?, z: Boolean) {
        if (usbmanager != null) {
            val intent = Intent(ACTION_USB_REQUEST_PERMISSION)
            intent.putExtra(EXTRA_ACTION_USB_REQUEST_PERMISSION_ONCREATE, z)
            usbmanager!!.requestPermission(
                usbDevice,
                PendingIntent.getBroadcast(this, 0, intent, 134217728)
            )
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        val usbDeviceConnection = usbdeviceconnection
        if (usbDeviceConnection != null) {
            usbDeviceConnection.close()
            usbdeviceconnection = null
        }
        unregisterReceiver(this.cardInsertationReceiver)
        unregisterReceiver(this.usbAttachmentReceiver)
        unregisterReceiver(this.stateChangeReceiver)
        RealmManager.close()
    }

    //the same as SDriver : Reading and appending from card
    fun io(bArr: ByteArray, holder: Holder): Boolean {
        val sb = StringBuilder()
        sb.append(">>> ")


        sb.append(toHexString(bArr))
        val holder2 = Holder()
        val holder3 = Holder()
        val holder4 = Holder()
        val holder5 = Holder()
        val holder6 = Holder()
        Timber.tag("CardReaderTest").i(sb.toString())
        if (usbdeviceconnection!!.bulkTransfer(usbEndpoint, bArr, bArr.size, 1000) != bArr.size) {
            return false
        }
        while (true) {
            val usbDeviceConnection = usbdeviceconnection
            val usbEndpoint = this.usbEndpoint2
            val bArr4 = this.buffer
            var bulkTransfer =
                usbDeviceConnection!!.bulkTransfer(usbEndpoint, bArr4, bArr4.size, 5000)
            if (bulkTransfer < 0) {
                return false
            } else {
                var res = toHexString(this.buffer, 0, bulkTransfer)
                while (res.endsWith("0100")) {
                    bulkTransfer =
                        usbDeviceConnection.bulkTransfer(usbEndpoint, bArr4, bArr4.size, 5000)
                    val sb2 = StringBuilder()
                    sb2.append("<<< ")
                    //sb2.append(Convert.ToHexString(this.buffer, 0, bulkTransfer));
                    sb2.append(toHexString(this.buffer, 0, bulkTransfer))
                    val result = sb2.toString()
                    //if (result.length>20)
                    Timber.tag("CardReaderTest").i(result)
                    res = toHexString(this.buffer, 0, bulkTransfer)
                }
            }


            val sb2 = StringBuilder()
            sb2.append("<<< ")
            sb2.append(toHexString(this.buffer, 0, bulkTransfer))
            val result = sb2.toString()
            //if (result.length>20)
            Timber.tag("CardReaderTest").i(result)

            if (!info(this.buffer, bulkTransfer, holder2, holder3, holder4, holder6, holder5)) {
                return false
            }
            val sb3 = StringBuilder()
            sb3.append("Command=0x")
            sb3.append(toHexString(holder2.GetByte()))
            sb3.append(", Error=")
            sb3.append(holder6.GetByte().toInt())
            sb3.append(", Length=")
            sb3.append(holder5.GetInt())
            Timber.tag("CardReaderTest").w(sb3.toString())
            return if (toHexString(this.buffer, 0, bulkTransfer).endsWith("9000")) {
                holder.Put(bArr4.copyOfRange(0, bulkTransfer))
                true
            } else
                false
        }


    }

    companion object {
        private var sequence_number: Byte = 0
        val FILE_IC = 5
        val FILE_ICC = 2
        val FILE_TACHO_Application_Identifier = 1281
        val FILE_TACHO_CA_Certificate = 49416
        val FILE_TACHO_Card_Certificate = 49408
        val FILE_TACHO_Card_Download = 1294
        val FILE_TACHO_Control_Activity_Data = 1288
        val FILE_TACHO_Current_Usage = 1287
        val FILE_TACHO_Driver_Activity_Data = 1284
        val FILE_TACHO_Driving_Licence_Info = 1313
        val FILE_TACHO_Events_Data = 1282
        val FILE_TACHO_Faults_Data = 1283
        val FILE_TACHO_Identification = 1312
        val FILE_TACHO_Places = 1286
        val FILE_TACHO_Specific_Conditions = 1314
        val FILE_TACHO_Vehicles_Used = 1285
        val FILE_TACHO_VehiclesUNITIS_Used = 1315

        val FILE_TACHOG2_Gnss_Places = 1316
        val FILE_TACHOG2_CardMA_Certificate = 49408
        val FILE_TACHOG2_CardSignCertificate = 49409
        val FILE_TACHOG2_Link_Certificat = 49417

        private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
        private val ACTION_USB_REQUEST_PERMISSION = "ACTION_USB_REQUEST_PERMISSION"
        private val EXTRA_ACTION_USB_REQUEST_PERMISSION_ONCREATE = "oncreate"

        /* access modifiers changed from: private */
        var usbdevice: UsbDevice? = null

        /* access modifiers changed from: private */
        var usbdeviceconnection: UsbDeviceConnection? = null

        /* access modifiers changed from: private */
        var usbmanager: UsbManager? = null

        internal var state: State? = State.connect_card_reader
        internal lateinit var ICCstate: iccsstatus
        private val EXTRA_STATE = "state"
        private val EXTRA_STATE_READ_PERCENT = "percent"
        private val SMART_CARD_GET_OUT = "SMART_CARD_GET_OUT"
        private val SMART_CARD_PUT_IN = "SMART_CARD_PUT_IN"
        private val STATE = "STATE"
        private val STATE_READ = "STATE_READ"

        fun UpdateBinary(j: Long): ByteArray {
            return byteArrayOf(
                0,
                -42,
                0,
                0,
                4,
                (-16777216 and j.toInt()).ushr(24).toByte(),
                (16711680 and j.toInt()).ushr(16).toByte(),
                (65280 and j.toInt()).ushr(8).toByte(),
                (j and 255).toInt().toByte()
            )
        }

        fun readFile(num: Int, b: Byte): ByteArray {
            return byteArrayOf(
                0,
                -80,
                ToByte((num and MotionEventCompat.ACTION_POINTER_INDEX_MASK).ushr(8)),
                ToByte(num and 255),
                b
            )
        }

        private fun IncreaseSequenceNumber() {
            sequence_number = (sequence_number + 1).toByte()
        }

        fun Selectfile(num: Int): ByteArray {
            return byteArrayOf(
                0,
                -92,
                2,
                12,
                2,
                ToByte((num and MotionEventCompat.ACTION_POINTER_INDEX_MASK).ushr(8)),
                ToByte(num and 255)
            )
        }

        private val PC_to_RDR_XfrBlock: Byte = 111

        /*  fun ChangeDirectory(str: String): ByteArray {
              val bArr = ByteArray(str.length + 6)
              bArr[0] = 0
              bArr[1] = -92
              bArr[2] = 4
              bArr[3] = 12
              bArr[4] = ToByte(str.length + 1)
              bArr[5] = -1
              bArr[6] = 84
              bArr[7] = 65
              bArr[8] = 67
              bArr[9] = 72
              bArr[10] = 79
              return bArr
          }
  */
        fun ChangeDirectoryd(str: String): ByteArray {
            val bArr = ByteArray(str.length + 6)
            bArr[0] = 0
            bArr[1] = -92
            bArr[2] = 4
            bArr[3] = 12
            bArr[4] = ToByte(str.length + 1)
            val bytes = str.toByteArray(Charset.forName("US-ASCII"))
            bArr[5] = -1
            bArr[6] = bytes[0]
            bArr[7] = bytes[1]
            bArr[8] = bytes[2]
            bArr[9] = bytes[3]
            bArr[10] = bytes[4]
            return bArr
        }

        private val PC_to_RDR_IccPowerOff: Byte = 99
        private val PC_to_RDR_IccPowerOn: Byte = 98

        internal fun SETSTATE(context: Context, state2: State) {
            val intent = Intent(STATE)
            intent.putExtra(EXTRA_STATE, state2)
            context.sendBroadcast(intent)
        }


        internal fun SETSTATEREAD(context: Context, i: Int) {
            val intent = Intent(STATE_READ)
            intent.putExtra(EXTRA_STATE_READ_PERCENT, i)
            context.sendBroadcast(intent)
        }

        private val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
        private fun getRandomString(sizeOfRandomString: Int): String {
            val random = Random()
            val sb = StringBuilder(sizeOfRandomString)
            for (i in 0 until sizeOfRandomString)
                sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
            return sb.toString()
        }
    }

    // send data fireBase
    fun optimizeFireBase(state: Boolean, id: String, msg: String, ref: String) {
        Timber.tag("STATE1").e(state.toString())

        if (state) {
            Timber.tag("STATE").e("here ")

            when (ref) {

                "1" -> {
                    log.readingCardSteps.add(msg)
                    databaseRef.child(id).setValue(log)
                }
                "2" -> {
                    log.endDate = msg
                    databaseRef.child(id).setValue(log)
                }
                "3" -> {
                    log.messages.add(msg)
                    databaseRef.child(id).setValue(log)
                }
                "4" -> {
                    log.startDate = msg
                    databaseRef.child(id).setValue(log)
                }
                "5" -> {
                    log.responseCodeTFD = msg
                    databaseRef.child(id).setValue(log)
                }

                "7" -> {
                    log.serialNumber = msg
                    databaseRef.child(id).setValue(log)
                }
                "8" -> {
                    log.lecteurName = msg
                    databaseRef.child(id).setValue(log)
                }
                "9" -> {
                    log.versionName = msg
                    databaseRef.child(id).setValue(log)
                }
                "10" -> {
                    log.deviceName = msg
                    databaseRef.child(id).setValue(log)
                }
                "11" -> {
                    log.udid = msg
                    databaseRef.child(id).setValue(log)
                }
                "12" -> {
                    log.fileName = msg
                    databaseRef.child(id).setValue(log)
                }
                "13" -> {
                    log.sendMessage.add(msg)
                    databaseRef.child(id).setValue(log)
                }
                else -> databaseRef.child(id).setValue(log)
            }
            databaseRef.child(id).setValue(log)
        }
    }

    fun read(): Boolean {
        var b = false
        lifecycleScope.launch {
            settingsPreferences.logged.collect {
                Timber.tag("STATE0").e(b.toString())
                b = it
            }
        }
        Timber.tag("STATE3").e(b.toString())
        return b
    }

}
