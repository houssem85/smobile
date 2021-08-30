package fr.strada.smobile.di.reglage

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.shawnlin.numberpicker.NumberPicker
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R
import fr.strada.smobile.utils.listner.DialogSynchroListener
import fr.strada.smobile.utils.listner.DialogTempsNotificationsPointeuseListner

@Module
class ReglageModule {
    @Provides
    fun provideDialogSeDeconnecter(context: Context, dialogTempsNotificationsPointeuseListner: DialogTempsNotificationsPointeuseListner): Dialog
    {
        val dataHours = arrayOf("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")
        val dataMinutes = arrayOf("00", "15", "30", "45")
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_temps_notifications_pointeuse, null)
        val btnAnnuler = dialogView.findViewById<AppCompatButton>(R.id.btnAnnuler)
        val btnTerminer = dialogView.findViewById<AppCompatButton>(R.id.btnTerminer)
        val hoursPicker = dialogView.findViewById<NumberPicker>(R.id.hoursPicker)
        hoursPicker.minValue = 0
        hoursPicker.maxValue = dataHours.size - 1
        hoursPicker.displayedValues = dataHours
        hoursPicker.value = 10
        val minutesPicker = dialogView.findViewById<NumberPicker>(R.id.minutesPicker)
        minutesPicker.minValue = 0
        minutesPicker.maxValue = dataMinutes.size - 1
        minutesPicker.displayedValues = dataMinutes
        minutesPicker.value = 0
        dialogBuilder.setView(dialogView)
        val dialog= dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnAnnuler.setOnClickListener {
            dialogTempsNotificationsPointeuseListner.onClickBtnAnnuler(dialog)
        }
        btnTerminer.setOnClickListener {
            dialogTempsNotificationsPointeuseListner.onClickBtnTerminer(dialog,hoursPicker,minutesPicker,dataHours,dataMinutes)
        }
        dialog.setOnShowListener {
            dialogTempsNotificationsPointeuseListner.onShowDialogListner(dialog,hoursPicker,minutesPicker,dataHours,dataMinutes)
        }
        dialog.setOnDismissListener {
            dialogTempsNotificationsPointeuseListner.onDismissDialogListner(dialog)
        }
        return dialog
    }
    private fun initDialogSyncro(context: Context, dialogsynchroListener: DialogSynchroListener) : Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_synchronise_firebase, null)
        val btnActiver = dialogView.findViewById<Button>(R.id.btnActiver)
        val btnAnnuler = dialogView.findViewById<Button>(R.id.btnAnnuler)
        dialogBuilder.setView(dialogView)

        val dialogSynchronize= dialogBuilder.create()
        dialogSynchronize.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSynchronize.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnActiver.setOnClickListener {
            dialogsynchroListener.onClickBtnConfirmer(dialogSynchronize)
        }
        btnAnnuler.setOnClickListener {
            dialogsynchroListener.onClickBtnAnnulerSynchro(dialogSynchronize)
        }
        return dialogSynchronize
    }
}