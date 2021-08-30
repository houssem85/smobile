package fr.strada.smobile.utils.cardlib.models
import com.google.gson.annotations.SerializedName
data class PlaceRecords (

	@SerializedName("entryTime") val entryTime : String,
	@SerializedName("entryTypeDailyWorkPeriod") val entryTypeDailyWorkPeriod : Int,
	@SerializedName("dailyWorkPeriodCountry") val dailyWorkPeriodCountry : Int,
	@SerializedName("dailyWorkPeriodRegion") val dailyWorkPeriodRegion : Int,
	@SerializedName("vehicleOdometerValue") val vehicleOdometerValue : Int
)