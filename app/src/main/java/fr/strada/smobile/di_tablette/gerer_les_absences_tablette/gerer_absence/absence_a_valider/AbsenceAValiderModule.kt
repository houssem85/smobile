package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider.AbsenceAValiderAdapter
import fr.strada.smobile.utils.listner.ItemAbsenceAValiderLisnter

@Module
class AbsenceAValiderModule  {

    @Provides
    fun provideAbsenceAValiderAdapter(context: Context,itemAbsenceAValiderLisnter: ItemAbsenceAValiderLisnter): AbsenceAValiderAdapter {
        return  AbsenceAValiderAdapter(context,itemAbsenceAValiderLisnter)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}