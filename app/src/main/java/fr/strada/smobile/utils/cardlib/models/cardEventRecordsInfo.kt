package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

data class cardEventRecordsInfo(

    @SerializedName("eventType") val eventType: Int,
    @SerializedName("eventBeginTime") val eventBeginTime: String,
    @SerializedName("eventEndTime") val eventEndTime: String,
    @SerializedName("eventVehicleRegistration") val eventVehicleRegistration: EventVehicleRegistration
)