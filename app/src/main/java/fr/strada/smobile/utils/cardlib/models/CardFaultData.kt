package fr.strada.smobile.utils.cardlib.models
import com.google.gson.annotations.SerializedName
data class CardFaultData (

	@SerializedName("cardFaultRecords") val cardFaultRecords : List<List<CardFaultRecords>>
)