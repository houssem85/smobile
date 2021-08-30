package fr.strada.smobile.di.app

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}