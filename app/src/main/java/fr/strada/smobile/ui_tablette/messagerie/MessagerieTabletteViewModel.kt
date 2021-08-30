package fr.strada.smobile.ui_tablette.messagerie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class MessagerieTabletteViewModel @Inject constructor(application: Application):AndroidViewModel(application) {

    val isConversationActive = MutableLiveData<Boolean>()

    init {
        isConversationActive.value = false
    }
}