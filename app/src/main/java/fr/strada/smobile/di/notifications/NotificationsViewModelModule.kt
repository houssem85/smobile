package fr.strada.smobile.di.notifications

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.notifications.NotificationsViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class NotificationsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel::class)
    abstract fun bindNotificationsViewModel(notificationsViewModel: NotificationsViewModel): ViewModel
}