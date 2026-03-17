package com.example.clauseclear.ui.summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clauseclear.R
import com.example.clauseclear.data.model.KeyPoint
import com.example.clauseclear.databinding.FragmentSummaryBinding
import com.example.clauseclear.ui.sharedDocument.SharedDocumentViewModel
import kotlin.collections.forEachIndexed
import kotlin.collections.lastIndex

class SummaryFragment : Fragment(R.layout.fragment_summary) {
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedDocumentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSummaryBinding.bind(view)

        sharedViewModel =
            ViewModelProvider(requireActivity())[SharedDocumentViewModel::class.java]

        val document = sharedViewModel.analyzedDocument ?: return

        binding.tvDocumentMeta.text =
            "📄  ${document.pageCount}-page PDF  •  ${document.clauseCount} clauses found"

        binding.tvHighRiskCount.text = document.highRiskCount.toString()
        binding.tvMediumRiskCount.text = document.mediumRiskCount.toString()
        binding.tvInfoRiskCount.text = document.infoCount.toString()

        bindKeyPoints(document.keyPoints)
        
        binding.btnViewClauses.setOnClickListener {
            findNavController().navigate(
                R.id.action_summaryFragment_to_clauseListFragment
            )
        }

        binding.btnBack.setOnClickListener {
            // Navigate back to Home and clear the stack
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun bindKeyPoints(keyPoints: List<KeyPoint>) {

        binding.layoutKeyPointsContainer.removeAllViews()

        if (keyPoints.isEmpty()) {
            binding.cardKeyPoints.visibility = View.GONE
            return
        }

        binding.cardKeyPoints.visibility = View.VISIBLE

        val inflater = LayoutInflater.from(requireContext())

        keyPoints.forEachIndexed { index, keyPoint ->

            val row = inflater.inflate(
                R.layout.item_key_point,
                binding.layoutKeyPointsContainer,
                false
            )

            val tvLabel = row.findViewById<TextView>(R.id.tvKeyLabel)
            val tvValue = row.findViewById<TextView>(R.id.tvKeyValue)

            tvLabel.text = keyPoint.label
            tvValue.text = keyPoint.value

            // Remove bottom divider from last item
            if (index == keyPoints.lastIndex) {
                row.background = null
            }

            binding.layoutKeyPointsContainer.addView(row)
        }
    }

}