package fr.strada.smobile.di.widget

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fr.strada.smobile.ui.widget.SMobileWidget

@Module
abstract class WidgetModule {

    @ContributesAndroidInjector
    abstract fun contributeSMobileWidget(): SMobileWidget
}