package fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.demande_absence_en_attente

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.data.repositories.AbsenceInProgressRepository
import javax.inject.Inject

class DemandeAbsenceEnAttenteViewModel @Inject constructor(application: Application, val absenceInProgressRepository: AbsenceInProgressRepository): AndroidViewModel(application){

    val absencesEnAttente = MutableLiveData<ArrayList<AbsenceModel>>()

    init {
        absencesEnAttente.value = arrayListOf()
    }

    fun getAbsencesInProgress()
    {
        absencesEnAttente.value = absenceInProgressRepository.getAbsenceInProgress()
    }
}