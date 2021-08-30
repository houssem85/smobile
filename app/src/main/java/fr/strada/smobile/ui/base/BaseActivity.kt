package fr.strada.smobile.ui.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.repositories.UserRepository
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.ui.auth.auth0.AuthActivity
import fr.strada.smobile.ui.auth.borne.AuthBorne
import fr.strada.smobile.ui.auth.tenant.TenantActivity
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.OPEN_ACCUEIL
import fr.strada.smobile.utils.Status
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

private const val INACTIVITY_SECONDS: Int = 500

open class BaseActivity : AppCompatActivity() {

    protected lateinit var locale : Locale
    @Inject
    internal lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        initLocale()
        setOriontationUnspecifiedIfInTabletteMode()
        startInactivityTimerIfNeed()
    }

    private fun initLocale()
    {
        locale = Utils.getCurrentLocal(this)
    }

    private fun injectDependencies()
    {
       SmobileApp.instance!!.appComponent.inject(this)
    }

    protected fun checkUserSession()
    {
        lifecycleScope.launch {
            val isUserModeBorne = userRepository.isUserModeBorne()
            val isUserLoggedIn = userRepository.isUserLoggedIn()
            val isUserModeBorneLoggedIn = userRepository.isUserModeBorneLoggedIn()
            val tenant = userRepository.getTenant()
            if(isUserLoggedIn){
                Timber.tag("auth").e("111111")
                if (tenant.isNotEmpty())
                {
                    if(isUserModeBorne){
                        if(isUserModeBorneLoggedIn){
                            startMainActivityMobileOrTablette()
                        }else {
                            startAuthBornActivity()
                        }
                    }else {
                        startActivity(Intent(this@BaseActivity , MainActivitySpi::class.java))
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        finish()
                        //startMainActivityMobileOrTablette()
                    }
                }else
                {
                    startAuthtenatActivity()
                }
            }else {
                Timber.tag("auth").e("2222")

                SmobileApp.instance!!.stopNotificationService()
                startAuthActivity()
            }
        }
    }

     fun startMainActivityMobileOrTablette()
    {
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        val intent = if(tabletSize) {
            Intent(this, MainTabletteActivity::class.java)
        }else {
            Intent(this, MainActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = OPEN_ACCUEIL
        startActivity(intent)
        finish()
    }


    private fun startAuthtenatActivity()
    {
        val intent  = Intent(this, TenantActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun startAuthBornActivity()
    {
        val intent  = Intent(this, AuthBorne::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun startAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    open fun setOriontationUnspecifiedIfInTabletteMode()
    {
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }

    private fun startInactivityTimerIfNeed()
    {
        lifecycleScope.launch {
            val isUserModeBorneLoggedIn = userRepository.isUserModeBorneLoggedIn()
            // TODO: 4/20/21 add card is reading
            if(isUserModeBorneLoggedIn)
            {
                inactivityCountDownTimer.start()
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        lifecycleScope.launch {
            val isUserModeBorneLoggedIn = userRepository.isUserModeBorneLoggedIn()
            if(isUserModeBorneLoggedIn)
            {
                inactivityCountDownTimer.cancel()
                inactivityCountDownTimer.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        inactivityCountDownTimer.cancel()
    }

    private var inactivityCountDownTimer = object : CountDownTimer(INACTIVITY_SECONDS * 1000.toLong(), 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            deconnectionModeBorne()
        }
    }

    protected fun deconnectionModeBorne()
    {
        lifecycleScope.launch {
            userRepository.isUserModeBorneLoggedIn(false).apply {
                if(this.status == Status.SUCCESS){
                    checkUserSession()
                }
            }
        }
    }

    fun deconnecter()
    {
        Timber.tag("dec").e("this")
        lifecycleScope.launch {
            userRepository.deconnecter()
            SmobileApp.instance!!.startShortCutService()
            checkUserSession()
        }
    }
}