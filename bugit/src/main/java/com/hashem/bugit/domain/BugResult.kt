package com.hashem.bugit.domain

sealed class BugResult<out R> {
    data class Success<out T>(val data: T) : BugResult<T>()
    data class Error(val exception: Exception) : BugResult<Nothing>()
}