package fr.strada.smobile.ui_tablette.messagerie.nouveau_message_predefinie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class NouveauMessagePredifinieTabletteViewModel @Inject constructor(application: Application):AndroidViewModel(application) {

    val _object = MutableLiveData<String>()
    val messagePredifinie = MutableLiveData<String>()

    init {
        _object.value = ""
        messagePredifinie.value = ""
    }
}