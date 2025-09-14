/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.repository.GitHubSearchRepository
import jp.co.yumemi.android.code_check.repository.GitHubSearchRepositoryResponse
import jp.co.yumemi.android.code_check.ui.top.RepositorySearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

@HiltViewModel
class RepositorySearchViewModel @Inject constructor(
    private val repository: GitHubSearchRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<RepositorySearchUiState> = MutableStateFlow(
        RepositorySearchUiState.WaitingInput
    )
    val uiState: StateFlow<RepositorySearchUiState> = _uiState

    // 検索結果
    fun searchResults(inputText: String) {
        viewModelScope.launch {
            _uiState.update { RepositorySearchUiState.Loading }
            val response = repository.search(inputText)
            lastSearchDate = Date()

            when (response) {
                is GitHubSearchRepositoryResponse.Error -> {
                    _uiState.update { RepositorySearchUiState.Error }
                }
                is GitHubSearchRepositoryResponse.Success -> {
                    val items = response.items
                    val result = RepositorySearchUiState.Success(items)
                    _uiState.update { result }

                }
            }
        }
    }
}
