package fr.strada.smobile.ui.gerer_absence.calendrier_equipe.demande_absence

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.data.repositories.AbsenceRepository
import javax.inject.Inject

class DemandeAbsenceViewModel @Inject constructor(application: Application, val absenceRepository: AbsenceRepository): AndroidViewModel(application) {

    val demandesAbsence = MutableLiveData<ArrayList<AbsenceModel>>()

    init {
        demandesAbsence.value = arrayListOf()
    }

    fun getDemandesAbsences()
    {
        demandesAbsence.value = absenceRepository.getAllAbsences()
    }

}