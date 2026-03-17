package com.example.clauseclear.ui.clause

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clauseclear.R
import com.example.clauseclear.data.model.RiskLevel
import com.example.clauseclear.databinding.FragmentClauseDetailBinding
import com.example.clauseclear.ui.sharedDocument.SharedDocumentViewModel

class ClauseDetailFragment : Fragment(R.layout.fragment_clause_detail) {

    private var _binding: FragmentClauseDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedDocumentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentClauseDetailBinding.bind(view)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedDocumentViewModel::class.java]

        val clause = sharedViewModel.selectedClause ?: return

        // Set Texts
        binding.tvClauseTitle.text = clause.title
        binding.tvOriginalText.text = clause.originalText
        binding.tvExplanation.text = clause.plainEnglish
        binding.tvWhyMatters.text = clause.whyItMatters
        binding.tvWatchFor.text = clause.whatToWatchFor

        // Dynamic Risk Badge Styling
        val context = requireContext()
        when (clause.riskLevel) {
            RiskLevel.HIGH -> {
                binding.tvRiskBadge.text = "🔴 High Risk"
                binding.tvRiskBadge.setTextColor(ContextCompat.getColor(context, R.color.red))
                binding.tvRiskBadge.setBackgroundResource(R.drawable.badge_red)
            }
            RiskLevel.MEDIUM -> {
                binding.tvRiskBadge.text = "🟡 Medium Risk"
                binding.tvRiskBadge.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                binding.tvRiskBadge.setBackgroundResource(R.drawable.badge_yellow)
            }
            RiskLevel.INFO -> {
                binding.tvRiskBadge.text = "🟢 Info"
                binding.tvRiskBadge.setTextColor(ContextCompat.getColor(context, R.color.green))
                binding.tvRiskBadge.setBackgroundResource(R.drawable.badge_green)
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
