package fr.strada.smobile.di_tablette.accueil_tablette

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.accueil.AccueilTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class AccueilTabletteViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccueilTabletteViewModel::class)
    abstract fun bindAccueilTabletteViewModel(accueilTabletteViewModel: AccueilTabletteViewModel): ViewModel

}