package fr.strada.smobile.ui.infractions.filtre

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class FiltreViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    val isPeriodeFilterActive = MutableLiveData(false)

    val dateDebut = MutableLiveData("")

    val dateFin = MutableLiveData("")

    val isCategorieClassFilterActive = MutableLiveData(false)

    val isCategorieClass4Active = MutableLiveData(false)
    val isCategorieClass5Active = MutableLiveData(false)
}