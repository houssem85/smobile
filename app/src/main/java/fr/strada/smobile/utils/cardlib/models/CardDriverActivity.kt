package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CardDriverActivity : RealmObject() {
    @PrimaryKey
    var CardId: String = ""

    @SerializedName("activityPointerOldestDayRecord")
    var activityPointerOldestDayRecord: Int = 0
    @SerializedName("activityPointerNewestRecord")
    var activityPointerNewestRecord: Int = 0
    @SerializedName("activityDailyRecords")
    var activityDailyRecords: RealmList<ActivityDailyRecords> = RealmList()

}