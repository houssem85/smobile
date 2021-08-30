package fr.strada.smobile.data.repositories

import com.auth0.android.result.Credentials
import fr.strada.smobile.data.models.userinfo.*
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.flow.Flow

class FakeUserRepository : UserRepository {
    override suspend fun isUserLoggedIn(): Boolean {
        return true
    }

    override suspend fun isUserModeBorne(): Boolean {
        return false
    }

    override suspend fun isUserModeBorneLoggedIn(): Boolean {
        return false
    }

    override suspend fun isUserModeBorneLoggedIn(isUserModeBorneLoggedIn: Boolean): Resource<Nothing> {
        return Resource.success(null)
    }

    override suspend fun loginWithEmail(email: String, password: String): Resource<Credentials> {
        val credentials = Credentials("FakeIdToken","FakeaAccessToken","FakeType","FakerefreshToken",20000)
        return Resource.success(credentials)
    }

    override suspend fun getUserProfile(email: String, token: String): Resource<UserInfo> {
       val userInfo = UserInfo(null,null,null,null,null,null,10,null,null,null,
            listOf(),null,null,null,null,null,
           null,null,null,"firstName","ere",
           null,null,null,null,
           100,null,null,
           null,null,null,null,null,
           null,null,"userName",null,null,
           null,null)
       return Resource.success(userInfo)
    }

    override suspend fun getUserProfile(): Resource<ProfileModel> {
        val card = Card("cn","","", Unload("",""))
        val drivingLicenseCard = DrivingLicenseCard("","","")
        val model = ProfileModel("id","driverId","houssem","daoud","maidenName","houssem eddine",null,null,null,null,null,null,null,null,null,"20","","","","",card,drivingLicenseCard)
        return Resource.success(model)
    }

    override fun saveCredentials(credentials: Credentials) {

    }

    override suspend fun isModeBorne(isModeBorne: Boolean): Boolean {
         return true
    }

    override suspend fun savetenant(tenant: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTenant(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(userInfo: UserInfo): Boolean {
        return true
    }

    override suspend fun getCollaborateurId(): String {
        TODO("Not yet implemented")
    }

    override fun getUserFlow(): Flow<UserInfo?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTenant(): String {
        return "fake"
    }

    override suspend fun getUserImage(): String {
        return ""
    }

    override fun getUserNameFlow(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getUserImageFlow(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deconnecter() {
        TODO("Not yet implemented")
    }

    override suspend fun getUserFunctionalities(): Resource<List<FunctionalityRow>> {
        TODO("Not yet implemented")
    }

    override fun getTenantFlow(): Flow<String> {
        TODO("Not yet implemented")
    }
}