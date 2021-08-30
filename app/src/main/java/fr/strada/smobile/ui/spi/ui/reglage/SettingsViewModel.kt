package fr.strada.smobile.ui.spi.ui.reglage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.local.SettingsPreferences
import fr.strada.smobile.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
                        application: Application,
                        private val settingsPreferences: SettingsPreferences
) : AndroidViewModel(application) {

    private val _pressBtnOpenMenuEvent = MutableLiveData<Event<Unit>>()
    val pressBtnOpenMenuEvent: LiveData<Event<Unit>> = _pressBtnOpenMenuEvent



    val isPrivate = MutableLiveData<Boolean>()
    var isDialogViePriveShown = false

    init {
        isPrivate.value = false
    }

     fun pressBtnOpenMenu() {
         _pressBtnOpenMenuEvent.value = Event(Unit)
     }

    fun pressPrivateLife(){
        isPrivate.value = !isPrivate.value!!
    }

    fun changeValuePrivate( isPrivate: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setStatus(isPrivate)
        }
    }
}