package fr.strada.smobile.ui_tablette.messagerie.boite_reception

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.R
import fr.strada.smobile.data.models.MessageModel
import fr.strada.smobile.data.repositories.BoiteReceptionRepository
import javax.inject.Inject

class BoiteReceptionTabletteViewModel @Inject constructor(application: Application,val boiteReceptionRepository: BoiteReceptionRepository):AndroidViewModel(application) {

    val context = application
    val typeFilter = MutableLiveData<String>()
    val isMenuFilterOpned = MutableLiveData<Boolean>()
    val messages = MutableLiveData<ArrayList<MessageModel>>()

    init {
        typeFilter.value = context.resources.getString(R.string.Tous)
        isMenuFilterOpned.value = false
    }

    fun getAllMessages()
    {
        messages.value = boiteReceptionRepository.getAllMessagesInBoiteReception()
    }
}