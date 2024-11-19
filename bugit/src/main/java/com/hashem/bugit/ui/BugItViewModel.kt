package com.hashem.bugit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hashem.bugit.BugIt
import com.hashem.bugit.data.DefaultBugItRepository
import com.hashem.bugit.domain.Bug
import com.hashem.bugit.domain.ReportBugUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class BugItViewModel(
    val allowMultipleImage: Boolean = false,
    val initialFields: Map<String, String> = emptyMap(),
    private val reportBugUseCase: ReportBugUseCase
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bugitConfig = BugIt.getInstance().config

                val repo = DefaultBugItRepository(bugitConfig.connector)
                val useCase = ReportBugUseCase(repo)
                BugItViewModel(bugitConfig.allowMultipleImage, bugitConfig.fields, useCase)
            }
        }
    }

//    private val _uiState = MutableStateFlow(initialFields)
//    val uiState: StateFlow<Map<String, String>> = _uiState.asStateFlow()

    fun reportBug(imagePath: String, fields: Map<String, String> = emptyMap()) {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                reportBugUseCase.report(Bug(imagePath, fields))
            }
        }
    }
}