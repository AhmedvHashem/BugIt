package com.hashem.bugit.domain

import org.json.JSONObject

internal interface BugItRepository {

    suspend fun report(bug: Bug): JSONObject
}