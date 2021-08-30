package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

data class CardControlActivityDataRecord(

    @SerializedName("controlType") val controlType: Int,
    @SerializedName("controlTime") val controlTime: String,
    @SerializedName("controlCardNumber") val controlCardNumber: ControlCardNumber,
    @SerializedName("controlVehicleRegistration") val controlVehicleRegistration: ControlVehicleRegistration,
    @SerializedName("controlDownloadPeriodBegin") val controlDownloadPeriodBegin: String,
    @SerializedName("controlDownloadPeriodEnd") val controlDownloadPeriodEnd: String
)