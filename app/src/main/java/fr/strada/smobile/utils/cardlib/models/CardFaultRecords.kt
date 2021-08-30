package fr.strada.smobile.utils.cardlib.models
import com.google.gson.annotations.SerializedName
data class CardFaultRecords (

	@SerializedName("faultType") val faultType : Int,
	@SerializedName("faultBeginTime") val faultBeginTime : String,
	@SerializedName("faultEndTime") val faultEndTime : String,
	@SerializedName("faultVehicleRegistration") val faultVehicleRegistration : FaultVehicleRegistration
)