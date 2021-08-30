package fr.strada.smobile.utils.cardlib.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Notifications : RealmObject{
    @PrimaryKey
    lateinit var id : String
    lateinit var date : String
    lateinit var title : String
    var currentJourNotfication : Int = 1
    var checkEd : Boolean = false

    // multi utilisateur
    var emailUser:String = ""

    constructor()
    constructor(id: String, Title: String,Date: String, checkEd: Boolean , currentJourNotfication : Int,emailUser:String) : super() {
        this.id = id
        this.date = Date
        this.title = Title
        this.checkEd = checkEd
        this.currentJourNotfication = currentJourNotfication
        this.emailUser = emailUser
    }

}