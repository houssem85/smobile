package fr.strada.smobile.ui.mes_frais.nouvelle_demande

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.mes_frais.Depense
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.data.repositories.MesFraisRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

class NouvelleDemandeViewModel @Inject constructor(application: Application,
                                                   private val mesFraisRepository : MesFraisRepository):AndroidViewModel(application) {

    val depenses = MutableLiveData<ArrayList<Depense>>(ArrayList())
    val titre = MutableLiveData("")
    val commentaire = MutableLiveData("")
    val isLoading = MutableLiveData(false)

    private val _createNoteFraisRespense = MutableLiveData<Resource<ResponseBody>>(Resource.noContent())
    val createNoteFraisRespense  : LiveData<Resource<ResponseBody>> = _createNoteFraisRespense

    fun addDepense(depenses : Depense)
    {
        this.depenses.value!!.add(depenses)
        this.depenses.value = this.depenses.value
    }

    fun removeDepense(position : Int)
    {
        this.depenses.value!!.removeAt(position)
        this.depenses.value = this.depenses.value
    }

    fun createNoteFrais(noteFrais: NoteFrais){
       viewModelScope.launch {
           isLoading.value = true
           val res = mesFraisRepository.createNoteFrais(noteFrais)
           _createNoteFraisRespense.postValue(res)
           isLoading.value = false
       }
    }
}