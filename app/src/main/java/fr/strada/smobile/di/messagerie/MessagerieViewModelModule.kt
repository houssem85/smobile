package fr.strada.smobile.di.messagerie

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.messagerie.MessagerieViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class MessagerieViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MessagerieViewModel::class)
    abstract fun bindMessagerieViewModel(messagerieViewModel: MessagerieViewModel): ViewModel
}