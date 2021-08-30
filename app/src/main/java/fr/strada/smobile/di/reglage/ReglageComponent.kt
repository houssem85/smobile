package fr.strada.smobile.di.reglage

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.reglage.ReglageFragment
import fr.strada.smobile.utils.listner.DialogSynchroListener
import fr.strada.smobile.utils.listner.DialogTempsNotificationsPointeuseListner


@Subcomponent(modules = [ReglageModule::class, ReglageViewModelModule::class])
interface ReglageComponent {

    fun inject(reglageFragment: ReglageFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        @BindsInstance
        fun setDialogTempsNotificationsPointeuseListner(dialogTempsNotificationsPointeuseListner: DialogTempsNotificationsPointeuseListner) : Builder

        @BindsInstance
        fun setdialogSynchroListener(dialogSynchroListener: DialogSynchroListener) : Builder

        fun build() : ReglageComponent
    }
}