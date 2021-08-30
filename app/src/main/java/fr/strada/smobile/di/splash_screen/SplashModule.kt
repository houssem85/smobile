package fr.strada.smobile.di.splash_screen

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R

@Module
class SplashModule {
    @Provides
    fun provideZoomANimation(context: Context,animationListener: Animation.AnimationListener):Animation{
        val logoAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom)
        logoAnimation.setAnimationListener(animationListener)
        return logoAnimation
    }
}