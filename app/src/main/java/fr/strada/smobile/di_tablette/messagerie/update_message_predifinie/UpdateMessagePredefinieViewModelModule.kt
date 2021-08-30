package fr.strada.smobile.di_tablette.messagerie.update_message_predifinie

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.messagerie.update_message_predifinie.UpdateMessagePredefinieViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class UpdateMessagePredefinieViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UpdateMessagePredefinieViewModel::class)
    abstract fun bindUpdateMessagePredefinieViewModel(updateMessagePredefinieViewModel: UpdateMessagePredefinieViewModel): ViewModel
}