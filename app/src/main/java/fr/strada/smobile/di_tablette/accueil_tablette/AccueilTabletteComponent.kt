package fr.strada.smobile.di_tablette.accueil_tablette

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.accueil.AccueilTabletteFragment

@Subcomponent(modules = [AccueilTabletteModule::class, AccueilTabletteViewModelModule::class])
interface AccueilTabletteComponent {

    fun inject(accueilTabletteFragment: AccueilTabletteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : AccueilTabletteComponent
    }
}