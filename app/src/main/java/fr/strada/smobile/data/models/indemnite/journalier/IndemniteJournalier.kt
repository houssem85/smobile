package fr.strada.smobile.data.models.indemnite.journalier


import com.google.gson.annotations.SerializedName

data class IndemniteJournalier(
    @SerializedName("cumulJournalierFrais")
    val cumulJournalierFrais: Int,
    @SerializedName("fraisRows")
    val fraisRows: List<FraisRow>,
    @SerializedName("jour")
    val jour: String
)