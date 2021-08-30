package fr.strada.smobile.di_tablette.accueil_tablette.statistique

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.accueil.statistique.StatistiqueViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class StatistiqueViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StatistiqueViewModel::class)
    abstract fun bindStatistiqueViewModel(statistiqueViewModel: StatistiqueViewModel): ViewModel

}