package fr.strada.smobile.data.network


import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import fr.strada.smobile.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


interface TachoFileAPI {

        //envoi fichier C1B
    @POST("api/TachoFile")
    fun fileSending(@Body jsonObject: JsonObject , @Header("Authorization") token:String): Call<ResponseBody>

    companion object Factory {
        private const val BASE_URL : String = BuildConfig.URL_BASE_TFD
        fun create(): TachoFileAPI
        {
            val gson = GsonBuilder().setLenient().create()
            val okhttpClientBuilder = OkHttpClient.Builder()
            okhttpClientBuilder.connectTimeout(120, TimeUnit.SECONDS)
            okhttpClientBuilder.readTimeout(120, TimeUnit.SECONDS)
            okhttpClientBuilder.writeTimeout(120, TimeUnit.SECONDS)
            okhttpClientBuilder.addInterceptor(httpLoggingInterceptor()) // used if network off OR on
            val builder = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okhttpClientBuilder.build())
            val retrofit=builder.build()
            return retrofit.create(TachoFileAPI::class.java)
        }
        private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor ()//{ message ->
                //    Timber.d("log_send: http log: $message")
           //     }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }
    }
}