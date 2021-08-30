package fr.strada.smobile.data.models.infractions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Infraction(
    val infractionId : String,
    val infractionCategorieId: String?,
    val infractionCategorieCode: String?,
    val infractionCategorieLibelle: String?,
    val infractionLibelle: String ,
    val infractionDate: String ,
    val infractionHeure: Value ,
    val infractionClasse :Int
) : Parcelable

@Parcelize
data class Value(
    val totalMilliseconds : Long
) : Parcelable
