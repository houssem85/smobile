package fr.strada.smobile.ui_tablette.messagerie.conversation

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.MessageConversationModel
import fr.strada.smobile.data.repositories.BoiteReceptionRepository
import javax.inject.Inject

class ConversationViewModel @Inject constructor(application: Application,val boiteReceptionRepository: BoiteReceptionRepository):AndroidViewModel(application) {
    val context=application
    val messages = MutableLiveData<ArrayList<MessageConversationModel>>()
    val message = MutableLiveData<String>()

    init {
        messages.value = ArrayList()
        message.value = ""
    }

    fun getMessagesConversation()
    {
        messages.value = boiteReceptionRepository.getMessagesConversations()
    }

    fun onFocusChange(view: View,hasFocus:Boolean){
    }
}