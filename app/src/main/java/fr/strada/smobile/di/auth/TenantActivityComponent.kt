package fr.strada.smobile.di.auth

import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.auth.tenant.TenantActivity

@Subcomponent(modules = [TenatActivityModule::class, TenantViewModelModule::class])
interface TenantActivityComponent {
    fun inject(tenantActivity: TenantActivity)
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun setContext(context: TenantActivity): Builder
        fun build(): TenantActivityComponent
    }
}