package jp.co.yumemi.android.code_check.ui.top

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.yumemi.android.code_check.model.GithubRepository
import jp.co.yumemi.android.code_check.ui.theme.CodeCheckTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositorySearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleRepository = GithubRepository(
        name = "test-repo",
        ownerIconUrl = "http://example.com",
        language = "kotlin",
        stargazersCount = 100,
        watchersCount = 50,
        forksCount = 25,
        openIssuesCount = 5,
    )

    @Test
    fun repositorySearchScreen_displays_waiting_input_state() {
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositorySearchScreen(
                    onRepositoryClick = {},
                    onSearch = {},
                    uiState = RepositorySearchUiState.WaitingInput
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("入力待機中です…").assertIsDisplayed()
    }

    @Test
    fun repositorySearchScreen_displays_error_state() {
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositorySearchScreen(
                    onRepositoryClick = {},
                    onSearch = {},
                    uiState = RepositorySearchUiState.Error
                )
            }
        }

        composeTestRule.onNodeWithText("エラーが発生しました").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("リトライ").assertIsDisplayed()
    }

    @Test
    fun repositorySearchScreen_displays_success_state_with_repositories() {
        val repositories = listOf(sampleRepository)
        
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositorySearchScreen(
                    onRepositoryClick = {},
                    onSearch = {},
                    uiState = RepositorySearchUiState.Success(repositories)
                )
            }
        }

        composeTestRule.onNodeWithText("test-repo").assertIsDisplayed()
    }

    @Test
    fun repositorySearchScreen_displays_success_state_with_empty_repositories() {
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositorySearchScreen(
                    onRepositoryClick = {},
                    onSearch = {},
                    uiState = RepositorySearchUiState.Success(emptyList())
                )
            }
        }

        composeTestRule.onNodeWithText("検索結果が見つかりませんでした").assertIsDisplayed()
    }

    @Test
    fun repositorySearchScreen_repository_click_triggers_callback() {
        var clickedRepository: GithubRepository? = null
        val repositories = listOf(sampleRepository)
        
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositorySearchScreen(
                    onRepositoryClick = { repository -> clickedRepository = repository },
                    onSearch = {},
                    uiState = RepositorySearchUiState.Success(repositories)
                )
            }
        }

        composeTestRule.onNodeWithText("test-repo").performClick()

        assert(clickedRepository == sampleRepository)
    }

    @Test
    fun repositorySearchScreen_retry_button_works() {
        var retryCallbackTriggered = false
        
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositorySearchScreen(
                    onRepositoryClick = {},
                    onSearch = { retryCallbackTriggered = true },
                    uiState = RepositorySearchUiState.Error
                )
            }
        }

        composeTestRule.onNodeWithText("リトライ").performClick()

        assert(retryCallbackTriggered)
    }
}