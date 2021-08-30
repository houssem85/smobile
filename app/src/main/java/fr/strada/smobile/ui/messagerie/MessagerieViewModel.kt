package fr.strada.smobile.ui.messagerie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class MessagerieViewModel @Inject constructor(application: Application): AndroidViewModel(application){

    private val _pressBtnOpenMenuEvent = MutableLiveData<Event<Unit>>()
    val pressBtnOpenMenuEvent: LiveData<Event<Unit>> = _pressBtnOpenMenuEvent

    private val _pressBtnFiltreMessagesEvent = MutableLiveData<Event<Unit>>()
    val pressBtnFiltreMessagesEvent: LiveData<Event<Unit>> = _pressBtnFiltreMessagesEvent

    private val _pressBtnAddMessageEvent = MutableLiveData<Event<Unit>>()
    val pressBtnAddMessageEvent: LiveData<Event<Unit>> = _pressBtnAddMessageEvent

    private val _pressTxtFiltreTousEvent = MutableLiveData<Event<Unit>>()
    val pressTxtFiltreTousEvent: LiveData<Event<Unit>> = _pressTxtFiltreTousEvent

    private val _pressTxtFiltreMessageLuEvent = MutableLiveData<Event<Unit>>()
    val pressTxtFiltreMessageLuEvent: LiveData<Event<Unit>> = _pressTxtFiltreMessageLuEvent

    private val _pressTxtFiltreMessageNonLuEvent = MutableLiveData<Event<Unit>>()
    val pressTxtFiltreMessageNonLuEvent: LiveData<Event<Unit>> = _pressTxtFiltreMessageNonLuEvent


    fun pressBtnOpenMenu()
    {
        _pressBtnOpenMenuEvent.value = Event(Unit)
    }

    fun pressBtnOpenFiltreMessage()
    {
        _pressBtnFiltreMessagesEvent.value = Event(Unit)
    }

    fun pressBtnAddMessage()
    {
        _pressBtnAddMessageEvent.value = Event(Unit)
    }


    fun pressTxtFiltreTous(){
        _pressTxtFiltreTousEvent.value = Event(Unit)
    }


    fun pressTxtFiltreMessageLu(){
        _pressTxtFiltreMessageLuEvent.value = Event(Unit)
    }


    fun pressTxtFiltreMessageNonLu(){
        _pressTxtFiltreMessageNonLuEvent.value = Event(Unit)
    }



}