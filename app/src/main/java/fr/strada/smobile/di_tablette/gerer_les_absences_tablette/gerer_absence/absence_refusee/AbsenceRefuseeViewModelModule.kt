package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee.AbsenceRefuseeViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class AbsenceRefuseeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AbsenceRefuseeViewModel::class)
    abstract fun bindAbsenceRefuseeViewModel(absenceRefuseeViewModel: AbsenceRefuseeViewModel): ViewModel
}