package fr.strada.smobile.di.messagerie

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.messagerie.MessagerieFragment

@Subcomponent(modules = [MessagerieModule::class, MessagerieViewModelModule::class])
interface MessagerieComponent {

    fun inject(messagerieFragment: MessagerieFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : MessagerieComponent
    }
}