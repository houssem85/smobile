package fr.strada.smobile.data.models.pointeuse

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalActivitiePointeuse(
    @PrimaryKey
    @NonNull
    var id : String = "",
    @ColumnInfo(name = "type_activite_pointeuse_id")
    val typeActivitePointeuseId : String = "",
    @ColumnInfo(name = "collaborateur_id")
    val collaborateurId : String = "",
    @ColumnInfo(name = "type_lieu_activite_id")
    val typeLieuActiviteId : String = "",
    @ColumnInfo(name = "date_activite")
    val dateActivite : String = "",
    @ColumnInfo(name = "fin_activite")
    val finActivite : String? = null,
    @ColumnInfo(name = "latitude_depart")
    val latitudeDepart : Double = 0.0 ,
    @ColumnInfo(name = "longitude_depart")
    val longitudeDepart : Double = 0.0,
    @ColumnInfo(name = "latitude_arrivee")
    val latitudeArrivee : Double = 0.0 ,
    @ColumnInfo(name = "longitude_arrivee")
    val longitudeArrivee : Double = 0.0,
    @ColumnInfo(name = "created_in_server")
    val createdInServer : Boolean = false
)