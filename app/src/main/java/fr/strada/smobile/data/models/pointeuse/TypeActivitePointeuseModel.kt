package fr.strada.smobile.data.models.pointeuse

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class TypeActivitePointeuseModel(

    @PrimaryKey
    @NonNull
    var id: String="",
    @ColumnInfo(name = "code")
    var code: String?="",
    @ColumnInfo(name = "libelle")
    var libelle: String?="",
    @ColumnInfo(name = "icone")
    var icone:String? = "",
    @ColumnInfo(name = "code_couleur")
    var codeCouleur:String ?= "",
    @ColumnInfo(name = "groupe_activitie_id")
    var groupeActiviteId: String?=""
): Parcelable

