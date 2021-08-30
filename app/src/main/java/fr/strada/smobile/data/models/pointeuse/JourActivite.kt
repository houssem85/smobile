package fr.strada.smobile.data.models.pointeuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JourActivite(
    var jourActivite: String? = null,
    var activites: List<Activitie>? = null,
    var commentaires: List<Commentaire>? = null
): Parcelable