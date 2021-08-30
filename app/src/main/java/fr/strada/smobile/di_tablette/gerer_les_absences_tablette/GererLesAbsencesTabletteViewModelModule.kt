package fr.strada.smobile.di_tablette.gerer_les_absences_tablette

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.GererLesAbsencesTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class GererLesAbsencesTabletteViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GererLesAbsencesTabletteViewModel::class)
    abstract fun bindGererLesAbsencesTabletteViewModel(gererLesAbsencesTabletteViewModel: GererLesAbsencesTabletteViewModel): ViewModel
}