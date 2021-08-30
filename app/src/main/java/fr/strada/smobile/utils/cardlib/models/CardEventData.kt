package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class CardEventData {
    @SerializedName("cardEventRecords")
    lateinit var cardEventRecords: List<cardEventRecordsInfo>

    constructor()
    constructor(cardEventRecords: List<cardEventRecordsInfo>) {
        this.cardEventRecords = cardEventRecords
    }
}