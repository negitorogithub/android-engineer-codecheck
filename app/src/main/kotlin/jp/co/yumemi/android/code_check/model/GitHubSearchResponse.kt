package jp.co.yumemi.android.code_check.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubSearchResponse(
    val items: List<GitHubRepositoryRaw>
)

@JsonClass(generateAdapter = true)
data class GitHubRepositoryRaw(
    @param:Json(name = "full_name") val fullName: String,
    val owner: Owner,
    val language: String?,
    @param:Json(name = "stargazers_count") val stargazersCount: Long,
    @param:Json(name = "watchers_count") val watchersCount: Long,
    @param:Json(name = "forks_count") val forksCount: Long,
    @param:Json(name = "open_issues_count") val openIssuesCount: Long,
)

@JsonClass(generateAdapter = true)
data class Owner(
    @param:Json(name = "avatar_url") val avatarUrl: String
)

// 拡張関数でGithubRepositoryに変換
fun GitHubRepositoryRaw.toGithubRepository(): GithubRepository {
    return GithubRepository(
        name = fullName,
        ownerIconUrl = owner.avatarUrl,
        language = language ?: "",
        stargazersCount = stargazersCount,
        watchersCount = watchersCount,
        forksCount = forksCount,
        openIssuesCount = openIssuesCount
    )
}