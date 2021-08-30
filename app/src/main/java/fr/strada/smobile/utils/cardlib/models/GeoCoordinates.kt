package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class GeoCoordinates() {

    @SerializedName("latitude")
    var latitude: String = ""
    @SerializedName("longitude")
    var longitude: String = ""
}