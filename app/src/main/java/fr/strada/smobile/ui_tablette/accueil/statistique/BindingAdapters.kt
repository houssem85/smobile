package fr.strada.smobile.ui_tablette.accueil.statistique

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("currentDate")
fun setCurrentDate(lbl: AppCompatTextView,date:MutableLiveData<Date>) {
    date.value?.let {
        val currentLocal = lbl.context.resources.configuration.locale
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", currentLocal)
        val dateFormatDay = SimpleDateFormat("EEEE", currentLocal)
        val dateFormatDayNumber = SimpleDateFormat("dd", currentLocal)
        val dateFormatMonth = SimpleDateFormat("MMMM", currentLocal)
        val dateFormatYear = SimpleDateFormat("yyyy", currentLocal)
        val day = dateFormatDay.format(it).capitalize(Locale.ROOT)
        val month = dateFormatMonth.format(it).capitalize(Locale.ROOT)
        val dayNumber = dateFormatDayNumber.format(it).capitalize(Locale.ROOT)
        val year = dateFormatYear.format(it).capitalize(Locale.ROOT)
        lbl.text = "$day ,$dayNumber $month $year"
    }
}
