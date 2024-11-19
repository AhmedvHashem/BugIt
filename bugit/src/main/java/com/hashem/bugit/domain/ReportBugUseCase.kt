package com.hashem.bugit.domain

import org.json.JSONObject

internal class ReportBugUseCase(private val repository: BugItRepository) {

    suspend fun report(bug: Bug): JSONObject {
       return repository.report(bug)
    }
}