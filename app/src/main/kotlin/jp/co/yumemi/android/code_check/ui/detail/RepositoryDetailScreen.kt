/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.co.yumemi.android.code_check.Item
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.ui.theme.CodeCheckTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    item: Item,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repository Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Owner Icon
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.ownerIconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Owner Icon",
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.jetbrains),
                error = painterResource(id = R.drawable.jetbrains)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Repository Name
            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Repository Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Column - Language
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = if (item.language.isNotEmpty()) "Written in ${item.language}" else "Language not specified",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                
                // Right Column - Statistics
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    StatisticItem(
                        text = "${item.stargazersCount} stars",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    StatisticItem(
                        text = "${item.watchersCount} watchers",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    StatisticItem(
                        text = "${item.forksCount} forks",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    StatisticItem(
                        text = "${item.openIssuesCount} open issues",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 12.sp,
        textAlign = TextAlign.End,
        modifier = modifier
    )
}

@PreviewLightDark
@Composable
private fun RepositoryDetailScreenPreview() {
    val sampleItem = Item(
        name = "JetBrains/kotlin",
        ownerIconUrl = "https://avatars.githubusercontent.com/u/878437?v=4",
        language = "Kotlin",
        stargazersCount = 38530,
        watchersCount = 38530,
        forksCount = 4675,
        openIssuesCount = 131
    )
    CodeCheckTheme {
        RepositoryDetailScreen(item = sampleItem)
    }
}
