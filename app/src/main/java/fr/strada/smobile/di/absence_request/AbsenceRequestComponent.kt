package fr.strada.smobile.di.absence_request

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.demande_absence.AbsenceRequestFragment

@Subcomponent(modules = [AbsenceRequestModule::class, AbsenceRequestViewModelModule::class])
interface AbsenceRequestComponent {

    fun inject(absenceRequestFragment: AbsenceRequestFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : AbsenceRequestComponent
    }
}