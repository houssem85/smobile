package fr.strada.smobile.ui_tablette.mes_absences_tablette.solde_absence_tablette

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import java.util.*
import javax.inject.Inject

class SoldeAbsenceTabletteViewModel  @Inject constructor(application: Application): AndroidViewModel(application){

    private val _pressBtnChangeMonthEvent = MutableLiveData<Event<Unit>>()
    val pressBtnChangeMonthEvent: LiveData<Event<Unit>> = _pressBtnChangeMonthEvent

    private val _pressBtnSoldeAbsenceEvent = MutableLiveData<Event<Unit>>()
    val pressBtnSoldeAbsenceEvent: LiveData<Event<Unit>> = _pressBtnSoldeAbsenceEvent

    val month = MutableLiveData<Int>()
    val year = MutableLiveData<Int>()

    init {
        val cal = Calendar.getInstance()
        month.value = cal.get(Calendar.MONTH)
        year.value = cal.get(Calendar.YEAR)
    }

    fun pressBtnChangeMonth()
    {
        _pressBtnChangeMonthEvent.value = Event(Unit)
    }

    fun pressBtnSoldeAbsence(){
        _pressBtnSoldeAbsenceEvent.value = Event(Unit)
    }

    fun resetViewModel()
    {
        _pressBtnChangeMonthEvent.value = null
        _pressBtnSoldeAbsenceEvent.value = null
    }
}