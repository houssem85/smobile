package fr.strada.smobile.di_tablette.mes_absences_tablette

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.mes_absences_tablette.MesAbsencesTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class MesAbsencesTabletteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MesAbsencesTabletteViewModel::class)
    abstract fun bindMesAbsencesTabletteViewModel(mesAbsencesTabletteViewModel: MesAbsencesTabletteViewModel): ViewModel
}