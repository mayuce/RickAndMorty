package com.aliyuce.rickandmorty.features.episodes.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import com.aliyuce.rickandmorty.features.episodes.domain.GetEpisodesUseCase
import com.aliyuce.rickandmorty.features.episodes.domain.GetEpisodesLastRefreshedUseCase
import com.aliyuce.rickandmorty.domain.model.Episode
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodesViewModelTest {

    private lateinit var getEpisodesUseCase: GetEpisodesUseCase
    private lateinit var getEpisodesLastRefreshedUseCase: GetEpisodesLastRefreshedUseCase
    private lateinit var vm: EpisodesViewModel

    // shared test dispatcher so we can control coroutine timing from tests
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        getEpisodesUseCase = mockk()
        getEpisodesLastRefreshedUseCase = mockk()
        // Do NOT create the ViewModel here. Tests will create it after configuring stubs so
        // the initial load in init() doesn't run before coEvery is configured.
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load success updates uiState to Success with episodes`() = runTest {
        val episodes = listOf(Episode.fake1)
        val page = Episode.pageOf(Episode.fake1)

        coEvery { getEpisodesUseCase(1) } returns Result.success(page)
        coEvery { getEpisodesLastRefreshedUseCase(1) } returns 12345L
        
        vm = EpisodesViewModel(getEpisodesUseCase, getEpisodesLastRefreshedUseCase)

        // advance until coroutines complete (runs init load)
        advanceUntilIdle()

        val state = vm.uiState.value
        require(state is EpisodesUiState.Success)
        assertEquals(episodes, state.episodes)
        assertEquals(1, state.page)
        assertEquals(1, state.totalPages)
        assertEquals(false, state.isRefreshing)
        assertEquals(false, state.isLoadingMore)
        assertEquals(12345L, state.lastRefreshed)

        coVerify(exactly = 1) { getEpisodesUseCase(1) }
        coVerify(exactly = 1) { getEpisodesLastRefreshedUseCase(1) }
    }

    @Test
    fun `load more appends episodes to existing list`() = runTest {
        val page1 = Episode.pageOf(Episode.fake1, count = 2, pages = 2)
        val page2 = Episode.pageOf(Episode.fake2, count = 2, pages = 2)

        coEvery { getEpisodesUseCase(1) } returns Result.success(page1)
        coEvery { getEpisodesLastRefreshedUseCase(1) } returns null

        coEvery { getEpisodesUseCase(2) } returns Result.success(page2)
        coEvery { getEpisodesLastRefreshedUseCase(2) } returns 999L

        vm = EpisodesViewModel(getEpisodesUseCase, getEpisodesLastRefreshedUseCase)

        // run initial load (page 1)
        advanceUntilIdle()

        // now load page 2
        vm.loadEpisodes(page = 2)
        advanceUntilIdle()

        val state = vm.uiState.value
        require(state is EpisodesUiState.Success)
        assertEquals(listOf(Episode.fake1, Episode.fake2), state.episodes)
        assertEquals(2, state.page)
        assertEquals(2, state.totalPages)
        assertEquals(999L, state.lastRefreshed)

        coVerify(exactly = 1) { getEpisodesUseCase(1) }
        coVerify(exactly = 1) { getEpisodesUseCase(2) }
    }

    @Test
    fun `error during load sets Error state when initial load`() = runTest {
        val error = RuntimeException("network")
        coEvery { getEpisodesUseCase(1) } returns Result.failure(error)

        vm = EpisodesViewModel(getEpisodesUseCase, getEpisodesLastRefreshedUseCase)

        // run initial load
        advanceUntilIdle()

        val state = vm.uiState.value
        require(state is EpisodesUiState.Error)
        assertEquals(error, state.throwable)

        coVerify(exactly = 1) { getEpisodesUseCase(1) }
    }

    @Test
    fun `error during refresh preserves existing episodes and sets error inside Success`() = runTest {
        val page1 = Episode.pageOf(Episode.fake1, count = 1, pages = 1)

        coEvery { getEpisodesUseCase(1) } returnsMany listOf(Result.success(page1), Result.failure(RuntimeException("refresh")))
        coEvery { getEpisodesLastRefreshedUseCase(1) } returns 111L

        vm = EpisodesViewModel(getEpisodesUseCase, getEpisodesLastRefreshedUseCase)
        advanceUntilIdle()

        // Trigger refresh - page 1 again
        vm.loadEpisodes(page = 1)
        advanceUntilIdle()

        val state = vm.uiState.value
        require(state is EpisodesUiState.Success)
        assertEquals(listOf(Episode.fake1), state.episodes)
        // error should be set inside Success
        assert(state.error != null)

        coVerify(exactly = 2) { getEpisodesUseCase(1) }
    }
}
