package com.hashem.bugit.data

import com.hashem.bugit.domain.Bug
import com.hashem.bugit.domain.BugItRepository
import org.json.JSONObject

internal class DefaultBugItRepository(private val dataSource: BugItDataSource) : BugItRepository {

    override suspend fun report(bug: Bug): JSONObject {
        return dataSource.report(bug.imagePath, bug.fields)
    }
}