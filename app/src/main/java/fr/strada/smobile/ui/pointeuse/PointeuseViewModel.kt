package fr.strada.smobile.ui.pointeuse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.pointeuse.JourActivite
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.data.repositories.PointeuseRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PointeuseViewModel @Inject constructor(application: Application,
                                             private val pointeuseRepository : PointeuseRepository) : AndroidViewModel(application){
    private val _jourActivities = MutableLiveData<Resource<List<JourActivite>>>()
    val jourActivities  : LiveData<Resource<List<JourActivite>>> = _jourActivities

    private val _typeActivities = MutableLiveData<Resource<List<TypeActivitePointeuseModel>>>()
    val typeActivities  : LiveData<Resource<List<TypeActivitePointeuseModel>>> = _typeActivities

    fun getJourActivities(jour : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            _jourActivities.postValue(Resource.loading(null))
            val res = pointeuseRepository.getLastSixDaysActivites(jour)
            _jourActivities.postValue(res)
        }
    }

    fun getTypeActivitePointeuse()
    {
        viewModelScope.launch {
            val res = pointeuseRepository.getListTypeActivitePointeuse()
            _typeActivities.postValue(res)
        }
    }
}