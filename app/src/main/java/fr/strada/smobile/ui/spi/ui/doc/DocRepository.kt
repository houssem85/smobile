package fr.strada.smobile.ui.spi.ui.doc

import com.auth0.android.authentication.storage.CredentialsManager
import fr.strada.smobile.data.models.DocumentModel
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.data.repositories.DocumentLocalRepository
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.MessageFoctory
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.data_stores.UserPreferences
import fr.strada.smobile.utils.ext.getToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

class DocRepository @Inject constructor(

    val docrepo: DocumentLocalRepository,
    @Named("api_gateway") val api: Api,
    private val internetConnectionChecker: InternetConnectionChecker,
    private val credentialsManager: CredentialsManager,
    private val userPreferences: UserPreferences,
    private val messageFoctory : MessageFoctory
    ) {


    suspend fun getDocumentsLocal(): List<DocumentModel>
    {


        var localList: List<DocumentModel> = ArrayList()



        CoroutineScope(Dispatchers.IO).launch {
            try {
                val get = async {
                    localList = docrepo.getDocuments()
                }

                get.await()
            } catch (exception: Exception) {
            }
        }.join()



        return localList


    }
    suspend fun uploadDocument ( FileType: MultipartBody.Part) = try {
        if(internetConnectionChecker.check())
        {
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            val res = api.uploadDocument("Bearer $token",collaborateurId,0,FileType)
            if (res.code() == 200) {
                Resource.success(res.body())
            } else {
                Resource.error(res.code().toString(), null)
            }
        } else {
            Resource.error(messageFoctory.getMessageNoInternetConnection(), null)
        }
    } catch (ex: Exception) {
        Resource.error(ex.message.toString(), null)
    }




}