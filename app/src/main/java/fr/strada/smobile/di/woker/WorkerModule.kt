package fr.strada.smobile.di.woker

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.strada.smobile.ui.pointeuse.PointeuseWorker
import fr.strada.smobile.utils.multi_binding.ChildWorkerFactory
import fr.strada.smobile.utils.multi_binding.WorkerKey

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(PointeuseWorker::class)
    fun bindPointeuseWorker(factory: PointeuseWorker.Factory): ChildWorkerFactory
}