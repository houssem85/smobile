package fr.strada.smobile.data.models.indemnite.mensuel


import com.google.gson.annotations.SerializedName

data class SemaineFrai(
    @SerializedName("semaine")
    var jour: String,
    @SerializedName("semaineMontantFrais")
    val jourMontantFrais: Int,
    @SerializedName("semaineNombreFrais")
    val jourNombreFrais: Int
)