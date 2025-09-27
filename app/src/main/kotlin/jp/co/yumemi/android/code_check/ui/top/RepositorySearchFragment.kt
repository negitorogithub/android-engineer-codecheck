package jp.co.yumemi.android.code_check.ui.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.model.GithubRepository
import jp.co.yumemi.android.code_check.ui.theme.CodeCheckTheme

@AndroidEntryPoint
class RepositorySearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: RepositorySearchViewModel by viewModels()
        fun gotoRepositoryFragment(githubRepository: GithubRepository) {
            val action = RepositorySearchFragmentDirections
                .actionRepositoriesFragmentToRepositoryFragment(item = githubRepository)
            findNavController().navigate(action)
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val state by viewModel.uiState.collectAsState()
                CodeCheckTheme {
                    RepositorySearchScreen(
                        onRepositoryClick = { gotoRepositoryFragment(it) },
                        onSearch = viewModel::searchResults,
                        uiState = state
                    )
                }
            }
        }
    }
}

