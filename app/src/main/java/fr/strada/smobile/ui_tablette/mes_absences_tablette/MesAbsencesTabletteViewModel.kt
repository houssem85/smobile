package fr.strada.smobile.ui_tablette.mes_absences_tablette

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class MesAbsencesTabletteViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    private val _pressBtnRetourEvent = MutableLiveData<Event<Unit>>()
    val pressBtnRetourEvent: LiveData<Event<Unit>> = _pressBtnRetourEvent

    private val _pressBtnDemandeAbsenceEvent = MutableLiveData<Event<Unit>>()
    val pressBtnDemandeAbsenceEvent: LiveData<Event<Unit>> = _pressBtnDemandeAbsenceEvent

    var isFragmentSoldeAbsenceLoaded = false
    var isFragmentSoldeAbsenceTabletteLoaded = false
    var isFragmentDetailMesAbsences = false
    var isFragmentHistoriqueAbsenceLoaded = false

    fun pressBtnDemandeAbsence()
    {
        _pressBtnDemandeAbsenceEvent.value = Event(Unit)
    }

    fun pressBtnRetour()
    {
        _pressBtnRetourEvent.value = Event(Unit)
    }

    fun resetViewModel()
    {
        _pressBtnRetourEvent.value = null
        _pressBtnDemandeAbsenceEvent.value = null
    }
}