package fr.strada.smobile.di.messagerie.nouveau_message

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.messagerie.nouveau_message.NouveauMessageViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class NouveauMessageViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(NouveauMessageViewModel::class)
    abstract fun bindNouveauMessageViewModel(nouveauMessageViewModel: NouveauMessageViewModel): ViewModel
}