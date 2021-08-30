package fr.strada.smobile.data.models.indemnite.hebdo


import com.google.gson.annotations.SerializedName

data class SummaryFrai(
    @SerializedName("cumulNombreFrais")
    val cumulNombreFrais: Int,
    @SerializedName("typeFraisId")
    val typeFraisId: String,
    @SerializedName("typeFraisLibelle")
    val typeFraisLibelle: String
)