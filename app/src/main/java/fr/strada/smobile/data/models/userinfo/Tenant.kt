package fr.strada.smobile.data.models.userinfo


import com.google.gson.annotations.SerializedName

data class Tenant(
    @SerializedName("compagny")
    val compagny: Company,
    @SerializedName("compagnyId")
    val compagnyId: String,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("namespace")
    val namespace: String,
    @SerializedName("statusTenantCode")
    val statusTenantCode: String,
    @SerializedName("statusTenantLabel")
    val statusTenantLabel: String,
    @SerializedName("url")
    val url: String
)