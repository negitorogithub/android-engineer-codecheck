package jp.co.yumemi.android.code_check.ui.top

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import jp.co.yumemi.android.code_check.model.GithubRepository
import jp.co.yumemi.android.code_check.repository.GitHubSearchRepository
import jp.co.yumemi.android.code_check.repository.GitHubSearchRepositoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositorySearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockRepository: GitHubSearchRepository
    private lateinit var viewModel: RepositorySearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        viewModel = RepositorySearchViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `初期状態はWaitingInputである`() {
        val currentState = viewModel.uiState.value

        // Assert
        assertTrue(currentState is RepositorySearchUiState.WaitingInput)
    }

    @Test
    fun `searchResults成功時にSuccessStateになる`() = runTest {
        // Arrange
        val query = "kotlin"
        val expectedRepositories = listOf(
            GithubRepository(
                name = "JetBrains/kotlin",
                ownerIconUrl = "https://avatars.githubusercontent.com/u/878437?v=4",
                language = "Kotlin",
                stargazersCount = 38530,
                watchersCount = 38530,
                forksCount = 4675,
                openIssuesCount = 131
            )
        )
        val successResponse = GitHubSearchRepositoryResponse.Success(expectedRepositories)

        coEvery { mockRepository.search(query) } returns successResponse

        // Act
        viewModel.searchResults(query)
        
        // Wait for coroutine to complete
        advanceUntilIdle()

        // Assert
        val finalState = viewModel.uiState.value
        assertTrue(finalState is RepositorySearchUiState.Success)
        val successState = finalState as RepositorySearchUiState.Success
        assertEquals(expectedRepositories, successState.repositories)
    }

    @Test
    fun `searchResultsエラー時にErrorStateになる`() = runTest {
        // Arrange
        val query = "kotlin"
        val errorResponse = GitHubSearchRepositoryResponse.Error

        coEvery { mockRepository.search(query) } returns errorResponse

        // Act
        viewModel.searchResults(query)
        
        // Wait for coroutine to complete
        advanceUntilIdle()

        // Assert
        val finalState = viewModel.uiState.value
        assertTrue(finalState is RepositorySearchUiState.Error)
        
    }

    @Test
    fun `空の検索結果でもSuccessStateになる`() = runTest {
        // Arrange
        val query = "nonExistentRepo"
        val emptyResponse = GitHubSearchRepositoryResponse.Success(emptyList())

        coEvery { mockRepository.search(query) } returns emptyResponse

        // Act
        viewModel.searchResults(query)
        advanceUntilIdle()

        // Assert
        val finalState = viewModel.uiState.value
        assertTrue(finalState is RepositorySearchUiState.Success)
        val successState = finalState as RepositorySearchUiState.Success
        assertTrue(successState.repositories.isEmpty())
    }

    @Test
    fun `複数回の検索で状態が正しく更新される`() = runTest {
        // Arrange
        val firstQuery = "kotlin"
        val secondQuery = "java"
        
        val firstResult = listOf(
            GithubRepository(
                name = "JetBrains/kotlin",
                ownerIconUrl = "https://example.com/kotlin.png",
                language = "Kotlin",
                stargazersCount = 1000,
                watchersCount = 1000,
                forksCount = 100,
                openIssuesCount = 10
            )
        )
        
        val secondResult = listOf(
            GithubRepository(
                name = "openjdk/jdk",
                ownerIconUrl = "https://example.com/java.png",
                language = "Java",
                stargazersCount = 2000,
                watchersCount = 2000,
                forksCount = 200,
                openIssuesCount = 20
            )
        )

        coEvery { mockRepository.search(firstQuery) } returns GitHubSearchRepositoryResponse.Success(firstResult)
        coEvery { mockRepository.search(secondQuery) } returns GitHubSearchRepositoryResponse.Success(secondResult)

        // Act & Assert - First search
        viewModel.searchResults(firstQuery)
        advanceUntilIdle()
        
        val firstState = viewModel.uiState.value
        assertTrue(firstState is RepositorySearchUiState.Success)
        assertEquals(firstResult, (firstState as RepositorySearchUiState.Success).repositories)

        // Act & Assert - Second search
        viewModel.searchResults(secondQuery)
        advanceUntilIdle()
        
        val secondState = viewModel.uiState.value
        assertTrue(secondState is RepositorySearchUiState.Success)
        assertEquals(secondResult, (secondState as RepositorySearchUiState.Success).repositories)
    }
}