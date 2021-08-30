package fr.strada.smobile.data.models.indemnite.hebdo


import com.google.gson.annotations.SerializedName

data class IndemniteHebdo(
    @SerializedName("cumulHebdoFrais")
    val cumulHebdoFrais: Int,
    @SerializedName("jourFrais")
    val jourFrais: List<JourFrai>,
    @SerializedName("semaine")
    val semaine: String,
    @SerializedName("summaryFrais")
    val summaryFrais: List<SummaryFrai>
)