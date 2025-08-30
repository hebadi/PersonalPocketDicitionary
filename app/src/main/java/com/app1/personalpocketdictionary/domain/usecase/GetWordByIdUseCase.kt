package com.app1.personalpocketdictionary.domain.usecase

import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a specific word by ID.
 */
class GetWordByIdUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    operator fun invoke(id: Int): Flow<DictionaryData> = repository.getWordById(id)
}
