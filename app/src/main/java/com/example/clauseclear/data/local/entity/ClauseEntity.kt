package com.example.clauseclear.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clauses")
data class ClauseEntity(

    @PrimaryKey
    val id: String,

    val documentId: String,

    val title: String,
    val riskLevel: String,

    val originalText: String,
    val plainEnglish: String,
    val whyItMatters: String,
    val whatToWatchFor: String
)
