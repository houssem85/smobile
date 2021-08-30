package fr.strada.smobile.di_tablette.detail_mes_absences.demande_absence_en_attente

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.demande_absence_en_attente.DemandeAbsenceEnAttenteFragment

@Subcomponent(modules = [DemandeAbsenceEnAttenteModule::class, DemandeAbsenceEnAttenteViewModelModule::class])
interface DemandeAbsenceEnAttenteComponent {

    fun inject(demandeAbsenceEnAttenteFragment: DemandeAbsenceEnAttenteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext( context: Context) : Builder

        fun build() : DemandeAbsenceEnAttenteComponent
    }
}