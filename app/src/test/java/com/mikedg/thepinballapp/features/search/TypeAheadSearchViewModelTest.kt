package com.mikedg.thepinballapp.features.search

import app.cash.turbine.test
import com.mikedg.thepinballapp.data.model.opdb.Machine
import com.mikedg.thepinballapp.data.model.opdb.TypeAheadSearchResult
import com.mikedg.thepinballapp.data.remote.OpdbApiService
import com.mikedg.thepinballapp.util.ApiResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TypeAheadSearchViewModelTest {
    private lateinit var viewModel: TypeAheadSearchViewModel
    private lateinit var apiService: OpdbApiService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        apiService = mockk()
        
        // Setup default responses for initialization
        coEvery { apiService.search(any()) } returns ApiResult.Success(emptyList())
        coEvery { apiService.searchTypeAhead(any()) } returns ApiResult.Success(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchQueryChange updates searchQuery state`() = runTest {
        // Given
        val query = "test"
        coEvery { apiService.searchTypeAhead(query) } returns ApiResult.Success(emptyList())
        viewModel = TypeAheadSearchViewModel(apiService)

        // When
        viewModel.onSearchQueryChange(query)
        advanceUntilIdle()

        // Then
        viewModel.searchQuery.test {
            assertEquals(query, awaitItem())
        }
    }

    @Test
    fun `onSearchQueryChange with successful API call updates typeAheadSearchResults`() = runTest {
        // Given
        val query = "test"
        val results = listOf(
            TypeAheadSearchResult("1", "Test Machine 1"),
            TypeAheadSearchResult("2", "Test Machine 2")
        )
        coEvery { apiService.searchTypeAhead(query) } returns ApiResult.Success(results)
        viewModel = TypeAheadSearchViewModel(apiService)

        // When
        viewModel.onSearchQueryChange(query)
        advanceUntilIdle()

        // Then
        viewModel.typeAheadSearchResults.test {
            assertEquals(results, awaitItem())
        }
        
        viewModel.searchError.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `onSearchQueryChange with API error updates searchError state`() = runTest {
        // Given
        val query = "test"
        val errorMessage = "Network error: Connection failed"
        coEvery { apiService.searchTypeAhead(query) } returns ApiResult.Error.NetworkError(IOException("Connection failed"))
        viewModel = TypeAheadSearchViewModel(apiService)

        // When
        viewModel.onSearchQueryChange(query)
        advanceUntilIdle()

        // Then
        viewModel.typeAheadSearchResults.test {
            assertTrue(awaitItem().isEmpty())
        }
        
        viewModel.searchError.test {
            assertEquals(errorMessage, awaitItem())
        }
    }

    @Test
    fun `performSearch with successful API call updates searchResults`() = runTest {
        // Given
        val query = "test"
        val results = listOf(
            Machine(
                opdbId = "1",
                name = "Test Machine 1",
                manufacturer = "Test Manufacturer",
                year = 2020,
                type = "SS",
                theme = null,
                players = 4,
                ipdbId = null,
                opdbUrl = null,
                designedBy = null,
                artBy = null,
                features = null,
                notableFeatures = null,
                toys = null,
                notes = null,
                images = null,
                videos = null,
                ipdbUrl = null,
                pinTipsUrl = null,
                pinRescueUrl = null,
                pinSiderUrl = null,
                tcfpaPodcastUrl = null,
                papa = null,
                pinballForumUrl = null,
                pinballVideosPinballYouTubePlaylistUrl = null,
                matchPlayPinballYouTubePlaylistUrl = null,
                deadFlipYouTubePlaylistUrl = null,
                pinballShowdownYouTubePlaylistUrl = null,
                aliases = null,
                grouping = null,
                groupingEntries = null
            )
        )
        coEvery { apiService.search(query) } returns ApiResult.Success(results)
        viewModel = TypeAheadSearchViewModel(apiService)

        // When
        viewModel.performSearch(query)
        advanceUntilIdle()

        // Then
        viewModel.searchResults.test {
            assertEquals(results, awaitItem())
        }
        
        viewModel.searchError.test {
            assertNull(awaitItem())
        }
        
        viewModel.isLoading.test {
            assertFalse(awaitItem())
        }
    }

    @Test
    fun `performSearch with API error updates searchError state`() = runTest {
        // Given
        val query = "test"
        val errorMessage = "Resource not found"
        coEvery { apiService.search(query) } returns ApiResult.Error.HttpError(404, "Not Found")
        viewModel = TypeAheadSearchViewModel(apiService)

        // When
        viewModel.performSearch(query)
        advanceUntilIdle()

        // Then
        viewModel.searchResults.test {
            assertTrue(awaitItem().isEmpty())
        }
        
        viewModel.searchError.test {
            assertEquals(errorMessage, awaitItem())
        }
        
        viewModel.isLoading.test {
            assertFalse(awaitItem())
        }
    }

    @Test
    fun `clearError sets searchError to null`() = runTest {
        // Given
        val query = "test"
        coEvery { apiService.search(query) } returns ApiResult.Error.HttpError(404, "Not Found")
        viewModel = TypeAheadSearchViewModel(apiService)
        viewModel.performSearch(query)
        advanceUntilIdle()
        
        // Verify error is set
        viewModel.searchError.test {
            assertEquals("Resource not found", awaitItem())
        }

        // When
        viewModel.clearError()

        // Then
        viewModel.searchError.test {
            assertNull(awaitItem())
        }
    }
}