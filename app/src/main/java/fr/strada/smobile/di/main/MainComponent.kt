package fr.strada.smobile.di.main

import android.app.Activity
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.di.absence_request.AbsenceRequestComponent
import fr.strada.smobile.di.card.CardComponent
import fr.strada.smobile.di.indemnites.IndemnityComponent
import fr.strada.smobile.di.mes_activities.MesActivitiesComponent
import fr.strada.smobile.di.messagerie.MessagerieComponent
import fr.strada.smobile.di.messagerie.boite_reception.BoiteReceptionComponent
import fr.strada.smobile.di.messagerie.message_predefini.MessagePredefiniComponent
import fr.strada.smobile.di.messagerie.nouveau_message.NouveauMessageComponent
import fr.strada.smobile.di.reglage.ReglageComponent
import fr.strada.smobile.di.solde_absence.SoldeAbsenceComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier.CalendrierEquipeComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.demande_absence.DemandesAbsencesComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_absents.SalariesAbsentsComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_presents.SalariesPresentsComponent
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.spi.ui.reglage.di.SettingsComponent
import fr.strada.smobile.utils.listner.OnClickListener

@Subcomponent(modules = [MainModule::class,MainViewModelModule::class])
interface MainComponent {

    fun inject(mainActivity: MainActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setOnClickListener(onClickListener: OnClickListener?) : Builder

        @BindsInstance
        fun setContext(activity: Activity) : Builder

        fun build() : MainComponent
    }

    fun mesActivitiesComponent() : MesActivitiesComponent.Builder
    fun indemnityComponent(): IndemnityComponent.Builder
    fun messagerieComponent(): MessagerieComponent.Builder
    fun boiteReceptionComponent(): BoiteReceptionComponent.Builder
    fun messagePredefiniComponent(): MessagePredefiniComponent.Builder
    fun nouveauMessageComponent(): NouveauMessageComponent.Builder
    fun calendrierEquipeComponent(): CalendrierEquipeComponent.Builder
    fun demandeAbsenceComponent(): DemandesAbsencesComponent.Builder
    fun salarieAbsentComponent(): SalariesAbsentsComponent.Builder
    fun salariePresentComponent(): SalariesPresentsComponent.Builder
    fun soldeAbsenceComponent():SoldeAbsenceComponent.Builder
    fun absenceRequestComponent(): AbsenceRequestComponent.Builder
    fun reglageComponentComponent(): ReglageComponent.Builder
    fun settingsComponent(): SettingsComponent.Builder
    fun cardComponent(): CardComponent.Builder
}