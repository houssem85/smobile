package fr.strada.smobile.ui.apropos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class AProposViewModel  @Inject constructor(application: Application): AndroidViewModel(application){

    private val _pressBtnOpenMenuEvent = MutableLiveData<Event<Unit>>()
    val pressBtnOpenMenuEvent: LiveData<Event<Unit>> = _pressBtnOpenMenuEvent

    fun pressBtnOpenMenu() {
        _pressBtnOpenMenuEvent.value = Event(Unit)
    }

}