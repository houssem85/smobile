package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class DriverCardApplicationIdentification {

	@SerializedName("typeOfTachographCardId")  var typeOfTachographCardId : String = ""
	@SerializedName("cardStructureVersion") lateinit var cardStructureVersion : CardStructureVersion
	@SerializedName("noOfEventsPerType")  var noOfEventsPerType : Byte = 0
	@SerializedName("noOfFaultsPerType")  var noOfFaultsPerType : Byte = 0
	@SerializedName("activityStructureLength")  var activityStructureLength : Int = 0
	@SerializedName("noOfCardVehicleRecords")  var noOfCardVehicleRecords : Int = 0
	@SerializedName("noOfCardPlaceRecords")  var noOfCardPlaceRecords : Byte = 0

	constructor()
	constructor(
        typeOfTachographCardId: String,
        cardStructureVersion: CardStructureVersion,
        noOfEventsPerType: Byte,
        noOfFaultsPerType: Byte,
        activityStructureLength: Int,
        noOfCardVehicleRecords: Int,
        noOfCardPlaceRecords: Byte
	) {
		this.typeOfTachographCardId = typeOfTachographCardId
		this.cardStructureVersion = cardStructureVersion
		this.noOfEventsPerType = noOfEventsPerType
		this.noOfFaultsPerType = noOfFaultsPerType
		this.activityStructureLength = activityStructureLength
		this.noOfCardVehicleRecords = noOfCardVehicleRecords
		this.noOfCardPlaceRecords = noOfCardPlaceRecords
	}

}