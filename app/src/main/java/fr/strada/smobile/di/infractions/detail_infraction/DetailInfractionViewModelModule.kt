package fr.strada.smobile.di.infractions.detail_infraction

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.infractions.detail_infraction.DetailInfractionViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class DetailInfractionViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DetailInfractionViewModel::class)
    abstract fun bindDetailInfractionViewModel(detailInfractionViewModel: DetailInfractionViewModel): ViewModel
}