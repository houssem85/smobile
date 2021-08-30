package fr.strada.smobile.ui.indemnites.mensuel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.indemnite.mensuel.IndemniteMensuel
import fr.strada.smobile.data.repositories.IndemniteRepository
import fr.strada.smobile.utils.Event
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class IndemniteMensuelleViewModel @Inject constructor(
    application: Application,
    private val indemniteRepository: IndemniteRepository
) : AndroidViewModel(application) {

    private val _pressBtnChangeMonthEvent = MutableLiveData<Event<Unit>?>()
    val pressBtnChangeMonthEvent: MutableLiveData<Event<Unit>?> = _pressBtnChangeMonthEvent

    val month = MutableLiveData<Int>()
    val year = MutableLiveData<Int>()

    val totalCumulMensuel = MutableLiveData<Int>()

    var isDialogChangeMonthShown = false
    private val _indemnitmensuell = MutableLiveData<Resource<IndemniteMensuel>>()
    val indemnitmensuell: LiveData<Resource<IndemniteMensuel>> = _indemnitmensuell

    init {
        totalCumulMensuel.value = 0
    }

    fun pressBtnChangeMonth() {
        _pressBtnChangeMonthEvent.value = Event(Unit)
    }

    fun resetViewModel() {
        _pressBtnChangeMonthEvent.value = null
    }

    fun getIndemniteMensuell(strStart: String, strEnd: String) {
        viewModelScope.launch {
            _indemnitmensuell.postValue(Resource.loading(null))
            val res = indemniteRepository.getIndemnitemensuell(strStart, strEnd)
            _indemnitmensuell.postValue(res)
        }
    }


}