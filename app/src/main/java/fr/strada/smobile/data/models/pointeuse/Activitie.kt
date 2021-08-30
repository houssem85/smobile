package fr.strada.smobile.data.models.pointeuse

import android.os.Parcelable
import fr.strada.smobile.data.models.activites.Value
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Activitie(
    var typeActivitePointeuse: TypeActivitePointeuseModel? = null,
    var heureDebut: Value? = null,
    var heureFin: Value? = null,
    var dureeActivite: Value? = null,
    var finActivite: String? = null,
    var dateActivite: String? = null
) : Parcelable