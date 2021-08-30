package fr.strada.smobile.utils.listner

import android.app.Dialog
import com.shawnlin.numberpicker.NumberPicker

interface DialogChangeWeekListner {

    fun onShowListner(dialog: Dialog,weekPicker: NumberPicker)

    fun onDismissListner(dialog: Dialog)

    fun onClickTerminerListner(dialog: Dialog,weekPicker: NumberPicker)

    fun onClickAnnulerListner(dialog: Dialog)

}