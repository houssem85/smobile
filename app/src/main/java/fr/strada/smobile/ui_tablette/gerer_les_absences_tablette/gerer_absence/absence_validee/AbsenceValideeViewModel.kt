package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.data.repositories.AbsenceRepository
import javax.inject.Inject

class AbsenceValideeViewModel @Inject constructor(application: Application, val absenceRepository: AbsenceRepository): AndroidViewModel(application) {

    val absencesValidee = MutableLiveData<ArrayList<AbsenceModel>>()

    init {
        absencesValidee.value = arrayListOf()
    }

    fun getAbsencesValidee()
    {
        absencesValidee.value = absenceRepository.getAbsenceValidees()
    }
}