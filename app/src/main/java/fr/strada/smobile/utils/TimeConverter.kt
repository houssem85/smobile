package fr.strada.smobile.utils

import fr.strada.smobile.data.models.activites.Value
import fr.strada.smobile.data.models.pointeuse.TimeModel
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun convertUTCtoLocalTime(list: List<TimeModel>, today: Date?): List<TimeModel> {

    val cal = Calendar.getInstance()
    list.forEach {
        val dtStart = it.dateActivite!!
        val dtEnd = it.finActivite
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sszzzzz", Locale.getDefault())
        try {
            val dateStr = format.parse(dtStart)
            val format2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            cal.time = dateStr!!
            it.heureDebutActivite!!.hours = cal.get(Calendar.HOUR_OF_DAY).toFloat()
            it.heureDebutActivite!!.minutes = cal.get(Calendar.MINUTE).toFloat()
            val s = format.format(dateStr)
            it.dateActivite = s
            if (it.finActivite.isNullOrEmpty()) {
                val dateEnd = format.parse(dtEnd!!)
                cal.time = dateEnd!!
                it.heureFinActivite!!.hours = cal.get(Calendar.HOUR_OF_DAY).toFloat()
                it.heureFinActivite!!.minutes = cal.get(Calendar.MINUTE).toFloat()
                it.finActivite = format.format(dateEnd)
            }


        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }
    return createActivitePointeuse(list, today)
}

fun createActivitePointeuse(list: List<TimeModel>, today: Date?): List<TimeModel> {
    val startdate = Calendar.getInstance()
    val endDate = Calendar.getInstance()
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val mToday = format.parse(format.format(today!!))
    val listAll = ArrayList<TimeModel>()
    Timber.tag("List size ").e(listAll.size.toString())
    with(list.iterator()) {
        forEach {
            listAll.add(it)
            val dateStr = format.parse(it.dateActivite!!)
            startdate.time = dateStr!!
            if (it.finActivite.isNullOrEmpty()) {
                val datEnd = format.parse(it.finActivite!!)
                endDate.time = datEnd!!
            }
            if (startdate.before(endDate) && (it.finActivite.isNullOrEmpty())) {
                val newEntry = TimeModel()
                val newValue = Value(0, 0F, 0F, 0F)
                newEntry.id = it.id
                newEntry.heureDebutActivite = newValue
                val newValueEnd = Value(
                    it.heureFinActivite!!.totalMilliseconds,
                    it.heureFinActivite!!.totalHours,
                    it.heureFinActivite!!.hours,
                    it.heureFinActivite!!.minutes
                )
                newEntry.heureFinActivite = newValueEnd
                newEntry.duree = newValueEnd
                newEntry.finActivite = it.finActivite
                newEntry.dateActivite = getMidNightStart(endDate)
                newEntry.typeActivite = it.typeActivite
                listAll.add(newEntry)
                Timber.tag("List size after ").e(listAll.size.toString())
                it.finActivite = getMidNight(startdate)
                it.heureFinActivite!!.totalMilliseconds = 86399000
                it.heureFinActivite!!.hours = 23F
                it.heureFinActivite!!.minutes = 59F
                it.heureFinActivite!!.totalHours = 23.99972222222222F
                it.duree!!.totalMilliseconds =
                    it.heureFinActivite!!.totalMilliseconds - it.heureDebutActivite!!.totalMilliseconds
                it.duree!!.hours = it.heureFinActivite!!.hours - it.heureDebutActivite!!.hours
                it.duree!!.minutes = it.heureFinActivite!!.minutes - it.heureDebutActivite!!.minutes
                it.duree!!.totalHours =
                    it.heureFinActivite!!.totalHours - it.heureDebutActivite!!.totalHours
                Timber.e(it.finActivite)
            }
        }
    }
    val newList: ArrayList<TimeModel> = ArrayList()
    listAll.forEach { timeModel ->
        val dateStr = format.parse(timeModel.dateActivite!!)
        startdate.time = dateStr!!
        val datEnd = format.parse(timeModel.finActivite!!)
        endDate.time = datEnd!!
        val b = startdate.time == mToday
        Timber.tag("TESTBoolean ").e(b.toString())
        val b1 = endDate.time == mToday
        Timber.tag("TESTBoolean1 ").e(b1.toString())
        if (startdate.time == mToday && endDate.time == mToday) {
            newList.add(timeModel)
            Timber.tag("New List").e(timeModel.dateActivite)
            Timber.tag("New List").e(timeModel.finActivite)
        }
        newList.forEach {
            Timber.tag("newList Data ").e(it.toString())
        }
    }
    return newList

}

fun getMidNight(c: Calendar): String {
    c.set(Calendar.HOUR_OF_DAY, 23)
    c.set(Calendar.MINUTE, 59)
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    return format.format(c.time)
}

fun getMidNightStart(c: Calendar): String {
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    return format.format(c.time)
}