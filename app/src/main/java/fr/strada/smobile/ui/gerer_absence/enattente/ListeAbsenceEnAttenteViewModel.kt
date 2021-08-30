package fr.strada.smobile.ui.gerer_absence.enattente

import androidx.lifecycle.ViewModel
import fr.strada.smobile.data.repositories.AbsenceRepository

class ListeAbsenceEnAttenteViewModel:ViewModel() {

    private val absenceRepository: AbsenceRepository by lazy {

        AbsenceRepository()
    }

    fun getListeAbsenceEnAttente () = absenceRepository.getAbsenceEnAttente()
}