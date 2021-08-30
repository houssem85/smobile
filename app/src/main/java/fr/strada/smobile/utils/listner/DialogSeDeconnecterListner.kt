package fr.strada.smobile.utils.listner

import android.app.Dialog

interface DialogSeDeconnecterListner {
    fun onClickAnnulerListner(dialog: Dialog)
    fun onClickDeconnexionListner(dialog: Dialog)
}