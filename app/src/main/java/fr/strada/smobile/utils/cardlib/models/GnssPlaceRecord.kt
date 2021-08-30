package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class GnssPlaceRecord() {
    @SerializedName("timestamp")
    var timestamp: String = ""
    @SerializedName("gnssAccuracy")
    var gnssAccuracy: Int = 0
    @SerializedName("geoCoordinates")
    var geoCoordinates: GeoCoordinates = GeoCoordinates()
}