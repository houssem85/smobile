package fr.strada.smobile.ui.absencehistory.absence_acceptee

import androidx.lifecycle.ViewModel
import fr.strada.smobile.data.repositories.AbsenceHistoryRepository

class DemandesAbsencesAccepteesViewModel : ViewModel() {

    private val absenceHistoryRepository: AbsenceHistoryRepository by lazy {

        AbsenceHistoryRepository()
    }

    fun getAbsenceAccepted () = absenceHistoryRepository.getAbsenceAccepted()
}