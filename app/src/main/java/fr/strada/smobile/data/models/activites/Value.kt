package fr.strada.smobile.data.models.activites


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Value(
    @SerializedName("totalMilliseconds")
    var totalMilliseconds: Long,
    @SerializedName("totalHours")
    var totalHours: Float,
    @SerializedName("hours")
    var hours: Float,
    @SerializedName("minutes")
    var minutes: Float,
): Parcelable