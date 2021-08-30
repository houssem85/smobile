package fr.strada.smobile.utils.listner

import android.app.Dialog
import com.shawnlin.numberpicker.NumberPicker

interface DialogTempsNotificationsPointeuseListner
{
    fun onClickBtnTerminer(dialog:Dialog, hoursPicker: NumberPicker, minutesPicker: NumberPicker,dataHours:Array<String>,dataMinutes:Array<String>)

    fun onClickBtnAnnuler(dialog:Dialog)

    fun onDismissDialogListner(dialog:Dialog)

    fun onShowDialogListner(dialog:Dialog,hoursPicker: NumberPicker, minutesPicker: NumberPicker,dataHours:Array<String>,dataMinutes:Array<String>)
}