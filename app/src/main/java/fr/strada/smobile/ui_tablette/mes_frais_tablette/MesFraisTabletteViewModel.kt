package fr.strada.smobile.ui_tablette.mes_frais_tablette

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class MesFraisTabletteViewModel @Inject constructor(application: Application):AndroidViewModel(application) {

    val isSecondFragmentSetted = MutableLiveData(false)
    val isNouvelleDemandeFragmentSetted = MutableLiveData(false)
}