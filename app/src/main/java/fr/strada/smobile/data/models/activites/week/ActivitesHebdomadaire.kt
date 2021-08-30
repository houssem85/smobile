package fr.strada.smobile.data.models.activites.week


import com.google.gson.annotations.SerializedName
import fr.strada.smobile.data.models.activites.Value

data class ActivitesHebdomadaire(
    @SerializedName("days")
    val days: List<Day>,
    @SerializedName("endOfTheWeek")
    val endOfTheWeek: String,
    @SerializedName("nuitCumul")
    val nuitCumul: Value,
    @SerializedName("serviceCumul")
    val serviceCumul: Value,
    @SerializedName("startOfTheWeek")
    val startOfTheWeek: String,
    @SerializedName("week")
    val week: Int,
    @SerializedName("year")
    val year: Int
)