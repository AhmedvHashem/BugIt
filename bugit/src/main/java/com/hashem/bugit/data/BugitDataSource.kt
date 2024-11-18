package com.hashem.bugit.data

interface BugitDataSource {

    suspend fun report(screenShot: String, fields: Map<String, String>)
}