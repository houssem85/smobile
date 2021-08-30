package fr.strada.smobile.ui_tablette.messagerie.message_predefinie

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.MessageModel
import fr.strada.smobile.data.repositories.MessagePredefiniRepository
import javax.inject.Inject

class MessagePredefinieTabletteViewModel @Inject constructor(application: Application,val messagePredefiniRepository: MessagePredefiniRepository):AndroidViewModel(application) {

    val messagesPredefini = MutableLiveData<ArrayList<MessageModel>>()

    init {
        messagesPredefini.value = arrayListOf()
    }

    fun getMessages()
    {
        messagesPredefini.value = messagePredefiniRepository.getMessagesPredefini()
    }

    fun onFocusChange(view: View, hasFocus:Boolean){
    }
}