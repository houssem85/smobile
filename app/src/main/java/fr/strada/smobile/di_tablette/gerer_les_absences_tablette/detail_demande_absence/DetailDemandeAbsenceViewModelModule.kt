package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.detail_demande_absence

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.detail_demande_absence.DetailDemandeAbsenceViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class DetailDemandeAbsenceViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailDemandeAbsenceViewModel::class)
    abstract fun bindDetailDemandeAbsenceViewModel(detailDemandeAbsenceViewModel: DetailDemandeAbsenceViewModel): ViewModel
}