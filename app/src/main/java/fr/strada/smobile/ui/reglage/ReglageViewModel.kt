package fr.strada.smobile.ui.reglage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.local.SettingsPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class ReglageViewModel @Inject constructor(
    application: Application,
    private val settingsPreferences: SettingsPreferences
) : AndroidViewModel(application) {

    val isItemNotificationsActive = MutableLiveData<Boolean>()
    val isGestionDesCongesActive = MutableLiveData<Boolean>()
    val isNotesDeFraisActive = MutableLiveData<Boolean>()
    val isPointeuseActive = MutableLiveData<Boolean>()
    val isInfractionsActive = MutableLiveData<Boolean>()
    val isMessagrieActive = MutableLiveData<Boolean>()
    val minutes = MutableLiveData<String>()
    val hours = MutableLiveData<String>()
    val isSynchronized = MutableLiveData<Boolean>()
    var isDialogTempsDeTravailShown = false
    val isLogged = settingsPreferences.logged.asLiveData()
    init {
        isItemNotificationsActive.value = false
        isGestionDesCongesActive.value = false
        isNotesDeFraisActive.value = false
        isPointeuseActive.value = false
        isInfractionsActive.value = false
        isMessagrieActive.value = false


        minutes.value = "00"
        hours.value = "10"
    }

    fun initializeSync() {
        viewModelScope.launch {
            try {
                isSynchronized.value = settingsPreferences.logged.first()
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }


    }

    fun pressItemNotifications() {
        isItemNotificationsActive.value = !isItemNotificationsActive.value!!
    }

    fun changeValueSynchronized( issynchronized: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setStatus(issynchronized)
        }
    }
}