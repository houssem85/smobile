package fr.strada.smobile.ui.spi.ui.tms.model


import com.google.gson.annotations.SerializedName

data class TypeCarosserieRow(
    @SerializedName("code")
    val code: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("libelle")
    val libelle: String?
)