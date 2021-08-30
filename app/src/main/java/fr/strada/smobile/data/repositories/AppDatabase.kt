package fr.strada.smobile.data.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.strada.smobile.data.models.DocumentModel
import fr.strada.smobile.data.models.pointeuse.ActivitiePointeuse
import fr.strada.smobile.data.models.pointeuse.LocalActivitiePointeuse
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.data.network.DocumentDao
import fr.strada.smobile.data.network.PointeuseDao

@Database(
    entities = [TypeActivitePointeuseModel::class, ActivitiePointeuse::class, LocalActivitiePointeuse::class, DocumentModel::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pointeuseDao(): PointeuseDao
    abstract fun documentDao(): DocumentDao
}