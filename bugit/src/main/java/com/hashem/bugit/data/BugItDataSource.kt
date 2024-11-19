package com.hashem.bugit.data

import org.json.JSONObject

interface BugItDataSource {

    suspend fun report(imagePath: String, fields: Map<String, String>): JSONObject
}