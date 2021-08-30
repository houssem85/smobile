package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class CardVehicleRecords : RealmObject{

	@SerializedName("vehicleOdometerBegin") var vehicleOdometerBegin : Int = 0
	@SerializedName("vehicleOdometerEnd") var vehicleOdometerEnd : Int = 0
	@SerializedName("vehicleFirstUse") var vehicleFirstUse : String = ""
	@SerializedName("vehicleLastUse") var vehicleLastUse : String = ""
	@SerializedName("vehicleRegistration") var vehicleRegistration : VehicleRegistration = VehicleRegistration()
	@SerializedName("vuDataBlockCounter") var vuDataBlockCounter : String = ""
	constructor()
	constructor(
		vehicleOdometerBegin: Int,
		vehicleOdometerEnd: Int,
		vehicleFirstUse: String,
		vehicleLastUse: String,
		vehicleRegistration: VehicleRegistration,
		vuDataBlockCounter: String
	) : super() {
		this.vehicleOdometerBegin = vehicleOdometerBegin
		this.vehicleOdometerEnd = vehicleOdometerEnd
		this.vehicleFirstUse = vehicleFirstUse
		this.vehicleLastUse = vehicleLastUse
		this.vehicleRegistration = vehicleRegistration
		this.vuDataBlockCounter = vuDataBlockCounter
	}

}