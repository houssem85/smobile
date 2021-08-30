package fr.strada.smobile.di.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fr.strada.smobile.ui.notification_pointeuse.NotificationService
import fr.strada.smobile.ui.pointeuse.PointeuseFragment
import fr.strada.smobile.ui.shortcut.ShortcutService
import fr.strada.smobile.ui.spi.tracker.service.TrackingService

@Module
abstract class ServiceBuilderModule
{
    @ContributesAndroidInjector
    abstract fun contributeNotificationService(): NotificationService

    @ContributesAndroidInjector
    abstract fun contributeShortcutService(): ShortcutService

    @ContributesAndroidInjector
    abstract fun contributePointeuseFragment(): PointeuseFragment

    @ContributesAndroidInjector
    abstract fun contributeTrackingService(): TrackingService
}