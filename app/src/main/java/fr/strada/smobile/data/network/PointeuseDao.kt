package fr.strada.smobile.data.network

import androidx.room.*
import fr.strada.smobile.data.models.pointeuse.ActivitiePointeuse
import fr.strada.smobile.data.models.pointeuse.LocalActivitiePointeuse
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel

@Dao
interface PointeuseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListTypeActivitePointeuse(list: List<TypeActivitePointeuseModel>)

    @Query("DELETE FROM TypeActivitePointeuseModel")
    suspend fun deleteAllListTypeActivitePointeuse()

    @Query("SELECT * FROM TypeActivitePointeuseModel")
    suspend fun getListTypeActivitePointeuse(): List<TypeActivitePointeuseModel>

    @Query("SELECT * FROM TypeActivitePointeuseModel WHERE id=:id")
    suspend fun getTypeActivitePointeuseById(id:String) : TypeActivitePointeuseModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDerniereActivitePointeuse(data: ActivitiePointeuse)

    @Query("SELECT * FROM DerniereActivitePointeuse")
    suspend fun getDerniereActivitePointeuse(): List<ActivitiePointeuse>

    @Query("UPDATE DerniereActivitePointeuse set fin_activite=:finActivite WHERE activitie_pointeuse_id=:id")
    suspend fun updateDerniereActivitePointeuse(id:String,finActivite : String)

    @Query("DELETE FROM DerniereActivitePointeuse")
    suspend fun deleteDerniereActivitePointeuse()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalActivitePointeuse( model: LocalActivitiePointeuse )

    @Query("SELECT * FROM LocalActivitiePointeuse")
    suspend fun getListLocalActivitiePointeuse(): List<LocalActivitiePointeuse>

    @Delete
    suspend fun deleteLocalActivitiePointeuse(model:LocalActivitiePointeuse)

    @Query("SELECT EXISTS(SELECT * FROM LocalActivitiePointeuse WHERE id=:id)")
    suspend fun isLocalActivitiePointeuseIsExist(id : String) : Boolean

    @Query("UPDATE LocalActivitiePointeuse set fin_activite=:finActivite ,latitude_arrivee=:latitudeArrivee ,longitude_arrivee=:longitudeArrivee WHERE id=:id")
    suspend fun updateLocalActivitiePointeuse(id:String,finActivite : String,latitudeArrivee : Double ,longitudeArrivee : Double)

}