package fr.strada.smobile.ui.pointeuse_graph

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.pointeuse.Commentaire
import fr.strada.smobile.data.models.pointeuse.TimeModel
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.data.repositories.CommentaireRepository
import fr.strada.smobile.data.repositories.PointeuseRepository
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PointeuseGraphViewModel @Inject constructor( application : Application,
                                                   private val pointeuseRepository : PointeuseRepository,
                                                   private val commentaireRepository : CommentaireRepository) : AndroidViewModel(application) {

    val day = MutableLiveData<Date?>()

    private val _typeActivities = MutableLiveData<Resource<List<TypeActivitePointeuseModel>>>()
    val typeActivities  : LiveData<Resource<List<TypeActivitePointeuseModel>>> = _typeActivities

    private val _activities = MutableLiveData<Resource<List<TimeModel>>>()
    val activities  : LiveData<Resource<List<TimeModel>>> = _activities

    private val _commentaires = MutableLiveData<Resource<List<Commentaire>>>()
    val commentaires  : LiveData<Resource<List<Commentaire>>> = _commentaires

    val ajoutCommentairesResponse = SingleLiveEvent<Resource<Boolean>>()

    init {
        day.value = Date()
    }

    fun getTypeActivitePointeuse()
    {
        viewModelScope.launch {
            val res = pointeuseRepository.getListTypeActivitePointeuse()
            _typeActivities.postValue(res)
        }
    }

    fun getListActivitePointeuseByDay()
    {
        viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            // your date
            val cal = Calendar.getInstance()
            cal.time = day.value!!
            println(sdf.format(cal.time))
            println(sdf.format(day.value!!.time))
            val cal2 = cal
            val cal3 = cal
            cal2.add(Calendar.DATE, -1)
            val datebefore = cal2.time
            val dayBefor = sdf.format(datebefore)
            println(dayBefor)
            cal3.add(Calendar.DATE, 2)
            val dateAfter = cal3.time
            val dayAfter = sdf.format(dateAfter)
            println(dayAfter)
            val res =
                pointeuseRepository.getListActivitePointeuseByDay(day.value!!, sdf.format(cal.time), sdf.format(cal.time))

            _activities.postValue(res)
        }
    }

    fun getCommentairesByDay()
    {
        viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val day = sdf.format(day.value!!)
            val res = commentaireRepository.getListCommentairesParJour(day)
            _commentaires.postValue(res)
        }
    }

    fun ajoutCommentaire(commentaire : String){
        viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val day = sdf.format(day.value!!)
            val res = commentaireRepository.ajoutCommentaire(commentaire,day)
            ajoutCommentairesResponse.postValue(res)
        }
    }

}