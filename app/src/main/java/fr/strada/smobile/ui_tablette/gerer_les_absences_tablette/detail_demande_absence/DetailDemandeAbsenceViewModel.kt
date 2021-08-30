package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.detail_demande_absence

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class DetailDemandeAbsenceViewModel @Inject constructor(application: Application): AndroidViewModel(application){

    private val _pressBtnValider = MutableLiveData<Event<Unit>>()
    val pressBtnValider: LiveData<Event<Unit>> = _pressBtnValider

    private val _pressBtnEnAttente = MutableLiveData<Event<Unit>>()
    val pressBtnEnAttente: LiveData<Event<Unit>> = _pressBtnEnAttente

    private val _pressBtnRefuser = MutableLiveData<Event<Unit>>()
    val pressBtnRefuser: LiveData<Event<Unit>> = _pressBtnRefuser

    fun pressBtnValider(){
        _pressBtnValider.value = Event(Unit)    }

    fun pressBtnEnAttente(){
        _pressBtnEnAttente.value = Event(Unit)    }

    fun pressBtnRefuser(){
        _pressBtnRefuser.value = Event(Unit) }

    fun resetViewModel(){
        _pressBtnValider.value = null
        _pressBtnEnAttente.value = null
        _pressBtnRefuser.value = null
    }
}