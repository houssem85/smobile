package fr.strada.smobile.ui.mes_frais

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.data.repositories.MesFraisRepository
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

class MesFraisViewModel @Inject constructor(
    application: Application,
    private val mesFraisRepository: MesFraisRepository
) : AndroidViewModel(application) {

    private val _notesFrais = MutableStateFlow<Resource<List<NoteFrais>>>(Resource.noContent())
    val notesFrais : StateFlow<Resource<List<NoteFrais>>> = _notesFrais

    val deleteNoteFraisRespense = SingleLiveEvent<Resource<ResponseBody>>()

    val strSearch = MutableLiveData("")

    fun getListNoteFrais(dateDebut : String ?= null,dateFin : String ?= null,enCour:Boolean? = null){
        viewModelScope.launch {
            _notesFrais.value = Resource.loading(null)
            val res = mesFraisRepository.getListNoteFrais(dateDebut,dateFin,enCour)
            _notesFrais.value = res
        }
    }

    fun deleteNoteFrais(noteFriasId : String){
        viewModelScope.launch {
            val res  = mesFraisRepository.deleteNoteFrais(noteFriasId)
            deleteNoteFraisRespense.postValue(res)
        }
    }
}