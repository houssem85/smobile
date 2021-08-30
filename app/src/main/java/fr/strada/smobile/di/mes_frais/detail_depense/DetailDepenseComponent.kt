package fr.strada.smobile.di.mes_frais.detail_depense

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.mes_frais.detail_depense.DetailDepenseFragment
import fr.strada.smobile.utils.listner.DialogSelectDateListner
import fr.strada.smobile.utils.listner.DialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter

@Subcomponent(modules = [DetailDepenseModule::class, DetailDepenseViewModelModule::class])
interface DetailDepenseComponent {

    fun inject(detailDepenseFragment: DetailDepenseFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        @BindsInstance
        fun setDialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter(dialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter: DialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter) : DetailDepenseComponent.Builder

        @BindsInstance
        fun setDialogSelectDateListner(dialogSelectDateListner: DialogSelectDateListner) : DetailDepenseComponent.Builder

        fun build() : DetailDepenseComponent

    }
}