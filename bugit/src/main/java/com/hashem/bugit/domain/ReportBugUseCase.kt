package com.hashem.bugit.domain

internal class ReportBugUseCase(private val repository: BugItRepository) {

    suspend fun report(bug: Bug) {
        repository.report(bug)
    }
}