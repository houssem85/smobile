package fr.strada.smobile.di.graphical_view

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.activities.journalier.GraphicalViewViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelKey

@Module
abstract class GraphicalViewViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GraphicalViewViewModel::class)
    abstract fun bindGraphicalViewViewModel(GraphicalViewViewModel: GraphicalViewViewModel): ViewModel
}