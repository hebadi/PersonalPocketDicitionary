package com.app1.personalpocketdictionary.presentation.state

import com.app1.personalpocketdictionary.data.DictionaryData

/**
 * UI State for the Item List Screen
 */
data class ItemListUiState(
    val words: UiState<List<DictionaryData>> = UiState.Idle,
    val searchQuery: String = "",
    val filteredWords: List<DictionaryData> = emptyList(),
    val sortOrder: SortOrder = SortOrder.DATE_ADDED
) {
    val isLoading: Boolean get() = words.isLoading
    val isEmpty: Boolean get() = words.data?.isEmpty() ?: false
    val hasError: Boolean get() = words.isError
    val errorMessage: String? get() = words.errorMessage
}
