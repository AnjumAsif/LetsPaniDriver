package com.example.letspani.apis

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    private const val TIME_OUT: Long = 20
    private var sOkHttpClient: OkHttpClient? = null

    val GSON = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create()

    /**
     * Create an implementation of the API endpoints defined by the {@param tClass} interface.
     *
     * @param context
     * @param <T>
     * @param tClass  @return
    </T> */
    fun <T> getService(context: Context, tClass: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://teach.acmmosmedia.co.uk/index.php/Web_service/")
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .client(getHttpClient(context))
            .build()
        return retrofit.create(tClass)
    }

    /**
     * Return singleton instance of [OkHttpClient].
     * Enable HttpLogging
     *
     * @return
     * @param context
     */
    private fun getHttpClient(context: Context): OkHttpClient {
        if (sOkHttpClient == null) {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            builder.readTimeout(TIME_OUT, TimeUnit.SECONDS)
            builder.addInterceptor { chain ->
                chain.proceed(
                    getRequestBuilderWithHeaders(
                        context,
                        chain.request().newBuilder()
                    ).build()
                )
            }

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            sOkHttpClient = builder.build()
        }
        return sOkHttpClient!!
    }

    private fun getRequestBuilderWithHeaders(context: Context, reqBuilder: Request.Builder): Request.Builder {
        //   val token: String? = SharedPrefHelper.getAccessToken(context)

        /*  if (token != null && !TextUtils.isEmpty(token)) {
              reqBuilder.header("Authorization", "Bearer $token")
          }*/

        reqBuilder.header("Accept", "application/json")
        return reqBuilder
    }
}