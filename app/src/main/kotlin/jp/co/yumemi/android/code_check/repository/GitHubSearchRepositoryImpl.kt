package jp.co.yumemi.android.code_check.repository

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import jakarta.inject.Inject
import jp.co.yumemi.android.code_check.model.GithubRepository
import org.json.JSONObject

class GitHubSearchRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
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

            response.toItems()
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

suspend fun HttpResponse.toItems(): List<GithubRepository> {
    val jsonBody = JSONObject(this.receive<String>())
    val jsonItems = jsonBody.optJSONArray("items")
    if (jsonItems == null) throw Exception("Invalid JSON")
    val githubRepositories = mutableListOf<GithubRepository>()


    /**
     * アイテムの個数分ループする
     */
    for (i in 0 until jsonItems.length()) {
        val jsonItem = jsonItems.optJSONObject(i)
        val name = jsonItem.optString("full_name")
        val ownerOptJSONObject = jsonItem.optJSONObject("owner")
        if (ownerOptJSONObject == null) throw Exception("Invalid JSON")
        val ownerIconUrl = ownerOptJSONObject.optString("avatar_url")
        val language = jsonItem.optString("language")
        val stargazersCount = jsonItem.optLong("stargazers_count")
        val watchersCount = jsonItem.optLong("watchers_count")
        val forksCount = jsonItem.optLong("forks_count")
        val openIssuesCount = jsonItem.optLong("open_issues_count")

        githubRepositories.add(
            GithubRepository(
                name = name,
                ownerIconUrl = ownerIconUrl,
                language = language,
                stargazersCount = stargazersCount,
                watchersCount = watchersCount,
                forksCount = forksCount,
                openIssuesCount = openIssuesCount
            )
        )
    }
    return githubRepositories
}