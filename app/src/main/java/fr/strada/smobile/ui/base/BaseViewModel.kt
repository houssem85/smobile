package fr.strada.smobile.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fr.strada.smobile.ui.activities.Utils
import java.util.*

open class BaseViewModel(application: Application) : AndroidViewModel(application){
    protected var locale : Locale = Utils.getCurrentLocal(application)
}