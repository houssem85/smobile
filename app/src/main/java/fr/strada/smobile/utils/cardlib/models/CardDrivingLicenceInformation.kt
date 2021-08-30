package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

data class CardDrivingLicenceInformation(

    @SerializedName("drivingLicenceIssuingAuthority") val drivingLicenceIssuingAuthority: String,
    @SerializedName("drivingLicenceIssuingNation") val drivingLicenceIssuingNation: Int,
    @SerializedName("drivingLicenceNumber") val drivingLicenceNumber: String
)