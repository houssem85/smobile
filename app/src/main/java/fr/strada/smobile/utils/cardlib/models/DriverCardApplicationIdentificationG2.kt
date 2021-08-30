package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class DriverCardApplicationIdentificationG2() {
	@SerializedName("typeOfTachographCardId") var typeOfTachographCardId : String = ""
	@SerializedName("cardStructureVersion") var cardStructureVersion : CardStructureVersion? = null
	@SerializedName("noOfEventsPerType") var noOfEventsPerType : Int = 0
	@SerializedName("noOfFaultsPerType") var noOfFaultsPerType : Int = 0
	@SerializedName("activityStructureLength") var activityStructureLength : Int = 0
	@SerializedName("noOfCardVehicleRecords") var noOfCardVehicleRecords : Int = 0
	@SerializedName("noOfCardPlaceRecords") var noOfCardPlaceRecords : Int = 0
	@SerializedName("noOfGNSSCDRecords") var noOfGNSSCDRecords : Int = 0
	@SerializedName("noOfSpecificConditionRecords") var noOfSpecificConditionRecords : Int = 0
}