package fr.strada.smobile.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DocumentModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val type: String,
    val dateAlert: Long?,
    val dateCreation: Long? ,
    val withNotification : Boolean ,
    )