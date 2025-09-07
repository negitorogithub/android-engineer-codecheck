/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.model.Item
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Date

/**
 * TwoFragment で使う
 */
class RepositorySearchViewModel() : ViewModel() {

    private val _searchResultsState: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())
    val searchResultsState: StateFlow<List<Item>> = _searchResultsState

    // 検索結果
    fun searchResults(inputText: String) {
        val client = HttpClient(Android)
        viewModelScope.launch {
            val result = viewModelScope.async {
                try {
                    val response: HttpResponse =
                        client.get("https://api.github.com/search/repositories") {
                            header("Accept", "application/vnd.github.v3+json")
                            parameter("q", inputText)
                        }

                    if (!response.status.isSuccess()) {
                        Log.e("Search Repositories Error", response.status.toString())
                        return@async emptyList()
                    }


                    val jsonBody = JSONObject(response.receive<String>())
                    val jsonItems = jsonBody.optJSONArray("items")
                    if (jsonItems == null) throw Exception("jsonItems is null")
                    val items = mutableListOf<Item>()


                    /**
                     * アイテムの個数分ループする
                     */
                    for (i in 0 until jsonItems.length()) {
                        val jsonItem = jsonItems.optJSONObject(i)
                        val name = jsonItem.optString("full_name")
                        val ownerOptJSONObject = jsonItem.optJSONObject("owner")
                        if (ownerOptJSONObject == null) throw Exception("ownerOptJSONObject is null")
                        val ownerIconUrl = ownerOptJSONObject.optString("avatar_url")
                        val language = jsonItem.optString("language")
                        val stargazersCount = jsonItem.optLong("stargazers_count")
                        val watchersCount = jsonItem.optLong("watchers_count")
                        val forksCount = jsonItem.optLong("forks_conut")
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
                    lastSearchDate = Date()
                    return@async items.toList()
                } catch (e: Exception) {
                    Log.e("Search Repositories Error", e.toString())
                    throw e
                } finally {
                    client.close()
                }
            }.await()
            _searchResultsState.update { result }
        }
    }
}
