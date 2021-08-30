package fr.strada.smobile.utils.listner

import android.app.Dialog

interface DialogEtesVousSurDeVouloirQuitterListner {
    fun onClickOuiListner(dialog: Dialog)
    fun onCLickNonListner(dialog: Dialog)
}