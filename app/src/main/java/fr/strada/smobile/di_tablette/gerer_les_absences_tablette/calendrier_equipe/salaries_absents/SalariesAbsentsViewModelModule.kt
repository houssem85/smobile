package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_absents

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salarieabsent.SalarieAbsentViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class SalariesAbsentsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SalarieAbsentViewModel::class)
    abstract fun bindSalarieAbsentViewModel(salarieAbsentViewModel: SalarieAbsentViewModel): ViewModel
}