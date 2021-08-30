package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.AbsenceModel

class AbsenceHistoryRepository  {

    fun getAbsenceAccepted(): ArrayList<AbsenceModel>{

        val data: ArrayList<AbsenceModel> = ArrayList()

        data.add(
            AbsenceModel(
                nbrJour = "1 Jour",
                type = "Repos compensateur",
                startDate = "29/03/2020",
                endDate = "06/04/2020"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-Journée",
                type = "Repos compensateur",
                startDate = "29/03/2020",
                endDate = "06/04/2020"
            )
        )
        return data
    }

    fun getAbsenceRefused(): ArrayList<AbsenceModel>{

        val data: ArrayList<AbsenceModel> = ArrayList()

        data.add(
            AbsenceModel(
                nbrJour = "2 Jours",
                type = "Repos compensateur",
                startDate = "29/03/2020",
                endDate = "06/04/2020"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-Journée",
                type = "Repos compensateur",
                startDate = "29/03/2020",
                endDate = "06/04/2020"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-Journée",
                type = "Repos compensateur",
                startDate = "29/03/2020",
                endDate = "06/04/2020"
            )
        )
        return data
    }
}