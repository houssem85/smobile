package fr.strada.smobile.data.repositories

import com.auth0.android.authentication.storage.CredentialsManager
import com.google.gson.JsonObject
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.MessageFoctory
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.ext.getToken
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class CommentaireRepository @Inject constructor(@Named("api_gateway")val api : Api,
                                                private val credentialsManager : CredentialsManager,
                                                private val internetConnectionChecker : InternetConnectionChecker,
                                                private val messageFoctory : MessageFoctory) {

    suspend fun ajoutCommentaire(commentaire : String,jourActivite:String) = try {
        if(internetConnectionChecker.check()){
            val jsonCommentaire = JsonObject()
            jsonCommentaire.addProperty("commentaire",commentaire)
            val token = credentialsManager.getToken()
            val res = api.createCommentairesByJourActivitePointeuse("Bearer $token",jourActivite,jsonCommentaire)
            if(res.code() == 200){
                Resource.success(res.body())
            }else{
                Resource.error(messageFoctory.getMessageNoInternetConnection(),null)
            }
        }else{
            Resource.error(messageFoctory.getMessageNoInternetConnection(),null)
        }
    }catch (ex:Exception){
        Resource.error(ex.message.toString(),null)
    }

    suspend fun getListCommentairesParJour(jourActivite:String) = try {
        if(internetConnectionChecker.check()){
            val token = credentialsManager.getToken()
            val res = api.getListCommentairesByJourActivite("Bearer $token",jourActivite)
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
}