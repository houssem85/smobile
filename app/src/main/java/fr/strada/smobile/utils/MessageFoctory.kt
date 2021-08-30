package fr.strada.smobile.utils

import android.app.Application
import fr.strada.smobile.R
import javax.inject.Inject

class MessageFoctory @Inject constructor(private val application: Application) {

    fun getMessageNoInternetConnection() = application.getString(R.string.Veuillez_vérifier_votre_connexion_Internet)
}