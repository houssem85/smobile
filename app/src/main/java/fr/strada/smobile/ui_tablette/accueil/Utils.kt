package fr.strada.smobile.ui_tablette.accueil

import android.app.Activity
import android.util.DisplayMetrics
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    fun getWidthScreen(activity:Activity):Int{
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getHeightScreen(activity:Activity):Int{
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun fromDpToPixels(activity:Activity,dpValue:Int):Int
    {
        val metrics = activity.resources.displayMetrics
        val dp = dpValue
        val fpixels = metrics.density * dp
        return (fpixels + 0.5f).toInt()
    }

    fun getFirstDayInCurrentWeek(firstDayInWeek:Int,locale: Locale) : String
    {
        val sf = SimpleDateFormat("yyyy-MM-dd",locale)
        val calendar = Calendar.getInstance(locale)
        calendar.firstDayOfWeek = firstDayInWeek
        calendar.set(Calendar.DAY_OF_WEEK,firstDayInWeek)
        val days = arrayOfNulls<String>(7)
        for (i in 0..6) {
            days[i] = sf.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return days[0]!!
    }

    fun getLastDayInCurrentWeek(firstDayInWeek:Int,locale: Locale) : String
    {
        val sf = SimpleDateFormat("yyyy-MM-dd",locale)
        val calendar = Calendar.getInstance(locale)
        calendar.firstDayOfWeek = firstDayInWeek
        calendar.set(Calendar.DAY_OF_WEEK,firstDayInWeek)
        val days = arrayOfNulls<String>(7)
        for (i in 0..6) {
            days[i] = sf.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return days[6]!!
    }
}