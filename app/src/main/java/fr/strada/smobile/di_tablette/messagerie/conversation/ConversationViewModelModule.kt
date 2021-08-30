package fr.strada.smobile.di_tablette.messagerie.conversation

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.messagerie.conversation.ConversationViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class ConversationViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ConversationViewModel::class)
    abstract fun bindConversationViewModel(conversationViewModel: ConversationViewModel): ViewModel
}