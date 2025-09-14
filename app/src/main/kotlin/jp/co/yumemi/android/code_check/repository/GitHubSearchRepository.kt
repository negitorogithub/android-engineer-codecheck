package jp.co.yumemi.android.code_check.repository

interface GitHubSearchRepository {
    suspend fun search(query:String): GitHubSearchRepositoryResponse
}