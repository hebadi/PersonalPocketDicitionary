package com.app1.personalpocketdictionary.domain.usecase

import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.repository.DictionaryRepository
import javax.inject.Inject

/**
 * Enhanced Use Case demonstrating advanced patterns:
 * - Multiple validation rules
 * - Business logic composition
 * - Detailed error types
 * - Logging and analytics
 */
class EnhancedAddWordUseCase @Inject constructor(
    private val repository: DictionaryRepository,
    private val validateWordUseCase: ValidateWordUseCase,
    private val checkDuplicateUseCase: CheckDuplicateUseCase,
    private val analyticsUseCase: LogAnalyticsUseCase
) {

    sealed class AddWordError : Throwable() {
        object EmptyFields : AddWordError()
        object InvalidCharacters : AddWordError()
        object TooShort : AddWordError()
        object AlreadyExists : AddWordError()
        data class DatabaseError(override val cause: Throwable) : AddWordError()
    }

    suspend operator fun invoke(
        word: String,
        partOfSpeech: String,
        definition: String,
        example: String
    ): Result<Unit> {
        return try {
            // Step 1: Input validation
            validateWordUseCase(word, partOfSpeech, definition, example)
                .onFailure { return Result.failure(it) }

            // Step 2: Business rules
            val cleanedWord = word.trim().lowercase()

            checkDuplicateUseCase(cleanedWord)
                .onFailure { return Result.failure(AddWordError.AlreadyExists) }

            // Step 3: Create and store
            val dictionaryData = DictionaryData(
                word = cleanedWord,
                partOfSpeech = partOfSpeech.trim(),
                definition = definition.trim(),
                example = example.trim()
            )

            repository.insertWord(dictionaryData)

            // Step 4: Analytics/Side effects
            analyticsUseCase.logWordAdded(cleanedWord, partOfSpeech)

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(AddWordError.DatabaseError(e))
        }
    }
}

// Supporting Use Cases for composition
class ValidateWordUseCase @Inject constructor() {
    operator fun invoke(word: String, pos: String, def: String, ex: String): Result<Unit> {
        return when {
            listOf(word, pos, def, ex).any { it.isBlank() } ->
                Result.failure(EnhancedAddWordUseCase.AddWordError.EmptyFields)

            word.length < 2 ->
                Result.failure(EnhancedAddWordUseCase.AddWordError.TooShort)

            word.any { !it.isLetter() && it != ' ' && it != '-' } ->
                Result.failure(EnhancedAddWordUseCase.AddWordError.InvalidCharacters)

            else -> Result.success(Unit)
        }
    }
}

class CheckDuplicateUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    suspend operator fun invoke(word: String): Result<Unit> {
        // Check if word already exists (this would need repository method)
        return Result.success(Unit) // Simplified
    }
}

class LogAnalyticsUseCase @Inject constructor() {
    suspend fun logWordAdded(word: String, partOfSpeech: String) {
        // Log to analytics service
    }
}
