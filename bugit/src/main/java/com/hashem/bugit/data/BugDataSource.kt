package com.hashem.bugit.data

interface BugDataSource {

    suspend fun report(imagePath: String, fields: Map<String, String>): BugData
}