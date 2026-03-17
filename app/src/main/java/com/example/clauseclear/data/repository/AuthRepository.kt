package com.example.clauseclear.data.repository

import com.example.clauseclear.data.model.AuthRequest
import com.example.clauseclear.data.model.AuthResponse
import com.example.clauseclear.data.model.RefreshRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {
    private val apiService: AuthApiService

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.109:8080/") // Must end with /
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        apiService = retrofit.create(AuthApiService::class.java)
    }

    suspend fun login(request: AuthRequest): Response<AuthResponse> {
        return apiService.login(request)
    }

    suspend fun register(request: AuthRequest): Response<AuthResponse> {
        return apiService.register(request)
    }

    suspend fun refresh(request: RefreshRequest): Response<AuthResponse> {
        return apiService.refresh(request)
    }

    suspend fun logout(): Response<Unit> {
        return apiService.logout()
    }
}