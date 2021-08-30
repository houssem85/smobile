package fr.strada.smobile.utils.cardlib.models
import com.google.gson.annotations.SerializedName
data class TachographG2Df (

	@SerializedName("applicationIdentification") val applicationIdentification : ApplicationIdentification,
	@SerializedName("identification") val identification : Identification,
	@SerializedName("vehiclesUsed") val vehiclesUsed : VehiclesUsed,
	@SerializedName("places") val places : Places,
	@SerializedName("gnssPlaces") val gnssPlaces : GnssPlaces
)