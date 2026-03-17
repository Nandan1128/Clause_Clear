package com.example.clauseclear.data.repository

import android.content.Context
import com.clauseclear.data.model.Document
import com.example.clauseclear.data.local.AppDatabase
import com.example.clauseclear.data.local.entity.ClauseEntity
import com.example.clauseclear.data.local.entity.DocumentEntity
import com.example.clauseclear.data.local.entity.KeyPointEntity
import com.example.clauseclear.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.util.*

class AnalysisRepository(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val dao = db.documentDao()
    private val apiService = RetrofitClient.getAnalysisApiService(context)

    suspend fun analyzePdf(file: File): Pair<Document, List<Clause>> {
        return withContext(Dispatchers.IO) {
            val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            
            val response = apiService.analyzePdf(body)
            if (response.isSuccessful && response.body() != null) {
                saveToRoom(response.body()!!)
            } else {
                val errorMsg = parseError(response)
                throw Exception("Analysis failed: $errorMsg")
            }
        }
    }

    private fun parseError(response: Response<*>): String {
        return try {
            val errorBodyString = response.errorBody()?.string() ?: ""
            if (errorBodyString.isNotEmpty()) {
                val jsonObject = JSONObject(errorBodyString)
                jsonObject.optString("message", jsonObject.optString("error", response.message()))
            } else {
                response.message().ifEmpty { "Server Error (${response.code()})" }
            }
        } catch (e: Exception) {
            "Error ${response.code()}"
        }
    }

    private suspend fun saveToRoom(response: AnalysisResponse): Pair<Document, List<Clause>> {
        val docId = response.documentId?.toString() ?: UUID.randomUUID().toString()
        
        // Map pageCount from refined prompt
        val serverPageCount = response.pageCount ?: 0

        val clauses = response.clauses?.mapIndexed { index, it ->
            Clause(
                id = it.id ?: "cl_$index",
                documentId = docId,
                title = it.title ?: "Untitled Clause",
                riskLevel = try { RiskLevel.valueOf(it.riskLevel?.uppercase() ?: "INFO") } catch(e: Exception) { RiskLevel.INFO },
                originalText = it.originalText ?: "Text not available",
                plainEnglish = it.plainEnglish ?: "",
                whyItMatters = it.whyItMatters ?: "",
                whatToWatchFor = it.whatToWatchFor ?: ""
            )
        } ?: emptyList()

        val hCount = response.highRiskCount ?: clauses.count { it.riskLevel == RiskLevel.HIGH }
        val mCount = response.mediumRiskCount ?: clauses.count { it.riskLevel == RiskLevel.MEDIUM }
        val iCount = response.infoRiskCount ?: clauses.count { it.riskLevel == RiskLevel.INFO }

        val keyPoints = response.keyPoints?.map { 
            KeyPoint(it.label ?: "", it.value ?: "")
        } ?: emptyList()

        val document = Document(
            id = docId,
            fileName = response.fileName ?: "New Analysis",
            pageCount = serverPageCount, 
            clauseCount = clauses.size,
            highRiskCount = hCount,
            mediumRiskCount = mCount,
            infoCount = iCount,
            analyzedAt = response.createdAt ?: System.currentTimeMillis(),
            keyPoints = keyPoints
        )

        // Local Storage Sync
        dao.insertDocument(DocumentEntity(document.id, document.fileName, document.pageCount, document.clauseCount, document.highRiskCount, document.mediumRiskCount, document.infoCount, document.analyzedAt))
        dao.insertClauses(clauses.map { ClauseEntity("${docId}_${it.id}", docId, it.title, it.riskLevel.name, it.originalText, it.plainEnglish, it.whyItMatters, it.whatToWatchFor) })
        dao.insertKeyPoints(keyPoints.mapIndexed { i, kp -> KeyPointEntity("${docId}_kp_$i", docId, kp.label, kp.value) })

        return Pair(document, clauses)
    }

    suspend fun analyzeText(text: String, fileName: String): Pair<Document, List<Clause>> {
        return withContext(Dispatchers.IO) {
            val response = apiService.analyzeText(AnalysisRequest(text, fileName))
            if (response.isSuccessful && response.body() != null) {
                saveToRoom(response.body()!!)
            } else {
                throw Exception("Text analysis failed")
            }
        }
    }

    suspend fun getDocumentWithClauses(docId: String): Triple<Document, List<Clause>, List<KeyPoint>>? {
        val documentEntity = dao.getDocumentById(docId) ?: return null
        val clauses = dao.getClausesForDocument(docId).map {
            Clause(it.id, it.documentId, it.title, RiskLevel.valueOf(it.riskLevel), it.originalText, it.plainEnglish, it.whyItMatters, it.whatToWatchFor)
        }
        val keyPoints = dao.getKeyPointsForDocument(docId).map { KeyPoint(it.label, it.value) }
        
        val doc = Document(documentEntity.id, documentEntity.fileName, documentEntity.pageCount, documentEntity.clauseCount, documentEntity.highRiskCount, documentEntity.mediumRiskCount, documentEntity.infoCount, keyPoints, documentEntity.analyzedAt)
        return Triple(doc, clauses, keyPoints)
    }

    suspend fun getAllDocuments(): List<Document> {
        return dao.getAllDocuments().map {
            Document(it.id, it.fileName, it.pageCount, it.clauseCount, it.highRiskCount, it.mediumRiskCount, it.infoCount, emptyList(), it.analyzedAt)
        }
    }
}