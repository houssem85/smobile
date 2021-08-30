package fr.strada.smobile.di_tablette.main_tablette

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class MainTabletteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainTabletteViewModel::class)
    abstract fun bindMainTabletteViewModel(mainTabletteViewModel: MainTabletteViewModel): ViewModel
}