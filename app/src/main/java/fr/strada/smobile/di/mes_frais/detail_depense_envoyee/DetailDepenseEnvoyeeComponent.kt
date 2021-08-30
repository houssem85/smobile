package fr.strada.smobile.di.mes_frais.detail_depense_envoyee

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.mes_frais.detail_depense_envoyee.DetailDepenseEnvoyeeFragment

@Subcomponent(modules = [DetailDepenseEnvoyeeModule::class,DetailDepenseEnvoyeeViewModelModule::class])
interface DetailDepenseEnvoyeeComponent {

    fun inject(detailDepenseEnvoyeeFragment: DetailDepenseEnvoyeeFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : DetailDepenseEnvoyeeComponent
    }
}