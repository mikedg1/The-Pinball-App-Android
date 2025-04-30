package com.mikedg.thepinballapp.features.machinedetail

import app.cash.turbine.test
import com.mikedg.thepinballapp.data.model.opdb.Machine
import com.mikedg.thepinballapp.data.remote.OpdbApiService
import com.mikedg.thepinballapp.util.ApiResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MachineDetailViewModelTest {
    private lateinit var viewModel: MachineDetailViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val mockOpdbService = mockk<OpdbApiService>()
    private val testOpdbId = "test-id"
    private val mockMachine = Machine(
        opdbId = testOpdbId,
        name = "Test Machine",
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

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock the OpdbApiService constructor to return our mock instance
        mockkConstructor(OpdbApiService::class)
        every { anyConstructed<OpdbApiService>().fetchMachine(any(), any()) } returns ApiResult.Success(mockMachine)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        // Given
        every { anyConstructed<OpdbApiService>().fetchMachine(any(), any()) } returns ApiResult.Success(mockMachine)
        
        // When
        viewModel = MachineDetailViewModel(testOpdbId)
        
        // Then
        viewModel.uiState.test {
            assertEquals(MachineDetailViewModel.UiState.Loading, awaitItem())
        }
    }

    @Test
    fun `successful data fetch updates state to Content`() = runTest {
        // Given
        every { anyConstructed<OpdbApiService>().fetchMachine(any(), any()) } returns ApiResult.Success(mockMachine)
        
        // When
        viewModel = MachineDetailViewModel(testOpdbId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        viewModel.uiState.test {
            assertEquals(MachineDetailViewModel.UiState.Content(mockMachine), awaitItem())
        }
    }

    @Test
    fun `http error during fetch updates state to Error`() = runTest {
        // Given
        val errorMessage = "Resource not found"
        every { anyConstructed<OpdbApiService>().fetchMachine(any(), any()) } returns 
            ApiResult.Error.HttpError(404, "Not Found")
        
        // When
        viewModel = MachineDetailViewModel(testOpdbId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        viewModel.uiState.test {
            assertEquals(MachineDetailViewModel.UiState.Error(errorMessage), awaitItem())
        }
    }

    @Test
    fun `retry function reloads data and updates state accordingly`() = runTest {
        // Given
        every { anyConstructed<OpdbApiService>().fetchMachine(any(), any()) } returns
            ApiResult.Error.HttpError(429, "Too Many Requests") andThen ApiResult.Success(mockMachine)
        
        // When
        viewModel = MachineDetailViewModel(testOpdbId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then - First verify error state
        viewModel.uiState.test {
            assertEquals(MachineDetailViewModel.UiState.Error("Too many requests. Please try again later."), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        // When - Retry
        viewModel.retry(testOpdbId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then - Verify success state after retry
        viewModel.uiState.test {
            assertEquals(MachineDetailViewModel.UiState.Content(mockMachine), awaitItem())
        }
    }
}