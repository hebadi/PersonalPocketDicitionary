package com.app1.personalpocketdictionary.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app1.personalpocketdictionary.domain.usecase.AddWordUseCase
import com.app1.personalpocketdictionary.domain.usecase.DeleteWordUseCase
import com.app1.personalpocketdictionary.domain.usecase.GetAllWordsUseCase
import com.app1.personalpocketdictionary.domain.usecase.GetWordByIdUseCase
import com.app1.personalpocketdictionary.domain.usecase.UpdateWordUseCase
import com.app1.personalpocketdictionary.presentation.state.ItemListUiState
import com.app1.personalpocketdictionary.presentation.state.UiState
import com.app1.personalpocketdictionary.presentation.state.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Modern ViewModel following Clean Architecture principles.
 * Uses Hilt for dependency injection and proper state management with UiState.
 */
@HiltViewModel
class ModernDictionaryViewModel @Inject constructor(
    private val getAllWordsUseCase: GetAllWordsUseCase,
    private val getWordByIdUseCase: GetWordByIdUseCase,
    private val addWordUseCase: AddWordUseCase,
    private val updateWordUseCase: UpdateWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemListUiState())
    val uiState: StateFlow<ItemListUiState> = _uiState.asStateFlow()

    init {
        loadAllWords()
    }

    private fun loadAllWords() {
        _uiState.value = _uiState.value.copy(words = UiState.Loading)

        getAllWordsUseCase()
            .onEach { words ->
                _uiState.value = _uiState.value.copy(
                    words = UiState.Success(words),
                    filteredWords = filterWords(words, _uiState.value.searchQuery)
                )
            }
            .catch { exception ->
                _uiState.value = _uiState.value.copy(
                    words = UiState.Error(exception)
                )
            }
            .launchIn(viewModelScope)
    }

    fun addWord(word: String, partOfSpeech: String, definition: String, example: String) {
        viewModelScope.launch {
            addWordUseCase(word, partOfSpeech, definition, example)
                .onSuccess {
                    // Word added successfully, loadAllWords will automatically update the UI
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        words = UiState.Error(exception)
                    )
                }
        }
    }

    fun updateWord(
        id: Int,
        word: String,
        partOfSpeech: String,
        definition: String,
        example: String
    ) {
        viewModelScope.launch {
            updateWordUseCase(id, word, partOfSpeech, definition, example)
                .onSuccess {
                    // Word updated successfully
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        words = UiState.Error(exception)
                    )
                }
        }
    }

    fun deleteWord(word: com.app1.personalpocketdictionary.data.DictionaryData) {
        viewModelScope.launch {
            deleteWordUseCase(word)
                .onSuccess {
                    // Word deleted successfully
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        words = UiState.Error(exception)
                    )
                }
        }
    }

    fun searchWords(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredWords = filterWords(_uiState.value.words.data ?: emptyList(), query)
        )
    }

    private fun filterWords(
        words: List<com.app1.personalpocketdictionary.data.DictionaryData>,
        query: String
    ): List<com.app1.personalpocketdictionary.data.DictionaryData> {
        return if (query.isBlank()) {
            words
        } else {
            words.filter {
                it.word.contains(query, ignoreCase = true) ||
                        it.definition.contains(query, ignoreCase = true) ||
                        it.partOfSpeech.contains(query, ignoreCase = true)
            }
        }
    }

    // Method to get a word by ID (for detail screen)
    fun getWordById(id: Int) = getWordByIdUseCase(id)
        .catch { exception ->
            emit(com.app1.personalpocketdictionary.data.DictionaryData(0, "", "", "", ""))
        }
}
