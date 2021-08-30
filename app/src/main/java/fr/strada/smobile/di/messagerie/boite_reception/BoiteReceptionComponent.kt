package fr.strada.smobile.di.messagerie.boite_reception

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.messagerie.boite_de_reception.BoiteReceptionFragment
import fr.strada.smobile.utils.listner.ItemBoiteReceptionListener

@Subcomponent(modules = [BoiteReceptionModule::class, BoiteReceptionViewModelModule::class])
interface BoiteReceptionComponent {

    fun inject(boiteReceptionFragment: BoiteReceptionFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : BoiteReceptionComponent

        @BindsInstance
        fun setOnClickListner(itemBoiteReceptionListener: ItemBoiteReceptionListener) : BoiteReceptionComponent.Builder
    }
}