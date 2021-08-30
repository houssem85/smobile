package fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salarieabsent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.SalarieModel
import fr.strada.smobile.data.repositories.SalarieRepository
import javax.inject.Inject

class SalarieAbsentViewModel @Inject constructor(application: Application, val salarieRepository: SalarieRepository): AndroidViewModel(application) {

    val salariesAbsents = MutableLiveData<ArrayList<SalarieModel>>()

    init {
        salariesAbsents.value = arrayListOf()
    }

    fun getSalarieAbsents()
    {
        salariesAbsents.value = salarieRepository.getAllAbsentEmployee()
    }
}