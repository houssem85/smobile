package fr.strada.smobile.ui.spi.tracker.di

import fr.strada.smobile.ui.spi.tracker.service.TrackingService

//@Subcomponent(modules = [TrackerModule::class])
interface TrackerComponent {

    fun inject(trackingService: TrackingService)

    /*@Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : TrackerComponent

    }*/

}