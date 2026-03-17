package com.example.clauseclear.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauseclear.R
import com.example.clauseclear.databinding.FragmentHomeFragmentBinding
import com.example.clauseclear.ui.sharedDocument.SharedDocumentViewModel

class Home_fragment : Fragment(R.layout.fragment_home_fragment) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: DocumentAdapter
    private lateinit var sharedViewModel: SharedDocumentViewModel


    private var _binding: FragmentHomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeFragmentBinding.bind(view)
        sharedViewModel =
            ViewModelProvider(requireActivity())[SharedDocumentViewModel::class.java]

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setupRecyclerView()
        viewModel.loadDocuments()
        viewModel.documents.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.btnUpload.setOnClickListener {
            findNavController().navigate(R.id.uploadFragment)
        }
        binding.cardCompare.setOnClickListener {
            findNavController().navigate(R.id.compareFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
    private fun setupRecyclerView() {

        adapter = DocumentAdapter { document ->

            viewModel.openDocument(document.id) { doc, clauses ->

                sharedViewModel.analyzedDocument = doc
                sharedViewModel.analyzedClauses = clauses

                findNavController().navigate(R.id.summaryFragment)
            }

        }

        binding.rvDocuments.layoutManager =
            LinearLayoutManager(requireContext())

        viewModel.documents.observe(viewLifecycleOwner){ list ->
            if(list.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.rvDocuments.visibility = View.GONE
            }else{
                binding.tvEmptyState.visibility = View.GONE
                binding.rvDocuments.visibility = View.VISIBLE
                adapter.submitList(list)
            }
        }

        binding.rvDocuments.adapter = adapter
    }

}