package fr.strada.smobile.ui.spi.ui.tms.model

import com.auth0.android.authentication.storage.CredentialsManager
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.MessageFoctory
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.data_stores.UserPreferences
import fr.strada.smobile.utils.ext.getToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class TmsRepository@Inject constructor(
    @Named("api_gateway_web") val api: Api,
    private val credentialsManager: CredentialsManager,
    private val userPreferences: UserPreferences,
    private val internetConnectionChecker: InternetConnectionChecker,
    private val messageFoctory : MessageFoctory) {

    suspend fun getListTournee() = try {
        if(internetConnectionChecker.check()){
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            val res = api.getListTournee("Bearer $token","9f908e8f-8031-480d-9668-7a6fa3bfc23c" , 1, 10)
            if(res.code() == 200){
                Resource.success(res.body()!!.items)
            }else{
                Resource.error(res.code().toString(),null)
            }
        }else{
            Resource.error(messageFoctory.getMessageNoInternetConnection(),null)
        }
    }catch (ex:Exception){
        Resource.error(ex.message.toString(),null)
    }

    suspend fun getdetailTournee(idTournee : String ) = try {
        if(internetConnectionChecker.check()){
            val token = credentialsManager.getToken()
            val res = api.getDetailCommande("Bearer $token",idTournee)
            if(res.code() == 200){
                Resource.success(res.body())
            }else{
                Resource.error(res.code().toString(),null)
            }
        }else{
            Resource.error(messageFoctory.getMessageNoInternetConnection(),null)
        }
    }catch (ex:Exception){
        Resource.error(ex.message.toString(),null)
    }

}