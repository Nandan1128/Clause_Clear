package com.example.clauseclear.data.repository

import com.example.clauseclear.data.model.AnalysisRequest
import com.example.clauseclear.data.model.AnalysisResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AnalysisApiService {
    @POST("api/analyze")
    suspend fun analyzeText(@Body request: AnalysisRequest): Response<AnalysisResponse>

    @Multipart
    @POST("api/analyze/pdf")
    suspend fun analyzePdf(@Part file: MultipartBody.Part): Response<AnalysisResponse>

    @GET("api/documents")
    suspend fun getDocuments(): Response<List<AnalysisResponse>> // Adjust if list response is different

    @GET("api/documents/{id}")
    suspend fun getDocument(@Path("id") id: String): Response<AnalysisResponse>

    @DELETE("api/documents/{id}")
    suspend fun deleteDocument(@Path("id") id: String): Response<Unit>
}