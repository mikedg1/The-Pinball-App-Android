package com.mikedg.thepinballapp.features.changelog

import app.cash.turbine.test
import com.mikedg.thepinballapp.data.model.opdb.ChangeLog
import com.mikedg.thepinballapp.data.remote.OpdbApiService
import com.mikedg.thepinballapp.util.ApiResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ChangeLogViewModelTest {
    private lateinit var viewModel: ChangeLogViewModel
    private lateinit var apiService: OpdbApiService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        apiService = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        coEvery { apiService.fetchChangeLogs() } returns ApiResult.Success(MOCK_CHANGELOGS)
        viewModel = ChangeLogViewModel(apiService)

        viewModel.uiState.test {
            assertEquals(ChangeLogViewModel.UiState.Loading, awaitItem())
        }
    }

    @Test
    fun `successful data fetch updates state to Content`() {
        runTest {
            coEvery { apiService.fetchChangeLogs() } returns ApiResult.Success(MOCK_CHANGELOGS)

            viewModel = ChangeLogViewModel(apiService)

            // Then
            viewModel.uiState.test {
                assertEquals(ChangeLogViewModel.UiState.Loading, awaitItem())
                assertEquals(ChangeLogViewModel.UiState.Content(MOCK_CHANGELOGS), awaitItem())
            }
        }
    }

    @Test
    fun `network error during fetch updates state to Error`() = runTest {
        val networkException = IOException("Network error")
        coEvery { apiService.fetchChangeLogs() } returns ApiResult.Error.NetworkError(networkException)

        viewModel = ChangeLogViewModel(apiService)

        viewModel.uiState.test {
            assertEquals(ChangeLogViewModel.UiState.Loading, awaitItem())
            assertEquals(ChangeLogViewModel.UiState.Error("Network error: Network error"), awaitItem())
        }
    }

    @Test
    fun `http error during fetch updates state to Error`() = runTest {
        coEvery { apiService.fetchChangeLogs() } returns ApiResult.Error.HttpError(404, "Not Found")

        viewModel = ChangeLogViewModel(apiService)

        viewModel.uiState.test {
            assertEquals(ChangeLogViewModel.UiState.Loading, awaitItem())
            assertEquals(ChangeLogViewModel.UiState.Error("Resource not found"), awaitItem())
        }
    }

    @Test
    fun `retry function reloads data and updates state accordingly`() = runTest {
        coEvery {
            apiService.fetchChangeLogs()
        } returns ApiResult.Error.HttpError(500, "Server Error") andThen ApiResult.Success(MOCK_CHANGELOGS)

        viewModel = ChangeLogViewModel(apiService)

        viewModel.uiState.test {
            assertEquals(ChangeLogViewModel.UiState.Loading, awaitItem())
            assertEquals(ChangeLogViewModel.UiState.Error("Server error. Please try again later."), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        // When retrying
        viewModel.retry()

        // Then
        viewModel.uiState.test {
            assertEquals(ChangeLogViewModel.UiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertEquals(ChangeLogViewModel.UiState.Content(MOCK_CHANGELOGS), awaitItem())
        }
    }

    private companion object {
        val TEST_CHANGELOG = ChangeLog(
            changelogId = 1,
            opdbIdDeleted = "GrdNZ-MQo1e",
            action = "move",
            opdbIdReplacement = "GRveZ-MNE38",
            createdAt = "2018-10-19T15:06:20.000000Z",
            updatedAt = "2018-10-19T15:06:20.000000Z"
        )
        val MOCK_CHANGELOGS = listOf(TEST_CHANGELOG)
    }
}