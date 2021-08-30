package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

data class ControlCardNumber(

    @SerializedName("cardType") val cardType: Int,
    @SerializedName("cardIssuingMemberState") val cardIssuingMemberState: Int,
    @SerializedName("cardNumber") val cardNumber: CardNumber
)