package fr.strada.smobile.utils.listner

import android.app.Dialog
import android.widget.TextView
import fr.strada.smobile.utils.DatePicker

interface DialogChangeMonthListner {

    fun onShowListner(dialog:Dialog,txtDate: TextView,datePicker: DatePicker)

    fun onClickValidateListner(dialog:Dialog,datePicker: DatePicker)

    fun onClickCancelListner(dialog:Dialog)

    fun onDismissListner()

}