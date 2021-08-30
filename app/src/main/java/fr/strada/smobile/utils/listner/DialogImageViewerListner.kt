package fr.strada.smobile.utils.listner

import android.app.Dialog
import androidx.appcompat.widget.AppCompatImageView

interface DialogImageViewerListner {

    fun onShowListner(imageview:AppCompatImageView)

    fun onClickDialogListner(dialog: Dialog)
}