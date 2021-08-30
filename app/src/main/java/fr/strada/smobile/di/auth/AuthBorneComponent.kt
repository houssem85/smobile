package fr.strada.smobile.di.auth

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.auth.borne.AuthBorne


@Subcomponent(modules = [AuthBorneModule::class,AuthBorneViewModelModule::class])
interface AuthBorneComponent{
    fun inject(authBorne: AuthBorne)


    @Subcomponent.Builder
    interface Builder
    {
        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : AuthBorneComponent
    }

}