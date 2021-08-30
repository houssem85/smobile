package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

data class DrivingLicenceInfo(

    @SerializedName("cardDrivingLicenceInformation") val cardDrivingLicenceInformation: CardDrivingLicenceInformation
)