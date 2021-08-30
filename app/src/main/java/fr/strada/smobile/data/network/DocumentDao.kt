package fr.strada.smobile.data.network

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.strada.smobile.data.models.DocumentModel


@Dao
interface DocumentDao {
    @Query("SELECT * FROM documentmodel")
    fun getAll(): List<DocumentModel>

    @Insert
    fun insert(document: DocumentModel)
}
