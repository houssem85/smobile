package fr.strada.smobile.di.messagerie.detailMessagePredefini

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.messagerie.detail_message_predefini.DetailMessagePredefiniActivity

@Subcomponent(modules = [DetailMessagePredefiniModule::class, DetailMessagePredefiniViewModelModule::class])
interface DetailMessagePredefiniComponent  {

    fun inject(DetailMessagePredefiniActivity: DetailMessagePredefiniActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : DetailMessagePredefiniComponent
    }
}