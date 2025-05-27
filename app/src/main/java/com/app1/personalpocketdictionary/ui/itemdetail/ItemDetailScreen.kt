package com.app1.personalpocketdictionary.ui.itemdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app1.personalpocketdictionary.R
import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.DictionaryViewModel
import com.app1.personalpocketdictionary.fragments.ItemDetailFragmentDirections // For navigation
import com.app1.personalpocketdictionary.ui.addandedit.PreviewDictionaryViewModel // For preview
import com.app1.personalpocketdictionary.ui.theme.PersonalPocketDictionaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    viewModel: DictionaryViewModel,
    itemId: Int,
    navController: NavController
) {
    val context = LocalContext.current
    val itemState = viewModel.retrieveData(itemId).observeAsState()
    val item = itemState.value

    var showDeleteDialog by remember { mutableStateOf(false) }

    fun googleIt(word: String?) {
        if (word == null) return
        val searchPrefix = "https://www.google.com/search?q="
        val queryUrl: Uri = Uri.parse("${searchPrefix}${word}+definition")
        val intent = Intent(Intent.ACTION_VIEW, queryUrl)
        context.startActivity(intent)
    }

    fun editItem(currentItem: DictionaryData?) {
        currentItem ?: return
        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddAndEditFragment(
            context.getString(R.string.edit_fragment_title),
            currentItem.id
        )
        navController.navigate(action)
    }

    fun deleteItem(currentItem: DictionaryData?) {
        currentItem ?: return
        viewModel.deleteWord(currentItem)
        navController.navigateUp()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.word?.uppercase() ?: stringResource(R.string.title_item_detail)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.navigate_up))
                    }
                }
            )
        },
        floatingActionButton = {
            if (item != null) { // Show FAB only if item is loaded
                FloatingActionButton(onClick = { editItem(item) }) {
                    Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit_item))
                }
            }
        }
    ) { paddingValues ->
        if (item == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                //CircularProgressIndicator() // Or some other loading state
                Text(stringResource(R.string.loading_item))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp) // Corresponds to @dimen/margin
                    .verticalScroll(rememberScrollState())
            ) {
                // Word (already in TopAppBar title, but can be repeated if design requires)
                // Text(
                //     text = item.word.uppercase(),
                //     style = MaterialTheme.typography.headlineMedium, // Or similar to 28sp
                //     modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp) // @dimen/margin_between_elements
                // )
                // Divider() // Corresponds to big_divider

                DetailRow(label = stringResource(R.string.part_of_speech), value = item.partOfSpeech)
                CustomDivider()
                DetailRow(label = stringResource(R.string.definition), value = item.definition)
                CustomDivider()
                DetailRow(label = stringResource(R.string.example), value = item.example ?: "") // Handle null example

                Spacer(Modifier.height(32.dp)) // Space before action buttons

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly, // Distributes space
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showDeleteDialog = true }, modifier = Modifier.size(56.dp) /* Approx FAB size */) {
                        Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete_item), tint = MaterialTheme.colorScheme.error)
                    }
                    IconButton(onClick = { googleIt(item.word) }, modifier = Modifier.size(64.dp) /* Larger for central button */ ) {
                        Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.google_the_term), modifier = Modifier.size(36.dp))
                    }
                    // Edit FAB is the main FAB for the screen
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(android.R.string.dialog_alert_title)) },
            text = { Text(stringResource(R.string.delete_question)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        deleteItem(item)
                    }
                ) { Text(stringResource(R.string.yes)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text(stringResource(R.string.no)) }
            }
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) { // @dimen/margin_between_elements
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp) // Indent value slightly or style as needed
        )
    }
}

@Composable
fun CustomDivider() {
    Divider(modifier = Modifier.padding(vertical = 8.dp)) // @dimen/margin_between_elements
}

@Preview(showBackground = true)
@Composable
fun ItemDetailScreenPreview() {
    PersonalPocketDictionaryTheme {
        // This preview will show the loading state or an empty state if PreviewDictionaryViewModel doesn't emit.
        // For a full preview, PreviewDictionaryViewModel would need to be enhanced or a specific item passed.
        val previewViewModel = PreviewDictionaryViewModel()
        // Simulate loading an item for preview - in a real app, this comes from LiveData
        val sampleItem = DictionaryData(1, "Preview Word", "Noun", "This is a preview definition.", "This is a preview example.")
        
        // A trick to make the preview work with the current setup:
        // Directly pass a sample item to a modified Composable, or make retrieveData in Preview VM return static LiveData.
        // For now, the screen handles null item state.
        
        ItemDetailScreen(
            viewModel = previewViewModel,
            itemId = 1,
            navController = NavController(LocalContext.current)
        )
    }
}
