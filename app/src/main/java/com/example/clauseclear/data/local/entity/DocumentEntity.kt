package com.example.clauseclear.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class DocumentEntity(

    @PrimaryKey
    val id: String,

    val fileName: String,
    val pageCount: Int,
    val clauseCount: Int,
    val highRiskCount: Int,
    val mediumRiskCount: Int,
    val infoCount: Int,
    val analyzedAt: Long
)
