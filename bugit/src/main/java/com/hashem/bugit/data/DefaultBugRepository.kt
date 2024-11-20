package com.hashem.bugit.data

import com.hashem.bugit.domain.Bug
import com.hashem.bugit.domain.BugRepository

internal class DefaultBugRepository(private val dataSource: BugDataSource) : BugRepository {

    override suspend fun report(imagePath: String, fields: Map<String, String>): Bug {
        return dataSource.report(imagePath, fields).toBug()
    }
}