package fr.strada.smobile.di.mes_activities

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.activities.MesActivitiesFragment
import fr.strada.smobile.ui.activities.hebdomadaire.MesActivitiesHebdomadaireFragment
import fr.strada.smobile.ui.activities.journalier.MyDailyActivitiesFragment
import fr.strada.smobile.ui.activities.mensuel.MesActivitiesMensuelFragment
import fr.strada.smobile.utils.listner.DialogChangeMonthListner
import fr.strada.smobile.utils.listner.DialogChangeWeekListner
import fr.strada.smobile.utils.listner.DialogChangeYearListner

@Subcomponent(modules =[MesActivitiesModule::class,MesActivitiesViewModelModule::class])
interface MesActivitiesComponent {

    fun injectMesActivitiesMensuelFragment(mesActivitiesMensuelFragment: MesActivitiesMensuelFragment)

    fun injectMesActivitiesHebdomadaireFragment(mesActivitiesHebdomadaireFragment: MesActivitiesHebdomadaireFragment)

    fun injectActivities(mesActivitiesFragment: MesActivitiesFragment)

    fun injectMyDailyActivitiesFragment(myDailyActivitiesFragment : MyDailyActivitiesFragment)

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

        fun build() : MesActivitiesComponent
    }
}