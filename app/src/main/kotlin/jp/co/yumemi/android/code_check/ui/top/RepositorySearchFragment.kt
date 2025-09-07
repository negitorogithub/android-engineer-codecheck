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
import androidx.navigation.fragment.findNavController
import jp.co.yumemi.android.code_check.RepositorySearchViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Item
import jp.co.yumemi.android.code_check.ui.theme.CodeCheckTheme

class RepositorySearchFragment: Fragment(R.layout.fragment_one) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = RepositorySearchViewModel()
        fun gotoRepositoryFragment(item: Item) {
            val action = RepositorySearchFragmentDirections
                .actionRepositoriesFragmentToRepositoryFragment(item = item)
            findNavController().navigate(action)
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val repositories by viewModel.searchResultsState.collectAsState()
                CodeCheckTheme {
                    RepositorySearchScreen(
                        onRepositoryClick = { gotoRepositoryFragment(it) },
                        onSearch = viewModel::searchResults,
                        repositories = repositories
                    )
                }
            }
        }
    }
}

