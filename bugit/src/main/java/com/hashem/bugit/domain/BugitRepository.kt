package com.hashem.bugit.domain

internal interface BugitRepository {

    fun report(bug: Bug)
}