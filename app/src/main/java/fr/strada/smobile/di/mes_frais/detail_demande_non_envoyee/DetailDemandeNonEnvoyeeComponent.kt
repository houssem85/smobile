package fr.strada.smobile.di.mes_frais.detail_demande_non_envoyee

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.mes_frais.detail_demande_non_envoyee.DetailDemandeNonEnvoyeeFragment
import fr.strada.smobile.utils.listner.DialogEnvoieEmailListner
import fr.strada.smobile.utils.listner.DialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter
import fr.strada.smobile.utils.listner.ItemDepenseListner

@Subcomponent(modules = [DetailDemandeNonEnvoyeeModule::class, DetailDemandeNonEnvoyeeViewModelModule::class])
interface DetailDemandeNonEnvoyeeComponent {

    fun inject(detailDemandeNonEnvoyeeFragment: DetailDemandeNonEnvoyeeFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context) : Builder

        @BindsInstance
        fun setItemDepenseListner(itemDepenseListner: ItemDepenseListner) : DetailDemandeNonEnvoyeeComponent.Builder

        @BindsInstance
        fun setDialogEnvoieEmailListner(dialogEnvoieEmailListner: DialogEnvoieEmailListner) : DetailDemandeNonEnvoyeeComponent.Builder

        @BindsInstance
        fun setDialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter(dialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter: DialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter) : DetailDemandeNonEnvoyeeComponent.Builder

        fun build() : DetailDemandeNonEnvoyeeComponent

    }
}