package fr.strada.smobile.di_tablette.gerer_les_absences_tablette.detail_demande_absence

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.detail_demande_absence.DetailDemandeAbsenceFragment

@Subcomponent(modules = [DetailDemandeAbsenceModule::class, DetailDemandeAbsenceViewModelModule::class])
interface DetailDemandeAbsenceComponent {

    fun inject(detailDemandeAbsenceFragment: DetailDemandeAbsenceFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : DetailDemandeAbsenceComponent
    }
}