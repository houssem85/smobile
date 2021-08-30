package fr.strada.smobile.ui.indemnites

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.ui.indemnites.journalier.IndemniteJournalierFragment
import fr.strada.smobile.ui.indemnites.mensuel.IndemniteMensuelleFragment
import fr.strada.smobile.utils.Event
import fr.strada.smobile.utils.KEY_MONTH
import fr.strada.smobile.utils.KEY_YEAR
import javax.inject.Inject

class IndemniteViewModel @Inject constructor(application: Application): AndroidViewModel(application){

    val year = MutableLiveData<Int>()
    val month = MutableLiveData<Int>()
    val day = MutableLiveData<Int>()
    val currentFragment = MutableLiveData<Fragment>()
    var isDialogHebdomadireShown = false

    private val _pressBtnMensuelEvent = MutableLiveData<Event<Unit>>()
    val pressBtnMensuelEvent: LiveData<Event<Unit>> = _pressBtnMensuelEvent

    private val _pressBtnHebdomadaireEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnHebdomadaireEvent: MutableLiveData<Event<Unit>?> = _pressBtnHebdomadaireEvent

    private val _showLoaderEvent = MutableLiveData<Event<Unit>>()
    val showLoaderEvent: LiveData<Event<Unit>> = _showLoaderEvent

    private val _pressBtnOpenMenuEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnOpenMenuEvent: MutableLiveData<Event<Unit>?> = _pressBtnOpenMenuEvent

    private val _pressBtnCurrentDayEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnCurrentDayEvent: MutableLiveData<Event<Unit>?> = _pressBtnCurrentDayEvent

    init {
        year.value = 2021
        month.value = 0
        currentFragment.value = IndemniteJournalierFragment()
    }


    fun pressBtnMensuel() {
       if(currentFragment.value is IndemniteMensuelleFragment)
        {
            currentFragment.value = IndemniteJournalierFragment()
        }else
        {
            val args = Bundle()
            args.putInt(KEY_MONTH, month.value!!)
            args.putInt(KEY_YEAR, year.value!!)
            val fragment = IndemniteMensuelleFragment()
            fragment.arguments = args
            currentFragment.value = fragment
        }

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
    }
}