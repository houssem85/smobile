package fr.strada.smobile.di.infractions.detail_infraction

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R
import fr.strada.smobile.utils.listner.DialogEtesVousSurDeVouloirQuitterListner
import fr.strada.smobile.utils.listner.DialogImageViewerListner
import fr.strada.smobile.utils.listner.DialogSelectCameraGalleryListner
import javax.inject.Named

@Module
class DetailInfractionModule {

    @Provides
    fun provideDialogSelectCameraGallery(context: Context, dialogSelectCameraGalleryListner: DialogSelectCameraGalleryListner?): BottomSheetDialog
    {
        val sheetBottomDialog = BottomSheetDialog(context, R.style.AppBottomSheetDialogThemeTransparent)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_camera_callery,null)
        val btnAnnuler=dialogView.findViewById<AppCompatTextView>(R.id.btnAnnuler)
        val btnCamera=dialogView.findViewById<AppCompatTextView>(R.id.btnCamera)
        val btnCalerie=dialogView.findViewById<AppCompatTextView>(R.id.btnCalerie)
        sheetBottomDialog.setContentView(dialogView)
        btnAnnuler.setOnClickListener {
            dialogSelectCameraGalleryListner?.onClickAnnulerListner(sheetBottomDialog)
        }
        btnCamera.setOnClickListener {
            dialogSelectCameraGalleryListner?.onClickCameraListner(sheetBottomDialog)
        }
        btnCalerie.setOnClickListener {
            dialogSelectCameraGalleryListner?.onClickGalleryListner(sheetBottomDialog)
        }
        return sheetBottomDialog
    }

    @Named(DIALOG_IMAGE_VIEWER)
    @Provides
    fun provideDialogImageViewer(context: Context,dialogImageViewerListner: DialogImageViewerListner?): Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image_viewer, null)
        val imageView = dialogView.findViewById<AppCompatImageView>(R.id.imageView)
        dialogBuilder.setView(dialogView)
        val dialog= dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setOnClickListener {
            dialogImageViewerListner?.onClickDialogListner(dialog)
        }
        dialog.setOnShowListener {
            dialogImageViewerListner?.onShowListner(imageView)
        }
        return dialog
    }

    @Named(DIALOG_ETES_VOUS_SUR_DE_VOULOIR_QUITTER)
    @Provides
    fun provideDialogEtesVousSurDeVouloirQuiter(context: Context,dialogEtesVousSurDeVouloirQuitterListner: DialogEtesVousSurDeVouloirQuitterListner?): Dialog
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_etes_vous_sur_de_vouloir_quitter, null)
        val btnOui = dialogView.findViewById<AppCompatButton>(R.id.btnOui)
        val btnNon = dialogView.findViewById<AppCompatButton>(R.id.btnNon)
        dialogBuilder.setView(dialogView)
        val dialog= dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnOui.setOnClickListener {
            dialogEtesVousSurDeVouloirQuitterListner?.onClickOuiListner(dialog)
        }
        btnNon.setOnClickListener {
            dialogEtesVousSurDeVouloirQuitterListner?.onCLickNonListner(dialog)
        }
        return dialog
    }
}