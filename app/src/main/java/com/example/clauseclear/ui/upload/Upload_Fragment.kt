package com.example.clauseclear.ui.upload

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clauseclear.R
import com.example.clauseclear.databinding.FragmentUploadBinding
import com.example.clauseclear.ui.sharedDocument.SharedDocumentViewModel

class Upload_Fragment : Fragment(R.layout.fragment_upload) {

    private lateinit var sharedViewModel: SharedDocumentViewModel
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    private val pickPdfLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                handleSelectedPdf(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUploadBinding.bind(view)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedDocumentViewModel::class.java]
        
        binding.uploadPdf.setOnClickListener {
            pickPdfLauncher.launch(arrayOf("application/pdf"))
        }

        binding.btnUpload.setOnClickListener {
            pickPdfLauncher.launch(arrayOf("application/pdf"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleSelectedPdf(uri: Uri) {
        val fileName = getFileName(uri)
        
        // Store URI and fileName in SharedViewModel instead of extracting text here
        sharedViewModel.documentUri = uri
        sharedViewModel.fileName = fileName
        
        // Navigate to ProcessingFragment where the API call will be made
        findNavController().navigate(R.id.processingFragment)
    }

    private fun getFileName(uri: Uri): String {
        var name = "document.pdf"
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
        return name
    }
}