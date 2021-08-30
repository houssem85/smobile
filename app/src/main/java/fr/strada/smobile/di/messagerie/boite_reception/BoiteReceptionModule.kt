package fr.strada.smobile.di.messagerie.boite_reception

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import fr.strada.smobile.ui.messagerie.boite_de_reception.BoiteReceptionAdapter
import fr.strada.smobile.utils.listner.ItemBoiteReceptionListener

@Module
class BoiteReceptionModule {

    @Provides
    fun provideBoiteReceptionAdapter(activity: Activity, itemBoiteReceptionListener: ItemBoiteReceptionListener): BoiteReceptionAdapter {
        return  BoiteReceptionAdapter(activity, itemBoiteReceptionListener)
    }

    @Provides
    fun provideLinearLayout(activity: Activity): LinearLayoutManager {
        return  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

}