package fr.strada.smobile.ui.activities.mensuel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.activites.month.ActivitesMensuel
import fr.strada.smobile.data.repositories.MesActivitesRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class MesActivitiesMensuelViewModel @Inject constructor(application:Application,val mesActivitesRepository: MesActivitesRepository):AndroidViewModel(application) {

    var isDialogChangeMonthShown = false

    val month = MutableLiveData<Int>()
    val year = MutableLiveData<Int>()

    private val _activitiesMensuel = MutableLiveData<Resource<ActivitesMensuel>>()
    val activitiesMensuel : LiveData<Resource<ActivitesMensuel>> = _activitiesMensuel

    private val _dailyActivitiesMensuel = MutableLiveData<Resource<List<String>>>()
    val dailyActivitiesMensuel : LiveData<Resource<List<String>>> = _dailyActivitiesMensuel

    /**
     * RM: month increment by 1 because api month begin with 1
     * Janury = 1
     */
    fun getActivitiesMensuel()
    {
        viewModelScope.launch {
            val year = year.value!!
            val month = month.value!! + 1
            _activitiesMensuel.postValue(Resource.loading(null))
            val resource = mesActivitesRepository.getActivitesMensuel(year,month)
            _activitiesMensuel.postValue(resource)
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