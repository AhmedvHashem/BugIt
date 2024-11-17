package com.hashem.bugit.domain

interface BugitRepository {

    fun report(bug: Bug)
}