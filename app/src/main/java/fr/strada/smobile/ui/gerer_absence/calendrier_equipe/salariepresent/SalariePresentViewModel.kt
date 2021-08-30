package fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salariepresent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.data.models.SalarieModel
import fr.strada.smobile.data.repositories.SalarieRepository
import javax.inject.Inject

class SalariePresentViewModel @Inject constructor(application: Application, val salarieRepository: SalarieRepository): AndroidViewModel(application) {

    val salariesPresents = MutableLiveData<ArrayList<SalarieModel>>()

    init {
        salariesPresents.value = arrayListOf()
    }

    fun getSalariePresents()
    {
        salariesPresents.value = salarieRepository.getAllPresentEmployee()
    }
}