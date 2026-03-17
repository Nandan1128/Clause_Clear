package com.example.clauseclear.data.repository

import com.example.clauseclear.data.model.Clause
import com.example.clauseclear.data.model.ClauseComparison

class CompareRepository {

    fun compareClausesByTitle(
        clausesA: List<Clause>,
        clausesB: List<Clause>
    ): List<ClauseComparison> {

        val mapA = clausesA.associateBy { it.title.lowercase().trim() }
        val mapB = clausesB.associateBy { it.title.lowercase().trim() }

        val allTitles = (mapA.keys + mapB.keys).toSet()

        return allTitles.map { title ->
            ClauseComparison(
                title = title,
                clauseA = mapA[title],
                clauseB = mapB[title]
            )
        }
    }
}