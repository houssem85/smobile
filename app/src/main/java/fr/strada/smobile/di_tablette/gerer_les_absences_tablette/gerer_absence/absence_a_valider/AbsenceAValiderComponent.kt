package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider.AbsenceAValiderFragment
import fr.strada.smobile.utils.listner.ItemAbsenceAValiderLisnter

@Subcomponent(modules = [AbsenceAValiderModule::class, AbsenceAValiderViewModelModule::class])
interface AbsenceAValiderComponent {

    fun inject(absenceAValiderFragment: AbsenceAValiderFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setItemAbsenceAValiderLisnter(itemAbsenceAValiderLisnter: ItemAbsenceAValiderLisnter) : Builder

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : AbsenceAValiderComponent
    }
}