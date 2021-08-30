package fr.strada.smobile.ui.splash_screen

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import android.view.animation.Animation
import androidx.lifecycle.lifecycleScope
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.di.splash_screen.SplashComponent
import fr.strada.smobile.ui.auth.auth0.AuthActivity
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.SPLASH_TIME_OUT
import kotlinx.android.synthetic.main.activity_splashscreen.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

class SplashScreenActivity : BaseActivity(), Animation.AnimationListener {

    private lateinit var component: SplashComponent

    @Inject
    internal lateinit var logoAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        initComponent()
        injectDependencies()
        checkUserSessionIfNeed()
        startLogoAnimation()
    }

    private fun initComponent() {
        component = (application as SmobileApp).appComponent.splashComponent().setContext(this).setAnimationListener(this).build()
    }

    private fun injectDependencies() {
        component.inject(this)
    }

    private fun startLogoAnimation() {
        lifecycleScope.launch {
            delay(SPLASH_TIME_OUT)
            startAuthActivity()
        }


    }

    override fun onAnimationRepeat(p0: Animation?) {

    }

    override fun onAnimationStart(p0: Animation?) {
        img_logo.visibility = VISIBLE
    }

    override fun onAnimationEnd(p0: Animation?) {
        startAuthActivity()
    }

    private fun startAuthActivity()
    {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun checkUserSessionIfNeed()
    {
         lifecycleScope.launch {
             if(userRepository.isUserLoggedIn()){
                 checkUserSession()
             }
         }
    }
}
