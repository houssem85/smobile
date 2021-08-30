package fr.strada.smobile.data.repositories

import com.auth0.android.authentication.storage.CredentialsManager
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.MessageFoctory
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.data_stores.UserPreferences
import fr.strada.smobile.utils.ext.getToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class MesFraisRepository @Inject constructor(
    @Named("api_gateway") val api: Api,
    private val credentialsManager: CredentialsManager,
    private val userPreferences: UserPreferences,
    private val internetConnectionChecker: InternetConnectionChecker,
    private val messageFoctory : MessageFoctory
) {

    suspend fun getListNoteFrais(dateDebut : String? = null,dateFin : String? = null,enCour : Boolean? = null) = try {
        if(internetConnectionChecker.check()){
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            val res = api.getListNoteFrais("Bearer $token",collaborateurId,dateDebut,dateFin,enCour)
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

    suspend fun getListTypesDepense() = try {
       if(internetConnectionChecker.check()){
           val token = credentialsManager.getToken()
           val res = api.getListTypesDepense("Bearer $token")
           if(res.code() == 200)
           {
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

    suspend fun createNoteFrais( noteFrais : NoteFrais) = try {
        if(internetConnectionChecker.check())
        {
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            noteFrais.collaborateurId = collaborateurId
            val res = api.createNoteFrais("Bearer $token", noteFrais)
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

    suspend fun updateNoteFrais(noteFrais: NoteFrais) = try {
        if (internetConnectionChecker.check()) {
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            noteFrais.collaborateurId = collaborateurId
            val res = api.updateNoteFrais("Bearer $token", noteFrais.noteFraisId, noteFrais)
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

    suspend fun deleteNoteFrais(noteFraisId: String) = try {
        if (internetConnectionChecker.check()) {
            val token = credentialsManager.getToken()
            val res = api.deleteNoteFrais("Bearer $token", noteFraisId)
            if(res.code() == 200){
                Resource.success(res.body())
            }else {
                Resource.error(res.code().toString(),null)
            }
        }else {
            Resource.error(messageFoctory.getMessageNoInternetConnection(),null)
        }
    }catch (ex:Exception){
        Resource.error(ex.message.toString(),null)
    }
}