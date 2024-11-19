package com.hashem.bugit.domain

internal interface BugItRepository {

    suspend fun report(bug: Bug)
}