package com.hashem.bugit.data

import com.hashem.bugit.domain.Bug
import com.hashem.bugit.domain.BugItRepository

internal class DefaultBugItRepository(private val dataSource: BugItDataSource) : BugItRepository {

    override suspend fun report(bug: Bug) {
        dataSource.report(bug.imagePath, bug.fields)
    }
}