package jp.co.yumemi.android.code_check.ui.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.co.yumemi.android.code_check.OneFragmentDirections
import jp.co.yumemi.android.code_check.OneViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Item
import jp.co.yumemi.android.code_check.ui.theme.CodeCheckTheme

class RepositorySearchFragment: Fragment(R.layout.fragment_one) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = OneViewModel()
        fun gotoRepositoryFragment(item: Item) {
            val action = OneFragmentDirections
                .actionRepositoriesFragmentToRepositoryFragment(item = item)
            findNavController().navigate(action)
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CodeCheckTheme {
                    RepositorySearchScreen(
                        onRepositoryClick = { gotoRepositoryFragment(it) },
                        search = viewModel::searchResults
                    )
                }
            }
        }
    }
}

