package fr.strada.smobile.di.graphical_view

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides

@Module
class GraphicalViewModule {

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}