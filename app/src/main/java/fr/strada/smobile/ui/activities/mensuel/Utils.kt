package fr.strada.smobile.ui.activities.mensuel

import android.content.Context
import com.shrikanthravi.collapsiblecalendarview.data.Event
import fr.strada.smobile.R
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.utils.ISO_DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.WEEK_OF_YEAR
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

object Utils {

    fun getFirstDayInCalenderOfYearMonth(year:Int,month:Int,firstDayOfWeek:Int,locale:Locale): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd",locale)
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.firstDayOfWeek = firstDayOfWeek
        calendar.time = Utils.getDate(year, month+1, 1, locale)
        calendar.set(Calendar.DAY_OF_WEEK,calendar.firstDayOfWeek)
        return sdf.format(calendar.time)
    }

    fun getLastDayInCalenderOfYearMonth(year:Int,month:Int,firstDayOfWeek:Int,locale:Locale): String
    {
        val sdf = SimpleDateFormat("yyyy-MM-dd", locale)
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.firstDayOfWeek = firstDayOfWeek
        calendar.time = Utils.getDate(year, month + 1, 1, locale)
        val numberDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.time = Utils.getDate(year, month + 1, numberDaysOfMonth, locale)
        val lastDayOfWeek =
            if (calendar.firstDayOfWeek == Calendar.SUNDAY) Calendar.SATURDAY else Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK, lastDayOfWeek)
        return sdf.format(calendar.time)
    }

    fun getLastDayMonth(year: Int, month: Int, locale: Locale): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", locale)
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.time = Utils.getDate(year, month + 1, 1, locale)
        val numberDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.time = Utils.getDate(year, month + 1, numberDaysOfMonth, locale)

        return sdf.format(calendar.time)
    }

    fun getFirstDayMonth(year: Int, month: Int, locale: Locale): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", locale)
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.time = Utils.getDate(year, month + 1, 1, locale)
        return sdf.format(calendar.time)
    }

    fun getMonthName(context: Context, month: Int): String {
        when (month) {
            0 -> {
                return context.resources.getString(R.string.janvier)
            }
            1 -> {
                return context.resources.getString(R.string.fevrier)
            }
            2 -> {
                return context.resources.getString(R.string.mars)
            }
            3 -> {
                return context.resources.getString(R.string.avril)
            }
            4-> {
                return context.resources.getString(R.string.mai)
            }
            5-> {
                return context.resources.getString(R.string.juin)
            }
            6-> {
                return context.resources.getString(R.string.juillet)
            }
            7-> {
                return context.resources.getString(R.string.août)
            }
            8-> {
                return context.resources.getString(R.string.septembre)
            }
            9-> {
                return context.resources.getString(R.string.octobre)
            }
            10-> {
                return context.resources.getString(R.string.novembre)
            }
            11->{
                return context.resources.getString(R.string.décembre)
            }
            else->{
                return ""
            }
        }
    }

    fun getWeeksNumbersInMonth(month:Int,year:Int,locale: Locale,firstDayOfWeek:Int):ArrayList<Int>
    {
        val cal = Calendar.getInstance(locale)
        cal.firstDayOfWeek = firstDayOfWeek
        cal.set(Calendar.MONTH,month)
        cal.set(Calendar.YEAR,year)
        val numberDaysOfMonth= cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val list = ArrayList<Int>()
        for(i in 1..numberDaysOfMonth)
        {
            cal.set(Calendar.DAY_OF_MONTH, i)
            val rang= cal.get(WEEK_OF_YEAR)
            if(!list.contains(rang)){
                list.add(rang)
            }
        }

        return list
    }

    /**
     * decrement month because in calendar view months begin with 0
     * @param srtdate ex : 2021-04-27T08:37:28.913Z
     */
    fun convertStrDateToEvent(srtdate:String) : Event
    {
        val year = srtdate.substring(0,4).toInt() // 2019
        val month = srtdate.substring(5,7).toInt() // 11
        val day = srtdate.substring(8,10).toInt() // 22
        val event = Event(year,month-1,day)
        return event
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
        return ((if (hours == 0L) "00:" else if (hours < 10) "0$hours:" else "$hours:") +
                (if (minutes == 0L) "00" else if (minutes < 10) "0$minutes" else minutes.toString()))
    }

    fun timeToMillis(time: String): Int {
      // time format  20:10
      val hours= time.substring(0,2).toInt()
      val minutes = time.substring(3,5).toInt()
        return (hours * 60 + minutes) * 60 * 1000
    }

    fun fromIsoFormatToDate(strDate:String):Date {
        val simpleDateFormat = SimpleDateFormat(ISO_DATE_PATTERN, Locale.FRANCE)
        return simpleDateFormat.parse(strDate)
    }
}