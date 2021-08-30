package fr.strada.smobile.ui.home


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.activites.week.ActivitesHebdomadaire
import fr.strada.smobile.data.repositories.AbsenceRepository
import fr.strada.smobile.data.repositories.MesActivitesRepository
import fr.strada.smobile.data.repositories.NotificationRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(application: Application,val mesActivitesRepository: MesActivitesRepository): AndroidViewModel(application){

    //// une variable à créer une seule fois = lazy
    private val notificationRepository:NotificationRepository by lazy {
        ////constructeur
        NotificationRepository()
    }

    private val absenceRepository: AbsenceRepository by lazy {

        AbsenceRepository()
    }

    fun getNotification () = notificationRepository.getNotification()
    fun getAbsence () = absenceRepository.getAbsence()

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