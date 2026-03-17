package com.example.clauseclear.data.local.dao

import androidx.room.*
import com.example.clauseclear.data.local.entity.*

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClauses(clauses: List<ClauseEntity>)

    @Query("SELECT * FROM documents WHERE id = :docId")
    suspend fun getDocumentById(docId: String): DocumentEntity?

    @Query("SELECT * FROM documents ORDER BY analyzedAt DESC")
    suspend fun getAllDocuments(): List<DocumentEntity>

    @Query("SELECT * FROM clauses WHERE documentId = :docId")
    suspend fun getClausesForDocument(docId: String): List<ClauseEntity>

    @Query("DELETE FROM documents WHERE id = :docId")
    suspend fun deleteDocument(docId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyPoints(keyPoints: List<KeyPointEntity>)

    @Query("SELECT * FROM key_points WHERE documentId = :docId")
    suspend fun getKeyPointsForDocument(docId: String): List<KeyPointEntity>
}
