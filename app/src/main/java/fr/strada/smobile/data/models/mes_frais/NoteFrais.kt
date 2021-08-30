package fr.strada.smobile.data.models.mes_frais


import android.os.Parcelable
import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteFrais(
    @SerializedName("approuvee")
    val approuvee: Boolean,
    @SerializedName("collaborateurId")
    var collaborateurId: String,
    @SerializedName("collaborateurNom")
    val collaborateurNom: String,
    //@Bindable
    @SerializedName("commentaire")
    var commentaire: String = "",
    @SerializedName("dateCreationNote")
    val dateCreationNote: String,
    @SerializedName("dateDernierDepense")
    val dateDernierDepense: String,
    @SerializedName("datePremierDepense")
    val datePremierDepense: String,
    @SerializedName("listDepenses")
    val listDepenses: ArrayList<Depense>,
    @SerializedName("nombreDepense")
    val nombreDepense: Int,
    @SerializedName("noteFraisId")
    val noteFraisId: String,
    @SerializedName("titre")
    val titre: String
): Parcelable , BaseObservable()