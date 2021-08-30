package fr.strada.smobile.data.repositories

import com.auth0.android.result.Credentials
import fr.strada.smobile.data.models.userinfo.FunctionalityRow
import fr.strada.smobile.data.models.userinfo.ProfileModel
import fr.strada.smobile.data.models.userinfo.UserInfo
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun isUserLoggedIn(): Boolean

    suspend fun isUserModeBorne(): Boolean

    suspend fun isUserModeBorneLoggedIn(): Boolean

    suspend fun isUserModeBorneLoggedIn(isUserModeBorneLoggedIn:Boolean): Resource<Nothing>

    suspend fun loginWithEmail(email:String,password:String): Resource<Credentials>

    suspend fun getUserProfile(email:String,token:String) : Resource<UserInfo>

    fun saveCredentials(credentials: Credentials)

    suspend fun isModeBorne(isModeBorne:Boolean): Boolean

    suspend fun savetenant(tenant: String): Boolean

    suspend fun deleteTenant(): Boolean

    suspend fun saveUser(userInfo: UserInfo): Boolean

    suspend fun getCollaborateurId(): String

    fun getUserFlow(): Flow<UserInfo?>

    suspend fun getTenant(): String

    suspend fun getUserImage(): String

    fun getUserNameFlow(): Flow<String>

    fun getUserImageFlow(): Flow<String>

    suspend fun getUserProfile() : Resource<ProfileModel>

    suspend fun deconnecter()

    /**
     * get user functionalities from api uam if connexion internet available
     * if not get UserDataStore
     * @return list of user functionalities
     */
    suspend fun getUserFunctionalities() : Resource<List<FunctionalityRow>>

    fun getTenantFlow(): Flow<String>
}