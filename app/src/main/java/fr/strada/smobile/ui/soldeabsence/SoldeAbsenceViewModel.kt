package fr.strada.smobile.ui.soldeabsence

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fr.strada.smobile.data.repositories.AbsenceInProgressRepository
import javax.inject.Inject

class SoldeAbsenceViewModel @Inject constructor(application: Application):AndroidViewModel(application){

    private val absenceInProgressRepository: AbsenceInProgressRepository by lazy {
        AbsenceInProgressRepository()
    }

    fun getSoldeAbsence () = absenceInProgressRepository.getAbsenceInProgress()

    fun resetViewModel(){
    }
}