package fr.strada.smobile.utils.listner

import android.app.Dialog
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

interface DialogSelectDateListner {

    fun onClickTerminerListner(dialog:Dialog,datePicker: MaterialCalendarView)

    fun onClickAnnulerlistner(dialog:Dialog,datePicker: MaterialCalendarView)

    fun onShowListner(dialog:Dialog,datePicker: MaterialCalendarView)
}