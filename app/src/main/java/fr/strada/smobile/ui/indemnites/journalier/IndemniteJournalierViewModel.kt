package fr.strada.smobile.ui.indemnites.journalier

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.indemnite.journalier.IndemniteJournalier
import fr.strada.smobile.data.repositories.IndemniteRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class IndemniteJournalierViewModel @Inject constructor(
    application: Application,
    private val indemniteRepository: IndemniteRepository
) : AndroidViewModel(application) {


    val year = MutableLiveData<Int>()
    val month = MutableLiveData<Int>()
    val day = MutableLiveData<Int>()
    val selectedYear = MutableLiveData<Int>()
    val selectedMonth = MutableLiveData<Int>()
    val selectedDay = MutableLiveData<Int>()
    var isDialogChangeMonthShown = true
    private val _indemniteJournalier = MutableLiveData<Resource<IndemniteJournalier>>()
    val indemniteJournalier: LiveData<Resource<IndemniteJournalier>> = _indemniteJournalier
    init {
        val c = Calendar.getInstance()
        year.value = c.get(Calendar.YEAR)
        month.value = c.get(Calendar.MONTH)
        day.value = c.get(Calendar.DAY_OF_MONTH)
        isDialogChangeMonthShown = false
    }

    fun getIndemniterJournalier(jour: String) {
        viewModelScope.launch {
            _indemniteJournalier.postValue(Resource.loading(null))
            val res = indemniteRepository.getIndemnitejournalier(jour)
            _indemniteJournalier.postValue(res)
        }

    }

}