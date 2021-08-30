package fr.strada.smobile.di_tablette.messagerie.update_message_predifinie

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.messagerie.update_message_predifinie.UpdateMessagePredefinieFragment


@Subcomponent(modules = [UpdateMessagePredefinieModule::class, UpdateMessagePredefinieViewModelModule::class])
interface UpdateMessagePredefinieComponent {

    fun inject(updateMessagePredefinieFragment : UpdateMessagePredefinieFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : UpdateMessagePredefinieComponent
    }
}