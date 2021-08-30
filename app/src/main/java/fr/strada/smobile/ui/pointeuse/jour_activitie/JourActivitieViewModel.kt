package fr.strada.smobile.ui.pointeuse.jour_activitie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.strada.smobile.data.models.pointeuse.Commentaire
import fr.strada.smobile.data.models.pointeuse.JourActivite
import fr.strada.smobile.data.repositories.CommentaireRepository
import fr.strada.smobile.data.repositories.PointeuseRepository
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.SingleLiveEvent
import fr.strada.smobile.utils.Status
import kotlinx.coroutines.launch
import java.util.Collections.sort
import javax.inject.Inject

class JourActivitieViewModel @Inject constructor(application: Application,
                                                 private val commentaireRepository : CommentaireRepository,
                                                 private val pointeuseRepository : PointeuseRepository) : AndroidViewModel(application){

    val jourActivitie = MutableLiveData<JourActivite>()

    private val _commentaires = MutableLiveData<Resource<List<Commentaire>>>()
    val commentaires  : LiveData<Resource<List<Commentaire>>> = _commentaires

    val ajoutCommentairesResponse = SingleLiveEvent<Resource<Boolean>>()

    fun ajoutCommentaire(commentaire : String)
    {
        viewModelScope.launch {
            val jour = jourActivitie.value!!.jourActivite!!.take(10)
            val res = commentaireRepository.ajoutCommentaire(commentaire, jour)
            ajoutCommentairesResponse.postValue(res)
        }
    }

    fun getCommentaires()
    {
        viewModelScope.launch()
        {
            val jour = jourActivitie.value!!.jourActivite!!.take(10)
            val res = commentaireRepository.getListCommentairesParJour(jour)
            _commentaires.postValue(res)
        }
    }

    fun getJourActivities(jour : String)
    {
        viewModelScope.launch {
            val res = pointeuseRepository.getLastSixDaysActivites(jour)
            if(res.status == Status.SUCCESS){
                val data = res.data!!
                if(data.isNotEmpty()){
                    jourActivitie.postValue(data.first())
                }
            }
        }
    }

}
