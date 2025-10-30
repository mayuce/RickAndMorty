package com.aliyuce.rickandmorty.features.episodes.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.aliyuce.rickandmorty.domain.model.Episode
import com.aliyuce.rickandmorty.ui.theme.RickAndMortyTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A small item that displays an episode name, its air date (dd/MM/yyyy) and the episode code.
 * The component is clickable and returns the episode id as string via [onClick].
 */
@Composable
fun EpisodeItem(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
) {
    val formattedDate = formatAirDate(episode.airDate)

    Column(modifier = modifier) {
        Text(
            text = episode.name,
            style =
                MaterialTheme
                    .typography
                    .titleMedium
                    .copy(color = MaterialTheme.colorScheme.primary),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$formattedDate • ${episode.episode}",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private fun formatAirDate(input: String): String {
    val outputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return runCatching {
        val parser = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        parser.isLenient = false
        val date: Date = parser.parse(input)
        outputFormatter.format(date)
    }.getOrElse {
        input
    }
}

@Preview(showBackground = true)
@Composable
private fun EpisodeItemPreview() {
    RickAndMortyTheme {
        EpisodeItem(
            episode =
                Episode(
                    id = 1,
                    name = "Pilot",
                    airDate = "December 2, 2013",
                    episode = "S01E01",
                ),
            onClick = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.NONE,
)
@Composable
private fun EpisodeItemDarkPreview() {
    RickAndMortyTheme {
        EpisodeItem(
            episode =
                Episode(
                    id = 1,
                    name = "Pilot",
                    airDate = "December 2, 2013",
                    episode = "S01E01",
                ),
            onClick = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        )
    }
}
