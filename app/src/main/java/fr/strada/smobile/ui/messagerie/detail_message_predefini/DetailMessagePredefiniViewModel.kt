package fr.strada.smobile.ui.messagerie.detail_message_predefini

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class DetailMessagePredefiniViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    private val _pressBtnBackEvent = MutableLiveData<Event<Unit>>()
    val pressBtnBackEvent: LiveData<Event<Unit>> = _pressBtnBackEvent

    val objet = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    init {
        objet.value = ""
        message.value = ""
    }


    fun pressBtnBack()
    {
        _pressBtnBackEvent.value = Event(Unit)
    }


}