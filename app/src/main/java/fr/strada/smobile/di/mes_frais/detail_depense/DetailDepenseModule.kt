package fr.strada.smobile.di.mes_frais.detail_depense

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R
import fr.strada.smobile.ui.demande_absence.TodayDecorator
import fr.strada.smobile.utils.listner.DialogSelectDateListner
import fr.strada.smobile.utils.listner.DialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter
import javax.inject.Named

const val DIALOG_SELECT_DATE = "DIALOG_SELECT_DATE"
const val DIALOG_VOUS_VOULEZ_ENREGISTRER_VOTRE_NOUVELLE_DEPENSE_AVANT_DE_QUITER = "DIALOG_VOUS_VOULEZ_ENREGISTRER_VOTRE_NOUVELLE_DEPENSE_AVANT_DE_QUITER"

@Module
class DetailDepenseModule {

    @Named(DIALOG_SELECT_DATE)
    @Provides
    fun provideDialogSelectDate(context: Activity, dialogSelectDateListner: DialogSelectDateListner) : Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_date, null)
        val datePicker = dialogView.findViewById<MaterialCalendarView>(R.id.datePicker)
        val btnTerminer = dialogView.findViewById<Button>(R.id.btnTerminer)
        val btnAnnuler = dialogView.findViewById<Button>(R.id.btnAnnuler)
        val currentDate= CalendarDay.today()
        dialogBuilder.setView(dialogView)
        datePicker.selectedDate = currentDate
        datePicker.addDecorators(TodayDecorator(context, currentDate))
        val dialogDate = dialogBuilder.create()
        dialogDate.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogDate.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogDate.setOnShowListener {
            dialogSelectDateListner.onShowListner(dialogDate,datePicker)
        }
        btnTerminer.setOnClickListener {
            dialogSelectDateListner.onClickTerminerListner(dialogDate,datePicker)
        }
        btnAnnuler.setOnClickListener {
            dialogSelectDateListner.onClickAnnulerlistner(dialogDate,datePicker)
        }
        return dialogDate
    }

    @Named(DIALOG_VOUS_VOULEZ_ENREGISTRER_VOTRE_NOUVELLE_DEPENSE_AVANT_DE_QUITER)
    @Provides
    fun provideDialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter(context: Activity, dialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter: DialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter) : Dialog{
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_vous_voulez_enregistrer_votre_nouvelle_depense_avant_de_quitter, null)
        val btnOui = dialogView.findViewById<AppCompatButton>(R.id.btnOui)
        val btnNon = dialogView.findViewById<AppCompatButton>(R.id.btnNon)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnOui.setOnClickListener {
            dialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter.onClickOuiListner(dialog)
        }
        btnNon.setOnClickListener {
            dialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter.onClickNonListner(dialog)
        }
        return dialog
    }

}