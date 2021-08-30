package fr.strada.smobile.ui.messagerie.message_predefini

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.MessageModel
import fr.strada.smobile.data.repositories.MessagePredefiniRepository
import javax.inject.Inject

class MessagePredefiniViewModel @Inject constructor(application: Application,val messagePredefiniRepository: MessagePredefiniRepository):AndroidViewModel(application)  {

    val messagesPredefini = MutableLiveData<ArrayList<MessageModel>>()

    init {
        messagesPredefini.value = arrayListOf()
    }

    fun getMessages()
    {
        messagesPredefini.value = messagePredefiniRepository.getMessagesPredefini()
    }

}