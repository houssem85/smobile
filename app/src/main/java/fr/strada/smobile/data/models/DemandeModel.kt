package fr.strada.smobile.data.models

data class DemandeModel(
    val idDemande: Int,
    val nbreDepense: Int? = null,
    val montantTotalDepense: Float? = null,
    val type: String? = null,
    val dateDemande:String? = null
)