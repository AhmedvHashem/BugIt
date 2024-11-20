package com.hashem.bugit.ui

sealed class BugItUIState {
    data object Initial : BugItUIState()
    data object Loading : BugItUIState()
    data class Success(val message: String) : BugItUIState()
    data class Error(val message: String) : BugItUIState()
}
