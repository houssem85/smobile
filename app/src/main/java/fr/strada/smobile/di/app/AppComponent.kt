package fr.strada.smobile.di.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.di.auth.AuthBorneComponent
import fr.strada.smobile.di.auth.AuthComponent
import fr.strada.smobile.di.auth.ResetPasswordComponent
import fr.strada.smobile.di.auth.TenantActivityComponent
import fr.strada.smobile.di.graphical_view.GraphicalViewComponent
import fr.strada.smobile.di.infractions.detail_infraction.DetailInfractionComponent
import fr.strada.smobile.di.main.MainComponent
import fr.strada.smobile.di.messagerie.detailMessagePredefini.DetailMessagePredefiniComponent
import fr.strada.smobile.di.notifications.NotificationsComponent
import fr.strada.smobile.di.splash_screen.SplashComponent
import fr.strada.smobile.di.widget.WidgetModule
import fr.strada.smobile.di.woker.WorkerModule
import fr.strada.smobile.di_tablette.main_tablette.MainTabletteComponent
import fr.strada.smobile.ui.apropos.AProposFragment
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.ui.card.ReaderActivityKotlin
import fr.strada.smobile.ui.home.HomeFragment
import fr.strada.smobile.ui.infractions.InfractionCategorieFragment
import fr.strada.smobile.ui.infractions.InfractionsFragment
import fr.strada.smobile.ui.infractions.filtre.FiltreFragment
import fr.strada.smobile.ui.mes_frais.MesFraisFragment
import fr.strada.smobile.ui.mes_frais.detail_demande_envoyee.DetailDemandeEnvoyeeFragment
import fr.strada.smobile.ui.mes_frais.detail_demande_non_envoyee.DetailDemandeNonEnvoyeeFragment
import fr.strada.smobile.ui.mes_frais.detail_depense.DetailDepenseFragment
import fr.strada.smobile.ui.mes_frais.detail_depense_envoyee.DetailDepenseEnvoyeeFragment
import fr.strada.smobile.ui.mes_frais.filtre.FiltreMesFraisFragment
import fr.strada.smobile.ui.mes_frais.nouvelle_demande.NouvelleDemandeFragment
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.NouvelleDepenseFragment
import fr.strada.smobile.ui.pointeuse.PointeuseFragment
import fr.strada.smobile.ui.pointeuse.dialog_type_activitie_pointeuse.DialogSelectTypeActivitiePointeuse
import fr.strada.smobile.ui.pointeuse.jour_activitie.JourActivitieFragment
import fr.strada.smobile.ui.pointeuse_graph.PointeuseGraphActivity
import fr.strada.smobile.ui.profil.ProfilFragment
import fr.strada.smobile.ui.spi.DialogProfilFragment
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.ui.spi.tracker.RunFragment
import fr.strada.smobile.ui.spi.tracker.TrackingFragment
import fr.strada.smobile.ui.spi.ui.doc.AjoutDocFragment
import fr.strada.smobile.ui.spi.ui.doc.DocFragment
import fr.strada.smobile.ui.spi.ui.home.HomeSpiFragment
import fr.strada.smobile.ui.spi.ui.menu.MenuFragment
import fr.strada.smobile.ui.spi.ui.reglage.SettingsFragment
import fr.strada.smobile.ui.spi.ui.tms.DetailTourneeFragment
import fr.strada.smobile.ui.spi.ui.tms.ListNotificationFragment
import fr.strada.smobile.ui.spi.ui.tms.ListTournerFragment
import fr.strada.smobile.ui.spi.ui.tms.TmsActivity
import fr.strada.smobile.ui.spi.ui.tms.information.*
import fr.strada.smobile.ui_tablette.infractions_tablette.InfractionsTabletteFragment
import fr.strada.smobile.ui_tablette.mes_frais_tablette.MesFraisTabletteFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelFactoryModule::class, ViewModelsModule::class, WorkerFactoryModule::class, WorkerModule::class, WidgetModule::class, AndroidInjectionModule::class, ServiceBuilderModule::class])
interface AppComponent {

    fun inject(sMobile: SmobileApp)

    fun inject(jourActivitieFragment: JourActivitieFragment)

    fun inject(baseActivity: BaseActivity)

    fun inject(PointeuseFragment: PointeuseFragment)

    fun inject(readerActivityKotlin: ReaderActivityKotlin)

    fun inject(pointeuseGraphActivity: PointeuseGraphActivity)

    fun inject(infractionsFragment: InfractionsFragment)

    fun inject(infractionCategorieFragment: InfractionCategorieFragment)

    fun inject(filtreFragment: FiltreFragment)

    fun inject(infractionsTabletteFragment: InfractionsTabletteFragment)

    fun inject(mesFraisFragment: MesFraisFragment)

    fun inject(filtreMesFraisFragment: FiltreMesFraisFragment)

    fun inject(nouvelleDemandeFragment: NouvelleDemandeFragment)

    fun inject(detailDemandeEnvoyeeFragment: DetailDemandeEnvoyeeFragment)

    fun inject(detailDepenseEnvoyeeFragment: DetailDepenseEnvoyeeFragment)

    fun inject(detailDemandeNonEnvoyeeFragment: DetailDemandeNonEnvoyeeFragment)

    fun inject(nouvelleDepenseFragment: NouvelleDepenseFragment)

    fun inject(mesFraisTabletteFragment: MesFraisTabletteFragment)

    fun inject(detailDepenseFragment: DetailDepenseFragment)

    fun inject(dialogSelectTypeActivitiePointeuse: DialogSelectTypeActivitiePointeuse)

    fun inject(on: DialogProfilFragment)

    fun inject(on: ProfilFragment)

    fun inject(on: MainActivitySpi)
    fun inject(on: SettingsFragment)

    fun inject(on: MenuFragment)
    fun inject(on: AProposFragment)
    fun inject(on: HomeSpiFragment)
    fun inject(on: DocFragment)
    fun inject(on: AjoutDocFragment)

    fun inject(on: HomeFragment)
    fun inject(runFragment: RunFragment)
    fun inject(trackingFragment: TrackingFragment)
    //fun inject(trackingService: TrackingService)

    fun inject (on : TmsActivity)
    fun inject (on : DestinaireFragment)
    fun inject (on : ExpiditeurFragment)
    fun inject (on : MainInformationFragment)
    fun inject (on : MarchandiseFragment)
    fun inject (on : PaletteFragment)
    fun inject (on : TarifFragment)
    fun inject (on : DetailTourneeFragment)
    fun inject (on : ListNotificationFragment)
    fun inject (on : ListTournerFragment)


    @Component.Builder
    interface Builder {

        fun appModule(appModule: AppModule): Builder

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun mainComponent(): MainComponent.Builder
    fun mainTabletteComponent(): MainTabletteComponent.Builder
    fun splashComponent(): SplashComponent.Builder
    fun authComponent(): AuthComponent.Builder
    fun tenantComponent(): TenantActivityComponent.Builder
    fun graphicalViewComponent(): GraphicalViewComponent.Builder
    fun detailInfractionComponent(): DetailInfractionComponent.Builder
    fun detailMessagePredefiniComponent(): DetailMessagePredefiniComponent.Builder
    fun authBorneComponent(): AuthBorneComponent.Builder
    fun resetPasswordComponent(): ResetPasswordComponent.Builder
    fun notificationsComponent(): NotificationsComponent.Builder

}