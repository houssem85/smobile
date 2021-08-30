package fr.strada.smobile.data.models.indemnite.journalier


import com.google.gson.annotations.SerializedName

data class FraisRow(
    @SerializedName("montantFrais")
    val montantFrais: Int,
    @SerializedName("typeFraisId")
    val typeFraisId: String,
    @SerializedName("typeFraisLibelle")
    val typeFraisLibelle: String
)