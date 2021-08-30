package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class CardNumber : RealmObject{
	@SerializedName("driverIdentification") var driverIdentification : String = ""
	@SerializedName("cardReplacementIndex") var cardReplacementIndex : String = ""
	@SerializedName("cardRenewalIndex") var cardRenewalIndex : String = ""
	constructor()
	constructor(
		driverIdentification: String,
		cardReplacementIndex: String,
		cardRenewalIndex: String
	) : super() {
		this.driverIdentification = driverIdentification
		this.cardReplacementIndex = cardReplacementIndex
		this.cardRenewalIndex = cardRenewalIndex
	}


}