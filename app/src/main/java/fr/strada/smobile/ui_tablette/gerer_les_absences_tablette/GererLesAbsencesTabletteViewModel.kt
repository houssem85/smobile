package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class GererLesAbsencesTabletteViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    private val _pressBtnRetourEvent = MutableLiveData<Event<Unit>>()
    val pressBtnRetourEvent: LiveData<Event<Unit>> = _pressBtnRetourEvent

    private val _pressBtnFilterEvent = MutableLiveData<Event<Unit>>()
    val pressBtnFilterEvent: LiveData<Event<Unit>> = _pressBtnFilterEvent

    var isSecondFragmentLoaded = false
    var isThirdFragmentVisible = false

    fun pressBtnRetour()
    {
        _pressBtnRetourEvent.value = Event(Unit)
    }

    fun pressBtnFilter()
    {
        _pressBtnFilterEvent.value = Event(Unit)
    }

    fun resetViewModel()
    {
        _pressBtnRetourEvent.value = null
        _pressBtnFilterEvent.value = null
    }
}