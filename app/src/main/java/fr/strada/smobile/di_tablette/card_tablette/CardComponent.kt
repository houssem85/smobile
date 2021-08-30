package fr.strada.smobile.di_tablette.card

import dagger.Subcomponent
import fr.strada.smobile.di.card.CardModule
import fr.strada.smobile.ui.card.ReaderActivityKotlin

@Subcomponent(modules = [CardModule::class])
interface CardComponent {

    fun inject(readerActivityKotlin: ReaderActivityKotlin)

    @Subcomponent.Builder
    interface Builder {

        fun build() : CardComponent
    }
}