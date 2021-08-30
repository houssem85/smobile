package fr.strada.smobile.data.models.activites.month


import com.google.gson.annotations.SerializedName
import fr.strada.smobile.data.models.activites.Value

data class Week(
    @SerializedName("daysCount")
    val daysCount: Int,
    @SerializedName("heureNuit")
    val heureNuit: Value,
    @SerializedName("tempsService")
    val tempsService: Value,
    @SerializedName("weekNumber")
    var weekNumber: String
)