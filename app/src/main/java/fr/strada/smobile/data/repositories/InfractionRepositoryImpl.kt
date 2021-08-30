package fr.strada.smobile.data.repositories

import com.auth0.android.authentication.storage.CredentialsManager
import com.google.gson.JsonObject
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.MessageFoctory
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.data_stores.UserPreferences
import fr.strada.smobile.utils.ext.getToken
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class InfractionRepositoryImpl @Inject constructor(
    @Named("api_gateway") private val api : Api,
    private val internetConnectionChecker: InternetConnectionChecker,
    private val credentialsManager : CredentialsManager,
    private val userPreferences : UserPreferences,
    private val messageFoctory : MessageFoctory) : InfractionRepository
{

    override suspend fun getInfractionsCategories() = try {
       if(internetConnectionChecker.check()){
           val token = credentialsManager.getToken()
           val res = api.getInfractionsCategories("Bearer $token")
           if(res.code() == 200){
               Resource.success(res.body())
           }else {
               Resource.error(res.code().toString(),null)
           }
       }else{
           Resource.error(messageFoctory.getMessageNoInternetConnection(),null)
       }
   }catch (ex : Exception){
       Resource.error(ex.message.toString(),null)
   }

   override suspend fun getInfractions(dateDebut : String?, dateFin : String?, infractionClass: Int?) = coroutineScope {
       try {
           if(internetConnectionChecker.check()){
               val token = async { credentialsManager.getToken() }
               val collaborateurId = async { userPreferences.collaborateurId.first() }
               val res = api.getInfractionsByFiltre("Bearer ${token.await()}",collaborateurId.await(),dateDebut,dateFin,infractionClass)
               if(res.code() == 200){
                   Resource.success(res.body())
               }else {
                   Resource.error(res.code().toString(),null)
               }
           }else{
               Resource.error(messageFoctory.getMessageNoInternetConnection(),null)
           }
       }catch (ex:Exception){
           Resource.error(ex.message.toString(),null)
       }
   }


   override suspend fun getDetailsInfraction(infractionId:String) = try {
       if(internetConnectionChecker.check()){
           val token = credentialsManager.getToken()
           val res = api.getDetailsInfraction("Bearer $token",infractionId)
           if(res.code() == 200){
               Resource.success(res.body())
           }else{
               Resource.error(res.code().toString(),null)
           }
       }else{
           Resource.error(messageFoctory.getMessageNoInternetConnection(),null)
       }
   }catch(ex:Exception){
       Resource.error(ex.message.toString(),null)
   }

   override suspend fun updateInfraction(infractionId:String, commentaire : String) = try {
       if(internetConnectionChecker.check()){
           val token = credentialsManager.getToken()
           val raw  = JsonObject()
           raw.addProperty("commentaire",commentaire)
           val res = api.updateInfraction("Bearer $token",infractionId,raw)
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