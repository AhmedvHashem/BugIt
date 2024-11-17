package com.hashem.bugit.data

interface BugitDataSource {

    fun report(screenShot: String, fields: Map<String, String>)
}