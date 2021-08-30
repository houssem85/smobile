package fr.strada.smobile.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.NotificationModel
import fr.strada.smobile.data.repositories.NotificationRepository
import fr.strada.smobile.utils.Event
import javax.inject.Inject

class NotificationsViewModel @Inject constructor(
    application: Application,
    val notificationRepository: NotificationRepository
) : AndroidViewModel(application) {

    private val _pressBtnOpenMenuEvent = MutableLiveData<Event<Unit>>()
    val pressBtnOpenMenuEvent: LiveData<Event<Unit>> = _pressBtnOpenMenuEvent


    private val _pressBtnReglageEvent = MutableLiveData<Event<Unit>>()
    val pressBtnReglageEvent: LiveData<Event<Unit>> = _pressBtnReglageEvent

    val notifications = MutableLiveData<ArrayList<NotificationModel>>()

    init {
        notifications.value = arrayListOf()
    }

    fun getNotifications() {
        notifications.value = notificationRepository.getAllNotifications()
    }

    fun pressBtnOpenMenu() {
        _pressBtnOpenMenuEvent.value = Event(Unit)
    }

    fun pressBtnReglage() {
        _pressBtnReglageEvent.value = Event(Unit)
    }
}