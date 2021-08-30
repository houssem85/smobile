package fr.strada.smobile.ui_tablette.mes_absences_tablette.solde_absence_tablette

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName

@BindingAdapter("month_tablette")
fun bindMonth(appCompatTextView: AppCompatTextView, month: MutableLiveData<Int>) {
    month.value?.let {
        appCompatTextView.text = getMonthName(appCompatTextView.context,it)
    }
}