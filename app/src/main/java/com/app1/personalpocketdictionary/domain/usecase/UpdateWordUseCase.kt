package com.app1.personalpocketdictionary.domain.usecase

import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.repository.DictionaryRepository
import javax.inject.Inject

/**
 * Use case for updating an existing word in the dictionary.
 */
class UpdateWordUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {

    suspend operator fun invoke(
        id: Int,
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
                id = id,
                word = word.trim(),
                partOfSpeech = partOfSpeech.trim(),
                definition = definition.trim(),
                example = example.trim()
            )

            repository.updateWord(dictionaryData)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
