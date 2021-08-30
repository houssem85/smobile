package fr.strada.smobile.data.models.mes_frais


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Depense(
    @SerializedName("commentaire")
    val commentaire: String?,
    @SerializedName("dateDepense")
    val dateDepense: String,
    @SerializedName("depenseId")
    var depenseId: String,
    @SerializedName("listJustificatifs")
    val listJustificatifs: List<Justificatif>,
    @SerializedName("montant")
    val montant: Float,
    @SerializedName("typeDepenseIcone")
    val typeDepenseIcone: String?,
    @SerializedName("typeDepenseId")
    val typeDepenseId: String,
    @SerializedName("typeDepenseLibelle")
    val typeDepenseLibelle: String
): Parcelable