package fr.strada.smobile.ui.activities.journalier

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.data.models.activites.day.ActivitesJournaliere
import fr.strada.smobile.data.repositories.MesActivitesRepository
import fr.strada.smobile.utils.Event
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class GraphicalViewViewModel @Inject constructor(application: Application,val mesActivitesRepository: MesActivitesRepository):AndroidViewModel(application) {

    private val _pressBtnBackEvent = MutableLiveData<Event<Unit>>()
    val pressBtnBackEvent: LiveData<Event<Unit>> = _pressBtnBackEvent

    private val _pressBtnShowCommentsEvent = MutableLiveData<Event<Unit>>()
    val pressBtnShowCommentsEvent: LiveData<Event<Unit>> = _pressBtnShowCommentsEvent

    private val _pressBtnHideCommentsEvent = MutableLiveData<Event<Unit>>()
    val pressBtnHideCommentsEvent: LiveData<Event<Unit>> = _pressBtnHideCommentsEvent

    private val _pressBtnNextDayEvent = MutableLiveData<Event<Unit>>()
    val pressBtnNextDayEvent: LiveData<Event<Unit>> = _pressBtnNextDayEvent

    private val _pressBtnPreviousDayEvent = MutableLiveData<Event<Unit>>()
    val pressBtnPreviousDayEvent: LiveData<Event<Unit>> = _pressBtnPreviousDayEvent

    private val _pressBtnSendCommentEvent = MutableLiveData<Event<Unit>>()
    val pressBtnSendCommentEvent: LiveData<Event<Unit>> = _pressBtnSendCommentEvent

    val currentDay = MutableLiveData<Day>()

    private val _activitesJournaliere = MutableLiveData<Resource<ActivitesJournaliere>>()
    val activitesJournaliere : LiveData<Resource<ActivitesJournaliere>> = _activitesJournaliere

    init {
        currentDay.value = Day(0,0,1)
    }

    fun pressBtnBack()
    {
        _pressBtnBackEvent.value = Event(Unit)
    }

    fun pressBtnSendComment()
    {
        _pressBtnSendCommentEvent.value = Event(Unit)
    }

    fun pressBtnNextDay()
    {
        currentDay.value = getNextDay(currentDay.value!!)
        _pressBtnNextDayEvent.value = Event(Unit)
    }

    fun pressBtnPreviousDay()
    {
        currentDay.value = getPreviusDay(currentDay.value!!)
        _pressBtnPreviousDayEvent.value = Event(Unit)
    }

    fun pressBtnShowComments()
    {
        _pressBtnShowCommentsEvent.value = Event(Unit)
    }

    fun pressBtnHideComments()
    {
        _pressBtnHideCommentsEvent.value = Event(Unit)
    }

    fun getActivitesJournaliere(jour:String)
    {
        viewModelScope.launch {
            _activitesJournaliere.postValue(Resource.loading(null))
            val res = mesActivitesRepository.getActivitesJournaliere(jour)
            _activitesJournaliere.postValue(res)
        }
    }
}