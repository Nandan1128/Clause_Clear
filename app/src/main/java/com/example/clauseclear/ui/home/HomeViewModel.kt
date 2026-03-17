package com.example.clauseclear.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.example.clauseclear.data.model.Clause
import com.clauseclear.data.model.Document
import com.example.clauseclear.data.local.AppDatabase
import com.example.clauseclear.data.local.entity.DocumentEntity
import com.example.clauseclear.data.repository.AnalysisRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository =
        AnalysisRepository(application.applicationContext)

    private val dao =
        AppDatabase.getDatabase(application).documentDao()

    private val _documents = MutableLiveData<List<DocumentEntity>>()
    val documents: LiveData<List<DocumentEntity>> = _documents

    fun loadDocuments() {
        viewModelScope.launch {
            _documents.value = dao.getAllDocuments()
        }
    }
    fun openDocument(docId: String,
                     onLoaded: (Document, List<Clause>) -> Unit
    ) {

        viewModelScope.launch {

            val result = repository.getDocumentWithClauses(docId)

            result?.let { (doc , clauses, _) ->
                onLoaded(doc, clauses)
            }
        }
    }

}
