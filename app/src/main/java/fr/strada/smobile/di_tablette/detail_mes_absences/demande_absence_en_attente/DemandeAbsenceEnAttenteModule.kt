package fr.strada.smobile.di_tablette.detail_mes_absences.demande_absence_en_attente

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui.soldeabsence.SoldeAbsenceAdapter

@Module
class DemandeAbsenceEnAttenteModule  {

    @Provides
    fun provideSoldeAbsenceAdapter(context: Context): SoldeAbsenceAdapter {
        return  SoldeAbsenceAdapter(context)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}