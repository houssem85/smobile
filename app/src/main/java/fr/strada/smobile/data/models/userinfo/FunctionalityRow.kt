package fr.strada.smobile.data.models.userinfo


import com.google.gson.annotations.SerializedName

data class FunctionalityRow(
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("idUniverse")
    val idUniverse: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("moduleId")
    val moduleId: String,
    @SerializedName("productRangeId")
    val productRangeId: String,
    @SerializedName("productRangeLabel")
    val productRangeLabel: String
)