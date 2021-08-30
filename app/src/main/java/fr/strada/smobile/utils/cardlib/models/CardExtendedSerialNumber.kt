package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class CardExtendedSerialNumber {
    @SerializedName("serialNumber")
    var serialNumber: Int = 0
    @SerializedName("month")
    var month: Int = 0
    @SerializedName("year")
    var year: Int = 0
    @SerializedName("type")
    var type: Int = 0
    @SerializedName("manufacturerCode")
    var manufacturerCode: Int = 0

    constructor()
    constructor(serialNumber: Int, month: Int, year: Int, type: Int, manufacturerCode: Int) {
        this.serialNumber = serialNumber
        this.month = month
        this.year = year
        this.type = type
        this.manufacturerCode = manufacturerCode
    }

    override fun toString(): String {
        return "CardExtendedSerialNumber( \n serialNumber=$serialNumber, \n month=$month, \n year=$year, \n type=$type, \n manufacturerCode=$manufacturerCode)"
    }


}
