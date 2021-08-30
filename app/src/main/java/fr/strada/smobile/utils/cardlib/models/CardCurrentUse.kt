package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

data class CardCurrentUse(

    @SerializedName("sessionOpenTime") val sessionOpenTime: String,
    @SerializedName("sessionOpenVehicle") val sessionOpenVehicle: SessionOpenVehicle
)