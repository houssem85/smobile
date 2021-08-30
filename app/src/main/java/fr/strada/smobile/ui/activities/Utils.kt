package fr.strada.smobile.ui.activities

import android.content.Context
import android.os.Build
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.R
import java.util.*
import kotlin.collections.ArrayList

object Utils {

    fun getFirstDayInCalenderOfYear(year:Int,firstDayOfWeek:Int,locale:Locale): Date {
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.firstDayOfWeek = firstDayOfWeek
        calendar.time = getDate(year, 1, 1,locale)
        calendar.set(Calendar.DAY_OF_WEEK,calendar.firstDayOfWeek)
        return calendar.time
    }

    fun getLastDayInCalenderOfYear(year:Int,firstDayOfWeek:Int,locale:Locale): Date
    {
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.firstDayOfWeek = firstDayOfWeek
        calendar.time = getDate(year, 12, 31,locale)
        val lastDayOfWeek= if(calendar.firstDayOfWeek == Calendar.SUNDAY ) Calendar.SATURDAY else Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK,lastDayOfWeek)
        return calendar.time
    }

    fun getDate(year: Int,month:Int,day:Int,locale:Locale): Date {
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.set(year,month-1,day)
        return calendar.time
    }

    fun getWeeksOfYear(year:Int,firstDayOfWeek:Int,locale:Locale): WeeksOfYear {

        val weeksNumbers = ArrayList<Int>()
        val startDates = ArrayList<Day>()
        val endDates = ArrayList<Day>()

        val firstDayInCalenderOfYear= getFirstDayInCalenderOfYear(year, firstDayOfWeek,locale)
        val lastDayInCalenderOfYear= getLastDayInCalenderOfYear(year, firstDayOfWeek,locale)
        val cFirstDayInCalenderOfYear = Calendar.getInstance(locale)
        cFirstDayInCalenderOfYear.clear()
        cFirstDayInCalenderOfYear.time = firstDayInCalenderOfYear
        val cEnd = Calendar.getInstance(locale)
        cEnd.clear()
        cEnd.firstDayOfWeek = firstDayOfWeek
        cEnd.time = lastDayInCalenderOfYear

        do {
            val weekNumber = cFirstDayInCalenderOfYear.get(Calendar.WEEK_OF_YEAR)
            weeksNumbers.add(weekNumber)
            val startDate = Day(cFirstDayInCalenderOfYear.get(Calendar.YEAR),cFirstDayInCalenderOfYear.get(Calendar.MONTH)+1,cFirstDayInCalenderOfYear.get(Calendar.DAY_OF_MONTH))
            startDates.add(startDate)
            cFirstDayInCalenderOfYear.add(Calendar.DAY_OF_MONTH, 6)
            val endDate = Day(cFirstDayInCalenderOfYear.get(Calendar.YEAR),cFirstDayInCalenderOfYear.get(Calendar.MONTH)+1,cFirstDayInCalenderOfYear.get(Calendar.DAY_OF_MONTH))
            endDates.add(endDate)
            cFirstDayInCalenderOfYear.add(Calendar.DAY_OF_MONTH, 1)
        }while (cFirstDayInCalenderOfYear.before(cEnd) || cFirstDayInCalenderOfYear == cEnd)
        return WeeksOfYear(
            year,
            startDates.size,
            weeksNumbers,
            startDates,
            endDates
        )
    }

    fun getDisplayedValuesFromWeeksOfYear(weeksOfYear:WeeksOfYear,context: Context):Array<String>
    {
        val displayedValues = Array(weeksOfYear.weeksCount){"it = $it"}
        for (i in 0 until weeksOfYear.weeksCount){
            val startDate = "${String.format("%02d",weeksOfYear.startDates[i].day)}/${String.format("%02d",weeksOfYear.startDates[i].month)}/${weeksOfYear.startDates[i].year}"
            val endDate = "${String.format("%02d",weeksOfYear.endDates[i].day)}/${String.format("%02d",weeksOfYear.endDates[i].month)}/${weeksOfYear.endDates[i].year}"
            val week = "${context.resources.getString(R.string.semaine)} ${weeksOfYear.weeksNumbers[i]}"
            displayedValues[i]="$week : $startDate - $endDate"
        }
        return displayedValues
    }

    fun getCurrentLocal(context: Context):Locale{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            context.resources.configuration.locales.get(0)
        } else{
            context.resources.configuration.locale
        }
    }

    fun inFromLeftAnimation(): Animation {

        val inFromLeft = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        inFromLeft.duration = 1000
        inFromLeft.interpolator = AccelerateInterpolator()
        return inFromLeft
    }

    fun inFromRightAnimation(): Animation {

        val inFromRight = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, +1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        inFromRight.duration = 1000
        return inFromRight
    }

    fun inFromBottomAnimation(): Animation {

        val inFromBottom = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, +1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        inFromBottom.duration = 1000
        return inFromBottom
    }

    fun getWeeksOfYearMonth(year:Int,month:Int,firstDayOfWeek:Int,locale:Locale): WeeksOfMonth {

        val weeksNumbers = ArrayList<Int>()
        val startDates = ArrayList<Day>()
        val endDates = ArrayList<Day>()

        val firstDayInCalenderOfYearMonth= getFirstDayInCalenderOfYearMonth(year, month,firstDayOfWeek,locale)
        val lastDayInCalenderOfYearMonth= getLastDayInCalenderOfYearMonth(year,month ,firstDayOfWeek,locale)
        val cFirstDayInCalenderOfYear = Calendar.getInstance(locale)
        cFirstDayInCalenderOfYear.clear()
        cFirstDayInCalenderOfYear.time = firstDayInCalenderOfYearMonth
        val cEnd = Calendar.getInstance(locale)
        cEnd.clear()
        cEnd.firstDayOfWeek = firstDayOfWeek
        cEnd.time = lastDayInCalenderOfYearMonth

        do {
            val weekNumber = cFirstDayInCalenderOfYear.get(Calendar.WEEK_OF_YEAR)
            weeksNumbers.add(weekNumber)
            val startDate = Day(cFirstDayInCalenderOfYear.get(Calendar.YEAR),cFirstDayInCalenderOfYear.get(Calendar.MONTH)+1,cFirstDayInCalenderOfYear.get(Calendar.DAY_OF_MONTH))
            startDates.add(startDate)
            cFirstDayInCalenderOfYear.add(Calendar.DAY_OF_MONTH, 6)
            val endDate = Day(cFirstDayInCalenderOfYear.get(Calendar.YEAR),cFirstDayInCalenderOfYear.get(Calendar.MONTH)+1,cFirstDayInCalenderOfYear.get(Calendar.DAY_OF_MONTH))
            endDates.add(endDate)
            cFirstDayInCalenderOfYear.add(Calendar.DAY_OF_MONTH, 1)
        }while (cFirstDayInCalenderOfYear.before(cEnd) || cFirstDayInCalenderOfYear == cEnd)
        return WeeksOfMonth(
            year,month,
            startDates.size,
            weeksNumbers,
            startDates,
            endDates
        )
    }

    fun getFirstDayInCalenderOfYearMonth(year:Int,month:Int,firstDayOfWeek:Int,locale:Locale): Date {
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.firstDayOfWeek = firstDayOfWeek
        calendar.time = Utils.getDate(year, month+1, 1, locale)
        calendar.set(Calendar.DAY_OF_WEEK,calendar.firstDayOfWeek)
        return calendar.time
    }

    fun getLastDayInCalenderOfYearMonth(year:Int,month:Int,firstDayOfWeek:Int,locale:Locale): Date
    {
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.firstDayOfWeek = firstDayOfWeek
        calendar.time = Utils.getDate(year, month+1, 1, locale)
        val numberDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.time = Utils.getDate(year, month+1, numberDaysOfMonth, locale)
        val lastDayOfWeek= if(calendar.firstDayOfWeek == Calendar.SUNDAY ) Calendar.SATURDAY else Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK,lastDayOfWeek)
        return calendar.time
    }
}

data class WeeksOfYear(var year:Int,var weeksCount:Int,var weeksNumbers:ArrayList<Int>,var startDates:ArrayList<Day>,var endDates:ArrayList<Day>)
data class WeeksOfMonth(var year:Int,val month:Int,var weeksCount:Int,var weeksNumbers:ArrayList<Int>,var startDates:ArrayList<Day>,var endDates:ArrayList<Day>)