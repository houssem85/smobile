package fr.strada.smobile.di_tablette.solde_absence_tablette

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R
import fr.strada.smobile.utils.DatePicker
import fr.strada.smobile.utils.listner.DialogChangeMonthListner
import javax.inject.Named

@Module
class SoldeAbsenceTabletteModule  {

    @Named(DIALOG_CHANGE_MONTH)
    @Provides
    fun provideDialogChangeMonth(context: Context, dialogChangeMonthListner: DialogChangeMonthListner?): Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_change_month, null)
        val btnValidate = dialogView.findViewById<AppCompatButton>(R.id.btnDone)
        val btnCancel = dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
        val txtDate = dialogView.findViewById<TextView>(R.id.txt_date)
        val picker = dialogView.findViewById(R.id.date_picker) as DatePicker
        dialogBuilder.setView(dialogView)
        val dialog= dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnValidate.setOnClickListener {
            dialogChangeMonthListner?.onClickValidateListner(dialog,picker)
        }
        btnCancel.setOnClickListener {
            dialogChangeMonthListner?.onClickCancelListner(dialog)
        }
        dialog.setOnShowListener {
            dialogChangeMonthListner?.onShowListner(dialog,txtDate,picker)
        }
        return dialog
    }

}