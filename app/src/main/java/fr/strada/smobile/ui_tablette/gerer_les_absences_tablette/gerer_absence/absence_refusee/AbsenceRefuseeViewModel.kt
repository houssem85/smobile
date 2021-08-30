package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.data.repositories.AbsenceRepository
import javax.inject.Inject

class AbsenceRefuseeViewModel @Inject constructor(application: Application, val absenceRepository: AbsenceRepository): AndroidViewModel(application) {

    val absencesRefusee = MutableLiveData<ArrayList<AbsenceModel>>()

    init {
        absencesRefusee.value = arrayListOf()
    }

    fun getAbsencesRefusee()
    {
        absencesRefusee.value = absenceRepository.getAbsenceRefusee()
    }
}