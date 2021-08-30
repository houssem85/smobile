package fr.strada.smobile.data.models.userinfo


import com.google.gson.annotations.SerializedName

data class UserPhoto(
    @SerializedName("contentType")
    val contentType: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("userId")
    val userId: String
)