package fr.strada.smobile.ui.mes_frais.filtre

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class FiltreMesFraisViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    val isPeriodeFilterActive = MutableLiveData(false)

    val dateDebut = MutableLiveData("")

    val dateFin = MutableLiveData("")

    val isDemandeAccepteeActive = MutableLiveData(false)

    val isDemandeEnCourActive = MutableLiveData(false)
}