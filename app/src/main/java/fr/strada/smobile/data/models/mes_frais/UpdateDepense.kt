package fr.strada.smobile.data.models.mes_frais

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateDepense(
    val index: Int,
    val depense: Depense
) : Parcelable