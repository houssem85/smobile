package fr.strada.smobile.ui.messagerie.boite_de_reception

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.MessageModel
import fr.strada.smobile.data.repositories.BoiteReceptionRepository
import javax.inject.Inject

class BoiteReceptionViewModel @Inject constructor(application: Application,val boiteReceptionRepository: BoiteReceptionRepository):AndroidViewModel(application)  {

    val messages = MutableLiveData<ArrayList<MessageModel>>()

    init {
        messages.value = arrayListOf()
    }

    fun getAllMessages()
    {
        messages.value = boiteReceptionRepository.getAllMessagesInBoiteReception()
    }

    fun getMessagesLu(){
        messages.value = boiteReceptionRepository.getMessagesLuInBoiteReception()
    }

    fun getMessagesNonLu(){
        messages.value = boiteReceptionRepository.getMessagesNonLuInBoiteReception()
    }

}