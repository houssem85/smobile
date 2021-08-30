package fr.strada.smobile.utils.cardlib.models
import com.google.gson.annotations.SerializedName
data class TachographDf (

	@SerializedName("applicationIdentification") val applicationIdentification : ApplicationIdentification,
	@SerializedName("identification") val identification : Identification,
	@SerializedName("drivingLicenceInfo") val drivingLicenceInfo : DrivingLicenceInfo,
	@SerializedName("eventsData") val eventsData : EventsData,
	@SerializedName("faultsData") val faultsData : FaultsData,
	@SerializedName("driverActivityData") val driverActivityData : DriverActivityData,
	@SerializedName("vehiclesUsed") val vehiclesUsed : VehiclesUsed,
	@SerializedName("places") val places : Places,
	@SerializedName("currentUsage") val currentUsage : CurrentUsage,
	@SerializedName("controlActivityData") val controlActivityData : ControlActivityData,
	@SerializedName("specificConditions") val specificConditions : SpecificConditions
)