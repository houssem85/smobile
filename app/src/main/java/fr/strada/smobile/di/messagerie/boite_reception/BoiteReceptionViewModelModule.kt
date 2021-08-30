package fr.strada.smobile.di.messagerie.boite_reception

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.messagerie.boite_de_reception.BoiteReceptionViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class BoiteReceptionViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BoiteReceptionViewModel::class)
    abstract fun bindBoiteReceptionViewModel(boiteReceptionViewModel: BoiteReceptionViewModel): ViewModel
}