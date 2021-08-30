package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_presents

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salariepresent.SalariePresentAdapter

@Module
class SalariesPresentsModule  {

    @Provides
    fun provideDemandeAbsenceAdapter(context: Context): SalariePresentAdapter {
        return  SalariePresentAdapter(context)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}