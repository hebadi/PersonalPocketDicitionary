package com.app1.personalpocketdictionary.presentation.state

/**
 * Represents the UI state for data operations.
 * This sealed class provides a clear contract for different states.
 */
sealed class UiState<out T> {

    /**
     * Initial state - no data has been loaded yet
     */
    data object Idle : UiState<Nothing>()

    /**
     * Loading state - data is being fetched
     */
    data object Loading : UiState<Nothing>()

    /**
     * Success state - data has been successfully loaded
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * Error state - an error occurred while loading data
     */
    data class Error(
        val exception: Throwable,
        val message: String = exception.message ?: "Unknown error"
    ) : UiState<Nothing>()
}

/**
 * Extension functions for easier state handling
 */
val <T> UiState<T>.isLoading: Boolean
    get() = this is UiState.Loading

val <T> UiState<T>.isSuccess: Boolean
    get() = this is UiState.Success

val <T> UiState<T>.isError: Boolean
    get() = this is UiState.Error

val <T> UiState<T>.data: T?
    get() = (this as? UiState.Success)?.data

val <T> UiState<T>.errorMessage: String?
    get() = (this as? UiState.Error)?.message
