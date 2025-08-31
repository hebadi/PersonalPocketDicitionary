package com.app1.personalpocketdictionary.ui.itemlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app1.personalpocketdictionary.R
import com.app1.personalpocketdictionary.presentation.state.SortOrder
import com.app1.personalpocketdictionary.presentation.state.data
import com.app1.personalpocketdictionary.presentation.viewmodel.ModernDictionaryViewModel
import com.app1.personalpocketdictionary.ui.composables.DictionaryListItem
import com.app1.personalpocketdictionary.ui.navigation.Screen
import com.app1.personalpocketdictionary.ui.theme.PersonalPocketDictionaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    viewModel: ModernDictionaryViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val items =
        uiState.filteredWords.takeIf { uiState.searchQuery.isNotBlank() } ?: (uiState.words.data
            ?: emptyList())

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.dictionary_title)) },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = stringResource(R.string.menu_description)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.sort_a_to_z)) },
                            onClick = {
                                viewModel.sortWords(SortOrder.ALPHABETICAL)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.sort_z_to_a)) },
                            onClick = {
                                viewModel.sortWords(SortOrder.REVERSE_ALPHABETICAL)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.sort_by_date_added)) },
                            onClick = {
                                viewModel.sortWords(SortOrder.DATE_ADDED)
                                showMenu = false
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.dev_notes)) },
                            onClick = {
                                navController.navigate(Screen.DevNotes.route)
                                showMenu = false
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val route = Screen.AddAndEditItem.createRoute()
                    navController.navigate(route)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_word_title))
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Table Header like in your original design
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.15f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "V O C A B U L A R Y",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.weight(0.85f),
                    textAlign = TextAlign.Center
                )
            }

            HorizontalDivider()

            // Content
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_words_added_yet),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            } else {
                LazyColumn {
                    itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                        DictionaryListItem(
                            itemData = item,
                            itemNumber = index + 1,
                            onItemClick = {
                                val route = Screen.ItemDetail.createRoute(item.id)
                                navController.navigate(route)
                            }
                        )
                        if (index < items.lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemListScreenPreview() {
    PersonalPocketDictionaryTheme {
        // Using PreviewDictionaryViewModel which is a simplified version for previews
        ItemListScreen(
            viewModel = com.app1.personalpocketdictionary.ui.addandedit.createPreviewViewModel(), // Ensure this can provide some dummy data or empty list
            navController = NavController(LocalContext.current) // Dummy NavController
        )
    }
}
