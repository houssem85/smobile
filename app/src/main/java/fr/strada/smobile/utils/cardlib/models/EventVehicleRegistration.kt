package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

data class EventVehicleRegistration(

    @SerializedName("vehicleRegistrationNation") val vehicleRegistrationNation: Int,
    @SerializedName("vehicleRegistrationNumber") val vehicleRegistrationNumber: String
)