package fr.strada.smobile.ui.absencehistory.absence_refusee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.data.repositories.AbsenceHistoryRepository

class DemandesAbsencesRefuseesViewModel : ViewModel() {

    val absencesRefused = MutableLiveData<List<AbsenceModel>>()
    private val absenceHistoryRepository: AbsenceHistoryRepository by lazy {

        AbsenceHistoryRepository()
    }


    fun getAbsenceRefused () = absenceHistoryRepository.getAbsenceRefused()
}