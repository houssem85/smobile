package fr.strada.smobile.di.mes_activities

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.activities.MesActivitiesViewModel
import fr.strada.smobile.ui.activities.hebdomadaire.MesActivitiesHebdomadaireViewModel
import fr.strada.smobile.ui.activities.journalier.MyDailyActivitiesViewModel
import fr.strada.smobile.ui.activities.mensuel.MesActivitiesMensuelViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class MesActivitiesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MesActivitiesMensuelViewModel::class)
    abstract fun bindMesActivitiesMensuelViewModel(mesActivitiesMensuelViewModel: MesActivitiesMensuelViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MesActivitiesHebdomadaireViewModel::class)
    abstract fun bindMesActivitiesHebdomadaireViewModel(mesActivitiesHebdomadaireViewModel: MesActivitiesHebdomadaireViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MesActivitiesViewModel::class)
    abstract fun bindMesActivitiesViewModel(mesActivitiesViewModel: MesActivitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyDailyActivitiesViewModel::class)
    abstract fun bindMyDailyActivitiesViewModel(myDailyActivitiesViewModel: MyDailyActivitiesViewModel): ViewModel
}