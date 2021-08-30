package fr.strada.smobile.ui.auth.password

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class ResetPasswordViewModel @Inject constructor(application: Application) : AndroidViewModel(application)  {
    private val _pressBtnContinuer = MutableLiveData<Event<Unit>>()
    val pressBtnContinuerEvent: LiveData<Event<Unit>> = _pressBtnContinuer

    fun pressBtnContinuer() {
        _pressBtnContinuer.value = Event(Unit)
    }
}