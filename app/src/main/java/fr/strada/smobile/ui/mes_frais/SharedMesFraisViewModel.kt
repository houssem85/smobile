package fr.strada.smobile.ui.mes_frais

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fr.strada.smobile.data.models.mes_frais.Depense
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.data.models.mes_frais.UpdateDepense
import fr.strada.smobile.utils.SingleLiveEvent
import javax.inject.Inject

class SharedMesFraisViewModel @Inject constructor(application:Application) : AndroidViewModel(application) {
    val addNouvelleDepenseEvent = SingleLiveEvent<Depense>()
    val updateDepenseEvent = SingleLiveEvent<UpdateDepense>()
    val refreshListNoteFraisEvent = SingleLiveEvent<Unit>()

    fun refreshListNoteFrais() {
        refreshListNoteFraisEvent.value = Unit
    }

    val navigateToDemandeAccepteeEvent = SingleLiveEvent<NoteFrais>()
    val navigateToDemandeEnCourEvent = SingleLiveEvent<NoteFrais>()
    val finishAddNouvelleDemandeEvent = SingleLiveEvent<Unit>()
    val finishDetailsDemandeEnCourEvent = SingleLiveEvent<Unit>()

    fun finishAddNouvelleDemande()
    {
        finishAddNouvelleDemandeEvent.value = Unit
    }

    fun finishDetailsDemandeEnCour()
    {
        finishDetailsDemandeEnCourEvent.value = Unit
    }

    fun navigateToDemandeAcceptee(noteFrais: NoteFrais){
        navigateToDemandeAccepteeEvent.value = noteFrais
    }

    fun navigateToDemandeEnCour(noteFrais: NoteFrais){
        navigateToDemandeEnCourEvent.value = noteFrais
    }
}