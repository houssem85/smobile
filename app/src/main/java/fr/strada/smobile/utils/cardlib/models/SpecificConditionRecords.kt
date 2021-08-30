package fr.strada.smobile.utils.cardlib.models
import com.google.gson.annotations.SerializedName
data class SpecificConditionRecords (

	@SerializedName("entryTime") val entryTime : String,
	@SerializedName("specificConditionType") val specificConditionType : Int
)