package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_presents

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salariepresent.SalariePresentViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class SalariesPresentsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SalariePresentViewModel::class)
    abstract fun bindSalariePresentViewModel(salariePresentViewModel: SalariePresentViewModel): ViewModel
}