package fr.strada.smobile.ui.activities.hebdomadaire

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.activites.week.ActivitesHebdomadaire
import fr.strada.smobile.data.repositories.MesActivitesRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class MesActivitiesHebdomadaireViewModel @Inject constructor(application: Application,val mesActivitesRepository: MesActivitesRepository):AndroidViewModel(application){

    var week = MutableLiveData<Int>()
    var month = MutableLiveData<Int>()
    var year = MutableLiveData<Int>()
    val yearDialogChangeWeek = MutableLiveData<Int>()
    var isDialogChangeYearShown = false
    var isDialogChangeWeekShown = false

    private val _activitiesHebdomadaire = MutableLiveData<Resource<ActivitesHebdomadaire>>()
    val activitiesHebdomadaire : LiveData<Resource<ActivitesHebdomadaire>> = _activitiesHebdomadaire

    private val _dailyActivitiesMensuel = MutableLiveData<Resource<List<String>>>()
    val dailyActivitiesMensuel : LiveData<Resource<List<String>>> = _dailyActivitiesMensuel

    init
    {
        week.value = 0
        month.value = 0
        year.value = 2021
        yearDialogChangeWeek.value = 2021
    }

    fun getActivitiesHebdomadaire(year:Int,month:Int,startDate:String,endDate:String)
    {
        viewModelScope.launch {
            _activitiesHebdomadaire.postValue(Resource.loading(null))
            val res = mesActivitesRepository.getActivitesHebdomadaire(year,month,startDate,endDate)
            _activitiesHebdomadaire.postValue(res)
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