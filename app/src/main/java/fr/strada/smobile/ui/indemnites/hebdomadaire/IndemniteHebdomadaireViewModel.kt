package fr.strada.smobile.ui.indemnites.hebdomadaire

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.indemnite.hebdo.IndemniteHebdo
import fr.strada.smobile.data.repositories.IndemniteRepository
import fr.strada.smobile.utils.Event
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class IndemniteHebdomadaireViewModel @Inject constructor(
    application: Application,
    val indemniteRepository: IndemniteRepository
) : AndroidViewModel(application) {

    var week = MutableLiveData<Int>()
    var month = MutableLiveData<Int>()
    var year = MutableLiveData<Int>()

    val totalCumulHebdomadaire = MutableLiveData<Int>()

    private val _pressBtnNextWeekEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnNextWeekEvent: MutableLiveData<Event<Unit>?> = _pressBtnNextWeekEvent

    private val _pressBtnPreviousWeekEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnPreviousWeekEvent: MutableLiveData<Event<Unit>?> = _pressBtnPreviousWeekEvent

    private val _pressBtnChangeYearEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnChangeYearEvent: MutableLiveData<Event<Unit>?> = _pressBtnChangeYearEvent

    val yearDialogChangeWeek = MutableLiveData<Int>()

    var isDialogChangeYearShown = false
    var isDialogChangeWeekShown = false

    private val _indemnitehebdo = MutableLiveData<Resource<IndemniteHebdo>>()
    val indemnitehebdo: LiveData<Resource<IndemniteHebdo>> = _indemnitehebdo

    init {
        week.value = 0
        month.value = 0
        year.value = 2021
        yearDialogChangeWeek.value = 2021
        totalCumulHebdomadaire.value = 0
    }

    fun getIndemniteHebdo(strStart: String, strEnd: String) {
        viewModelScope.launch {
            _indemnitehebdo.postValue(Resource.loading(null))
            val res = indemniteRepository.getIndemnitehebdomadair(strStart, strEnd)
            _indemnitehebdo.postValue(res)
        }
    }

    fun pressBtnNextWeek() {
        _pressBtnNextWeekEvent.value = Event(Unit)
    }

    fun pressBtnPreviousWeek() {
        _pressBtnPreviousWeekEvent.value = Event(Unit)
    }

    fun pressBtnChangeYear() {
        _pressBtnChangeYearEvent.value = Event(Unit)
    }


    fun resetViewModel() {
        _pressBtnNextWeekEvent.value = null
        _pressBtnPreviousWeekEvent.value = null
        _pressBtnChangeYearEvent.value = null
    }

}