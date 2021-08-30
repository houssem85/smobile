package fr.strada.smobile.ui_tablette.accueil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import javax.inject.Inject

class AccueilTabletteViewModel @Inject constructor(application: Application): AndroidViewModel(application) {
  var isMesActivitesFragmentActive = false
}