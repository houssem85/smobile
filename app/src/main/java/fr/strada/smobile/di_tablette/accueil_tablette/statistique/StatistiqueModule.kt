package fr.strada.smobile.di_tablette.accueil_tablette.statistique

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui_tablette.accueil.statistique.ListeAbsenceAdapter
import fr.strada.smobile.utils.TopSpacingItemDecoration

@Module
class StatistiqueModule {

    @Provides
    fun provideAbsenceAdapter(activity: Activity): ListeAbsenceAdapter {
        return  ListeAbsenceAdapter(activity)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    @Provides
    fun provideTopSpacingDecorator(activity: Activity): TopSpacingItemDecoration{
        return TopSpacingItemDecoration(10, 0, 0, 10)
    }

}