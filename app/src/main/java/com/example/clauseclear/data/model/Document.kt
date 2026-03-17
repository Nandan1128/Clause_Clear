package com.clauseclear.data.model

import com.example.clauseclear.data.model.KeyPoint

data class Document(
    val id: String,
    val fileName: String,
    val pageCount: Int,
    val clauseCount: Int,
    val highRiskCount: Int,
    val mediumRiskCount: Int,
    val infoCount: Int,
    val keyPoints: List<KeyPoint>,
    val analyzedAt: Long
)
