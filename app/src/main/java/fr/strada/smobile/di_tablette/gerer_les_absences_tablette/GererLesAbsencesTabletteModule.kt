package fr.strada.smobile.di_tablette.gerer_les_absences_tablette

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.Switch
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R

@Module
class GererLesAbsencesTabletteModule  {

    @Provides
    fun provideDialogFilterGererLesAbsencesTablette(context: Context): Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_filtre_gerer_les_absences, null)
        val switchAlpha = dialogView.findViewById<Switch>(R.id.switch_alpha)
        val btnAZ = dialogView.findViewById<AppCompatButton>(R.id.btn_a_z)
        val btnZA = dialogView.findViewById<AppCompatButton>(R.id.btn_z_a)
        dialogBuilder.setView(dialogView)
        // logic of dialog
        btnAZ.setOnClickListener {
            if(switchAlpha.isChecked){
                btnAZ.background = ContextCompat.getDrawable(context, R.drawable.bg_btn_filter_blue)
                btnZA.background = ContextCompat.getDrawable(context, R.drawable.bg_btn_solde_white)
                btnZA.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                btnAZ.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            }
        }

        btnZA.setOnClickListener {
            if(switchAlpha.isChecked){
                btnAZ.background = ContextCompat.getDrawable(context, R.drawable.bg_btn_solde_white)
                btnZA.background = ContextCompat.getDrawable(context, R.drawable.bg_widget_blue)
                btnZA.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                btnAZ.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
        }

        switchAlpha.setOnCheckedChangeListener{_, isChecked->
           if(isChecked)
           {
               btnAZ.background = ContextCompat.getDrawable(context, R.drawable.bg_btn_filter_blue)
               btnZA.background = ContextCompat.getDrawable(context, R.drawable.bg_btn_solde_white)
               btnZA.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
               btnAZ.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
               btnAZ.performClick()
           }else
           {
               btnAZ.background = ContextCompat.getDrawable(context, R.drawable.bg_btn_filter_gray)
               btnZA.background = ContextCompat.getDrawable(context, R.drawable.bg_btn_filter_gray)
               btnZA.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
               btnAZ.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
           }
        }

        val dialog = dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}