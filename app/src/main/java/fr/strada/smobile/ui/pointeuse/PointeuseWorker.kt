package fr.strada.smobile.ui.pointeuse

import android.content.Context
import android.content.Intent
import androidx.work.*
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.repositories.PointeuseRepository
import fr.strada.smobile.ui.widget.SMobileWidget
import fr.strada.smobile.utils.multi_binding.ChildWorkerFactory
import javax.inject.Inject
import javax.inject.Provider

class PointeuseWorker(val context: Context, params: WorkerParameters
                      , val pointeuseRepository : PointeuseRepository
                      ) : CoroutineWorker(context,params){

    override suspend fun doWork(): Result {
        val res = pointeuseRepository.syncronisationActivitePointeuse()
        if(res.status == fr.strada.smobile.utils.Status.SUCCESS)
        {
            sendBrodcastInitAttributePointeuse()
            refreshDataWidget()
            return Result.success()
        }else
        {
            return Result.retry()
        }
    }

    private fun sendBrodcastInitAttributePointeuse()
    {
        val intent = Intent(SmobileApp.INIT_ATTRIBUTE_POINTEUSE)
        context.sendBroadcast(intent)
    }

    private fun refreshDataWidget()
    {
        val intent = Intent(context, SMobileWidget::class.java)
        intent.action = SMobileWidget.REFRESH_WIDGET
        context.sendBroadcast(intent)
    }

    class Factory @Inject constructor(
        private val pointeuseRepository: Provider<PointeuseRepository>
    ) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return PointeuseWorker(
                appContext,
                params
                , pointeuseRepository.get()
            )
        }
    }

    companion object {

        const val TAG = "POINTEUSE_WORKER"

        fun enqueue (context: Context) {
            val work = OneTimeWorkRequestBuilder<PointeuseWorker>()
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .addTag(TAG)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(TAG,ExistingWorkPolicy.KEEP,work)
        }
    }

    enum class FutureStatus {
        ENQUEUED , NOT_ENQUEUED
    }
}