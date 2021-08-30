package fr.strada.smobile.data.models.mes_frais


import com.google.gson.annotations.SerializedName

data class TypesDepense(
    @SerializedName("icone")
    val icone: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("libelle")
    val libelle: String
)