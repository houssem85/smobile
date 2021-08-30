package fr.strada.smobile.ui.activities.journalier

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName

@BindingAdapter("currentDay")
fun bindPathImage(txt: AppCompatTextView, currentDay: MutableLiveData<Day>) {
    currentDay.value?.let {
        val strMonth = getMonthName(txt.context,it.month)
        val strDate="${it.day} $strMonth ${it.year}"
        txt.text = strDate
    }
}