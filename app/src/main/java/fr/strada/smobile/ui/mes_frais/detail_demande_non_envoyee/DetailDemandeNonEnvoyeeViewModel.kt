package fr.strada.smobile.ui.mes_frais.detail_demande_non_envoyee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.mes_frais.Depense
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.data.models.mes_frais.UpdateDepense
import fr.strada.smobile.data.repositories.MesFraisRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

class DetailDemandeNonEnvoyeeViewModel @Inject constructor(
    application: Application,
    val mesFraisRepository: MesFraisRepository
) : AndroidViewModel(application) {

    val detailNoteFrais = MutableLiveData<NoteFrais?>(null)
    val isLoading = MutableLiveData(false)

    private val _updateNoteFraisRespense = MutableLiveData<Resource<ResponseBody>>(Resource.noContent())
    val updateNoteFraisRespense: LiveData<Resource<ResponseBody>> = _updateNoteFraisRespense


    fun addDepense(depenses: Depense) {
        detailNoteFrais.value!!.listDepenses.add(depenses)
        detailNoteFrais.value = detailNoteFrais.value
    }

    fun updateDepense(updateDepense: UpdateDepense)
    {
        detailNoteFrais.value!!.listDepenses[updateDepense.index] = updateDepense.depense
        detailNoteFrais.value = detailNoteFrais.value
    }

    fun removeDepense(position: Int) {
        detailNoteFrais.value!!.listDepenses.removeAt(position)
        detailNoteFrais.value = detailNoteFrais.value
    }

    fun updateNoteFrais(noteFrais: NoteFrais) {
        viewModelScope.launch {
            isLoading.value = true
            val res = mesFraisRepository.updateNoteFrais(noteFrais)
            _updateNoteFraisRespense.postValue(res)
            isLoading.value = false
        }
    }
}