package fr.strada.smobile.ui.spi.ui.tms.model


import com.google.gson.annotations.SerializedName

data class TourneeItem(
    @SerializedName("dateDebut")
    val dateDebut: String?,
    @SerializedName("dateFin")
    val dateFin: String?,
    @SerializedName("reference")
    val reference: String?,
    @SerializedName("tourneeId")
    val tourneeId: String?
)