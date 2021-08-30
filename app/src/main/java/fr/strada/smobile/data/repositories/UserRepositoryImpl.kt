package fr.strada.smobile.data.repositories

import android.app.Application
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.result.Credentials
import fr.strada.smobile.R
import fr.strada.smobile.data.models.userinfo.FunctionalityRow
import fr.strada.smobile.data.models.userinfo.ProfileModel
import fr.strada.smobile.data.models.userinfo.UserInfo
import fr.strada.smobile.data.models.userinfo.hasTonnents
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.data_stores.UserPreferences
import fr.strada.smobile.utils.ext.authWithEmail
import fr.strada.smobile.utils.ext.getToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(@Named("api_uam") private val apiUAM : Api,
                                         @Named("api_gateway") val api : Api,
                                         private val apiAuth0 : AuthenticationAPIClient,
                                         private val userPreferences : UserPreferences,
                                         private val credentialsManager: CredentialsManager,
                                         private val internetConnectionChecker: InternetConnectionChecker,
                                         private val context : Application) : UserRepository {
    override suspend fun isUserLoggedIn() = try {
        credentialsManager.getToken()
       true
    }catch (ex:Exception){
       false
    }

    override suspend fun isUserModeBorne() = try {
        userPreferences.isModeBorne.first()
    }catch (ex:Exception){
        false
    }

    override suspend fun isUserModeBorneLoggedIn() = try {
        userPreferences.isUserModeBorneLoggedIn.first()
    }catch (ex:Exception){
        false
    }

    override suspend fun isUserModeBorneLoggedIn(isUserModeBorneLoggedIn:Boolean) = try {
        userPreferences.isUserModeBorneLoggedIn(isUserModeBorneLoggedIn)
        Resource.success(null)
    }catch (ex:Exception){
        Resource.error(ex.message.toString(),null)
    }

    override suspend fun loginWithEmail(email:String, password:String):Resource<Credentials>  = try {
      if(internetConnectionChecker.check()){
          val credentials = apiAuth0.authWithEmail(email,password)
          Resource.success(credentials)
      }else {
          Resource.error(context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),null)
      }
   }catch (ex: AuthenticationException){
       if(ex.isInvalidCredentials){
           Resource.error(context.getString(R.string.email_ou_mot_de_passe_incorrect),null)
       }else {
           Resource.error(ex.message.toString(),null)
       }
   }

    override suspend fun getUserProfile(email:String, token:String) : Resource<UserInfo> {
        return try {
            if(internetConnectionChecker.check()){
                val res =  apiUAM.getUserInfo("Bearer $token",email)
                if(res.code() == 200){
                    val userInfo = res.body()!!
                    if(userInfo.hasTonnents()){
                        Resource.success(userInfo)
                    }else{
                        Resource.error("l'utilisateur n'a pas des tennants id",null)
                    }
                }else {
                    Resource.error(res.code().toString(),null)
                }
            }else{
                Resource.error(context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),null)
            }
        }catch (ex:Exception){
            Resource.error(ex.message.toString(),null)
        }
    }

    override fun saveCredentials(credentials:Credentials)
    {
        credentialsManager.saveCredentials(credentials)
    }

    override suspend fun isModeBorne(isModeBorne:Boolean) = try {
        userPreferences.isModeBorne(isModeBorne)
        true
    }catch (ex:Exception){
        false
    }

    override suspend fun savetenant(tenant: String) = try {
        userPreferences.saveTenant(tenant)
        true
    }catch (ex:Exception){
        false
    }

    override suspend fun deleteTenant() = try {
        userPreferences.deleteTenant()
        true
    }catch (ex:Exception){
        false
    }


    override suspend fun saveUser(userInfo: UserInfo) = try {
        userPreferences.saveUser(userInfo)
        true
    }catch (ex:Exception){
        false
    }

    override suspend fun getCollaborateurId() = try {
        userPreferences.collaborateurId.first()
    }catch (ex:Exception){
        ""
    }

    override fun getUserFlow() = userPreferences.user

    override suspend fun getTenant() = try {
        userPreferences.tenant.first()
    }catch (ex:Exception){
        ""
    }

    override suspend fun getUserImage() = try {
        userPreferences.image.first()
    }catch (ex:Exception){
        ""
    }

    override fun getUserNameFlow() = userPreferences.userName
    override fun getUserImageFlow() = userPreferences.image

    override suspend fun getUserProfile() : Resource<ProfileModel>  = try {
       if(internetConnectionChecker.check())
       {
           val collaborateurId = userPreferences.collaborateurId.first()
           val token = credentialsManager.getToken()
           val res = api.getUserProfile("Bearer $token",collaborateurId)
           if(res.code() == 200){
               Resource.success(res.body())
           }else{
               Resource.error(res.code().toString(),null)
           }
       }else
       {
           Resource.error(context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),null)
       }
   }catch (ex:Exception){
       Resource.error(ex.message.toString(),null)
   }

    override suspend fun deconnecter()
    {
       credentialsManager.clearCredentials()
       userPreferences.deleteTenant()
    }

    /**
     * get user functionalities from api uam if connexion internet available
     * if not get UserDataStore
     * @return list of user functionalities
     */
    override suspend fun getUserFunctionalities() : Resource<List<FunctionalityRow>> = try {
        if(internetConnectionChecker.check()){
            val email = userPreferences.userEmail.first()
            val token = credentialsManager.getToken()
            val res = apiUAM.getUserInfo("Bearer $token",email)
            if(res.code() == 200) {
                val userInfo = res.body()
                val listFunctionalitiesByRolesAndUser = userInfo!!.listFunctionalitiesByRolesAndUser!!
                val functionalityRows = ArrayList<FunctionalityRow>()
                listFunctionalitiesByRolesAndUser.forEach {
                    it.functionalityRows?.let {
                        functionalityRows.addAll(it)
                    }
                }
                Resource.success(functionalityRows)
            }else{
                Resource.error(res.code().toString(),null)
            }
        }else{
            val localUser = userPreferences.user.first()
            val listFunctionalitiesByRolesAndUser = localUser!!.listFunctionalitiesByRolesAndUser!!
            val functionalityRows = ArrayList<FunctionalityRow>()
            listFunctionalitiesByRolesAndUser.forEach {
                it.functionalityRows?.let {
                    functionalityRows.addAll(it)
                }
            }
            Resource.success(functionalityRows)
        }
    }catch (ex:Exception){
        Resource.error(ex.message.toString(),null)
    }

    override fun getTenantFlow() = userPreferences.tenant
}