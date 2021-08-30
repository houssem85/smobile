package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.DemandeModel
import javax.inject.Inject

class DemandeFraisRepository @Inject constructor(){

    fun getAllDemandesFrais(): ArrayList<DemandeModel> {

        val data: ArrayList<DemandeModel> = ArrayList()

        data.add(
            DemandeModel(
                idDemande = 6,
                nbreDepense = 2,
                montantTotalDepense = 290.75f,
                type = "envoyee",
                dateDemande = "15/03/2020"
            )
        )

        data.add(
            DemandeModel(
                idDemande = 5,
                nbreDepense = 4,
                montantTotalDepense = 380.15f,
                type = "non envoyee",
                dateDemande = "14/07/2020"
            )
        )

        data.add(
            DemandeModel(
                idDemande = 4,
                nbreDepense = 3,
                montantTotalDepense = 400.75f,
                type = "envoyee",
                dateDemande = "25/07/2020"
            )
        )

        data.add(
            DemandeModel(
                idDemande = 3,
                nbreDepense = 2,
                montantTotalDepense = 150.15f,
                type = "non envoyee",
                dateDemande = "03/04/2020"
            )
        )

        data.add(
            DemandeModel(
                idDemande = 2,
                nbreDepense = 6,
                montantTotalDepense = 590.75f,
                type = "non envoyee",
                dateDemande = "20/06/2020"
            )
        )

        data.add(
            DemandeModel(
                idDemande = 1,
                nbreDepense = 4,
                montantTotalDepense = 300.25f,
                type = "envoyee",
                dateDemande = "26/07/2020"
            )
        )
        return data
    }
}