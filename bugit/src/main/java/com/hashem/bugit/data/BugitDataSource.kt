package com.hashem.bugit.data

interface BugitDataSource {

    suspend fun report(image: String, fields: Map<String, String>)
}