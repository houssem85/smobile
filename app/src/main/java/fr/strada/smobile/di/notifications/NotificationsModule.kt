package fr.strada.smobile.di.notifications

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui.notifications.NotificationsAdapter
import fr.strada.smobile.utils.listner.ItemNotificationListener

@Module
class NotificationsModule {

    @Provides
    fun provideNotificationsAdapter(
        activity: Activity,
        itemNotificationListener: ItemNotificationListener
    ): NotificationsAdapter {
        return NotificationsAdapter(activity, itemNotificationListener)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

}