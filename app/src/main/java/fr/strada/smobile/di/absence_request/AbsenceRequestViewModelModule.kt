package fr.strada.smobile.di.absence_request

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.demande_absence.AbsenceRequestViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class AbsenceRequestViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AbsenceRequestViewModel::class)
    abstract fun bindAbsenceRequestViewModel(absenceRequestViewModel: AbsenceRequestViewModel): ViewModel
}