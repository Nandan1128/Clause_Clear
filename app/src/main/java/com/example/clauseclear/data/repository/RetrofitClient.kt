package com.example.clauseclear.data.repository

import android.content.Context
import android.util.Log
import com.example.clauseclear.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.109:8080/"

    private fun getOkHttpClient(context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY 
        }

        val authInterceptor = Interceptor { chain ->
            val sessionManager = SessionManager(context.applicationContext)
            val token = sessionManager.fetchAuthToken()
            
            val requestBuilder = chain.request().newBuilder()
            
            if (!token.isNullOrEmpty()) {
                Log.d("RetrofitClient", "Using token: ${token.take(10)}...")
                requestBuilder.header("Authorization", "Bearer $token")
            }
            
            val response = chain.proceed(requestBuilder.build())
            
            if (response.code == 403 || response.code == 401) {
                Log.e("RetrofitClient", "Access Denied (403/401). Token might be expired.")
            }
            
            response
        }

        // AI Analysis can take a long time. 
        // We set a long timeout (60s) to allow the server to finish processing.
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    fun getAnalysisApiService(context: Context): AnalysisApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient(context))
            .build()
            .create(AnalysisApiService::class.java)
    }
}