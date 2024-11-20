package com.hashem.bugit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hashem.bugit.BugIt
import com.hashem.bugit.data.DefaultBugRepository
import com.hashem.bugit.domain.ReportBugUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class BugItViewModel(
    val initialFields: Map<String, String> = emptyMap(),
    private val reportBugUseCase: ReportBugUseCase
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bugitConfig = BugIt.getInstance().config

                val repo = DefaultBugRepository(bugitConfig.connector)
                val useCase = ReportBugUseCase(repo)
                BugItViewModel(bugitConfig.fields, useCase)
            }
        }
    }

    private val _uiState by lazy { MutableStateFlow<BugItUIState>(BugItUIState.Initial) }
    val uiState: StateFlow<BugItUIState> get() = _uiState.asStateFlow()

    fun reportBug(imagePath: String, fields: Map<String, String> = emptyMap()) {
        viewModelScope.launch {
            _uiState.value = BugItUIState.Loading
            try {
                val response = reportBugUseCase(imagePath, fields)
                _uiState.value = BugItUIState.Success("Bug Reported!")
                println("response: $response")
            } catch (e: Exception) {
                _uiState.value = BugItUIState.Error("Failed to report bug")
                println("error: $e")
            }
        }
    }
}