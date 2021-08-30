package fr.strada.smobile.di.indemnites

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.indemnites.IndemnitesFragment
import fr.strada.smobile.ui.indemnites.hebdomadaire.IndemniteHebdomadaireFragment
import fr.strada.smobile.ui.indemnites.journalier.IndemniteJournalierFragment
import fr.strada.smobile.ui.indemnites.mensuel.IndemniteMensuelleFragment
import fr.strada.smobile.utils.listner.DialogChangeMonthListner
import fr.strada.smobile.utils.listner.DialogChangeWeekListner
import fr.strada.smobile.utils.listner.DialogChangeYearListner


@Subcomponent(modules =[IndemnityModule::class, IndemnityViewModelModule::class])
interface IndemnityComponent {

    fun injectIndemnitesMensuelFragment(indemniteMensuelleFragment: IndemniteMensuelleFragment)
    fun injectIndemnitesHebdomadaireFragment(indemniteHebdomadaireFragment: IndemniteHebdomadaireFragment)
    fun injectIndemnitesJournalier(indemnitesjournalierFragment: IndemniteJournalierFragment)
    fun injectIndemnites(indemnitesFragment: IndemnitesFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setDialogChangeYearListner(dialogChangeYearListner: DialogChangeYearListner?) : Builder

        @BindsInstance
        fun setDialogChangeMonthListner(dialogChangeMonthListner: DialogChangeMonthListner?) : Builder

        @BindsInstance
        fun setDialogChangeWeekListner(dialogChangeWeekListner: DialogChangeWeekListner?) : Builder

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : IndemnityComponent
    }


}