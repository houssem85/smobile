package fr.strada.smobile.utils.listner

import android.app.Dialog

interface DialogVousVoulezEnregistrerVotreNouvelleDepenseAvantDeQuitter {
    fun onClickOuiListner(dialog: Dialog)
    fun onClickNonListner(dialog: Dialog)
}