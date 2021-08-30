package fr.strada.smobile.data.repositories

import android.content.Context
import android.content.SharedPreferences
import fr.strada.smobile.utils.IS_SYNCHRONIZED
import fr.strada.smobile.utils.PREFERENCE_NAME
import javax.inject.Inject

class ReglageRepository @Inject constructor(val context: Context) {


    private val pref: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val editor = pref.edit()

   // private val gson = Gson()


    private fun String.put(boolean: Boolean) {
        editor.putBoolean(this, boolean)
        editor.commit()
    }


    private fun String.getBoolean() = pref.getBoolean(this, false)


    fun loggedIn(isLoggedIn: Boolean) {
        IS_SYNCHRONIZED.put(isLoggedIn)
    }

    fun loggedIn() = IS_SYNCHRONIZED.getBoolean()

}