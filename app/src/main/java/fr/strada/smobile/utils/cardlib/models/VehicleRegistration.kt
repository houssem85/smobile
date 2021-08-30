package fr.strada.smobile.utils.cardlib.models
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class VehicleRegistration : RealmObject{
	@SerializedName("vehicleRegistrationNation") var vehicleRegistrationNation : Int = 0
	@SerializedName("vehicleRegistrationNumber") var vehicleRegistrationNumber : String = ""
	constructor()
	constructor(vehicleRegistrationNation: Int, vehicleRegistrationNumber: String) {
		this.vehicleRegistrationNation = vehicleRegistrationNation
		this.vehicleRegistrationNumber = vehicleRegistrationNumber
	}
}