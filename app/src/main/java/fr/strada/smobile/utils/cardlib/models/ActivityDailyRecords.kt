package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ActivityDailyRecords : RealmObject() {
    @PrimaryKey
    @SerializedName("activityRecordDate")
    var activityRecordDate: String = ""

    @SerializedName("activityPreviousRecordLength")
    var activityPreviousRecordLength: Int = 0

    @SerializedName("activityRecordLength")
    var activityRecordLength: Int = 0

    @SerializedName("activityDailyPresenceCounter")
    var activityDailyPresenceCounter: Int = 0

    @SerializedName("activityDayDistance")
    var activityDayDistance: Int = 0
    @SerializedName("activityChangeInfo")
    var activityChangeInfo: RealmList<ActivityChangeInfo> = RealmList()

}