package fr.strada.smobile.ui.spi.ui.reglage.di

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.utils.listner.DialogSynchroListener
import fr.strada.smobile.utils.listner.DialogTempsNotificationsPointeuseListner

@Subcomponent(modules = [SettingsModule::class, SettingsViewModelModule::class])
interface SettingsComponent {

    //fun inject(settingsFragment: SettingsFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        @BindsInstance
        fun setDialogTempsNotificationsPointeuseListner(dialogTempsNotificationsPointeuseListner: DialogTempsNotificationsPointeuseListner) : Builder

        @BindsInstance
        fun setdialogSynchroListener(dialogSynchroListener: DialogSynchroListener) : Builder

        fun build() : SettingsComponent
    }
}