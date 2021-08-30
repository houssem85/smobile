package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider.AbsenceAValiderViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class AbsenceAValiderViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AbsenceAValiderViewModel::class)
    abstract fun bindAbsenceAValiderViewModel(absenceAValiderViewModel: AbsenceAValiderViewModel): ViewModel
}