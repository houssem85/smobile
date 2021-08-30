package fr.strada.smobile.ui_tablette.accueil.statistique

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.auth0.android.authentication.storage.CredentialsManager
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.data.models.activites.week.ActivitesHebdomadaire
import fr.strada.smobile.data.repositories.AbsenceRepository
import fr.strada.smobile.data.repositories.MesActivitesRepository
import fr.strada.smobile.utils.Event
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class StatistiqueViewModel @Inject constructor(application: Application,val absenceRepository: AbsenceRepository,val mesActivitesRepository: MesActivitesRepository,val credentialsManager: CredentialsManager):AndroidViewModel(application) {

    val context = application
    val absences = MutableLiveData<List<AbsenceModel>>()
    val currentDate = MutableLiveData<Date>()

    private val _pressBtnVoirTousEvent = MutableLiveData<Event<Unit>>()
    val pressBtnVoirTousEvent: LiveData<Event<Unit>> = _pressBtnVoirTousEvent

    var voirTous = MutableLiveData<Int>()

    init {
        absences.value = emptyList()
        currentDate.value = Date()
        voirTous.value = View.INVISIBLE
    }

    fun getAbsences()
    {
        absences.value = absenceRepository.getAbsence()
    }

    fun pressBtnVoirTous()
    {
        _pressBtnVoirTousEvent.value = Event(Unit)
    }

    fun resetViewModel()
    {
        _pressBtnVoirTousEvent.value = null
    }

    private val _activitiesHebdomadaire = MutableLiveData<Resource<ActivitesHebdomadaire>>()
    val activitiesHebdomadaire : LiveData<Resource<ActivitesHebdomadaire>> = _activitiesHebdomadaire

    fun getActivitiesHebdomadaire(year:Int,month:Int,startDate:String,endDate:String)
    {
        viewModelScope.launch {
            val res = mesActivitesRepository.getActivitesHebdomadaire(year,month,startDate,endDate)
            _activitiesHebdomadaire.postValue(res)
        }
    }
}