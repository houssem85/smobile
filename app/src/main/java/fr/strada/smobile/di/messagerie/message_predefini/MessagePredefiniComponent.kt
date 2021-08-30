package fr.strada.smobile.di.messagerie.message_predefini

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.messagerie.message_predefini.MessagePredefiniFragment
import fr.strada.smobile.utils.listner.ItemMessagePredefiniListener

@Subcomponent(modules = [MessagePredefiniModule::class, MessagePredefiniViewModelModule::class])
interface MessagePredefiniComponent {

    fun inject(messagePredefiniFragment: MessagePredefiniFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : MessagePredefiniComponent

        @BindsInstance
        fun setOnClickListner(itemMessagePredefiniListener: ItemMessagePredefiniListener) : MessagePredefiniComponent.Builder

    }



}