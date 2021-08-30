package fr.strada.smobile.ui.auth.auth0

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.auth0.android.result.Credentials
import fr.strada.smobile.data.models.userinfo.UserInfo
import fr.strada.smobile.data.repositories.UserRepository
import fr.strada.smobile.utils.SingleLiveEvent
import fr.strada.smobile.utils.Status
import kotlinx.coroutines.launch
import javax.inject.Inject

class Auth0ViewModel @Inject constructor(application: Application,private val userRepository: UserRepository) : AndroidViewModel(application) {

    val msgError = SingleLiveEvent<String>()

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo : LiveData<UserInfo> = _userInfo

    private val _credentials = MutableLiveData<Credentials>()
    val credentials : LiveData<Credentials> = _credentials

    fun loginWithEmail(email:String,password:String)
    {
        viewModelScope.launch {
            val resCredentials = userRepository.loginWithEmail(email,password)
            if(resCredentials.status == Status.SUCCESS){
                val resUserInfo = userRepository.getUserProfile(email,resCredentials.data!!.idToken.toString())
                if(resUserInfo.status == Status.SUCCESS) {
                    _credentials.postValue(resCredentials.data!!)
                    _userInfo.postValue(resUserInfo.data!!)
                }else {
                    msgError.postValue(resUserInfo.message!!)
                }
            }else if(resCredentials.status == Status.ERROR)
            {
                msgError.postValue(resCredentials.message!!)
            }
        }
    }

    fun saveCredentials(credentials:Credentials){
        userRepository.saveCredentials(credentials)
    }

    suspend fun isModeBorne(isModeBorne : Boolean) {
        userRepository.isModeBorne(isModeBorne)
    }

    suspend fun saveUser(userInfo: UserInfo) {
        userRepository.saveUser(userInfo)
    }
}