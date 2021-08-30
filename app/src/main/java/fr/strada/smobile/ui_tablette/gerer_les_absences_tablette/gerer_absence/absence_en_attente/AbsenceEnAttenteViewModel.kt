package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.data.repositories.AbsenceRepository
import javax.inject.Inject

class AbsenceEnAttenteViewModel @Inject constructor(application: Application, val absenceRepository: AbsenceRepository): AndroidViewModel(application) {

    val absencesEnAttente = MutableLiveData<ArrayList<AbsenceModel>>()

    init {
        absencesEnAttente.value = arrayListOf()
    }

    fun getAbsencesEnAttente()
    {
        absencesEnAttente.value = absenceRepository.getAbsenceEnAttente()
    }
}