package jp.co.yumemi.android.code_check.ui.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.yumemi.android.code_check.model.GithubRepository
import jp.co.yumemi.android.code_check.model.Owner
import jp.co.yumemi.android.code_check.ui.theme.CodeCheckTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryDetailScreenTest {

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
    fun repositoryDetailScreen_displays_repository_information() {
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositoryDetailScreen(
                    githubRepository = sampleRepository,
                    onBackClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("test-repo").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("100 stars").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("50 watchers").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("25 forks").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("5 open issues").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Written in kotlin").assertIsDisplayed()
    }

    @Test
    fun repositoryDetailScreen_displays_top_app_bar() {
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositoryDetailScreen(
                    githubRepository = sampleRepository,
                    onBackClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Repository Details").assertIsDisplayed()
        
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun repositoryDetailScreen_back_button_triggers_callback() {
        var backButtonClicked = false
        
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositoryDetailScreen(
                    githubRepository = sampleRepository,
                    onBackClick = { backButtonClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        
        assert(backButtonClicked)
    }

    @Test
    fun repositoryDetailScreen_displays_owner_information() {
        composeTestRule.setContent {
            CodeCheckTheme {
                RepositoryDetailScreen(
                    githubRepository = sampleRepository,
                    onBackClick = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Owner Icon").assertIsDisplayed()
    }

}