package fr.strada.smobile.di.messagerie.message_predefini

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui.messagerie.message_predefini.MessagePredefiniAdapter
import fr.strada.smobile.utils.listner.ItemMessagePredefiniListener

@Module
class MessagePredefiniModule {
    @Provides
    fun provideMessagePredefiniAdapter(context: Context,  itemMessagePredefiniListener: ItemMessagePredefiniListener): MessagePredefiniAdapter {

        return  MessagePredefiniAdapter(context, itemMessagePredefiniListener)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {

          return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}