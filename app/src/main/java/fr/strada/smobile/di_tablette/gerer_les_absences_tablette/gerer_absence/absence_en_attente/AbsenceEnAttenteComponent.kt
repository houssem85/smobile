package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente.AbsenceEnAttenteFragment

@Subcomponent(modules = [AbsenceEnAttenteModule::class, AbsenceEnAttenteViewModelModule::class])
interface AbsenceEnAttenteComponent {

    fun inject(absenceEnAttenteFragment: AbsenceEnAttenteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : AbsenceEnAttenteComponent
    }
}