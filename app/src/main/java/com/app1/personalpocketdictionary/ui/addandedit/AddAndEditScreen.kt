package com.app1.personalpocketdictionary.ui.addandedit

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app1.personalpocketdictionary.R
import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.DictionaryViewModel
import com.app1.personalpocketdictionary.fragments.AddAndEditFragmentDirections // May need to adjust if NavController is used directly
import com.app1.personalpocketdictionary.ui.theme.PersonalPocketDictionaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAndEditScreen(
    viewModel: DictionaryViewModel,
    navController: NavController,
    itemId: Int? // Null for Add, non-null for Edit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // State for text fields
    var wordText by remember { mutableStateOf("") }
    var speechText by remember { mutableStateOf("") }
    var definitionText by remember { mutableStateOf("") }
    var exampleText by remember { mutableStateOf("") }

    // If itemId is provided (Edit mode), observe the LiveData for the item
    if (itemId != null && itemId > 0) {
        val itemState by viewModel.retrieveData(itemId).collectAsState(initial = null)
        LaunchedEffect(itemState) { // Use LaunchedEffect to update states when itemState changes
            itemState?.let { item ->
                wordText = item.word
                speechText = item.partOfSpeech
                definitionText = item.definition
                exampleText = item.example ?: ""
            }
        }
    }

    fun hideKeyboard() {
        focusManager.clearFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow((context as? android.app.Activity)?.currentFocus?.windowToken, 0)
    }

    fun handleSaveChanges() {
        if (viewModel.isEntryValid(wordText, speechText, definitionText, exampleText)) {
            if (itemId != null && itemId > 0) {
                viewModel.updateItem(itemId, wordText, speechText, definitionText, exampleText)
            } else {
                viewModel.addNewItem(wordText, speechText, definitionText, exampleText)
            }
            // Navigate back or to the list screen
            // The original fragment used AddAndEditFragmentDirections.actionAddAndEditFragmentToItemFragment()
            // This assumes that the current destination in the NavGraph is the one that can perform this action.
            navController.navigate(R.id.action_addAndEditFragment_to_itemFragment)

        } else {
            hideKeyboard()
            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (itemId != null && itemId > 0) stringResource(R.string.edit_word_title) else stringResource(R.string.add_word_title)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                hideKeyboard()
                handleSaveChanges()
            }) {
                Text(stringResource(R.string.save_action)) // Using Text for FAB, can be Icon
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = wordText,
                onValueChange = { wordText = it },
                label = { Text(stringResource(R.string.word_to_learn)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = speechText,
                onValueChange = { speechText = it },
                label = { Text(stringResource(R.string.paraphrase_speech)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = definitionText,
                onValueChange = { definitionText = it },
                label = { Text(stringResource(R.string.paraphrase_definition)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true // Original was multiline, but XML had singleLine="true"
            )

            OutlinedTextField(
                value = exampleText,
                onValueChange = { exampleText = it },
                label = { Text(stringResource(R.string.paraphrase_example)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true // Original was multiline, but XML had singleLine="true"
            )

            // The FAB replaces the explicit save button from the XML
            // If a button within the scrollable content is still desired:
            /*
            Button(
                onClick = {
                    hideKeyboard()
                    handleSaveChanges()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_action))
            }
            */
        }
    }
}

// Dummy ViewModel for Preview - In a real scenario, you might use a mock or a simple implementation.
class PreviewDictionaryViewModel : DictionaryViewModel(null) { // Assuming null DAO is acceptable for preview or mock it
    override fun isEntryValid(word: String, speech: String, definition: String, example: String): Boolean = true
    override fun addNewItem(word: String, speech: String, definition: String, example: String) {}
    override fun updateItem(id: Int, word: String, speech: String, definition: String, example: String) {}
    // You might need to return mock LiveData if your Composable directly observes it for initial population.
    // For simplicity, AddAndEditScreen takes initial values directly or fetches via LaunchedEffect.
}


@Preview(showBackground = true)
@Composable
fun AddScreenPreview() {
    PersonalPocketDictionaryTheme {
        AddAndEditScreen(
            viewModel = PreviewDictionaryViewModel(), // Use a preview-specific ViewModel or mock
            navController = NavController(LocalContext.current), // Dummy NavController
            itemId = null // For Add mode
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditScreenPreview() {
    // To effectively preview edit mode, you'd ideally have the ViewModel provide some data.
    // The current AddAndEditScreen uses LaunchedEffect to fetch, which works with a real ViewModel.
    // For preview, you might pass initial text values or enhance PreviewDictionaryViewModel.
    PersonalPocketDictionaryTheme {
        AddAndEditScreen(
            viewModel = PreviewDictionaryViewModel(),
            navController = NavController(LocalContext.current),
            itemId = 1 // For Edit mode
        )
        // Example of passing initial text directly if ViewModel isn't providing it for preview
        //LaunchedEffect(Unit){ wordText = "Sample Word" ... } // This would need state hoisting or direct params
    }
}
