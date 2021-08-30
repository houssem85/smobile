package fr.strada.smobile.di.indemnites

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.shawnlin.numberpicker.NumberPicker
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_MONTH
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_WEEK
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_YEAR
import fr.strada.smobile.di.mes_activities.DIALOG_LOADER
import fr.strada.smobile.ui.activities.hebdomadaire.Utils
import fr.strada.smobile.utils.listner.DialogChangeMonthListner
import fr.strada.smobile.utils.listner.DialogChangeWeekListner
import fr.strada.smobile.utils.listner.DialogChangeYearListner
import javax.inject.Named

@Module
class IndemnityModule {

    @Provides
    fun provideLayoutManager(application: Application): LinearLayoutManager
    {
        return LinearLayoutManager(application, LinearLayoutManager.VERTICAL,false)
    }

    @Named(DIALOG_CHANGE_MONTH)
    @Provides
    fun provideDialogChangeMonth(context: Context, dialogChangeMonthListner: DialogChangeMonthListner?): Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_change_month, null)
        val btnValidate = dialogView.findViewById<AppCompatButton>(R.id.btnDone)
        val btnCancel = dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
        val txtDate = dialogView.findViewById<TextView>(R.id.txt_date)
        val picker = dialogView.findViewById(R.id.date_picker) as fr.strada.smobile.utils.DatePicker
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
        dialog.setOnDismissListener {
            dialogChangeMonthListner?.onDismissListner()
        }
        return dialog
    }

    @Named(DIALOG_CHANGE_WEEK)
    @Provides
    fun provideDialogChangeWeek(context: Context, dialogChangeWeekListner: DialogChangeWeekListner?): Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_change_week, null)
        val btnTerminer = dialogView.findViewById<AppCompatButton>(R.id.btnTerminer)
        val btnAnnuler = dialogView.findViewById<AppCompatButton>(R.id.btnAnnuler)
        val weekPicker = dialogView.findViewById<NumberPicker>(R.id.weekPicker)
        dialogBuilder.setView(dialogView)
        val dialog= dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnTerminer.setOnClickListener {
            dialogChangeWeekListner?.onClickTerminerListner(dialog,weekPicker)
        }
        btnAnnuler.setOnClickListener {
            dialogChangeWeekListner?.onClickAnnulerListner(dialog)
        }
        dialog.setOnShowListener {
            dialogChangeWeekListner?.onShowListner(dialog,weekPicker)
        }
        dialog.setOnDismissListener {
            dialogChangeWeekListner?.onDismissListner(dialog)
        }
        return dialog
    }

    @Named(DIALOG_CHANGE_YEAR)
    @Provides
    fun provideDialogChangeYear(context: Context, dialogChangeYearListner: DialogChangeYearListner?): Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_change_year, null)
        val btnTerminer = dialogView.findViewById<AppCompatButton>(R.id.btnTerminer)
        val btnAnnuler = dialogView.findViewById<AppCompatButton>(R.id.btnAnnuler)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        yearPicker.minValue = 1999
        yearPicker.maxValue = 2050
        yearPicker.displayedValues =
            Utils.getDisplayedYears(yearPicker.minValue, yearPicker.maxValue)
        dialogBuilder.setView(dialogView)
        val dialog= dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnTerminer.setOnClickListener {
            dialogChangeYearListner?.onClickTerminerListner(dialog,yearPicker)
        }
        btnAnnuler.setOnClickListener {
            dialogChangeYearListner?.onClickAnnulerListner(dialog)
        }
        dialog.setOnShowListener {
            dialogChangeYearListner?.onShowListner(dialog,yearPicker)
        }
        dialog.setOnDismissListener {
            dialogChangeYearListner?.onDismissListner()
        }
        return dialog
    }

    @Named(DIALOG_LOADER)
    @Provides
    fun provideDialogLoader(context: Context): Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loader_empty_msg,null)
        dialogBuilder.setView(dialogView)
        val dialog= dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        return dialog
    }

}