package fr.strada.smobile.utils.listner

import android.app.Dialog
import fr.strada.smobile.databinding.DialogEnvoieDemandeNonEnvoyeeBinding

interface DialogEnvoieEmailListner {

    fun onClickEnvoyerListner(dialog: Dialog,email:String)

    fun onClickAnnulerListner(dialog: Dialog)

    fun onShowListner(dialog: Dialog,binding: DialogEnvoieDemandeNonEnvoyeeBinding)

    fun onDismissListner(dialog: Dialog)
}