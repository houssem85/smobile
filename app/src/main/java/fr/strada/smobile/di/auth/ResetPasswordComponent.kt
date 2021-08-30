package fr.strada.smobile.di.auth

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.auth.password.ResetPasswordActivity

@Subcomponent(modules = [ResetModule::class,ResetModelModule::class])
interface ResetPasswordComponent {

    fun inject(resetpassword: ResetPasswordActivity)


    @Subcomponent.Builder
    interface Builder
    {
        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : ResetPasswordComponent
    }
}