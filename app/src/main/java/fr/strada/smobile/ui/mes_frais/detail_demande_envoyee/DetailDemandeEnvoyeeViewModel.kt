package fr.strada.smobile.ui.mes_frais.detail_demande_envoyee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class DetailDemandeEnvoyeeViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    val detailNoteFrais = MutableStateFlow<NoteFrais?>(null)
}