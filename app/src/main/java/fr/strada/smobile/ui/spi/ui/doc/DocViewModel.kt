package fr.strada.smobile.ui.spi.ui.doc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.data.models.DocumentModel
import fr.strada.smobile.data.repositories.AppDatabase
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DocViewModel @Inject constructor(
    application: Application,
    val db: AppDatabase,
    val documentrepo: DocRepository
) : AndroidViewModel(application) {
    val dateDepense = MutableLiveData("")
    val dayDepense = MutableLiveData<Day>()

    private val _documents = MutableLiveData<Resource<List<DocumentModel>>>()

    // public
    val documents: LiveData<Resource<List<DocumentModel>>> = _documents


    init {
        val sfd = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        dayDepense.value = Day(year, month, day)
        dateDepense.value = sfd.format(calendar.time)
    }

    fun savePdfInLocal(name: String, description: String) {

        val documentdao = db.documentDao()
        documentdao.insert(
            DocumentModel(
                null,
                name,
                description,
                25,
                25,
                false,
            )
        )


    }

    fun getPdf() {
        viewModelScope.launch {
            _documents.value = Resource.success(
                data = documentrepo.getDocumentsLocal()
            )

        }

    }
  suspend  fun uploadFile(FileType: MultipartBody.Part){
        documentrepo.uploadDocument(FileType)

    }


}