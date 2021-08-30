package fr.strada.smobile.di.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.auth.auth0.Auth0ViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class Auth0ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(Auth0ViewModel::class)
    abstract fun bindAuth0ViewModel(homeViewModel: Auth0ViewModel): ViewModel


}