package fr.strada.smobile.ui.activities.journalier

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.activites.day.ActivitesJournaliere
import fr.strada.smobile.data.repositories.MesActivitesRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MyDailyActivitiesViewModel @Inject constructor(application: Application,val mesActivitesRepository: MesActivitesRepository) : AndroidViewModel(application) {

    val year = MutableLiveData<Int>()
    val month = MutableLiveData<Int>()
    val day = MutableLiveData<Int>()

    val selectedYear = MutableLiveData<Int>()
    val selectedMonth = MutableLiveData<Int>()
    val selectedDay = MutableLiveData<Int>()

    var isDialogChangeMonthShown = true

    private val _activitesJournaliere = MutableLiveData<Resource<ActivitesJournaliere>>()
    val activitesJournaliere : LiveData<Resource<ActivitesJournaliere>> = _activitesJournaliere

    private val _dailyActivitiesMensuel = MutableLiveData<Resource<List<String>>>()
    val dailyActivitiesMensuel : LiveData<Resource<List<String>>> = _dailyActivitiesMensuel

    init {
        val c = Calendar.getInstance()
        year.value = c.get(Calendar.YEAR)
        month.value = c.get(Calendar.MONTH)
        day.value = c.get(Calendar.DAY_OF_MONTH)
        isDialogChangeMonthShown = false
    }

    fun getActivitesJournaliere(jour:String)
    {
        viewModelScope.launch {
            _activitesJournaliere.postValue(Resource.loading(null))
            val res = mesActivitesRepository.getActivitesJournaliere(jour)
            _activitesJournaliere.postValue(res)
        }
    }

    /**
     * RM: month increment by 1 because api month begin with 1
     * Janury = 1
     */
    fun getDailyActivitiesMensuel()
    {
        viewModelScope.launch {
            val year = year.value!!
            val month = month.value!! + 1
            val resource = mesActivitesRepository.getDailyActivitesMensuel(year,month)
            _dailyActivitiesMensuel.postValue(resource)
        }
    }
}