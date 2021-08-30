package fr.strada.smobile.ui.spi.ui.tms.model


import com.google.gson.annotations.SerializedName

data class EnlevementLivraison(
    @SerializedName("adresse1")
    val adresse1: String?,
    @SerializedName("adresse2")
    val adresse2: String?,
    @SerializedName("codePostal")
    val codePostal: String?,
    @SerializedName("commandeId")
    val commandeId: String?,
    @SerializedName("dateSouhaite")
    val dateSouhaite: String?,
    @SerializedName("heureSouhaiteMax")
    val heureSouhaiteMax: String?,
    @SerializedName("heureSouhaiteMin")
    val heureSouhaiteMin: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("isExpedition")
    val isExpedition: Boolean?,
    @SerializedName("mlp")
    val mlp: Int?,
    @SerializedName("nom")
    val nom: String?,
    @SerializedName("pays")
    val pays: String?,
    @SerializedName("poids")
    val poids: Int?,
    @SerializedName("statueCommande")
    val statueCommande: String?,
    @SerializedName("typeCarosserieRows")
    val typeCarosserieRows: List<TypeCarosserieRow>?,
    @SerializedName("typeMaterielRows")
    val typeMaterielRows: List<TypeMaterielRow>?,
    @SerializedName("ville")
    val ville: String?,
    @SerializedName("volume")
    val volume: Int?
)