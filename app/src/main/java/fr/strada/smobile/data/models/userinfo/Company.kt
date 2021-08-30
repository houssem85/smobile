package fr.strada.smobile.data.models.userinfo


import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("id")
    val id: String,
    @SerializedName("languageId")
    val languageId: String,
    @SerializedName("languageLabel")
    val languageLabel: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("tenants")
    val tenants: List<Tenant>
)