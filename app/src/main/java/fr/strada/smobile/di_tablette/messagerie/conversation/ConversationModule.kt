package fr.strada.smobile.di_tablette.messagerie.conversation

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui_tablette.messagerie.conversation.ConversationAdapter

@Module
class ConversationModule {

    @Provides
    fun provideConversationAdapter(context: Context):ConversationAdapter{
       return ConversationAdapter(context)
    }

    @Provides
    fun provideLayoutManager(context: Context): RecyclerView.LayoutManager {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        return layoutManager
    }

}