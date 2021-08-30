package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.SalarieModel
import javax.inject.Inject

class SalarieRepository @Inject constructor(){

    fun getAllPresentEmployee(): ArrayList<SalarieModel> {

        val data: ArrayList<SalarieModel> = ArrayList()

        data.add(
            SalarieModel(
                telephoneNumber = "0750203040",
                name = "Miguel Albert"
            )
        )
        data.add(
            SalarieModel(
                telephoneNumber = "0750203040",
                name = "Miguel Albert"
            )
        )
        data.add(
            SalarieModel(
                telephoneNumber = "0750203040",
                name = "Miguel Albert"
            )
        )
        data.add(
            SalarieModel(
                telephoneNumber = "0750203040",
                name = "Miguel Albert"
            )
        )
        return data
    }

    fun getAllAbsentEmployee(): ArrayList<SalarieModel> {

        val data: ArrayList<SalarieModel> = ArrayList()

        data.add(
            SalarieModel(
                name = "Miguel Albert",
                startDateAbsence = "18/02/2020",
                endDateAbsence = "20/02/2020"
            )
        )
        data.add(
            SalarieModel(
                name = "Miguel Albert",
                startDateAbsence = "18/02/2020",
                endDateAbsence = "20/02/2020"            )
        )
        data.add(
            SalarieModel(
                name = "Miguel Albert",
                startDateAbsence = "18/02/2020",
                endDateAbsence = "20/02/2020"            )
        )
        data.add(
            SalarieModel(
                name = "Miguel Albert",
                startDateAbsence = "18/02/2020",
                endDateAbsence = "20/02/2020"            )
        )
        data.add(
            SalarieModel(
                name = "Miguel Albert",
                startDateAbsence = "18/02/2020",
                endDateAbsence = "20/02/2020"
            )
        )
        data.add(
            SalarieModel(
                name = "Miguel Albert",
                startDateAbsence = "18/02/2020",
                endDateAbsence = "20/02/2020"            )
        )
        data.add(
            SalarieModel(
                name = "Miguel Albert",
                startDateAbsence = "18/02/2020",
                endDateAbsence = "20/02/2020"            )
        )
        data.add(
            SalarieModel(
                name = "Miguel Albert",
                startDateAbsence = "18/02/2020",
                endDateAbsence = "20/02/2020"            )
        )
        return data
    }


}