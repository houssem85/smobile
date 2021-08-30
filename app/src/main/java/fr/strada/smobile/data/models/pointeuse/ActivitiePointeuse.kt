package fr.strada.smobile.data.models.pointeuse

import androidx.annotation.NonNull
import androidx.room.*
import fr.strada.smobile.data.models.activites.Value

@Entity(tableName = "DerniereActivitePointeuse")
data class ActivitiePointeuse (
    @PrimaryKey
    @ColumnInfo(name = "activitie_pointeuse_id")
    @NonNull
    var id : String = "",
    @ColumnInfo(name = "date_activite")
    var dateActivite : String? = "",
    @ColumnInfo(name = "fin_activite")
    var finActivite : String? = "",
    @Ignore
    var heureDebutActivite : Value? = null,
    @Ignore
    var heureFinActivite : Value?= null,
    @Ignore
    var duree : Value? = null,
    @Embedded
    var typeActivite : TypeActivitePointeuseModel? = null
)