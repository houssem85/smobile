package fr.strada.smobile.di.app

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.apropos.AProposViewModel
import fr.strada.smobile.ui.home.HomeViewModel
import fr.strada.smobile.ui.infractions.InfractionsViewModel
import fr.strada.smobile.ui.infractions.filtre.FiltreViewModel
import fr.strada.smobile.ui.mes_frais.MesFraisViewModel
import fr.strada.smobile.ui.mes_frais.SharedMesFraisViewModel
import fr.strada.smobile.ui.mes_frais.detail_demande_envoyee.DetailDemandeEnvoyeeViewModel
import fr.strada.smobile.ui.mes_frais.detail_demande_non_envoyee.DetailDemandeNonEnvoyeeViewModel
import fr.strada.smobile.ui.mes_frais.detail_depense.DetailDepenseViewModel
import fr.strada.smobile.ui.mes_frais.detail_depense_envoyee.DetailDepenseEnvoyeeViewModel
import fr.strada.smobile.ui.mes_frais.filtre.FiltreMesFraisViewModel
import fr.strada.smobile.ui.mes_frais.nouvelle_demande.NouvelleDemandeViewModel
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.NouvelleDepenseViewModel
import fr.strada.smobile.ui.pointeuse.PointeuseViewModel
import fr.strada.smobile.ui.pointeuse.jour_activitie.JourActivitieViewModel
import fr.strada.smobile.ui.pointeuse_graph.PointeuseGraphViewModel
import fr.strada.smobile.ui.profil.ProfilViewModel
import fr.strada.smobile.ui.spi.MainActivitySpiViewModel
import fr.strada.smobile.ui.spi.ui.doc.DocViewModel
import fr.strada.smobile.ui.spi.ui.reglage.SettingsViewModel
import fr.strada.smobile.ui.spi.ui.tms.TmsActivityViewModel
import fr.strada.smobile.ui_tablette.mes_frais_tablette.MesFraisTabletteViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(PointeuseViewModel::class)
    abstract fun bindPointeuseViewModel(viewModel: PointeuseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JourActivitieViewModel::class)
    abstract fun bindJourActivitieViewModel(viewModel: JourActivitieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PointeuseGraphViewModel::class)
    abstract fun bindPointeuseGraphViewModel(viewModel: PointeuseGraphViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InfractionsViewModel::class)
    abstract fun bindInfractionsViewModel(viewModel: InfractionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FiltreViewModel::class)
    abstract fun bindFiltreViewModel(viewModel: FiltreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MesFraisViewModel::class)
    abstract fun bindMesFraisViewModel(viewModel: MesFraisViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FiltreMesFraisViewModel::class)
    abstract fun bindFiltreMesFraisViewModel(viewModel: FiltreMesFraisViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NouvelleDepenseViewModel::class)
    abstract fun bindNouvelleDepenseViewModel(nouvelleDepenseViewModel: NouvelleDepenseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NouvelleDemandeViewModel::class)
    abstract fun bindNouvelleDemandeViewModel(nouvelleDemandeViewModel: NouvelleDemandeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedMesFraisViewModel::class)
    abstract fun bindSharedMesFraisViewModel(viewModel: SharedMesFraisViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailDemandeEnvoyeeViewModel::class)
    abstract fun bindDetailDemandeEnvoyeeViewModel(detailDemandeEnvoyeeViewModel: DetailDemandeEnvoyeeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailDepenseEnvoyeeViewModel::class)
    abstract fun bindMesDetailDepenseEnvoyeeViewModel(detailDepenseEnvoyeeViewModel: DetailDepenseEnvoyeeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(DetailDemandeNonEnvoyeeViewModel::class)
    abstract fun bindDetailDemandeNonEnvoyeeViewModel(detailDemandeNonEnvoyeeViewModel: DetailDemandeNonEnvoyeeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MesFraisTabletteViewModel::class)
    abstract fun bindMesFraisTabletteFragmentViewModel(viewModel: MesFraisTabletteViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(DetailDepenseViewModel::class)
    abstract fun bindDetailDepenseViewModel(detailDepenseViewModel: DetailDepenseViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ProfilViewModel::class)
    abstract fun bindProfilViewModel(profilViewModel: ProfilViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivitySpiViewModel::class)
    abstract fun bindMainActivitySpiViewModel(mainActivitySpiViewModel: MainActivitySpiViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AProposViewModel::class)
    abstract fun bindAProposViewModel(aProposViewModel: AProposViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(DocViewModel::class)
    abstract fun bindajoutdocViewModel(docViewModel: DocViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TmsActivityViewModel::class)
    abstract fun bindtmsActivityViewModelViewModel(tmsActivityViewModel: TmsActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindsettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel
}