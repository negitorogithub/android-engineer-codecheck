package jp.co.yumemi.android.code_check.ui.top

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Item
import jp.co.yumemi.android.code_check.ui.theme.CodeCheckTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositorySearchScreen(
    onRepositoryClick: (Item) -> Unit,
    onSearch: (String) -> Unit,
    uiState: RepositorySearchUiState
) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    // Search Bar
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 4.dp),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.searchInputText_hint),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search_content_description),
                                    tint = Color.Gray
                                )
                            },
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = { searchText = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = stringResource(R.string.clear_content_description),
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (searchText.isNotEmpty()) {
                                        onSearch(searchText)
                                        keyboardController?.hide()
                                    }
                                }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                            ),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Content based on UI State
                    when (uiState) {
                        is RepositorySearchUiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is RepositorySearchUiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = stringResource(R.string.error_message),
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { onSearch(searchText) },
                                        modifier = Modifier.width(120.dp)
                                    ) {
                                        Text(text = stringResource(R.string.retry))
                                    }
                                }

                            }
                        }

                        is RepositorySearchUiState.Success -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                items(uiState.repositories) { repository ->
                                    RepositoryItem(
                                        item = repository,
                                        onClick = { onRepositoryClick(repository) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        })
}

@Composable
fun RepositoryItem(
    item: Item,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 12.sp,
        )
    }
}

@PreviewLightDark
@Composable
fun RepositorySearchScreenPreview() {
    CodeCheckTheme {
        RepositorySearchScreen(
            onRepositoryClick = { },
            onSearch = { },
            uiState = RepositorySearchUiState.Success(
                repositories = (1..10).map {
                    Item(
                        name = "JetBrains/compose",
                        ownerIconUrl = "",
                        language = "",
                        stargazersCount = 0,
                        watchersCount = 0,
                        forksCount = 0,
                        openIssuesCount = 0,
                    )
                }
            )
        )
    }
}

@PreviewLightDark
@Composable
fun RepositoryItemPreview() {
    CodeCheckTheme {
        RepositoryItem(
            item = Item(
                name = "JetBrains/compose",
                ownerIconUrl = "",
                language = "",
                stargazersCount = 0,
                watchersCount = 0,
                forksCount = 0,
                openIssuesCount = 0,
            ),
            onClick = {}
        )
    }
}



@PreviewLightDark
@Composable
fun RepositorySearchScreenLoadingPreview() {
    CodeCheckTheme {
        RepositorySearchScreen(
            onRepositoryClick = { },
            onSearch = { },
            uiState = RepositorySearchUiState.Loading
        )
    }
}

@PreviewLightDark
@Composable
fun RepositorySearchScreenErrorPreview() {
    CodeCheckTheme {
        RepositorySearchScreen(
            onRepositoryClick = { },
            onSearch = { },
            uiState = RepositorySearchUiState.Error
        )
    }
}
