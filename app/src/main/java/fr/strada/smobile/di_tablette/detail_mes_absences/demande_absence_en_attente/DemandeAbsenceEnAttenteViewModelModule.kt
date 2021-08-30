package fr.strada.smobile.di_tablette.detail_mes_absences.demande_absence_en_attente

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.demande_absence_en_attente.DemandeAbsenceEnAttenteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class DemandeAbsenceEnAttenteViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DemandeAbsenceEnAttenteViewModel::class)
    abstract fun bindDemandeAbsenceEnAttenteViewModel(demandeAbsenceEnAttenteViewModel: DemandeAbsenceEnAttenteViewModel): ViewModel
}