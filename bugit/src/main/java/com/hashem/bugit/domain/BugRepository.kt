package com.hashem.bugit.domain

internal interface BugRepository {

    suspend fun report(imagePath: String, fields: Map<String, String>): Bug
}