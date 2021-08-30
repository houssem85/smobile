package fr.strada.smobile.ui.gerer_absence.avalider

import androidx.lifecycle.ViewModel
import fr.strada.smobile.data.repositories.AbsenceRepository

class ListeAbsenceAvaliderViewModel: ViewModel() {

    private val absenceRepository: AbsenceRepository by lazy {

        AbsenceRepository()
    }

    fun getListeAbsenceAValider () = absenceRepository.getAbsenceAValider()
}