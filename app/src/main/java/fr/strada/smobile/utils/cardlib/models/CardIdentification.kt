package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class CardIdentification : RealmObject{
	@SerializedName("cardIssuingMemberState") var cardIssuingMemberState : Int =0
	@SerializedName("cardNumber") var cardNumber : CardNumber? = null
	@SerializedName("cardIssuingAuthorityName") var cardIssuingAuthorityName : String? = null
	@SerializedName("cardIssueDate") var cardIssueDate : String? = null
	@SerializedName("cardValidityBegin") var cardValidityBegin : String? = null
	@SerializedName("cardExpiryDate") var cardExpiryDate : String? = null
	var notificationChecked :Boolean = true

	constructor()

	constructor(
		cardIssuingMemberState: Int,
		cardNumber: CardNumber,
		cardIssuingAuthorityName: String,
		cardIssueDate: String,
		cardValidityBegin: String,
		cardExpiryDate: String
	) {
		this.cardIssuingMemberState = cardIssuingMemberState
		this.cardNumber = cardNumber
		this.cardIssuingAuthorityName = cardIssuingAuthorityName
		this.cardIssueDate = cardIssueDate
		this.cardValidityBegin = cardValidityBegin
		this.cardExpiryDate = cardExpiryDate
		this.notificationChecked = true
	}
}