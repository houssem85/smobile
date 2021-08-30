package fr.strada.smobile.ui.demande_absence

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CurrentDayDecorator (context: Activity?, currentDay: CalendarDay) : DayViewDecorator {
    private val drawable: Drawable? = ContextCompat.getDrawable(context!!, fr.strada.smobile.R.drawable.bg_selected_date)
    var myDay = currentDay
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
        view.addSpan( ForegroundColorSpan(Color.WHITE))
    }
}

class TodayDecorator (context: Activity?, currentDay: CalendarDay) : DayViewDecorator {

    private val drawable: Drawable? = ContextCompat.getDrawable(context!!, fr.strada.smobile.R.drawable.bg_circle_with_strock)
    var myDay = currentDay
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(drawable!!)
    }
}