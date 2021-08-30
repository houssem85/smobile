package fr.strada.smobile.ui.infractions.detail_infraction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.infractions.DetailsInfraction
import fr.strada.smobile.data.models.infractions.Infraction
import fr.strada.smobile.data.repositories.InfractionRepository
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailInfractionViewModel @Inject constructor(appliation:Application ,
                                                    private val infractionRepository: InfractionRepository):AndroidViewModel(appliation) {

    val infraction = MutableStateFlow<Infraction?>(null)

    private val _detailsInfraction = MutableStateFlow<Resource<DetailsInfraction>>(Resource.noContent())
    val detailsInfraction  : StateFlow<Resource<DetailsInfraction>> = _detailsInfraction

    val updateInfractionResponse = SingleLiveEvent<Resource<Boolean>>()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading  : StateFlow<Boolean> = _isLoading

    fun getDetailsInfraction(infractionId : String)
    {
        viewModelScope.launch {
            _detailsInfraction.value = Resource.loading(null)
            val res = infractionRepository.getDetailsInfraction(infractionId)
            _detailsInfraction.value = res
        }
    }

    fun updateInfraction(commentaire:String)
    {
        viewModelScope.launch {
            infraction.value?.let {
                _isLoading.value = true
                val res = infractionRepository.updateInfraction(infraction.value!!.infractionId,commentaire)
                updateInfractionResponse.value = res
                _isLoading.value = false
            }
        }
    }
    
}