package fr.strada.smobile.ui.activities

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import java.util.*
import javax.inject.Inject

class MesActivitiesViewModel @Inject constructor(application: Application):AndroidViewModel(application) {

    val year = MutableLiveData<Int>()
    val month = MutableLiveData<Int>()
    val day = MutableLiveData<Int>()
    val currentFragment = MutableLiveData<Fragment>()

    private val _pressBtnHebdomadaireEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnHebdomadaireEvent: MutableLiveData<Event<Unit>?> = _pressBtnHebdomadaireEvent

    private val _pressBtnMensuelEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnMensuelEvent: MutableLiveData<Event<Unit>?> = _pressBtnMensuelEvent

    private val _pressBtnOpenMenuEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnOpenMenuEvent: MutableLiveData<Event<Unit>?> = _pressBtnOpenMenuEvent

    private val _pressBtnCurrentDayEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnCurrentDayEvent: MutableLiveData<Event<Unit>?> = _pressBtnCurrentDayEvent

    var isDialogHebdomadireShown = false

    init {
        val c = Calendar.getInstance()
        year.value = c.get(Calendar.YEAR)
        month.value = c.get(Calendar.MONTH)
        day.value = c.get(Calendar.DAY_OF_MONTH)
    }

    fun pressBtnMensuel()
    {
        _pressBtnMensuelEvent.value = Event(Unit)
    }

    fun pressBtnHebdomadaire()
    {
        _pressBtnHebdomadaireEvent.value = Event(Unit)
    }

    fun pressBtnOpenMenu()
    {
        _pressBtnOpenMenuEvent.value = Event(Unit)
    }

    fun pressBtnCurrentDay()
    {
        _pressBtnCurrentDayEvent.value = Event(Unit)
    }

    fun resetViewModel()
    {
        _pressBtnHebdomadaireEvent.value = null
        _pressBtnOpenMenuEvent.value = null
        _pressBtnCurrentDayEvent.value = null
        _pressBtnMensuelEvent.value = null
    }
}