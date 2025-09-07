package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.Item

sealed interface GitHubSearchRepositoryResponse {
    data object Error : GitHubSearchRepositoryResponse
    class Success(val items: List<Item>) : GitHubSearchRepositoryResponse
}