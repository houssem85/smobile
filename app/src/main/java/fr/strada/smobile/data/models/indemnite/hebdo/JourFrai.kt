package fr.strada.smobile.data.models.indemnite.hebdo


import com.google.gson.annotations.SerializedName

data class JourFrai(
    @SerializedName("jour")
    val jour: String,
    @SerializedName("jourMontantFrais")
    val jourMontantFrais: Int,
    @SerializedName("jourNombreFrais")
    val jourNombreFrais: Int
)