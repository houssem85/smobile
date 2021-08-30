package fr.strada.smobile.di_tablette.accueil_tablette.statistique

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.accueil.statistique.StatistiqueFragment

@Subcomponent(modules = [StatistiqueModule::class, StatistiqueViewModelModule::class])
interface StatistiqueComponent {
    fun inject(statistiqueFragment: StatistiqueFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : StatistiqueComponent
    }
}