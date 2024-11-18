package com.hashem.bugit.domain

internal interface BugitRepository {

    suspend fun report(bug: Bug)
}