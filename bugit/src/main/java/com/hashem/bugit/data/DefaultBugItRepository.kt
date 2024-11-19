package com.hashem.bugit.data

import com.hashem.bugit.domain.Bug
import com.hashem.bugit.domain.BugitRepository

internal class DefaultBugItRepository(private val dataSource: BugitDataSource) : BugitRepository {

    override suspend fun report(bug: Bug) {
        dataSource.report(bug.image, bug.fields)
    }
}