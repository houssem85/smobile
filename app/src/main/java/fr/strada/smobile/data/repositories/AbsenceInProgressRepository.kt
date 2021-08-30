package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.AbsenceModel
import javax.inject.Inject

class AbsenceInProgressRepository @Inject constructor(){

    fun getAbsenceInProgress(): ArrayList<AbsenceModel>{

        val data: ArrayList<AbsenceModel> = ArrayList()

        data.add(
            AbsenceModel(
                nbrJour = "2",
                startDate= "15/04/2020",
                endDate = "17/04/2020",
                type = "Congé payé"

            )
        )

        data.add(
            AbsenceModel(
                nbrJour = "2",
                startDate = "",
                endDate = "25/04/2020",
                type = "Récupération"

            )
        )

        data.add(
            AbsenceModel(
                nbrJour = "2",
                startDate = "",
                endDate = "28/04/2020",
                type = "Repos compensateur"
            )
        )

        return data

    }

}