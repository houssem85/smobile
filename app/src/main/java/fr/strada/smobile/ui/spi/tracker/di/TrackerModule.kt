package fr.strada.smobile.ui.spi.tracker.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.runningapprm.other.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import fr.strada.smobile.R
import fr.strada.smobile.ui.main.MainActivity

import javax.inject.Singleton

@Module
open class TrackerModule {

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(
        app: Context
    ) = FusedLocationProviderClient(app)

    @Singleton
    @Provides
    fun provideMainActivityPendingIntent(
        app: Context
    ) = PendingIntent.getActivity(
        app,
        0,
        Intent(app, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    @Singleton
    @Provides
    fun provideBaseNotificationBuilder(
        app: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(app, Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(false)
        .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
        .setContentTitle("Tracking App")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)
}