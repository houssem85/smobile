package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.GererAbsenceFragment

@Subcomponent(modules = [GererAbsenceModule::class, GererAbsenceViewModelModule::class])
interface GererAbsenceComponent {

    fun inject(gererAbsenceFragment: GererAbsenceFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : GererAbsenceComponent
    }
}