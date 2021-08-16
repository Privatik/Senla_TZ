package com.example.senla_tz.di.module

import com.example.senla_tz.BuildConfig
import com.example.senla_tz.repository.network.LoginAndRegisterApi
import com.example.senla_tz.repository.network.TracksApi
import com.example.senla_tz.util.Constant
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level =  if (BuildConfig.DEBUG)  HttpLoggingInterceptor.Level.BODY
                        else                    HttpLoggingInterceptor.Level.NONE

        return OkHttpClient().newBuilder()
            .addInterceptor(logger)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    fun provideLoginAndRegisterApi(retrofit: Retrofit): LoginAndRegisterApi = retrofit.create(LoginAndRegisterApi::class.java)

    @Provides
    fun provideTracksApi(retrofit: Retrofit): TracksApi = retrofit.create(TracksApi::class.java)
}