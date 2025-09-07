package jp.co.yumemi.android.code_check.ui.top

import jp.co.yumemi.android.code_check.model.Item

sealed class RepositorySearchUiState {
    object WaitingInput : RepositorySearchUiState()
    object Loading : RepositorySearchUiState()
    object Error : RepositorySearchUiState()
    data class Success(val repositories: List<Item>) : RepositorySearchUiState()
}