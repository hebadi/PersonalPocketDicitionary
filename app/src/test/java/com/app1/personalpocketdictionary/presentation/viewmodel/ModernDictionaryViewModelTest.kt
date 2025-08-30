package com.app1.personalpocketdictionary.presentation.viewmodel

import app.cash.turbine.test
import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.domain.usecase.*
import com.app1.personalpocketdictionary.presentation.state.UiState
import com.app1.personalpocketdictionary.presentation.state.data
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for ModernDictionaryViewModel demonstrating:
 * - ViewModel testing with coroutines
 * - StateFlow testing with Turbine
 * - Mocking use cases
 * - Testing UI state changes
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ModernDictionaryViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val mockGetAllWordsUseCase = mockk<GetAllWordsUseCase>()
    private val mockGetWordByIdUseCase = mockk<GetWordByIdUseCase>()
    private val mockAddWordUseCase = mockk<AddWordUseCase>()
    private val mockUpdateWordUseCase = mockk<UpdateWordUseCase>()
    private val mockDeleteWordUseCase = mockk<DeleteWordUseCase>()

    private lateinit var viewModel: ModernDictionaryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel initialization should load words successfully`() = runTest {
        // Given
        val testWords = listOf(
            DictionaryData(1, "Test", "Noun", "Test definition", "Test example")
        )
        every { mockGetAllWordsUseCase() } returns flowOf(testWords)

        // When
        viewModel = ModernDictionaryViewModel(
            mockGetAllWordsUseCase,
            mockGetWordByIdUseCase,
            mockAddWordUseCase,
            mockUpdateWordUseCase,
            mockDeleteWordUseCase
        )

        // Then
        viewModel.uiState.test {
            val emission = awaitItem()
            assertTrue(emission.words is UiState.Success)
            assertEquals(testWords, emission.words.data)
            assertEquals(testWords, emission.filteredWords)
        }
    }

    @Test
    fun `searchWords should filter words correctly`() = runTest {
        // Given
        val testWords = listOf(
            DictionaryData(1, "Apple", "Noun", "A fruit", "I ate an apple"),
            DictionaryData(2, "Book", "Noun", "Reading material", "I read a book")
        )
        every { mockGetAllWordsUseCase() } returns flowOf(testWords)

        viewModel = ModernDictionaryViewModel(
            mockGetAllWordsUseCase,
            mockGetWordByIdUseCase,
            mockAddWordUseCase,
            mockUpdateWordUseCase,
            mockDeleteWordUseCase
        )

        // When
        viewModel.searchWords("Apple")

        // Then
        viewModel.uiState.test {
            val emission = awaitItem()
            assertEquals("Apple", emission.searchQuery)
            assertEquals(1, emission.filteredWords.size)
            assertEquals("Apple", emission.filteredWords.first().word)
        }
    }
}
