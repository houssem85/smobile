package fr.strada.smobile.di.app

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import fr.strada.smobile.utils.multi_binding.DaggerWorkerFactory

@Module
abstract class WorkerFactoryModule {
    @Binds
    abstract fun bindWorkManagerFactory(factory: DaggerWorkerFactory): WorkerFactory
}