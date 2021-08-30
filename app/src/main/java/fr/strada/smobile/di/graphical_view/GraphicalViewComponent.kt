package fr.strada.smobile.di.graphical_view

import android.app.Activity
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.activities.journalier.GraphicalViewActivity

@Subcomponent(modules = [GraphicalViewModule::class,GraphicalViewViewModelModule::class])
interface GraphicalViewComponent {

    fun inject(graphicalViewActivity : GraphicalViewActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(activity: Activity) : Builder

        fun build() : GraphicalViewComponent
    }
}