package com.keren.control.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.keren.control.data.remote.CoreUrlHolder
import com.keren.control.data.remote.api.KerenApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Retrofit requires a non-empty base at create-time.
    // Runtime host is rewritten by DynamicBaseUrlInterceptor from CoreUrlHolder.
    private const val PLACEHOLDER_BASE = "http://127.0.0.1:8080/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideDynamicBaseInterceptor(urlHolder: CoreUrlHolder): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val configured = urlHolder.getHttpBaseUrl().toHttpUrlOrNull()
            if (configured == null) {
                return@Interceptor chain.proceed(original)
            }
            val newUrl = original.url.newBuilder()
                .scheme(configured.scheme)
                .host(configured.host)
                .port(configured.port)
                .build()
            chain.proceed(original.newBuilder().url(newUrl).build())
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(dynamicBase: Interceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(dynamicBase)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PLACEHOLDER_BASE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideKerenApi(retrofit: Retrofit): KerenApi =
        retrofit.create(KerenApi::class.java)
}
