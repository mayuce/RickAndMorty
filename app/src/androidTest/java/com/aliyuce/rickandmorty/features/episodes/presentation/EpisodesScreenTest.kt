package com.aliyuce.rickandmorty.features.episodes.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.aliyuce.rickandmorty.R
import com.aliyuce.rickandmorty.domain.model.Episode
import com.aliyuce.rickandmorty.ui.theme.RickAndMortyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EpisodesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            RickAndMortyTheme {
                Episodes(
                    uiState = EpisodesUiState.Loading,
                    onCharacterClick = {},
                    onLoadMore = {}
                )
            }
        }

        // The loading state shows a CircularProgressIndicator; check for existence by tag
        composeTestRule.onNodeWithTag("episodes_loading_indicator").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorAndRetry() {
        val err = Throwable("Network failed")
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            RickAndMortyTheme {
                Episodes(
                    uiState = EpisodesUiState.Error(err),
                    onCharacterClick = {},
                    onLoadMore = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Network failed").assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.retry)).assertIsDisplayed()
    }

    @Test
    fun successState_showsEpisodeItems_and_openCharactersSheet() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val episodes = Episode.sampleList()

        composeTestRule.setContent {
            RickAndMortyTheme {
                Episodes(
                    uiState = EpisodesUiState.Success(
                        episodes = episodes,
                        page = 1,
                        totalPages = 1,
                        isLoadingMore = false,
                        isRefreshing = false,
                        lastRefreshed = null,
                        error = null
                    ),
                    onCharacterClick = {},
                    onLoadMore = {}
                )
            }
        }

        // Verify items are displayed
        composeTestRule.onNodeWithText("Pilot").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lawnmower Dog").assertIsDisplayed()

        // Click the first item to open sheet
        composeTestRule.onNodeWithText("Pilot").performClick()

        // The characters sheet title should appear
        composeTestRule.onNodeWithText(context.getString(R.string.characters_sheet_title)).assertIsDisplayed()

        // Character id should be shown
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }
}


