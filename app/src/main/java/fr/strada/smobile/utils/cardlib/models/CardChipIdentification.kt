package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class CardChipIdentification(
    @SerializedName("icSerialNumber") var icSerialNumber: String,
    @SerializedName("icManufacturingReferences") var icManufacturingReferences: String
) {

    override fun toString(): String {
        return "CardChipIdentification(icSerialNumber='$icSerialNumber', icManufacturingReferences='$icManufacturingReferences')"
    }
}