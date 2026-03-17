package com.example.clauseclear.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_points")
data class KeyPointEntity (

    @PrimaryKey
    val id: String,
    val documentId: String,
    val label: String,
    val value: String
)