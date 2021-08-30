package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_absents

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salarieabsent.SalarieAbsentAdapter

@Module
class SalariesAbsentsModule  {

    @Provides
    fun provideDemandeAbsenceAdapter(context: Context): SalarieAbsentAdapter {
        return  SalarieAbsentAdapter(context)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}