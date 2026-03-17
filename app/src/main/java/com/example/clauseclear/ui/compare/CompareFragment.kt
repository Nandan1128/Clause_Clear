package com.example.clauseclear.ui.compare

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauseclear.R
import com.example.clauseclear.data.model.Clause
import com.example.clauseclear.data.model.ClauseComparison
import com.example.clauseclear.data.repository.AnalysisRepository
import com.example.clauseclear.databinding.FragmentCompareBinding
import com.example.clauseclear.ui.sharedDocument.SharedDocumentViewModel
import kotlinx.coroutines.launch

class CompareFragment : Fragment(R.layout.fragment_compare) {

    private var _binding: FragmentCompareBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var sharedViewModel: SharedDocumentViewModel
    private lateinit var repository: AnalysisRepository
    private lateinit var adapter: ComparisonRowAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCompareBinding.bind(view)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedDocumentViewModel::class.java]
        repository = AnalysisRepository(requireContext())

        setupRecyclerView()
        setupSelectionClicks()
        updateUIState()
        tryCompare()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSideBySide.setOnClickListener {
            if (sharedViewModel.comparisonResult.isNotEmpty()) {
                findNavController().navigate(R.id.action_compareFragment_to_sideBySideFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ComparisonRowAdapter()
        binding.rvChanges.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChanges.adapter = adapter
    }

    private fun setupSelectionClicks() {
        binding.slotold.setOnClickListener { showDocumentPicker(isOld = true) }
        binding.slotnew.setOnClickListener { showDocumentPicker(isOld = false) }
    }

    private fun updateUIState() {
        // Hide checks initially if no ID
        binding.imgOldCheck.visibility = if (sharedViewModel.selectedOldDocId != null) View.VISIBLE else View.INVISIBLE
        binding.imgNewCheck.visibility = if (sharedViewModel.selectedNewDocId != null) View.VISIBLE else View.INVISIBLE
        
        // Enable button only if both selected
        binding.btnSideBySide.isEnabled = sharedViewModel.selectedOldDocId != null && sharedViewModel.selectedNewDocId != null
        binding.btnSideBySide.alpha = if (binding.btnSideBySide.isEnabled) 1.0f else 0.5f
    }

    private fun showDocumentPicker(isOld: Boolean) {
        lifecycleScope.launch {
            val documents = repository.getAllDocuments()
            val names = documents.map { it.fileName }.toTypedArray()

            if (names.isEmpty()) {
                android.widget.Toast.makeText(requireContext(), "No documents found to compare", android.widget.Toast.LENGTH_SHORT).show()
                return@launch
            }

            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Select Document")
                .setItems(names) { _, which ->
                    val selected = documents[which]
                    if (isOld) {
                        sharedViewModel.selectedOldDocId = selected.id
                        binding.tvOldDocLabel.text = "Selected Old Version"
                        binding.tvOldFileName.text = selected.fileName
                    } else {
                        sharedViewModel.selectedNewDocId = selected.id
                        binding.tvNewDocLabel.text = "Selected New Version"
                        binding.tvNewFileName.text = selected.fileName
                    }
                    updateUIState()
                    tryCompare()
                }
                .show()
        }
    }

    private fun tryCompare() {
        val oldId = sharedViewModel.selectedOldDocId
        val newId = sharedViewModel.selectedNewDocId

        if (oldId == null || newId == null) return

        lifecycleScope.launch {
            val oldData = repository.getDocumentWithClauses(oldId)
            val newData = repository.getDocumentWithClauses(newId)

            if (oldData == null || newData == null) return@launch

            val comparison = compareClauses(oldData.second, newData.second)
            sharedViewModel.comparisonResult = comparison

            binding.tvChangesHeader.text = "CHANGES FOUND — ${comparison.size} ITEMS"
            adapter.submitList(comparison)
        }
    }

    private fun compareClauses(oldClauses: List<Clause>, newClauses: List<Clause>): List<ClauseComparison> {
        val result = mutableListOf<ClauseComparison>()
        val maxSize = maxOf(oldClauses.size, newClauses.size)

        for (i in 0 until maxSize) {
            val oldClause = oldClauses.getOrNull(i)
            val newClause = newClauses.getOrNull(i)

            if (oldClause?.plainEnglish != newClause?.plainEnglish) {
                result.add(ClauseComparison(
                    title = newClause?.title ?: oldClause?.title ?: "Unknown Clause",
                    clauseA = oldClause,
                    clauseB = newClause
                ))
            }
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}