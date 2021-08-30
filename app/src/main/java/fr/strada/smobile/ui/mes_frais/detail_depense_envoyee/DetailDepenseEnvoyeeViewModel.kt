package fr.strada.smobile.ui.mes_frais.detail_depense_envoyee

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import fr.strada.smobile.data.models.mes_frais.Depense
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class DetailDepenseEnvoyeeViewModel @Inject constructor(application: Application):AndroidViewModel(application){

    val detaildepenseenvoyer = MutableStateFlow<Depense?>(null)

}