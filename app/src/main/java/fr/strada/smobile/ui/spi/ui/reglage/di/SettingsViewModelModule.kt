package fr.strada.smobile.ui.spi.ui.reglage.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.spi.ui.reglage.SettingsViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class SettingsViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel
}