package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class CardIccIdentification {
    @SerializedName("clockStop")
    var clockStop: Int = 0
    @SerializedName("cardExtendedSerialNumber")
    var cardExtendedSerialNumber: CardExtendedSerialNumber? = null
    @SerializedName("cardApprovalNumber")
    var cardApprovalNumber: String? = null
    @SerializedName("cardPersonnaliserID")
    var cardPersonnaliserID: Int = 0
    @SerializedName("embedderIcAssemblerId")
    var embedderIcAssemblerId: EmbedderIcAssemblerId? = null
    @SerializedName("icIdentifier")
    var icIdentifier: String? = null

    constructor()
    constructor(
        clockStop: Int,
        cardExtendedSerialNumber: CardExtendedSerialNumber,
        cardApprovalNumber: String,
        cardPersonnaliserID: Int,
        embedderIcAssemblerId: EmbedderIcAssemblerId,
        icIdentifier: String
    ) {
        this.clockStop = clockStop
        this.cardExtendedSerialNumber = cardExtendedSerialNumber
        this.cardApprovalNumber = cardApprovalNumber
        this.cardPersonnaliserID = cardPersonnaliserID
        this.embedderIcAssemblerId = embedderIcAssemblerId
        this.icIdentifier = icIdentifier
    }

    override fun toString(): String {
        return "CardIccIdentification(\n clockStop=$clockStop \n , cardExtendedSerialNumber=${cardExtendedSerialNumber.toString()}, \n cardApprovalNumber=$cardApprovalNumber, \n cardPersonnaliserID=$cardPersonnaliserID, \n icIdentifier=$icIdentifier)"
    }

}