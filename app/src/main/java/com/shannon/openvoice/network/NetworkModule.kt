package com.shannon.openvoice.network

import com.google.gson.Gson
import com.shannon.openvoice.BuildConfig
import com.shannon.openvoice.FunApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.network
 * @ClassName:      NetworkModule
 * @Description:     java类作用描述
 * @Author:         czhen
 * @CreateDate:     2022/7/26 14:00
 */
class NetworkModule {

    private val okHttpClient: OkHttpClient
    private val retrofit: Retrofit

    init {
        okHttpClient = providesHttpClient()
        retrofit = providesRetrofit()
    }

    private fun providesHttpClient(): OkHttpClient {
        val cacheSize = 25 * 1024 * 1024L
        val context = FunApplication.getInstance()
        val builder = OkHttpClient.Builder()
            .addInterceptor(UserAgentInterceptor())
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cache(Cache(context.cacheDir, cacheSize))

        return builder.apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
            }
        }.build()
    }

    private fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.HOST_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    fun api(): ApiService = retrofit.create()

    companion object {
        val instance: NetworkModule by lazy { NetworkModule() }
    }
}

