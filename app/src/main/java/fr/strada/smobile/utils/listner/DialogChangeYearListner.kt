package fr.strada.smobile.utils.listner

import android.app.Dialog
import com.shawnlin.numberpicker.NumberPicker

interface DialogChangeYearListner {

    fun onShowListner(dialog: Dialog, yearPicker: NumberPicker)

    fun onClickTerminerListner(dialog: Dialog, yearPicker: NumberPicker)

    fun onClickAnnulerListner(dialog: Dialog)

    fun onDismissListner()

}