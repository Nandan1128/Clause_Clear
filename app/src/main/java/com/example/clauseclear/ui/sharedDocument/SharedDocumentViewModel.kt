package com.example.clauseclear.ui.sharedDocument

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.clauseclear.data.model.Document
import com.example.clauseclear.data.model.Clause
import com.example.clauseclear.data.model.ClauseComparison

class SharedDocumentViewModel : ViewModel() {
    var documentText: String? = null
    var documentUri: Uri? = null
    var fileName: String? = null
    
    var analyzedDocument: Document? = null
    var analyzedClauses: List<Clause> = emptyList()
    var selectedClause: Clause? = null

    var selectedDocumentId: String? = null
    var comparisonResult : List<ClauseComparison> = emptyList()

    var selectedOldDocId: String? = null
    var selectedNewDocId: String? = null
}