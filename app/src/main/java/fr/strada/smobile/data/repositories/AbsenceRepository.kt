package fr.strada.smobile.data.repositories

import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import javax.inject.Inject

class AbsenceRepository @Inject constructor(){

    fun getAbsence(): ArrayList<AbsenceModel> {

        val data: ArrayList<AbsenceModel> = ArrayList()

        data.add(
            AbsenceModel(
                nbrJour = "2",
                statusValidation = "Demande d'absence validée",
                startDate = "15/04/2020",
                endDate = "17/04/2020",
                color = "#CEFFEA",
                image = R.drawable.ic_ok

            )
        )

        data.add(
            AbsenceModel(
                nbrJour = "2",
                statusValidation = "Demande d'absence en attente",
                startDate = "15/04/2020",
                endDate = "17/04/2020",
                color = "#FFDABC",
                image = R.drawable.ic_interrogation
            )
        )

        return data

    }

    fun getAbsenceAValider(): ArrayList<AbsenceModel> {

        val data: ArrayList<AbsenceModel> = ArrayList()

        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "CP",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "CP",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "RC",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "CP",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "CP",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "RC",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "RC",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )

        return data

    }

    fun getAbsenceEnAttente(): ArrayList<AbsenceModel> {

        val data: ArrayList<AbsenceModel> = ArrayList()

        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "RCR",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "RCR",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "RCR",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "RCR",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )

        return data
    }

    fun getAbsenceValidees(): ArrayList<AbsenceModel> {

        val data: ArrayList<AbsenceModel> = ArrayList()

        data.add(
            AbsenceModel(
                nbrJour = "2",
                type = "CP",
                startDate = "18/02/2020",
                endDate = "20/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "2",
                type = "RCR",
                startDate = "18/02/2020",
                endDate = "20/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "2",
                type = "CP",
                startDate = "18/02/2020",
                endDate = "20/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "2",
                type = "CP",
                startDate = "18/02/2020",
                endDate = "20/02/2020",
                author = "Miguel Albert"
            )
        )

        return data
    }

    fun getAbsenceRefusee(): ArrayList<AbsenceModel> {

        val data: ArrayList<AbsenceModel> = ArrayList()

        data.add(
            AbsenceModel(
                nbrJour = "2",
                type = "CP",
                startDate = "18/02/2020",
                endDate = "20/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "2",
                type = "CP",
                startDate = "18/02/2020",
                endDate = "20/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "2",
                type = "RC",
                startDate = "18/02/2020",
                endDate = "20/02/2020",
                author = "Miguel Albert"
            )
        )

        return data
    }

   fun getAllAbsences(): ArrayList<AbsenceModel> {

        val data: ArrayList<AbsenceModel> = ArrayList()

        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "CP",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )
        data.add(
            AbsenceModel(
                nbrJour = "Demi-journée",
                type = "CP",
                startDate = "18/02/2020",
                author = "Miguel Albert"
            )
        )

        return data
    }

}