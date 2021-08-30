package fr.strada.smobile.ui.mes_frais.nouvelle_depense

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.data.models.mes_frais.TypesDepense
import fr.strada.smobile.data.repositories.MesFraisRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NouvelleDepenseViewModel @Inject constructor(
    application: Application,
    private val mesFraisRepository: MesFraisRepository
) : AndroidViewModel(application) {

    val dateDepense = MutableLiveData("")
    val dayDepense = MutableLiveData<Day>()
    val strTypeDepense = MutableLiveData("")
    val typeDepense = MutableLiveData<TypesDepense>()
    val montant = MutableLiveData("")
    val commentaire = MutableLiveData("")

    val images: ObservableList<File> = ObservableArrayList()

    private val _typesDepense = MutableStateFlow<Resource<List<TypesDepense>>>(Resource.noContent())
    val typesDepense: StateFlow<Resource<List<TypesDepense>>> = _typesDepense

    init {
        val sfd = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        dayDepense.value = Day(year, month, day)
        dateDepense.value = sfd.format(calendar.time)
    }


    fun getTypesDepense() {
        viewModelScope.launch {
            val res = mesFraisRepository.getListTypesDepense()
            _typesDepense.value = res
        }
    }

    fun reset() {
        val sfd = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        dayDepense.value = Day(year, month, day)
        dateDepense.value = sfd.format(calendar.time)
        montant.value = ""
        commentaire.value = ""
        strTypeDepense.value = ""
        images.clear()
    }
}