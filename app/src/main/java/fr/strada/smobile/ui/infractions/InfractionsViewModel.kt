package fr.strada.smobile.ui.infractions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.infractions.Infraction
import fr.strada.smobile.data.models.infractions.InfractionsCategorie
import fr.strada.smobile.data.repositories.InfractionRepository
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfractionsViewModel @Inject constructor(application : Application, private val infractionRepository : InfractionRepository): AndroidViewModel(application) {

   private val _infractionsCategories = MutableStateFlow<Resource<List<InfractionsCategorie>>>(Resource.noContent())
   val infractionsCategories  : StateFlow<Resource<List<InfractionsCategorie>>> = _infractionsCategories

    private val _infractions = MutableStateFlow<Resource<List<Infraction>>>(Resource.noContent())
    val infractions : StateFlow<Resource<List<Infraction>>> = _infractions

    val pressItemInfractionEvent = SingleLiveEvent<Infraction?>()

    val hideDetailsInfractionEvent = SingleLiveEvent<Unit>()

   fun getInfractionsCategories()
   {
       viewModelScope.launch(Dispatchers.IO) {
           val res = infractionRepository.getInfractionsCategories()
           _infractionsCategories.value = res
       }
   }

    fun getInfractions(dateDebut : String? = null,dateFin:String? = null,infractionClass: Int? = null)
    {
        viewModelScope.launch {
            _infractions.value = Resource.loading(null)
            val res = infractionRepository.getInfractions(dateDebut,dateFin,infractionClass)
            _infractions.value = res
        }
    }

    fun setPressItemInfractionEvent(infraction:Infraction?)
    {
        pressItemInfractionEvent.postValue(infraction)
    }

    fun setHideDetailsInfractionEvent()
    {
        hideDetailsInfractionEvent.postValue(Unit)
    }
}