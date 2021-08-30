package fr.strada.smobile.di_tablette.messagerie


import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.messagerie.MessagerieTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class MessagerieTabletteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MessagerieTabletteViewModel::class)
    abstract fun bindMessagerieTabletteViewModel(messagerieTabletteViewModel: MessagerieTabletteViewModel): ViewModel
}