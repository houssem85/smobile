package fr.strada.smobile.di.messagerie.message_predefini

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.messagerie.message_predefini.MessagePredefiniViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class MessagePredefiniViewModelModule  {

    @Binds
    @IntoMap
    @ViewModelKey(MessagePredefiniViewModel::class)
    abstract fun bindMessagePredefiniViewModel(messagePredefiniViewModel: MessagePredefiniViewModel): ViewModel

}