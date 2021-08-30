package fr.strada.smobile.data.models.mes_frais


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Justificatif(
    @SerializedName("fichier")
    val fichier: String
): Parcelable