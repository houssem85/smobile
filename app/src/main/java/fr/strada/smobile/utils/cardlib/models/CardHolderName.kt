package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName
data class CardHolderName (

	@SerializedName("holderSurname") val holderSurname : String,
	@SerializedName("holderFirstName") val holderFirstName : String
)