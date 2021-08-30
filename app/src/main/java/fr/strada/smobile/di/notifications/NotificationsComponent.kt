package fr.strada.smobile.di.notifications

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.notifications.NotificationsFragment
import fr.strada.smobile.utils.listner.ItemNotificationListener

@Subcomponent(modules = [NotificationsModule::class, NotificationsViewModelModule::class])
interface NotificationsComponent {

    fun inject(notificationsFragment: NotificationsFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        @BindsInstance
        fun setOnClickListner(itemNotificationListener: ItemNotificationListener) : NotificationsComponent.Builder

        fun build() : NotificationsComponent

    }
}