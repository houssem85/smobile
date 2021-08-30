package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class GererAbsenceViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    private val _pressBtnCalendrierEquiperEvent = MutableLiveData<Event<Unit>>()
    val pressBtnCalendrierEquipeEvent: LiveData<Event<Unit>> = _pressBtnCalendrierEquiperEvent

    fun pressBtnCalendrierEquipe()
    {
        _pressBtnCalendrierEquiperEvent.value = Event(Unit)
    }

    fun resetViewModel(){
        _pressBtnCalendrierEquiperEvent.value = null
    }
}