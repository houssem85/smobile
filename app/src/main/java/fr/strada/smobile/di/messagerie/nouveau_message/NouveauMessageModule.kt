package fr.strada.smobile.di.messagerie.nouveau_message

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
import fr.strada.smobile.ui.messagerie.nouveau_message.DestinataireAdapter
import fr.strada.smobile.utils.listner.ItemDestinataireListner

@Module
class NouveauMessageModule {

    @Provides
    fun provideDialogCherchezVotreDestinataire(context: Activity,destinataireAdapter:DestinataireAdapter,linearLayoutManager:LinearLayoutManager): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_cherchez_votre_destinataire, null)
        val recycleView = dialogView.findViewById<RecyclerView>(R.id.recycleView)
        recycleView.layoutManager = linearLayoutManager
        recycleView.adapter = destinataireAdapter
        dialogBuilder.setView(dialogView)
        val dialogLoading = dialogBuilder.create()
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialogLoading
    }

    @Provides
    fun provideDestinataireAdapter(context: Activity,itemDestinataireListner: ItemDestinataireListner?): DestinataireAdapter {
        return  DestinataireAdapter(context,itemDestinataireListner)
    }

    @Provides
    fun provideLinearLayoutManager(context: Activity): LinearLayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}