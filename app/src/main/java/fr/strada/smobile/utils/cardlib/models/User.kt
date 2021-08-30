package fr.strada.smobile.utils.cardlib.models

class User {

    lateinit var name : String
    lateinit var  email : String
    lateinit var phone : String
    lateinit var date : String
    lateinit var cardNumber : String

    constructor()

    constructor(email: String, phone: String, date: String) {
        this.email = email
        this.phone = phone
        this.date = date
    }
    constructor(name:String,email: String, phone: String, date: String,cardNumber: String) {
        this.email = email
        this.phone = phone
        this.date = date
        this.name = name
        this.cardNumber = cardNumber
    }
}