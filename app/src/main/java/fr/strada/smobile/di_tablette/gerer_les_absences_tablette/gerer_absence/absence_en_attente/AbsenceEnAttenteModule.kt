package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente.AbsenceEnAttenteAdapter

@Module
class AbsenceEnAttenteModule  {

    @Provides
    fun provideAbsenceEnAttenteAdapter(context: Context): AbsenceEnAttenteAdapter {
        return  AbsenceEnAttenteAdapter(context)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}