package fr.strada.smobile.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.data.models.userinfo.FunctionalityRow
import fr.strada.smobile.data.repositories.PointeuseRepository
import fr.strada.smobile.data.repositories.UserRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(application: Application,
                                        private val pointeuseRepository : PointeuseRepository,
                                        private val userRepository: UserRepository) : AndroidViewModel(application) {

    val responseGetTypeActivitePointeuse = MutableLiveData<Resource<List<TypeActivitePointeuseModel>>>()

    private val _userFunctionalities = MutableLiveData<Resource<List<FunctionalityRow>>>()
    val userFunctionalities: LiveData<Resource<List<FunctionalityRow>>> = _userFunctionalities

    var userName = userRepository.getUserNameFlow()
    var userImage = userRepository.getUserImageFlow()

    fun fetchTypeActivitesPointeuseFromServer()
    {
        responseGetTypeActivitePointeuse.postValue(Resource.loading(null))
        viewModelScope.launch (Dispatchers.IO) {
            val resource = pointeuseRepository.getListTypeActivitePointeuse()
            responseGetTypeActivitePointeuse.postValue(resource)
        }
    }

    fun getUserFunctionalities()
    {
        if(_userFunctionalities.value == null)
        {
            _userFunctionalities.postValue(Resource.loading(null))
            viewModelScope.launch {
                val res = userRepository.getUserFunctionalities()
                _userFunctionalities.postValue(res)
            }
        }
    }
}