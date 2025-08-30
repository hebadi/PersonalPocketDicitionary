package com.app1.personalpocketdictionary.presentation.state

import com.app1.personalpocketdictionary.data.DictionaryData

/**
 * UI State for the Item Detail Screen
 */
data class ItemDetailUiState(
    val word: UiState<DictionaryData> = UiState.Idle,
    val isDeleting: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val deleteResult: UiState<Unit> = UiState.Idle
) {
    val isLoading: Boolean get() = word.isLoading
    val hasError: Boolean get() = word.isError || deleteResult.isError
    val errorMessage: String? get() = word.errorMessage ?: deleteResult.errorMessage
}
