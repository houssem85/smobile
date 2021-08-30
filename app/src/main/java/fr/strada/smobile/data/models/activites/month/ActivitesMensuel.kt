package fr.strada.smobile.data.models.activites.month


import com.google.gson.annotations.SerializedName
import fr.strada.smobile.data.models.activites.Value

data class ActivitesMensuel(
    @SerializedName("endOfTheMonth")
    val endOfTheMonth: String,
    @SerializedName("month")
    val month: Int,
    @SerializedName("nuitCumul")
    val nuitCumul: Value,
    @SerializedName("serviceCumul")
    val serviceCumul: Value,
    @SerializedName("startOfTheMonth")
    val startOfTheMonth: String,
    @SerializedName("weeks")
    val weeks: List<Week>,
    @SerializedName("year")
    val year: Int
)