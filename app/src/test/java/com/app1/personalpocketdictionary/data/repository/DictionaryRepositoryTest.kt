package com.app1.personalpocketdictionary.data.repository

import com.app1.personalpocketdictionary.data.DictionaryDao
import com.app1.personalpocketdictionary.data.DictionaryData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for DictionaryRepository demonstrating:
 * - Repository pattern testing
 * - DAO mocking
 * - Flow testing
 * - Data validation testing
 */
class DictionaryRepositoryTest {

    private val mockDao = mockk<DictionaryDao>()
    private lateinit var repository: DictionaryRepository

    @Before
    fun setup() {
        repository = DictionaryRepository(mockDao)
    }

    @Test
    fun `getAllWords should return words from DAO`() = runTest {
        // Given
        val testWords = listOf(
            DictionaryData(1, "Test", "Noun", "Test definition", "Test example")
        )
        every { mockDao.getAll() } returns flowOf(testWords)

        // When
        val result = repository.getAllWords().first()

        // Then
        assertEquals(testWords, result)
    }

    @Test
    fun `insertWord should call DAO insert`() = runTest {
        // Given
        val testWord = DictionaryData(1, "Test", "Noun", "Test definition", "Test example")
        coEvery { mockDao.insert(testWord) } returns Unit

        // When
        repository.insertWord(testWord)

        // Then
        coVerify { mockDao.insert(testWord) }
    }

    @Test
    fun `isWordValid should return true for valid data`() {
        // Given
        val word = "Test"
        val partOfSpeech = "Noun"
        val definition = "Test definition"
        val example = "Test example"

        // When
        val result = repository.isWordValid(word, partOfSpeech, definition, example)

        // Then
        assertTrue(result)
    }

    @Test
    fun `isWordValid should return false for empty word`() {
        // Given
        val word = ""
        val partOfSpeech = "Noun"
        val definition = "Test definition"
        val example = "Test example"

        // When
        val result = repository.isWordValid(word, partOfSpeech, definition, example)

        // Then
        assertFalse(result)
    }

    @Test
    fun `isWordValid should return false for blank definition`() {
        // Given
        val word = "Test"
        val partOfSpeech = "Noun"
        val definition = "   "
        val example = "Test example"

        // When
        val result = repository.isWordValid(word, partOfSpeech, definition, example)

        // Then
        assertFalse(result)
    }
}
