package fr.strada.smobile.data.models.userinfo


import com.google.gson.annotations.SerializedName

data class ModuleFunctionalityRow(
    @SerializedName("code")
    val code: String,
    @SerializedName("functionalityRows")
    val functionalityRows: List<FunctionalityRow>,
    @SerializedName("id")
    val id: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("productTitle")
    val productTitle: String
)