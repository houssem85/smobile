package fr.strada.smobile.data.models.indemnite.mensuel


import com.google.gson.annotations.SerializedName

data class IndemniteMensuel(
    @SerializedName("cumulMensuelFrais")
    val cumulMensuelFrais: Int,
    @SerializedName("mois")
    val mois: String,
    @SerializedName("semaineFrais")
    val semaineFrais: List<SemaineFrai>,
    @SerializedName("summaryFrais")
    val summaryFrais: List<SummaryFrai>
)