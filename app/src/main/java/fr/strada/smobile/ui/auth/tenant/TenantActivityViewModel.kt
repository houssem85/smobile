package fr.strada.smobile.ui.auth.tenant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fr.strada.smobile.data.repositories.UserRepository
import javax.inject.Inject

class TenantActivityViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {


}