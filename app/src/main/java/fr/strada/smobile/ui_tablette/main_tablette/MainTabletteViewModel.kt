package fr.strada.smobile.ui_tablette.main_tablette

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.sidemenuview.data.MenuItem
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.data.repositories.PointeuseRepository
import fr.strada.smobile.data.repositories.UserRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainTabletteViewModel @Inject constructor(application: Application,
                                                private val pointeuseRepository : PointeuseRepository,
                                                private val userRepository: UserRepository ): AndroidViewModel(application){

    val responseGetTypeActivitePointeuse = MutableLiveData<Resource<List<TypeActivitePointeuseModel>>>()
    var menuItems = ArrayList<MenuItem>()

    fun fetchTypeActivitesPointeuseFromServer()
    {
        responseGetTypeActivitePointeuse.postValue(Resource.loading(null))
        viewModelScope.launch (Dispatchers.IO) {
            val resource = pointeuseRepository.getListTypeActivitePointeuse()
            responseGetTypeActivitePointeuse.postValue(resource)
        }
    }

    suspend fun getUserFunctionalities() = userRepository.getUserFunctionalities()
}