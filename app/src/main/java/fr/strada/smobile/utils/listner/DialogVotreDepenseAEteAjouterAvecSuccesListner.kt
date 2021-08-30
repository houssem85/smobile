package fr.strada.smobile.utils.listner

import android.app.Dialog

interface DialogVotreDepenseAEteAjouterAvecSuccesListner {

    fun onClickBtnAjouterUneAutreDepenseLisnter(dialog: Dialog)

    fun onClickBtnTerminerLisnter(dialog: Dialog)

    fun onShowLisnter(dialog: Dialog)

    fun onDismissLisnter(dialog: Dialog)

}