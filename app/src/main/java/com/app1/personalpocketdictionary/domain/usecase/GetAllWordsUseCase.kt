package com.app1.personalpocketdictionary.domain.usecase

import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all words from the dictionary.
 * Encapsulates the business logic for fetching dictionary data.
 */
class GetAllWordsUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    operator fun invoke(): Flow<List<DictionaryData>> = repository.getAllWords()
}
