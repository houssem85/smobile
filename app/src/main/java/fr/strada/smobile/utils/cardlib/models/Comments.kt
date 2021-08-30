package fr.strada.smobile.utils.cardlib.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Comments : RealmObject {

    @PrimaryKey
    var commID : String = ""
    var date : String = ""
    var comment : String = ""
    var emailUser:String =""
    constructor()
    constructor(date: String, comment: String,emailUser:String) : super() {
        this.commID = UUID.randomUUID().toString()
        this.date = date
        this.comment = comment
        this.emailUser = emailUser
    }

    constructor(commID: String,date: String, comment: String,emailUser:String) : super() {
        this.commID = commID
        this.date = date
        this.comment = comment
        this.emailUser = emailUser
    }

}