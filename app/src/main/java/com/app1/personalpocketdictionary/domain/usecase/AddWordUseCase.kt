package com.app1.personalpocketdictionary.domain.usecase

import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.repository.DictionaryRepository
import javax.inject.Inject

/**
 * Use case for adding a new word to the dictionary.
 * Contains validation logic and business rules.
 */
class AddWordUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {

    suspend operator fun invoke(
        word: String,
        partOfSpeech: String,
        definition: String,
        example: String
    ): Result<Unit> {
        return try {
            // Validate input
            if (!repository.isWordValid(word, partOfSpeech, definition, example)) {
                return Result.failure(IllegalArgumentException("All fields must be filled"))
            }

            val dictionaryData = DictionaryData(
                word = word.trim(),
                partOfSpeech = partOfSpeech.trim(),
                definition = definition.trim(),
                example = example.trim()
            )

            repository.insertWord(dictionaryData)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
