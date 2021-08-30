package fr.strada.smobile.di_tablette.main_tablette

import android.app.Activity
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.di.absence_request.AbsenceRequestComponent
import fr.strada.smobile.di.indemnites.IndemnityComponent
import fr.strada.smobile.di.mes_activities.MesActivitiesComponent
import fr.strada.smobile.di.mes_frais.detail_demande_non_envoyee.DetailDemandeNonEnvoyeeComponent
import fr.strada.smobile.di.mes_frais.detail_depense.DetailDepenseComponent
import fr.strada.smobile.di.mes_frais.detail_depense_envoyee.DetailDepenseEnvoyeeComponent
import fr.strada.smobile.di.messagerie.nouveau_message.NouveauMessageComponent
import fr.strada.smobile.di.reglage.ReglageComponent
import fr.strada.smobile.di.solde_absence.SoldeAbsenceComponent
import fr.strada.smobile.di_tablette.accueil_tablette.AccueilTabletteComponent
import fr.strada.smobile.di_tablette.accueil_tablette.statistique.StatistiqueComponent
import fr.strada.smobile.di_tablette.detail_mes_absences.DetailMesAbsencesComponent
import fr.strada.smobile.di_tablette.detail_mes_absences.demande_absence_en_attente.DemandeAbsenceEnAttenteComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.GererLesAbsencesTabletteComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier.CalendrierEquipeComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.demande_absence.DemandesAbsencesComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_absents.SalariesAbsentsComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_presents.SalariesPresentsComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.detail_demande_absence.DetailDemandeAbsenceComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.GererAbsenceComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider.AbsenceAValiderComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente.AbsenceEnAttenteComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee.AbsenceRefuseeComponent
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee.AbsenceValideeComponent
import fr.strada.smobile.di_tablette.mes_absences_tablette.MesAbsencesTabletteComponent
import fr.strada.smobile.di_tablette.messagerie.MessagerieTabletteComponent
import fr.strada.smobile.di_tablette.messagerie.boite_reception.BoiteReceptionTabletteComponent
import fr.strada.smobile.di_tablette.messagerie.conversation.ConversationComponent
import fr.strada.smobile.di_tablette.messagerie.message_predifinie.MessagePredefinieTabletteComponent
import fr.strada.smobile.di_tablette.messagerie.nouveau_message_predifinie.NouveauMessagePredifinieTabletteComponent
import fr.strada.smobile.di_tablette.messagerie.update_message_predifinie.UpdateMessagePredefinieComponent
import fr.strada.smobile.di_tablette.solde_absence_tablette.SoldeAbsenceTabletteComponent
import fr.strada.smobile.ui.spi.ui.reglage.di.SettingsComponent
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.listner.OnClickListener

@Subcomponent(modules = [MainTabletteModule::class, MainTabletteViewModelModule::class])
interface MainTabletteComponent {

    fun inject(mainTabletteActivity: MainTabletteActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setOnClickListener(onClickListener: OnClickListener) : Builder

        @BindsInstance
        fun setContext(activity: Activity) : Builder

        fun build() : MainTabletteComponent
    }

    fun accueilTabletteComponent(): AccueilTabletteComponent.Builder
    fun statistiqueComponent(): StatistiqueComponent.Builder
    fun mesActivitiesComponent() : MesActivitiesComponent.Builder
    fun gererLesAbsencesComponent(): GererLesAbsencesTabletteComponent.Builder
    fun gererAbsenceComponent(): GererAbsenceComponent.Builder
    fun absenceAValiderComponent(): AbsenceAValiderComponent.Builder
    fun absenceEnAttenteComponent(): AbsenceEnAttenteComponent.Builder
    fun absenceValideeComponent(): AbsenceValideeComponent.Builder
    fun absenceRefuseeComponent(): AbsenceRefuseeComponent.Builder
    fun calendrierEquipeComponent(): CalendrierEquipeComponent.Builder
    fun demandeAbsenceComponent(): DemandesAbsencesComponent.Builder
    fun salarieAbsentComponent(): SalariesAbsentsComponent.Builder
    fun salariePresentComponent(): SalariesPresentsComponent.Builder
    fun soldeAbsenceTabletteComponent(): SoldeAbsenceTabletteComponent.Builder
    fun detailMesAbsencesComponent(): DetailMesAbsencesComponent.Builder
    fun mesAbsencesTabletteComponent(): MesAbsencesTabletteComponent.Builder
    fun soldeAbsenceComponent(): SoldeAbsenceComponent.Builder
    fun demandeAbsenceEnAttenteComponent(): DemandeAbsenceEnAttenteComponent.Builder
    fun detailDemandeAbsenceComponent(): DetailDemandeAbsenceComponent.Builder
    fun absenceRequestComponent(): AbsenceRequestComponent.Builder
    fun indemnityComponent(): IndemnityComponent.Builder
    fun detailDemandeNonEnvoyeeComponent(): DetailDemandeNonEnvoyeeComponent.Builder
    fun detailDepenseComponent(): DetailDepenseComponent.Builder
    fun detailDepenseEnvoyeeComponent(): DetailDepenseEnvoyeeComponent.Builder
    fun messagerieTabletteComponent(): MessagerieTabletteComponent.Builder
    fun boiteReceptionTabletteComponent(): BoiteReceptionTabletteComponent.Builder
    fun conversationComponent(): ConversationComponent.Builder
    fun nouveauMessageComponent(): NouveauMessageComponent.Builder
    fun messagePredefinieTabletteComponent(): MessagePredefinieTabletteComponent.Builder
    fun nouveauMessagePredifinieTabletteComponent(): NouveauMessagePredifinieTabletteComponent.Builder
    fun updateMessagePredefinieComponent(): UpdateMessagePredefinieComponent.Builder
    fun reglageComponentComponent(): ReglageComponent.Builder
    fun settingsComponent(): SettingsComponent.Builder

}
