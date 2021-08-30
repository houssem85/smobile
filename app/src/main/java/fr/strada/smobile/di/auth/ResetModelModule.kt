package fr.strada.smobile.di.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.auth.password.ResetPasswordViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class ResetModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    abstract fun bindAuth0ViewModel(homeViewModel: ResetPasswordViewModel): ViewModel
}