package fr.strada.smobile.ui.card

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import fr.strada.smobile.data.models.userinfo.UserInfo
import fr.strada.smobile.utils.cardlib.models.User
import javax.inject.Inject

class StorageCardTemporaryManager  @Inject constructor(var application: Application) {

    companion object {
        private const val PREFS_NAME = "CARD_PREFS"
        private const val TYPE_FILE = "TYPE_FILE"
        private const val IS_SACANED_IN_FIRST_TIME = "IS_FIRSTSCANNED_IN_FIRST_TIME"
    }

    private var shared: SharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        val editor = shared.edit()
        editor.putString("email", user.email)
        editor.putString("phone", user.phone)
        editor.putString("date", user.date)
        editor.apply()
    }
    fun saveUser1(user: UserInfo) {
        val editor = shared.edit()
        editor.putString("email", user.email)
        editor.putString("phone", user.phone)
        editor.putString("customerName", user.firstName + " "+ user.lastName)
        editor.putString("lastName", user.lastName)
        editor.putString("firstName",user.firstName)
        editor.apply()
    }

    fun setModeBorne (borne : Boolean){
        val editor = shared.edit()
        editor.putBoolean("Borne",borne)
        editor.apply()
    }
    fun getModeBorne () : Boolean {
        return shared.getBoolean("Borne",false)
    }
    fun setCollabID (id : String){
        val editor = shared.edit()
        editor.putString("custID",id)
        editor.apply()
    }
    fun getCollabID () : String {
        return shared.getString("custID","").toString()
    }
    fun setImage (id : String){
        val editor = shared.edit()
        editor.putString("image",id)
        editor.apply()
    }
    fun getImage () : String {
        return shared.getString("image","").toString()
    }
    fun setDriverID (id : String){
        val editor = shared.edit()
        editor.putString("driverID",id)
        editor.apply()
    }
    fun getDriverID () : String {
        return shared.getString("driverID","").toString()
    }
    fun setTenantID (tenant : String){
        val editor = shared.edit()
        editor.putString("tenantStr",tenant)
        editor.apply()
    }
    fun getTenantID  () : String {
        return shared.getString("tenantStr","").toString()
    }
    fun getnom () : String {
        return shared.getString("customerName","").toString()
    }
    fun getfirst  () : String {
        return shared.getString("firstName","").toString()
    }
    fun getlast  () : String {
        return shared.getString("lastName","").toString()
    }
    fun getemail  () : String {
        return shared.getString("email","").toString()
    } fun getphone  () : String {
        return shared.getString("phone","").toString()
    }
    fun settenant (borne : Boolean){
        val editor = shared.edit()
        editor.putBoolean("tenant",borne)
        editor.apply()
    }
    fun gettenant () : Boolean {
        return shared.getBoolean("tenant",false)
    }
    fun setModeBorneLogin (borne : Boolean){
        val editor = shared.edit()
        editor.putBoolean("Bornelogin",borne)
        editor.apply()
    }
    fun getModeBorneLogin () : Boolean {
        return shared.getBoolean("Bornelogin",false)
    }

    fun saveUserName(name: String,cardNumber:String) {
        val editor = shared.edit()
        editor.putString("name", name)
        editor.putString("cardNumber",cardNumber)
        editor.apply()
    }

    fun saveCardDate(date: String?)
    {
        val editor = shared.edit()
        if(date!=null){
            editor.putString("card_date", date)
        }else{
            editor.putString("card_date", "")
        }
        editor.apply()
    }

    fun clearUser(){
        shared.edit().clear().apply()
    }

    fun getUser() : User {
        return User(
            shared.getString("name","").toString(),
            shared.getString("email","").toString(),
            shared.getString("phone","").toString(),
            shared.getString("date","").toString(),
            shared.getString("cardNumber","").toString()
        )
    }

    fun setFileType(fileType:String)
    {
        val editor = shared.edit()
        editor.putString(TYPE_FILE, fileType)
        editor.apply()
    }

    fun getFileType(): String?
    {
        return shared.getString(TYPE_FILE, "C1B")
    }

    fun setScannedFirstTime(firstTime:Boolean)
    {
        val editor = shared.edit()
        editor.putBoolean(IS_SACANED_IN_FIRST_TIME, firstTime)
        editor.apply()
    }

    fun isScannedFirstTime(): Boolean
    {
        return shared.getBoolean(IS_SACANED_IN_FIRST_TIME, true)
    }
}