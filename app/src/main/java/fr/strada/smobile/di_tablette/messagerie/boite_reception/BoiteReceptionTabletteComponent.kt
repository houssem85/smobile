package fr.strada.smobile.di_tablette.messagerie.boite_reception

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.messagerie.boite_reception.BoiteReceptionTabletteFragment
import fr.strada.smobile.utils.listner.ItemBoiteReceptionListener

@Subcomponent(modules = [BoiteReceptionTabletteModule::class,BoiteReceptionTabletteViewModelModule::class])
interface BoiteReceptionTabletteComponent {

    fun inject(BoiteReceptionTabletteFragment: BoiteReceptionTabletteFragment)
    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setOnClickListner(itemBoiteReceptionListener: ItemBoiteReceptionListener) : Builder

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : BoiteReceptionTabletteComponent
    }
}