package fr.strada.smobile.di.auth

import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.auth.auth0.AuthActivity

@Subcomponent(modules = [AuthModule::class, Auth0ViewModelModule::class])
interface AuthComponent {
    fun inject(authActivity: AuthActivity)
    @Subcomponent.Builder
    interface Builder{
        @BindsInstance
        fun setContext(context: AuthActivity) : Builder
        fun build() : AuthComponent
    }
}