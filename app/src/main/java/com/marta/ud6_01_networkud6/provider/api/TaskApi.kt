package com.marta.ud6_01_networkud6.provider.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TaskApi {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    // TODO pon tu ip
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://999.999.9.999:8080/")
        .client(client)//capta la petición
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(TaskApiService::class.java)
}
