package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class ApplicationIdentification {
    @SerializedName("driverCardApplicationIdentification")
    lateinit var driverCardApplicationIdentification: DriverCardApplicationIdentification

    constructor()
    constructor(driverCardApplicationIdentification: DriverCardApplicationIdentification) {
        this.driverCardApplicationIdentification = driverCardApplicationIdentification
    }
}