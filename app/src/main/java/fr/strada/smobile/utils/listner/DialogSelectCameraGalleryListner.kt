package fr.strada.smobile.utils.listner

import android.app.Dialog
import com.google.android.material.bottomsheet.BottomSheetDialog

interface DialogSelectCameraGalleryListner {

    fun onClickAnnulerListner(dialog: BottomSheetDialog)

    fun onClickGalleryListner(dialog: BottomSheetDialog)

    fun onClickCameraListner(dialog: BottomSheetDialog)

    fun onShowListner(dialog: Dialog)

    fun onDismissListner()
}