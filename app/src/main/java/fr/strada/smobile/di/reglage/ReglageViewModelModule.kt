package fr.strada.smobile.di.reglage

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.reglage.ReglageViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class ReglageViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ReglageViewModel::class)
    abstract fun bindReglageViewModel(reglageViewModel: ReglageViewModel): ViewModel
}