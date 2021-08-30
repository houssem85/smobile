package fr.strada.smobile.ui.gerer_absence.refusee

import androidx.lifecycle.ViewModel
import fr.strada.smobile.data.repositories.AbsenceRepository

class ListeAbsenceRefuseeViewModel : ViewModel() {

    private val absenceRepository: AbsenceRepository by lazy {

        AbsenceRepository()
    }

    fun getListeAbsenceRefusee () = absenceRepository.getAbsenceRefusee()
}