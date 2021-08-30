package fr.strada.smobile.ui.gerer_absence.calendrier_equipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class CalendrierEquipeViewModel @Inject constructor(application: Application): AndroidViewModel(application)  {

    private val _pressBtnRetourEvent = MutableLiveData<Event<Unit>>()
    val pressBtnRetourEvent: LiveData<Event<Unit>> = _pressBtnRetourEvent

    fun pressBtnRetour()
    {
        _pressBtnRetourEvent.value = Event(Unit)
    }
}