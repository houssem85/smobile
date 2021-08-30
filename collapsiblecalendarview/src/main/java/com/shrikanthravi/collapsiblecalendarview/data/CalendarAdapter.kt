package com.shrikanthravi.collapsiblecalendarview.data

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompatSideChannelService
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackground

import com.shrikanthravi.collapsiblecalendarview.R
import kotlinx.android.synthetic.main.day_layout.view.*
import java.util.*

/**
 * Created by shrikanthravi on 06/03/18.
 */

class CalendarAdapter(val context: Context, cal: Calendar) {
    private var mFirstDayOfWeek = 0
    var calendar: Calendar = cal.clone() as Calendar
    private val mInflater: LayoutInflater

    private val mItemList = ArrayList<Day>()
    private val mViewList = ArrayList<View>()
    var mEventList = ArrayList<Event>()
    var mEmployeeList = ArrayList<Employee>()
    var mIntervalList = ArrayList<Interval>()

    var mIndicatorList = ArrayList<Event>()

    // public methods
    val count: Int
        get() = mItemList.size

    init {
        this.calendar.set(Calendar.DAY_OF_MONTH, 1)

        mInflater = LayoutInflater.from(context)

        refresh()
    }

    fun getItem(position: Int): Day {
        return mItemList[position]
    }

    fun getView(position: Int): View {
        return mViewList[position]
    }

    fun setFirstDayOfWeek(firstDayOfWeek: Int) {
        mFirstDayOfWeek = firstDayOfWeek
    }

    fun addEvent(event: Event) {
        mEventList.add(event)
    }

    fun addIndicator(event: Event)
    {
        mIndicatorList.add(event)
    }

    fun setIndicators(indicators: List<Event>)
    {
        mIndicatorList.clear()
        mIndicatorList.addAll(indicators)
    }

    fun addEmployee(employee: Employee){
        mEmployeeList.add(employee)
    }

    fun addInterval(interval: Interval){
        mIntervalList.add(interval)
    }

    fun addListEvent(event: List<Event>) {
        mEventList.addAll(event)
    }

    fun addListEmployee(employee: List<Employee>){
        mEmployeeList.addAll(employee)
    }

    fun addListIntervals(intervals: List<Interval>){
        mIntervalList.addAll(intervals)
    }


