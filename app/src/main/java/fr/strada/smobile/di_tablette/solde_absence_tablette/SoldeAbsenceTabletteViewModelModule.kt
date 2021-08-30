package fr.strada.smobile.di_tablette.solde_absence_tablette

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui_tablette.mes_absences_tablette.solde_absence_tablette.SoldeAbsenceTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class SoldeAbsenceTabletteViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SoldeAbsenceTabletteViewModel::class)
    abstract fun bindSoldeAbsenceTabletteViewModel(soldeAbsenceTabletteViewModel: SoldeAbsenceTabletteViewModel): ViewModel
}