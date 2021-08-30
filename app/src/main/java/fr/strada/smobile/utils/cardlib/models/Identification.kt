package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class Identification(
    @SerializedName("cardIdentification") var cardIdentification: CardIdentification,
    @SerializedName("driverCardHolderIdentification") var driverCardHolderIdentification: DriverCardHolderIdentification
)