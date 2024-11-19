package com.hashem.bugit.data

interface BugItDataSource {

    suspend fun report(imagePath: String, fields: Map<String, String>)
}