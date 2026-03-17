package com.example.clauseclear.ui.processing

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.clauseclear.data.model.Clause
import com.clauseclear.data.model.Document
import com.example.clauseclear.data.repository.AnalysisRepository
import com.example.clauseclear.utils.UiState
import kotlinx.coroutines.launch
import java.io.File

class ProcessingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AnalysisRepository(application.applicationContext)

    private val _uiState = MutableLiveData<UiState<Pair<Document, List<Clause>>>>()
    val uiState: LiveData<UiState<Pair<Document, List<Clause>>>> = _uiState

    fun startAnalysis(text: String, fileName: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repository.analyzeText(text, fileName)
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                Log.e("ProcessingVM", "Error: ${e.message}")
                _uiState.value = UiState.Error(e.message ?: "Analysis failed")
            }
        }
    }

    fun startPdfAnalysis(file: File) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repository.analyzePdf(file)
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                Log.e("ProcessingVM", "PDF Error: ${e.message}")
                _uiState.value = UiState.Error(e.message ?: "PDF Analysis failed")
            }
        }
    }
}