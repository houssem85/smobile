package fr.strada.smobile.utils.data_stores

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.userinfo.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(val context: Application) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

    suspend fun saveUser(user: UserInfo) {
        val gson = Gson()
        val jsonUser = gson.toJson(user)
        context.dataStore.edit { preferences ->
            preferences[KEY_USER] = jsonUser
            preferences[KEY_COLLABORATEUR_ID] = user.id
            user.email?.let {
                preferences[KEY_USER_EMAIL] = it
            }
            user.userPhoto?.let {
                preferences[KEY_USER_IMAGE] = it.image
            }
            preferences[KEY_USER_NAME] = user.userName
            if (user.companies.size == 1) {
                preferences[KEY_TENANT] = user.companies.first().tenants.first().namespace
                SmobileApp.tonnentID = user.companies.first().tenants.first().namespace
            }
        }
    }

    suspend fun saveTenant(tenant: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TENANT] = tenant
            SmobileApp.tonnentID = tenant
        }
    }

    suspend fun deleteTenant()
    {
        context.dataStore.edit { preferences ->
            preferences[KEY_TENANT] = ""
            SmobileApp.tonnentID = ""
        }
    }

    val user : Flow<UserInfo?>
    get() = context.dataStore.data.map { preferences ->
        val jsonUser = preferences[KEY_USER]
        val gson = Gson()
        gson.fromJson(jsonUser, UserInfo::class.java)
    }

    val tenant : Flow<String>
        get() = context.dataStore.data.map { preferences ->
            preferences[KEY_TENANT] ?: ""
        }

    val collaborateurId : Flow<String>
    get() = context.dataStore.data.map { preferences ->
        preferences[KEY_COLLABORATEUR_ID] ?: ""
    }

    val image : Flow<String>
    get() = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_IMAGE] ?: ""
    }

    suspend fun isModeBorne(isModeBorne: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_MODE_BORNE] = isModeBorne
        }
    }

    val isModeBorne : Flow<Boolean>
    get() = context.dataStore.data.map { preferences ->
        preferences[KEY_IS_MODE_BORNE] ?: false
    }

    suspend fun isUserModeBorneLoggedIn(isUserModeBorneLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_USER_MODE_BORNE_LOGGED_IN] = isUserModeBorneLoggedIn
        }
    }

    val isUserModeBorneLoggedIn : Flow<Boolean>
    get() = context.dataStore.data.map { preferences ->
        preferences[KEY_IS_USER_MODE_BORNE_LOGGED_IN] ?: false
    }.catch {

    }

    val userEmail : Flow<String>
    get() = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_EMAIL] ?: ""
    }

    val userName : Flow<String>
    get() = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_NAME] ?: ""
    }

    companion object {
        const val USER_PREFERENCES_NAME = "USER_PREFERENCES_NAME"
        val KEY_USER = stringPreferencesKey("KEY_USER")
        val KEY_TENANT = stringPreferencesKey("KEY_Tenant")
        val KEY_COLLABORATEUR_ID = stringPreferencesKey("KEY_COLLABORATEUR_ID")
        val KEY_USER_IMAGE = stringPreferencesKey("KEY_USER_IMAGE")
        val KEY_USER_EMAIL = stringPreferencesKey("KEY_USER_EMAIL")
        val KEY_USER_NAME = stringPreferencesKey("KEY_USER_NAME")
        val KEY_IS_MODE_BORNE = booleanPreferencesKey("KEY_IS_MODE_BORNE")
        val KEY_IS_USER_MODE_BORNE_LOGGED_IN =
            booleanPreferencesKey("KEY_IS_USER_MODE_BORNE_LOGGED_IN")
        val LOGGED = booleanPreferencesKey("logged")
    }
}