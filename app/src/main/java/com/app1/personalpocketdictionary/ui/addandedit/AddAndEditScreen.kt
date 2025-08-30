package com.app1.personalpocketdictionary.ui.addandedit

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app1.personalpocketdictionary.R
import com.app1.personalpocketdictionary.data.DictionaryDao
import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.DictionaryViewModel
import com.app1.personalpocketdictionary.ui.navigation.Screen
import com.app1.personalpocketdictionary.ui.theme.PersonalPocketDictionaryTheme
import kotlinx.coroutines.flow.Flow

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
            // Navigate back to the list screen
            navController.navigate(Screen.ItemList.route) {
                popUpTo(Screen.ItemList.route) { inclusive = true }
            }

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

// Mock DAO for Preview
class MockDictionaryDao : DictionaryDao {
    override fun getAll(): Flow<List<DictionaryData>> = kotlinx.coroutines.flow.flowOf(emptyList())
    override fun getContents(id: Int): Flow<DictionaryData> = kotlinx.coroutines.flow.flowOf(
        DictionaryData(1, "Sample", "Noun", "A sample definition", "This is a sample.")
    )

    override fun getWordsList(): Flow<List<String>> = kotlinx.coroutines.flow.flowOf(emptyList())
    override suspend fun insert(word: DictionaryData) {}
    override suspend fun update(word: DictionaryData) {}
    override suspend fun delete(word: DictionaryData) {}
}

// Preview ViewModel using mock DAO
class PreviewDictionaryViewModel : DictionaryViewModel(MockDictionaryDao())


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
