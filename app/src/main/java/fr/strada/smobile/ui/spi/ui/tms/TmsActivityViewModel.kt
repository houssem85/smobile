package fr.strada.smobile.ui.spi.ui.tms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.ui.spi.ui.tms.model.EnlevementLivraison
import fr.strada.smobile.ui.spi.ui.tms.model.TmsRepository
import fr.strada.smobile.ui.spi.ui.tms.model.TourneeItem
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TmsActivityViewModel @Inject constructor(
    application: Application,
    private val mTmsRepository: TmsRepository
) : AndroidViewModel(application) {

    private val _listTournee =MutableStateFlow<Resource<ArrayList<TourneeItem>>>(Resource.noContent())
    val listeTournee : StateFlow<Resource<ArrayList<TourneeItem>>> = _listTournee
    private val _detailTournee =MutableStateFlow<Resource<ArrayList<EnlevementLivraison>>>(Resource.noContent())
    val detailTournee : StateFlow<Resource<ArrayList<EnlevementLivraison>>> = _detailTournee

    fun getListTournee(){

        viewModelScope.launch {
            _listTournee.value = Resource.loading(null)
            val res = mTmsRepository.getListTournee()
            _listTournee.value = res
        }
    }
    fun getdetailTournee( id :  String){

        viewModelScope.launch {
            _detailTournee.value = Resource.loading(null)
            val result = mTmsRepository.getdetailTournee(id)
            _detailTournee.value = result
        }
    }
}