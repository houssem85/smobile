package fr.strada.smobile.utils.cardlib.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CardDownload : RealmObject() {
    @PrimaryKey
    var LastCardDownloadId = 1
    var LastCardDownload : String = ""
    var notificationChecked : Boolean = true
}