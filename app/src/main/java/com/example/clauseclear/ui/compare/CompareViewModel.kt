package com.example.clauseclear.ui.compare

import android.app.Application
import androidx.lifecycle.*
import com.example.clauseclear.data.model.ClauseComparison
import com.example.clauseclear.data.repository.AnalysisRepository
import com.example.clauseclear.data.repository.CompareRepository
import kotlinx.coroutines.launch

class CompareViewModel(application: Application) :
    AndroidViewModel(application) {

    private val analysisRepository =
        AnalysisRepository(application.applicationContext)

    private val compareRepository =
        CompareRepository()

    private val _comparisonResult =
        MutableLiveData<List<ClauseComparison>>()

    val comparisonResult: LiveData<List<ClauseComparison>>
        get() = _comparisonResult

    fun compareDocuments(docIdA: String, docIdB: String) {

        viewModelScope.launch {

            // 1️⃣ Load both documents from DB
            val resultA =
                analysisRepository.getDocumentWithClauses(docIdA)

            val resultB =
                analysisRepository.getDocumentWithClauses(docIdB)

            if (resultA != null && resultB != null) {

                val clausesA = resultA.second
                val clausesB = resultB.second

                // 2️⃣ Compare clauses by title
                val comparison =
                    compareRepository.compareClausesByTitle(
                        clausesA,
                        clausesB
                    )

                // 3️⃣ Send result to UI
                _comparisonResult.value = comparison
            }
        }
    }
}
