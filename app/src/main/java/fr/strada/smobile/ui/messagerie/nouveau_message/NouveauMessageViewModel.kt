package fr.strada.smobile.ui.messagerie.nouveau_message

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class NouveauMessageViewModel @Inject constructor(application: Application):AndroidViewModel(application) {

    private val _pressBtnBackEvent = MutableLiveData<Event<Unit>>()
    val pressBtnBackEvent: LiveData<Event<Unit>> = _pressBtnBackEvent

    private val _pressSelectDestinataireEvent = MutableLiveData<Event<Unit>>()
    val pressSelectDestinataireEvent: LiveData<Event<Unit>> = _pressSelectDestinataireEvent

    private val _pressBtnEnvoyerEvent = MutableLiveData<Event<Unit>>()
    val pressBtnEnvoyerEvent: LiveData<Event<Unit>> = _pressBtnEnvoyerEvent

    val destinataire = MutableLiveData<String>()
    val object_ = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    init {
        destinataire.value = ""
        object_.value = ""
        message.value = ""
    }

    fun pressBtnBack()
    {
        _pressBtnBackEvent.value = Event(Unit)
    }

    fun pressSelectDestinataire()
    {
        _pressSelectDestinataireEvent.value = Event(Unit)
    }

    fun pressBtnEnvoyer()
    {
        _pressBtnEnvoyerEvent.value = Event(Unit)
    }

    fun resetViewModel()
    {
        _pressBtnBackEvent.value = null
        _pressSelectDestinataireEvent.value = null
        _pressBtnEnvoyerEvent.value = null
    }
}
