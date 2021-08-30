package fr.strada.smobile.di_tablette.messagerie.conversation

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.messagerie.conversation.ConversationFragment

@Subcomponent(modules = [ConversationModule::class,ConversationViewModelModule::class])
interface ConversationComponent {

    fun inject(conversationFragment: ConversationFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : ConversationComponent
    }

}