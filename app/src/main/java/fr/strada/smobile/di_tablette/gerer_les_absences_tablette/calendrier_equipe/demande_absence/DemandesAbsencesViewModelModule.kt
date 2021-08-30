package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.demande_absence

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.demande_absence.DemandeAbsenceViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class DemandesAbsencesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DemandeAbsenceViewModel::class)
    abstract fun bindDemandeAbsenceViewModel(demandeAbsenceViewModel: DemandeAbsenceViewModel): ViewModel
}