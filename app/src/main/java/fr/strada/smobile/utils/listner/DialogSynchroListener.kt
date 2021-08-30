package fr.strada.smobile.utils.listner

import android.app.Dialog

interface DialogSynchroListener {
    fun onClickBtnAnnulerSynchro(dialog: Dialog)
    fun onClickBtnConfirmer(dialog:Dialog)
}