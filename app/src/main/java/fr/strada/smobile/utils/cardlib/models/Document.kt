package fr.strada.smobile.utils.cardlib.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

open class Document : RealmObject, Comparable<Document> {

    @PrimaryKey
    var docId: String = ""
    var docName: String = ""
    var docEcheance = ""
    var notification = false
    var currentJourNotfication: Int = 1

    var file = ""
    var file1 = ""
    var file2 = ""

    // multi utilisateur
    var emailUser: String = ""

    constructor()
    constructor(
        docId: String,
        docName: String,
        docEcheance: String,
        notification: Boolean,
        currentJourNotfication: Int,
        file: String,
        file1: String,
        file2: String,
        emailUser: String
    ) : super() {
        this.docId = docId
        this.docName = docName
        this.docEcheance = docEcheance
        this.notification = notification
        this.currentJourNotfication = currentJourNotfication
        this.file = file
        this.file1 = file1
        this.file2 = file2
        this.emailUser = emailUser

    }

    override fun compareTo(other: Document): Int {
        val luangageFormatFrancais = SimpleDateFormat("d MMMM yyyy", Locale.FRENCH)
        var myDocumentDateEcheance = Date()
        var otherDocumentDateEcheance = Date()
        try {
            myDocumentDateEcheance = luangageFormatFrancais.parse(this.docEcheance)
            otherDocumentDateEcheance = luangageFormatFrancais.parse(other.docEcheance)
        } catch (ex: Exception) {

        }
        if (myDocumentDateEcheance.after(otherDocumentDateEcheance)) {
            return 1
        } else {
            return -1
        }
    }
}