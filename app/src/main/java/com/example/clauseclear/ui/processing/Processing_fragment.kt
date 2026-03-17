package com.example.clauseclear.ui.processing

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.clauseclear.R
import com.example.clauseclear.databinding.FragmentProcessingFragmentBinding
import com.example.clauseclear.ui.sharedDocument.SharedDocumentViewModel
import com.example.clauseclear.utils.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ProcessingFragment : Fragment() {

    private var _binding: FragmentProcessingFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedDocumentViewModel
    private lateinit var viewModel: ProcessingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcessingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProcessingViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedDocumentViewModel::class.java]

        initializeSteps()
        startStepAnimation()
        
        val uri = sharedViewModel.documentUri
        val text = sharedViewModel.documentText
        val fileName = sharedViewModel.fileName ?: "document.pdf"

        observeState()

        if (uri != null) {
            val file = uriToFile(requireContext(), uri, fileName)
            if (file != null) {
                viewModel.startPdfAnalysis(file)
            } else {
                Toast.makeText(requireContext(), "Failed to prepare file", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        } else if (!text.isNullOrEmpty()) {
            viewModel.startAnalysis(text, fileName)
        } else {
            Toast.makeText(requireContext(), "No document found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun initializeSteps() {
        binding.stepReading.tvStepLabel.text = "Reading document text"
        binding.stepIdentifying.tvStepLabel.text = "Identifying clause types"
        binding.stepDetecting.tvStepLabel.text = "Detecting risk levels"
        binding.stepSummary.tvStepLabel.text = "Generating plain English summary"
        binding.stepReport.tvStepLabel.text = "Preparing your report"

        val steps = listOf(
            binding.stepReading,
            binding.stepIdentifying,
            binding.stepDetecting,
            binding.stepSummary,
            binding.stepReport
        )

        steps.forEach { step ->
            step.tvStepStatus.text = ""
            step.tvStepStatus.setBackgroundResource(0)
            step.root.alpha = 0.5f // Dim inactive steps
        }
    }

    private fun startStepAnimation() {
        lifecycleScope.launch {
            val steps = listOf(
                binding.stepReading,
                binding.stepIdentifying,
                binding.stepDetecting,
                binding.stepSummary,
                binding.stepReport
            )

            for (i in 0 until steps.size - 1) {
                // Show current step as active/loading
                steps[i].root.alpha = 1.0f
                steps[i].tvStepStatus.text = "⏳"
                
                // Simulate processing time (2-4 seconds per step)
                delay(3000)
                
                // Mark current step as done
                steps[i].tvStepStatus.text = ""
                steps[i].tvStepStatus.setBackgroundResource(R.drawable.bg_step_done)
            }
            
            // Last step remains active until API completes
            steps.last().root.alpha = 1.0f
            steps.last().tvStepStatus.text = "⏳"
        }
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    Log.d("ProcessingFragment", "Loading")
                }
                is UiState.Success -> {
                    Log.d("ProcessingFragment", "Success received")
                    
                    // Mark all steps as complete immediately on success
                    val steps = listOf(
                        binding.stepReading,
                        binding.stepIdentifying,
                        binding.stepDetecting,
                        binding.stepSummary,
                        binding.stepReport
                    )
                    steps.forEach { 
                        it.root.alpha = 1.0f
                        it.tvStepStatus.text = ""
                        it.tvStepStatus.setBackgroundResource(R.drawable.bg_step_done) 
                    }

                    val (document, clauses) = state.data
                    sharedViewModel.analyzedDocument = document
                    sharedViewModel.analyzedClauses = clauses
                    
                    // Small delay so user sees the last checkmark
                    lifecycleScope.launch {
                        delay(800)
                        findNavController().navigate(R.id.action_processingFragment_to_summaryFragment)
                    }
                }
                is UiState.Error -> {
                    Log.e("ProcessingFragment", "Error: ${state.message}")
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri, fileName: String): File? {
        val file = File(context.cacheDir, fileName)
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}