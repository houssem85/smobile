package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_absents

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salarieabsent.SalarieAbsentFragment

@Subcomponent(modules = [SalariesAbsentsModule::class, SalariesAbsentsViewModelModule::class])
interface SalariesAbsentsComponent {

    fun inject(salarieAbsentFragment: SalarieAbsentFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : SalariesAbsentsComponent
    }
}