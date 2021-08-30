package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee.AbsenceValideeAdapter

@Module
class AbsenceValideeModule  {

    @Provides
    fun provideAbsenceValideeAdapter(context: Context): AbsenceValideeAdapter {
        return  AbsenceValideeAdapter(context)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}