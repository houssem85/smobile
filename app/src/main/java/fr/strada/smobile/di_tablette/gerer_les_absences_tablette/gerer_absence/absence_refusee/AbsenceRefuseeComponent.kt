package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee.AbsenceRefuseeFragment

@Subcomponent(modules = [AbsenceRefuseeModule::class, AbsenceRefuseeViewModelModule::class])
interface AbsenceRefuseeComponent {

    fun inject(absenceRefuseeFragment: AbsenceRefuseeFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : AbsenceRefuseeComponent
    }
}