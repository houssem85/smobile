package fr.strada.smobile.di.mes_frais.detail_demande_non_envoyee

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.DepenseAdapter
import fr.strada.smobile.utils.listner.DialogEnvoieEmailListner
import fr.strada.smobile.utils.listner.DialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter
import fr.strada.smobile.utils.listner.ItemDepenseListner
import javax.inject.Named

const val DIALOG_VOUS_VOULEZ_ENREGISTRER_VOTRE_NOUVELLE_DEMANDE_AVANT_DE_QUITER = "DIALOG_VOUS_VOULEZ_ENREGISTRER_VOTRE_NOUVELLE_DEMANDE_AVANT_DE_QUITER"
const val DIALOG_ENVOIE_DEMANDE = "DIALOG_ENVOIE_DEMANDE"

@Module
class DetailDemandeNonEnvoyeeModule {

    @Provides
    fun provideDepenseAdapter(context:Activity, itemDepenseListner: ItemDepenseListner): DepenseAdapter {
        return DepenseAdapter(context, itemDepenseListner)
    }

    @Provides
    fun provideLayoutManager(application: Application): LinearLayoutManager {
        return LinearLayoutManager(application, LinearLayoutManager.VERTICAL,false)
    }

    @Named(DIALOG_VOUS_VOULEZ_ENREGISTRER_VOTRE_NOUVELLE_DEMANDE_AVANT_DE_QUITER)
    @Provides
    fun provideDialogVousVoulezEnregistrerVotreNouvelleDemandeAvantDeQuitter(context: Activity, dialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter: DialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter) : Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_vous_voulez_enregistrer_votre_nouvelle_demande_avant_de_quitter, null)
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

    @Named(DIALOG_ENVOIE_DEMANDE)
    @Provides
    fun provideDialogEnvoieEmail(context: Activity, dialogEnvoieEmailListner: DialogEnvoieEmailListner) : Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val binding= DataBindingUtil.inflate<fr.strada.smobile.databinding.DialogEnvoieDemandeNonEnvoyeeBinding>(LayoutInflater.from(context), R.layout.dialog_envoie_demande_non_envoyee,null, false)
        val dialogView = binding.root
        val btnEnvoyer = dialogView.findViewById<AppCompatButton>(R.id.btnEnvoyer)
        val btnAnnuler = dialogView.findViewById<AppCompatButton>(R.id.btnAnnuler)
        val txtEmail = dialogView.findViewById<AppCompatEditText>(R.id.txtEmail)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnEnvoyer.setOnClickListener {
            dialogEnvoieEmailListner.onClickEnvoyerListner(dialog,txtEmail.text.toString())
        }
        btnAnnuler.setOnClickListener {
            dialogEnvoieEmailListner.onClickAnnulerListner(dialog)
        }
        dialog.setOnDismissListener {
            dialogEnvoieEmailListner.onDismissListner(dialog)
        }
        dialog.setOnShowListener {
            // dialogEnvoieEmailListner.onShowListner(dialog,binding)
        }
        return dialog
    }
}