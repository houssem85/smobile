package fr.strada.smobile.di.messagerie.detailMessagePredefini

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.messagerie.detail_message_predefini.DetailMessagePredefiniViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class DetailMessagePredefiniViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailMessagePredefiniViewModel::class)
    abstract fun bindDetailMessagePredefiniViewModel(detailMessagePredefiniViewModel: DetailMessagePredefiniViewModel): ViewModel
}