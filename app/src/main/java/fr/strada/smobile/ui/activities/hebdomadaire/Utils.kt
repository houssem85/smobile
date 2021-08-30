package fr.strada.smobile.ui.activities.hebdomadaire

import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.ui.activities.mensuel.Utils.getWeeksNumbersInMonth
import java.util.*

object Utils {

    fun getWeekOfMonth(year:Int,month:Int,weekOfYear:Int,locale: Locale,fistDayOfWeek:Int):Int
    {
        var index = 0
        val weeks = getWeeksNumbersInMonth(month, year, locale,fistDayOfWeek)
        for (i in 0 until weeks.size)
        {
            if(weeks[i] == weekOfYear){
                index = i
            }
        }
        return index
    }

    fun getDisplayedYears(minYear:Int,maxYear:Int):Array<String>
    {
        val displayedValues = Array((maxYear-minYear)+1){"it = $it"}
        var index = 0
        for (i in minYear .. maxYear){
            displayedValues[index] = i.toString()
            index++
        }
        return displayedValues
    }


    fun getDayOfWeek(year:Int,week:Int,index:Int,firstDayOfWeek:Int,locale:Locale):Day
    {
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.firstDayOfWeek = firstDayOfWeek
        calendar.set(Calendar.WEEK_OF_YEAR, week)
        calendar.set(Calendar.YEAR, year)
        calendar.add(Calendar.DAY_OF_MONTH,index)
        return Day(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
    }
    
    fun getRangeOfDates(startDate:Date,endDate:Date,firstDayOfWeek:Int,locale:Locale):List<Date>
    {
        val result = ArrayList<Date>()
        val calendar = Calendar.getInstance(locale)
        calendar.firstDayOfWeek = firstDayOfWeek
        calendar.time = startDate
        var index = startDate
        while (index <= endDate) {
            result.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
            index = calendar.time
        }
        return result
    }
}