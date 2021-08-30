package fr.strada.smobile.data.models.activites.week


import com.google.gson.annotations.SerializedName
import fr.strada.smobile.data.models.activites.Value

data class Day(
    @SerializedName("day")
    val day: String,
    @SerializedName("heureDebut")
    val heureDebut: Value,
    @SerializedName("heureFin")
    val heureFin: Value,
    @SerializedName("heureNuit")
    val heureNuit: Value,
    @SerializedName("tempsService")
    val tempsService: Value
)