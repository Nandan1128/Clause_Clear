package com.example.clauseclear.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.clauseclear.R
import com.example.clauseclear.data.local.SessionManager
import com.example.clauseclear.databinding.FragmentSettingsBinding
import com.example.clauseclear.ui.auth.AuthViewModel
import com.example.clauseclear.utils.UiState

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
        }

        authViewModel.logoutState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.btnLogout.isEnabled = false
                }
                is UiState.Success -> {
                    sessionManager.clearTokens()
                    Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                    // Navigate to Login and clear backstack
                    findNavController().navigate(R.id.loginFragment)
                }
                is UiState.Error -> {
                    binding.btnLogout.isEnabled = true
                    // Even if API logout fails, we clear local tokens and go to login
                    sessionManager.clearTokens()
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }

        binding.btnClearAll.setOnClickListener {
            // Implementation for clearing all documents
            Toast.makeText(requireContext(), "Feature coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}