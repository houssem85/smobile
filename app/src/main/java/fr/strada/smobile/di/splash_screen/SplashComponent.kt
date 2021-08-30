package fr.strada.smobile.di.splash_screen

import android.content.Context
import android.view.animation.Animation
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.splash_screen.SplashScreenActivity

@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {

    fun inject(splashScreenActivity:SplashScreenActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setAnimationListener(animationListener: Animation.AnimationListener) : Builder

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : SplashComponent
    }

}