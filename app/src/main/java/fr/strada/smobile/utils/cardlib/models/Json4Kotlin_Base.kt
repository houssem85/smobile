package fr.strada.smobile.utils.cardlib.models
import com.google.gson.annotations.SerializedName
data class Json4Kotlin_Base (

	@SerializedName("icc") val icc : Icc,
	@SerializedName("ic") val ic : Ic,
	@SerializedName("tachographDf") val tachographDf : TachographDf,
	@SerializedName("tachographG2Df") val tachographG2Df : TachographG2Df
)