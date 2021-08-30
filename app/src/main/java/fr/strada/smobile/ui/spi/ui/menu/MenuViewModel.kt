package fr.strada.smobile.ui.spi.ui.menu

import android.app.Application
import androidx.lifecycle.*
import fr.strada.smobile.data.models.userinfo.ProfileModel
import fr.strada.smobile.data.repositories.UserRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {


    private val _userProfile = MutableLiveData<Resource<ProfileModel>>()
    val userProfile: LiveData<Resource<ProfileModel>> = _userProfile

    private val _userImage = MutableLiveData<String>()
    val userImage: LiveData<String> = _userImage


    fun getUserProfile() {
        viewModelScope.launch {
            _userProfile.postValue(Resource.loading(null))
            val userProfile = userRepository.getUserProfile()
            _userProfile.postValue(userProfile)
        }
    }

    fun getUserImage() {
        viewModelScope.launch {
            val userImage = userRepository.getUserImage()
            _userImage.postValue(userImage)
        }
    }
}