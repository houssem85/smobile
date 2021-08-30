package fr.strada.smobile.ui.demande_absence

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class AbsenceRequestViewModel @Inject constructor(application: Application): AndroidViewModel(application){

    private val _pressBtnOpenMenuEvent = MutableLiveData<Event<Unit>>()
    val pressBtnOpenMenuEvent: LiveData<Event<Unit>> = _pressBtnOpenMenuEvent

    private val _pressBtnSelectTypeAbsenceEvent = MutableLiveData<Event<Unit>>()
    val pressBtnSelectTypeAbsence: LiveData<Event<Unit>> = _pressBtnSelectTypeAbsenceEvent

    private val _selectCongeEvent = MutableLiveData<Event<Unit>>()
    val selectCongeEvent: LiveData<Event<Unit>> = _selectCongeEvent

    private val _selectRecuperationEvent = MutableLiveData<Event<Unit>>()
    val selectRecuperationEvent: LiveData<Event<Unit>> = _selectRecuperationEvent

    private val _selectReposCompensateurEvent = MutableLiveData<Event<Unit>>()
    val selectReposCompensateurEvent: LiveData<Event<Unit>> = _selectReposCompensateurEvent

    private val _selectReposCompensateurRemplacementEvent = MutableLiveData<Event<Unit>>()
    val selectReposCompensateurRemplacementEvent: LiveData<Event<Unit>> = _selectReposCompensateurRemplacementEvent

    private val _pressBtnSelectDateDebutEvent = MutableLiveData<Event<Unit>>()
    val pressBtnSelectDateDebutEvent: LiveData<Event<Unit>> = _pressBtnSelectDateDebutEvent

    private val _pressBtnSelectDateFinEvent = MutableLiveData<Event<Unit>>()
    val pressBtnSelectDateFinEvent: LiveData<Event<Unit>> = _pressBtnSelectDateFinEvent

    private val _pressBtnValiderEvent = MutableLiveData<Event<Unit>>()
    val pressBtnValiderEvent: LiveData<Event<Unit>> = _pressBtnValiderEvent

    var duration = MutableLiveData<String>()
    var comment = MutableLiveData<String>()
    var layoutErrorTypeAbsence = MutableLiveData<Int>()
    var layoutErrorDateDeb = MutableLiveData<Int>()
    var dialogStartIsShown = MutableLiveData<Boolean>()
    var dialogEndIsShow = MutableLiveData<Boolean>()
    var loadingDialogIsShown = MutableLiveData<Boolean>()
    var successDialogIsShown = MutableLiveData<Boolean>()

    var typeAbsence = MutableLiveData<String>()

    var dateS: String = ""
    var dateE: String = ""

    init {
        duration.value = "Votre duree d'absence apparaît ici"
        comment.value = ""
        layoutErrorTypeAbsence.value = View.GONE
        layoutErrorDateDeb.value = View.GONE
        dialogStartIsShown.value = false
        dialogEndIsShow.value = false
        loadingDialogIsShown.value = false
        successDialogIsShown.value = false
        typeAbsence.value = ""
    }



    fun pressBtnOpenMenu()
    {
        _pressBtnOpenMenuEvent.value = Event(Unit)
    }

    fun pressBtnSelectTypeAbsence(){
        _pressBtnSelectTypeAbsenceEvent.value = Event(Unit)
    }

    fun selectConge(){
        _selectCongeEvent.value = Event(Unit)
    }

    fun selectRecuperation(){
        _selectRecuperationEvent.value = Event(Unit)
    }

    fun selectReposCompensateur(){
        _selectReposCompensateurEvent.value = Event(Unit)
    }

    fun selectReposCompensateurRemplacement(){
        _selectReposCompensateurRemplacementEvent.value = Event(Unit)
    }

    fun pressBtnSelectDateDebut(){
        _pressBtnSelectDateDebutEvent.value = Event(Unit)
    }

    fun pressBtnSelectDateFin(){
        _pressBtnSelectDateFinEvent.value = Event(Unit)
    }

    fun pressBtnValider(){
        _pressBtnValiderEvent.value = Event(Unit)
    }

    fun resetViewModel(){
        _pressBtnOpenMenuEvent.value = null
        _pressBtnSelectDateDebutEvent.value = null
        _pressBtnSelectDateFinEvent.value = null
        _pressBtnSelectTypeAbsenceEvent.value = null
        _pressBtnValiderEvent.value = null
        _selectCongeEvent.value = null
        _selectRecuperationEvent.value = null
        _selectReposCompensateurEvent.value = null
        _selectReposCompensateurRemplacementEvent.value = null
    }
}