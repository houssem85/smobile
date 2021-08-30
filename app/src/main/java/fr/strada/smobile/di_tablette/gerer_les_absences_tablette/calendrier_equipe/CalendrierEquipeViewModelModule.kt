package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.CalendrierEquipeViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class CalendrierEquipeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CalendrierEquipeViewModel::class)
    abstract fun bindCalendrierEquipeViewModel(calendrierEquipeViewModel: CalendrierEquipeViewModel): ViewModel
}