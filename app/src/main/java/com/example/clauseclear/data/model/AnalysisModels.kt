package com.example.clauseclear.data.model

import com.google.gson.annotations.SerializedName

data class AnalysisRequest(
    val text: String,
    val fileName: String
)

data class AnalysisResponse(
    @SerializedName("documentId")
    val documentId: Any?,
    @SerializedName("clauses")
    val clauses: List<ClauseResponse>?,
    @SerializedName("fileName")
    val fileName: String? = null,
    @SerializedName("createdAt")
    val createdAt: Long? = null,
    @SerializedName("highRiskCount")
    val highRiskCount: Int? = null,
    @SerializedName("mediumRiskCount")
    val mediumRiskCount: Int? = null,
    @SerializedName("infoRiskCount")
    val infoRiskCount: Int? = null,
    @SerializedName("keyPoints")
    val keyPoints: List<KeyPointResponse>? = null,
    @SerializedName("pageCount")
    val pageCount: Int? = null
)

data class ClauseResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("riskLevel")
    val riskLevel: String? = null,
    @SerializedName("originalText")
    val originalText: String? = null,
    @SerializedName("plainEnglish")
    val plainEnglish: String? = null,
    @SerializedName("whyItMatters")
    val whyItMatters: String? = null,
    @SerializedName("whatToWatchFor")
    val whatToWatchFor: String? = null
)

data class KeyPointResponse(
    @SerializedName("label")
    val label: String?,
    @SerializedName("value")
    val value: String?
)