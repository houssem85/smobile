package fr.strada.smobile.ui.gerer_absence.detaildemandeabsence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.strada.smobile.utils.Event

class DetailDemandAbsenceViewModel : ViewModel() {

    private val _pressBtnBackEvent = MutableLiveData<Event<Unit>>()
    val pressBtnBackEvent: LiveData<Event<Unit>> = _pressBtnBackEvent

    private val _pressBtnValider = MutableLiveData<Event<Unit>>()
    val pressBtnValider: LiveData<Event<Unit>> = _pressBtnValider

    private val _pressBtnEnAttente = MutableLiveData<Event<Unit>>()
    val pressBtnEnAttente: LiveData<Event<Unit>> = _pressBtnEnAttente

    private val _pressBtnRefuser = MutableLiveData<Event<Unit>>()
    val pressBtnRefuser: LiveData<Event<Unit>> = _pressBtnRefuser

    private val _pressBtnChrono = MutableLiveData<Event<Unit>>()
    val pressBtnChrono: LiveData<Event<Unit>> = _pressBtnChrono


    fun pressBtnBack() {
        _pressBtnBackEvent.value = Event(Unit)
    }

    fun pressBtnValider(){
        _pressBtnValider.value = Event(Unit)    }

    fun pressBtnEnAttente(){
        _pressBtnEnAttente.value = Event(Unit)    }

    fun pressBtnRefuser(){
        _pressBtnRefuser.value = Event(Unit)    }

    fun pressBtnChrono(){
        _pressBtnChrono.value = Event(Unit)
    }


}