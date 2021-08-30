package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject

open class CardVehiclesUsed : RealmObject() {

    @SerializedName("vehiclePointerNewestRecord")
    var vehiclePointerNewestRecord: Int = 0
    @SerializedName("cardVehicleRecords")
    var cardVehicleRecords: RealmList<CardVehicleRecords> = RealmList()

}

