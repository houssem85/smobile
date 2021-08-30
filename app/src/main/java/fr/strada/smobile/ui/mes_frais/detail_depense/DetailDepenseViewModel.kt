package fr.strada.smobile.ui.mes_frais.detail_depense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.data.models.mes_frais.TypesDepense
import fr.strada.smobile.data.models.mes_frais.UpdateDepense
import fr.strada.smobile.data.repositories.MesFraisRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailDepenseViewModel @Inject constructor(application: Application,private val mesFraisRepository: MesFraisRepository): AndroidViewModel(application) {

    var updateDepense : UpdateDepense? = null
    val dateDepense = MutableLiveData<String>()
    val dayDepense = MutableLiveData<Day>()
    val typeDepense = MutableLiveData<TypesDepense>()
    val montant = MutableLiveData<String>()
    val commentaire = MutableLiveData<String>()

    private val _typesDepense = MutableStateFlow<Resource<List<TypesDepense>>>(Resource.noContent())
    val typesDepense  : StateFlow<Resource<List<TypesDepense>>> = _typesDepense

    fun getTypesDepense()
    {
        viewModelScope.launch {
            val res = mesFraisRepository.getListTypesDepense()
            _typesDepense.value = res
        }
    }

}