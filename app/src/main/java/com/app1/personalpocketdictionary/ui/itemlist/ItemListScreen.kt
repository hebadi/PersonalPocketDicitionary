package com.app1.personalpocketdictionary.ui.itemlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.app1.personalpocketdictionary.R
import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.DictionaryViewModel
import com.app1.personalpocketdictionary.ui.addandedit.PreviewDictionaryViewModel
import com.app1.personalpocketdictionary.ui.composables.DictionaryListItem
import com.app1.personalpocketdictionary.ui.navigation.Screen
import com.app1.personalpocketdictionary.ui.theme.PersonalPocketDictionaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    viewModel: DictionaryViewModel,
    navController: NavController
) {
    val items: List<DictionaryData> by viewModel.allData.collectAsState()

    Scaffold(
        topBar = {
            // The original Fragment ItemFragment has options menu handled by the Fragment itself.
            // If a TopAppBar is desired here, it can be added, but menu inflation from XML
            // is typically managed at the Fragment/Activity level when mixing Compose and Views.
            // For simplicity, we'll assume the Fragment handles the options menu for "Dev Notes".
            // If a title is needed: TopAppBar(title = { Text("Dictionary") })
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
        Box(modifier = Modifier.padding(paddingValues)) {
            if (items.isEmpty()) {
                // Optional: Display a message when the list is empty
                Text(
                    text = stringResource(R.string.no_words_yet), // Assuming you have such a string
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
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
                            Divider() // Add a divider between items
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
            viewModel = PreviewDictionaryViewModel(), // Ensure this can provide some dummy data or empty list
            navController = NavController(LocalContext.current) // Dummy NavController
        )
    }
}
