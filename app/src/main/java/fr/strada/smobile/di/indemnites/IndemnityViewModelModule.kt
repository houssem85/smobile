package fr.strada.smobile.di.indemnites

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.indemnites.IndemniteViewModel
import fr.strada.smobile.ui.indemnites.hebdomadaire.IndemniteHebdomadaireViewModel
import fr.strada.smobile.ui.indemnites.journalier.IndemniteJournalierViewModel
import fr.strada.smobile.ui.indemnites.mensuel.IndemniteMensuelleViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class IndemnityViewModelModule  {

    @Binds
    @IntoMap
    @ViewModelKey(IndemniteMensuelleViewModel::class)
    abstract fun bindIndemniteMensuelleViewModel(IndemniteMensuelleViewModel: IndemniteMensuelleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IndemniteHebdomadaireViewModel::class)
    abstract fun bindIndemniteHebdomadaireViewModel(indemniteHebdomadaireViewModel: IndemniteHebdomadaireViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(IndemniteJournalierViewModel::class)
    abstract fun bindIndemniteJournalierViewModel(indemniteJournalierViewModel: IndemniteJournalierViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IndemniteViewModel::class)
    abstract fun bindIndemniteViewModel(indemniteViewModel: IndemniteViewModel): ViewModel
}