package fr.strada.smobile.di_tablette.detail_mes_absences

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.DetailMesAbsencesFragment

@Subcomponent(modules = [DetailMesAbsencesModule::class, DetailMesAbsencesViewModelModule::class])
interface DetailMesAbsencesComponent {

    fun inject(detailMesAbsencesFragment: DetailMesAbsencesFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext( context: Context) : Builder

        fun build() : DetailMesAbsencesComponent
    }
}