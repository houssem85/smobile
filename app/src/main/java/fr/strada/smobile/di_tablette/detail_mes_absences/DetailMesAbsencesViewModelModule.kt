package fr.strada.smobile.di_tablette.detail_mes_absences

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.DetailMesAbsencesViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class DetailMesAbsencesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailMesAbsencesViewModel::class)
    abstract fun bindDetailMesAbsencesViewModel(detailMesAbsencesViewModel: DetailMesAbsencesViewModel): ViewModel
}