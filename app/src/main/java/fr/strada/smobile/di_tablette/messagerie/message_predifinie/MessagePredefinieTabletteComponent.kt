package fr.strada.smobile.di_tablette.messagerie.message_predifinie

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.messagerie.message_predefinie.MessagePredefinieTabletteFragment
import fr.strada.smobile.utils.listner.ItemMessagePredefiniListener

@Subcomponent(modules =[MessagePredefinieTabletteModule::class,MessagePredefinieTabletteViewModelModule::class])
interface MessagePredefinieTabletteComponent {

    fun inject(messagePredefinieTabletteFragment: MessagePredefinieTabletteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) :Builder

        @BindsInstance
        fun setOnClickListner(itemMessagePredefiniListener: ItemMessagePredefiniListener) : Builder

        fun build() : MessagePredefinieTabletteComponent
    }
}