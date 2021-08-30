package fr.strada.smobile.di.app

import android.app.Application
import androidx.room.Room
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.moczul.ok2curl.CurlInterceptor
import dagger.Module
import dagger.Provides
import fr.strada.smobile.BuildConfig
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.data.network.PointeuseDao
import fr.strada.smobile.data.repositories.*
import fr.strada.smobile.utils.DATABASE_NAME
import fr.strada.smobile.utils.TIME_OUT_NETWORK
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
open class AppModule {

    @Singleton
    @Provides
    fun provideAuth0(): Auth0 {
        val auth0 = Auth0(
            BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN
        )
        auth0.let {
            it.isOIDCConformant = true
            it.isLoggingEnabled = true
        }
        return auth0
    }


    @Provides
    fun provideAuthenticationAPIClient(auth0: Auth0): AuthenticationAPIClient {
        return AuthenticationAPIClient(auth0)
    }

    @Singleton
    @Provides
    fun provideCredentialsManager(
        context: Application,
        authenticationAPIClient: AuthenticationAPIClient
    ): CredentialsManager {
        return CredentialsManager(authenticationAPIClient, SharedPreferencesStorage(context))
    }

    @Singleton
    @Named("okhttp_uam")
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder.connectTimeout(TIME_OUT_NETWORK, TimeUnit.SECONDS)
        okhttpClientBuilder.readTimeout(TIME_OUT_NETWORK, TimeUnit.SECONDS)
        okhttpClientBuilder.writeTimeout(TIME_OUT_NETWORK, TimeUnit.SECONDS)
        okhttpClientBuilder.addInterceptor(httpLoggingInterceptor())
        okhttpClientBuilder.hostnameVerifier { _, _ -> true }

        okhttpClientBuilder.addInterceptor(CurlInterceptor { message ->
            Timber.tag(
                "Ok2Curl"
            ).e(
                message
            )

        })
        return okhttpClientBuilder.build()
    }

    @Named("okhttp_gateway")
    @Provides
    fun provideOkHttpClientGetWay(): OkHttpClient {
        val okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder.connectTimeout(TIME_OUT_NETWORK, TimeUnit.SECONDS)
        okhttpClientBuilder.readTimeout(TIME_OUT_NETWORK, TimeUnit.SECONDS)
        okhttpClientBuilder.writeTimeout(TIME_OUT_NETWORK, TimeUnit.SECONDS)
        okhttpClientBuilder.addInterceptor(httpLoggingInterceptor())
        Timber.e(BuildConfig.BUILD_TYPE)
        if (BuildConfig.BUILD_TYPE == "prod") {
            okhttpClientBuilder.addInterceptor { request ->
                val tonentId = SmobileApp.tonnentID
                val newRequest = request.request().newBuilder()
                newRequest.addHeader("TenantId", tonentId)
                request.proceed(newRequest.build())
            }
        }
        okhttpClientBuilder.addInterceptor(CurlInterceptor { message ->
            Timber.tag(
                "Ok2Curl"
            ).e(
                message
            )

        })
        return okhttpClientBuilder.build()
    }


    @Named("retrofit_uam")
    @Singleton
    @Provides
    fun provideRetrofit(@Named("okhttp_uam") okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        val builder = Retrofit.Builder().baseUrl(BuildConfig.URL_BASE_UAM)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
        return builder.build()
    }

    @Named("retrofit_gateway")
    @Provides
    fun provideRetrofitGetWay(@Named("okhttp_gateway") okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        val builder = Retrofit.Builder().baseUrl(BuildConfig.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
        return builder.build()
    }
    @Named("retrofit_gateway_web")
    @Provides
    fun provideRetrofitGetWayweb(@Named("okhttp_gateway") okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        val builder = Retrofit.Builder().baseUrl(BuildConfig.URL_BASE_WEB)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
        return builder.build()
    }

    @Singleton
    @Provides
    @Named("api_uam")
    fun provideApi(@Named("retrofit_uam") retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @Named("api_gateway")
    fun provideApiGetway(@Named("retrofit_gateway") retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
    @Provides
    @Named("api_gateway_web")
    fun provideApiGetwayWeb(@Named("retrofit_gateway_web") retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    private fun  httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideAppDataBase(context: Application): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePointeuseDao(appDatabase: AppDatabase): PointeuseDao {
        return appDatabase.pointeuseDao()
    }

    @Singleton
    @Provides
    open fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl):UserRepository{
       return userRepositoryImpl
    }

    @Singleton
    @Provides
    open fun provideMesActivitiesRepository(mesActivitesRepository : MesActivitesRepositoryImpl): MesActivitesRepository{
        return mesActivitesRepository
    }

    @Singleton
    @Provides
    open fun provideInfractionsRepository(infractionRepositoryImpl : InfractionRepositoryImpl): InfractionRepository{
        return infractionRepositoryImpl
    }

    /*@Singleton
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
        .setContentIntent(pendingIntent)*/


}