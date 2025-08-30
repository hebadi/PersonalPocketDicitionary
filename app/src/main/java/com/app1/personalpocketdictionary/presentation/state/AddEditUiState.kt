package com.app1.personalpocketdictionary.presentation.state

import com.app1.personalpocketdictionary.data.DictionaryData

/**
 * UI State for the Add/Edit Screen
 */
data class AddEditUiState(
    val word: String = "",
    val partOfSpeech: String = "",
    val definition: String = "",
    val example: String = "",
    val isLoading: Boolean = false,
    val loadedWordData: UiState<DictionaryData> = UiState.Idle,
    val saveResult: UiState<Unit> = UiState.Idle,
    val isEditMode: Boolean = false
) {
    val isFormValid: Boolean
        get() = word.isNotBlank() &&
                partOfSpeech.isNotBlank() &&
                definition.isNotBlank() &&
                example.isNotBlank()

    val isSaving: Boolean get() = saveResult.isLoading
    val hasError: Boolean get() = loadedWordData.isError || saveResult.isError
    val errorMessage: String? get() = loadedWordData.errorMessage ?: saveResult.errorMessage
}
