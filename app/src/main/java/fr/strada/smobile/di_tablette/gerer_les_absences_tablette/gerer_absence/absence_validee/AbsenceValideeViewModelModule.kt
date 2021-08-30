package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee.AbsenceValideeViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class AbsenceValideeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AbsenceValideeViewModel::class)
    abstract fun bindAbsenceValideeViewModel(absenceValideeViewModel: AbsenceValideeViewModel): ViewModel
}