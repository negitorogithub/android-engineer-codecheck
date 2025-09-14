package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.GithubRepository

sealed interface GitHubSearchRepositoryResponse {
    data object Error : GitHubSearchRepositoryResponse
    class Success(val githubRepositories: List<GithubRepository>) : GitHubSearchRepositoryResponse
}