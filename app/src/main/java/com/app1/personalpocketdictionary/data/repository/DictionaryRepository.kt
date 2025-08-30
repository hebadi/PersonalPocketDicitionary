package com.app1.personalpocketdictionary.data.repository

import com.app1.personalpocketdictionary.data.DictionaryDao
import com.app1.personalpocketdictionary.data.DictionaryData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that provides a clean API for data access to the rest of the application.
 * This abstracts the data layer and provides a single source of truth for dictionary data.
 */
@Singleton
class DictionaryRepository @Inject constructor(
    private val dictionaryDao: DictionaryDao
) {

    /**
     * Get all dictionary entries as a Flow
     */
    fun getAllWords(): Flow<List<DictionaryData>> = dictionaryDao.getAll()

    /**
     * Get a specific word by ID
     */
    fun getWordById(id: Int): Flow<DictionaryData> = dictionaryDao.getContents(id)

    /**
     * Get list of all words (just the strings)
     */
    fun getWordsList(): Flow<List<String>> = dictionaryDao.getWordsList()

    /**
     * Insert a new word into the dictionary
     */
    suspend fun insertWord(word: DictionaryData) = dictionaryDao.insert(word)

    /**
     * Update an existing word
     */
    suspend fun updateWord(word: DictionaryData) = dictionaryDao.update(word)

    /**
     * Delete a word from the dictionary
     */
    suspend fun deleteWord(word: DictionaryData) = dictionaryDao.delete(word)

    /**
     * Check if a word entry is valid
     */
    fun isWordValid(
        word: String,
        partOfSpeech: String,
        definition: String,
        example: String
    ): Boolean {
        return word.isNotBlank() &&
                partOfSpeech.isNotBlank() &&
                definition.isNotBlank() &&
                example.isNotBlank()
    }
}
