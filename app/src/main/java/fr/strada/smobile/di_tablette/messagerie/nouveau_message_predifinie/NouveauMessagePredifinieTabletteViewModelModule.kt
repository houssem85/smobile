package fr.strada.smobile.di_tablette.messagerie.nouveau_message_predifinie

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.messagerie.nouveau_message_predefinie.NouveauMessagePredifinieTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class NouveauMessagePredifinieTabletteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(NouveauMessagePredifinieTabletteViewModel::class)
    abstract fun bindNouveauMessagePredifinieTabletteViewModel(nouveauMessagePredifinieTabletteViewModel: NouveauMessagePredifinieTabletteViewModel): ViewModel
}