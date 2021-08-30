package fr.strada.smobile.ui.pointeuse

import android.content.Context
import fr.strada.smobile.R
import fr.strada.smobile.data.models.CumulativeDayModel
import fr.strada.smobile.data.models.pointeuse.TimeModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

fun getCurrentDateTimeUTC(locale:Locale) : String {
    val sf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",locale)
    sf.timeZone = TimeZone.getTimeZone("UTC")
    val now = Date()
    return sf.format(now)
}

fun getDayAgo(locale:Locale,daysAgo: Int): String {
    val sf = SimpleDateFormat("yyyy-MM-dd'T'",locale)
    val calendar = Calendar.getInstance(locale)
    calendar.add(Calendar.DATE, - daysAgo)
    val date = calendar.time
    return "${sf.format(date)}00:00:00Z"
}

fun getDaysAgo(locale:Locale,daysAgo: Int): List<String> {
    val result = ArrayList<String>()
    val sf = SimpleDateFormat("yyyy-MM-dd",locale)
    val calendar = Calendar.getInstance(locale)
    for(i in 0..daysAgo){
        val date = sf.format(calendar.time)
        result.add(date)
        calendar.add(Calendar.DATE, - 1)
    }
    return result
}

fun getAbreviationMonthFromDate(context: Context,date:String):String{
    val month = date.substring(5,7)
    when(month){
        "01" -> {
            return context.getString(R.string._janv)
        }
        "02" -> {
            return context.getString(R.string._févr)
        }
        "03" -> {
            return context.getString(R.string._mars)
        }
        "04" -> {
            return context.getString(R.string._avril)
        }
        "05" -> {
            return context.getString(R.string._mai)
        }
        "06" -> {
            return context.getString(R.string._juin)
        }
        "07" -> {
            return context.getString(R.string._juil)
        }
        "08" -> {
            return context.getString(R.string._août)
        }
        "09"->{
            return context.getString(R.string._sept)
        }
        "10"->{
            return context.getString(R.string._oct)
        }
        "11"->{
            return context.getString(R.string._nov)
        }
        "12" -> {
            return context.getString(R.string._déc)
        }
        else -> {
            return "eror"
        }
    }
}

fun millisToTime(millis: Long): String {
    val seconds = (TimeUnit.MILLISECONDS.toSeconds(millis)
            - TimeUnit.MINUTES.toSeconds(
        TimeUnit.MILLISECONDS.toMinutes(
            millis
        )
    ))
    val minutes = (TimeUnit.MILLISECONDS.toMinutes(millis)
            - TimeUnit.HOURS.toMinutes(
        TimeUnit.MILLISECONDS.toHours(
            millis
        )
    ))
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    return (if (hours == 0L) "00:" else if (hours < 10) "0$hours:" else "$hours:") + (if (minutes == 0L) "00" else if (minutes < 10) "0$minutes" else minutes.toString())

}

fun getDaysBetweenTwoDates(startDate : String, endDate : String) : Long {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)
        val diff : Long = start!!.time - end!!.time
        TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }catch (ex:Exception){
        0
    }
}

fun getCurrentDate() : String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        sdf.format(date)
    }catch (ex:Exception){
        ""
    }
}

fun decrementeDateByOne(strDate: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(strDate)
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, -1)
        sdf.format(c.time)
    }catch (ex:Exception){
        ""
    }
}

