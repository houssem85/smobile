package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee.AbsenceValideeFragment

@Subcomponent(modules = [AbsenceValideeModule::class, AbsenceValideeViewModelModule::class])
interface AbsenceValideeComponent {

    fun inject(absenceValideeFragment: AbsenceValideeFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : AbsenceValideeComponent
    }
}