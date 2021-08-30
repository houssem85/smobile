package fr.strada.smobile.ui.activities.mensuel

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName

@BindingAdapter("month")
fun bindMonth(appCompatTextView: AppCompatTextView, month: MutableLiveData<Int>) {
    month.value?.let {
        appCompatTextView.text = getMonthName(appCompatTextView.context,it)
    }
}


