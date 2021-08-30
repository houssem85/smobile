package fr.strada.smobile.di.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.auth.borne.AuthBorneViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey


@Module
abstract class AuthBorneViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthBorneViewModel::class)
    abstract fun  authBorneViewModel(authBorneViewModel: AuthBorneViewModel): ViewModel
}