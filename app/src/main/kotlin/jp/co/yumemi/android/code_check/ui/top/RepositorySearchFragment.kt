package jp.co.yumemi.android.code_check.ui.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.ui.theme.CodeCheckTheme

class RepositorySearchFragment: Fragment(R.layout.fragment_one) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CodeCheckTheme {
                    RepositorySearchScreen(
                        onRepositoryClick = {  },
                        search = {emptyList()}
                    )
                }
            }
        }
    }
}