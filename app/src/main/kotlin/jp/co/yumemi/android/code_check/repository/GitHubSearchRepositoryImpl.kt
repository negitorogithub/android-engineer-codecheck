package jp.co.yumemi.android.code_check.repository

import android.util.Log
import com.squareup.moshi.Moshi
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import jakarta.inject.Inject
import jp.co.yumemi.android.code_check.model.GitHubSearchResponse
import jp.co.yumemi.android.code_check.model.toGithubRepository

class GitHubSearchRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val moshi: Moshi
) : GitHubSearchRepository {
    override suspend fun search(query: String): GitHubSearchRepositoryResponse {
        return runCatching {
            val response: HttpResponse = httpClient.get("https://api.github.com/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", query)
            }

            if (!response.status.isSuccess()) {
                throw IllegalStateException("API call failed with status: ${response.status}")
            }

            val jsonString = response.receive<String>()
            val adapter = moshi.adapter(GitHubSearchResponse::class.java)
            val searchResponse = adapter.fromJson(jsonString)
                ?: throw IllegalStateException("Failed to parse JSON response")

            val githubRepositories = searchResponse.items.map { it.toGithubRepository() }
            githubRepositories
        }.fold(
            onSuccess = { items ->
                GitHubSearchRepositoryResponse.Success(items)
            },
            onFailure = { throwable ->
                Log.e("API Call Error", throwable.toString())
                GitHubSearchRepositoryResponse.Error
            }
        )
    }
}