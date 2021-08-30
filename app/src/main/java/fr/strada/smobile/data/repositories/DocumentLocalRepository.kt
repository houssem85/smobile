package fr.strada.smobile.data.repositories

import androidx.lifecycle.LiveData
import fr.strada.smobile.data.models.DocumentModel
import javax.inject.Inject


class DocumentLocalRepository @Inject constructor(val db: AppDatabase) {
    companion object {
        var documentList: LiveData<List<DocumentModel>>? = null
    }

    fun getDocuments(): List<DocumentModel> {
        val documentDao = db.documentDao()

        return documentDao.getAll()

    }


}