package com.example.clauseclear.ui.compare

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauseclear.R
import com.example.clauseclear.databinding.FragmentSideBySideBinding
import com.example.clauseclear.ui.sharedDocument.SharedDocumentViewModel

class SideBySideFragment :
    Fragment(R.layout.fragment_side_by_side) {

    private lateinit var binding: FragmentSideBySideBinding
    private lateinit var sharedViewModel: SharedDocumentViewModel
    private lateinit var adapter: SideBySideAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSideBySideBinding.bind(view)

        sharedViewModel =
            ViewModelProvider(requireActivity())[SharedDocumentViewModel::class.java]

        adapter = SideBySideAdapter()

        binding.rvComparison.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvComparison.adapter = adapter

        loadData()

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadData() {

        val data = sharedViewModel.comparisonResult

        adapter.submitList(data)

        val unfavorableCount = data.count {
            it.clauseA?.plainEnglish != it.clauseB?.plainEnglish
        }

        binding.tvWarning.text =
            "⚠️  $unfavorableCount unfavorable changes detected"
    }
}
