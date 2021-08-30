package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.demande_absence

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.demande_absence.DemandeAbsenceFragment
import fr.strada.smobile.utils.listner.ItemDemandeAbsenceListner

@Subcomponent(modules = [DemandesAbsencesModule::class, DemandesAbsencesViewModelModule::class])
interface DemandesAbsencesComponent {

    fun inject(demandeAbsenceFragment: DemandeAbsenceFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        @BindsInstance
        fun setDetailDemandeAbsenceListner(itemDemandeAbsenceListner: ItemDemandeAbsenceListner) : DemandesAbsencesComponent.Builder

        fun build() : DemandesAbsencesComponent
    }
}