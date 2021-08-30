package fr.strada.smobile.utils

import androidx.preference.PreferenceManager
import fr.strada.smobile.SmobileApp


object SharedPreferencesUtils {

    private val IS_LOGGED_IN_KEY = "IS_LOGGED_IN_KEY"
    private val IS_SACANED_IN_FIRST_TIME = "IS_FIRSTSCANNED_IN_FIRST_TIME"
    private val SESSION_TOKEN_KEY = "SESSION_TOKEN_KEY"
    // SETINGS APPLICATION
    private val TIME_MODE_HHMM = "TIME_MODE_HHMM"
    private val TIME_MODE_FORMAT = "TIME_MODE_FORMAT"
    private val TIME_MODE_FORMAT_AUTO = "TIME_MODE_FORMAT_AUTO"
    //
    private val FIRST_DAY_MONTH = "FIRST_DAY_MONTH"
    private val NIGHT_STAR = "NIGHT_STAR"
    private val NIGHT_END = "NIGHT_END"
    private val TYPE_FILE = "TYPE_FILE"
    // SETTINGS NOTIFCATION FRPR CARD
    private val DELAIS_AVERTISSEMENT = "DELAIS_AVERTISSEMENT"
    private val DELAIS_AVERTISSEMENT_CARTE = "DELAIS_AVERTISSEMENT_CARTE"
    // ete hiver time
    private val IS_HIVER_ETE = "IS_HIVER_ETE"
    private val DECALAGE_MINUTES="DECALAGE_MINUTES"
    // heuere notification
    private val HEURE_NOTIFICATION = "HEURE_NOTIFICATION"
    // rappel a chaque apelle envoie de fichier b1b au dispatcher
    private val IS_POPUP_SHOW_DISPATCHER_FILE= "IS_POPUP_SHOW_DISPATCHER_FILE"
    // First open of app
    private val IS_FIRST_OPEN= "IS_FIRST_OPEN"
    // auth 0 informations
    private val REFRESH_TOKEN="REFRESH_TOKEN"
    private val ID_TOKEN="ID_TOKEN"
    //
    private val IS_LOGGING_ENABLED_KEY = "IS_LOGGING_ENABLED_KEY"
    //

    var isLoggedIn: Boolean
        get() = getPreferencesBoolean(IS_LOGGED_IN_KEY, false)
        set(isLoggedIn) = savePreferencesBoolean(IS_LOGGED_IN_KEY, isLoggedIn)

    var isSCANNEDInFirstTime: Boolean
        get() = getPreferencesBoolean(IS_SACANED_IN_FIRST_TIME, false)
        set(isLoggedIn) = savePreferencesBoolean(IS_SACANED_IN_FIRST_TIME, isLoggedIn)
    ///
    var isTimeModeHHMM: Boolean
        get() = getPreferencesBoolean(TIME_MODE_HHMM, true)
        set(isHHMM) = savePreferencesBoolean(TIME_MODE_HHMM, isHHMM)

    var timeFormat: String?
        get() = getPreferencesString(TIME_MODE_FORMAT, "UTC")
        set(format) = savePreferencesString(TIME_MODE_FORMAT, format!!)

    var timeFormatAUTO: String?
        get() = getPreferencesString(TIME_MODE_FORMAT_AUTO, "")
        set(format) = savePreferencesString(TIME_MODE_FORMAT_AUTO, format!!)
    ///
    var firstDayOfMonth: Int?
        get() = getPreferencesInt(FIRST_DAY_MONTH, 1)
        set(format) = savePreferencesInt(FIRST_DAY_MONTH, format!!)

    var delaisAvertisement: Int?
        get() = getPreferencesInt(DELAIS_AVERTISSEMENT, 7)
        set(format) = savePreferencesInt(DELAIS_AVERTISSEMENT, format!!)

