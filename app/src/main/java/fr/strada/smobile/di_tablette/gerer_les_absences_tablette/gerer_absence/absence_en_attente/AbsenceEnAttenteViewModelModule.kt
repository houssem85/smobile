package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente.AbsenceEnAttenteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class AbsenceEnAttenteViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AbsenceEnAttenteViewModel::class)
    abstract fun bindAbsenceEnAttenteViewModel(absenceEnAttenteViewModel: AbsenceEnAttenteViewModel): ViewModel
}