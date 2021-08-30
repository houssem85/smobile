package fr.strada.smobile.ui.spi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.util.Base64
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.runningapprm.other.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.ActivityMainSpiBinding
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.ext.*
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main_spi.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainActivitySpi : BaseActivity() {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var binding: ActivityMainSpiBinding
    lateinit var viewModel: MainActivitySpiViewModel

    private val TAG = "MyBroadcastReceiver"

    private var trigered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainSpiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SmobileApp.instance!!.initAttributesPointeuse()
        SmobileApp.instance!!.refreshDataWidget()
        SmobileApp.instance!!.startShortCutService()
        initComponent()
        injectDepencencices()
        initViewModel()
        observeUserImage()
        setupNavigation()
        checkAskPermission(this)
        registerCheckGPSReceiver()
        navigateToTrackingFragmentIfNeeded(intent)
        val navView: BottomNavigationView = binding.navView
        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main_spi)!!
                .findNavController()
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        trigered = false
        registerCheckGPSReceiver()
        //registerReceiver(gpsReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        showDialogActiverLocalisation(this)
    }


    override fun onPause() {
        super.onPause()
        unregisterCheckGPSReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterCheckGPSReceiver()
    }


    private val checkGPSReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if(!trigered){
                trigered = true
                Timber.e("onReceive")
                showDialogActiverLocalisation(this@MainActivitySpi)
            }

        }
    }

    private fun registerCheckGPSReceiver() {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(checkGPSReceiver, filter)
    }

    private fun unregisterCheckGPSReceiver() {
        unregisterReceiver(checkGPSReceiver)
        Timber.e("unregistered!!")
    }

    private fun initComponent() {}

    private fun injectDepencencices() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(MainActivitySpiViewModel::class.java)
    }

    fun showLoading() {}
    fun dismissLoading() {}
    private fun observeUserImage() {
        viewModel.userImage.observe(this, {
            if (it.isNotEmpty()) {
                val decodedBytes: ByteArray = Base64.decode(it, Base64.DEFAULT)
                val decodedByte: Bitmap =
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                imageprofile_spi_menu.load(decodedByte) { transformations(CircleCropTransformation()) }
            }
        })
    }

    private fun setupNavigation() {
        image_back_main.setOnClickListener {
            findNavController(R.id.nav_host_fragment_activity_main_spi).navigate(R.id.action_global_navigation_home_spi)
        }
        imageprofile_spi_menu.setOnClickListener {
            val dialog = DialogProfilFragment.newInstance()
            dialog.onClickDeconnecter = {
                deconnecter()
            }
            dialog.onClickprofil = {
                findNavController(R.id.nav_host_fragment_activity_main_spi).navigate(R.id.action_global_profilFragment2)
                dialog.dismiss()

            }
            dialog.onClickaPropos = {
                findNavController(R.id.nav_host_fragment_activity_main_spi).navigate(R.id.action_global_AProposFragment2)
                dialog.dismiss()

            }
            dialog.onClickReglage = {
                findNavController(R.id.nav_host_fragment_activity_main_spi).navigate(R.id.action_global_settingsFragment)
                dialog.dismiss()
            }
            dialog.show(supportFragmentManager, DialogProfilFragment.TAG)
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            navigateToTrackingFragmentIfNeeded(intent)
        }
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent) {
        if (intent?.action == Constants.ACTION_SHOW_TRACKING_FRAGMENT) {
            image_back_main.findNavController().navigate(R.id.action_global_trackingFragment)
            Timber.e("navigateToTrackingFragmentIfNeeded")
        } else
            Timber.e("navigateToTrackingFragmentIfNeeded: " + intent?.action)
    }

    private fun checkAskPermission(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            val isPermissionGranted = checkPermissionLocation(context)
            if (!isPermissionGranted) {
                insistOnPermission(context)
                return@launch
            }

        }

    }


}