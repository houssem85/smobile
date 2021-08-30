package fr.strada.smobile.di.messagerie.nouveau_message

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.messagerie.nouveau_message.NouveauMessageFragment
import fr.strada.smobile.ui_tablette.messagerie.nouveau_message.NouveauMessageTabletteFragment
import fr.strada.smobile.utils.listner.ItemDestinataireListner

@Subcomponent (modules = [NouveauMessageModule::class , NouveauMessageViewModelModule::class])
interface NouveauMessageComponent {

    fun inject(nouveauMessageFragment: NouveauMessageFragment)

    fun inject(nouveauMessageTabletteFragment: NouveauMessageTabletteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setItemDestinataireListner(itemDestinataireListner: ItemDestinataireListner?) : Builder

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : NouveauMessageComponent
    }
}