package fr.strada.smobile.di_tablette.messagerie.boite_reception

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.messagerie.boite_reception.BoiteReceptionTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class BoiteReceptionTabletteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(BoiteReceptionTabletteViewModel::class)
    abstract fun bindBoiteReceptionTabletteViewModel(boiteReceptionTabletteViewModel: BoiteReceptionTabletteViewModel): ViewModel
}