package fr.strada.smobile.utils

import com.google.gson.Gson
import fr.strada.smobile.data.models.activites.Value
import fr.strada.smobile.data.models.pointeuse.Activitie
import fr.strada.smobile.data.models.pointeuse.JourActivite
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun convertUTCtoLocalTime(list: List<JourActivite>, today: Date? = null): List<JourActivite> {
    val cal = Calendar.getInstance()
    list.forEach { jourActivite ->
        jourActivite.activites!!.forEach {
            val dtStart = it.dateActivite!!
            val dtEnd = it.finActivite
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
            try {
                val dateStr = format.parse(dtStart)
                cal.time = dateStr!!
                it.heureDebut!!.hours = cal.get(Calendar.HOUR_OF_DAY).toFloat()
                it.heureDebut!!.minutes = cal.get(Calendar.MINUTE).toFloat()
                val s = format.format(dateStr)
                Timber.tag("Date").e(s)
                it.dateActivite = s
                if (it.finActivite != null) {
                    val dateEnd = format.parse(dtEnd!!)
                    cal.time = dateEnd!!
                    it.heureFin!!.hours = cal.get(Calendar.HOUR_OF_DAY).toFloat()
                    it.heureFin!!.minutes = cal.get(Calendar.MINUTE).toFloat()
                    it.finActivite = format.format(dateEnd)
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }
    return createActivitePointeuse(list, today)
}

fun createActivitePointeuse(list: List<JourActivite>, today: Date?): List<JourActivite> {
    val startdate = Calendar.getInstance()
    val endDate = Calendar.getInstance()
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val listAll = ArrayList<JourActivite>()
    val listActivitie = ArrayList<Activitie>()
    Timber.tag("List size ").e(listAll.size.toString())
    with(list.iterator()) {
        forEach { jourActivite ->
            jourActivite.activites!!.forEach {
                val dateStr = format.parse(it.dateActivite!!)
                startdate.time = dateStr!!
                if (it.finActivite != null) {
                    val datEnd = format.parse(it.finActivite!!)
                    endDate.time = datEnd!!
                }
                if (startdate.before(endDate) && (it.finActivite != null)) {
                    val newEntry = Activitie()
                    val newValue = Value(0, 0F, 0F, 0F)
                    newEntry.heureDebut = newValue
                    if (it.finActivite != null) {
                        val newValueEnd = Value(
                            it.heureFin!!.totalMilliseconds,
                            it.heureFin!!.totalHours,
                            it.heureFin!!.hours,
                            it.heureFin!!.minutes
                        )
                        newEntry.heureFin = newValueEnd
                        newEntry.dureeActivite = newValueEnd
                        newEntry.finActivite = it.finActivite
                        newEntry.dateActivite = getMidNightStart(endDate)
                        newEntry.typeActivitePointeuse = it.typeActivitePointeuse
                        listActivitie.add(newEntry)
                    }
                    it.finActivite = getMidNight(startdate)
                    it.heureFin!!.totalMilliseconds = 86399000
                    it.heureFin!!.hours = 23F
                    it.heureFin!!.minutes = 59F
                    it.heureFin!!.totalHours = 23.99972222222222F
                    it.dureeActivite!!.totalMilliseconds =
                        it.heureFin!!.totalMilliseconds - it.heureDebut!!.totalMilliseconds
                    it.dureeActivite!!.hours = it.heureFin!!.hours - it.heureDebut!!.hours
                    it.dureeActivite!!.minutes = it.heureFin!!.minutes - it.heureDebut!!.minutes
                    it.dureeActivite!!.totalHours =
                        it.heureFin!!.totalHours - it.heureDebut!!.totalHours
                    Timber.e(it.finActivite)
                    listActivitie.add(it)
                    jourActivite.activites = listActivitie
                } else
                    listActivitie.add(it)
            }

        }
    }
    val lista = listActivitie.groupBy {
        it.dateActivite!!.take(10)
    }
    lista.forEach {
        val newJourActivite = JourActivite()
        val date = it.key + "T00:00:00+00:00"
        newJourActivite.jourActivite = date
        newJourActivite.activites = it.value
        newJourActivite.commentaires = emptyList()
        listAll.add(newJourActivite)
        Timber.tag("List size after ").e(date)
    }
    listAll.sortBy { it.jourActivite!!.take(10) }
    listAll.reverse()
    listAll.forEach {
        val jsoonView = Gson().toJson(it)
        // Timber.tag("List Final ").e(jsoonView!!)
    }
    Timber.tag("List ").e(Gson().toJson(listAll))
    return listAll

}

