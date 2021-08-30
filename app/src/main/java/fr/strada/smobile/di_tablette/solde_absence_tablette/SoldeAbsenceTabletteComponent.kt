package fr.strada.smobile.di_tablette.solde_absence_tablette

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui_tablette.mes_absences_tablette.solde_absence_tablette.SoldeAbsenceTabletteFragment
import fr.strada.smobile.utils.listner.DialogChangeMonthListner

@Subcomponent(modules = [SoldeAbsenceTabletteModule::class, SoldeAbsenceTabletteViewModelModule::class])
interface SoldeAbsenceTabletteComponent {

    fun inject(soldeAbsenceTabletteFragment: SoldeAbsenceTabletteFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        @BindsInstance
        fun setDialogChangeMonthListner(dialogChangeMonthListner: DialogChangeMonthListner?) : Builder

        fun build() : SoldeAbsenceTabletteComponent
    }
}