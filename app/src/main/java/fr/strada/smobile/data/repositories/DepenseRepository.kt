package fr.strada.smobile.data.repositories

import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.data.models.DepenseModel
import javax.inject.Inject

class DepenseRepository @Inject constructor() {

    fun getDepenseDisabled(): ArrayList<DepenseModel> {

        val data: ArrayList<DepenseModel> = ArrayList()

        data.add(
            DepenseModel(
                type = "Carburant",
                montant = 290.5,
                date = Day(2020, 10, 15)
            )
        )

        data.add(
            DepenseModel(
                type = "Hôtel",
                montant = 380.15,
                date = Day(2020, 7, 25)
            )
        )

        data.add(
            DepenseModel(
                type = "Péage",
                montant = 400.75,
                date = Day(2020, 4, 6)
            )
        )

        return data
    }

    fun getAllDepenses(): ArrayList<DepenseModel> {

        val data: ArrayList<DepenseModel> = ArrayList()

        data.add(
            DepenseModel(
                type = "Carburant",
                montant = 122.5,
                date = Day(2020, 10, 15),
                fileIsJoined = false,
                commentIsSet = true
            )
        )

        data.add(
            DepenseModel(
                type = "Hôtel",
                montant = 144.15,
                date = Day(2020, 7, 25),
                fileIsJoined = true,
                commentIsSet = true
            )
        )

        data.add(
            DepenseModel(
                type = "Péage",
                montant = 300.75,
                date = Day(2020, 4, 6),
                fileIsJoined = true,
                commentIsSet = false
            )
        )

        return data
    }

}