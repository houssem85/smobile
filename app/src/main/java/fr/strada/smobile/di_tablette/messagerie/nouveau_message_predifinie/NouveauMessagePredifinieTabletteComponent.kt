package fr.strada.smobile.di_tablette.messagerie.nouveau_message_predifinie

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.messagerie.nouveau_message_predefinie.NouveauMessagePredifinieTabletteFragment

@Subcomponent(modules = [NouveauMessagePredifinieTabletteModule::class, NouveauMessagePredifinieTabletteViewModelModule::class])
interface NouveauMessagePredifinieTabletteComponent {

    fun inject(nouveauMessagePredifinieTabletteFragment: NouveauMessagePredifinieTabletteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : NouveauMessagePredifinieTabletteComponent
    }

}