    fun refresh() {
        // clear data
        mItemList.clear()
        mViewList.clear()

        // set calendar
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        calendar.set(year, month, 1)

        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if(firstDayOfWeek == 0)
        {
            firstDayOfWeek = 7
        }
        // generate day list
        val offset = 0 - (firstDayOfWeek - mFirstDayOfWeek) + 1
        val length = Math.ceil(((lastDayOfMonth - offset + 1).toFloat() / 7).toDouble()).toInt() * 7
        for (i in offset until length + offset) {
            val numYear: Int
            val numMonth: Int
            val numDay: Int

            val tempCal = Calendar.getInstance()
            if (i <= 0) { // prev month
                if (month == 0) {
                    numYear = year - 1
                    numMonth = 11
                } else {
                    numYear = year
                    numMonth = month - 1
                }
                tempCal.set(numYear, numMonth, 1)
                numDay = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH) + i
            } else if (i > lastDayOfMonth) { // next month
                if (month == 11) {
                    numYear = year + 1
                    numMonth = 0
                } else {
                    numYear = year
                    numMonth = month + 1
                }
                tempCal.set(numYear, numMonth, 1)
                numDay = i - lastDayOfMonth
            } else {
                numYear = year
                numMonth = month
                numDay = i
            }

            val day = Day(numYear, numMonth, numDay)

            val view = mInflater.inflate(R.layout.day_layout, null)
            val txtDay = view.findViewById<View>(R.id.txt_day) as TextView
            val imgIndictorTag = view.findViewById<View>(R.id.img_event_tag) as ImageView
            val imgEmployeePresent = view.findViewById<View>(R.id.img_employee_present) as ImageView
            val imgEmployeeAbsent = view.findViewById<View>(R.id.img_employee_absent) as ImageView
            val imgRequestAbsence = view.findViewById<View>(R.id.img_request_absence) as ImageView
            val txtNbrEmployeePresent = view.findViewById<View>(R.id.txt_nb_sp) as TextView
            txtDay.text = day.day.toString()

            if (day.month != calendar.get(Calendar.MONTH)) {
                txtDay.alpha = 0.3f
            }

            for (j in mEventList.indices) {
                val event = mEventList[j]
                if (day.year == event.year && day.month == event.month && day.day == event.day) {
                    view.background = ContextCompat.getDrawable(context, event.color)
                  //  txtDay.setTextColor(ContextCompat.getColor(context, event.color))
                }
            }

            // indicators

            for (j in mIndicatorList.indices) {
                val indicator = mIndicatorList[j]
                if (day.year == indicator.year && day.month == indicator.month && day.day == indicator.day) {
                    imgIndictorTag.visibility  = VISIBLE
                }
            }
            ///

            for (i in mEmployeeList.indices){
                val employee = mEmployeeList[i]
                if (day.year == employee.year && day.month == employee.month && day.day == employee.day){

                    if (employee.nbEmployeeAbsent > 0){
                        imgEmployeeAbsent.visibility = View.VISIBLE
                    }
                    else{
                        imgEmployeeAbsent.visibility = View.INVISIBLE
                    }
                    if(employee.nbEmployeePresent > 0){
                        imgEmployeePresent.visibility = View.VISIBLE
                        txtNbrEmployeePresent.visibility = View.VISIBLE
                        txtNbrEmployeePresent.text = employee.nbEmployeePresent.toString()
                    }
                    else
                    {
                        imgEmployeePresent.visibility = View.INVISIBLE
                        txtNbrEmployeePresent.visibility = View.INVISIBLE
                    }

                    if(employee.nbRequestAbsence>0){
                        imgRequestAbsence.visibility = View.VISIBLE
                    }
                    else{
                        imgRequestAbsence.visibility = View.INVISIBLE
                    }
                }
            }

            for(h in mIntervalList.indices){
               val interval =  mIntervalList[h]
               val locale= getCurrentLocal(context)
               val startDate = getDate(interval.start.year,interval.start.month,interval.start.day,locale)
               val endDate = getDate(interval.end.year,interval.end.month,interval.end.day,locale)
               val curentDay =  getDate(day.year,day.month,day.day,locale)
               if(curentDay.after(startDate) && curentDay.before(endDate))
               {
                   if(interval.typeConge == TypeConge.DEMMANDE)
                   {
                       view.background = ContextCompat.getDrawable(context,R.drawable.bg_middle_day_interval_conge_demmande)
                   }else{
                       view.background = ContextCompat.getDrawable(context,R.drawable.bg_middle_day_interval_conge_pris)
                   }

               }else if(curentDay == startDate)
               {
                   if(interval.typeConge == TypeConge.DEMMANDE)
                   {
                       view.background = ContextCompat.getDrawable(context,R.drawable.bg_first_day_interval_conge_demmande)
                   }else
                   {
                       view.background = ContextCompat.getDrawable(context,R.drawable.bg_first_day_interval_conge_pris)
                   }

               }else if(curentDay == endDate)
               {
                   if(interval.typeConge == TypeConge.DEMMANDE)
                   {
                       view.background = ContextCompat.getDrawable(context,R.drawable.bg_last_day_interval_conge_demmande)
                   }else
                   {
                       view.background = ContextCompat.getDrawable(context,R.drawable.bg_last_day_interval_conge_pris)
                   }
               }
            }
            mItemList.add(day)
            mViewList.add(view)
        }
    }

    fun getDate(year: Int,month:Int,day:Int,locale: Locale): Date {
        val calendar = Calendar.getInstance(locale)
        calendar.clear()
        calendar.set(year,month,day)
        return calendar.time
    }

    fun getCurrentLocal(context: Context):Locale{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            context.resources.configuration.locales.get(0)
        } else{
            context.resources.configuration.locale
        }
    }
}
