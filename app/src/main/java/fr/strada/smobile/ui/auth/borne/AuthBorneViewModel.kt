package fr.strada.smobile.ui.auth.borne

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class AuthBorneViewModel @Inject constructor(application: Application): AndroidViewModel(application){

    private val _pressBtnSeconnecterEvent = MutableLiveData<Event<Unit>>()
    val pressBtnSeconnecterEvent: LiveData<Event<Unit>> = _pressBtnSeconnecterEvent


    fun pressBtnSeconnecter()
    {
        _pressBtnSeconnecterEvent.value = Event(Unit)
    }
}