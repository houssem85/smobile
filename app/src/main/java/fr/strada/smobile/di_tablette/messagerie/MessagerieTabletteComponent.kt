package fr.strada.smobile.di_tablette.messagerie

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.messagerie.MessagerieTabletteFragment

@Subcomponent(modules = [MessagerieTabletteModule::class,MessagerieTabletteViewModelModule::class])
interface MessagerieTabletteComponent {

    fun inject(messagerieTabletteFragment: MessagerieTabletteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : MessagerieTabletteComponent.Builder

        fun build() : MessagerieTabletteComponent
    }
}