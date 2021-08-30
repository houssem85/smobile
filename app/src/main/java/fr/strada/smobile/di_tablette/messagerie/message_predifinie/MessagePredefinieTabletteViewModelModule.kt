package fr.strada.smobile.di_tablette.messagerie.message_predifinie

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.messagerie.message_predefinie.MessagePredefinieTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey


@Module
abstract class MessagePredefinieTabletteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MessagePredefinieTabletteViewModel::class)
    abstract fun bindMessagePredefinieTabletteViewModel(messagePredefinieTabletteViewModel: MessagePredefinieTabletteViewModel): ViewModel
}