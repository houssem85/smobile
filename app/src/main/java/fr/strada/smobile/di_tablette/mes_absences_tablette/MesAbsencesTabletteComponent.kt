package fr.strada.smobile.di_tablette.mes_absences_tablette

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.mes_absences_tablette.MesAbsencesTabletteFragment

@Subcomponent(modules = [MesAbsencesTabletteModule::class,MesAbsencesTabletteViewModelModule::class])
interface MesAbsencesTabletteComponent {

    fun inject(mesAbsencesTabletteFragment: MesAbsencesTabletteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : MesAbsencesTabletteComponent
    }
}