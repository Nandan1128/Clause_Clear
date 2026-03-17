package com.example.clauseclear.data.model

data class Clause(
    val id: String,
    val documentId: String,
    val title: String,
    val riskLevel: RiskLevel,
    val originalText: String,
    val plainEnglish: String,
    val whyItMatters: String,
    val whatToWatchFor: String
)
