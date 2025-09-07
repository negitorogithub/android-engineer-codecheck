package jp.co.yumemi.android.code_check.repository

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import jp.co.yumemi.android.code_check.model.Item
import org.json.JSONObject

class GitHubSearchRepositoryImpl : GitHubSearchRepository {
    override suspend fun search(query: String): GitHubSearchRepositoryResponse {
        val client = HttpClient(Android)
        val response: HttpResponse =
            client.get("https://api.github.com/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", query)
            }

        if (!response.status.isSuccess()) {
            Log.e("Search Repositories Error", response.status.toString())
            return GitHubSearchRepositoryResponse.Error
        }

        try {
            val items = response.toItems()
            return GitHubSearchRepositoryResponse.Success(items)
        } catch (e: Exception) {
            Log.e("JSON Parsing Error", e.toString())
            return GitHubSearchRepositoryResponse.Error
        }
    }
}

suspend fun HttpResponse.toItems(): List<Item> {
    val jsonBody = JSONObject(this.receive<String>())
    val jsonItems = jsonBody.optJSONArray("items")
    if (jsonItems == null) throw Exception("Invalid JSON")
    val items = mutableListOf<Item>()


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

        items.add(
            Item(
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
    return items
}