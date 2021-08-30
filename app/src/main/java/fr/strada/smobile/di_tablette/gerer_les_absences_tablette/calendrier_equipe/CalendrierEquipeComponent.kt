package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.CalendrierEquipeFragment

@Subcomponent(modules = [CalendrierEquipeModule::class, CalendrierEquipeViewModelModule::class])
interface CalendrierEquipeComponent {

    fun inject(calendrierEquipeFragment: CalendrierEquipeFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : CalendrierEquipeComponent
    }
}