package fr.strada.smobile.di.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.auth.tenant.TenantActivityViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey
@Module
abstract class TenantViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TenantActivityViewModel::class)
    abstract fun bindtenantViewModel(tenantViewModel: TenantActivityViewModel): ViewModel


}