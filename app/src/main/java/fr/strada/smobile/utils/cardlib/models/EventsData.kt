package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

data class EventsData(

    @SerializedName("cardEventData") val cardEventData: CardEventData
)