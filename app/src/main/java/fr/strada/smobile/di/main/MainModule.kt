package fr.strada.smobile.di.main

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R
import fr.strada.smobile.ui.main.TypeActivitiePointeuseAdapter
import fr.strada.smobile.utils.listner.OnClickListener
import javax.inject.Named

@Module
class MainModule {

    @Named(DIALOG_LOADING)
    @Provides
    fun provideDialogLoading(context: Activity):Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loader_empty_msg, null)
        dialogBuilder.setView(dialogView)
        val dialogLoading = dialogBuilder.create()
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setCancelable(false)
        return dialogLoading
    }

    @Provides
    fun provideTypeActivitiePointeuseAdapter(context: Activity,onClickListener: OnClickListener?): TypeActivitiePointeuseAdapter {
        return TypeActivitiePointeuseAdapter(context,onClickListener)
    }

    @Provides
    fun provideLinearLayoutManager(context: Activity): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }
}