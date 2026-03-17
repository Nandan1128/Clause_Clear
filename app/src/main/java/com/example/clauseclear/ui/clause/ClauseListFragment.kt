package com.example.clauseclear.ui.clause

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauseclear.R
import com.example.clauseclear.data.model.RiskLevel
import com.example.clauseclear.databinding.FragmentClauseListBinding
import com.example.clauseclear.ui.sharedDocument.SharedDocumentViewModel

class ClauseListFragment : Fragment(R.layout.fragment_clause_list) {

    private var _binding: FragmentClauseListBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedDocumentViewModel
    private lateinit var adapter: ClauseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentClauseListBinding.bind(view)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedDocumentViewModel::class.java]

        setupRecyclerView()
        setupFilters()
        loadData()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        binding.rvClauses.layoutManager = LinearLayoutManager(requireContext())
        adapter = ClauseAdapter { clause ->
            sharedViewModel.selectedClause = clause
            findNavController().navigate(R.id.action_clauseListFragment_to_clauseDetailFragment)
        }
        binding.rvClauses.adapter = adapter
    }

    private fun setupFilters() {
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            filterClauses(checkedIds.firstOrNull() ?: R.id.chip_all)
        }
    }

    private fun filterClauses(checkedId: Int) {
        val allClauses = sharedViewModel.analyzedClauses
        
        val filteredList = when (checkedId) {
            R.id.chip_high -> allClauses.filter { it.riskLevel == RiskLevel.HIGH }
            R.id.chip_medium -> allClauses.filter { it.riskLevel == RiskLevel.MEDIUM }
            R.id.chip_info -> allClauses.filter { it.riskLevel == RiskLevel.INFO }
            else -> allClauses
        }
        
        adapter.submitList(filteredList)
        binding.rvClauses.scrollToPosition(0)
        
        // Update count in header
        val docName = sharedViewModel.analyzedDocument?.fileName ?: "Document"
        binding.tvDocName.text = "$docName  •  ${filteredList.size} found"
    }

    private fun loadData() {
        val clauses = sharedViewModel.analyzedClauses
        adapter.submitList(clauses)
        
        val docName = sharedViewModel.analyzedDocument?.fileName ?: "Document"
        binding.tvDocName.text = "$docName  •  ${clauses.size} found"
        
        binding.chipAll.isChecked = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
