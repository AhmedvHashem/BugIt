package com.hashem.bugit.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ReportBugUseCase(
    private val repository: BugRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(imagePath: String, fields: Map<String, String>): Bug {
        return withContext(defaultDispatcher) {
            repository.report(imagePath, fields)
        }
    }
}