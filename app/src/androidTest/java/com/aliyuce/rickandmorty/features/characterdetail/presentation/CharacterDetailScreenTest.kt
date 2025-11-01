package com.aliyuce.rickandmorty.features.characterdetail.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.aliyuce.rickandmorty.R
import com.aliyuce.rickandmorty.domain.model.Character
import com.aliyuce.rickandmorty.ui.theme.RickAndMortyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState

@RunWith(AndroidJUnit4::class)
class CharacterDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            RickAndMortyTheme {
                CharacterDetailContent(
                    innerPadding = PaddingValues(0.dp),
                    listState = rememberLazyListState(),
                    uiState = CharacterDetailUiState.Loading,
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("character_detail_loading_indicator").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorAndRetry() {
        val err = Throwable("Network failed")
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            RickAndMortyTheme {
                CharacterDetailContent(
                    innerPadding = PaddingValues(0.dp),
                    listState = rememberLazyListState(),
                    uiState = CharacterDetailUiState.Error(err),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Network failed").assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.retry)).assertIsDisplayed()
    }

    @Test
    fun successState_showsCharacterInfoAndEpisodes() {
        val character = Character.fake

        composeTestRule.setContent {
            RickAndMortyTheme {
                CharacterDetailContent(
                    innerPadding = PaddingValues(0.dp),
                    listState = rememberLazyListState(),
                    uiState = CharacterDetailUiState.Success(character = character, lastRefreshed = null),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNodeWithText("Alive • Human").assertIsDisplayed()
        composeTestRule.onNodeWithText("Origin: Earth").assertIsDisplayed()
        composeTestRule.onNodeWithText("S01E01").assertIsDisplayed()
        composeTestRule.onNodeWithText("S01E02").assertIsDisplayed()
    }
}
