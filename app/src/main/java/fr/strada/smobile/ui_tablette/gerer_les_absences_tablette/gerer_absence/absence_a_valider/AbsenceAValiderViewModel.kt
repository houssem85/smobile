package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.data.repositories.AbsenceRepository
import javax.inject.Inject

class AbsenceAValiderViewModel @Inject constructor(application: Application, val absenceRepository: AbsenceRepository): AndroidViewModel(application) {

    val absencesAValider = MutableLiveData<ArrayList<AbsenceModel>>()

    init {
        absencesAValider.value = arrayListOf()
    }

    fun getAbsencesAValider()
    {
        absencesAValider.value = absenceRepository.getAbsenceAValider()
    }
}