    var delaisAvertisementCarte: Int?
        get() = getPreferencesInt(DELAIS_AVERTISSEMENT_CARTE, 90)
        set(format) = savePreferencesInt(DELAIS_AVERTISSEMENT_CARTE, format!!)

    var nightStart: String?
        get() = getPreferencesString(NIGHT_STAR, "21:00")
        set(format) = savePreferencesString(NIGHT_STAR, format!!)

    var fileType: String?
        get() = getPreferencesString(TYPE_FILE, "C1B")
        set(format) = savePreferencesString(TYPE_FILE, format!!)

    var nightend: String?
        get() = getPreferencesString(NIGHT_END, "06:00")
        set(format) = savePreferencesString(NIGHT_END, format!!)

    var isEteHiver: Boolean
        get() = getPreferencesBoolean(IS_HIVER_ETE, false)
        set(isEteHiver) = savePreferencesBoolean(IS_HIVER_ETE, isEteHiver)

    var decalageMinutes: Int?
        get() = getPreferencesInt( DECALAGE_MINUTES , 0)  //  a parametre from code
        set(decalageMinutes) = savePreferencesInt( DECALAGE_MINUTES , decalageMinutes!!)

    var isPopupShowDispatcherFile: Boolean?
        get() = getPreferencesBoolean( IS_POPUP_SHOW_DISPATCHER_FILE , false)
        set(isPopupShowDispatcherFile) = savePreferencesBoolean( IS_POPUP_SHOW_DISPATCHER_FILE ,isPopupShowDispatcherFile!!)

    var UserSessionToken: String?
        get() = getPreferencesString(SESSION_TOKEN_KEY, "")
        set(UserSessionToken) = savePreferencesString(SESSION_TOKEN_KEY, UserSessionToken.toString())

    var heureNotification: String?
        get() = getPreferencesString(HEURE_NOTIFICATION, "08:00")
        set(heureNotification) = savePreferencesString(HEURE_NOTIFICATION , heureNotification!!)

    var isFirstOpen: Boolean?
        get() = getPreferencesBoolean(IS_FIRST_OPEN, true)
        set(isFirstOpen) = savePreferencesBoolean(IS_FIRST_OPEN , isFirstOpen!!)

    var refreshToken: String?
        get() = getPreferencesString(REFRESH_TOKEN, "")
        set(refreshToken) = savePreferencesString(REFRESH_TOKEN, refreshToken!!)

    var idToken: String?
        get() = getPreferencesString(ID_TOKEN, "")
        set(refreshToken) = savePreferencesString(ID_TOKEN, refreshToken!!)

    var isLoggingEnabled: Boolean
        get() = getPreferencesBoolean(IS_LOGGING_ENABLED_KEY, true)
        set(isLoggingEnabled) = savePreferencesBoolean(IS_LOGGING_ENABLED_KEY, isLoggingEnabled)

     fun savePreferencesString(key: String, value: String) {
        PreferenceManager.getDefaultSharedPreferences(SmobileApp.instance).edit().putString(key, value).apply()
    }

     fun getPreferencesString(key: String, defaultValue: String): String? {
        return PreferenceManager.getDefaultSharedPreferences(SmobileApp.instance).getString(key, defaultValue)
    }

     fun savePreferencesBoolean(key: String, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(SmobileApp.instance).edit().putBoolean(key, value).apply()
    }

     fun getPreferencesBoolean(key: String, defaultValue: Boolean): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(SmobileApp.instance).getBoolean(key, defaultValue)
    }

     fun savePreferencesInt(key: String, value: Int) {
        PreferenceManager.getDefaultSharedPreferences(SmobileApp.instance).edit().putInt(key, value).apply()
    }

     fun getPreferencesInt(key: String, defaultValue: Int): Int {
        return PreferenceManager.getDefaultSharedPreferences(SmobileApp.instance).getInt(key, defaultValue)
    }

    fun clear(){
        PreferenceManager.getDefaultSharedPreferences(SmobileApp.instance).edit().clear().apply()
    }


}
