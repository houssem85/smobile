package fr.strada.smobile.di.solde_absence

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.soldeabsence.SoldeAbsenceViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class SoldeAbsenceViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SoldeAbsenceViewModel::class)
    abstract fun bindSoldeAbsenceViewModel(soldeAbsenceViewModel: SoldeAbsenceViewModel): ViewModel
}