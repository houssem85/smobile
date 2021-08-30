package fr.strada.smobile.data.local

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsPreferences @Inject constructor(val context: Application) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_PREFERENCES_NAME)

    companion object {
        const val SETTINGS_PREFERENCES_NAME = "SETTINGS_PREFERENCES_NAME"
        val LOGGED = booleanPreferencesKey("logged")
    }

    val logged: Flow<Boolean> = context.dataStore.data.map { settings ->
        settings[LOGGED] ?: false
    }

    suspend fun setStatus(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[LOGGED] = value
        }
    }
}