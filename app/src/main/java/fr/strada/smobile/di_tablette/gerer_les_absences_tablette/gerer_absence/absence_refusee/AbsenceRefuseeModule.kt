package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee.AbsenceRefuseeAdapter

@Module
class AbsenceRefuseeModule  {

    @Provides
    fun provideAbsenceRefuseeAdapter(context: Context): AbsenceRefuseeAdapter {
        return  AbsenceRefuseeAdapter(context)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}