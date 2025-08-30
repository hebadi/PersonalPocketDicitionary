package com.app1.personalpocketdictionary.domain.usecase

import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.repository.DictionaryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for AddWordUseCase demonstrating modern testing practices:
 * - Using MockK for mocking
 * - Testing coroutines with runTest
 * - Testing business logic in isolation
 */
class AddWordUseCaseTest {

    private val mockRepository = mockk<DictionaryRepository>()
    private lateinit var useCase: AddWordUseCase

    @Before
    fun setup() {
        useCase = AddWordUseCase(mockRepository)
    }

    @Test
    fun `invoke with valid data should return success`() = runTest {
        // Given
        val word = "Test"
        val partOfSpeech = "Noun"
        val definition = "A test definition"
        val example = "This is a test example"

        every { mockRepository.isWordValid(word, partOfSpeech, definition, example) } returns true
        coEvery { mockRepository.insertWord(any()) } returns Unit

        // When
        val result = useCase(word, partOfSpeech, definition, example)

        // Then
        assertTrue(result.isSuccess)
        coVerify { mockRepository.insertWord(any<DictionaryData>()) }
    }

    @Test
    fun `invoke with invalid data should return failure`() = runTest {
        // Given
        val word = ""
        val partOfSpeech = "Noun"
        val definition = "A test definition"
        val example = "This is a test example"

        every { mockRepository.isWordValid(word, partOfSpeech, definition, example) } returns false

        // When
        val result = useCase(word, partOfSpeech, definition, example)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        coVerify(exactly = 0) { mockRepository.insertWord(any()) }
    }

    @Test
    fun `invoke with repository exception should return failure`() = runTest {
        // Given
        val word = "Test"
        val partOfSpeech = "Noun"
        val definition = "A test definition"
        val example = "This is a test example"
        val exception = RuntimeException("Database error")

        every { mockRepository.isWordValid(word, partOfSpeech, definition, example) } returns true
        coEvery { mockRepository.insertWord(any()) } throws exception

        // When
        val result = useCase(word, partOfSpeech, definition, example)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
