package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.GererAbsenceViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class GererAbsenceViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GererAbsenceViewModel::class)
    abstract fun bindGererAbsenceViewModel(gererAbsenceViewModel: GererAbsenceViewModel): ViewModel
}