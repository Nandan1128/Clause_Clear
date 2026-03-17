package com.example.clauseclear.data.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class RefreshRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)