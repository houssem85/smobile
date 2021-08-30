package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.demande_absence

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.demande_absence.DemandeAbsenceAdapter
import fr.strada.smobile.utils.listner.ItemDemandeAbsenceListner

@Module
class DemandesAbsencesModule  {

    @Provides
    fun provideDemandeAbsenceAdapter(context: Context, itemDemandeAbsenceListner: ItemDemandeAbsenceListner): DemandeAbsenceAdapter {
        return  DemandeAbsenceAdapter(context, itemDemandeAbsenceListner)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}