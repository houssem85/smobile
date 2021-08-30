package fr.strada.smobile.ui.gerer_absence.validee

import androidx.lifecycle.ViewModel
import fr.strada.smobile.data.repositories.AbsenceRepository

class ListeAbsenceValideeViewModel : ViewModel() {

    private val absenceRepository: AbsenceRepository by lazy {

        AbsenceRepository()
    }

    fun getListeAbsenceValidees () = absenceRepository.getAbsenceValidees()
}