package fr.strada.smobile.data.models.userinfo


import com.google.gson.annotations.SerializedName

data class Driver(
    @SerializedName("birthdayDate")
    val birthdayDate: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("driverCode")
    val driverCode: String,
    @SerializedName("formation")
    val formation: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("licenceType")
    val licenceType: String,
    @SerializedName("odometer")
    val odometer: Int,
    @SerializedName("truckModel")
    val truckModel: String,
    @SerializedName("zelloName")
    val zelloName: String,
    @SerializedName("zipCode")
    val zipCode: String
)