package com.app1.personalpocketdictionary.domain.usecase

import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.repository.DictionaryRepository
import javax.inject.Inject

/**
 * Use case for deleting a word from the dictionary.
 */
class DeleteWordUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {

    suspend operator fun invoke(word: DictionaryData): Result<Unit> {
        return try {
            repository.deleteWord(word)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
