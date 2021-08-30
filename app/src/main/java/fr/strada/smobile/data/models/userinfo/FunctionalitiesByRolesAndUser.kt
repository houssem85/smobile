package fr.strada.smobile.data.models.userinfo


import com.google.gson.annotations.SerializedName

data class FunctionalitiesByRolesAndUser(
    @SerializedName("code")
    val code: String,
    @SerializedName("codeUniverse")
    val codeUniverse: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("functionalityRows")
    val functionalityRows: List<FunctionalityRow>?,
    @SerializedName("id")
    val id: String,
    @SerializedName("idUniverse")
    val idUniverse: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("labelUniverse")
    val labelUniverse: String,
    @SerializedName("moduleFunctionalityRows")
    val moduleFunctionalityRows: List<ModuleFunctionalityRow>?
)