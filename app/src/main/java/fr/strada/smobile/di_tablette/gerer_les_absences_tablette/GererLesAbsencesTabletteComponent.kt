package fr.strada.smobile.di_tablette.gerer_les_absences_tablette

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.GererLesAbsencesTabletteFragment

@Subcomponent(modules = [GererLesAbsencesTabletteModule::class, GererLesAbsencesTabletteViewModelModule::class])
interface GererLesAbsencesTabletteComponent {

    fun inject(gererLesAbsencesTabletteFragment: GererLesAbsencesTabletteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : GererLesAbsencesTabletteComponent
    }
}