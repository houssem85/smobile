package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_presents

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salariepresent.SalariePresentFragment

@Subcomponent(modules = [SalariesPresentsModule::class, SalariesPresentsViewModelModule::class])
interface SalariesPresentsComponent {

    fun inject(salariePresentFragment: SalariePresentFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : SalariesPresentsComponent
    }
}