package fr.strada.smobile.di.solde_absence

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.soldeabsence.SoldeAbsenceFragment

@Subcomponent(modules = [SoldeAbsenceModule::class,SoldeAbsenceViewModelModule::class])
interface SoldeAbsenceComponent{

    fun inject(soldeAbsenceFragment: SoldeAbsenceFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : SoldeAbsenceComponent
    }
}